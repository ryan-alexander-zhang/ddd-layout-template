#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.common.inbox;

public enum InboxStatus {
  PROCESSING,
  DONE,
  FAILED
}
