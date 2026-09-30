package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.UserService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;

/** Responds the HTML of the boards page. */
public final class BoardsPageHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final UserService userService;
  private final BoardService boardService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param userService the service to resolve users with
   * @param boardService the service to fetch boards with
   */
  public BoardsPageHandler(
      final TemplateService templateService,
      final UserService userService,
      final BoardService boardService) {
    this.templateService = templateService;
    this.userService = userService;
    this.boardService = boardService;
  }

  @Override
  protected JteModel provideModel(final Context ctx) {
    final var currentUser = userService.getFromContext(ctx);
    final var boards = boardService.findAllForUserFetchAll(currentUser);
    return templateService.getTemplates().boards(currentUser, boards);
  }
}
