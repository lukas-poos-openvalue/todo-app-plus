package de.lpo.todo;

import dasniko.testcontainers.keycloak.KeycloakContainer;

public class TestKeycloakContainer extends KeycloakContainer {
  private static final String IMAGE_VERSION = "docker.io/keycloak/keycloak:26.7";

  @SuppressWarnings("resource")
  public static KeycloakContainer createInstance() {
    return new TestKeycloakContainer().withRealmImportFile("realm-export.json").withReuse(true);
  }

  private TestKeycloakContainer() {
    super(IMAGE_VERSION);
  }

  @Override
  public void start() {
    // Start the container
    super.start();

    // Set system properties, so that the dynamic properties appear in the config
    final var sysProps = System.getProperties();
    sysProps.setProperty("auth.baseUri", "http://%s:%s".formatted(getHost(), getMappedPort(8080)));
    sysProps.setProperty("auth.realm", "todo-app-plus");
    sysProps.setProperty("auth.clientId", "todo-app-plus");
    sysProps.setProperty("auth.secret", "wV6UnjCSlUMTYEsn6nZqd9zSmigyXgIX");
    sysProps.setProperty("auth.callbackUrl", "http://localhost:8080/api/login/callback");
  }
}
