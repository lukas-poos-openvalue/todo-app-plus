package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.security.IdentityService;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.model.Identity;
import de.lpo.todo.model.Role;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class AdminPageHandlerTest {
  private final TemplateService templateService = mock();
  private final IdentityService identityService = mock();
  private final AdminPageHandler handler = new AdminPageHandler(templateService, identityService);

  @Test
  void should_respond_admin_page() {
    // GIVEN
    // - Templates
    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    // - Auth user
    final var Identity = new Identity("a", "a", "a", "a", Role.USER);
    when(identityService.getFromContext(any())).thenReturn(Identity);

    // - Context
    final Context ctx = mock();

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).admin(Identity);
  }
}
