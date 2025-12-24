package com.ryan.ddd.common.event;

import com.ryan.ddd.domain.common.event.DomainEvent;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HandlerRegistry {

  private final Map<String, EventHandler<? extends DomainEvent>> byType;

  public HandlerRegistry(List<EventHandler<? extends DomainEvent>> handlers) {
    this.byType = handlers.stream().collect(Collectors.toMap(
        EventHandler::type,
        Function.identity(),
        (a, b) -> {
          throw new IllegalStateException("Duplicate handler type: " + a.type());
        }
    ));
  }

  public EventHandler<?> get(String type) {
    return byType.get(type);
  }
}
