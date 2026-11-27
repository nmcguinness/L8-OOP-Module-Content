package t09_interface.exercises.ex12;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import t09_interface.demos.de01.IImageFilter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t09 ex12 - FlipHorizontalFilter")
class FlipHorizontalFilterTest {

    @Test
    void flip_reversesEachRow() {
        int[][] input = {{1, 2, 3}, {4, 5, 6}};
        int[][] result = new FlipHorizontalFilter().filter(input);
        assertArrayEquals(new int[] {3, 2, 1}, result[0]);
        assertArrayEquals(new int[] {6, 5, 4}, result[1]);
    }

    @Test
    void flip_twiceGivesTheOriginal() {
        int[][] input = {{1, 2, 3, 4}, {5, 6, 7, 8}};
        IImageFilter flip = new FlipHorizontalFilter();
        int[][] result = flip.filter(flip.filter(input));
        assertArrayEquals(input[0], result[0]);
        assertArrayEquals(input[1], result[1]);
    }

    @Test
    void flip_oneColumnImageIsUnchanged() {
        int[][] input = {{7}, {8}};
        int[][] result = new FlipHorizontalFilter().filter(input);
        assertEquals(7, result[0][0]);
        assertEquals(8, result[1][0]);
    }

    @Test
    void flip_doesNotChangeItsInput() {
        int[][] input = {{1, 2, 3}};
        new FlipHorizontalFilter().filter(input);
        assertArrayEquals(new int[] {1, 2, 3}, input[0]);
    }
}
