package com.ryan.ddd.infra.event;

import com.ryan.ddd.app.event.EventBus;
import com.ryan.ddd.app.event.EventEnvelope;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaEventBus implements EventBus {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String topic;

  public KafkaEventBus(KafkaTemplate<String, String> kafkaTemplate, String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.topic = topic;
  }

  @Override
  public void publish(EventEnvelope envelope) {
    kafkaTemplate.send(topic, envelope.eventId(), envelope.payload());
  }
}
