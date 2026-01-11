package com.ryan.ddd.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.Objects;

public final class Identifier implements Serializable {
  private final String value;

  @JsonCreator
  public Identifier(@JsonProperty("value") String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Identifier value cannot be blank");
    }
    this.value = value;
  }

  @JsonValue
  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Identifier)) {
      return false;
    }
    Identifier that = (Identifier) other;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
