package de.lpo.todo.context;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import de.lpo.todo.api.ApiBootstrapService;
import de.lpo.todo.api.security.IdentityService;
import de.lpo.todo.api.security.OidcSecurityService;
import de.lpo.todo.api.web.TemplateService;
import de.lpo.todo.api.web.handlers.AdminPageHandler;
import de.lpo.todo.api.web.handlers.BoardDeleteActionHandler;
import de.lpo.todo.api.web.handlers.BoardEditActionHandler;
import de.lpo.todo.api.web.handlers.BoardEditModalHandler;
import de.lpo.todo.api.web.handlers.BoardsAddActionHandler;
import de.lpo.todo.api.web.handlers.BoardsAddModalHandler;
import de.lpo.todo.api.web.handlers.BoardsPageHandler;
import de.lpo.todo.api.web.handlers.BoardsSecurityHandler;
import de.lpo.todo.api.web.handlers.IndexPageHandler;
import de.lpo.todo.api.web.handlers.TodoDeleteActionHandler;
import de.lpo.todo.api.web.handlers.TodoEditActionHandler;
import de.lpo.todo.api.web.handlers.TodoItemHandler;
import de.lpo.todo.api.web.handlers.TodosAddActionHandler;
import de.lpo.todo.api.web.handlers.TodosPageHandler;
import de.lpo.todo.config.ConfigService;
import de.lpo.todo.database.DataSourceService;
import de.lpo.todo.database.JooqService;
import de.lpo.todo.database.MigrationService;
import de.lpo.todo.repository.BoardRepo;
import de.lpo.todo.repository.IdentityRepo;
import de.lpo.todo.repository.TodoRepo;
import de.lpo.todo.repository.UserRepo;
import de.lpo.todo.repository.mapper.BoardMapper;
import de.lpo.todo.repository.mapper.IdentityMapper;
import de.lpo.todo.repository.mapper.TodoMapper;
import de.lpo.todo.repository.mapper.UserMapper;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.TodoService;
import de.lpo.todo.service.UserService;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The dependency management for this application.
 *
 * <ul>
 *   <li>All dependencies are managed as singletons
 *   <li>All DI is done in this class
 *   <li>All DI is done during compile time
 *   <li>All dependencies are easily accessible when you have the application context
 * </ul>
 */
public final class ApplicationContext implements AutoCloseable {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ClassToInstanceMap<Object> dependencies = MutableClassToInstanceMap.create();

  // Config
  public final ConfigService configService = new ConfigService();

  // Database
  public final DataSourceService dataSourceService = new DataSourceService(configService);
  public final MigrationService migrationService = new MigrationService(dataSourceService);
  public final JooqService jooqService = new JooqService(configService, dataSourceService);

  // Repository + Mappers
  public final IdentityMapper identityMapper = new IdentityMapper();
  public final TodoMapper todoMapper = new TodoMapper();
  public final UserMapper userMapper = new UserMapper();
  public final BoardMapper boardMapper = new BoardMapper(todoMapper, userMapper);
  public final IdentityRepo identityRepo = new IdentityRepo(jooqService, identityMapper);
  public final TodoRepo todoRepo = new TodoRepo(jooqService, todoMapper);
  public final BoardRepo boardRepo = new BoardRepo(jooqService, boardMapper);
  public final UserRepo userRepo = new UserRepo(jooqService, userMapper);

  // Security
  public final IdentityService identityService = new IdentityService(identityRepo);
  public final OidcSecurityService oidcSecurityService =
      new OidcSecurityService(configService, identityService);

  // Services
  public final TodoService todoService = new TodoService(todoRepo);
  public final BoardService boardService = new BoardService(boardRepo);
  public final UserService userService = new UserService(identityService, userRepo, userMapper);

  // Web + Handlers
  public final TemplateService templateService = new TemplateService(configService);
  public final IndexPageHandler indexPageHandler = new IndexPageHandler(templateService);
  public final AdminPageHandler adminPageHandler =
      new AdminPageHandler(templateService, identityService);
  public final BoardsPageHandler boardsPageHandler =
      new BoardsPageHandler(templateService, userService, boardService);
  public final BoardsSecurityHandler boardsSecurityHandler = new BoardsSecurityHandler(userService);
  public final BoardsAddModalHandler boardsAddModalHandler =
      new BoardsAddModalHandler(templateService, userService);
  public final BoardsAddActionHandler boardsAddActionHandler =
      new BoardsAddActionHandler(templateService, userService, boardService);
  public final BoardEditModalHandler boardEditModalHandler =
      new BoardEditModalHandler(templateService, userService, boardService);
  public final BoardEditActionHandler boardEditActionHandler =
      new BoardEditActionHandler(templateService, userService, boardService);
  public final BoardDeleteActionHandler boardDeleteActionHandler =
      new BoardDeleteActionHandler(boardService);
  public final TodosPageHandler todosPageHandler =
      new TodosPageHandler(templateService, boardService);
  public final TodosAddActionHandler todosAddActionHandler =
      new TodosAddActionHandler(templateService, todoService);
  public final TodoItemHandler todoItemHandler = new TodoItemHandler(templateService, todoService);
  public final TodoEditActionHandler todoEditActionHandler =
      new TodoEditActionHandler(templateService, todoService);
  public final TodoDeleteActionHandler todoDeleteActionHandler =
      new TodoDeleteActionHandler(todoService);

  // API
  public final ApiBootstrapService apiBootstrapService = new ApiBootstrapService(this);

  /** Constructs all dependencies in order and indexes them by type for lookup. */
  public ApplicationContext() {
    try {
      for (final Field field : ApplicationContext.class.getDeclaredFields()) {
        if (Modifier.isStatic(field.getModifiers())) {
          continue;
        }
        final Object dependency = field.get(this);
        dependencies.put(dependency.getClass(), dependency);
      }
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns whether a dependency of the given type is managed by this context.
   *
   * @param type the dependency's type
   * @return {@code true} if a dependency of the given type is managed by this context
   */
  public boolean has(final Class<?> type) {
    return dependencies.containsKey(type);
  }

  /**
   * Returns the managed dependency of the given type, if any.
   *
   * @param type the dependency's type
   * @return the managed dependency, or {@code null} if none is found
   */
  public <T> @Nullable T get(final Class<T> type) {
    return dependencies.getInstance(type);
  }

  @Override
  public void close() {
    for (final Object dependency : dependencies.values()) {
      if (dependency instanceof AutoCloseable closeable) {
        try {
          closeable.close();
        } catch (final Exception e) {
          LOG.warn("Could not close {}", dependency.getClass().getSimpleName(), e);
        }
      }
    }
  }
}
