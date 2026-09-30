package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.UserService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.apache.commons.lang3.StringUtils;

/** Performs the creation of a new board, and then responds the HTML of the board item. */
public final class BoardsAddActionHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final UserService userService;
  private final BoardService boardService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param userService the service to resolve users with
   * @param boardService the service to add boards with
   */
  public BoardsAddActionHandler(
      final TemplateService templateService,
      final UserService userService,
      final BoardService boardService) {
    this.templateService = templateService;
    this.userService = userService;
    this.boardService = boardService;
  }

  @Override
  protected JteModel provideModel(final Context ctx) {
    // Read submitted data
    final var title = ctx.formParam("title");
    if (StringUtils.isBlank(title)) {
      throw new BadRequestResponse("Endpoint expects one form param: title!");
    }
    final var memberIdHashes = ctx.formParams("members").stream().map(StringId::ofHash).toList();

    // Create new board for current user
    final var currentUser = userService.getFromContext(ctx);
    final var board = boardService.addFetchAll(title, currentUser.id(), memberIdHashes);

    // Respond updated board
    return templateService.getTemplates().boardsBoardItem(currentUser, board);
  }
}
