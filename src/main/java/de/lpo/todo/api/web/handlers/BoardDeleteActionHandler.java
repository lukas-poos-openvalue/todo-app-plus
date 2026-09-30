package de.lpo.todo.api.web.handlers;

import de.lpo.todo.service.BoardService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;

/** Performs deletion of given board, then responds empty HTML of the deleted board item. */
public final class BoardDeleteActionHandler extends AbstractHandler {
  private final BoardService boardService;

  /**
   * Creates a handler backed by the given service.
   *
   * @param boardService the service to delegate to
   */
  public BoardDeleteActionHandler(final BoardService boardService) {
    this.boardService = boardService;
  }

  @Override
  protected JteModel provideModel(final Context ctx) {
    final var boardId = getBoardIdFromPath(ctx);
    boardService.deleteById(boardId);
    return emptyModel();
  }
}
