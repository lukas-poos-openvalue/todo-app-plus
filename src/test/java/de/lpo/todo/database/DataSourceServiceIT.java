package de.lpo.todo.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.lpo.todo.IntegrationTest;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationTest.class)
class DataSourceServiceIT {
  @Test
  void should_provide_working_datasource(final DataSourceService dataSourceService)
      throws SQLException {
    // GIVEN
    final var datasource = dataSourceService.getDataSource();

    // WHEN
    int result = -1;
    try (final var conn = datasource.getConnection()) {
      final var rs = conn.createStatement().executeQuery("select 1");
      if (rs.next()) {
        result = rs.getInt(1);
      }
    }

    // THEN
    assertThat(result).isEqualTo(1);
  }
}
