package de.lpo.todo.service;

import de.lpo.todo.api.security.IdentityService;
import de.lpo.todo.model.Identity;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.repository.UserRepo;
import de.lpo.todo.repository.mapper.UserMapper;
import io.javalin.http.Context;
import java.util.List;

/**
 * Service for working with {@link User users}. A {@link User} is a domain view of the {@link
 * Identity}. Note that the access to users is primarily readonly.
 */
public final class UserService {
  private final IdentityService authUserService;
  private final UserRepo userRepo;
  private final UserMapper userMapper;

  /**
   * Creates a service backed by the given identity service, repository, and mapper.
   *
   * @param authUserService the service to resolve the current session's identity
   * @param userRepo the repository to delegate to
   * @param userMapper the mapper to convert between {@link Identity} and {@link User}
   */
  public UserService(
      final IdentityService authUserService, final UserRepo userRepo, final UserMapper userMapper) {
    this.authUserService = authUserService;
    this.userRepo = userRepo;
    this.userMapper = userMapper;
  }

  /**
   * Returns the {@link User} of the current session.
   *
   * @return the user of the current session
   * @see IdentityService#getFromContext(Context)
   */
  public User getFromContext(final Context ctx) {
    return userMapper.toDomain(authUserService.getFromContext(ctx));
  }

  /**
   * Returns a list of all users in the database, excluding the one with the given id.
   *
   * @param excludeUserId the user id to exclude from the result
   * @return a list of all users, excluding the one with the given id
   */
  public List<User> findAllUsersExcluding(final Id<String> excludeUserId) {
    return userRepo.findUsersExcluding(excludeUserId);
  }

  /**
   * Returns whether the given user should have access to the given board.
   *
   * @param userId the user id
   * @param boardId the board id
   * @return {@code true} when the given user should have access to the given board
   */
  public boolean canUserAccessBoard(final Id<String> userId, final Id<Integer> boardId) {
    return userRepo.isUserOwnerOrMemberOfBoard(userId, boardId);
  }
}
