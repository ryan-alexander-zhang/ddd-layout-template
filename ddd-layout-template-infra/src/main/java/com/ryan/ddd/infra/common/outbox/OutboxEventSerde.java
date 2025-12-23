package com.ryan.ddd.infra.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
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

  public EventEnvelope<?> toEnvelope(OutboxEventPO po) throws Exception {
    Class<?> clazz = registry.required(po.getType());
    Object payload = objectMapper.readValue(po.getPayload(), clazz);
    return new EventEnvelope<>(po.getId(), po.getType(), payload, po.getOccurredAt());
  }
}
