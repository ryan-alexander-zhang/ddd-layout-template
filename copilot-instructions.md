## Part 1: YAML Directory Structure and Explanation

```yaml
project:
  root: ${input:projectName:ddd-layout}
  basePackage: ${input:basePackage:com.erpang.ddd}
  architecture: "DDD + Spring Boot + CQRS + Outbox/Inbox"

modules:
  - name: start
    path: "${input:projectName:ddd-layout}/ddd-layout-template-start"
    purpose: "Spring Boot entrypoint + bootstrap wiring/config; registers outbox event types; schedules outbox publishing."
    dependsOn:
      - adapter
      - infra
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.start"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-start/src/main/java/${input:basePackage:com.erpang.ddd}/start"
        purpose: "Spring Boot main application."
        allowedContents:
          - "Spring Boot @SpringBootApplication main class"
        forbiddenContents:
          - "Business/domain logic"
          - "Persistence implementations"
        namingConventions:
          - "*Application for the boot class"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.start.bootstrap.config"
          - "Spring Boot"
      - name: "${input:basePackage:com.erpang.ddd}.start.bootstrap.config"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-start/src/main/java/${input:basePackage:com.erpang.ddd}/start/bootstrap/config"
        purpose: "Bootstrap and configuration for application startup, including event type registration and infrastructure wiring."
        allowedContents:
          - "@Configuration classes that wire adapters/app/infra together"
          - "Event type registration (e.g., outbox event registry)"
          - "Bean composition for cross-cutting components"
        forbiddenContents:
          - "Domain invariants or business rules"
          - "Use-case orchestration (belongs in app layer)"
        namingConventions:
          - "*Config for configuration classes"
          - "*WiringConfig for bean wiring"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.adapter..*"
          - "${input:basePackage:com.erpang.ddd}.app..*"
          - "${input:basePackage:com.erpang.ddd}.infra..*"
          - "Spring Framework"

  - name: adapter
    path: "${input:projectName:ddd-layout}/ddd-layout-template-adapter"
    purpose: "HTTP/API boundary (Spring MVC) for command/query endpoints; maps transport DTOs to app layer DTOs."
    dependsOn:
      - app
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/<context>/adapter/api/command"
        purpose: "Hosts HTTP controllers that handle Command-side operations (write model)."
        allowedContents:
          - "@RestController classes for writes"
          - "Mapping request DTOs to application command DTOs"
          - "Calling application command handlers/services"
          - "Boundary validation (JSR 303) and HTTP error mapping"
        forbiddenContents:
          - "Calling infra repositories/mappers directly"
          - "Calling domain model directly"
          - "Business logic and domain rules"
        namingConventions:
          - "*CommandController for write controllers"
          - "Request DTOs live in .request, Response DTOs in .response"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.app..*"
          - "${input:basePackage:com.erpang.ddd}.<context>.adapter.assembler..*"
          - "Spring Web / Validation"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command.request"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/<context>/adapter/api/command/request"
        purpose: "REST request DTOs for command endpoints (JSR 303 validation at boundary)."
        allowedContents:
          - "Request DTOs with validation annotations"
        forbiddenContents:
          - "Domain entities/aggregates"
          - "Persistence annotations/entities"
        namingConventions:
          - "*Request"
        dependenciesAllowed:
          - "Jakarta Validation"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command.response"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/<context>/adapter/api/command/response"
        purpose: "REST response DTOs for command endpoints (acknowledgements, identifiers, metadata; not read views)."
        allowedContents:
          - "Response DTOs for commands"
        forbiddenContents:
          - "Query/read view models"
          - "Domain objects"
        namingConventions:
          - "*Response"
        dependenciesAllowed:
          - "(none beyond JDK/serialization if needed)"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.api.query"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/<context>/adapter/api/query"
        purpose: "Hosts HTTP controllers that handle Query-side operations (read model)."
        allowedContents:
          - "@RestController classes for reads"
          - "Mapping query parameters to application query objects"
          - "Calling application query handlers"
        forbiddenContents:
          - "Calling infra persistence directly"
          - "Calling domain model directly"
          - "Side effects (writes)"
        namingConventions:
          - "*QueryController for read controllers"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.app..*"
          - "${input:basePackage:com.erpang.ddd}.<context>.adapter.assembler..*"
          - "Spring Web"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.adapter.assembler"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-adapter/src/main/java/${input:basePackage:com.erpang.ddd}/<context>/adapter/assembler"
        purpose: "Transport/application mapping helpers (API assemblers)."
        allowedContents:
          - "Assembler/mapping utilities between adapter DTOs and app DTOs/views"
        forbiddenContents:
          - "Business logic"
          - "Infrastructure mappers"
        namingConventions:
          - "*Assembler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.app..*"
          - "${input:basePackage:com.erpang.ddd}.<context>.adapter.api..*"

  - name: app
    path: "${input:projectName:ddd-layout}/ddd-layout-template-app"
    purpose: "Application layer (use-cases) split into command/query; orchestrates domain and emits events via outbox."
    dependsOn:
      - domain
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.common"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/common"
        purpose: "Cross-cutting application abstractions (handlers, response wrappers, event handler registry)."
        allowedContents:
          - "Generic handler interfaces (e.g., CommandHandler)"
          - "Event handling abstractions (EventHandler, registry)"
          - "Idempotency guards for inbox/outbox usage"
        forbiddenContents:
          - "Domain-specific rules"
          - "Infrastructure implementations (DB, messaging)"
        namingConventions:
          - "*Handler for handler interfaces"
          - "*Registry for registries"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common..*"
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.command"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/command"
        purpose: "Application command layer entry package (write use-cases)."
        allowedContents:
          - "Command DTOs"
          - "Command handlers orchestrating domain"
        forbiddenContents:
          - "Transport/web concerns"
          - "Concrete persistence/mappers"
          - "Business rules that belong to domain"
        namingConventions:
          - "*Command for inputs"
          - "*Result for outputs"
          - "*CommandHandler for use-case handlers"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain..*"
          - "${input:basePackage:com.erpang.ddd}.domain.common.outbox..*"
          - "Spring Tx (transaction boundary support)"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.command.handler"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/command/handler"
        purpose: "Application command handlers orchestrating domain operations; coordinate validation, transactions, repository access."
        allowedContents:
          - "Thin orchestration handlers"
          - "Transaction demarcation"
          - "Event envelope creation and outbox append (through domain outbox port)"
        forbiddenContents:
          - "Domain business rules"
          - "Persistence mapper/entity usage"
        namingConventions:
          - "*CommandHandler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain..*"
          - "${input:basePackage:com.erpang.ddd}.domain.common.outbox..*"
          - "${input:basePackage:com.erpang.ddd}.common..*"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.command.dto"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/command/dto"
        purpose: "Command DTOs for application use-cases; technology-agnostic inputs/outputs."
        allowedContents:
          - "Command input DTOs"
          - "Command result DTOs"
        forbiddenContents:
          - "Business logic"
          - "Web/persistence framework annotations"
        namingConventions:
          - "*Command, *Result"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query"
        purpose: "Application query layer entry package (read use-cases)."
        allowedContents:
          - "Query objects, handlers, and view models"
          - "Read repository interfaces (ports)"
        forbiddenContents:
          - "Writes/side effects"
          - "Transport/web concerns"
          - "Concrete persistence"
        namingConventions:
          - "*Query for inputs"
          - "*View for outputs"
          - "*QueryHandler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain..* (only if needed for IDs/value objects)"
          - "${input:basePackage:com.erpang.ddd}.common..*"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.handler"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/handler"
        purpose: "Application query handlers; side-effect free; coordinate access to read repositories."
        allowedContents:
          - "Read-only handlers"
        forbiddenContents:
          - "Writes/transactions with mutation"
          - "Persistence implementation types"
        namingConventions:
          - "*QueryHandler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.query"
          - "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.view"
          - "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.repository"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.query"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/query"
        purpose: "Query objects representing read requests; immutable contracts for retrieving projections/views."
        allowedContents:
          - "Query objects (criteria)"
          - "Read-port interfaces (e.g., *Queries)"
        forbiddenContents:
          - "Framework annotations"
          - "Persistence entities"
        namingConventions:
          - "*Query"
          - "*Queries for read-port interfaces"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.dto"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/dto"
        purpose: "Query DTOs and result types for application read models."
        allowedContents:
          - "Query input DTOs"
        forbiddenContents:
          - "Domain logic"
        namingConventions:
          - "*Query"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.view"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/view"
        purpose: "View models for query responses, optimized for presentation."
        allowedContents:
          - "*View models"
        forbiddenContents:
          - "Exposing domain aggregates directly"
        namingConventions:
          - "*View"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.readmodel"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/readmodel"
        purpose: "Read model representations optimized for querying (CQRS)."
        allowedContents:
          - "Read model structures (if used)"
        forbiddenContents:
          - "Domain business logic"
        namingConventions:
          - "*ReadModel (if used)"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.repository"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/query/repository"
        purpose: "Read model repository interfaces (ports) and (if present) app-level implementations."
        allowedContents:
          - "Read repository interfaces used by query handlers"
        forbiddenContents:
          - "Concrete DB mappers/entities (infra-only)"
        namingConventions:
          - "*Repository or *Queries (prefer *Queries as port name)"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.event"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-app/src/main/java/${input:basePackage:com.erpang.ddd}/app/<context>/event"
        purpose: "Application-level event handlers reacting to domain events."
        allowedContents:
          - "Event handler classes implementing app-level handler abstraction"
        forbiddenContents:
          - "Infrastructure listener mechanisms"
        namingConventions:
          - "*EventHandler"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.event..*"
          - "${input:basePackage:com.erpang.ddd}.domain.<context>.event..*"
          - "${input:basePackage:com.erpang.ddd}.common.event..*"

  - name: domain
    path: "${input:projectName:ddd-layout}/ddd-layout-template-domain"
    purpose: "Pure domain model (aggregates, value objects, domain events) + ports for outbox/inbox. Framework-agnostic."
    dependsOn: []
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.domain.common.event"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/common/event"
        purpose: "Domain event contracts and envelopes (framework-agnostic)."
        allowedContents:
          - "DomainEvent interface"
          - "EventEnvelope<T>"
        forbiddenContents:
          - "Spring/JPA/MyBatis annotations"
          - "Infrastructure publishing code"
        namingConventions:
          - "*Event for domain events"
          - "EventEnvelope for transportable envelope"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.domain.common.outbox"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/common/outbox"
        purpose: "Outbox port (repository) and status model for reliable event publishing."
        allowedContents:
          - "OutboxRepository port"
          - "OutboxStatus enum"
        forbiddenContents:
          - "DB schemas/entities"
          - "Spring scheduling/publishing"
        namingConventions:
          - "Outbox*"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.event"
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.domain.common.inbox"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/common/inbox"
        purpose: "Inbox port (repository) and status model for idempotent event consumption."
        allowedContents:
          - "InboxRepository port"
          - "InboxStatus enum"
        forbiddenContents:
          - "Spring listeners"
          - "DB mappers/entities"
        namingConventions:
          - "Inbox*"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.domain.<context>.model"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/<context>/model"
        purpose: "Domain aggregate roots and value objects."
        allowedContents:
          - "Aggregates (entities), value objects, invariants"
          - "Factory methods (e.g., create)"
        forbiddenContents:
          - "Spring/JPA/MyBatis annotations"
          - "Application orchestration"
        namingConventions:
          - "Aggregate root named after concept (e.g., Demo)"
          - "*Id, *Name value objects"
        dependenciesAllowed:
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.domain.<context>.event"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/<context>/event"
        purpose: "Domain events emitted by aggregates."
        allowedContents:
          - "*Event implementations"
        forbiddenContents:
          - "Publishing mechanisms"
        namingConventions:
          - "*CreatedEvent, *UpdatedEvent etc."
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.event"
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.domain.<context>.repository"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-domain/src/main/java/${input:basePackage:com.erpang.ddd}/domain/<context>/repository"
        purpose: "Domain repository ports (interfaces) for aggregates."
        allowedContents:
          - "Repository interfaces (ports)"
        forbiddenContents:
          - "Repository implementations"
          - "MyBatis/JPA types"
        namingConventions:
          - "*Repository"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.<context>.model"

  - name: infra
    path: "${input:projectName:ddd-layout}/ddd-layout-template-infra"
    purpose: "Infrastructure implementation: persistence (MyBatis), outbox publisher job, inbox dispatcher, Spring event bridge."
    dependsOn:
      - app
      - domain
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.infra.common.persistence"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/common/persistence"
        purpose: "Infrastructure persistence utilities (e.g., MyBatis type handlers)."
        allowedContents:
          - "TypeHandlers, DB-specific utilities"
        forbiddenContents:
          - "Domain rules"
        namingConventions:
          - "*TypeHandler"
        dependenciesAllowed:
          - "MyBatis / MyBatis-Plus"
          - "JDK"
      - name: "${input:basePackage:com.erpang.ddd}.infra.common.outbox"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/common/outbox"
        purpose: "Concrete outbox persistence + scheduled publisher + event type registry and serialization."
        allowedContents:
          - "OutboxRepository implementation"
          - "Outbox publisher job/service"
          - "Outbox event serialization and mapper/PO"
          - "Event type registry (type -> class mapping)"
        forbiddenContents:
          - "Adapter/web controllers"
          - "Domain model mutation"
        namingConventions:
          - "*Outbox*"
          - "*Job for scheduled job"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.outbox"
          - "${input:basePackage:com.erpang.ddd}.domain.common.event"
          - "Spring Framework / Scheduling"
          - "MyBatis / MyBatis-Plus"
      - name: "${input:basePackage:com.erpang.ddd}.infra.common.inbox"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/common/inbox"
        purpose: "Concrete inbox persistence + dispatcher to route events to registered application handlers."
        allowedContents:
          - "InboxRepository implementation"
          - "Dispatcher that uses HandlerRegistry"
          - "Inbox mapper/PO"
        forbiddenContents:
          - "Business logic"
        namingConventions:
          - "Inbox*"
          - "*Dispatcher"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.common.inbox"
          - "${input:basePackage:com.erpang.ddd}.common.event"
          - "Spring Framework"
          - "MyBatis / MyBatis-Plus"
      - name: "${input:basePackage:com.erpang.ddd}.infra.common.event"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/common/event"
        purpose: "Framework bridge: listens for published EventEnvelope and forwards to inbox dispatcher."
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
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/persistence/write"
        purpose: "Write-side persistence implementations for an aggregate; repositories and mappers store domain state."
        allowedContents:
          - "Repository implementations of domain ports"
          - "Write mappers and POs"
        forbiddenContents:
          - "Adapter/web code"
          - "Exposing POs outside infra"
        namingConventions:
          - "*RepositoryImpl"
          - "*Mapper"
          - "*PO"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.domain.<context>.repository"
          - "${input:basePackage:com.erpang.ddd}.domain.<context>.model"
          - "MyBatis / MyBatis-Plus"
          - "Spring"
      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.write.mapper"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/persistence/write/mapper"
        purpose: "Mappers for write-side persistence; infra-only."
        allowedContents:
          - "MyBatis mapper interfaces"
        forbiddenContents:
          - "Use outside infra"
        namingConventions:
          - "*Mapper"
        dependenciesAllowed:
          - "MyBatis / MyBatis-Plus"
      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.write.po"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/persistence/write/po"
        purpose: "Persistence objects for write-side storage; infra-only."
        allowedContents:
          - "POJOs with persistence annotations (if any)"
        forbiddenContents:
          - "Domain logic"
        namingConventions:
          - "*PO"
        dependenciesAllowed:
          - "MyBatis / MyBatis-Plus"
      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.read"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/persistence/read"
        purpose: "Read-side persistence implementations for queries; returns read views/projections."
        allowedContents:
          - "Read port implementations (e.g., *QueriesImpl)"
          - "Read mappers and POs"
        forbiddenContents:
          - "Writes/side effects"
          - "Exposing mapper/PO types outside infra"
        namingConventions:
          - "*QueriesImpl"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.query (port interfaces)"
          - "${input:basePackage:com.erpang.ddd}.<context>.app.<feature>.query.view"
          - "MyBatis / MyBatis-Plus"
          - "Spring"
      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.read.mapper"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/persistence/read/mapper"
        purpose: "Mappers for read-side persistence; infra-only."
        allowedContents:
          - "MyBatis mapper interfaces"
        forbiddenContents:
          - "Use outside infra"
        namingConventions:
          - "*Mapper"
        dependenciesAllowed:
          - "MyBatis / MyBatis-Plus"
      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.read.po"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/persistence/read/po"
        purpose: "Persistence objects for read-side models; infra-only."
        allowedContents:
          - "Read POJOs mirroring read schemas"
        forbiddenContents:
          - "Business logic"
        namingConventions:
          - "*PO"
        dependenciesAllowed:
          - "MyBatis / MyBatis-Plus"
      - name: "${input:basePackage:com.erpang.ddd}.infra.<context>.listener"
        path: "${input:projectName:ddd-layout}/ddd-layout-template-infra/src/main/java/${input:basePackage:com.erpang.ddd}/infra/<context>/listener"
        purpose: "Application event listeners. App/domain publish domain events; listeners react; infra provides mechanisms (e.g., outbox/inbox)."
        allowedContents:
          - "Spring listeners / subscribers that delegate to application event handlers"
          - "Idempotency and explicit error handling"
        forbiddenContents:
          - "Domain business rules"
          - "Direct adapter/web dependencies"
        namingConventions:
          - "*EventListener"
        dependenciesAllowed:
          - "${input:basePackage:com.erpang.ddd}.common.event..*"
          - "${input:basePackage:com.erpang.ddd}.domain.common.event..*"
          - "Spring Framework"
```

