package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.service.BoardService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class BoardDeleteActionHandlerTest {
  private final BoardService boardService = mock();
  private final BoardDeleteActionHandler handler = new BoardDeleteActionHandler(boardService);

  @Test
  void should_delete_board_and_respond_empty_html() {
    // GIVEN
    final var boardId = IntId.ofValue(1);
    final Context ctx = mock();
    TestUtils.Web.mockPathParamBoardId(ctx, boardId);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html("");
    verify(boardService).deleteById(boardId);
    verifyNoMoreInteractions(boardService);
  }
}
