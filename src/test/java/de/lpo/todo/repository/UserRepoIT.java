package de.lpo.todo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import de.lpo.todo.IntegrationTest;
import de.lpo.todo.TestUtils;
import de.lpo.todo.api.security.IdentityService;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.repository.mapper.UserMapper;
import de.lpo.todo.service.BoardService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationTest.class)
class UserRepoIT {

  @Test
  void should_confirm_that_user_is_owner_of_board(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final UserRepo userRepo) {
    // GIVEN
    // - Test-scenario
    final var board =
        TestUtils.IT.Domain.storeOneBoardTwoUsers(identityService, userMapper, boardService);
    final var owner = board.owner();

    // WHEN
    final var result = userRepo.isUserOwnerOrMemberOfBoard(owner.id(), board.id());

    // THEN
    assertThat(result).isTrue();
  }

  @Test
  void should_confirm_that_user_is_member_of_board(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final UserRepo userRepo) {
    // GIVEN
    // - Test-scenario
    final var board =
        TestUtils.IT.Domain.storeOneBoardTwoUsers(identityService, userMapper, boardService);
    final var member = board.members().unwrap().getFirst();

    // WHEN
    final var result = userRepo.isUserOwnerOrMemberOfBoard(member.id(), board.id());

    // THEN
    assertThat(result).isTrue();
  }

  @Test
  void should_reject_that_user_is_owner_or_member_of_board(
      final IdentityService identityService,
      final UserMapper userMapper,
      final BoardService boardService,
      final UserRepo userRepo) {
    // GIVEN
    // - Test-scenario
    final var board =
        TestUtils.IT.Domain.storeOneBoardTwoUsers(identityService, userMapper, boardService);
    final var userId = StringId.ofValue(RandomStringUtils.secure().next(10));

    // WHEN
    final var result = userRepo.isUserOwnerOrMemberOfBoard(userId, board.id());

    // THEN
    assertThat(result).isFalse();
  }
}
