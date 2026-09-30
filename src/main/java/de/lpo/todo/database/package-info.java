/// ### Database
///
/// #### Requirements
///
/// - Handle connections to PostgreSQL
/// - Run schema migrations
/// - Create little overhead with model-and-schema synchronization
/// - Support Postgres-specific queries
///
/// #### Options
///
/// | Category | Option | Evaluation |
/// | --- |---|---|
/// | Connection-Pool | `HikariCP` | Small, fast, and default in Spring Boot |
/// | Connection-Pool | `Agroal` | Small, fast, and default in Quarkus |
/// | Schema-Migrations | `Liquibase` | Flexibel, many cool features |
/// | Schema-Migrations | `Flyway` | Simple, but complexer features are paid (e.g. rollbacks) |
/// | Queries | `Pure Java` | JDBC classes are old and unergonomic |
/// | Queries | `JPA / Hibernate` | ORM, too simplified, much synchronization overhead |
/// | Queries | `Jdbi` | Bind SQL queries to methods / records (no code generation) |
/// | Queries | `jOOQ` | Typesafe queries based on migrated schema |
///
/// #### Decision: `HikariCP`+`Liquibase`+`jOOQ`
///
/// For connection pools, all options are fine, but I'm familiar with `HikariCP`.
/// For migrations, I personally prefer `Liquibase` (many changelog-formats, rollbacks,
/// preconditions, contexts, ...).
/// For queries, I strongly recommend `jOOQ`.
///
/// @see DataSourceService
/// @see MigrationService
/// @see JooqService
@NullMarked
package de.lpo.todo.database;

import org.jspecify.annotations.NullMarked;
