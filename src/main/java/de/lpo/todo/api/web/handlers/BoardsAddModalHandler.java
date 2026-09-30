package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.service.UserService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;

/** Responds the HTML of the modal for adding a board. */
public final class BoardsAddModalHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final UserService userService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param userService the service to resolve users with
   */
  public BoardsAddModalHandler(
      final TemplateService templateService, final UserService userService) {
    this.templateService = templateService;
    this.userService = userService;
  }

  @Override
  protected JteModel provideModel(final Context ctx) {
    final var currentUser = userService.getFromContext(ctx);
    final var otherUsers = userService.findAllUsersExcluding(currentUser.id());
    return templateService.getTemplates().boardsModalsBoardAddModal(otherUsers);
  }
}
