package com.ryan.ddd.demo.adapter.api.query;

import com.ryan.ddd.app.demo.query.dto.GetDemoDetailQuery;
import com.ryan.ddd.app.demo.query.handler.GetDemoDetailQueryHandler;
import com.ryan.ddd.demo.adapter.api.query.response.GetDemoDetailResponse;
import com.ryan.ddd.demo.adapter.assembler.DemoApiAssembler;
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
  public ResponseEntity<GetDemoDetailResponse> getDemoDetailById(@PathVariable("id") UUID id) {
    return handler.handle(new GetDemoDetailQuery(id)).map(DemoApiAssembler::toResponse)
        .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
}
