package de.lpo.todo.model.refs;

import de.lpo.todo.model.ids.HasId;
import java.util.List;

/**
 * A list of references domain objects. This list of references can have two states:
 *
 * <ul>
 *   <li>Loaded: You can access the referenced domain objects
 *   <li>Unloaded: You can access nothing
 * </ul>
 *
 * @param <T> The type of the referenced domain objects
 */
public sealed interface Refs<T extends HasId<?>> {
  /**
   * Creates a loaded list of references, providing the referenced domain objects.
   *
   * @param values the referenced domain objects
   * @return a loaded list of references
   */
  static <T extends HasId<?>> Refs<T> loaded(final List<T> values) {
    return new Loaded<>(values);
  }

  /**
   * Creates an unloaded list of references, providing nothing.
   *
   * @return an unloaded list of references
   */
  static <T extends HasId<?>> Refs<T> unloaded() {
    return new Unloaded<>();
  }

  /**
   * Returns the referenced domain objects if loaded, or throws an error otherwise. For safe access,
   * use pattern matching instead!
   *
   * @return the referenced domain objects
   */
  default List<T> unwrap() {
    if (this instanceof Loaded<T>(List<T> values)) {
      return values;
    }
    throw new IllegalStateException("Cannot get values from unloaded references!");
  }

  /** A loaded list of references, containing the referenced domain objects. */
  record Loaded<T extends HasId<?>>(List<T> values) implements Refs<T> {}

  /** An unloaded list of references, containing nothing. */
  record Unloaded<T extends HasId<?>>() implements Refs<T> {}
}
