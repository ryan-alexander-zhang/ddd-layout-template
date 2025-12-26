---
description: 'Guidelines for building Java base applications'
applyTo: '**/*.java'
---

How to compile: `mvn clean compile` in the root directory.

## Part 1: YAML Directory Structure and Explanation

```yaml
project:
  root: ${input:projectName:ddd-layout}
  basePackage: ${input:basePackage:com.erpang.ddd}
  architecture: "DDD + Spring Boot + CQRS + Outbox/Inbox"

  # Naming placeholders
  # - <context>: bounded context name (e.g., demo). Keep stable and consistent.

modules:
  - name: start
    path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-start"
    purpose: >
      Spring Boot entrypoint + bootstrap wiring/config.
      Only keep wiring, bean registration, and event type registration here.
    dependsOn:
      - adapter
      - infra
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.start"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-start/src/main/java/${input:basePackage:com.erpang.ddd}/start"
        purpose: "Spring Boot main application."
        allowedContents:
          - "Spring Boot @SpringBootApplication main class"
          - "Scheduling / mapper scanning / application runtime bootstrap"
        forbiddenContents:
          - "Business/domain logic"
          - "Use-case orchestration (belongs in app)"
          - "Persistence implementations (belongs in infra)"
        namingConventions:
          - "*Application for the boot class"
        dependenciesAllowed:
          - "Spring Boot"
          - "${input:basePackage:com.erpang.ddd}.start.bootstrap.config"

      - name: "${input:basePackage:com.erpang.ddd}.start.bootstrap.config"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-start/src/main/java/${input:basePackage:com.erpang.ddd}/start/bootstrap/config"
        purpose: "Bootstrap and configuration: wire app+infra beans and register event types."
        allowedContents:
          - "@Configuration classes: bean wiring only"
          - "Event type registration (e.g., OutboxEventTypeRegistry)"
          - "Cross-cutting bean registration (e.g., HandlerRegistry, InboxGuard)"
        forbiddenContents:
          - "Business logic"
          - "Domain invariants"
          - "Transport/controller code"
        namingConventions:
          - "*Config"
          - "*WiringConfig"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.adapter..* (for wiring only)"
          - "${input:basePackage:com.erpang.ddd}.app..* (for wiring only)"
          - "${input:basePackage:com.erpang.ddd}.infra..* (for wiring only)"
          - "${input:basePackage:com.erpang.ddd}.domain..* (for event type constants/classes only)"
          - "Spring Framework"

  - name: adapter
    path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-adapter"
    purpose: "HTTP/API boundary (Spring MVC) for command/query endpoints; maps transport DTOs to app layer DTOs."
    dependsOn:
      - app
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/adapter/<context>/api/command"
        purpose: "Command-side HTTP controllers (write model)."
        allowedContents:
          - "@RestController for write endpoints"
          - "Boundary validation (JSR 303)"
          - "Map request DTOs -> app command DTOs via assembler"
          - "Call app command handlers"
        forbiddenContents:
          - "Direct domain access"
          - "Direct infra access"
          - "Business logic"
        namingConventions:
          - "*CommandController"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.app..*"
          - "${input:basePackage:com.erpang.ddd}.<context>.adapter.assembler..*"
          - "Spring Web / Validation"

      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command.request"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/adapter/<context>/api/command/request"
        purpose: "REST requests for command endpoints (JSR 303 validation)."
        allowedContents:
          - "Request DTOs"
        forbiddenContents:
          - "Domain/infra types"
        namingConventions:
          - "*Request"
        dependenciesAllowed:
          - "Jakarta Validation"

      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command.response"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/adapter/<context>/api/command/response"
        purpose: "REST responses for command endpoints (ack/ids/metadata; not read views)."
        allowedContents:
          - "Response DTOs"
        forbiddenContents:
          - "Query/read view models"
          - "Domain/infra types"
        namingConventions:
          - "*Response"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.api.query"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/adapter/<context>/api/query"
        purpose: "Query-side HTTP controllers (read model)."
        allowedContents:
          - "@RestController for read endpoints"
          - "Map request params -> app query DTOs"
          - "Call app query handlers"
        forbiddenContents:
          - "Writes/side effects"
          - "Direct domain access"
          - "Direct infra access"
        namingConventions:
          - "*QueryController"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.app..*"
          - "${input:basePackage:com.erpang.ddd}.<context>.adapter.assembler..*"
          - "Spring Web"

      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.assembler"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/adapter/<context>/assembler"
        purpose: "Assembler/mapping utilities between adapter DTOs and app DTOs/views."
        allowedContents:
          - "*Assembler classes"
        forbiddenContents:
          - "Business logic"
        namingConventions:
          - "*Assembler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.app..*"

  - name: app
    path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app"
    purpose: "Application layer use-cases split into command/query; orchestrates domain and persists events via outbox."
    dependsOn:
      - domain
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.common"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/common"
        purpose: "Cross-cutting application abstractions (handlers, event handler registry, idempotency guard)."
        allowedContents:
          - "CommandHandler interface"
          - "EventHandler + HandlerRegistry"
          - "InboxGuard"
        forbiddenContents:
          - "Domain-specific logic"
          - "Infrastructure implementations"
        namingConventions:
          - "*Handler, *Registry"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common..*"
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.command"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/command"
        purpose: "Application command (write) use-cases."
        allowedContents:
          - "command.dto"
          - "command.handler"
        forbiddenContents:
          - "Transport/web concerns"
          - "Infrastructure detail types"
        namingConventions:
          - "*Command, *Result, *CommandHandler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain..*"
          - "Spring Tx (allowed only for @Transactional)"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.command.dto"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/command/dto"
        purpose: "Technology-agnostic command input/output DTOs."
        allowedContents:
          - "*Command"
          - "*Result"
        forbiddenContents:
          - "Business logic"
          - "Framework annotations"
        namingConventions:
          - "*Command, *Result"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.command.handler"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/command/handler"
        purpose: "Orchestrate domain operations; define transactions; persist outbox events via ports."
        allowedContents:
          - "Thin orchestration"
          - "@Transactional boundary"
          - "Create domain events and append EventEnvelope to OutboxRepository"
        forbiddenContents:
          - "Domain business rules (belong in domain model)"
          - "Infra mappers/POs"
          - "Spring stereotypes (@Service/@Component) if wiring is done in start"
        namingConventions:
          - "*CommandHandler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain..*"
          - "${input:basePackage:com.erpang.ddd}.domain.common.outbox..*"
          - "${input:basePackage:com.erpang.ddd}.domain.common.event..*"
          - "${input:basePackage:com.erpang.ddd}.common..*"
          - "Spring Tx (allowed only for @Transactional)"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.query"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query"
        purpose: "Application query (read) use-cases; side-effect free."
        allowedContents:
          - "query.dto"
          - "query.handler"
          - "query.port (read ports)"
          - "query.view"
          - "query.readmodel (optional, for projections)"
          - "query.repository (optional, for projector persistence)"
        forbiddenContents:
          - "Writes/side effects"
          - "Infra mappers/POs"
        namingConventions:
          - "*Query, *QueryHandler, *View"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain..* (only for IDs/value objects if needed)"
          - "${input:basePackage:com.erpang.ddd}.common..*"
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.query.dto"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/dto"
        purpose: "Query DTOs representing incoming read requests."
        allowedContents:
          - "*Query"
        forbiddenContents:
          - "Business logic"
        namingConventions:
          - "*Query"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.query.handler"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/handler"
        purpose: "Read-only handlers; orchestrate calls to query ports."
        allowedContents:
          - "Side-effect free handlers"
        forbiddenContents:
          - "Writes"
          - "Infra types"
        namingConventions:
          - "*QueryHandler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.app.<context>.query.port..*"
          - "${input:basePackage:com.erpang.ddd}.app.<context>.query.view..*"
          - "${input:basePackage:com.erpang.ddd}.app.<context>.query.dto..*"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.query.port"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/port"
        purpose: "Read ports (interfaces) for queries. Infra provides implementations."
        allowedContents:
          - "*Queries interfaces"
        forbiddenContents:
          - "Framework annotations"
          - "Persistence types"
        namingConventions:
          - "*Queries"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.query.view"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/view"
        purpose: "View models for query responses (presentation-optimized, read-only)."
        allowedContents:
          - "*View"
        forbiddenContents:
          - "Expose domain aggregates directly"
        namingConventions:
          - "*View"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.query.readmodel"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/readmodel"
        purpose: "Optional read model structures for projections/projectors (CQRS)."
        allowedContents:
          - "Read model representations"
        forbiddenContents:
          - "Domain business rules"
        namingConventions:
          - "*ReadModel (if used)"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.query.repository"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/repository"
        purpose: "Optional repository abstractions for projector/readmodel persistence (if needed)."
        allowedContents:
          - "Repository interfaces for read model persistence"
        forbiddenContents:
          - "Infra mappers/POs"
        namingConventions:
          - "*Repository"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.app.<context>.event"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/event"
        purpose: "Application event handlers reacting to domain events (business reaction)."
        allowedContents:
          - "Event handler implementations"
        forbiddenContents:
          - "Infrastructure listener mechanisms"
        namingConventions:
          - "*EventHandler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.event..*"
          - "${input:basePackage:com.erpang.ddd}.domain.<context>.event..*"
          - "${input:basePackage:com.erpang.ddd}.common.event..*"

  - name: domain
    path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-domain"
    purpose: "Pure domain model (aggregates/value objects/domain events) + ports for outbox/inbox. Framework-agnostic."
    dependsOn: []
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.domain.common.event"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/common/event"
        purpose: "Domain event contracts and EventEnvelope."
        allowedContents:
          - "DomainEvent"
          - "EventEnvelope<T>"
        forbiddenContents:
          - "Spring/MyBatis/JPA code"
        namingConventions:
          - "*Event"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.domain.common.outbox"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/common/outbox"
        purpose: "Outbox port + status model for reliable publication."
        allowedContents:
          - "OutboxRepository"
          - "OutboxStatus"
        forbiddenContents:
          - "Infrastructure persistence"
        namingConventions:
          - "Outbox*"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.event"
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.domain.common.inbox"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/common/inbox"
        purpose: "Inbox port + status model for idempotent consumption."
        allowedContents:
          - "InboxRepository"
          - "InboxStatus"
        forbiddenContents:
          - "Infrastructure persistence"
        namingConventions:
          - "Inbox*"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.domain.<context>.model"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/<context>/model"
        purpose: "Aggregates + value objects + invariants."
        allowedContents:
          - "Aggregate roots"
          - "Value objects"
          - "Invariant checks"
        forbiddenContents:
          - "Framework annotations"
          - "Application orchestration"
        namingConventions:
          - "*Id, *Name"
        dependenciesAllowed:
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.domain.<context>.event"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/<context>/event"
        purpose: "Domain event definitions (types)."
        allowedContents:
          - "*Event"
        forbiddenContents:
          - "Publishing mechanisms"
        namingConventions:
          - "*CreatedEvent, *UpdatedEvent"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.event"
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.domain.<context>.repository"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/<context>/repository"
        purpose: "Domain repository ports (interfaces)."
        allowedContents:
          - "*Repository interfaces"
        forbiddenContents:
          - "Implementations"
        namingConventions:
          - "*Repository"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.<context>.model"

  - name: infra
    path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra"
    purpose: "Infrastructure implementations: persistence (MyBatis), outbox publisher, inbox dispatcher, Spring event bridge."
    dependsOn:
      - app
      - domain
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.infra.common.persistence"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/common/persistence"
        purpose: "Infra persistence utilities (type handlers etc.)."
        allowedContents:
          - "Type handlers and DB-specific helpers"
        forbiddenContents:
          - "Domain logic"
        namingConventions:
          - "*TypeHandler"
        dependenciesAllowed:
          - "MyBatis / MyBatis-Plus"
          - "JDK"

      - name: "${input:basePackage:com.erpang.ddd}.infra.common.outbox"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/common/outbox"
        purpose: "Outbox persistence + publisher job/service + serialization + type registry."
        allowedContents:
          - "OutboxRepository implementation"
          - "Outbox publish job/service"
          - "Event serialization and storage model"
          - "OutboxEventTypeRegistry"
        forbiddenContents:
          - "Adapter/web"
          - "Domain model mutation"
        namingConventions:
          - "Outbox*"
          - "*Job"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.outbox"
          - "${input:basePackage:com.erpang.ddd}.domain.common.event"
          - "Spring Framework / Scheduling"
          - "MyBatis / MyBatis-Plus"

      - name: "${input:basePackage:com.erpang.ddd}.infra.common.inbox"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/common/inbox"
        purpose: "Inbox persistence + dispatcher (routes to app handlers via HandlerRegistry)."
        allowedContents:
          - "InboxRepository implementation"
          - "InboxEventDispatcher"
        forbiddenContents:
          - "Business logic"
        namingConventions:
          - "Inbox*"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.inbox"
          - "${input:basePackage:com.erpang.ddd}.common.event"
          - "Spring Framework"
          - "MyBatis / MyBatis-Plus"

      - name: "${input:basePackage:com.erpang.ddd}.infra.common.event"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/common/event"
        purpose: "Spring bridge: listens for EventEnvelope and forwards to inbox dispatcher."
        allowedContents:
          - "Spring @EventListener bridge components"
        forbiddenContents:
          - "Domain event definitions"
        namingConventions:
          - "*Listener"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.event"
          - "${input:basePackage:com.erpang.ddd}.infra.common.inbox"
          - "Spring Framework"

      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.write"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/persistence/write"
        purpose: "Write-side persistence implementations for an aggregate."
        allowedContents:
          - "Repository implementations of domain ports"
          - "Write mappers and POs"
        forbiddenContents:
          - "Adapter/web"
          - "Expose POs outside infra"
        namingConventions:
          - "*RepositoryImpl, *Mapper, *PO"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.<context>.repository"
          - "${input:basePackage:com.erpang.ddd}.domain.<context>.model"
          - "MyBatis / MyBatis-Plus"
          - "Spring"

      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.read"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/persistence/read"
        purpose: "Read-side persistence implementations for query ports."
        allowedContents:
          - "*QueriesImpl"
          - "Read mappers and POs"
        forbiddenContents:
          - "Writes/side effects"
          - "Expose mapper/PO types outside infra"
        namingConventions:
          - "*QueriesImpl"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.app.<context>.query.port (port interfaces)"
          - "${input:basePackage:com.erpang.ddd}.app.<context>.query.view"
          - "MyBatis / MyBatis-Plus"
          - "Spring"

      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.listener"
        path: "${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/listener"
        purpose: "Event listeners/subscribers (infra mechanism) that delegate to app event handlers with explicit idempotency/error handling."
        allowedContents:
          - "Listener components"
          - "Idempotency guard usage"
          - "Delegation to app event handlers"
        forbiddenContents:
          - "Domain business rules"
          - "Adapter dependencies"
        namingConventions:
          - "*EventListener"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.common.event..*"
          - "${input:basePackage:com.erpang.ddd}.app.<context>.event..*"
          - "${input:basePackage:com.erpang.ddd}.domain.common.event..*"
          - "Spring Framework"
```

