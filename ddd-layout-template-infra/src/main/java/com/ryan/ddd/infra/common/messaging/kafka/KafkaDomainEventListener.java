package com.ryan.ddd.infra.common.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.ddd.domain.common.event.DomainEvent;
import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.infra.common.inbox.InboxEventDispatcher;
import com.ryan.ddd.infra.common.outbox.OutboxEventTypeRegistry;
import java.time.Instant;
import java.util.UUID;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaDomainEventListener {

  private final InboxEventDispatcher dispatcher;
  private final OutboxEventTypeRegistry registry;
  private final ObjectMapper objectMapper;

  public KafkaDomainEventListener(InboxEventDispatcher dispatcher,
      OutboxEventTypeRegistry registry,
      ObjectMapper objectMapper) {
    this.dispatcher = dispatcher;
    this.registry = registry;
    this.objectMapper = objectMapper;
  }

  @KafkaListener(
      topics = "#{'${messaging.kafka.topic:ddd-events}'}",
      groupId = "${messaging.kafka.consumer-group-id:ddd-layout-template}")
  public void onMessage(String message) throws JsonProcessingException {
    JsonNode root = objectMapper.readTree(message);

    UUID eventId = UUID.fromString(requiredText(root, "eventId"));
    Instant occurredAt = Instant.parse(requiredText(root, "occurredAt"));

    String type = requiredText(root, "type");
    Class<? extends DomainEvent> clazz = registry.required(type);

    DomainEvent payload = objectMapper.treeToValue(required(root, "payload"), clazz);

    dispatcher.dispatch(new EventEnvelope<>(eventId, payload, occurredAt));
  }

  private static JsonNode required(JsonNode root, String field) {
    JsonNode n = root.get(field);
    if (n == null || n.isNull()) {
      throw new IllegalArgumentException("Missing field: " + field);
    }
    return n;
  }

  private static String requiredText(JsonNode root, String field) {
    JsonNode n = required(root, field);
    String v = n.asText();
    if (v == null || v.isBlank()) {
      throw new IllegalArgumentException("Missing field: " + field);
    }
    return v;
  }
}
