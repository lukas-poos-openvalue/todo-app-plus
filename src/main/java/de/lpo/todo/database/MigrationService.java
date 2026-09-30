package de.lpo.todo.database;

import java.lang.invoke.MethodHandles;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.UpdateSummaryOutputEnum;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.command.core.helpers.ShowSummaryArgument;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.ui.LoggerUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Service for running DB migrations.
 *
 * <p>Uses {@link Liquibase} for the execution of the updates. The migration scripts can be found in
 * the resources.
 */
public final class MigrationService {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String CHANGELOG_PATH = "db-migrations/master.yml";

  static {
    if (!SLF4JBridgeHandler.isInstalled()) {
      SLF4JBridgeHandler.removeHandlersForRootLogger(); // drop JUL's default console handler
      SLF4JBridgeHandler.install();
    }
  }

  private final DataSourceService datasourceService;

  /**
   * Creates a migration service backed by the given data source.
   *
   * @param datasourceService the service providing the pooled connections
   */
  public MigrationService(final DataSourceService datasourceService) {
    this.datasourceService = datasourceService;
  }

  /** Executes updates to the database using {@link Liquibase}. */
  public void updateDatabase() {
    try (final var jdbcConnection = datasourceService.getDataSource().getConnection()) {
      // Create a database wrapper with the JDBC connection
      final var database =
          DatabaseFactory.getInstance()
              .findCorrectDatabaseImplementation(new JdbcConnection(jdbcConnection));

      // Create an update command passing the database and the path to the changelogs
      final var updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
      updateCommand.addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database);
      updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, CHANGELOG_PATH);
      updateCommand.addArgumentValue(
          ShowSummaryArgument.SHOW_SUMMARY_OUTPUT, UpdateSummaryOutputEnum.LOG);

      // Run the update command
      LOG.info("Performing database migration with Liquibase");
      Scope.child(Scope.Attr.ui, new LoggerUIService(), updateCommand::execute);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }
}
