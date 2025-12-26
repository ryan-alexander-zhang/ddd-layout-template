package com.ryan.ddd.adapter.demo.adapter.api.query;

import com.ryan.ddd.app.common.CommandHandler;
import com.ryan.ddd.app.demo.query.dto.GetDemoDetailQuery;
import com.ryan.ddd.app.demo.query.view.GetDemoDetailView;
import com.ryan.ddd.adapter.common.SingleResponse;
import com.ryan.ddd.adapter.demo.adapter.api.query.response.GetDemoDetailResponse;
import com.ryan.ddd.adapter.demo.adapter.assembler.DemoApiAssembler;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoQueryController {

  private final CommandHandler<GetDemoDetailQuery, Optional<GetDemoDetailView>> handler;

  public DemoQueryController(CommandHandler<GetDemoDetailQuery, Optional<GetDemoDetailView>> handler) {
    this.handler = handler;
  }

  @GetMapping("/{id}/detail")
  public ResponseEntity<SingleResponse<GetDemoDetailResponse>> getDemoDetailById(
      @PathVariable("id") UUID id) {
    return handler.handle(new GetDemoDetailQuery(id))
        .map(DemoApiAssembler::toResponse)
        .map(SingleResponse::of)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
