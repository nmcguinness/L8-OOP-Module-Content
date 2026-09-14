package t06_collections_2.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t06 ex07 - Josephus problem with a ListIterator")
class ExerciseTest {

    @Test
    void josephus_sevenPlayersEveryThird_lastStandingIsFour() {
        // The classic worked case: n=7, k=3 leaves player 4
        assertEquals(4, Exercise.josephus(7, 3));
    }

    @Test
    void josephus_singlePlayer_thatPlayerWins() {
        assertEquals(1, Exercise.josephus(1, 3));
    }

    @Test
    void josephus_countOfOne_lastPlayerWins() {
        // With k=1 everyone is eliminated in order, so the highest number survives
        assertEquals(5, Exercise.josephus(5, 1));
    }

    @Test
    void josephus_twoPlayersEverySecond_firstWins() {
        assertEquals(1, Exercise.josephus(2, 2));
    }
}