## Part 2: Dependency Order and Call Flows

### Dependency Direction

*Modules*
- `${input:projectName:ddd-layout}/ddd-layout-template-domain` is the bottom layer and must not depend on any other module.
- `${input:projectName:ddd-layout}/ddd-layout-template-app` depends only on `domain`.
- `${input:projectName:ddd-layout}/ddd-layout-template-adapter` depends only on `app`.
- `${input:projectName:ddd-layout}/ddd-layout-template-infra` depends on `domain` and `app` (implements ports).
- `${input:projectName:ddd-layout}/ddd-layout-template-start` depends on `adapter` and `infra` (bootstraps/wires everything).

*Packages (key rules)*
- `${input:basePackage:com.erpang.ddd}.<context>.adapter..*` may depend on `${input:basePackage:com.erpang.ddd}.app..*`, but must not import `${input:basePackage:com.erpang.ddd}.domain..*` or `${input:basePackage:com.erpang.ddd}.infra..*`.
- `${input:basePackage:com.erpang.ddd}.app..*` may depend on `${input:basePackage:com.erpang.ddd}.domain..*` ports and models; it must not depend on `${input:basePackage:com.erpang.ddd}.infra..*`.
- `${input:basePackage:com.erpang.ddd}.domain..*` must remain framework-agnostic (no Spring/MyBatis/JPA annotations).
- `${input:basePackage:com.erpang.ddd}.infra..*` may depend on `app` + `domain` to implement ports, and on frameworks (Spring/MyBatis).
- `${input:basePackage:com.erpang.ddd}.start..*` wires beans and configuration; keep it declarative and free of business logic.

