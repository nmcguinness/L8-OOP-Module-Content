package t01_arrays.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for Exercise 01 - fill, sum, average.
 *
 * <p>Run these to check your own implementation: they state the contract precisely,
 * including what should happen for null and empty input.
 */
@DisplayName("t01 ex01 - fill, sum, average")
class ExerciseTest {

    @Test
    void fillWith_lengthAndValue_fillsEverySlot() {
        assertArrayEquals(new int[]{5, 5, 5, 5}, Exercise.fillWith(4, 5));
    }

    @Test
    void fillWith_zeroLength_returnsEmptyArray() {
        assertEquals(0, Exercise.fillWith(0, 7).length);
    }

    @Test
    void fillWith_negativeLength_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.fillWith(-1, 5));
    }

    @Test
    void sum_typicalArray_addsEveryElement() {
        assertEquals(20, Exercise.sum(new int[]{5, 5, 5, 5}));
    }

    @Test
    void sum_null_returnsZero() {
        assertEquals(0, Exercise.sum(null));
    }

    @Test
    void sum_emptyArray_returnsZero() {
        assertEquals(0, Exercise.sum(new int[]{}));
    }

    @Test
    void average_typicalArray_dividesAsFloatingPoint() {
        // 3 + 7 = 10 over 2 elements. Integer division would wrongly give 5.0 here too,
        // so use a case where truncation would show: 1 + 2 = 3 over 2 -> 1.5, not 1.0
        assertEquals(1.5, Exercise.average(new int[]{1, 2}), 1e-9);
    }

    @Test
    void average_emptyArray_returnsZero() {
        assertEquals(0.0, Exercise.average(new int[]{}), 1e-9);
    }

    @Test
    void average_null_returnsZero() {
        assertEquals(0.0, Exercise.average(null), 1e-9);
    }
}
