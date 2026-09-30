package de.lpo.todo.model;

import de.lpo.todo.model.ids.HasId;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.model.refs.Ref;

/** A simple task on a {@link Board}. */
public record Todo(Id<Integer> id, String content, Boolean completed, Ref<Integer, Board> board)
    implements HasId<Integer> {}
