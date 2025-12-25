#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.outbox;

import ${package}.domain.common.event.EventEnvelope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

// TODO for now
@Component
public class SpringOutboxEventPublisher implements OutboxEventPublisher {

  private final ApplicationEventPublisher publisher;

  public SpringOutboxEventPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void publish(EventEnvelope<?> envelope) {
    publisher.publishEvent(envelope);
  }
}
