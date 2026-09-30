package de.lpo.todo.model;

import de.lpo.todo.model.ids.HasId;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.model.refs.Refs;

/**
 * A board is a collection of {@link Todo todos} that is owned by a {@link User}. Boards can have
 * multiple {@link User members}, who can access the board and its {@link Todo todos} as well.
 */
public record Board(
    Id<Integer> id, String title, Refs<Todo> todos, Ref<String, User> owner, Refs<User> members)
    implements HasId<Integer> {}
