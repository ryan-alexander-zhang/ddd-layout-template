#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ${package}.domain.common.event.EventEnvelope;
import ${package}.domain.common.outbox.OutboxRepository;
import ${package}.domain.common.outbox.OutboxStatus;
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
    po.setId(envelope.getId());
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

