package com.ryan.ddd.domain.demo.event;

import java.util.UUID;
import lombok.Getter;

@Getter
public class DemoCreatedEvent {

  public static final String TYPE = "demo.created";

  private final UUID id;

  public DemoCreatedEvent(UUID demoId) {
    this.id = demoId;
  }
}
