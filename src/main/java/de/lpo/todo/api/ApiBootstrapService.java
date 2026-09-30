package de.lpo.todo.api;

import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import de.lpo.todo.config.Profile;
import de.lpo.todo.context.ApplicationContext;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;

/**
 * This service is responsible to bootstrap all APIs to the {@link io.javalin.Javalin application}.
 */
public final class ApiBootstrapService {
  private final ApplicationContext context;

  /**
   * Creates a bootstrap service backed by the given application context.
   *
   * @param context the {@link ApplicationContext} providing the handlers and services to wire up
   */
  public ApiBootstrapService(final ApplicationContext context) {
    this.context = context;
  }

  /**
   * Registers static file serving and all application routes on the given Javalin config.
   *
   * @param appConfig the {@link JavalinConfig} to configure
   */
  public void bootstrap(final JavalinConfig appConfig) {
    final var profile = context.configService.getProfile();

    // Serve assets as static files
    appConfig.staticFiles.add(
        staticFiles -> {
          final boolean isDev = profile == Profile.DEV;
          staticFiles.hostedPath = "/";
          staticFiles.location = isDev ? Location.EXTERNAL : Location.CLASSPATH;
          staticFiles.directory = isDev ? "src/main/resources/assets" : "/assets";
          staticFiles.precompressMaxSize = !isDev ? 0 : -1;
        });

    appConfig.routes.apiBuilder(
        () -> {
          get(context.indexPageHandler);

          path(
              "api",
              () -> {
                get("login", ctx -> ctx.redirect("/app"));
                get("login/callback", context.oidcSecurityService.handleOidcLoginCallback("/app"));
                get("logout", context.oidcSecurityService.handleOidcLogout("/"));
              });

          get("app", ctx -> ctx.redirect("/app/boards"));
          path(
              "app",
              () -> {
                before(context.oidcSecurityService.handleOidcSecurity());

                path(
                    "admin",
                    () -> {
                      before(context.oidcSecurityService.handleAdminRoleRequired());
                      get(context.adminPageHandler);
                    });

                path(
                    "boards",
                    () -> {
                      get(context.boardsPageHandler);
                      post(context.boardsAddActionHandler);

                      path(
                          "{board-id-hashed}",
                          () -> {
                            before(context.boardsSecurityHandler);
                            patch(context.boardEditActionHandler);
                            delete(context.boardDeleteActionHandler);

                            path(
                                "todos",
                                () -> {
                                  get(context.todosPageHandler);
                                  post(context.todosAddActionHandler);

                                  path(
                                      "{todo-id-hashed}",
                                      () -> {
                                        get(context.todoItemHandler);
                                        patch(context.todoEditActionHandler);
                                        delete(context.todoDeleteActionHandler);
                                      });
                                });
                          });
                    });

                path(
                    "modals",
                    () -> {
                      get("boards/add", context.boardsAddModalHandler);
                      path(
                          "boards/{board-id-hashed}/edit",
                          () -> {
                            before(context.boardsSecurityHandler);
                            get(context.boardEditModalHandler);
                          });
                    });
              });
        });
  }
}
