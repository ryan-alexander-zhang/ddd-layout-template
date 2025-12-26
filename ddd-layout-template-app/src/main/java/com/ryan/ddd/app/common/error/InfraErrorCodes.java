package com.ryan.ddd.app.common.error;

/**
 * Error codes that represent infrastructure/technical failures.
 *
 * <p>These codes are still application-facing (adapter can map them to HTTP), but the failures
 * originate from infra (DB/MQ/etc.) and should generally be considered retryable depending on the
 * code.
 */
public enum InfraErrorCodes implements ErrorCode {
  DB_DEADLOCK("DDD-COM-DB-0001", 503, true, "Database busy. Please retry."),
  DB_LOCK_TIMEOUT("DDD-COM-DB-0002", 503, true, "Database timeout. Please retry."),
  DB_CONSTRAINT("DDD-COM-DB-0003", 400, false, "Constraint violated."),
  DB_ERROR("DDD-COM-DB-0004", 500, false, "Database error."),

  SERDE_ERROR("DDD-COM-SER-0001", 500, false, "Serialization error."),

  DEPENDENCY_FAILURE("DDD-COM-EXT-0001", 503, true, "Dependency failure. Please retry."),
  ;

  private final String code;
  private final int httpStatus;
  private final boolean retryable;
  private final String message;

  InfraErrorCodes(String code, int httpStatus, boolean retryable, String message) {
    this.code = code;
    this.httpStatus = httpStatus;
    this.retryable = retryable;
    this.message = message;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public int httpStatus() {
    return httpStatus;
  }

  @Override
  public boolean retryable() {
    return retryable;
  }

  @Override
  public String message() {
    return message;
  }
}

