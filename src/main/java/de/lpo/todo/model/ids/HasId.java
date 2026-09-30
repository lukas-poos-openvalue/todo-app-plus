package de.lpo.todo.model.ids;

/**
 * Common interface for domain objects, for they all provide IDs. Is primarily used for making a
 * domain object referencable.
 */
public interface HasId<T> {
  /**
   * Returns the ID of the domain object.
   *
   * @return the ID
   */
  Id<T> id();
}
