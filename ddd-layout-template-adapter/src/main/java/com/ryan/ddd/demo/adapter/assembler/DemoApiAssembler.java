package com.ryan.ddd.demo.adapter.assembler;

import com.ryan.ddd.app.demo.command.dto.CreateDemoCommand;
import com.ryan.ddd.app.demo.command.dto.CreateDemoResult;
import com.ryan.ddd.demo.adapter.api.command.request.CreateDemoRequest;
import com.ryan.ddd.demo.adapter.api.command.response.CreateDemoResponse;

public class DemoApiAssembler {

  public static CreateDemoCommand toCommand(CreateDemoRequest request) {
    return CreateDemoCommand.builder().name(request.getName()).build();
  }

  public static CreateDemoResponse toResponse(CreateDemoResult createDemoResult) {
    return CreateDemoResponse.builder().id(String.valueOf(createDemoResult.getId())).build();
  }
}
