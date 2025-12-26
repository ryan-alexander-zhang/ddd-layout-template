package com.ryan.ddd.app.common.error;

public enum CommonErrorCodes implements ErrorCode {
  // What is the best code pattern?
  VALIDATION_ERROR("01", 400, false, "Invalid request."),
  UNAUTHORIZED("01", 401, false, "Unauthorized."),
  FORBIDDEN("03", 403, false, "Forbidden."),
  NOT_FOUND("04", 404, false, "Resource not found."),
  CONFLICT("05", 409, false, "Conflict."),
  SYSTEM_ERROR("DDD-COM-SYS-0001", 500, false, "Internal error."),

  // Database Error Code
  DB_DEADLOCK("DDD-COM-DB-0001", 503, true, "Database busy. Please retry."),
  DB_LOCK_TIMEOUT("DDD-COM-DB-0002", 503, true, "Database timeout. Please retry."),
  DB_CONSTRAINT("DDD-COM-DB-0003", 400, false, "Constraint violated."),
  DB_ERROR("DDD-COM-DB-0004", 500, false, "Database error."),

  // Downstream Error Code
  DEPENDENCY_FAILURE("DDD-COM-EXT-0001", 503, true, "Dependency failure. Please retry."),
  ;

  private final String code;
  private final int httpStatus;
  private final boolean retryable;
  private final String message;

  CommonErrorCodes(String code, int httpStatus, boolean retryable, String message) {
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
