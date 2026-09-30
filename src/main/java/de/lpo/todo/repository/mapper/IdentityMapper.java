package de.lpo.todo.repository.mapper;

import de.lpo.todo.generated.jooq.tables.records.TblIdentityRecord;
import de.lpo.todo.model.Board;
import de.lpo.todo.model.Identity;
import de.lpo.todo.model.Role;

/**
 * A service that provides mapping utilities between {@link Board boards} and their table record
 * representation.
 */
public final class IdentityMapper {

  /**
   * Maps the given {@link Identity} to its table record representation.
   *
   * @param identity the identity to map
   * @return the table record
   */
  public TblIdentityRecord toDatabase(final Identity identity) {
    return new TblIdentityRecord(
        identity.userId(),
        identity.userName(),
        identity.email(),
        identity.displayName(),
        identity.role().name());
  }

  /**
   * Maps the given table record to an {@link Identity}.
   *
   * @param record the table record to map
   * @return the identity
   */
  public Identity toDomain(final TblIdentityRecord record) {
    return new Identity(
        record.getUserId(),
        record.getName(),
        record.getEmail(),
        record.getDisplayName(),
        Role.valueOf(record.getRole()));
  }
}
