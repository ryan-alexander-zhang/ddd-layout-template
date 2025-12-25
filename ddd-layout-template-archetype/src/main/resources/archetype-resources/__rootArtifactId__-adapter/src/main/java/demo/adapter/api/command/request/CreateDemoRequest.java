#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.demo.adapter.api.command.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDemoRequest {

  @NotBlank(message = "Name must not be blank")
  private String name;
}
