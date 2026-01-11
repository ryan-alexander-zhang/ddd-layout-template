package com.ryan.ddd.app.event;

public interface EventBus {
  void publish(EventEnvelope envelope);
}
