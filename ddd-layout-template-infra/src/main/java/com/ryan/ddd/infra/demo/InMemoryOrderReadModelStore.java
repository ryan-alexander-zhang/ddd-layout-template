package com.ryan.ddd.infra.demo;

import com.ryan.ddd.app.demo.OrderReadModelStore;
import com.ryan.ddd.app.demo.OrderSummary;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderReadModelStore implements OrderReadModelStore {
  private final ConcurrentHashMap<String, OrderSummary> store = new ConcurrentHashMap<>();

  @Override
  public void save(OrderSummary summary) {
    store.put(summary.orderId(), summary);
  }

  @Override
  public Optional<OrderSummary> find(String orderId) {
    return Optional.ofNullable(store.get(orderId));
  }
}
