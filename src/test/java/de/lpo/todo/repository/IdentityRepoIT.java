package de.lpo.todo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import de.lpo.todo.IntegrationTest;
import de.lpo.todo.model.Identity;
import de.lpo.todo.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationTest.class)
class IdentityRepoIT {

  @Test
  void should_save_and_find_by_id(final IdentityRepo identityRepo) {
    // GIVEN
    final var identity = new Identity("id", "name", "mail", "display", Role.USER);

    // WHEN
    identityRepo.save(identity);
    final var result = identityRepo.findOneById(identity.userId());

    // THEN
    assertThat(result).hasValue(identity);
  }
}
