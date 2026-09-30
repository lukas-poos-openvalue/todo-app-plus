package de.lpo.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.lpo.todo.model.Todo;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.repository.TodoRepo;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

class TodoServiceTest {
  private final TodoRepo todoRepo = mock();
  private final TodoService todoService = new TodoService(todoRepo);

  private static Todo oneTodo(int id) {
    return new Todo(IntId.ofValue(id), "content", true, Ref.unloaded(IntId.ofValue(1)));
  }

  @Test
  void should_add() {
    // GIVEN
    // - Add data
    final var boardId = IntId.ofValue(RandomUtils.secure().randomInt());
    final var content = RandomStringUtils.secure().next(10);

    // - Mock repo
    final var newTodoId = IntId.ofValue(RandomUtils.secure().randomInt());
    when(todoRepo.add(any(), any())).thenReturn(newTodoId);

    // WHEN
    final var result = todoService.addFetchNone(boardId, content);

    // THEN
    assertThat(result)
        .extracting(Todo::id, Todo::content, Todo::completed, t -> t.board().id())
        .containsExactly(newTodoId, content, false, boardId);
    verify(todoRepo).add(boardId, content);
  }

  @Test
  void should_find_by_id() {
    // GIVEN
    // - Existing Todo
    final var todo = oneTodo(RandomUtils.secure().randomInt());

    // - Mock repo
    when(todoRepo.findOneByIdFetchNone(any())).thenReturn(Optional.of(todo));

    // WHEN
    final var result = todoService.getOneByIdFetchNone(todo.id());

    // THEN
    assertThat(result).isEqualTo(todo);
    verify(todoRepo).findOneByIdFetchNone(todo.id());
  }

  @Test
  void should_update() {
    // GIVEN
    // - Existing Todo
    final var todo = oneTodo(RandomUtils.secure().randomInt());

    // - Mock repo
    final var updatedContent = RandomStringUtils.secure().next(10);
    final var updatedCompleted = true;
    final var updatedTodo = new Todo(todo.id(), updatedContent, updatedCompleted, todo.board());
    when(todoRepo.updateFetchNone(any(), any(), any())).thenReturn(updatedTodo);

    // WHEN
    final var result = todoService.updateFetchNone(todo.id(), updatedContent, updatedCompleted);

    // THEN
    assertThat(result).isEqualTo(updatedTodo);
    verify(todoRepo).updateFetchNone(todo.id(), updatedContent, updatedCompleted);
  }

  @Test
  void should_delete() {
    // GIVEN
    final var todoId = IntId.ofValue(RandomUtils.secure().randomInt());

    // WHEN
    todoService.deleteById(todoId);

    // THEN
    verify(todoRepo).deleteById(todoId);
  }
}
