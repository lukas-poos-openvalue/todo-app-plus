package de.lpo.todo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import de.lpo.todo.IntegrationTest;
import de.lpo.todo.TestUtils;
import de.lpo.todo.api.security.IdentityService;
import de.lpo.todo.model.Todo;
import de.lpo.todo.repository.mapper.UserMapper;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.TodoService;
import java.util.Objects;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationTest.class)
class TodoRepoIT {

  @Test
  void should_add(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final TodoRepo todoRepo) {
    // GIVEN
    // - Test-Scenario
    final var board =
        TestUtils.IT.Domain.storeOneBoardTwoUsers(identityService, userMapper, boardService);

    // - Domain-Object to create
    final var content = RandomStringUtils.secure().next(10);

    // WHEN
    final var result = todoRepo.add(board.id(), content);

    // THEN
    assertThat(result).isNotNull();
  }

  @Test
  void should_find_by_id_fetching_board(
      final IdentityService identityService,
      final UserMapper userMapper,
      final TodoService todoService,
      final BoardService boardService,
      final TodoRepo todoRepo) {
    // GIVEN
    // - Test-Scenario
    final var todo =
        TestUtils.IT.Domain.storeOneTodoOneBoardTwoUsers(
            identityService, userMapper, boardService, todoService);
    final var todoId = Objects.requireNonNull(todo.id());

    // WHEN
    final var result = todoRepo.findOneByIdFetchNone(todoId);

    // THEN
    assertThat(result)
        .isPresent()
        .get()
        .extracting(Todo::content, Todo::completed, t -> t.board().isLoaded())
        .containsExactly(todo.content(), todo.completed(), false);
  }

  @Test
  void should_update(
      final IdentityService identityService,
      final UserMapper userMapper,
      final TodoService todoService,
      final BoardService boardService,
      final TodoRepo todoRepo) {
    // GIVEN
    // - Test-Scenario
    final var todo =
        TestUtils.IT.Domain.storeOneTodoOneBoardTwoUsers(
            identityService, userMapper, boardService, todoService);

    // - Domain-Object to update
    final var content = RandomStringUtils.secure().next(10);
    final var completed = true;

    // WHEN
    final var result = todoRepo.updateFetchNone(todo.id(), content, completed);

    // THEN
    assertThat(result)
        .extracting(Todo::id, Todo::content, Todo::completed, t -> t.board().isLoaded())
        .containsExactly(todo.id(), content, completed, false);
  }

  @Test
  void should_delete(
      final IdentityService identityService,
      final UserMapper userMapper,
      final TodoService todoService,
      final BoardService boardService,
      final TodoRepo todoRepo) {
    // GIVEN
    // - Test-Scenario
    final var todo =
        TestUtils.IT.Domain.storeOneTodoOneBoardTwoUsers(
            identityService, userMapper, boardService, todoService);
    final var todoId = todo.id();

    // WHEN
    todoRepo.deleteById(todoId);

    // THEN
    assertThat(todoRepo.findOneByIdFetchNone(todoId)).isEmpty();
  }
}
