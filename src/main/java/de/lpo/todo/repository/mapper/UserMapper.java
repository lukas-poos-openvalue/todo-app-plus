package de.lpo.todo.repository.mapper;

import de.lpo.todo.generated.jooq.tables.records.TblIdentityRecord;
import de.lpo.todo.model.Identity;
import de.lpo.todo.model.Role;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.StringId;

/**
 * A service that provides mapping utilities between {@link User users} and their table record
 * representation.
 */
public final class UserMapper {
  /**
   * Maps the given record to a {@link User user domain object}, filled with all the given data.
   *
   * @param identityRecord the {@link TblIdentityRecord user record}
   * @return a {@link User user domain object}, filled with all the given data
   */
  public User toDomain(final TblIdentityRecord identityRecord) {
    return new User(
        StringId.ofValue(identityRecord.getUserId()),
        identityRecord.getDisplayName(),
        Role.valueOf(identityRecord.getRole()));
  }

  /**
   * Maps the given auth user object to a {@link User user domain object}.
   *
   * @param identity the {@link Identity original auth user object}
   * @return a {@link User user domain object}, based on the original object
   */
  public User toDomain(final Identity identity) {
    return new User(StringId.ofValue(identity.userId()), identity.displayName(), identity.role());
  }
}
