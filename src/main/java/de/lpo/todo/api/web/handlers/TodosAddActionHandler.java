package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.service.TodoService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

/** Perform addition of todo, then responds HTML of todo item. */
public final class TodosAddActionHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final TodoService todoService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param todoService the service to add todos with
   */
  public TodosAddActionHandler(
      final TemplateService templateService, final TodoService todoService) {
    this.templateService = templateService;
    this.todoService = todoService;
  }

  @Override
  protected @Nullable JteModel provideModel(final Context ctx) {
    // Read submitted content
    final var content = ctx.formParam("content");
    if (StringUtils.isBlank(content)) {
      ctx.status(HttpStatus.BAD_REQUEST).result("'content' is empty!");
      return null;
    }

    // Add to database
    final var boardId = getBoardIdFromPath(ctx);
    final var todo = todoService.addFetchNone(boardId, content);

    // Render response
    return templateService.getTemplates().todosTodoItem(todo, false);
  }
}
