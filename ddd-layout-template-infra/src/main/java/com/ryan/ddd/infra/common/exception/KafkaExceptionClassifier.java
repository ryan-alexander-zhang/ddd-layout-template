package com.ryan.ddd.infra.common.exception;

import com.ryan.ddd.app.common.exception.InfraException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class KafkaExceptionClassifier {

  private KafkaExceptionClassifier() {
  }

  public static MqException classifyConsume(String op,
      Throwable cause,
      String topic,
      Integer partition,
      Long offset,
      String groupId) {
    InfraException.FailureReason reason = mapKafka(cause);
    boolean retryable = isRetryable(reason);

    Map<String, Serializable> attrs = new HashMap<>();
    attrs.put("op", op);
    attrs.put("topic", topic);
    attrs.put("partition", partition);
    attrs.put("offset", offset);
    attrs.put("groupId", groupId);
    attrs.put("kafkaException", cause.getClass().getName());

    return new MqException(reason, retryable, "MQ failure on " + op, cause, attrs);
  }

  private static InfraException.FailureReason mapKafka(Throwable t) {
    String name = t.getClass().getName();

    if (name.endsWith("TimeoutException")) {
      return InfraException.FailureReason.TIMEOUT;
    }
    if (name.endsWith("NetworkException")) {
      return InfraException.FailureReason.CONNECTION;
    }
    if (name.endsWith("DisconnectException")) {
      return InfraException.FailureReason.CONNECTION;
    }
    if (name.endsWith("RetriableException")) {
      return InfraException.FailureReason.UNAVAILABLE;
    }
    if (name.endsWith("AuthorizationException")) {
      return InfraException.FailureReason.PERMISSION;
    }
    if (name.endsWith("AuthenticationException")) {
      return InfraException.FailureReason.AUTH;
    }
    if (name.endsWith("SerializationException")) {
      return InfraException.FailureReason.SERDE;
    }
    if (name.endsWith("RecordTooLargeException")) {
      return InfraException.FailureReason.BAD_RESPONSE;
    }

    return InfraException.FailureReason.UNKNOWN;
  }

  private static boolean isRetryable(InfraException.FailureReason reason) {
    return reason == InfraException.FailureReason.TIMEOUT
        || reason == InfraException.FailureReason.CONNECTION
        || reason == InfraException.FailureReason.UNAVAILABLE
        || reason == InfraException.FailureReason.THROTTLED
        || reason == InfraException.FailureReason.CANCELLED
        || reason == InfraException.FailureReason.CONFLICT;
  }
}
