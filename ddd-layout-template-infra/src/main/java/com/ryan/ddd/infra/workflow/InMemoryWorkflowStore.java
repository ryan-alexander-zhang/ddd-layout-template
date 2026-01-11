package com.ryan.ddd.infra.workflow;

import com.ryan.ddd.app.workflow.WorkflowExecutionRecord;
import com.ryan.ddd.app.workflow.WorkflowStore;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryWorkflowStore implements WorkflowStore {
  private final ConcurrentHashMap<String, WorkflowExecutionRecord> store = new ConcurrentHashMap<>();

  @Override
  public void save(WorkflowExecutionRecord record) {
    store.put(record.executionId(), record);
  }

  @Override
  public Optional<WorkflowExecutionRecord> find(String executionId) {
    return Optional.ofNullable(store.get(executionId));
  }

  @Override
  public void update(WorkflowExecutionRecord record) {
    store.put(record.executionId(), record);
  }
}