## Part 2: Dependency Order and Call Flows

### Dependency Direction

Modules:
- `${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-domain` must not depend on any other module.
- `${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-app` depends only on `domain`.
- `${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-adapter` depends only on `app`.
- `${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-infra` depends on `domain` and `app`.
- `${input:projectName:ddd-layout}/${input:projectName:ddd-layout}-start` depends on `adapter` and `infra`.

Key package rules:
- `${input:basePackage:com.erpang.ddd}.<context>.adapter..*` may depend on `${input:basePackage:com.erpang.ddd}.app..*` only.
- `${input:basePackage:com.erpang.ddd}.app..*` may depend on `${input:basePackage:com.erpang.ddd}.domain..*` (ports/models). It must not depend on infra.
- `${input:basePackage:com.erpang.ddd}.domain..*` is framework-agnostic: no Spring, no MyBatis/JPA, no persistence annotations.
- `${input:basePackage:com.erpang.ddd}.infra..*` may depend on app+domain to implement ports; frameworks are allowed here.
- `${input:basePackage:com.erpang.ddd}.start.bootstrap.config..*` is wiring-only: bean creation and event type registration. No business logic.

### Read Path (Query)

1. Adapter query controller: `${input:basePackage:com.erpang.ddd}.<context>.adapter.api.query.*QueryController`
2. Map request -> app query DTO: `${input:basePackage:com.erpang.ddd}.app.<context>.query.dto.*Query`
3. Call app query handler: `${input:basePackage:com.erpang.ddd}.app.<context>.query.handler.*QueryHandler`
4. Handler calls query port interface: `${input:basePackage:com.erpang.ddd}.app.<context>.query.port.*Queries`
5. Infra implements query ports via read persistence: `${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.read.*QueriesImpl`
6. Return view model: `${input:basePackage:com.erpang.ddd}.app.<context>.query.view.*View` -> assembled into adapter response.

