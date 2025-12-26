package com.ryan.ddd.app.common.exception;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class InfraException extends RuntimeException {

  private final FailureCategory category;
  private final FailureReason reason;
  private final boolean retryable;
  private final Map<String, Serializable> attributes;
  protected InfraException(FailureCategory category,
      FailureReason reason,
      boolean retryable,
      String message,
      Throwable cause,
      Map<String, Serializable> attributes) {
    super(message, cause);
    this.category = category;
    this.reason = reason;
    this.retryable = retryable;
    this.attributes = attributes == null
        ? Collections.emptyMap()
        : Collections.unmodifiableMap(new HashMap<>(attributes));
  }

  public FailureCategory category() {
    return category;
  }

  public FailureReason reason() {
    return reason;
  }

  public boolean retryable() {
    return retryable;
  }

  public Map<String, Serializable> attributes() {
    return attributes;
  }

  public enum FailureCategory {DB, MQ, CACHE, HTTP, SERDE, UNKNOWN}

  public enum FailureReason {
    TIMEOUT,
    CONNECTION,
    UNAVAILABLE,
    THROTTLED,
    AUTH,
    PERMISSION,
    BAD_RESPONSE,
    SERDE,
    DATA_INTEGRITY,
    CONFLICT,
    DEADLOCK,
    LOCK_TIMEOUT,
    CONSTRAINT_VIOLATION,
    NOT_FOUND,
    CANCELLED,
    UNKNOWN
  }
}
