package com.ryan.ddd.shared.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public final class EventEnvelope<T> {

  private final UUID id;
  private final String type;
  private final T payload;
  private final Instant occurredAt;

  public EventEnvelope(UUID id, String type, T payload, Instant occurredAt) {
    this.id = Objects.requireNonNull(id, "id");
    this.type = Objects.requireNonNull(type, "type");
    this.payload = Objects.requireNonNull(payload, "payload");
    this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt");
  }
}

