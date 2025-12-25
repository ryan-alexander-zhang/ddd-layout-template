#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.event;

import ${package}.domain.common.event.EventEnvelope;
import ${package}.infra.common.inbox.InboxEventDispatcher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
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
