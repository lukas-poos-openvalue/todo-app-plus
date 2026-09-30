package de.lpo.todo.repository.mapper;

import de.lpo.todo.generated.jooq.tables.records.TblTodoRecord;
import de.lpo.todo.model.Todo;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.model.refs.Ref;

/**
 * A service that provides mapping utilities between {@link Todo todos} and their table record
 * representation.
 */
public final class TodoMapper {

  /**
   * Maps the given record to a {@link Todo todo domain object}, filled with all the given data.
   *
   * @param todoRecord the {@link TblTodoRecord todo record}
   * @return a {@link Todo todo domain object}, filled with all the given data
   */
  public Todo toDomain(final TblTodoRecord todoRecord) {
    return new Todo(
        IntId.ofValue(todoRecord.getTodoId()),
        todoRecord.getContent(),
        todoRecord.getCompleted(),
        Ref.unloaded(IntId.ofValue(todoRecord.getBoardId())));
  }
}