### Read Path (Query) Flow

1. **Entry point**: `${input:basePackage:com.erpang.ddd}.<context>.adapter.api.query.*QueryController`
2. **Mapping**: Controller parses path/query params and creates a query object from `${input:basePackage:com.erpang.ddd}.app.<context>.query.dto` (or directly calls a handler with query DTO)
3. **Application orchestration**: `${input:basePackage:com.erpang.ddd}.app.<context>.query.handler.*QueryHandler` calls a read port interface in `${input:basePackage:com.erpang.ddd}.app.<context>.query.query` (e.g., `*Queries`)
4. **Infrastructure access**: `${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.read.*QueriesImpl` uses infra-only mappers/POs in `.mapper` and `.po`
5. **Return**: infra returns view models in `${input:basePackage:com.erpang.ddd}.app.<context>.query.view.*View` to the handler, which returns to adapter; adapter assembles transport response DTOs.

### Write Path (Command) Flow

1. **Command entry**: `${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command.*CommandController`
2. **Mapping + validation**: request DTO in `${input:basePackage:com.erpang.ddd}.<context>.adapter.api.command.request` validated (JSR 303), mapped via `${input:basePackage:com.erpang.ddd}.<context>.adapter.assembler.*Assembler` to `${input:basePackage:com.erpang.ddd}.app.<context>.command.dto.*Command`
3. **Use case / application service**: `${input:basePackage:com.erpang.ddd}.app.<context>.command.handler.*CommandHandler` runs orchestration (often transactional)
4. **Aggregate modification**: handler creates/loads and mutates aggregates in `${input:basePackage:com.erpang.ddd}.domain.<context>.model` and enforces invariants there
5. **Persistence**: handler calls a domain repository port in `${input:basePackage:com.erpang.ddd}.domain.<context>.repository.*Repository`; infra implements it in `${input:basePackage:com.erpang.ddd}.infra.<context>.persistence.write.*RepositoryImpl`
6. **Domain event creation**: handler creates a `${input:basePackage:com.erpang.ddd}.domain.<context>.event.*Event` and wraps it in `${input:basePackage:com.erpang.ddd}.domain.common.event.EventEnvelope`
7. **Reliable publication intent**: handler appends the envelope to `${input:basePackage:com.erpang.ddd}.domain.common.outbox.OutboxRepository` (port). Infra persists it.

