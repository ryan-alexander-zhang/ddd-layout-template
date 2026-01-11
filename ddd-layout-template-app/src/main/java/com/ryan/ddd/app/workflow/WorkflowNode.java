package com.ryan.ddd.app.workflow;

import java.util.Objects;

public final class WorkflowNode {
  private final String id;
  private final String name;
  private final WorkflowNodeHandler handler;

  public WorkflowNode(String id, String name, WorkflowNodeHandler handler) {
    this.id = Objects.requireNonNull(id, "id");
    this.name = Objects.requireNonNull(name, "name");
    this.handler = Objects.requireNonNull(handler, "handler");
  }

  public String id() {
    return id;
  }

  public String name() {
    return name;
  }

  public WorkflowNodeHandler handler() {
    return handler;
  }
}
