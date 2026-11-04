package t22_networking.exercises.ex03;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class Exercise {

    public static void run() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ScoreEntry e1 = new ScoreEntry(1, "P001", "Alice", 4_200, "ranked");
        ScoreEntry e2 = new ScoreEntry(2, "P002", "Bob", 3_875, "ranked");

        ServerResponse<List<ScoreEntry>> ok = ServerResponse.ok("scores loaded", List.of(e1, e2));
        ServerResponse<Void> error = ServerResponse.error("player not found");

        System.out.println(mapper.writeValueAsString(ok));
        System.out.println(mapper.writeValueAsString(error));
    }
}

class ScoreEntry {

    // === Fields ===
    private final int id;
    private final String playerId;
    private final String playerName;
    private final int score;
    private final String gameMode;

    // === Constructors ===
    // Creates: a validated score entry
    public ScoreEntry(int id, String playerId, String playerName, int score, String gameMode) {
        if (id < 0)
            throw new IllegalArgumentException("id must be >= 0");
        if (playerId == null || playerId.isBlank())
            throw new IllegalArgumentException("playerId is required");
        if (playerName == null || playerName.isBlank())
            throw new IllegalArgumentException("playerName is required");
        if (score < 0)
            throw new IllegalArgumentException("score must be >= 0");
        if (gameMode == null || gameMode.isBlank())
            throw new IllegalArgumentException("gameMode is required");

        this.id = id;
        this.playerId = playerId.trim();
        this.playerName = playerName.trim();
        this.score = score;
        this.gameMode = gameMode.trim().toUpperCase();
    }

    // === Public API ===
    public int getId() { return id; }
    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public String getGameMode() { return gameMode; }

    // === Overrides ===
    @Override
    public String toString() {
        return "ScoreEntry{id=" + id
            + ", player=" + playerName
            + ", score=" + score
            + ", mode=" + gameMode + "}";
    }
}

class ServerResponse<T> {

    // === Fields ===
    // "OK" or "ERROR"
    private final String status;
    private final String message;
    private final T data;

    // === Constructors ===
    // Creates: a response with all fields set
    public ServerResponse(String status, String message, T data) {
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // === Public API ===
    // Creates: a successful response with data
    public static <T> ServerResponse<T> ok(String message, T data) {
        return new ServerResponse<>("OK", message, data);
    }

    // Creates: an error response with no data
    public static <T> ServerResponse<T> error(String message) {
        return new ServerResponse<>("ERROR", message, null);
    }

    // Gets: the status string
    public String getStatus() { return status; }

    // Gets: the human-readable message
    public String getMessage() { return message; }

    // Gets: the data payload
    public T getData() { return data; }

    // Checks: whether the response represents success.
    // @JsonIgnore keeps it out of the JSON: it is derived from status
    @JsonIgnore
    public boolean isOk() { return "OK".equals(status); }
}
