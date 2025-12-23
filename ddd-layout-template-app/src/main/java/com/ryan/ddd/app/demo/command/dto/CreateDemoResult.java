package com.ryan.ddd.app.demo.command.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateDemoResult {

  private final UUID id;
  private final String name;
}
