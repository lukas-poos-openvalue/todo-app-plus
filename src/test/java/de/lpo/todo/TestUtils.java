package de.lpo.todo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.lpo.todo.api.security.IdentityService;
import de.lpo.todo.generated.jooq.Tables;
import de.lpo.todo.generated.jte.Templates;
import de.lpo.todo.model.Board;
import de.lpo.todo.model.Identity;
import de.lpo.todo.model.Role;
import de.lpo.todo.model.Todo;
import de.lpo.todo.model.User;
import de.lpo.todo.model.ids.HasId;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.model.refs.Refs;
import de.lpo.todo.repository.mapper.UserMapper;
import de.lpo.todo.service.BoardService;
import de.lpo.todo.service.TodoService;
import gg.jte.models.runtime.JteModel;
import io.javalin.http.Context;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.jooq.Table;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public final class TestUtils {
  public static final class IT {
    public static final class Database {
      public static final List<Table<?>> TABLES =
          List.of(Tables.TBL_TODO, Tables.TBL_BOARD_MEMBER, Tables.TBL_BOARD, Tables.TBL_IDENTITY);
      public static final List<String> TABLE_NAMES =
          TABLES.stream().map(tbl -> tbl.getName().toLowerCase()).toList();

      public static void clearDomainTables() {
        try (final var conn = connectToContainer();
            final var stmt = conn.createStatement()) {
          for (final var tableName : TABLE_NAMES) {
            stmt.execute("delete from %s where 1=1".formatted(tableName));
          }
        } catch (final SQLException e) {
          throw new RuntimeException(e);
        }
      }

      public static List<String> getAllTableNames() {
        try (final var conn = connectToContainer()) {
          final List<String> tables = new ArrayList<>();
          try (final ResultSet rs =
              conn.getMetaData().getTables(null, "public", "%", new String[] {"TABLE"})) {
            while (rs.next()) {
              tables.add(rs.getString("TABLE_NAME").toLowerCase());
            }
          }
          return tables;
        } catch (final SQLException e) {
          throw new RuntimeException(e);
        }
      }

      private static Connection connectToContainer() throws SQLException {
        return IntegrationTest.POSTGRES.createConnection("");
      }
    }

    public static final class Domain {
      public static List<User> storeThreeUsers(
          final IdentityService identityService, final UserMapper userMapper) {
        final var identities =
            List.of(
                new Identity("userIdA", "userNameA", "emailA", "displayNameA", Role.USER),
                new Identity("userIdB", "userNameB", "emailB", "displayNameB", Role.USER),
                new Identity("userIdC", "userNameC", "emailC", "displayNameC", Role.USER));
        identities.forEach(identityService::save);
        return identities.stream().map(userMapper::toDomain).toList();
      }

      public static Board storeOneBoardTwoUsers(
          final IdentityService identityService,
          final UserMapper userMapper,
          BoardService boardService) {
        final var identities =
            List.of(
                new Identity("userIdA", "userNameA", "emailA", "displayNameA", Role.USER),
                new Identity("userIdB", "userNameB", "emailB", "displayNameB", Role.USER));
        identities.forEach(identityService::save);
        final var userA = userMapper.toDomain(identities.get(0));
        final var userB = userMapper.toDomain(identities.get(1));

        return boardService.addFetchAll("title", userA.id(), List.of(userB.id()));
      }

      public static Todo storeOneTodoOneBoardTwoUsers(
          final IdentityService identityService,
          final UserMapper userMapper,
          final BoardService boardService,
          final TodoService todoService) {
        final var board = storeOneBoardTwoUsers(identityService, userMapper, boardService);
        final var todo = todoService.addFetchNone(board.id(), "content");
        return new Todo(todo.id(), todo.content(), todo.completed(), Ref.loaded(board));
      }

      public static <ID, T extends HasId<ID>> T extractRef(final Ref<ID, T> ref) {
        return switch (ref) {
          case Ref.Loaded(var value) -> value;
          default -> Assertions.fail("Ref should have been loaded!");
        };
      }

      public static <T extends HasId<?>> List<T> extractRefs(final Refs<T> refs) {
        return switch (refs) {
          case Refs.Loaded(var values) -> values;
          default -> Assertions.fail("Refs should have been loaded!");
        };
      }
    }
  }

  public static final class Web {
    public static Templates mockTemplates() {
      final Answer<JteModel> defaultAnswer =
          i -> {
            if (JteModel.class.equals(i.getMethod().getReturnType())) {
              return mock(JteModel.class);
            }
            return null;
          };
      return Mockito.mock(defaultAnswer);
    }

    public static void mockPathParamBoardId(final Context ctx, final Id<Integer> boardIdValue) {
      when(ctx.pathParam("{board-id-hashed}")).thenReturn(boardIdValue.hash());
    }

    public static void mockPathParamTodoId(final Context ctx, final Id<Integer> todoIdValue) {
      when(ctx.pathParam("{todo-id-hashed}")).thenReturn(todoIdValue.hash());
    }
  }
}
