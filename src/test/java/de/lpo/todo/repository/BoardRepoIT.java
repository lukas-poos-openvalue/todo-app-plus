package de.lpo.todo.repository;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

import de.lpo.todo.IntegrationTest;
import de.lpo.todo.TestUtils;
import de.lpo.todo.api.security.IdentityService;
import de.lpo.todo.model.Board;
import de.lpo.todo.model.Todo;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.repository.mapper.UserMapper;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.TodoService;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationTest.class)
class BoardRepoIT {

  @Test
  void should_add(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardRepo boardRepo) {
    // GIVEN
    // - Test-Scenario
    final var users = TestUtils.IT.Domain.storeThreeUsers(identityService, userMapper);

    // - Board data
    final var title = RandomStringUtils.secure().next(10);
    final var ownerId = users.getFirst().id();
    final var memberIds = List.of(users.getLast().id());

    // WHEN
    final var result = boardRepo.add(title, ownerId, memberIds);

    // THEN
    assertThat(result).isNotNull();
  }

  @Test
  void should_find_one_by_id_fetching_all(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final BoardRepo boardRepo,
      final TodoService todoService) {
    // GIVEN
    // - Test-Scenario
    final var todo =
        TestUtils.IT.Domain.storeOneTodoOneBoardTwoUsers(
            identityService, userMapper, boardService, todoService);
    final var boardId = requireNonNull(todo.board()).id();

    // WHEN
    final var result = boardRepo.findOneByIdFetchAll(boardId);

    // THEN
    final var assertBoard =
        assertThat(result)
            .isPresent()
            .get()
            .satisfies(b -> assertThat(b).extracting(Board::title).isEqualTo("title"));
    assertBoard
        .extracting(Board::owner)
        .extracting(TestUtils.IT.Domain::extractRef)
        .extracting(User::displayName)
        .isEqualTo("displayNameA");
    assertBoard
        .extracting(Board::members)
        .extracting(TestUtils.IT.Domain::extractRefs, list(User.class))
        .hasSize(1)
        .first()
        .extracting(User::displayName)
        .isEqualTo("displayNameB");
    assertBoard
        .extracting(Board::todos)
        .extracting(TestUtils.IT.Domain::extractRefs, list(Todo.class))
        .hasSize(1)
        .first()
        .extracting(Todo::id, Todo::content, Todo::content, Todo::completed)
        .containsExactly(todo.id(), todo.content(), todo.content(), todo.completed());
  }

  @Test
  void should_find_all_for_owner(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final BoardRepo boardRepo) {
    // GIVEN
    // - Test-Scenario
    final var board =
        TestUtils.IT.Domain.storeOneBoardTwoUsers(identityService, userMapper, boardService);
    final var owner = board.owner().unwrap();

    // WHEN
    final var result = boardRepo.findAllForUserFetchAll(owner);

    // THEN
    assertThat(result)
        .hasSize(1)
        .first()
        .extracting(
            Board::id,
            Board::title,
            x -> x.todos().unwrap().size(),
            x -> x.owner().unwrap().displayName(),
            x -> x.members().unwrap().getFirst().displayName())
        .containsExactly(board.id(), board.title(), 0, "displayNameA", "displayNameB");
  }

  @Test
  void should_find_all_for_member(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final BoardRepo boardRepo) {
    // GIVEN
    // - Test-Scenario
    final var board =
        TestUtils.IT.Domain.storeOneBoardTwoUsers(identityService, userMapper, boardService);
    final var member = board.members().unwrap().getFirst();

    // WHEN
    final var result = boardRepo.findAllForUserFetchAll(member);

    // THEN
    assertThat(result)
        .hasSize(1)
        .first()
        .extracting(
            Board::id,
            Board::title,
            x -> x.todos().unwrap().size(),
            x -> x.owner().unwrap().displayName(),
            x -> x.members().unwrap().getFirst().displayName())
        .containsExactly(board.id(), board.title(), 0, "displayNameA", "displayNameB");
  }

  @Test
  void should_update(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final BoardRepo boardRepo) {
    // GIVEN
    // - Test-Scenario
    final var board =
        TestUtils.IT.Domain.storeOneBoardTwoUsers(identityService, userMapper, boardService);

    // - Updated board
    final var title = RandomStringUtils.secure().next(10);
    final List<Id<String>> memberIds = List.of();

    // WHEN
    boardRepo.update(board.id(), title, memberIds);

    // THEN
  }

  @Test
  void should_delete_by_id(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final BoardRepo boardRepo) {
    // GIVEN
    // - Test-scenario
    final var board =
        TestUtils.IT.Domain.storeOneBoardTwoUsers(identityService, userMapper, boardService);
    final var boardId = board.id();

    // WHEN
    boardRepo.deleteById(boardId);

    // THEN
    assertThat(boardRepo.findOneByIdFetchAll(boardId)).isEmpty();
  }
}
