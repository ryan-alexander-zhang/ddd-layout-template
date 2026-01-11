package com.ryan.ddd.app.demo;

import java.util.Optional;

public interface OrderReadModelStore {
  void save(OrderSummary summary);

  Optional<OrderSummary> find(String orderId);
}
