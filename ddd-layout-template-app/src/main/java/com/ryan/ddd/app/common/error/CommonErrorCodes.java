package com.ryan.ddd.app.common.error;

public enum CommonErrorCodes implements ErrorCode {
  // Common (transport-agnostic) errors
  VALIDATION_ERROR("DDD-COM-VAL-0001", 400, false, "Invalid request."),
  UNAUTHORIZED("DDD-COM-AUTH-0001", 401, false, "Unauthorized."),
  FORBIDDEN("DDD-COM-AUTH-0002", 403, false, "Forbidden."),
  NOT_FOUND("DDD-COM-RES-0001", 404, false, "Resource not found."),
  CONFLICT("DDD-COM-BIZ-0001", 409, false, "Conflict."),
  SYSTEM_ERROR("DDD-COM-SYS-0001", 500, false, "Internal error."),

  // Dependency / downstream (generic)
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
