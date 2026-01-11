# ddd-layout-template

## Overview
This template provides a reusable DDD + CQRS framework with event handling, read model projection,
and a DAG workflow engine. It is structured for general-purpose business development and includes
demo implementations for quick onboarding.

## Modules
- **domain**: Base DDD abstractions (AggregateRoot, Entity, DomainEvent) and demo domain model.
- **app**: CQRS interfaces, Event Outbox, Read Model projection, Workflow engine.
- **infra**: Kafka/Redis integrations, serializer implementations, in-memory demo stores.
- **adapter**: REST APIs demonstrating commands, read models, and workflow usage.
- **start**: Spring Boot application entry and demo wiring.

## Demo Usage
1. Start the Spring Boot app (`ddd-layout-template-start`).
2. Create an order (command + event + read model):
   - `POST /demo/orders`
   - Body: `{ "orderId": "order-1", "customerId": "customer-1" }`
3. Query read model:
   - `GET /demo/orders/order-1`
4. Trigger workflow engine:
   - `POST /demo/workflows/sync`
   - `POST /demo/workflows/async`

## Event Handling (Outbox + Idempotency)
- Use `EventOutboxService` to persist events within the same transaction as aggregates.
- Use `EventOutboxProcessor` to publish events to Kafka and dispatch local read model handlers.
- Configure `EventDedupStore` (e.g., Redis) for distributed idempotency.

## Workflow Engine (DAG)
- Define nodes and dependencies in `WorkflowDefinition`.
- Execute synchronously or asynchronously via `WorkflowEngine`.
- Persist execution state via `WorkflowStore` for retries and observability.

## Dependencies
This template uses Spring Boot, MyBatis-Plus, Redis (Redisson), and Kafka. See module `pom.xml`
files for details and adjust the parent `pom.xml` if you introduce additional dependencies.

## Archetype Build
```shell
mvn archetype:create-from-project

mkdir -p /tmp/ddd-demo-gen && cd /tmp/ddd-demo-gen

mvn archetype:generate \
  -DarchetypeGroupId=com.ryan.ddd \
  -DarchetypeArtifactId=ddd-layout-template-archetype \
  -DarchetypeVersion=0.0.1-SNAPSHOT \
  -DgroupId=ai.saharalabs.hive \
  -DartifactId=hive-server \
  -Dversion=0.0.1-SNAPSHOT \
  -Dpackage=ai.saharalabs.hive \
  -DinteractiveMode=false
```
