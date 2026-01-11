package com.ryan.ddd.app.workflow;

public final class NodeResult {
  private final boolean success;
  private final String message;

  private NodeResult(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public static NodeResult success(String message) {
    return new NodeResult(true, message);
  }

  public static NodeResult failure(String message) {
    return new NodeResult(false, message);
  }

  public boolean success() {
    return success;
  }

  public String message() {
    return message;
  }
}
