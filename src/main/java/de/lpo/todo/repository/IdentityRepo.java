package de.lpo.todo.repository;

import static de.lpo.todo.generated.jooq.Tables.TBL_IDENTITY;

import de.lpo.todo.database.JooqService;
import de.lpo.todo.model.Identity;
import de.lpo.todo.repository.mapper.IdentityMapper;
import java.util.Optional;

/** A repository to encapsulate database access in order to manage {@link Identity} objects. */
public final class IdentityRepo {

  private final JooqService jooqService;
  private final IdentityMapper identityMapper;

  /**
   * Creates a repository backed by the given jOOQ service and mapper.
   *
   * @param jooqService the service to access the database with
   * @param identityMapper the mapper to convert between database records and {@link Identity}
   */
  public IdentityRepo(final JooqService jooqService, final IdentityMapper identityMapper) {
    this.jooqService = jooqService;
    this.identityMapper = identityMapper;
  }

  /**
   * Saves the given identity, updating the record if it already exists.
   *
   * @param identity the {@link Identity} to store. The record is updated when it already exists.
   */
  public void save(final Identity identity) {
    final var identityRecord = identityMapper.toDatabase(identity);
    jooqService
        .create()
        .insertInto(TBL_IDENTITY)
        .set(identityRecord)
        .onDuplicateKeyUpdate()
        .set(identityRecord)
        .execute();
  }

  /**
   * Selects one {@link Identity} from the database by the given id.
   *
   * @param userId the id of the {@link Identity}
   * @return a value for the {@link Identity} found, or an empty result
   */
  public Optional<Identity> findOneById(final String userId) {
    return jooqService
        .create()
        .selectFrom(TBL_IDENTITY)
        .where(TBL_IDENTITY.USER_ID.eq(userId))
        .fetchOptional(identityMapper::toDomain);
  }
}
