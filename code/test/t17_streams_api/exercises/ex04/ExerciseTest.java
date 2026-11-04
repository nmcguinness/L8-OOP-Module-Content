package t17_streams_api.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t17 ex04 - groupingBy")
class ExerciseTest {

    @Test
    void groupByLength_example() {
        Map<Integer, List<String>> groups =
                Exercise.groupByLength(List.of("cat", "horse", "dog", "mouse", "ox"));

        assertEquals(List.of("ox"), groups.get(2));
        assertEquals(List.of("cat", "dog"), groups.get(3));
        assertEquals(List.of("horse", "mouse"), groups.get(5));
    }

    @Test
    void groupByLength_keysInIncreasingOrder() {
        Map<Integer, List<String>> groups = Exercise.groupByLength(List.of("ccc", "a", "bb"));

        assertEquals(List.of(1, 2, 3), new ArrayList<>(groups.keySet()));
    }

    @Test
    void groupByLength_emptyOrNull_returnsEmptyMap() {
        assertTrue(Exercise.groupByLength(List.of()).isEmpty());
        assertTrue(Exercise.groupByLength(null).isEmpty());
    }
}
