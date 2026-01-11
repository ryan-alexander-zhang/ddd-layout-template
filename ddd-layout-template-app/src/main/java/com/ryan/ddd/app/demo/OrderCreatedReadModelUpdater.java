package com.ryan.ddd.app.demo;

import com.ryan.ddd.app.event.EventHandler;
import com.ryan.ddd.domain.demo.OrderCreatedEvent;
import java.util.Objects;

public class OrderCreatedReadModelUpdater implements EventHandler<OrderCreatedEvent> {
  private final OrderReadModelStore store;

  public OrderCreatedReadModelUpdater(OrderReadModelStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  @Override
  public String eventType() {
    return "order.created";
  }

  @Override
  public Class<OrderCreatedEvent> eventClass() {
    return OrderCreatedEvent.class;
  }

  @Override
  public void handle(OrderCreatedEvent event) {
    OrderSummary summary = new OrderSummary(event.aggregateId().value(), event.customerId(),
        event.occurredAt());
    store.save(summary);
  }
}
