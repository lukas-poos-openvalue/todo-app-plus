package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.service.TodoService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class TodosAddActionHandlerTest {
  private final TemplateService templateService = mock();
  private final TodoService todoService = mock();
  private final TodosAddActionHandler handler =
      new TodosAddActionHandler(templateService, todoService);

  @Test
  void should_add_todo_and_respond_todo_html() {
    // GIVEN
    // - Templates
    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    // - Context
    final Context ctx = mock();
    final var boardId = IntId.ofValue(123);
    TestUtils.Web.mockPathParamBoardId(ctx, IntId.ofValue(123));
    when(ctx.formParam("content")).thenReturn("content");

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).todosTodoItem(any(), eq(false));
    verify(todoService).addFetchNone(boardId, "content");
    verifyNoMoreInteractions(todoService);
  }
}
