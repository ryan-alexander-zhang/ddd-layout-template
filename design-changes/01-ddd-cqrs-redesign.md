# DDD + CQRS Framework Redesign Record

## Why the redesign
- 原始工程几乎没有任何可用的 DDD/CQRS 代码结构，需要补齐基础能力。
- 事件处理缺少分布式一致性与幂等治理，需要引入 Outbox + Dedup 的可持久化模型。
- 缺少 Read Model 与 Workflow 引擎，无法覆盖常见 DDD/CQRS 使用方式。
- 需要提供可复用的模块化模板与 Demo，方便业务接入。

## What changed
1. **Domain 层**：新增 AggregateRoot、Entity、DomainEvent 等基础建模抽象。
2. **Application 层**：新增 CQRS 接口、事件总线与 Outbox 处理流程、Read Model 处理器、DAG Workflow 引擎。
3. **Infrastructure 层**：新增可替换的持久化实现（内存 Demo 版本）、Kafka 发布、Redis 幂等存储（Redisson）。
4. **Adapter/Start 层**：新增 REST Demo，展示命令、事件、Read Model 与 Workflow 使用方式。

## How to use (template guidance)
- **定义领域模型**：在 `ddd-layout-template-domain` 中定义 Aggregate、Value Object 与 DomainEvent。
- **编排应用服务**：在 `ddd-layout-template-app` 中实现 Command/Query Handler，并使用 `EventOutboxService` 收集事件。
- **事件处理**：使用 `EventOutboxProcessor`（支持同步/异步）+ `EventDedupStore` 做幂等处理。
- **Read Model**：实现 `EventHandler` 或 `ReadModelUpdater`，在 `SimpleEventDispatcher` 中注册。
- **DAG Workflow**：使用 `WorkflowDefinition` 声明节点与依赖，调用 `WorkflowEngine.startSync/startAsync`。
- **Demo 运行**：在 `ddd-layout-template-start` 中查看 Demo 配置与控制器，替换为业务实现即可。

## Notes
- 所有新增功能均提供 Demo：订单创建事件、Read Model 更新、Workflow 执行。
- 目前提供 InMemory 实现，业务可在 Infra 层替换为 MyBatis/Redis/Kafka 的真实存储。
