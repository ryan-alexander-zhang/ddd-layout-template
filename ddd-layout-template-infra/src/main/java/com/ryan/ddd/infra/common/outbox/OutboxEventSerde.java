package com.ryan.ddd.infra.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.ddd.domain.common.event.DomainEvent;
import com.ryan.ddd.domain.common.event.EventEnvelope;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventSerde {

  private final ObjectMapper objectMapper;
  private final OutboxEventTypeRegistry registry;

  public OutboxEventSerde(ObjectMapper objectMapper, OutboxEventTypeRegistry registry) {
    this.objectMapper = objectMapper;
    this.registry = registry;
  }

  public EventEnvelope<DomainEvent> toEnvelope(OutboxEventPO po)
      throws JsonProcessingException {

    Class<? extends DomainEvent> clazz = registry.required(po.getType());
    DomainEvent payload = objectMapper.readValue(po.getPayload(), clazz);

    if (!po.getType().equals(payload.type())) {
      throw new IllegalArgumentException(
          "Outbox type mismatch. outbox=" + po.getType() + ", payload=" + payload.type());
    }

    return new EventEnvelope<>(po.getId(), payload, po.getOccurredAt());
  }
}
