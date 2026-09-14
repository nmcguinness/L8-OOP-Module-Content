package t05_collections_1.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t05 ex04 - de-duplication preserving order")
class ExerciseTest {

    @Test
    void uniqueOrderPreserving_duplicates_keepsFirstOccurrenceOrder() {
        ArrayList<String> in = new ArrayList<>(List.of("b", "a", "b", "c", "a"));
        assertEquals(List.of("b", "a", "c"), Exercise.uniqueOrderPreserving(in));
    }

    @Test
    void uniqueOrderPreserving_noDuplicates_returnsSameSequence() {
        ArrayList<String> in = new ArrayList<>(List.of("x", "y", "z"));
        assertEquals(List.of("x", "y", "z"), Exercise.uniqueOrderPreserving(in));
    }

    @Test
    void uniqueOrderPreserving_doesNotModifyTheInput() {
        ArrayList<String> in = new ArrayList<>(List.of("a", "a", "b"));
        Exercise.uniqueOrderPreserving(in);
        assertEquals(3, in.size(), "the caller's list must be left alone");
    }

    @Test
    void uniqueOrderPreserving_emptyList_returnsEmpty() {
        assertTrue(Exercise.uniqueOrderPreserving(new ArrayList<>()).isEmpty());
    }
}
