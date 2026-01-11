package com.ryan.ddd.app.event;

import com.ryan.ddd.domain.event.DomainEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleEventDispatcher implements EventDispatcher {
  private final Map<String, EventHandler<? extends DomainEvent>> handlers = new ConcurrentHashMap<>();
  private final EventSerializer serializer;

  public SimpleEventDispatcher(EventSerializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public void registerHandler(EventHandler<? extends DomainEvent> handler) {
    handlers.put(handler.eventType(), handler);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void dispatch(EventEnvelope envelope) {
    EventHandler<? extends DomainEvent> handler = handlers.get(envelope.eventType());
    if (handler == null) {
      return;
    }
    DomainEvent event = serializer.deserialize(envelope.payload(),
        (Class<DomainEvent>) handler.eventClass());
    ((EventHandler<DomainEvent>) handler).handle(event);
  }
}
