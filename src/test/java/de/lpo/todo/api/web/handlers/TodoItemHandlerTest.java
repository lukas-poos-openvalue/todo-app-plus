package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.Todo;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.service.TodoService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class TodoItemHandlerTest {
  private final TemplateService templateService = mock();
  private final TodoService todoService = mock();
  private final TodoItemHandler handler = new TodoItemHandler(templateService, todoService);

  @Test
  void should_respond_todo_html() {
    // GIVEN
    // - Templates and how they render
    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    // - Todoitem
    final var boardId = IntId.ofValue(1);
    final var todoId = IntId.ofValue(1);
    final var todo = new Todo(todoId, "content", false, Ref.unloaded(boardId));
    when(todoService.getOneByIdFetchNone(any())).thenReturn(todo);

    // - Context
    final Context ctx = mock();
    TestUtils.Web.mockPathParamTodoId(ctx, todoId);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).todosTodoItem(todo, false);
    verify(todoService).getOneByIdFetchNone(todoId);
    verifyNoMoreInteractions(todoService);
  }
}