### Write Path (Command)

1. Adapter command controller: `${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command.*CommandController`
2. Validate request DTO: `${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command.request.*Request`
3. Assemble -> app command DTO: `${input:basePackage:com.erpang.ddd}.app.<context>.command.dto.*Command`
4. Call command handler: `${input:basePackage:com.erpang.ddd}.app.<context>.command.handler.*CommandHandler` (transaction boundary)
5. Handler uses domain model + ports:
    - Domain model: `${input:basePackage:com.erpang.ddd}.domain.<context>.model..*`
    - Domain repo port: `${input:basePackage:com.erpang.ddd}.domain.<context>.repository..*`
6. Infra implements persistence:
    - write: `${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.write..*`
7. Handler creates a domain event (type defined in domain), then persists it to outbox:
    - Domain event type: `${input:basePackage:com.erpang.ddd}.domain.<context>.event.*Event`
    - Envelope: `${input:basePackage:com.erpang.ddd}.domain.common.event.EventEnvelope`
    - Outbox append via port: `${input:basePackage:com.erpang.ddd}.domain.common.outbox.OutboxRepository`

### Event Publish Flow (Outbox)

- Application command handler writes `EventEnvelope` to outbox via the domain outbox port.
- Infra outbox publisher job claims + publishes events asynchronously.
- Bootstrap registers `(eventType -> eventClass)` mappings into `OutboxEventTypeRegistry`.

