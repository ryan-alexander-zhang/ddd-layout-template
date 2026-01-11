package com.ryan.ddd.app.event;

import com.ryan.ddd.domain.event.DomainEvent;

public interface EventSerializer {
  String serialize(DomainEvent event);

  <E extends DomainEvent> E deserialize(String payload, Class<E> eventClass);
}
