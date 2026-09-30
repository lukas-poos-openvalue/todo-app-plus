package de.lpo.todo.api.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import de.lpo.todo.config.ConfigService;
import org.junit.jupiter.api.Test;

class TemplateServiceTest {
  private final ConfigService configService = mock(ConfigService.class);

  @Test
  void should_initialize_properly() {
    // WHEN
    final var jteService = new TemplateService(configService);
    final var result = jteService.getTemplates();

    // THEN
    assertThat(result).isNotNull();
  }
}
