package com.ryan.ddd.app.event;

import com.ryan.ddd.domain.event.DomainEvent;

public interface EventHandler<E extends DomainEvent> {
  String eventType();

  Class<E> eventClass();

  void handle(E event);
}
