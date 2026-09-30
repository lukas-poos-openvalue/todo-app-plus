package de.lpo.todo.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.lpo.todo.IntegrationTest;
import de.lpo.todo.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationTest.class)
class MigrationServiceIT {

  @Test
  void should_perform_database_migration() {
    // WHEN
    final var resultTableNames = TestUtils.IT.Database.getAllTableNames();

    // THEN
    final var expectedTableNames = TestUtils.IT.Database.TABLE_NAMES;
    assertThat(resultTableNames).containsAll(expectedTableNames);
  }
}
