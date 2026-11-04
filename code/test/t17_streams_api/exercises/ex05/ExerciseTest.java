package t17_streams_api.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t17 ex05 - sum, average and max")
class ExerciseTest {

    private final List<Integer> scores = List.of(70, 85, 90, 55);

    @Test
    void sum_example() {
        assertEquals(300, Exercise.sum(scores));
    }

    @Test
    void average_example() {
        assertEquals(75.0, Exercise.average(scores).getAsDouble(), 1e-9);
    }

    @Test
    void max_example() {
        assertEquals(Optional.of(90), Exercise.max(scores));
    }

    @Test
    void emptyList_sumIsZero_averageAndMaxAreEmpty() {
        assertEquals(0, Exercise.sum(List.of()));
        assertEquals(OptionalDouble.empty(), Exercise.average(List.of()));
        assertEquals(Optional.empty(), Exercise.max(List.of()));
    }
}
