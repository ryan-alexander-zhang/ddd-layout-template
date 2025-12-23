package com.ryan.ddd.domain.common.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
  String type();
}
