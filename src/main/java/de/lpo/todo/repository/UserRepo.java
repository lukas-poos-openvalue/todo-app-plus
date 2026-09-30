package de.lpo.todo.repository;

import static de.lpo.todo.generated.jooq.Tables.TBL_BOARD;
import static de.lpo.todo.generated.jooq.Tables.TBL_BOARD_MEMBER;
import static de.lpo.todo.generated.jooq.Tables.TBL_IDENTITY;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;

import de.lpo.todo.database.JooqService;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.repository.mapper.UserMapper;
import java.util.List;
import org.jooq.Record1;

/** A repository to encapsulate database access in order to manage {@link User} objects. */
public final class UserRepo {
  private final JooqService jooqService;
  private final UserMapper userMapper;

  /**
   * Creates a repository backed by the given jOOQ service and mapper.
   *
   * @param jooqService the service to access the database with
   * @param userMapper the mapper to convert between database records and {@link User}
   */
  public UserRepo(final JooqService jooqService, final UserMapper userMapper) {
    this.jooqService = jooqService;
    this.userMapper = userMapper;
  }

  /**
   * Returns a list of all {@link User users} excluding the user with the given id.
   *
   * @param excludeUserId the user id to exclude
   * @return a list of all {@link User users} excluding the user with the given id
   */
  public List<User> findUsersExcluding(final Id<String> excludeUserId) {
    return jooqService
        .create()
        .selectFrom(TBL_IDENTITY)
        .where(TBL_IDENTITY.USER_ID.notEqual(excludeUserId.value()))
        .orderBy(TBL_IDENTITY.DISPLAY_NAME)
        .fetch(userMapper::toDomain);
  }

  /**
   * Returns whether the user is either owner or member of the board.
   *
   * @param userId the user id
   * @param boardId the board id
   * @return {@code true} if the user is either owner or member of the board
   */
  public boolean isUserOwnerOrMemberOfBoard(final Id<String> userId, final Id<Integer> boardId) {
    return jooqService
        .create()
        // select true if there are fitting records
        .select(field(count().gt(0)))

        // from board joining all members
        .from(TBL_BOARD)
        .leftJoin(TBL_BOARD_MEMBER)
        .on(TBL_BOARD_MEMBER.BOARD_ID.eq(TBL_BOARD.BOARD_ID))

        // where the board id fits and the user is either owner or member
        .where(
            TBL_BOARD
                .BOARD_ID
                .eq(boardId.value())
                .and(
                    TBL_BOARD
                        .OWNER_ID
                        .eq(userId.value())
                        .or(TBL_BOARD_MEMBER.USER_ID.eq(userId.value()))))

        // execute and get the result
        .fetchSingle(Record1::value1);
  }
}
