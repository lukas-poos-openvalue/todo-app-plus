package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.service.BoardService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jspecify.annotations.Nullable;

/** Responds the HTML of the todos page. */
public final class TodosPageHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final BoardService boardService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param boardService the service to fetch boards with
   */
  public TodosPageHandler(final TemplateService templateService, final BoardService boardService) {
    this.templateService = templateService;
    this.boardService = boardService;
  }

  @Override
  protected @Nullable JteModel provideModel(final Context ctx) {
    final var boardId = getBoardIdFromPath(ctx);
    final var board = boardService.getOneByIdFetchAll(boardId);
    return templateService.getTemplates().todos(board);
  }
}
