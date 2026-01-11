package com.ryan.ddd.domain.model;

import com.ryan.ddd.domain.event.DomainEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot extends Entity {
  private final List<DomainEvent> domainEvents = new ArrayList<>();

  protected AggregateRoot(Identifier id) {
    super(id);
  }

  protected void raiseEvent(DomainEvent event) {
    domainEvents.add(event);
  }

  public List<DomainEvent> domainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  public void clearDomainEvents() {
    domainEvents.clear();
  }
}
