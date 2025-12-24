package com.ryan.ddd.common.event;

import com.ryan.ddd.domain.common.event.DomainEvent;

public interface EventHandler<T extends DomainEvent> {
  default String consumerId() {
    return getClass().getSimpleName();
  }
  void handler(T event);
  String type();
  Class<T> payloadClass();
}
