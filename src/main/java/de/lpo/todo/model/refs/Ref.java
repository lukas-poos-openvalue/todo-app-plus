package de.lpo.todo.model.refs;

import de.lpo.todo.model.ids.HasId;
import de.lpo.todo.model.ids.Id;

/**
 * A reference to another domain object. The reference can have two states:
 *
 * <ul>
 *   <li>Loaded: You can access the ids and the referenced domain object as well
 *   <li>Unloaded: You only have access to the ids.
 * </ul>
 *
 * @param <IdT> The type of the ID's value (usually {@link String} or {@link Integer})
 * @param <T> The type of the referenced domain object
 */
public sealed interface Ref<IdT, T extends HasId<IdT>> {
  /**
   * Creates a loaded reference, providing the referenced domain object.
   *
   * @param value the referenced domain object
   * @return a loaded reference
   */
  static <IdT, T extends HasId<IdT>> Ref<IdT, T> loaded(final T value) {
    return new Loaded<>(value);
  }

  /**
   * Creates an unloaded reference, providing only the ID.
   *
   * @param id the technical id of the referenced object
   * @return an unloaded reference
   */
  static <IdT, T extends HasId<IdT>> Ref<IdT, T> unloaded(final Id<IdT> id) {
    return new Unloaded<>(id);
  }

  /**
   * Returns the technical ID of the referenced object.
   *
   * @return the technical ID
   */
  Id<IdT> id();

  /**
   * Returns whether the reference is loaded, hence whether the referenced domain object can be
   * obtained.
   *
   * @return {@code true} if the reference is loaded
   */
  default boolean isLoaded() {
    return this instanceof Ref.Loaded<IdT, T>;
  }

  /**
   * Returns the referenced domain object if loaded, or throws an error otherwise. For safe access,
   * use pattern matching instead!
   *
   * @return the referenced domain object
   * @throws IllegalStateException if the reference is not {@link Loaded}
   */
  default T unwrap() {
    return switch (this) {
      case Loaded<IdT, T>(T value) -> value;
      case Unloaded<IdT, T> ignored ->
          throw new IllegalStateException("Cannot get value from unloaded reference!");
    };
  }

  /** A loaded reference, containing the referenced domain object. */
  record Loaded<IdT, T extends HasId<IdT>>(T value) implements Ref<IdT, T> {
    @Override
    public Id<IdT> id() {
      return value.id();
    }
  }

  /** An unloaded reference, containing only the ids of the referenced domain object. */
  record Unloaded<IdT, T extends HasId<IdT>>(Id<IdT> id) implements Ref<IdT, T> {}
}
