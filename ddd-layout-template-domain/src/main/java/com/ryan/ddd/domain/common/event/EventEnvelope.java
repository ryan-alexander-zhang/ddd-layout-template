package com.ryan.ddd.domain.common.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public final class EventEnvelope<T extends DomainEvent> {

  private final UUID eventId;
  private final String type;
  private final T payload;
  private final UUID aggregateId;    // Aggregation ID. Used the ID for message key.
  private final Instant occurredAt;

  public EventEnvelope(UUID eventId, T payload, Instant occurredAt) {
    this.eventId = Objects.requireNonNull(eventId, "eventId");
    this.payload = Objects.requireNonNull(payload, "payload");
    this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt");

    this.type = Objects.requireNonNull(payload.type(), "type");
    this.aggregateId = Objects.requireNonNull(payload.aggregateId(), "aggregateId");
  }
}

