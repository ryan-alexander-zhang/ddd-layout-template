package com.ryan.ddd.app.event;

import com.ryan.ddd.domain.event.DomainEvent;

public interface EventDispatcher {
  void registerHandler(EventHandler<? extends DomainEvent> handler);

  void dispatch(EventEnvelope envelope);
}
