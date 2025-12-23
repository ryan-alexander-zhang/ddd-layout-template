package com.ryan.ddd.app.demo.command.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateDemoResult {

  private final String id;

}
