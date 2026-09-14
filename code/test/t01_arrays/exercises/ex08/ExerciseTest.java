package t01_arrays.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("t01 ex08 - heatmap normalisation")
class ExerciseTest {

    @Test
    void normalize_typicalGrid_scalesByTheGridMaximum() {
        double[][] out = Exercise.normalize(new int[][]{{0, 64, 128}, {255, 128, 0}});
        assertEquals(0.0, out[0][0], 1e-9);
        assertEquals(64 / 255.0, out[0][1], 1e-9);
        assertEquals(1.0, out[1][0], 1e-9, "the maximum value maps to 1.0");
    }

    @Test
    void normalize_returnsANewArray_leavingTheInputUnchanged() {
        int[][] input = {{0, 128}, {255, 64}};
        Exercise.normalize(input);
        assertEquals(255, input[1][0], "the original grid must not be modified");
    }

    @Test
    void normalize_allZeroGrid_returnsAllZerosWithoutDividingByZero() {
        double[][] out = Exercise.normalize(new int[][]{{0, 0}, {0, 0}});
        assertEquals(0.0, out[0][0], 1e-9);
        assertEquals(0.0, out[1][1], 1e-9);
    }

    @Test
    void normalize_jaggedGrid_preservesEachRowLength() {
        double[][] out = Exercise.normalize(new int[][]{{10}, {20, 30, 40}});
        assertEquals(1, out[0].length);
        assertEquals(3, out[1].length);
    }

    @Test
    void normalize_nullRow_staysNullInTheResult() {
        double[][] out = Exercise.normalize(new int[][]{{10, 20}, null});
        assertNull(out[1], "a null row should not become an empty row");
    }

    @Test
    void normalize_nullGrid_returnsNull() {
        assertNull(Exercise.normalize(null));
    }
}
