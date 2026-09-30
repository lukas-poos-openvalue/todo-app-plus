package de.lpo.todo.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ConfigServiceTest {

  private final ConfigService configService = new ConfigService();

  @Test
  void should_read_test_configuration_properly() {
    // WHEN
    final var result = configService.getConfig();

    // THEN
    assertThat(result)
        .extracting(Config::databaseDriver, Config::serverPort)
        .containsExactly("postgresql", 8080);
  }
}
