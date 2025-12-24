package com.ryan.ddd.start.bootstrap.config;

import com.ryan.ddd.domain.demo.event.DemoCreatedEvent;
import com.ryan.ddd.infra.common.outbox.OutboxEventTypeRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxEventTypesConfig {

  private final OutboxEventTypeRegistry registry;

  public OutboxEventTypesConfig(OutboxEventTypeRegistry registry) {
    this.registry = registry;
  }

  @PostConstruct
  public void register() {
    registry.register(DemoCreatedEvent.TYPE, DemoCreatedEvent.class);
  }
}