### Event Publish Flow

- **Created**: Domain events are instantiated in `domain.<context>.event` (in the template, event objects are created in the app command handler after saving).
- **Persisted**: The application writes an `EventEnvelope` to the outbox via the `domain.common.outbox.OutboxRepository` port.
- **Published (async)**: `${input:basePackage:com.erpang.ddd}.infra.common.outbox.OutboxPublisherJob` periodically claims a batch (fixed delay via `outbox.publisher.fixedDelayMs`) and publishes via an infra publisher.
- **Type mapping**: `${input:basePackage:com.erpang.ddd}.start.bootstrap.config.*OutboxEventTypesConfig` registers `(type -> class)` mappings in `${input:basePackage:com.erpang.ddd}.infra.common.outbox.OutboxEventTypeRegistry`.

### Event Subscribe Flow

- **Bridge**: `${input:basePackage:com.erpang.ddd}.infra.common.event.SpringDomainEventListener` listens for published `EventEnvelope<?>` and forwards it to `${input:basePackage:com.erpang.ddd}.infra.common.inbox.InboxEventDispatcher`.
- **Idempotency**: The inbox layer (domain port `InboxRepository` + app `InboxGuard`) must guard handler execution and persist consumption state.
- **Handlers**: Application handlers live in `${input:basePackage:com.erpang.ddd}.app.<context>.event` and are registered in `${input:basePackage:com.erpang.ddd}.common.event.HandlerRegistry` (wired by `${input:basePackage:com.erpang.ddd}.start.bootstrap.config.*`).
- **Allowed touches**: Subscribers may call application handlers and ports; they must not call adapter controllers or leak infra persistence types upwards.

