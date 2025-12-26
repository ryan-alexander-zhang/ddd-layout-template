package com.ryan.ddd.app.common.event;

import com.ryan.ddd.domain.common.event.DomainEvent;

public interface EventHandle<T extends DomainEvent> {
  default String consumerId() {
    return getClass().getSimpleName();
  }
  void handler(T event);
  String getType();
  Class<T> payloadClass();
}

