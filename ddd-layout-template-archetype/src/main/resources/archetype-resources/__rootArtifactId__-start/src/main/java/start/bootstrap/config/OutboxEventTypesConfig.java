#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.start.bootstrap.config;

import ${package}.domain.demo.event.DemoCreatedEvent;
import ${package}.infra.common.outbox.OutboxEventTypeRegistry;
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
