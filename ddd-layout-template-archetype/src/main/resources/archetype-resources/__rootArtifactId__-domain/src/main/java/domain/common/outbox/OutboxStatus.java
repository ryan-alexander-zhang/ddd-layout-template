#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.common.outbox;

public class OutboxStatus {
  public static final String PENDING = "PENDING";
  public static final String SENT = "SENT";
  public static final String PROCESSING = "PROCESSING";
  public static final String DEAD = "DEAD";
}
