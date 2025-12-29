package com.ryan.ddd.domain.common.event;

import java.util.UUID;

public interface DomainEvent {
  String type();
  UUID aggregateId();
}
