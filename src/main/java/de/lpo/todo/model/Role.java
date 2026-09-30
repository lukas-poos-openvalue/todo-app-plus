package de.lpo.todo.model;

import io.javalin.security.RouteRole;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/** The available roles that an authenticated user can have. */
public enum Role implements RouteRole {
  ADMIN,
  USER;

  /**
   * Returns the first role that can be found in the given collection of names.
   *
   * @param names the names to search
   * @return the first matching role
   */
  public static Optional<Role> tryFirstValueOf(final Collection<String> names) {
    return names.stream()
        .flatMap(r -> tryValueOf(r).stream())
        .min(Comparator.comparingInt(Role::ordinal));
  }

  /**
   * Behaves like {@link #valueOf(String)}, but without the exception.
   *
   * @param name the name to look up
   * @return the matching role, if any
   */
  public static Optional<Role> tryValueOf(final String name) {
    if (StringUtils.isBlank(name)) {
      return Optional.empty();
    }
    return Arrays.stream(values()).filter(v -> v.name().equals(name)).findFirst();
  }
}
