package com.ryan.ddd.domain.demo.model;

import java.util.UUID;
import lombok.Getter;

@Getter
public final class DemoId {

  private final String value;

  private DemoId(String value) {
    this.value = value;
  }

  public static DemoId newId() {
    return new DemoId(UUID.randomUUID().toString());
  }
}
