package com.ryan.ddd.infra.common.event;

import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.infra.common.inbox.InboxEventDispatcher;
import org.springframework.context.event.EventListener;

public class SpringDomainEventListener {

  private final InboxEventDispatcher dispatcher;

  public SpringDomainEventListener(InboxEventDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  @EventListener
  public void on(EventEnvelope<?> envelope) {
    dispatcher.dispatch(envelope);
  }
}
