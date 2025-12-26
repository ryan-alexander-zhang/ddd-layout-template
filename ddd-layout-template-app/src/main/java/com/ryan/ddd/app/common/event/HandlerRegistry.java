package com.ryan.ddd.app.common.event;

import com.ryan.ddd.domain.common.event.DomainEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HandlerRegistry {

  private final Map<String, List<EventHandle<? extends DomainEvent>>> byType;

  public HandlerRegistry(List<EventHandle<? extends DomainEvent>> handlers) {
    Map<String, List<EventHandle<? extends DomainEvent>>> m = new HashMap<>();
    for (EventHandle<? extends DomainEvent> h : handlers) {
      m.computeIfAbsent(h.getType(), k -> new ArrayList<>()).add(h);
    }

    for (List<EventHandle<? extends DomainEvent>> list : m.values()) {
      list.sort(Comparator.comparing(EventHandle::consumerId));
    }

    this.byType = Collections.unmodifiableMap(m);
  }

  public void forEachHandler(String type, Consumer<EventHandle<? extends DomainEvent>> consumer) {
    for (var h : byType.getOrDefault(type, List.of())) {
      consumer.accept(h);
    }
  }
}