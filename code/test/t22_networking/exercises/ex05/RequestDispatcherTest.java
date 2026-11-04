package t22_networking.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t22 ex05 - DISCONNECT and stricter validation, tested without sockets")
class RequestDispatcherTest {

    private final RequestDispatcher dispatcher = new RequestDispatcher(new ScoreRepository());

    private ServerResponse<?> submit(int score, String mode) {
        return dispatcher.dispatch(new ClientRequest(RequestType.SUBMIT_SCORE,
            Map.of("playerId", "P1", "playerName", "Alice", "score", score, "gameMode", mode)));
    }

    @Test
    void disconnect_returnsGoodbyeWithNoData() {
        ServerResponse<?> r = dispatcher.dispatch(new ClientRequest(RequestType.DISCONNECT, Map.of()));

        assertTrue(r.isOk());
        assertEquals("goodbye", r.getMessage());
        assertNull(r.getData());
    }

    @Test
    void submitScore_aboveMaximum_isRejected() {
        ServerResponse<?> r = submit(RequestDispatcher.MAX_SCORE + 1, "ranked");

        assertFalse(r.isOk());
        assertTrue(r.getMessage().contains("exceeds maximum"));
    }

    @Test
    void submitScore_atMaximum_isAccepted() {
        assertTrue(submit(RequestDispatcher.MAX_SCORE, "ranked").isOk());
    }

    @Test
    void submitScore_unknownGameMode_namesTheBadValue() {
        ServerResponse<?> r = submit(100, "TOURNAMENT");

        assertEquals("invalid request: invalid game mode: TOURNAMENT", r.getMessage());
    }

    @Test
    void submitScore_gameModeIsCaseInsensitive_andStoredUppercase() {
        ServerResponse<?> r = submit(100, "casual");

        assertEquals("CASUAL", ((ScoreEntry) r.getData()).getGameMode());
    }
}
