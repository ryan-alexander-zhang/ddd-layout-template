package com.ryan.ddd.infra.common.exception;

import com.ryan.ddd.app.common.exception.InfraException;

import java.io.Serializable;
import java.util.Map;

public class MqException extends InfraException {
  public MqException(FailureReason reason,
      boolean retryable,
      String message,
      Throwable cause,
      Map<String, Serializable> attributes) {
    super(FailureCategory.MQ, reason, retryable, message, cause, attributes);
  }
}

