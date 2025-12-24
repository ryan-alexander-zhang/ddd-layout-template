package com.ryan.ddd.common.event;

import com.ryan.ddd.domain.common.event.DomainEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerRegistry {

  private final Map<String, List<EventHandler<? extends DomainEvent>>> byType;

  public HandlerRegistry(List<EventHandler<? extends DomainEvent>> handlers) {
    Map<String, List<EventHandler<? extends DomainEvent>>> m = new HashMap<>();
    for (EventHandler<? extends DomainEvent> h : handlers) {
      m.computeIfAbsent(h.type(), k -> new ArrayList<>()).add(h);
    }

    for (List<EventHandler<? extends DomainEvent>> list : m.values()) {
      list.sort(Comparator.comparing(EventHandler::consumerId));
    }

    this.byType = Collections.unmodifiableMap(m);
  }

  public List<EventHandler<? extends DomainEvent>> list(String type) {
    return byType.getOrDefault(type, List.of());
  }
}
