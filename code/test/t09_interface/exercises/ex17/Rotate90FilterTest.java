package t09_interface.exercises.ex17;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import t09_interface.demos.de01.IImageFilter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 ex17 - Rotate90Filter")
class Rotate90FilterTest {

    @Test
    void rotate_turnsAQuarterClockwise() {
        // 2 rows x 3 columns
        int[][] input = {
            {1, 2, 3},
            {4, 5, 6}
        };
        int[][] result = new Rotate90Filter().filter(input);
        // 3 rows x 2 columns; the old left column, read upwards, becomes the top row
        assertArrayEquals(new int[] {4, 1}, result[0]);
        assertArrayEquals(new int[] {5, 2}, result[1]);
        assertArrayEquals(new int[] {6, 3}, result[2]);
    }

    @Test
    void rotate_swapsWidthAndHeight() {
        int[][] result = new Rotate90Filter().filter(new int[2][5]);
        assertEquals(5, result.length);
        assertEquals(2, result[0].length);
    }

    @Test
    void rotate_fourTimesGivesTheOriginal() {
        int[][] input = {{1, 2, 3}, {4, 5, 6}};
        IImageFilter rotate = new Rotate90Filter();
        int[][] result = input;
        for (int turn = 0; turn < 4; turn++) {
            result = rotate.filter(result);
        }
        assertTrue(Exercise.sameImage(input, result));
    }

    @Test
    void rotate_doesNotChangeItsInput() {
        int[][] input = {{1, 2}, {3, 4}};
        new Rotate90Filter().filter(input);
        assertArrayEquals(new int[] {1, 2}, input[0]);
        assertArrayEquals(new int[] {3, 4}, input[1]);
    }

    @Test
    void sameImage_isFalseForDifferentSizes() {
        assertFalse(Exercise.sameImage(new int[2][3], new int[3][2]));
    }
}
