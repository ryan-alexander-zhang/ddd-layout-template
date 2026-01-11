package com.ryan.ddd.app.demo;

import com.ryan.ddd.app.cqrs.Command;
import java.util.Objects;

public final class CreateOrderCommand implements Command {
  private final String orderId;
  private final String customerId;

  public CreateOrderCommand(String orderId, String customerId) {
    this.orderId = Objects.requireNonNull(orderId, "orderId");
    this.customerId = Objects.requireNonNull(customerId, "customerId");
  }

  public String orderId() {
    return orderId;
  }

  public String customerId() {
    return customerId;
  }
}
