package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.security.IdentityService;
import de.lpo.todo.api.web.TemplateService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;

/** Responds the HTML of the admin page. */
public final class AdminPageHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final IdentityService identityService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param identityService the service to resolve the current identity with
   */
  public AdminPageHandler(
      final TemplateService templateService, final IdentityService identityService) {
    this.templateService = templateService;
    this.identityService = identityService;
  }

  @Override
  protected JteModel provideModel(final Context ctx) {
    final var currentAuthUser = identityService.getFromContext(ctx);
    return templateService.getTemplates().admin(currentAuthUser);
  }
}
