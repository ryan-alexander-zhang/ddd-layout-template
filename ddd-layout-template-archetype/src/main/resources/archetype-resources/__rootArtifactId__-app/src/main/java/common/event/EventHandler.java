#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.event;

import ${package}.domain.common.event.DomainEvent;

public interface EventHandler<T extends DomainEvent> {
  default String consumerId() {
    return getClass().getSimpleName();
  }
  void handler(T event);
  String type();
  Class<T> payloadClass();
}
