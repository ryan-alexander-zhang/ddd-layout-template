package com.ryan.ddd.domain.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryan.ddd.domain.event.DomainEvent;
import com.ryan.ddd.domain.model.Identifier;
import java.time.Instant;
import java.util.Objects;

public final class OrderCreatedEvent implements DomainEvent {
  private final Identifier eventId;
  private final Identifier aggregateId;
  private final String customerId;
  private final Instant occurredAt;

  @JsonCreator
  public OrderCreatedEvent(@JsonProperty("eventId") Identifier eventId,
      @JsonProperty("aggregateId") Identifier aggregateId,
      @JsonProperty("customerId") String customerId,
      @JsonProperty("occurredAt") Instant occurredAt) {
    this.eventId = Objects.requireNonNull(eventId, "eventId");
    this.aggregateId = Objects.requireNonNull(aggregateId, "aggregateId");
    this.customerId = Objects.requireNonNull(customerId, "customerId");
    this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt");
  }

  @Override
  public Identifier eventId() {
    return eventId;
  }

  @Override
  public Identifier aggregateId() {
    return aggregateId;
  }

  @Override
  public String eventType() {
    return "order.created";
  }

  @Override
  public Instant occurredAt() {
    return occurredAt;
  }

  public String customerId() {
    return customerId;
  }
}
