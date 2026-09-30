# todo-app-plus

> **Note:** This is a demo project built for a presentation called **"Can the modern Java dev
> survive without Spring-Boot & Co.?"**. See [`presentation.md`](./presentation.md) for the slides.

A server-rendered todo app (boards + todos) with OIDC login via Keycloak, built on Java 25 without an application
framework like Spring Boot or Quarkus — just Javalin, jOOQ, JTE templates, and HTMX.

## Stack

- **[Javalin](https://javalin.io/)** — HTTP routing and request handling
- **[jOOQ](https://www.jooq.org/)** — type-safe SQL against Postgres, code-generated from Liquibase migrations
- **[JTE](https://jte.gg/)** — type-safe, precompiled HTML templates
- **[HTMX](https://htmx.org/)** — frontend interactivity without a JS framework/build step
- **[pac4j](https://www.pac4j.org/)** — OIDC authentication against Keycloak
- **[Liquibase](https://www.liquibase.org/)** — database schema migrations
- Dependency injection is hand-rolled: a single `ApplicationContext` class wires up every service/repo/handler in its
  constructor — no Spring/Guice/Dagger

See [`CLAUDE.md`](./CLAUDE.md) for a deeper architecture walkthrough.

## Prerequisites

- Java 25
- Docker/Podman (for Postgres, Keycloak, and Testcontainers-based builds/tests)

## Running locally

```bash
docker compose up -d        # starts Postgres (5432) + Keycloak (8082)
./mvnw compile exec:java    # or run TodoAppPlus.main() from your IDE
```

The app reads its configuration from `src/main/resources/application.properties` and serves on
`http://localhost:8080`. Keycloak's realm is pre-imported from `src/test/resources/realm-export.json`.

## Build & test

```bash
./mvnw compile                          # compile (runs jOOQ + JTE codegen via Testcontainers)
./mvnw test                             # unit tests
./mvnw verify                           # unit + integration tests, plus checkstyle/spotbugs/jacoco/enforcer
```

Formatting and linting are off by default and opt in via profiles:

```bash
./mvnw validate -Pformatting-apply   # auto-format with google-java-format
./mvnw verify -Pformatting-check     # CI-style formatting check
./mvnw verify -Plinting-check        # spotbugs + checkstyle + enforcer + coverage gate
```

Docker/Pdodman must be available for `compile`, `verify`, or any `*IT` test, since both the jOOQ codegen plugin and the
integration tests spin up ephemeral Postgres/Keycloak containers via Testcontainers.
