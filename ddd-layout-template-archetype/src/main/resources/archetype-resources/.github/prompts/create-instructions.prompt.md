
# DDD Layout Contract Generator (Repo Scanner Prompt)

You are generating a strict architecture contract for a DDD Spring Boot template project.
Your output will be used as the long-term coding standard for future code generation.

## Inputs (Variables)
- Project name: ${input:projectName:ddd-layout-template}
- Base package: ${input:basePackage:com.erpang.ddd}

## Goal
Scan the entire repository under `${input:projectName:ddd-layout}` and produce a **single structured contract** that explains:
1) Module and package directory layout, responsibilities, naming rules, and dependencies.
2) Allowed dependency order and call flows for read, write, and events (publish/subscribe).
3) Concrete example code showing read, write, and event publish/subscribe flows in a concise but complete way.

You must ground explanations in evidence from the codebase, especially `package-info.java`. If something is not certain, mark it as `UNKNOWN` and list what you would need to confirm. Do not invent business logic.

---

# What to Scan

1) **Modules**
- Identify all top-level modules (Gradle/Maven multi-module or logical modules).
- For each module, detect dependencies (build files and imports).

2) **Packages**
- For each module, enumerate packages under `${input:basePackage}`.
- For each package, read `package-info.java` (if present) and treat it as authoritative.

3) **Code Roles**
Classify classes by role:
- API layer (controllers, request/response DTOs)
- Application layer (use cases, command/query handlers, orchestration)
- Domain layer (aggregates, entities, value objects, domain services, domain events)
- Infrastructure layer (repositories impl, persistence, messaging, external clients)
- Common base (shared DDD primitives and cross-cutting utilities)

4) **Read/Write Paths + Events**
- Find at least one existing example for: a read endpoint, a write endpoint, an event publish, an event subscriber.

---

# Output Format (MUST FOLLOW)

Your final output has exactly **three sections** in this order:

## Part 1: YAML Directory Structure and Explanations
Output a YAML document describing the architecture. Requirements:
- Use `${input:projectName:ddd-layout}` and `${input:basePackage:com.ryan.ddd}` as variables in paths.
- Structure it as `modules -> packages`.
- For each module:
  - `purpose`
  - `dependsOn` (other modules)
  - `exports` (public API packages if applicable)
- For each package:
  - `purpose` (use package-info if present)
  - `allowedContents` (what classes belong here)
  - `forbiddenContents` (what must not be here)
  - `namingConventions` (class suffix/prefix rules)
  - `dependenciesAllowed` (which other packages it may import)
- Special rule: the `common` module/package is DDD foundational and cross-cutting. Describe what belongs there and what must not.

**YAML template you must fill from the repo:**
```yaml
project:
  name: ${input:projectName:ddd-layout}
  basePackage: ${input:basePackage:com.ryan.ddd}
  style:
    architecture: "DDD + Spring Boot"
    language: "Java"
    framework: "Spring Boot"
modules:
  - name: "<moduleName>"
    path: "${input:projectName:ddd-layout}/<moduleDir>"
    purpose: "<from README/package-info/build files or UNKNOWN>"
    dependsOn: ["<moduleA>", "<moduleB>"]
    notes:
      - "<key constraints>"
    packages:
      - name: "${input:basePackage:com.ryan.ddd}.<...>"
        path: "${input:projectName:ddd-layout}/<moduleDir>/src/main/java/${input:basePackage:com.ryan.ddd}/<...>"
        purpose: "<package-info summary or UNKNOWN>"
        allowedContents:
          - "<e.g., Controllers, UseCases, Aggregates...>"
        forbiddenContents:
          - "<e.g., JPA Entities in domain, Controllers in domain...>"
        namingConventions:
          - "<e.g., *Controller, *Command, *Query, *Handler, *Repository>"
        dependenciesAllowed:
          - "<list of allowed package imports>"
common:
  rule: "Common holds foundational DDD primitives and cross-cutting concerns."
  allowed:
    - "<e.g., Identifier, DomainEvent base, Result/Either, Exceptions, Clock, Utils that are truly generic>"
  forbidden:
    - "Business-specific domain logic"
    - "Dependencies on application/domain modules (unless explicitly allowed by the template)"
````

## Part 2: Dependency Order and Call Flows

Output a strict rule set. Requirements:

* Provide a **dependency direction** statement (what can depend on what).
* Provide a **Read flow** (Query) call sequence including module + package names.
* Provide a **Write flow** (Command) call sequence including module + package names.
* Provide **Event publish** flow and **Event subscribe** flow including module + package names.
* State explicit forbidden dependency examples (at least 5) derived from your architecture.

Must include these headings verbatim:

* `Dependency Direction`
* `Read Path (Query) Flow`
* `Write Path (Command) Flow`
* `Event Publish Flow`
* `Event Subscribe Flow`
* `Forbidden Dependencies (Examples)`

## Part 3: Minimal Complete Example Code

Provide a concise but complete example that demonstrates:

1. A read endpoint and query handler.
2. A write endpoint and command handler that changes domain state.
3. Publishing a domain event.
4. Subscribing to the event (same service or integration event, depending on template).
5. Repository interface in domain and implementation in infrastructure.
6. DTOs at the boundary.

Rules for example code:

* Use the real package layout discovered from the repo. If multiple bounded contexts exist, choose one.
* Use `${input:basePackage:com.ryan.ddd}` in package declarations.
* Keep it minimal but compilable in principle.
* Do not include business-specific fake logic. Use generic names like `Order`, `Customer`, `Invoice` only if the repo already has them. Otherwise use the repo’s existing sample aggregate names.
* Annotate with short comments only where it clarifies layer boundaries.

---

# Architecture Enforcement Rules (MUST APPLY)

1. **Authority of package-info**

* If `package-info.java` exists for a package, it overrides any guesswork. Quote its intent in your own words.

2. **Layering**

* API layer may call Application layer only.
* Application layer orchestrates. It may call Domain (interfaces, aggregates) and Infrastructure abstractions via interfaces.
* Domain layer must not depend on Spring, persistence, HTTP, messaging libraries.
* Infrastructure layer implements domain/application ports and integrates external systems.

3. **Common**

* `common` is foundational. It should not import business domain packages.
* Business domains may import `common`.

4. **No leaks**

* No JPA annotations in domain entities unless the template explicitly uses “domain-as-entity” and package-info says so.
* No Controller/HTTP DTOs inside domain.

5. **Be explicit**

* When you list dependencies, include module + package granularity.

---

# Final Quality Bar

* Output must be English.
* Output must contain exactly the three parts above.
* If uncertain, mark `UNKNOWN` and list evidence you looked for.
* No filler. No generic DDD lecture. Everything must map to this repository.
