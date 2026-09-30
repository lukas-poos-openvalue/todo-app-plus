package de.lpo.todo.repository;

import static de.lpo.todo.generated.jooq.Tables.TBL_TODO;
import static org.jooq.impl.DSL.val;

import de.lpo.todo.database.JooqService;
import de.lpo.todo.model.Board;
import de.lpo.todo.model.Todo;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.repository.mapper.TodoMapper;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

/**
 * A repository to encapsulate database access in order to manage {@link Todo} objects. These
 * operations a classic CRUD operations.
 */
public class TodoRepo {
  private final JooqService jooqService;
  private final TodoMapper todoMapper;

  /**
   * Creates a repository backed by the given jOOQ service and mapper.
   *
   * @param jooqService the service to access the database with
   * @param todoMapper the mapper to convert between database records and {@link Todo}
   */
  public TodoRepo(final JooqService jooqService, final TodoMapper todoMapper) {
    this.jooqService = jooqService;
    this.todoMapper = todoMapper;
  }

  /**
   * Adds a new {@link Todo} to the database.
   *
   * @param boardId the id of the {@link Board}
   * @param content the initial content
   * @return the id of the created {@link Todo}
   */
  public Id<Integer> add(final Id<Integer> boardId, final String content) {
    return jooqService
        .create()
        .insertInto(TBL_TODO)
        .set(TBL_TODO.BOARD_ID, boardId.value())
        .set(TBL_TODO.CONTENT, content)
        .set(TBL_TODO.COMPLETED, false)
        .returning(TBL_TODO.TODO_ID)
        .fetchSingle(r -> IntId.ofValue(r.getTodoId()));
  }

  /**
   * Selects one {@link Todo} by the given id.
   *
   * @param todoId the id of the {@link Todo}
   * @return the value of the {@link Todo} found (excluding the referenced {@link Board}), or an
   *     empty result
   */
  public Optional<Todo> findOneByIdFetchNone(final Id<Integer> todoId) {
    return jooqService
        .create()
        .selectFrom(TBL_TODO)
        .where(TBL_TODO.TODO_ID.eq(todoId.value()))
        .fetchOptional(todoMapper::toDomain);
  }

  /**
   * Updates the {@link Todo} with the provided values.
   *
   * @param todoId the id of the {@link Todo}
   * @param content the new content value (optional; if null it's left unchanged)
   * @param completed the new completed value (optional; if null it's left unchanged)
   * @return the {@link Todo} with all the updated values
   */
  public Todo updateFetchNone(
      final Id<Integer> todoId, @Nullable final String content, @Nullable final Boolean completed) {
    return jooqService
        .create()
        .update(TBL_TODO)
        .set(TBL_TODO.CONTENT, content != null ? val(content) : TBL_TODO.CONTENT)
        .set(TBL_TODO.COMPLETED, completed != null ? val(completed) : TBL_TODO.COMPLETED)
        .where(TBL_TODO.TODO_ID.eq(todoId.value()))
        .returning()
        .fetchSingle(todoMapper::toDomain);
  }

  /**
   * Deletes a {@link Todo} by the given id.
   *
   * @param todoId the id of the {@link Todo}
   */
  public void deleteById(final Id<Integer> todoId) {
    jooqService.create().delete(TBL_TODO).where(TBL_TODO.TODO_ID.eq(todoId.value())).execute();
  }
}
