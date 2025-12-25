# Prompt: Generate copilot-instructions.md from DDD Template (Variable-Based)

You are analyzing a DDD-based Spring Boot TEMPLATE repository.

IMPORTANT:
- You MUST scan the real repository structure, modules, and packages.
- You MUST read package-info.java files and treat them as authoritative.
- HOWEVER, in the FINAL OUTPUT you MUST NOT use the real project name or base package.
- Instead, you MUST abstract them into variables so the instructions are reusable.

### Variable Rules (MANDATORY)
In the final output:
- Replace the real project root with:
  ${input:projectName:ddd-layout}
- Replace the real Java base package with:
  ${input:basePackage:com.erpang.ddd}

Even if the template currently uses a different name or package, DO NOT leak real values.
All paths and package names must use the variables above.

---

## Objective

Generate a **copilot-instructions.md** file that acts as a strict, long-term architectural contract.
This contract will guide future LLM code generation to:
- Follow the same DDD module and package layout
- Place classes in correct packages
- Respect dependency directions
- Implement read, write, and event flows consistently

The output must be written in **English**.

---

## Output Structure (STRICT)

The output MUST contain exactly **three parts**, in this order.

---

## Part 1: YAML Directory Structure and Explanation

Produce a YAML document describing the architecture.

### Requirements
- Structure must be: `modules -> packages`
- Use `${input:projectName:ddd-layout}` and `${input:basePackage:com.erpang.ddd}` everywhere
- Reflect the REAL template structure, but abstracted
- Every module must include:
  - purpose
  - dependsOn (other modules)
- Every package must include:
  - purpose (derived from package-info.java first, code second)
  - allowedContents
  - forbiddenContents
  - namingConventions
  - dependenciesAllowed

### Special Rule: common
If a `common` module or package exists:
- Treat it as DDD foundational infrastructure
- Explicitly describe what is allowed and forbidden
- Emphasize that it must remain business-agnostic

### YAML Skeleton (you must fill it based on the template)
```yaml
project:
  root: ${input:projectName:ddd-layout}
  basePackage: ${input:basePackage:com.erpang.ddd}
  architecture: "DDD + Spring Boot"

modules:
  - name: "<moduleName>"
    path: "${input:projectName:ddd-layout}/<moduleDir>"
    purpose: "<derived from template or UNKNOWN>"
    dependsOn:
      - "<moduleName>"
    packages:
      - name: "${input:basePackage:com.erpang.ddd}.<context>.<layer>"
        path: "${input:projectName:ddd-layout}/<moduleDir>/src/main/java/${input:basePackage:com.erpang.ddd}/<context>/<layer>"
        purpose: "<from package-info or inferred>"
        allowedContents:
          - "<what belongs here>"
        forbiddenContents:
          - "<what must never appear here>"
        namingConventions:
          - "<class naming rules>"
        dependenciesAllowed:
          - "<allowed package dependencies>"
```

If something is unclear, mark it as `UNKNOWN` and explain why.

---

## Part 2: Dependency Order and Call Flows

Describe the **behavioral rules** of the architecture.

### Must include the following sections (verbatim titles):

### Dependency Direction

* Explicitly state which layers/modules may depend on which.
* Describe this in terms of **module + package**, not just “layers”.

### Read Path (Query) Flow

Describe a typical read request:

* Entry point
* Application orchestration
* Domain interaction
* Infrastructure access (if any)

Include module and package names using variables.

### Write Path (Command) Flow

Describe a typical write request:

* Command entry
* Use case / application service
* Aggregate modification
* Persistence
* Domain event creation (if applicable)

### Event Publish Flow

Describe:

* Where domain events are created
* Where they are published
* Sync vs async if inferable

### Event Subscribe Flow

Describe:

* Where subscribers live
* How they react to events
* What layers they are allowed to touch

### Forbidden Dependencies (Examples)

List at least 5 concrete forbidden examples, such as:

* API depending directly on infrastructure
* Domain importing Spring or JPA
* Application depending on concrete repository implementations

---

## Part 3: Example Code (Minimal but Complete)

Provide a concise, end-to-end example demonstrating:

1. A read endpoint (Query)
2. A write endpoint (Command)
3. A domain aggregate
4. A repository interface (domain)
5. A repository implementation (infrastructure)
6. A domain event
7. Event publication
8. Event subscription

### Rules

* Use `${input:basePackage:com.erpang.ddd}` in all package declarations
* Follow the actual template layout and naming style
* Keep code minimal but structurally correct
* Use generic example concepts unless the template already defines sample ones
* Add short comments only to clarify architectural boundaries

---

## Hard Constraints

* package-info.java overrides inference
* Domain layer must remain framework-agnostic
* Application layer orchestrates, does not contain business rules
* Infrastructure implements ports only
* common must never depend on business modules
* If uncertain, say so explicitly

---

## Final Check Before Output

* Output is English only
* Exactly three parts
* No real template names leaked
* All paths and packages use variables
* No DDD theory explanations not grounded in this template
