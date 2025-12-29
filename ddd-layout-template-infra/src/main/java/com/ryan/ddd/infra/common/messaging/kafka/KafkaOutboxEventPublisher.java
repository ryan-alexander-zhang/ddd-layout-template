package com.ryan.ddd.infra.common.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.ddd.domain.common.event.DomainEvent;
import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.infra.common.exception.KafkaExceptionClassifier;
import com.ryan.ddd.infra.common.outbox.OutboxEventPublisher;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaOutboxEventPublisher implements OutboxEventPublisher {

  private static final String OP_OUTBOX_PUBLISH = "outbox.publish";
  private static final long SEND_TIMEOUT_SECONDS = 10;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;
  private final String consumerGroupId;

  public KafkaOutboxEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper,
      String topic,
      String consumerGroupId) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
    this.consumerGroupId = consumerGroupId;
  }

  @Override
  public void publish(EventEnvelope<? extends DomainEvent> envelope) {
    String key = envelope.getAggregateId().toString();
    try {
      String json = toJson(envelope);
      sendToKafka(envelope, json);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      throw KafkaExceptionClassifier.classifyConsume(OP_OUTBOX_PUBLISH, ie, topic, null, null,
          consumerGroupId);
    } catch (TimeoutException | ExecutionException | JsonProcessingException |
             RuntimeException ex) {
      throw KafkaExceptionClassifier.classifyProduce(OP_OUTBOX_PUBLISH, ex, topic, null, null,
          key);
    } catch (Exception ex) {
      throw KafkaExceptionClassifier.classifyConsume(OP_OUTBOX_PUBLISH, ex, topic, null, null,
          consumerGroupId);
    }
  }

  private String toJson(EventEnvelope<?> envelope) throws JsonProcessingException {
    Map<String, Object> msg = new HashMap<>();
    msg.put("eventId", envelope.getEventId());
    msg.put("type", envelope.getType());
    msg.put("occurredAt", envelope.getOccurredAt());
    msg.put("payload", envelope.getPayload());
    return objectMapper.writeValueAsString(msg);
  }

  private void sendToKafka(EventEnvelope<?> envelope, String json)
      throws InterruptedException, ExecutionException, TimeoutException {
    kafkaTemplate
        .send(topic, envelope.getAggregateId().toString(), json)
        .get(SEND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
  }
}
