package com.ryan.ddd.adapter.demo;

import com.ryan.ddd.app.workflow.WorkflowContext;
import com.ryan.ddd.app.workflow.WorkflowDefinition;
import com.ryan.ddd.app.workflow.WorkflowEngine;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/workflows")
public class WorkflowController {
  private final WorkflowEngine workflowEngine;
  private final WorkflowDefinition demoWorkflow;

  public WorkflowController(WorkflowEngine workflowEngine, WorkflowDefinition demoWorkflow) {
    this.workflowEngine = workflowEngine;
    this.demoWorkflow = demoWorkflow;
  }

  @PostMapping("/sync")
  public ResponseEntity<String> runSync(@Valid @RequestBody WorkflowRequest request) {
    WorkflowContext context = new WorkflowContext();
    context.put("workflowId", request.getWorkflowId());
    String executionId = workflowEngine.startSync(demoWorkflow, context);
    return ResponseEntity.ok(executionId);
  }

  @PostMapping("/async")
  public ResponseEntity<String> runAsync(@Valid @RequestBody WorkflowRequest request) {
    WorkflowContext context = new WorkflowContext();
    context.put("workflowId", request.getWorkflowId());
    String executionId = workflowEngine.startAsync(demoWorkflow, context);
    return ResponseEntity.ok(executionId);
  }
}
