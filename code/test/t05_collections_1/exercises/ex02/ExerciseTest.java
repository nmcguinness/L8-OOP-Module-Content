package t05_collections_1.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t05 ex02 - array to list and back")
class ExerciseTest {

    @Test
    void toList_array_preservesOrderAndValues() {
        assertEquals(List.of(1, 2, 3), Exercise.toList(new int[]{1, 2, 3}));
    }

    @Test
    void toList_emptyArray_returnsEmptyList() {
        assertTrue(Exercise.toList(new int[]{}).isEmpty());
    }

    @Test
    void toArray_list_returnsSameValuesInOrder() {
        ArrayList<Integer> list = new ArrayList<>(List.of(4, 5, 6));
        assertArrayEquals(new int[]{4, 5, 6}, Exercise.toArray(list));
    }

    @Test
    void roundTrip_arrayToListToArray_isUnchanged() {
        int[] original = {7, 8, 9};
        assertArrayEquals(original, Exercise.toArray(Exercise.toList(original)));
    }
}
