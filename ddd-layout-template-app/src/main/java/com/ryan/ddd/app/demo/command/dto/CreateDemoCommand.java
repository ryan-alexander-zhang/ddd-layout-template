package com.ryan.ddd.app.demo.command.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class CreateDemoCommand {

  private final String name;
}
