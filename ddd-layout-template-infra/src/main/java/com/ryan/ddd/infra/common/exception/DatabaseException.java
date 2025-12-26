package com.ryan.ddd.infra.common.exception;

import com.ryan.ddd.app.common.exception.InfraException;
import java.io.Serializable;
import java.util.Map;

public class DatabaseException extends InfraException {

  public DatabaseException(FailureReason reason,
      boolean retryable,
      String message,
      Throwable cause,
      Map<String, Serializable> attributes) {
    super(FailureCategory.DB, reason, retryable, message, cause, attributes);
  }
}