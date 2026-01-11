package com.ryan.ddd.app.event;

import com.ryan.ddd.domain.event.DomainEvent;
import java.time.Instant;
import java.util.Objects;

public final class EventEnvelope {
  private final String eventId;
  private final String aggregateId;
  private final String eventType;
  private final String payload;
  private final Instant occurredAt;

  public EventEnvelope(String eventId, String aggregateId, String eventType, String payload,
      Instant occurredAt) {
    this.eventId = Objects.requireNonNull(eventId, "eventId");
    this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId");
    this.eventType = Objects.requireNonNull(eventType, "eventType");
    this.payload = Objects.requireNonNull(payload, "payload");
    this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt");
  }

  public String eventId() {
    return eventId;
  }

  public String aggregateId() {
    return aggregateId;
  }

  public String eventType() {
    return eventType;
  }

  public String payload() {
    return payload;
  }

  public Instant occurredAt() {
    return occurredAt;
  }

  public static EventEnvelope fromDomainEvent(DomainEvent event, String payload) {
    return new EventEnvelope(
        event.eventId().value(),
        event.aggregateId().value(),
        event.eventType(),
        payload,
        event.occurredAt());
  }
}
