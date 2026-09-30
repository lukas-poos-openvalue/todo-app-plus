package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.Role;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.UserService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class BoardsPageHandlerTest {
  private final TemplateService templateService = mock();
  private final UserService userService = mock();
  private final BoardService boardService = mock();
  private final BoardsPageHandler handler =
      new BoardsPageHandler(templateService, userService, boardService);

  @Test
  void should_load_boards_and_respond_boards_page() {
    // GIVEN
    final Context ctx = mock();

    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    final var userId = StringId.ofValue("a");
    final var user = new User(userId, "a", Role.USER);
    when(userService.getFromContext(any())).thenReturn(user);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).boards(eq(user), any());
    verify(userService).getFromContext(ctx);
    verify(boardService).findAllForUserFetchAll(user);
    verifyNoMoreInteractions(userService, boardService);
  }
}
