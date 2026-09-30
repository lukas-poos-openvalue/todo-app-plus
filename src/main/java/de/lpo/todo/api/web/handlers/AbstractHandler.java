package de.lpo.todo.api.web.handlers;

import de.lpo.todo.model.ids.Id;
import de.lpo.todo.model.ids.IntId;
import gg.jte.TemplateOutput;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jspecify.annotations.Nullable;

/**
 * Base class that makes its implementations return a {@link JteModel} (created by using {@link
 * de.lpo.todo.generated.jte.Templates}).
 */
abstract class AbstractHandler implements Handler {
  @Override
  public void handle(final Context ctx) {
    // Response is filled with rendered model
    final var model = provideModel(ctx);
    if (model != null) {
      ctx.html(model.render());
    }
  }

  /**
   * Provides the model to render for the given request, or writes an error response directly to
   * {@code ctx} and returns {@code null}.
   *
   * @param ctx the request context
   * @return the model to render, or {@code null} if an error response was already written
   */
  protected abstract @Nullable JteModel provideModel(final Context ctx);

  /**
   * Reads the board id from the {@code {board-id-hashed}} path parameter.
   *
   * @param ctx the request context
   * @return the board id
   */
  protected Id<Integer> getBoardIdFromPath(final Context ctx) {
    final var boardId = IntId.ofHash(ctx.pathParam("{board-id-hashed}"));
    if (boardId == null) {
      throw new BadRequestResponse("URL does not contain board id!");
    }
    return boardId;
  }

  /**
   * Reads the todo id from the {@code {todo-id-hashed}} path parameter.
   *
   * @param ctx the request context
   * @return the todo id
   */
  protected Id<Integer> getTodoIdFromPath(final Context ctx) {
    final var todoId = IntId.ofHash(ctx.pathParam("{todo-id-hashed}"));
    if (todoId == null) {
      throw new BadRequestResponse("URL does not contain todo id!");
    }
    return todoId;
  }

  /**
   * Returns a model that renders to an empty response.
   *
   * @return the empty model
   */
  protected JteModel emptyModel() {
    return new JteModel() {
      @Override
      public void render(final TemplateOutput output) {
        output.writeContent("");
      }

      @Override
      public void writeTo(final TemplateOutput output) {
        output.writeContent("");
      }
    };
  }
}
