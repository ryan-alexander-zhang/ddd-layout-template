package com.ryan.ddd.infra.demo;

import com.ryan.ddd.app.demo.OrderRepository;
import com.ryan.ddd.domain.demo.Order;
import com.ryan.ddd.domain.model.Identifier;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepository {
  private final ConcurrentHashMap<String, Order> store = new ConcurrentHashMap<>();

  @Override
  public void save(Order order) {
    store.put(order.id().value(), order);
  }

  @Override
  public Optional<Order> find(Identifier id) {
    return Optional.ofNullable(store.get(id.value()));
  }
}
