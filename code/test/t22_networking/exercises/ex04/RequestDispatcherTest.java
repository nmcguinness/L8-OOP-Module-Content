package t22_networking.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The dispatcher holds all of the request logic and has no socket code, so it is
 * tested with plain method calls: no server, no ports, no threads.
 */
@DisplayName("t22 ex04 - RequestDispatcher, tested without sockets")
class RequestDispatcherTest {

    private final RequestDispatcher dispatcher = new RequestDispatcher(new ScoreRepository());

    private ServerResponse<?> submit(String id, String name, int score) {
        return dispatcher.dispatch(new ClientRequest(RequestType.SUBMIT_SCORE,
            Map.of("playerId", id, "playerName", name, "score", score, "gameMode", "ranked")));
    }

    @Test
    void submitScore_validPayload_returnsEntryWithGeneratedId() {
        ServerResponse<?> r = submit("P001", "Alice", 4200);

        assertTrue(r.isOk());
        assertEquals(1, ((ScoreEntry) r.getData()).getId());
    }

    @Test
    void submitScore_missingPlayerId_returnsInvalidRequest() {
        ServerResponse<?> r = dispatcher.dispatch(new ClientRequest(RequestType.SUBMIT_SCORE,
            Map.of("playerName", "Bob", "score", 10, "gameMode", "ranked")));

        assertFalse(r.isOk());
        assertEquals("invalid request: playerId is required", r.getMessage());
    }

    @Test
    void getAll_returnsEntriesHighestScoreFirst() {
        submit("P001", "Alice", 100);
        submit("P002", "Bob", 300);

        List<?> all = (List<?>) dispatcher.dispatch(
            new ClientRequest(RequestType.GET_ALL, Map.of())).getData();

        assertEquals("Bob", ((ScoreEntry) all.get(0)).getPlayerName());
    }

    @Test
    void getTopN_limitsResults_andRejectsZero() {
        submit("P001", "Alice", 100);
        submit("P002", "Bob", 300);
        submit("P003", "Carol", 200);

        ServerResponse<?> top = dispatcher.dispatch(
            new ClientRequest(RequestType.GET_TOP_N, Map.of("n", 2)));
        ServerResponse<?> zero = dispatcher.dispatch(
            new ClientRequest(RequestType.GET_TOP_N, Map.of("n", 0)));

        assertEquals(2, ((List<?>) top.getData()).size());
        assertFalse(zero.isOk());
    }

    @Test
    void dispatch_missingRequestType_returnsError() {
        assertFalse(dispatcher.dispatch(new ClientRequest(null, Map.of())).isOk());
    }

    @Test
    void handleLine_invalidJsonOrUnknownType_isMalformedRequest() {
        ClientHandler handler = new ClientHandler(new java.net.Socket(), dispatcher);

        assertTrue(handler.handleLine("not json").getMessage().startsWith("malformed request"));
        assertTrue(handler.handleLine("{\"requestType\":\"NOPE\",\"payload\":{}}")
            .getMessage().startsWith("malformed request"));
    }
}
