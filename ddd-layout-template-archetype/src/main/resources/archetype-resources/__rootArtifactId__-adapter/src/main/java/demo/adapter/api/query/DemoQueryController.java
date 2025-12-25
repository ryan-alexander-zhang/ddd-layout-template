#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.demo.adapter.api.query;

import ${package}.app.demo.query.dto.GetDemoDetailQuery;
import ${package}.app.demo.query.handler.GetDemoDetailQueryHandler;
import ${package}.common.SingleResponse;
import ${package}.demo.adapter.api.query.response.GetDemoDetailResponse;
import ${package}.demo.adapter.assembler.DemoApiAssembler;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoQueryController {

  private final GetDemoDetailQueryHandler handler;

  public DemoQueryController(GetDemoDetailQueryHandler handler) {
    this.handler = handler;
  }

  @GetMapping("/{id}/detail")
  public ResponseEntity<SingleResponse<GetDemoDetailResponse>> getDemoDetailById(@PathVariable("id") UUID id) {
    return handler.handle(new GetDemoDetailQuery(id))
        .map(DemoApiAssembler::toResponse)
        .map(SingleResponse::of)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
