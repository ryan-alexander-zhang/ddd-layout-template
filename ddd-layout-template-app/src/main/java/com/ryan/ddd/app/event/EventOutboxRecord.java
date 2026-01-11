package com.ryan.ddd.app.event;

import java.time.Instant;
import java.util.Objects;

public final class EventOutboxRecord {
  private final String id;
  private final EventEnvelope envelope;
  private final OutboxStatus status;
  private final int retryCount;
  private final Instant nextAttemptAt;

  public EventOutboxRecord(String id, EventEnvelope envelope, OutboxStatus status, int retryCount,
      Instant nextAttemptAt) {
    this.id = Objects.requireNonNull(id, "id");
    this.envelope = Objects.requireNonNull(envelope, "envelope");
    this.status = Objects.requireNonNull(status, "status");
    this.retryCount = retryCount;
    this.nextAttemptAt = Objects.requireNonNull(nextAttemptAt, "nextAttemptAt");
  }

  public String id() {
    return id;
  }

  public EventEnvelope envelope() {
    return envelope;
  }

  public OutboxStatus status() {
    return status;
  }

  public int retryCount() {
    return retryCount;
  }

  public Instant nextAttemptAt() {
    return nextAttemptAt;
  }

  public EventOutboxRecord withStatus(OutboxStatus newStatus, int newRetryCount,
      Instant newNextAttemptAt) {
    return new EventOutboxRecord(id, envelope, newStatus, newRetryCount, newNextAttemptAt);
  }
}
