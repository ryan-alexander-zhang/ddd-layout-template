package com.ryan.ddd.app.demo;

import java.time.Instant;
import java.util.Objects;

public final class OrderSummary {
  private final String orderId;
  private final String customerId;
  private final Instant createdAt;

  public OrderSummary(String orderId, String customerId, Instant createdAt) {
    this.orderId = Objects.requireNonNull(orderId, "orderId");
    this.customerId = Objects.requireNonNull(customerId, "customerId");
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
  }

  public String orderId() {
    return orderId;
  }

  public String customerId() {
    return customerId;
  }

  public Instant createdAt() {
    return createdAt;
  }
}
