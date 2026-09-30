package de.lpo.todo;

import static java.util.Objects.requireNonNull;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import de.lpo.todo.context.ApplicationContext;
import java.util.Arrays;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startable;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.postgresql.PostgreSQLContainer;

public class IntegrationTest implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {
  static final PostgreSQLContainer POSTGRES = TestPostgreSQLContainer.createInstance();
  static final KeycloakContainer KEYCLOAK = TestKeycloakContainer.createInstance();

  private static final Namespace NAMESPACE = Namespace.create(IntegrationTest.class);

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    extensionContext
        .getRoot()
        .getStore(Namespace.GLOBAL)
        .computeIfAbsent("containers", ignored -> new RunningContainers(POSTGRES, KEYCLOAK));
  }

  @Override
  public void beforeEach(final ExtensionContext extensionContext) {
    final TodoAppPlus app =
        extensionContext
            .getStore(NAMESPACE)
            .computeIfAbsent("application", ignored -> new TodoAppPlus(), TodoAppPlus.class);
    app.start();
    TestUtils.IT.Database.clearDomainTables();
  }

  @Override
  public boolean supportsParameter(
      final ParameterContext parameterContext, final ExtensionContext extensionContext) {
    final Class<?> type = parameterContext.getParameter().getType();
    if (type.equals(ApplicationContext.class)) {
      return true;
    }

    final var context = getApplicationContext(extensionContext);
    return context.has(type);
  }

  @Override
  public @Nullable Object resolveParameter(
      final ParameterContext parameterContext, final ExtensionContext extensionContext) {
    final Class<?> type = parameterContext.getParameter().getType();
    final var context = getApplicationContext(extensionContext);

    if (type.equals(ApplicationContext.class)) {
      return context;
    }
    return context.get(type);
  }

  private ApplicationContext getApplicationContext(final ExtensionContext extensionContext) {
    final TodoAppPlus app =
        requireNonNull(extensionContext.getStore(NAMESPACE).get("application", TodoAppPlus.class));
    return app.context;
  }

  private record RunningContainers(GenericContainer<?>... containers) implements AutoCloseable {
    private RunningContainers {
      Startables.deepStart(containers).join(); // start in parallel
    }

    @Override
    public void close() {
      Arrays.stream(containers).forEach(Startable::close);
    }
  }
}
