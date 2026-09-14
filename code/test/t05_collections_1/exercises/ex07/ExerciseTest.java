package t05_collections_1.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t05 ex07 - partition into evens and odds")
class ExerciseTest {

    private ArrayList<Integer> list(int... xs) {
        ArrayList<Integer> out = new ArrayList<>();
        for (int x : xs) out.add(x);
        return out;
    }

    @Test
    void evens_mixedInput_returnsOnlyEvenNumbersInOrder() {
        assertEquals(List.of(2, 4, 6), Exercise.evens(list(1, 2, 3, 4, 5, 6)));
    }

    @Test
    void odds_mixedInput_returnsOnlyOddNumbersInOrder() {
        assertEquals(List.of(1, 3, 5), Exercise.odds(list(1, 2, 3, 4, 5, 6)));
    }

    @Test
    void evensAndOdds_together_accountForEveryElement() {
        ArrayList<Integer> in = list(1, 2, 3, 4, 5, 6);
        assertEquals(in.size(), Exercise.evens(in).size() + Exercise.odds(in).size());
    }

    @Test
    void evens_noEvenNumbers_returnsEmptyList() {
        assertTrue(Exercise.evens(list(1, 3, 5)).isEmpty());
    }

    @Test
    void evens_emptyInput_returnsEmptyList() {
        assertTrue(Exercise.evens(list()).isEmpty());
    }
}
