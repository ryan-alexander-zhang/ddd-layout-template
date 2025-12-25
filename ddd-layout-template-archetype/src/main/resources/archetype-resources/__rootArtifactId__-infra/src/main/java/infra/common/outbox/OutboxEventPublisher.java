#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.outbox;

import ${package}.domain.common.event.EventEnvelope;

public interface OutboxEventPublisher {

  void publish(EventEnvelope<?> envelope) throws Exception;
}
