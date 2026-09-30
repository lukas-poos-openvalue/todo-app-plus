package de.lpo.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.lpo.todo.model.Board;
import de.lpo.todo.model.ids.Id;
import de.lpo.todo.model.ids.IntId;
import de.lpo.todo.model.ids.StringId;
import de.lpo.todo.model.refs.Ref;
import de.lpo.todo.model.refs.Refs;
import de.lpo.todo.repository.BoardRepo;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

class BoardServiceTest {

  private final BoardRepo boardRepo = mock();
  private final BoardService boardService = new BoardService(boardRepo);

  private static Board oneBoard(Id<Integer> boardId) {
    return new Board(
        boardId, "title", Refs.unloaded(), Ref.unloaded(StringId.ofValue("a")), Refs.unloaded());
  }

  @Test
  void should_add() {
    // GIVEN
    // - Board data
    final var title = RandomStringUtils.secure().next(10);
    final var ownerId = StringId.ofValue(RandomStringUtils.secure().next(2));
    final var memberIdHashes =
        List.of(
            StringId.ofValue(RandomStringUtils.secure().next(2)),
            StringId.ofValue(RandomStringUtils.secure().next(2)),
            StringId.ofValue(RandomStringUtils.secure().next(2)));

    // - Mock repo
    final var boardId = IntId.ofValue(RandomUtils.secure().randomInt());
    when(boardRepo.add(any(), any(), any())).thenReturn(boardId);
    final var board = oneBoard(boardId);
    when(boardRepo.findOneByIdFetchAll(boardId)).thenReturn(Optional.of(board));

    // WHEN
    final var result = boardService.addFetchAll(title, ownerId, memberIdHashes);

    // THEN
    assertThat(result).isEqualTo(board);
    verify(boardRepo).add(title, ownerId, memberIdHashes);
    verify(boardRepo).findOneByIdFetchAll(boardId);
  }

  @Test
  void should_update() {
    // GIVEN
    final var boardId = IntId.ofValue(RandomUtils.secure().randomInt());
    final var board = oneBoard(boardId);
    final var title = RandomStringUtils.secure().next(10);
    final var memberIdHashes =
        List.of(
            StringId.ofValue(RandomStringUtils.secure().next(2)),
            StringId.ofValue(RandomStringUtils.secure().next(2)),
            StringId.ofValue(RandomStringUtils.secure().next(2)));

    // - Mock repo
    when(boardRepo.findOneByIdFetchAll(boardId)).thenReturn(Optional.of(board));

    // WHEN
    final var result = boardService.updateFetchAll(boardId, title, memberIdHashes);

    // THEN
    assertThat(result).isEqualTo(board);
    verify(boardRepo).update(board.id(), title, memberIdHashes);
    verify(boardRepo).findOneByIdFetchAll(board.id());
  }

  @Test
  void should_delete() {
    // GIVEN
    // - Existing IDs
    final var boardId = IntId.ofValue(RandomUtils.secure().randomInt());

    // WHEN
    boardService.deleteById(boardId);

    // THEN
    verify(boardRepo).deleteById(boardId);
  }
}
