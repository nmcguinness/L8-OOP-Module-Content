package t07_collections_3_set_map.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t07 ex03 - leaderboard with a PriorityQueue")
class ExerciseTest {

    @Test
    void leaderboard_distinctScores_highestFirst() {
        Map<String, Integer> scores = Map.of("Ana", 50, "Ben", 90, "Cara", 70);

        assertEquals(List.of("Ben", "Cara", "Ana"), Exercise.leaderboard(scores));
    }

    @Test
    void leaderboard_tiedScores_orderedByNameAscending() {
        Map<String, Integer> scores = Map.of("Zoe", 80, "Ana", 80, "Ben", 95);

        assertEquals(List.of("Ben", "Ana", "Zoe"), Exercise.leaderboard(scores));
    }

    @Test
    void leaderboard_extremeValues_doNotOverflow() {
        // A subtraction comparator (b - a) overflows here and ranks these the wrong way round
        Map<String, Integer> scores = Map.of("Low", Integer.MIN_VALUE, "High", Integer.MAX_VALUE);

        assertEquals(List.of("High", "Low"), Exercise.leaderboard(scores));
    }

    @Test
    void leaderboard_empty_returnsEmptyList() {
        assertTrue(Exercise.leaderboard(Map.of()).isEmpty());
    }

    @Test
    void leaderboard_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.leaderboard(null));
    }
}