### Forbidden Dependencies (Examples)

1. `${input:basePackage:com.erpang.ddd}.<context>.adapter.api.*` importing `${input:basePackage:com.erpang.ddd}.infra..*`.
2. `${input:basePackage:com.erpang.ddd}.<context>.adapter.api.*` importing `${input:basePackage:com.erpang.ddd}.domain..*` (adapter must go through app).
3. `${input:basePackage:com.erpang.ddd}.domain..*` importing any Spring (`org.springframework.*`) or MyBatis/JPA annotations.
4. `${input:basePackage:com.erpang.ddd}.app..*` constructing/using `${input:basePackage:com.erpang.ddd}.infra..*` repository implementations directly (must depend on ports).
5. `${input:basePackage:com.erpang.ddd}.infra..*` exposing `.po` or `.mapper` types in method signatures of app/domain interfaces.
6. `${input:basePackage:com.erpang.ddd}.start..*` containing business logic or domain invariants (bootstrap only).

## Part 3: Example Code (Minimal but Complete)

> Note: This example mirrors the template’s structure and naming patterns, but uses variables for the base package.

### 1) Read endpoint (Query)

```java
package ${input:basePackage:com.erpang.ddd}.demo.adapter.api.query;

import ${input:basePackage:com.erpang.ddd}.app.demo.query.dto.GetDemoDetailQuery;
import ${input:basePackage:com.erpang.ddd}.app.demo.query.handler.GetDemoDetailQueryHandler;
import ${input:basePackage:com.erpang.ddd}.common.SingleResponse;
import ${input:basePackage:com.erpang.ddd}.demo.adapter.assembler.DemoApiAssembler;
import ${input:basePackage:com.erpang.ddd}.demo.adapter.api.query.response.GetDemoDetailResponse;
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
  public ResponseEntity<SingleResponse<GetDemoDetailResponse>> getDemoDetailById(@PathVariable UUID id) {
    return handler.handle(new GetDemoDetailQuery(id))
        .map(DemoApiAssembler::toResponse)
        .map(SingleResponse::of)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
```

