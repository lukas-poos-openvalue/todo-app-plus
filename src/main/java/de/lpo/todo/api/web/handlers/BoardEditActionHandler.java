package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.UserService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.jspecify.annotations.Nullable;

/** Performs edition of a given board, then responds the HTML of the board item. */
public final class BoardEditActionHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final UserService userService;
  private final BoardService boardService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param userService the service to resolve users with
   * @param boardService the service to update boards with
   */
  public BoardEditActionHandler(
      final TemplateService templateService,
      final UserService userService,
      final BoardService boardService) {
    this.templateService = templateService;
    this.userService = userService;
    this.boardService = boardService;
  }

  @Override
  protected @Nullable JteModel provideModel(final Context ctx) {
    // Read submitted data
    if (!ctx.formParamMap().containsKey("title") || !ctx.formParamMap().containsKey("members")) {
      throw new BadRequestResponse("Endpoint expects two form params: title and members!");
    }
    final var updatedTitle = ctx.formParam("title");
    final var updatedMemberIds = ctx.formParams("members").stream().map(StringId::ofHash).toList();

    // Update board
    final var boardId = getBoardIdFromPath(ctx);
    final var currentUser = userService.getFromContext(ctx);
    final var board = boardService.updateFetchAll(boardId, updatedTitle, updatedMemberIds);

    // Respond updated board
    return templateService.getTemplates().boardsBoardItem(currentUser, board);
  }
}
