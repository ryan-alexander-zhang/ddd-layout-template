package com.ryan.ddd.infra.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.domain.common.outbox.OutboxRepository;
import com.ryan.ddd.domain.common.outbox.OutboxStatus;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class OutboxRepositoryImpl implements OutboxRepository {

  private final OutboxEventMapper mapper;
  private final ObjectMapper objectMapper;

  public OutboxRepositoryImpl(OutboxEventMapper mapper, ObjectMapper objectMapper) {
    this.mapper = mapper;
    this.objectMapper = objectMapper;
  }

  @Override
  public void append(EventEnvelope<?> envelope) {
    OutboxEventPO po = new OutboxEventPO();
    po.setId(envelope.getEventId());
    po.setType(envelope.getType());
    po.setPayload(toJson(envelope.getPayload()));
    po.setOccurredAt(envelope.getOccurredAt());

    po.setStatus(OutboxStatus.PENDING);
    po.setAttempts(0);

    Instant now = Instant.now();
    po.setNextAttemptAt(envelope.getOccurredAt().isAfter(now) ? envelope.getOccurredAt() : now);

    mapper.insert(po);
  }

  private String toJson(Object payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new OutboxSerializationException("Failed to serialize outbox payload", e);
    }
  }
}

