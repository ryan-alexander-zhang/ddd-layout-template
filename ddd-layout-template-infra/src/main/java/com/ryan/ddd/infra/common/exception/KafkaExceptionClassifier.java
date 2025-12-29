package com.ryan.ddd.infra.common.exception;

import com.ryan.ddd.app.common.exception.InfraException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class KafkaExceptionClassifier {

  private KafkaExceptionClassifier() {}

  public static MqException classifyConsume(
      String op,
      Throwable cause,
      String topic,
      Integer partition,
      Long offset,
      String groupId
  ) {
    Throwable root = unwrap(cause);
    InfraException.FailureReason reason = mapKafka(root);
    boolean retryable = isRetryable(reason);

    Map<String, Serializable> attrs = new HashMap<>();
    attrs.put("op", op);
    attrs.put("topic", topic);
    attrs.put("partition", partition);
    attrs.put("offset", offset);
    attrs.put("groupId", groupId);
    attrs.put("exception", cause.getClass().getName());
    attrs.put("rootException", root.getClass().getName());

    return new MqException(reason, retryable, "MQ failure on " + op, cause, attrs);
  }

  public static MqException classifyProduce(
      String op,
      Throwable cause,
      String topic,
      Integer partition,
      Long offset,
      String key
  ) {
    Throwable root = unwrap(cause);
    InfraException.FailureReason reason = mapKafka(root);
    boolean retryable = isRetryable(reason);

    Map<String, Serializable> attrs = new HashMap<>();
    attrs.put("op", op);
    attrs.put("topic", topic);
    attrs.put("partition", partition);
    attrs.put("offset", offset);
    attrs.put("key", key);
    attrs.put("exception", cause.getClass().getName());
    attrs.put("rootException", root.getClass().getName());

    return new MqException(reason, retryable, "MQ failure on " + op, cause, attrs);
  }

  private static InfraException.FailureReason mapKafka(Throwable t) {
    if (hasTypeName(t, "TimeoutException")) {
      return InfraException.FailureReason.TIMEOUT;
    }
    if (hasTypeName(t, "NetworkException") || hasTypeName(t, "DisconnectException")) {
      return InfraException.FailureReason.CONNECTION;
    }
    if (hasTypeName(t, "AuthorizationException")) {
      return InfraException.FailureReason.PERMISSION;
    }
    if (hasTypeName(t, "AuthenticationException")) {
      return InfraException.FailureReason.AUTH;
    }
    if (hasTypeName(t, "SerializationException") || hasTypeName(t, "JsonProcessingException")) {
      return InfraException.FailureReason.SERDE;
    }
    if (hasTypeName(t, "RecordTooLargeException")) {
      return InfraException.FailureReason.BAD_RESPONSE;
    }
    if (hasTypeName(t, "RetriableException")) {
      return InfraException.FailureReason.UNAVAILABLE;
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

  private static Throwable unwrap(Throwable t) {
    Throwable cur = t;
    while (cur instanceof ExecutionException || cur instanceof CompletionException) {
      Throwable next = cur.getCause();
      if (next == null || next == cur) break;
      cur = next;
    }

    Set<Throwable> seen = new HashSet<>();
    while (cur.getCause() != null && cur.getCause() != cur && !seen.contains(cur.getCause())) {
      seen.add(cur);
      cur = cur.getCause();
    }
    return cur;
  }

  private static boolean hasTypeName(Throwable t, String simpleName) {
    Deque<Class<?>> q = new ArrayDeque<>();
    Set<Class<?>> seen = new HashSet<>();
    q.add(t.getClass());

    while (!q.isEmpty()) {
      Class<?> c = q.removeFirst();
      if (c == null || !seen.add(c)) continue;

      if (simpleName.equals(c.getSimpleName()) || c.getName().endsWith("." + simpleName)) {
        return true;
      }

      Class<?> sup = c.getSuperclass();
      if (sup != null) q.addLast(sup);

      for (Class<?> itf : c.getInterfaces()) {
        if (itf != null) q.addLast(itf);
      }
    }

    return false;
  }
}
