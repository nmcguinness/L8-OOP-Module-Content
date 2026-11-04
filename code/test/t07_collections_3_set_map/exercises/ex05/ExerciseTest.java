package t07_collections_3_set_map.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t07 ex05 - set intersection")
class ExerciseTest {

    @Test
    void intersection_withDuplicates_returnsEachCommonValueOnce() {
        assertEquals(Set.of(2, 4), Exercise.intersection(List.of(1, 2, 2, 3, 4), List.of(2, 4, 4)));
    }

    @Test
    void intersection_noOverlap_returnsEmptySet() {
        assertTrue(Exercise.intersection(List.of(1, 2), List.of(3, 4)).isEmpty());
    }

    @Test
    void intersection_inputs_areNotModified() {
        List<Integer> a = new ArrayList<>(List.of(1, 2, 3));
        List<Integer> b = new ArrayList<>(List.of(3));

        Exercise.intersection(a, b);

        assertEquals(List.of(1, 2, 3), a);
        assertEquals(List.of(3), b);
    }

    @Test
    void intersection_nullList_throws() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.intersection(null, List.of()));
    }
}
