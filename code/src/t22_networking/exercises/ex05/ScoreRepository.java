package t22_networking.exercises.ex05;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * In-memory scores shared by every client thread, so every method that
 * touches the list is synchronized.
 */
public class ScoreRepository {

    private final List<ScoreEntry> entries = new ArrayList<>();
    private int nextId = 1;

    // Inserts: a new score entry and returns it with the generated ID
    public synchronized ScoreEntry insert(String playerId, String playerName, int score,
                                          String gameMode) {
        ScoreEntry entry = new ScoreEntry(nextId, playerId, playerName, score, gameMode);
        entries.add(entry);
        nextId++;
        return entry;
    }

    // Gets: all entries sorted by score descending
    public synchronized List<ScoreEntry> getAll() {
        return entries.stream()
            .sorted(Comparator.comparingInt(ScoreEntry::getScore).reversed())
            .toList();
    }

    // Gets: the top N entries by score descending
    public synchronized List<ScoreEntry> getTop(int n) {
        return getAll().stream().limit(Math.max(0, n)).toList();
    }
}
