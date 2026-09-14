package t01_arrays.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t01 ex02 - indexOf and count")
class ExerciseTest {

    @Test
    void indexOf_valuePresentTwice_returnsFirstIndex() {
        assertEquals(1, Exercise.indexOf(new int[]{3, 7, 7, 2}, 7));
    }

    @Test
    void indexOf_valueAbsent_returnsMinusOne() {
        assertEquals(-1, Exercise.indexOf(new int[]{3, 7, 7, 2}, 9));
    }

    @Test
    void indexOf_emptyArray_returnsMinusOne() {
        assertEquals(-1, Exercise.indexOf(new int[]{}, 9));
    }

    @Test
    void indexOf_null_returnsMinusOne() {
        assertEquals(-1, Exercise.indexOf(null, 9));
    }

    @Test
    void count_valuePresentTwice_returnsTwo() {
        assertEquals(2, Exercise.count(new int[]{3, 7, 7, 2}, 7));
    }

    @Test
    void count_valueAbsent_returnsZero() {
        assertEquals(0, Exercise.count(new int[]{3, 7, 7, 2}, 9));
    }

    @Test
    void count_null_returnsZero() {
        assertEquals(0, Exercise.count(null, 7));
    }
}
