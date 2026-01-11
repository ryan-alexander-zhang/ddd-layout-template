package com.ryan.ddd.start.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.ddd.app.demo.CreateOrderHandler;
import com.ryan.ddd.app.demo.OrderCreatedReadModelUpdater;
import com.ryan.ddd.app.demo.OrderReadModelStore;
import com.ryan.ddd.app.demo.OrderRepository;
import com.ryan.ddd.app.event.EventBus;
import com.ryan.ddd.app.event.EventDedupStore;
import com.ryan.ddd.app.event.EventDispatcher;
import com.ryan.ddd.app.event.EventOutboxProcessor;
import com.ryan.ddd.app.event.EventOutboxRepository;
import com.ryan.ddd.app.event.EventOutboxService;
import com.ryan.ddd.app.event.EventSerializer;
import com.ryan.ddd.app.event.SimpleEventDispatcher;
import com.ryan.ddd.app.workflow.RetryPolicy;
import com.ryan.ddd.app.workflow.WorkflowDefinition;
import com.ryan.ddd.app.workflow.WorkflowEngine;
import com.ryan.ddd.app.workflow.WorkflowNode;
import com.ryan.ddd.app.workflow.WorkflowStore;
import com.ryan.ddd.infra.demo.InMemoryOrderReadModelStore;
import com.ryan.ddd.infra.demo.InMemoryOrderRepository;
import com.ryan.ddd.infra.event.InMemoryEventDedupStore;
import com.ryan.ddd.infra.event.InMemoryEventOutboxRepository;
import com.ryan.ddd.infra.event.JacksonEventSerializer;
import com.ryan.ddd.infra.event.LoggingEventBus;
import com.ryan.ddd.infra.workflow.InMemoryWorkflowStore;
import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfiguration {
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public EventSerializer eventSerializer(ObjectMapper objectMapper) {
    return new JacksonEventSerializer(objectMapper);
  }

  @Bean
  public EventOutboxRepository eventOutboxRepository() {
    return new InMemoryEventOutboxRepository();
  }

  @Bean
  public EventBus eventBus() {
    return new LoggingEventBus();
  }

  @Bean
  public EventDedupStore eventDedupStore() {
    return new InMemoryEventDedupStore();
  }

  @Bean
  public EventDispatcher eventDispatcher(EventSerializer serializer,
      OrderCreatedReadModelUpdater updater) {
    SimpleEventDispatcher dispatcher = new SimpleEventDispatcher(serializer);
    dispatcher.registerHandler(updater);
    return dispatcher;
  }

  @Bean
  public EventOutboxService eventOutboxService(EventOutboxRepository repository,
      EventSerializer serializer) {
    return new EventOutboxService(repository, serializer, Clock.systemUTC());
  }

  @Bean
  public Executor workflowExecutor() {
    return Executors.newFixedThreadPool(4);
  }

  @Bean
  public EventOutboxProcessor eventOutboxProcessor(EventOutboxRepository repository,
      EventBus eventBus, EventDispatcher dispatcher, EventDedupStore dedupStore,
      Executor workflowExecutor) {
    return new EventOutboxProcessor(repository, eventBus, dispatcher, dedupStore,
        Duration.ofMinutes(10), Duration.ofSeconds(5), Clock.systemUTC(), workflowExecutor);
  }

  @Bean
  public OrderRepository orderRepository() {
    return new InMemoryOrderRepository();
  }

  @Bean
  public OrderReadModelStore orderReadModelStore() {
    return new InMemoryOrderReadModelStore();
  }

  @Bean
  public OrderCreatedReadModelUpdater orderCreatedReadModelUpdater(
      OrderReadModelStore store) {
    return new OrderCreatedReadModelUpdater(store);
  }

  @Bean
  public CreateOrderHandler createOrderHandler(OrderRepository repository,
      EventOutboxService outboxService) {
    return new CreateOrderHandler(repository, outboxService);
  }

  @Bean
  public WorkflowStore workflowStore() {
    return new InMemoryWorkflowStore();
  }

  @Bean
  public WorkflowEngine workflowEngine(WorkflowStore workflowStore, Executor workflowExecutor) {
    return new WorkflowEngine(workflowStore, new RetryPolicy(2, Duration.ofSeconds(1)),
        Clock.systemUTC(), workflowExecutor);
  }

  @Bean
  public WorkflowDefinition demoWorkflow() {
    WorkflowNode validate = new WorkflowNode("validate", "Validate", context -> {
      context.put("validated", true);
      return com.ryan.ddd.app.workflow.NodeResult.success("validated");
    });
    WorkflowNode reserve = new WorkflowNode("reserve", "Reserve", context -> {
      context.put("reserved", true);
      return com.ryan.ddd.app.workflow.NodeResult.success("reserved");
    });
    WorkflowNode notify = new WorkflowNode("notify", "Notify", context -> {
      context.put("notified", true);
      return com.ryan.ddd.app.workflow.NodeResult.success("notified");
    });

    return WorkflowDefinition.builder("demo-workflow")
        .addNode(validate)
        .addNode(reserve)
        .addNode(notify)
        .dependsOn("reserve", "validate")
        .dependsOn("notify", "reserve")
        .build();
  }
}
