#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.common.outbox;


import ${package}.domain.common.event.EventEnvelope;

public interface OutboxRepository {
  void append(EventEnvelope<?> envelope);
}
