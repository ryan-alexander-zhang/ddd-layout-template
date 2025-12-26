package com.ryan.ddd.infra.common.outbox;

import com.ryan.ddd.app.common.exception.InfraException;
import java.io.Serializable;
import java.util.Map;

public class OutboxSerializationException extends InfraException {

  public OutboxSerializationException(String message, Throwable cause) {
    super(FailureCategory.SERDE, FailureReason.SERDE, false, message, cause,
        Map.of("component", "outbox"));
  }

  public OutboxSerializationException(String message, Throwable cause, Map<String, Serializable> attributes) {
    super(FailureCategory.SERDE, FailureReason.SERDE, false, message, cause, attributes);
  }
}