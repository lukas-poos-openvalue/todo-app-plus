/// ### Testing
///
/// #### Requirements
///
/// - Unit tests only use mock dependencies and assertions
/// - There is only one major integration test context
///     - The service gets actually started and reset for each test
///     - Dependent services are started once
///
/// #### Options
///
/// | Category | Option | Evaluation |
/// |---|---|---|
/// | Test Runner | `JUnit` | De facto default |
/// | Assertions | `JUnit` | Already available, but very simplistic API |
/// | Assertions | `AssertJ` | Nice fluent API to write assertions |
/// | Mocking | `Mockito` | De facto default |
/// | Test-Context | `JUnit+Testcontainers` | Define extensions for specialized test contexts |
///
/// #### Decision: `JUnit` + `AssertJ` + `Mockito` + `Testcontainers`
///
/// It is a surprisingly powerful combination.
///
/// @see IntegrationTest
@NullMarked
package de.lpo.todo;

import org.jspecify.annotations.NullMarked;
