package com.ryan.ddd.app.event;

import com.ryan.ddd.domain.event.DomainEvent;
import com.ryan.ddd.domain.model.AggregateRoot;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EventOutboxService {
  private final EventOutboxRepository repository;
  private final EventSerializer serializer;
  private final Clock clock;

  public EventOutboxService(EventOutboxRepository repository, EventSerializer serializer,
      Clock clock) {
    this.repository = repository;
    this.serializer = serializer;
    this.clock = clock;
  }

  public void collectAndSave(AggregateRoot aggregate) {
    List<DomainEvent> events = aggregate.domainEvents();
    for (DomainEvent event : events) {
      String payload = serializer.serialize(event);
      EventEnvelope envelope = EventEnvelope.fromDomainEvent(event, payload);
      String id = UUID.randomUUID().toString();
      EventOutboxRecord record = new EventOutboxRecord(id, envelope, OutboxStatus.PENDING, 0,
          Instant.now(clock));
      repository.save(record);
    }
    aggregate.clearDomainEvents();
  }
}