### 2) Write endpoint (Command)

```java
package ${input:basePackage:com.erpang.ddd}.demo.adapter.api.command;

import ${input:basePackage:com.erpang.ddd}.app.demo.command.handler.CreateDemoCommandHandler;
import ${input:basePackage:com.erpang.ddd}.app.demo.command.dto.CreateDemoResult;
import ${input:basePackage:com.erpang.ddd}.common.SingleResponse;
import ${input:basePackage:com.erpang.ddd}.demo.adapter.api.command.request.CreateDemoRequest;
import ${input:basePackage:com.erpang.ddd}.demo.adapter.api.command.response.CreateDemoResponse;
import ${input:basePackage:com.erpang.ddd}.demo.adapter.assembler.DemoApiAssembler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoCommandController {

  private final CreateDemoCommandHandler createHandler;

  public DemoCommandController(CreateDemoCommandHandler createHandler) {
    this.createHandler = createHandler;
  }

  @PostMapping
  public ResponseEntity<SingleResponse<CreateDemoResponse>> create(@Valid @RequestBody CreateDemoRequest request) {
    CreateDemoResult result = createHandler.handle(DemoApiAssembler.toCommand(request));
    return ResponseEntity.ok(SingleResponse.of(DemoApiAssembler.toResponse(result)));
  }
}
```

