package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.Role;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.service.UserService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class BoardsAddModalHandlerTest {
  private final TemplateService templateService = mock();
  private final UserService userService = mock();
  private final BoardsAddModalHandler handler =
      new BoardsAddModalHandler(templateService, userService);

  @Test
  void should_load_user_options_and_respond_add_board_modal_html() {
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
    verify(templates).boardsModalsBoardAddModal(any());
    verify(userService).getFromContext(ctx);
    verify(userService).findAllUsersExcluding(userId);
    verifyNoMoreInteractions(userService);
  }
}
