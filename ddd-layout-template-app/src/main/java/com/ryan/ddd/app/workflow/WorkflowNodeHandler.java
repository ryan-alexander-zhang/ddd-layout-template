package com.ryan.ddd.app.workflow;

public interface WorkflowNodeHandler {
  NodeResult execute(WorkflowContext context) throws Exception;
}
