package t06_collections_2.exercises.ex09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t06 ex09 - sliding window maximum")
class ExerciseTest {

    @Test
    void maxWindow_windowOfThree_returnsMaxOfEachWindow() {
        // windows: [1,3,-1] [3,-1,-3] [-1,-3,5] [-3,5,3] [5,3,6] [3,6,7]
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7},
                          Exercise.maxWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
    }

    @Test
    void maxWindow_windowOfOne_returnsTheArrayItself() {
        assertArrayEquals(new int[]{4, 2, 9}, Exercise.maxWindow(new int[]{4, 2, 9}, 1));
    }

    @Test
    void maxWindow_windowEqualToLength_returnsSingleMaximum() {
        assertArrayEquals(new int[]{9}, Exercise.maxWindow(new int[]{4, 2, 9}, 3));
    }

    @Test
    void maxWindow_windowLargerThanArray_returnsEmpty() {
        assertEquals(0, Exercise.maxWindow(new int[]{1, 2}, 5).length);
    }

    @Test
    void maxWindow_nullOrNonPositiveWindow_returnsEmpty() {
        assertEquals(0, Exercise.maxWindow(null, 3).length);
        assertEquals(0, Exercise.maxWindow(new int[]{1, 2, 3}, 0).length);
    }
}
