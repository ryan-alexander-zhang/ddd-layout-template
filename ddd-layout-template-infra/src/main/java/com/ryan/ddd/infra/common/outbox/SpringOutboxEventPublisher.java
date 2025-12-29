package com.ryan.ddd.infra.common.outbox;

import com.ryan.ddd.domain.common.event.DomainEvent;
import com.ryan.ddd.domain.common.event.EventEnvelope;
import org.springframework.context.ApplicationEventPublisher;

public class SpringOutboxEventPublisher implements OutboxEventPublisher {

  private final ApplicationEventPublisher publisher;

  public SpringOutboxEventPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void publish(EventEnvelope<? extends DomainEvent> envelope) {
    publisher.publishEvent(envelope);
  }
}
