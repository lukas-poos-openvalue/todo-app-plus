package de.lpo.todo.api.web.handlers;

import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.service.TodoService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jspecify.annotations.Nullable;

/** Responds the HTML of the todo item. */
public final class TodoItemHandler extends AbstractHandler {
  private final TemplateService templateService;
  private final TodoService todoService;

  /**
   * Creates a handler backed by the given services.
   *
   * @param templateService the service to render templates with
   * @param todoService the service to fetch todos with
   */
  public TodoItemHandler(final TemplateService templateService, final TodoService todoService) {
    this.templateService = templateService;
    this.todoService = todoService;
  }

  @Override
  protected @Nullable JteModel provideModel(final Context ctx) {
    final var todoId = getTodoIdFromPath(ctx);
    final var todo = todoService.getOneByIdFetchNone(todoId);
    final var editing = "true".equals(ctx.queryParam("edit"));
    return templateService.getTemplates().todosTodoItem(todo, editing);
  }
}
