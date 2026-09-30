package de.lpo.todo.api.web;

import de.lpo.todo.config.ConfigService;
import de.lpo.todo.config.Profile;
import de.lpo.todo.generated.jte.DynamicTemplates;
import de.lpo.todo.generated.jte.StaticTemplates;
import de.lpo.todo.generated.jte.Templates;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import java.nio.file.Path;

/**
 * Service to give access to {@link Templates JTE templates}, which can be rendered to HTML in a
 * type safe manner. The template files are located in the 'templates' folder.
 */
public final class TemplateService {
  private static final Path TEMPLATE_PATH_HMR = Path.of("src", "main", "templates");
  private static final Path OUTPUT_PATH_HMR = Path.of("target", "jte");

  private final Templates templates;

  /**
   * Chooses dynamic or precompiled templates based on the application profile.
   *
   * @param configService the service to read the profile from
   */
  public TemplateService(final ConfigService configService) {
    // Read the profile from the config
    final var profile = configService.getProfile();

    // Set the templates to be either dynamic (for DEV) or static
    if (profile == Profile.DEV) {
      final var codeResolver = new DirectoryCodeResolver(TEMPLATE_PATH_HMR);
      final var templateEngine =
          TemplateEngine.create(codeResolver, OUTPUT_PATH_HMR, ContentType.Html);
      this.templates = new DynamicTemplates(templateEngine);
    } else {
      this.templates = new StaticTemplates();
    }
  }

  /**
   * Returns the {@link Templates} for rendering HTML templates.
   *
   * @return the templates
   */
  public Templates getTemplates() {
    return templates;
  }
}
