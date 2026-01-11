package com.ryan.ddd.app.readmodel;

import com.ryan.ddd.domain.event.DomainEvent;

public interface ReadModelUpdater<E extends DomainEvent> {
  String eventType();

  Class<E> eventClass();

  void update(E event);
}
