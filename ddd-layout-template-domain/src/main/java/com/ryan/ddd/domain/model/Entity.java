package com.ryan.ddd.domain.model;

import java.util.Objects;

public abstract class Entity {
  private final Identifier id;

  protected Entity(Identifier id) {
    this.id = Objects.requireNonNull(id, "id");
  }

  public Identifier id() {
    return id;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Entity)) {
      return false;
    }
    Entity that = (Entity) other;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
