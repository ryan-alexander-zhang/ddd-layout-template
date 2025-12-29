package com.ryan.ddd.infra.common.outbox;

import com.ryan.ddd.domain.common.event.DomainEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventTypeRegistry {

  private final Map<String, Class<? extends DomainEvent>> types = new ConcurrentHashMap<>();

  public void register(String type, Class<? extends DomainEvent> clazz) {
    if (type == null || type.isBlank()) {
      throw new IllegalArgumentException("type must not be blank");
    }
    if (clazz == null) {
      throw new IllegalArgumentException("clazz must not be null");
    }
    types.put(type, clazz);
  }

  public Class<? extends DomainEvent> required(String type) {
    Class<? extends DomainEvent> c = types.get(type);
    if (c == null) {
      throw new IllegalArgumentException("Unknown outbox event type: " + type);
    }
    return c;
  }
}
