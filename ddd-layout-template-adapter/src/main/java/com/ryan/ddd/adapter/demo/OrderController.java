package com.ryan.ddd.adapter.demo;

import com.ryan.ddd.app.demo.CreateOrderCommand;
import com.ryan.ddd.app.demo.CreateOrderHandler;
import com.ryan.ddd.app.demo.OrderReadModelStore;
import com.ryan.ddd.app.demo.OrderSummary;
import com.ryan.ddd.app.event.EventOutboxProcessor;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/orders")
public class OrderController {
  private final CreateOrderHandler createOrderHandler;
  private final OrderReadModelStore readModelStore;
  private final EventOutboxProcessor outboxProcessor;

  public OrderController(CreateOrderHandler createOrderHandler,
      OrderReadModelStore readModelStore, EventOutboxProcessor outboxProcessor) {
    this.createOrderHandler = createOrderHandler;
    this.readModelStore = readModelStore;
    this.outboxProcessor = outboxProcessor;
  }

  @PostMapping
  public ResponseEntity<String> create(@Valid @RequestBody CreateOrderRequest request) {
    String orderId = createOrderHandler.handle(
        new CreateOrderCommand(request.getOrderId(), request.getCustomerId()));
    outboxProcessor.processBatch(20);
    return ResponseEntity.ok(orderId);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderSummaryResponse> find(@PathVariable String orderId) {
    Optional<OrderSummary> summary = readModelStore.find(orderId);
    if (summary.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    OrderSummaryResponse response = new OrderSummaryResponse();
    response.setOrderId(summary.get().orderId());
    response.setCustomerId(summary.get().customerId());
    response.setCreatedAt(summary.get().createdAt());
    return ResponseEntity.ok(response);
  }
}
