package com.ryan.ddd.app.demo.command.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public final class CreateDemoCommand {

  private final String name;
}