Event type registration rule (matches template):
- Put event-type registration in `${input:basePackage:com.erpang.ddd}.start.bootstrap.config.*OutboxEventTypesConfig`.
- The config must reference:
    - the domain event type constant/class (e.g., `DemoCreatedEvent.TYPE`, `DemoCreatedEvent.class`)
    - `OutboxEventTypeRegistry` from infra
- The registration must happen at startup (e.g., `@PostConstruct`).

### Event Subscribe Flow (Inbox)

- Infra receives `EventEnvelope<?>` (via framework mechanism).
- Infra forwards to inbox dispatcher.
- Inbox guard ensures idempotency (based on a stable `consumerId` contract).
- Business reactions live in app event handlers (`app.<context>.event`).

### Forbidden Dependencies (Examples)

1. Adapter importing infra.
2. Adapter importing domain.
3. Domain importing Spring/MyBatis/JPA.
4. App importing infra implementations.
5. Infra exposing mapper/PO types in app/domain public APIs.
6. start/bootstrap config containing business logic instead of wiring/registration.

### Testing Guidance (keep aligned with current repo)

- Domain: unit test value objects/invariants in `domain.<context>.model`.
- App: unit test command/query handlers by mocking/faking domain ports (repositories, outbox/inbox ports).
- Infra: keep persistence tests minimal; prefer integration tests if/when DB containers are introduced.

