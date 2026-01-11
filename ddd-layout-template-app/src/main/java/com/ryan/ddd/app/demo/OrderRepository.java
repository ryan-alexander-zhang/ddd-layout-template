package com.ryan.ddd.app.demo;

import com.ryan.ddd.domain.demo.Order;
import com.ryan.ddd.domain.model.Identifier;
import java.util.Optional;

public interface OrderRepository {
  void save(Order order);

  Optional<Order> find(Identifier id);
}
