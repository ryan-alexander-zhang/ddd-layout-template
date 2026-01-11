package com.ryan.ddd.adapter.demo;

import jakarta.validation.constraints.NotBlank;

public class WorkflowRequest {
  @NotBlank
  private String workflowId;

  public String getWorkflowId() {
    return workflowId;
  }

  public void setWorkflowId(String workflowId) {
    this.workflowId = workflowId;
  }
}