## Part 3: Example Code (Minimal but Complete)

> Note: This mirrors the template’s conventions. Replace `<context>` with your bounded context name (e.g., `demo`).

### 1) Outbox event type registration (start/bootstrap)

```java
package ${input:basePackage:com.erpang.ddd}.start.bootstrap.config;

import ${input:basePackage:com.erpang.ddd}.domain.<context>.event.DemoCreatedEvent;
import ${input:basePackage:com.erpang.ddd}.infra.common.outbox.OutboxEventTypeRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxEventTypesConfig {

  private final OutboxEventTypeRegistry registry;

  public OutboxEventTypesConfig(OutboxEventTypeRegistry registry) {
    this.registry = registry;
  }

  @PostConstruct
  public void register() {
    registry.register(DemoCreatedEvent.TYPE, DemoCreatedEvent.class);
  }
}
```

### 2) Query endpoint -> query handler -> query port

```java
package ${input:basePackage:com.erpang.ddd}.<context>.adapter.api.query;

import ${input:basePackage:com.erpang.ddd}.app.<context>.query.dto.GetDemoDetailQuery;
import ${input:basePackage:com.erpang.ddd}.app.<context>.query.handler.GetDemoDetailQueryHandler;
import ${input:basePackage:com.erpang.ddd}.common.SingleResponse;
import ${input:basePackage:com.erpang.ddd}.<context>.adapter.assembler.DemoApiAssembler;
import ${input:basePackage:com.erpang.ddd}.<context>.adapter.api.query.response.GetDemoDetailResponse;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoQueryController {

  private final GetDemoDetailQueryHandler handler;

  public DemoQueryController(GetDemoDetailQueryHandler handler) {
    this.handler = handler;
  }

  @GetMapping("/{id}/detail")
  public ResponseEntity<SingleResponse<GetDemoDetailResponse>> getDetail(@PathVariable UUID id) {
    return handler.handle(new GetDemoDetailQuery(id))
        .map(DemoApiAssembler::toResponse)
        .map(SingleResponse::of)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
```