### 3) Domain aggregate

```java
package ${input:basePackage:com.erpang.ddd}.domain.demo.model;

import java.time.Instant;
import java.util.Objects;

public final class Demo {

  private final DemoId id;
  private DemoName name;
  private DemoStatus status;
  private final Instant createdAt;

  private Demo(DemoId id, DemoName name, DemoStatus status) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.createdAt = Instant.now();
  }

  public static Demo create(DemoName name) {
    return new Demo(DemoId.newId(), Objects.requireNonNull(name, "demo name is required"), DemoStatus.SUBMITTED);
  }

  public DemoId id() {
    return id;
  }

  public DemoName name() {
    return name;
  }

  public Instant createdAt() {
    return createdAt;
  }
}
```

### 4) Repository interface (domain)

```java
package ${input:basePackage:com.erpang.ddd}.domain.demo.repository;

import ${input:basePackage:com.erpang.ddd}.domain.demo.model.Demo;

public interface DemoRepository {
  void save(Demo demo);
}
```

### 5) Repository implementation (infrastructure)

```java
package ${input:basePackage:com.erpang.ddd}.infra.demo.persistence.write;

import ${input:basePackage:com.erpang.ddd}.domain.demo.model.Demo;
import ${input:basePackage:com.erpang.ddd}.domain.demo.repository.DemoRepository;
import ${input:basePackage:com.erpang.ddd}.infra.demo.persistence.write.mapper.DemoMapper;
import ${input:basePackage:com.erpang.ddd}.infra.demo.persistence.write.po.DemoPO;
import org.springframework.stereotype.Repository;

@Repository
public class DemoRepositoryImpl implements DemoRepository {

  private final DemoMapper mapper;

  public DemoRepositoryImpl(DemoMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void save(Demo demo) {
    DemoPO po = new DemoPO();
    po.setId(demo.id().getValue());
    po.setName(demo.name().getValue());
    po.setCreatedAt(demo.createdAt());
    mapper.insert(po);
  }
}
```

