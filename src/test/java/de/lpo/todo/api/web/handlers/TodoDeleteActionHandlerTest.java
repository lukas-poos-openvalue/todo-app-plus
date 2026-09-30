package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.service.TodoService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class TodoDeleteActionHandlerTest {
  private final TodoService todoService = mock();
  private final TodoDeleteActionHandler handler = new TodoDeleteActionHandler(todoService);

  @Test
  void should_delete_todo_and_respond_empty_html() {
    // GIVEN
    final var todoId = IntId.ofValue(123);
    final Context ctx = mock();
    TestUtils.Web.mockPathParamTodoId(ctx, todoId);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html("");
    verify(todoService).deleteById(todoId);
    verifyNoMoreInteractions(todoService);
  }
}
