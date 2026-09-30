package de.lpo.todo.service;

import de.lpo.todo.model.Board;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.repository.BoardRepo;
import java.util.List;
import org.jspecify.annotations.Nullable;

/** Service for managing {@link Board boards}. Provides the basic CRUD functionality. */
public final class BoardService {
  private final BoardRepo boardRepo;

  /**
   * Creates a service backed by the given repository.
   *
   * @param boardRepo the repository to delegate to
   */
  public BoardService(final BoardRepo boardRepo) {
    this.boardRepo = boardRepo;
  }

  /**
   * Adds a new {@link Board} to the database.
   *
   * @param title the title
   * @param ownerId the id of the {@link User owner}
   * @param memberIds the ids of the {@link User members}
   * @return the new {@link Board board-instance}, with all references loaded.
   */
  public Board addFetchAll(
      final String title, final Id<String> ownerId, final List<Id<String>> memberIds) {
    final var boardId = boardRepo.add(title, ownerId, memberIds);
    return boardRepo.findOneByIdFetchAll(boardId).orElseThrow(BoardNotFoundException::new);
  }

  /**
   * Selects all the boards that the given {@link User} belongs to.
   *
   * @param user the {@link User}
   * @return a list of {@link Board boards}
   */
  public List<Board> findAllForUserFetchAll(final User user) {
    return boardRepo.findAllForUserFetchAll(user);
  }

  /**
   * Selects one {@link Board} from the database by the given id.
   *
   * @param boardId the hashed id
   * @return the value of the {@link Board} found or an empty result
   */
  public Board getOneByIdFetchAll(final Id<Integer> boardId) {
    return boardRepo.findOneByIdFetchAll(boardId).orElseThrow(BoardNotFoundException::new);
  }

  /**
   * Performs updates to the given {@link Board}.
   *
   * @param boardId the hashed id
   * @param title the title (remains untouched if null)
   * @param memberIds (remain untouched if null)
   * @return the {@link Board} with the updated values, with all references loaded
   */
  public Board updateFetchAll(
      final Id<Integer> boardId,
      @Nullable final String title,
      @Nullable final List<Id<String>> memberIds) {
    boardRepo.update(boardId, title, memberIds);
    return boardRepo.findOneByIdFetchAll(boardId).orElseThrow(BoardNotFoundException::new);
  }

  /**
   * Deletes a {@link Board}.
   *
   * @param boardId the hashed id
   */
  public void deleteById(final Id<Integer> boardId) {
    boardRepo.deleteById(boardId);
  }

  /** Thrown when no {@link Board} can be found for a given ID. */
  public static class BoardNotFoundException extends RuntimeException {
    /** Creates the exception with a fixed, descriptive message. */
    public BoardNotFoundException() {
      super("No board was found with the given ID!");
    }
  }
}
