package com.ryan.ddd.start.bootstrap.config;

import com.ryan.ddd.infra.common.outbox.OutboxEventTypeRegistry;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class OutboxEventTypesConfig {

  private final OutboxEventTypeRegistry registry;

  public OutboxEventTypesConfig(OutboxEventTypeRegistry registry) {
    this.registry = registry;
  }

  @PostConstruct
  public void register() {
//    registry.register(OrderCreatedEvent.TYPE, OrderCreatedEvent.class);
  }
}
