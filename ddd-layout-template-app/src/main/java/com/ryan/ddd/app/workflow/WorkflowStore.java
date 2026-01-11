package com.ryan.ddd.app.workflow;

import java.util.Optional;

public interface WorkflowStore {
  void save(WorkflowExecutionRecord record);

  Optional<WorkflowExecutionRecord> find(String executionId);

  void update(WorkflowExecutionRecord record);
}
