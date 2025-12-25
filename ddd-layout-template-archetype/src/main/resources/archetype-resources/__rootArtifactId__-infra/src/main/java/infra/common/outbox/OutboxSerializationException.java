#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.outbox;

public class OutboxSerializationException extends RuntimeException {
  public OutboxSerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}