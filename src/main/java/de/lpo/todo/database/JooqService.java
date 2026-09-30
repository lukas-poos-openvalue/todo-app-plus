package de.lpo.todo.database;

import de.lpo.todo.config.ConfigService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.JDBCUtils;

/** Provides easy access the database using the {@link DSLContext} from jOOQ. */
public final class JooqService {

  private final DSLContext dsl;

  /**
   * Creates a {@link DSLContext} configured for the database behind the given data source.
   *
   * @param configService the service to read the database dialect from
   * @param datasourceService the service providing the pooled connections
   */
  public JooqService(final ConfigService configService, final DataSourceService datasourceService) {
    final var dataSource = datasourceService.getDataSource();
    final var sqlDialect = JDBCUtils.dialect(configService.getConfig().databaseUrl());
    this.dsl = DSL.using(dataSource, sqlDialect);
  }

  /**
   * Returns the instance of the {@link DSLContext}.
   *
   * @return the DSL context
   */
  public DSLContext create() {
    return dsl;
  }
}
