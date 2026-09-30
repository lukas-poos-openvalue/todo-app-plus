# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A Java 25 server-rendered todo app (boards + todos, OIDC login via Keycloak) built with Javalin,
jOOQ, JTE templates, and HTMX. Package root: `de.lpo.todo`.

## Commands

Build system is Maven (use `./mvnw` if a matching local `mvn` isn't available).

```bash
./mvnw compile                          # compile (also runs jOOQ codegen + JTE codegen via testcontainers)
./mvnw test                             # unit tests only (surefire)
./mvnw verify                           # unit + integration tests (failsafe), plus checkstyle/spotbugs/jacoco/enforcer
./mvnw test -Dtest=BoardServiceTest     # single unit test class
./mvnw test -Dtest=BoardServiceTest#methodName
./mvnw verify -Dit.test=BoardRepoIT     # single integration test class (*IT.java, runs via failsafe)
```

Formatting and linting are **off by default** (`<skip>true</skip>` for spotless/spotbugs/checkstyle/enforcer
in `pom.xml`) and are enabled via Maven profiles:

```bash
./mvnw validate -Pformatting-apply   # auto-format with google-java-format (spotless)
./mvnw verify -Pformatting-check     # CI-style check: fails if not formatted
./mvnw verify -Plinting-check        # spotbugs + checkstyle + enforcer + jacoco coverage gate (80% instr / 50% branch)
```

Run the app locally:

```bash
docker compose up -d        # starts Postgres (5432) + Keycloak (8082), realm imported from src/test/resources/realm-export.json
./mvnw compile exec:java    # or run TodoAppPlus.main() from the IDE; reads src/main/resources/application.properties
```

Integration tests (`*IT.java`) and the jOOQ codegen Maven plugin spin up ephemeral Postgres/Keycloak
containers via Testcontainers — Docker must be available to run `verify`, `compile`, or any `*IT` test.

## Architecture

**Layering**: `api.web.handlers` → `service` → `repository` → jOOQ-generated code → Postgres.
`api.security` is a parallel module for identity/OIDC, consumed by `service.UserService` and by
Javalin's `before(...)` security filters. Read the package-level Javadoc (`package-info.java`) in
each package first — `api`, `api.web`, `api.security`, `config`, `database`, and `context` each have
a short design rationale written by the author; don't duplicate work already explained there.

**Dependency injection — no framework, no annotations.** `context/ApplicationContext.java` is a
single class that constructs every service/repo/handler as a `public final` field, in dependency
order, in its constructor. There is no Spring/Guice/Dagger. To wire up a new class: add a field to
`ApplicationContext` after its dependencies, in topological order. Constructors take their
dependencies as plain parameters. Tests get access to any wired instance via `ApplicationContext`
reflection lookup (`has`/`get`) exposed through the JUnit extension `IntegrationTest` (see below).

**Routing**: all routes are declared in one place, `api.ApiBootstrapService.bootstrap(JavalinConfig)`
(invoked from `TodoAppPlus.setupApplication()`), using Javalin's `apiBuilder` DSL (nested
`path(...)`/`get`/`post`/`patch`/`delete`). Handlers are plain objects from `ApplicationContext`, not
annotated controllers. Security (`before(...)`) is layered per path segment: OIDC auth on `/app`,
admin role check on `/app/admin`, board-ownership/membership check (`BoardsSecurityHandler`) on
`/app/boards/{board-id-hashed}`.

**Web handlers** extend the package-private `api.web.handlers.AbstractHandler` and implement
`provideModel(Context)`, returning a typed `JteModel` (or `null` after writing an error response
directly to `ctx`). `TemplateService` picks between hot-reloading dynamic templates (`Profile.DEV`)
and precompiled static templates (`Profile.PROD`). Templates live in `src/main/templates/*.jte` and
are compiled to type-safe Java model classes (package `de.lpo.todo.generated.jte`) by the
`jte-maven-plugin` (`generate-sources` phase, output in `target/generated-sources/jte`) — template
changes require a compile before the generated model API updates. Frontend interactivity is HTMX
(`assets/js/htmx.min.js`); handlers return HTML fragments, not JSON.

**IDs are opaque to the outside world.** `model.ids.Id<T>` (sealed: `IntId`, `StringId`) wraps a raw
DB value and lazily converts to/from a short public "hash" via Sqids (`{board-id-hashed}` path
params). Never expose raw integer/string PKs in routes or templates — always go through
`Id.hash()`/`ofHash(...)`. `model.refs.Ref<ID,T>` / `Refs<T>` model a loaded-vs-unloaded foreign
reference (sealed `Loaded`/`Unloaded`), used by repositories/mappers to decide whether to eagerly
join related rows — check `isLoaded()` or pattern-match before calling `unwrap()`.

**Database**: jOOQ is used for type-safe SQL (no ORM/entity magic); `JooqService` exposes the
`DSLContext`. Schema is Liquibase YAML changesets under `src/main/resources/db-migrations/` (master
file `master.yml`, one file per table under `tables/`), applied at startup by `MigrationService` and
also replayed by the `testcontainers-jooq-codegen-maven-plugin` at build time to regenerate jOOQ
classes into `target/generated-sources/jooq` (package `de.lpo.todo.generated.jooq`) — editing
jOOQ-generated code directly is pointless, change the Liquibase changeset instead and recompile.

**Config**: `config.ConfigService` reads `avaje-config` properties (`src/main/resources/application.properties`,
overridable via env vars/system properties) into an immutable `Config` record; no reflection-based
binding library is used deliberately (see `config/package-info.java`). `Profile` (`DEV`/`PROD`, via
`env.profile`) toggles dev-only behavior: serving `assets/` from disk vs. classpath, and dynamic vs.
precompiled JTE templates.

**Auth**: OIDC via pac4j + Keycloak (`api.security.OidcSecurityService`), with a workaround layer for
integrating pac4j into Javalin. `api.security.IdentityService` / `repository.IdentityRepo` persist
authenticated identities; `service.UserService` wraps that for domain-level use (roles, board
ownership checks).

## Testing

- Unit tests (`*Test.java`, surefire): mock dependencies with Mockito; fast, no containers.
- Integration tests (`*IT.java`, failsafe): implement/annotate with `IntegrationTest`
  (`src/test/java/de/lpo/todo/IntegrationTest.java`), a JUnit `BeforeAllCallback` +
  `ParameterResolver` that starts shared Postgres + Keycloak Testcontainers once, boots a real
  `TodoAppPlus`, clears domain tables before each test, and injects any field from
  `ApplicationContext` (or the context itself) directly as a test method parameter.
- `TestUtils`, `TestPostgreSQLContainer`, `TestKeycloakContainer` hold shared test fixtures/container
  setup — reuse rather than duplicating container config.
