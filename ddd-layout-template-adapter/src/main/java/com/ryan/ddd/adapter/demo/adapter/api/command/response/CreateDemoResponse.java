package com.ryan.ddd.adapter.demo.adapter.api.command.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDemoResponse {

  private String id;
}