### 3) Command handler -> domain -> outbox

```java
package ${input:basePackage:com.erpang.ddd}.app.<context>.command.handler;

import ${input:basePackage:com.erpang.ddd}.app.<context>.command.dto.CreateDemoCommand;
import ${input:basePackage:com.erpang.ddd}.app.<context>.command.dto.CreateDemoResult;
import ${input:basePackage:com.erpang.ddd}.common.CommandHandler;
import ${input:basePackage:com.erpang.ddd}.domain.common.event.EventEnvelope;
import ${input:basePackage:com.erpang.ddd}.domain.common.outbox.OutboxRepository;
import ${input:basePackage:com.erpang.ddd}.domain.<context>.event.DemoCreatedEvent;
import ${input:basePackage:com.erpang.ddd}.domain.<context>.model.Demo;
import ${input:basePackage:com.erpang.ddd}.domain.<context>.model.DemoName;
import ${input:basePackage:com.erpang.ddd}.domain.<context>.repository.DemoRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class CreateDemoCommandHandler implements CommandHandler<CreateDemoCommand, CreateDemoResult> {

  private final DemoRepository demoRepository;
  private final OutboxRepository outboxRepository;

  public CreateDemoCommandHandler(DemoRepository demoRepository, OutboxRepository outboxRepository) {
    this.demoRepository = demoRepository;
    this.outboxRepository = outboxRepository;
  }

  @Override
  @Transactional
  public CreateDemoResult handle(CreateDemoCommand command) {
    Demo demo = Demo.create(DemoName.of(command.getName()));
    demoRepository.save(demo);

    DemoCreatedEvent event = new DemoCreatedEvent(demo.getId().getValue());
    outboxRepository.append(new EventEnvelope<>(UUID.randomUUID(), event.type(), event, Instant.now()));

    return CreateDemoResult.builder().id(demo.getId().getValue()).build();
  }
}
```

