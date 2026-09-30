package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import de.lpo.todo.TestUtils;
import de.lpo.todo.api.web.TemplateService;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

class IndexPageHandlerTest {
  private final TemplateService templateService = mock();
  private final IndexPageHandler handler = new IndexPageHandler(templateService);

  @Test
  void should_respond_index_page() {
    // GIVEN
    final Context ctx = mock();
    final var templates = TestUtils.Web.mockTemplates();
    when(templateService.getTemplates()).thenReturn(templates);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html(any());
    verify(templates).index();
  }
}
