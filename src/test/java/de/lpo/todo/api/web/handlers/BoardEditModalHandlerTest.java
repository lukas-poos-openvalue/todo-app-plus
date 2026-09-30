package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.Board;
import de.lpo.todo.model.Role;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.model.refs.Refs;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.UserService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class BoardEditModalHandlerTest {
  private final TemplateService templateService = mock();
  private final UserService userService = mock();
  private final BoardService boardService = mock();
  private final BoardEditModalHandler handler =
      new BoardEditModalHandler(templateService, userService, boardService);

  @Test
  void should_respond_edit_board_modal_html() {
    // GIVEN
    // - Templates
    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    // - BoardService
    final var boardId = IntId.ofValue(1);
    final var ownerId = StringId.ofValue("a");
    final var board =
        new Board(boardId, "title", Refs.unloaded(), Ref.unloaded(ownerId), Refs.unloaded());
    when(boardService.getOneByIdFetchAll(any())).thenReturn(board);

    // - UserService
    final var user = new User(ownerId, "a", Role.USER);
    when(userService.getFromContext(any())).thenReturn(user);

    // - Context
    final Context ctx = mock();
    TestUtils.Web.mockPathParamBoardId(ctx, boardId);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).boardsModalsBoardEditModal(any(), any());
    verify(userService).getFromContext(ctx);
    verify(userService).findAllUsersExcluding(ownerId);
    verify(boardService).getOneByIdFetchAll(boardId);
    verifyNoMoreInteractions(userService, boardService);
  }
}
