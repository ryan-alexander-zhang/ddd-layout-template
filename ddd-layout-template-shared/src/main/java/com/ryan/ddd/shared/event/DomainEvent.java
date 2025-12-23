package com.ryan.ddd.shared.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

  UUID eventId();

  Instant occurredAt();
}
