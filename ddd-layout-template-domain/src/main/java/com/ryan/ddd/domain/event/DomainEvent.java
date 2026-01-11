package com.ryan.ddd.domain.event;

import com.ryan.ddd.domain.model.Identifier;
import java.time.Instant;

public interface DomainEvent {
  Identifier eventId();

  Identifier aggregateId();

  String eventType();

  Instant occurredAt();
}
