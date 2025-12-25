#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.app.demo.command.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public final class CreateDemoCommand {

  private final String name;
}
