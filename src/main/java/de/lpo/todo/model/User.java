package de.lpo.todo.model;

import de.lpo.todo.model.ids.HasId;
import de.lpo.todo.model.ids.Id;

/** Domain-representation of {@link Identity}. */
public record User(Id<String> id, String displayName, Role role) implements HasId<String> {}
