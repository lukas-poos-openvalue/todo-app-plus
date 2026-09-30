package de.lpo.todo.service;

import de.lpo.todo.model.Board;
import de.lpo.todo.model.Todo;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.repository.TodoRepo;
import org.jspecify.annotations.Nullable;

/** Service for managing todos. It provides the basic CRUD functionalities. */
public final class TodoService {
  private final TodoRepo todoRepo;

  /**
   * Creates a service backed by the given repository.
   *
   * @param todoRepo the repository to delegate to
   */
  public TodoService(final TodoRepo todoRepo) {
    this.todoRepo = todoRepo;
  }

  /**
   * Adds a {@link Todo} to the database, and returns the created object. Note that the reference to
   * the board remains unloaded.
   *
   * @param boardId the id of the {@link Board} that this todo belongs to.
   * @param content the content of the task
   * @return the created {@link Todo} (without the board)
   */
  public Todo addFetchNone(final Id<Integer> boardId, final String content) {
    final var todoId = todoRepo.add(boardId, content);
    return new Todo(todoId, content, false, Ref.unloaded(boardId));
  }

  /**
   * Selects one {@link Todo} from the database by the given id, without loading its board.
   *
   * @param todoId the id of the {@link Todo}
   * @return the value of the {@link Todo} with the given id (without the board), or an empty value
   */
  public Todo getOneByIdFetchNone(final Id<Integer> todoId) {
    return todoRepo.findOneByIdFetchNone(todoId).orElseThrow(TodoNotFoundException::new);
  }

  /**
   * Performs updates to the {@link Todo}.
   *
   * @param todoId the id
   * @param content the updated content (remains unchanged when null)
   * @param completed the updated completed flag (remains unchanged when null)
   * @return the {@link Todo} with the updated values
   */
  public Todo updateFetchNone(
      final Id<Integer> todoId, @Nullable final String content, @Nullable final Boolean completed) {
    return todoRepo.updateFetchNone(todoId, content, completed);
  }

  /**
   * Deletes a {@link Todo}.
   *
   * @param todoId the id
   */
  public void deleteById(final Id<Integer> todoId) {
    todoRepo.deleteById(todoId);
  }

  /** Thrown when no {@link Todo} can be found for a given ID. */
  public static class TodoNotFoundException extends RuntimeException {
    /** Creates the exception with a fixed, descriptive message. */
    public TodoNotFoundException() {
      super("No todo was found with the given ID!");
    }
  }
}
