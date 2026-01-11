package com.ryan.ddd.app.workflow;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowEngine {
  private static final Logger logger = LoggerFactory.getLogger(WorkflowEngine.class);

  private final WorkflowStore store;
  private final RetryPolicy retryPolicy;
  private final Clock clock;
  private final Executor executor;

  public WorkflowEngine(WorkflowStore store, RetryPolicy retryPolicy, Clock clock,
      Executor executor) {
    this.store = Objects.requireNonNull(store, "store");
    this.retryPolicy = Objects.requireNonNull(retryPolicy, "retryPolicy");
    this.clock = Objects.requireNonNull(clock, "clock");
    this.executor = Objects.requireNonNull(executor, "executor");
  }

  public String startSync(WorkflowDefinition definition, WorkflowContext context) {
    return execute(definition, context);
  }

  public String startAsync(WorkflowDefinition definition, WorkflowContext context) {
    String executionId = UUID.randomUUID().toString();
    WorkflowContext snapshot = new WorkflowContext();
    context.snapshot().forEach(snapshot::put);
    executor.execute(() -> executeWithId(executionId, definition, snapshot));
    return executionId;
  }

  private String execute(WorkflowDefinition definition, WorkflowContext context) {
    String executionId = UUID.randomUUID().toString();
    return executeWithId(executionId, definition, context);
  }

  private String executeWithId(String executionId, WorkflowDefinition definition,
      WorkflowContext context) {
    Map<String, WorkflowNodeStatus> nodeStatuses = new LinkedHashMap<>();
    definition.nodes().keySet().forEach(id -> nodeStatuses.put(id, WorkflowNodeStatus.PENDING));
    WorkflowExecutionRecord record = new WorkflowExecutionRecord(executionId, definition.name(),
        WorkflowExecutionStatus.RUNNING, Instant.now(clock), null, nodeStatuses);
    store.save(record);

    List<String> order = topologicalSort(definition);
    Map<String, Integer> retryCounts = new HashMap<>();
    for (String nodeId : order) {
      WorkflowNode node = definition.nodes().get(nodeId);
      if (node == null) {
        continue;
      }
      nodeStatuses.put(nodeId, WorkflowNodeStatus.RUNNING);
      store.update(record.withStatus(WorkflowExecutionStatus.RUNNING, null, nodeStatuses));

      boolean success = executeWithRetry(node, context, retryCounts);
      if (!success) {
        nodeStatuses.put(nodeId, WorkflowNodeStatus.FAILED);
        record = record.withStatus(WorkflowExecutionStatus.FAILED, Instant.now(clock),
            nodeStatuses);
        store.update(record);
        logger.warn("Workflow {} failed at node {}", definition.name(), nodeId);
        return executionId;
      }
      nodeStatuses.put(nodeId, WorkflowNodeStatus.SUCCESS);
      record = record.withStatus(WorkflowExecutionStatus.RUNNING, null, nodeStatuses);
      store.update(record);
    }

    record = record.withStatus(WorkflowExecutionStatus.SUCCESS, Instant.now(clock), nodeStatuses);
    store.update(record);
    return executionId;
  }

  private boolean executeWithRetry(WorkflowNode node, WorkflowContext context,
      Map<String, Integer> retryCounts) {
    int attempt = retryCounts.getOrDefault(node.id(), 0);
    while (attempt <= retryPolicy.maxRetries()) {
      try {
        NodeResult result = node.handler().execute(context);
        if (result.success()) {
          return true;
        }
        logger.warn("Node {} failed: {}", node.name(), result.message());
      } catch (Exception ex) {
        logger.warn("Node {} threw exception", node.name(), ex);
      }
      attempt++;
      retryCounts.put(node.id(), attempt);
      sleep(retryPolicy.backoff().multipliedBy(attempt));
    }
    return false;
  }

  private void sleep(java.time.Duration delay) {
    if (delay == null || delay.isZero() || delay.isNegative()) {
      return;
    }
    try {
      Thread.sleep(delay.toMillis());
    } catch (InterruptedException ignored) {
      Thread.currentThread().interrupt();
    }
  }

  private List<String> topologicalSort(WorkflowDefinition definition) {
    Map<String, Set<String>> deps = definition.dependencies();
    Map<String, Integer> inDegree = new HashMap<>();
    definition.nodes().keySet().forEach(id -> inDegree.put(id, 0));
    deps.forEach((node, dependsOn) -> inDegree.put(node, dependsOn.size()));

    Queue<String> queue = new ArrayDeque<>();
    inDegree.forEach((node, degree) -> {
      if (degree == 0) {
        queue.add(node);
      }
    });

    List<String> order = new ArrayList<>();
    while (!queue.isEmpty()) {
      String node = queue.poll();
      order.add(node);
      deps.forEach((target, dependsOn) -> {
        if (dependsOn.contains(node)) {
          int degree = inDegree.get(target) - 1;
          inDegree.put(target, degree);
          if (degree == 0) {
            queue.add(target);
          }
        }
      });
    }
    return order;
  }
}
