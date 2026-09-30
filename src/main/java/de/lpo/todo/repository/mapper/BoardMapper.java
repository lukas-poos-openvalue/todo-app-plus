package de.lpo.todo.repository.mapper;

import de.lpo.todo.generated.jooq.tables.records.TblBoardRecord;
import de.lpo.todo.generated.jooq.tables.records.TblIdentityRecord;
import de.lpo.todo.generated.jooq.tables.records.TblTodoRecord;
import de.lpo.todo.model.Board;
import de.lpo.todo.model.Todo;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.model.refs.Refs;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A service that provides mapping utilities between {@link Board boards} and their table record
 * representation.
 */
public final class BoardMapper {
  private final TodoMapper todoMapper;
  private final UserMapper userMapper;

  /**
   * Creates a mapper backed by the given todo and user mappers.
   *
   * @param todoMapper the mapper to convert {@link Todo} records
   * @param userMapper the mapper to convert {@link User} records
   */
  public BoardMapper(final TodoMapper todoMapper, final UserMapper userMapper) {
    this.todoMapper = todoMapper;
    this.userMapper = userMapper;
  }

  /**
   * Maps the given records to a {@link Board board domain object}, filled with all the given data.
   *
   * @param boardRecord the {@link TblBoardRecord board record} (required)
   * @param todoRecords the {@link TblTodoRecord task records} (optional; if null {@link
   *     Refs.Unloaded} is used)
   * @param ownerRecord the {@link TblIdentityRecord owner record} (optional; if null {@link
   *     Ref.Unloaded} is used)
   * @param memberRecords the {@link TblIdentityRecord member records} (optional; if null {@link
   *     Refs.Unloaded} is used)
   * @return a {@link Board board domain object}, filled with all the given data
   */
  public Board toDomain(
      final TblBoardRecord boardRecord,
      @Nullable final List<TblTodoRecord> todoRecords,
      @Nullable final TblIdentityRecord ownerRecord,
      @Nullable final List<TblIdentityRecord> memberRecords) {
    // Read data directly from board record
    final var id = boardRecord.getBoardId();
    final var title = boardRecord.getTitle();

    // Wrap todos into loaded references
    final Refs<Todo> todoRefs =
        todoRecords != null
            ? Refs.loaded(
                todoRecords.stream()
                    .filter(t -> t.getTodoId() != null)
                    .map(todoMapper::toDomain)
                    .toList())
            : Refs.unloaded();

    // Wrap owner into a loaded reference
    final Ref<String, User> ownerRef =
        ownerRecord != null
            ? Ref.loaded(userMapper.toDomain(ownerRecord))
            : Ref.unloaded(StringId.ofValue(boardRecord.getOwnerId()));

    // Wrap members into loaded references
    final Refs<User> memberRefs =
        memberRecords != null
            ? Refs.loaded(
                memberRecords.stream()
                    .filter(r -> r.getUserId() != null)
                    .map(userMapper::toDomain)
                    .toList())
            : Refs.unloaded();

    // Create the resulting board object
    return new Board(IntId.ofValue(id), title, todoRefs, ownerRef, memberRefs);
  }
}
