package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.UserService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jspecify.annotations.Nullable;

/** Responds the HTML of modal for editing a given board. */
public final class BoardEditModalHandler extends AbstractHandler {
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
  public BoardEditModalHandler(
      final TemplateService templateService,
      final UserService userService,
      final BoardService boardService) {
    this.templateService = templateService;
    this.userService = userService;
    this.boardService = boardService;
  }

  @Override
  protected @Nullable JteModel provideModel(final Context ctx) {
    // Fetch board
    final var boardId = getBoardIdFromPath(ctx);
    final var board = boardService.getOneByIdFetchAll(boardId);

    // Determine other users
    final var currentUser = userService.getFromContext(ctx);
    final var otherUsers = userService.findAllUsersExcluding(currentUser.id());

    // Return HTML for modal
    return templateService.getTemplates().boardsModalsBoardEditModal(board, otherUsers);
  }
}
