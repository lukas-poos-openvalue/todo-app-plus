package de.lpo.todo.api.web.handlers;

import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.service.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;

/** Rejects requests to a board that the current user is not owner or member of. */
public final class BoardsSecurityHandler implements Handler {
  private final UserService userService;

  /**
   * Creates a handler backed by the given service.
   *
   * @param userService the service to check board access with
   */
  public BoardsSecurityHandler(final UserService userService) {
    this.userService = userService;
  }

  @Override
  public void handle(final Context ctx) {
    // Path param for hashed board ID is required!
    final var boardId = IntId.ofHash(ctx.pathParam("board-id-hashed"));
    if (boardId == null) {
      throw new UnauthorizedResponse("Cannot access board without an ID!");
    }

    // Get the ids for the board and the current user, then check the access
    final var currentUser = userService.getFromContext(ctx);
    if (!userService.canUserAccessBoard(currentUser.id(), boardId)) {
      throw new UnauthorizedResponse("Cannot access board with ID '" + boardId + "'!");
    }
  }
}
