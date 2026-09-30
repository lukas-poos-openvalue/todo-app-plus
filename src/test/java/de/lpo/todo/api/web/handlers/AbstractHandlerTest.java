package de.lpo.todo.api.web.handlers;

import static org.mockito.Mockito.*;

import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class AbstractHandlerTest {

  @Test
  void should_render_model_as_html() {
    // GIVEN
    final Context ctx = mock();
    final JteModel model = mock();
    when(model.render()).thenReturn("Hello World!");
    final var handler = mockJteHandler(model);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx).html("Hello World!");
  }

  @Test
  void should_ignore_null_model() {
    // GIVEN
    final Context ctx = mock();
    final var handler = mockJteHandler(null);

    // WHEN
    handler.handle(ctx);

    // THEN
    verify(ctx, never()).html(any());
  }

  private AbstractHandler mockJteHandler(@Nullable final JteModel result) {
    return new AbstractHandler() {
      @Override
      protected @Nullable JteModel provideModel(final Context ctx) {
        return result;
      }
    };
  }
}
