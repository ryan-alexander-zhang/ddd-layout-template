package com.ryan.ddd.demo.adapter.api.command;

import com.ryan.ddd.app.demo.command.dto.CreateDemoResult;
import com.ryan.ddd.app.demo.command.handler.CreateDemoCommandHandler;
import com.ryan.ddd.common.SingleResponse;
import com.ryan.ddd.demo.adapter.api.command.request.CreateDemoRequest;
import com.ryan.ddd.demo.adapter.api.command.response.CreateDemoResponse;
import com.ryan.ddd.demo.adapter.assembler.DemoApiAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoCommandController {

  private final CreateDemoCommandHandler createDemoCommandHandler;

  public DemoCommandController(CreateDemoCommandHandler createDemoCommandHandler) {
    this.createDemoCommandHandler = createDemoCommandHandler;
  }

  @PostMapping
  public ResponseEntity<SingleResponse<CreateDemoResponse>> create(
      @Validated @RequestBody CreateDemoRequest request) {
    // Implementation goes here
    CreateDemoResult createDemoResult = createDemoCommandHandler.handle(
        DemoApiAssembler.toCommand(request));
    return ResponseEntity.ok(SingleResponse.of(DemoApiAssembler.toResponse(createDemoResult)));
  }
}
