#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.demo.adapter.assembler;

import ${package}.app.demo.command.dto.CreateDemoCommand;
import ${package}.app.demo.command.dto.CreateDemoResult;
import ${package}.app.demo.query.view.GetDemoDetailView;
import ${package}.demo.adapter.api.command.request.CreateDemoRequest;
import ${package}.demo.adapter.api.command.response.CreateDemoResponse;
import ${package}.demo.adapter.api.query.response.GetDemoDetailResponse;

public class DemoApiAssembler {

  public static CreateDemoCommand toCommand(CreateDemoRequest request) {
    return CreateDemoCommand.builder().name(request.getName()).build();
  }

  public static CreateDemoResponse toResponse(CreateDemoResult createDemoResult) {
    return CreateDemoResponse.builder().id(createDemoResult.getId().toString()).build();
  }

  public static GetDemoDetailResponse toResponse(GetDemoDetailView view) {
    return GetDemoDetailResponse.builder().id(view.getId().toString()).name(view.getName()).build();
  }

}