### 6) Domain event

```java
package ${input:basePackage:com.erpang.ddd}.domain.demo.event;

import ${input:basePackage:com.erpang.ddd}.domain.common.event.DomainEvent;
import java.util.UUID;

public class DemoCreatedEvent implements DomainEvent {

  public static final String TYPE = "demo.created";

  private final UUID demoId;

  public DemoCreatedEvent(UUID demoId) {
    this.demoId = demoId;
  }

  public UUID demoId() {
    return demoId;
  }

  @Override
  public String type() {
    return TYPE;
  }
}
```

### 7) Event publication (command handler -> outbox)

```java
package ${input:basePackage:com.erpang.ddd}.app.demo.command.handler;

import ${input:basePackage:com.erpang.ddd}.app.demo.command.dto.CreateDemoCommand;
import ${input:basePackage:com.erpang.ddd}.app.demo.command.dto.CreateDemoResult;
import ${input:basePackage:com.erpang.ddd}.common.CommandHandler;
import ${input:basePackage:com.erpang.ddd}.domain.common.event.EventEnvelope;
import ${input:basePackage:com.erpang.ddd}.domain.common.outbox.OutboxRepository;
import ${input:basePackage:com.erpang.ddd}.domain.demo.event.DemoCreatedEvent;
import ${input:basePackage:com.erpang.ddd}.domain.demo.model.Demo;
import ${input:basePackage:com.erpang.ddd}.domain.demo.model.DemoName;
import ${input:basePackage:com.erpang.ddd}.domain.demo.repository.DemoRepository;
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

### 8) Event subscription (spring listener bridge -> inbox dispatcher -> app handlers)

```java
package ${input:basePackage:com.erpang.ddd}.infra.common.event;

import ${input:basePackage:com.erpang.ddd}.domain.common.event.EventEnvelope;
import ${input:basePackage:com.erpang.ddd}.infra.common.inbox.InboxEventDispatcher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringDomainEventListener {

  private final InboxEventDispatcher dispatcher;

  public SpringDomainEventListener(InboxEventDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  @EventListener
  public void on(EventEnvelope<?> envelope) {
    dispatcher.dispatch(envelope);
  }
}
```

