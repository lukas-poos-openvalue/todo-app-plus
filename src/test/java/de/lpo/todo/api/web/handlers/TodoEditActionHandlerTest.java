package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.service.TodoService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class TodoEditActionHandlerTest {
  private final TemplateService templateService = mock();
  private final TodoService todoService = mock();
  private final TodoEditActionHandler handler =
      new TodoEditActionHandler(templateService, todoService);

  @Test
  void should_edit_todo_and_respond_todo_html() {
    // GIVEN
    final var todoId = IntId.ofValue(123);

    // - Templates
    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    // - Context
    final Context ctx = mock();
    TestUtils.Web.mockPathParamTodoId(ctx, todoId);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).todosTodoItem(any(), eq(false));
    verify(todoService).updateFetchNone(todoId, null, null);
    verifyNoMoreInteractions(todoService);
  }
}
