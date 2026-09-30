package de.lpo.todo;

import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestPostgreSQLContainer extends PostgreSQLContainer {
  private static final String IMAGE_TAG = "docker.io/postgres:18.6-alpine";
  private static final DockerImageName IMAGE_NAME =
      DockerImageName.parse(IMAGE_TAG).asCompatibleSubstituteFor("postgres");

  @SuppressWarnings("resource")
  public static PostgreSQLContainer createInstance() {
    return new TestPostgreSQLContainer()
        .withDatabaseName("test-db")
        .withUsername("test-user")
        .withPassword("test-pw")
        .withExposedPorts(5432)
        .withReuse(true);
  }

  private TestPostgreSQLContainer() {
    super(IMAGE_NAME);
  }

  @Override
  public void start() {
    // Start the container
    super.start();

    // Set system properties, so that the dynamic properties appear in the config
    final var sysProps = System.getProperties();
    sysProps.setProperty("db.host", getHost());
    sysProps.setProperty("db.port", String.valueOf(getMappedPort(5432)));
    sysProps.setProperty("db.name", getDatabaseName());
    sysProps.setProperty("db.username", getUsername());
    sysProps.setProperty("db.password", getPassword());
  }
}
