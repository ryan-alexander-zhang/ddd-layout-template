#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.common.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
  String type();
}
