package de.lpo.todo;

import de.lpo.todo.context.ApplicationContext;
import io.javalin.Javalin;
import io.javalin.compression.Brotli;
import io.javalin.compression.CompressionStrategy;
import io.javalin.compression.Gzip;

/**
 * Entry point of the application: wires up the {@link ApplicationContext context} and the {@link
 * Javalin application}.
 */
public final class TodoAppPlus implements AutoCloseable {
  final ApplicationContext context = new ApplicationContext();
  final Javalin application = setupApplication();

  @SuppressWarnings("resource")
  static void main() {
    final var todoAppPlus = new TodoAppPlus();
    todoAppPlus.start();
  }

  /** Runs pending database migrations, then starts the HTTP server. */
  public void start() {
    // Run the migration
    context.migrationService.updateDatabase();

    // Start the server with the configured port
    final var config = context.configService.getConfig();
    application.start(config.serverPort());
  }

  @Override
  public void close() {
    application.stop();
  }

  private Javalin setupApplication() {
    return Javalin.create(
        appConfig -> {
          // General config
          appConfig.concurrency.useVirtualThreads = true;
          appConfig.http.compressionStrategy = new CompressionStrategy(new Brotli(4), new Gzip(4));

          // Bootstrap the API
          context.apiBootstrapService.bootstrap(appConfig);

          // Register shutdown hooks
          appConfig.events.serverStopped(context::close);
          Runtime.getRuntime()
              .addShutdownHook(
                  new Thread(
                      () -> {
                        try {
                          context.close();
                        } catch (final Exception e) {
                          throw new RuntimeException(e);
                        }
                      }));
        });
  }
}
