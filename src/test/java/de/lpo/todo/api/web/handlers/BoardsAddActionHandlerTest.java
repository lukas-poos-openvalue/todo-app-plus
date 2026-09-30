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
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BoardsAddActionHandlerTest {
  private final TemplateService templateService = mock();
  private final UserService userService = mock();
  private final BoardService boardService = mock();
  private final BoardsAddActionHandler handler =
      new BoardsAddActionHandler(templateService, userService, boardService);

  @Test
  void should_add_board_and_respond_board_html() {
    // GIVEN
    // - Templates
    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    // - UserService
    final var userId = StringId.ofValue("a");
    final var user = new User(userId, "a", Role.USER);
    when(userService.getFromContext(any())).thenReturn(user);

    // - Context
    final Context ctx = mock();
    when(ctx.formParamMap()).thenReturn(Map.of("title", List.of("title")));
    when(ctx.formParam("title")).thenReturn("title");

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).boardsBoardItem(eq(user), any());
    verify(userService).getFromContext(ctx);
    verify(boardService).addFetchAll("title", userId, List.of());
    verifyNoMoreInteractions(userService, boardService);
  }
}
