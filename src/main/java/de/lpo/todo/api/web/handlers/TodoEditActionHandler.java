package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.service.TodoService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jspecify.annotations.Nullable;

/** Perfoms edition of the given todo, then responds HTML of todo item. */
public final class TodoEditActionHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final TodoService todoService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param todoService the service to update todos with
   */
  public TodoEditActionHandler(
      final TemplateService templateService, final TodoService todoService) {
    this.templateService = templateService;
    this.todoService = todoService;
  }

  @Override
  protected @Nullable JteModel provideModel(final Context ctx) {
    // Read submitted content and completed flag
    final var newContent = ctx.formParam("content");
    final var newCompletedStr = ctx.formParam("completed");
    final var newCompleted = newCompletedStr != null ? Boolean.parseBoolean(newCompletedStr) : null;

    // Edit todo
    final var todoId = getTodoIdFromPath(ctx);
    final var todo = todoService.updateFetchNone(todoId, newContent, newCompleted);

    // Respond updated todo item
    return templateService.getTemplates().todosTodoItem(todo, false);
  }
}
