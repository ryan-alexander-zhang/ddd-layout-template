package com.ryan.ddd.app.workflow;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class WorkflowExecutionRecord {
  private final String executionId;
  private final String workflowName;
  private final WorkflowExecutionStatus status;
  private final Instant startedAt;
  private final Instant finishedAt;
  private final Map<String, WorkflowNodeStatus> nodeStatuses;

  public WorkflowExecutionRecord(String executionId, String workflowName,
      WorkflowExecutionStatus status, Instant startedAt, Instant finishedAt,
      Map<String, WorkflowNodeStatus> nodeStatuses) {
    this.executionId = Objects.requireNonNull(executionId, "executionId");
    this.workflowName = Objects.requireNonNull(workflowName, "workflowName");
    this.status = Objects.requireNonNull(status, "status");
    this.startedAt = Objects.requireNonNull(startedAt, "startedAt");
    this.finishedAt = finishedAt;
    this.nodeStatuses = new LinkedHashMap<>(nodeStatuses);
  }

  public String executionId() {
    return executionId;
  }

  public String workflowName() {
    return workflowName;
  }

  public WorkflowExecutionStatus status() {
    return status;
  }

  public Instant startedAt() {
    return startedAt;
  }

  public Instant finishedAt() {
    return finishedAt;
  }

  public Map<String, WorkflowNodeStatus> nodeStatuses() {
    return Collections.unmodifiableMap(nodeStatuses);
  }

  public WorkflowExecutionRecord withStatus(WorkflowExecutionStatus newStatus, Instant finishedAt,
      Map<String, WorkflowNodeStatus> newNodeStatuses) {
    return new WorkflowExecutionRecord(executionId, workflowName, newStatus, startedAt,
        finishedAt, newNodeStatuses);
  }
}
