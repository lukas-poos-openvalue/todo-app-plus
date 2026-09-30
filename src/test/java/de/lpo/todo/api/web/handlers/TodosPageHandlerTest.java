package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.Board;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.model.refs.Refs;
import de.lpo.todo.service.BoardService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class TodosPageHandlerTest {
  private final TemplateService templateService = mock();
  private final BoardService boardService = mock();
  private final TodosPageHandler handler = new TodosPageHandler(templateService, boardService);

  @Test
  void should_load_board_and_respond_todos_page() {
    // GIVEN
    // - Templates
    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    // - BoardService
    final var boardId = IntId.ofValue(1);
    final var ownerId = StringId.ofValue("a");
    final Board board =
        new Board(boardId, "title", Refs.unloaded(), Ref.unloaded(ownerId), Refs.unloaded());
    when(boardService.getOneByIdFetchAll(any())).thenReturn(board);

    // - Context
    final Context ctx = mock();
    TestUtils.Web.mockPathParamBoardId(ctx, boardId);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).todos(board);
    verify(boardService).getOneByIdFetchAll(boardId);
    verifyNoMoreInteractions(boardService);
  }
}
