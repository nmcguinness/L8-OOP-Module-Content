package t07_collections_3_set_map.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t07 ex02 - word frequency with a HashMap")
class ExerciseTest {

    @Test
    void wordFrequency_mixedCase_countsCaseInsensitively() {
        Map<String, Integer> freq = Exercise.wordFrequency("The cat saw the other cat");

        assertEquals(Map.of("the", 2, "cat", 2, "saw", 1, "other", 1), freq);
    }

    @Test
    void wordFrequency_extraWhitespace_isIgnored() {
        assertEquals(Map.of("a", 2, "b", 1), Exercise.wordFrequency("  a   b\ta  "));
    }

    @Test
    void wordFrequency_blankOrNull_returnsEmptyMap() {
        assertTrue(Exercise.wordFrequency("   ").isEmpty());
        assertTrue(Exercise.wordFrequency(null).isEmpty());
    }
}
