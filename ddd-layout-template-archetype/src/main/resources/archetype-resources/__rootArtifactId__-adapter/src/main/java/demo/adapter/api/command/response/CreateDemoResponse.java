#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.demo.adapter.api.command.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDemoResponse {

  private String id;
}
