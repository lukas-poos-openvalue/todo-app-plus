package de.lpo.todo.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.lpo.todo.config.ConfigService;
import javax.sql.DataSource;

/** The service provides pooled connections to the database with a {@link DataSource}. */
public final class DataSourceService implements AutoCloseable {
  private final HikariDataSource dataSource;

  /**
   * Creates a pooled {@link DataSource} from the database settings in the given config.
   *
   * @param configService the service to read the database settings from
   */
  public DataSourceService(final ConfigService configService) {
    // Get the application config (for database info)
    final var config = configService.getConfig();

    // Configure a HikariDataSource
    final var hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(config.databaseUrl());
    hikariConfig.setUsername(config.databaseUsername());
    hikariConfig.setPassword(config.databasePassword());

    // Create and save the DataSource
    this.dataSource = new HikariDataSource(hikariConfig);
  }

  @Override
  public void close() {
    dataSource.close();
  }

  /**
   * Returns the {@link DataSource} that provides pooled connections to the database.
   *
   * @return the data source
   */
  public DataSource getDataSource() {
    return dataSource;
  }
}
