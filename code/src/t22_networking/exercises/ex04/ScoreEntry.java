package t22_networking.exercises.ex04;

public final class ScoreEntry {

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
    // Getters named getX so that Jackson includes each one in the JSON
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
