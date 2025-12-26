package com.ryan.ddd.adapter.demo.adapter.assembler;

import com.ryan.ddd.adapter.demo.adapter.api.command.request.CreateDemoRequest;
import com.ryan.ddd.adapter.demo.adapter.api.command.response.CreateDemoResponse;
import com.ryan.ddd.adapter.demo.adapter.api.query.response.GetDemoDetailResponse;
import com.ryan.ddd.app.demo.command.dto.CreateDemoCommand;
import com.ryan.ddd.app.demo.command.dto.CreateDemoResult;
import com.ryan.ddd.app.demo.query.view.GetDemoDetailView;

public class DemoApiAssembler {

  private DemoApiAssembler() {
  }

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
