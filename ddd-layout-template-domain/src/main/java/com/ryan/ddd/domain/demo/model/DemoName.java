package com.ryan.ddd.domain.demo.model;

import com.ryan.ddd.domain.common.exception.DomainException;
import java.util.Objects;
import lombok.Getter;

@Getter
public final class DemoName {

  private final String value;

  private DemoName(String value) {
    this.value = value;
  }

  public static DemoName of(String value) {
    String v = Objects.requireNonNull(value, "name").trim();
    if (v.isEmpty()) {
      throw new DomainException("name must not be blank");
    }
    return new DemoName(v);
  }
}
