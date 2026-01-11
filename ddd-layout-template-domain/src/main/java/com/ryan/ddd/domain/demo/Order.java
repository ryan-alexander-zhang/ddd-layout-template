package com.ryan.ddd.domain.demo;

import com.ryan.ddd.domain.model.AggregateRoot;
import com.ryan.ddd.domain.model.Identifier;
import java.time.Instant;
import java.util.Objects;

public class Order extends AggregateRoot {
  private final String customerId;
  private OrderStatus status;

  private Order(Identifier id, String customerId) {
    super(id);
    this.customerId = Objects.requireNonNull(customerId, "customerId");
    this.status = OrderStatus.CREATED;
  }

  public static Order create(Identifier id, String customerId) {
    Order order = new Order(id, customerId);
    order.raiseEvent(new OrderCreatedEvent(new Identifier(id.value() + ":evt"), id, customerId,
        Instant.now()));
    return order;
  }

  public String customerId() {
    return customerId;
  }

  public OrderStatus status() {
    return status;
  }

  public void confirm() {
    this.status = OrderStatus.CONFIRMED;
  }
}
