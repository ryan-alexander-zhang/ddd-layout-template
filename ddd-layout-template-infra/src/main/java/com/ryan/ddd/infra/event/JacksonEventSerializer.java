package com.ryan.ddd.infra.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.ddd.app.event.EventSerializer;
import com.ryan.ddd.domain.event.DomainEvent;

public class JacksonEventSerializer implements EventSerializer {
  private final ObjectMapper objectMapper;

  public JacksonEventSerializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String serialize(DomainEvent event) {
    try {
      return objectMapper.writeValueAsString(event);
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException("Failed to serialize event", ex);
    }
  }

  @Override
  public <E extends DomainEvent> E deserialize(String payload, Class<E> eventClass) {
    try {
      return objectMapper.readValue(payload, eventClass);
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException("Failed to deserialize event", ex);
    }
  }
}
