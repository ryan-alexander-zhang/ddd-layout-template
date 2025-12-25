#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common;

public interface CommandHandler<C, R> {

  R handle(C command);
}
