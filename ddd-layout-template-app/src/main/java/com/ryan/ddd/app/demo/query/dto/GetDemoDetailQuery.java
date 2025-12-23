package com.ryan.ddd.app.demo.query.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public final class GetDemoDetailQuery {
  private final UUID id;

  public GetDemoDetailQuery(UUID id) {
    this.id = id;
  }
}
