package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;

/** Responds the HTML of the index page. */
public final class IndexPageHandler extends AbstractHandler {
  private final TemplateService templateService;

  /**
   * Creates a handler backed by the given service.
   *
   * @param templateService the service to render templates with
   */
  public IndexPageHandler(final TemplateService templateService) {
    this.templateService = templateService;
  }

  @Override
  protected JteModel provideModel(final Context ctx) {
    return templateService.getTemplates().index();
  }
}
