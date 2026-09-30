package de.lpo.todo.api.web.handlers;

import de.lpo.todo.service.TodoService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;

/** Performs deletion of the given todo, then responds the empty HTML of the deleted todo item. */
public final class TodoDeleteActionHandler extends AbstractHandler {
  private final TodoService todoService;

  /**
   * Creates a handler backed by the given service.
   *
   * @param todoService the service to delegate to
   */
  public TodoDeleteActionHandler(final TodoService todoService) {
    this.todoService = todoService;
  }

  @Override
  protected JteModel provideModel(final Context ctx) {
    final var todoId = getTodoIdFromPath(ctx);
    todoService.deleteById(todoId);
    return emptyModel();
  }
}
