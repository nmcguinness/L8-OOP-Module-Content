package t01_arrays.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t01 ex03 - min and max with guards")
class ExerciseTest {

    @Test
    void min_typicalArray_returnsSmallest() {
        assertEquals(1, Exercise.min(new int[]{5, 2, 8, 1}));
    }

    @Test
    void max_typicalArray_returnsLargest() {
        assertEquals(8, Exercise.max(new int[]{5, 2, 8, 1}));
    }

    @Test
    void min_allNegative_returnsSmallest() {
        // Catches the classic bug of seeding the running minimum with 0
        assertEquals(-9, Exercise.min(new int[]{-5, -2, -9}));
    }

    @Test
    void max_allNegative_returnsLargest() {
        // Catches the same bug in max: seeding with 0 would wrongly return 0
        assertEquals(-2, Exercise.max(new int[]{-5, -2, -9}));
    }

    @Test
    void min_singleElement_returnsThatElement() {
        assertEquals(42, Exercise.min(new int[]{42}));
    }

    @Test
    void min_emptyArray_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.min(new int[]{}));
    }

    @Test
    void min_null_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.min(null));
    }

    @Test
    void max_emptyArray_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.max(new int[]{}));
    }
}
