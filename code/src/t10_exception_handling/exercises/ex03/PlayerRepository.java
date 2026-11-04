package t10_exception_handling.exercises.ex03;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerRepository {

    private final Map<String, Integer> scores;

    public PlayerRepository(Map<String, Integer> scores) {
        if (scores == null) {
            throw new IllegalArgumentException("scores must not be null");
        }
        this.scores = new HashMap<>(scores);
    }

    // "Might not exist" is a normal outcome: say so in the return type
    public Optional<Integer> findScore(String name) {
        return Optional.ofNullable(scores.get(name));
    }

    // The caller claims the player exists; absence is exceptional
    public int requireScore(String name) throws PlayerNotFoundException {
        Integer score = scores.get(name);
        if (score == null) {
            throw new PlayerNotFoundException(name);
        }
        return score;
    }
}
