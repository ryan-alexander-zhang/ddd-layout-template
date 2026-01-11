package com.ryan.ddd.app.demo;

import com.ryan.ddd.app.cqrs.CommandHandler;
import com.ryan.ddd.app.event.EventOutboxService;
import com.ryan.ddd.domain.demo.Order;
import com.ryan.ddd.domain.model.Identifier;
import java.util.Objects;

public class CreateOrderHandler implements CommandHandler<CreateOrderCommand, String> {
  private final OrderRepository repository;
  private final EventOutboxService outboxService;

  public CreateOrderHandler(OrderRepository repository, EventOutboxService outboxService) {
    this.repository = Objects.requireNonNull(repository, "repository");
    this.outboxService = Objects.requireNonNull(outboxService, "outboxService");
  }

  @Override
  public String handle(CreateOrderCommand command) {
    Order order = Order.create(new Identifier(command.orderId()), command.customerId());
    repository.save(order);
    outboxService.collectAndSave(order);
    return order.id().value();
  }
}
