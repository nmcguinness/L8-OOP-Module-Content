package t09_interface.exercises.ex16;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t09 ex16 - PixelateFilter")
class PixelateFilterTest {

    @Test
    void blockOfTwo_copiesTheTopLeftPixel() {
        int[][] input = {
            {1, 2, 3, 4},
            {5, 6, 7, 8}
        };
        int[][] result = new PixelateFilter(2).filter(input);
        assertArrayEquals(new int[] {1, 1, 3, 3}, result[0]);
        assertArrayEquals(new int[] {1, 1, 3, 3}, result[1]);
    }

    @Test
    void partialBlocksAtTheEdges_areFilledToo() {
        // 3 x 3 with blocks of 2: the right column and bottom row are partial blocks
        int[][] input = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        int[][] result = new PixelateFilter(2).filter(input);
        assertArrayEquals(new int[] {1, 1, 3}, result[0]);
        assertArrayEquals(new int[] {1, 1, 3}, result[1]);
        assertArrayEquals(new int[] {7, 7, 9}, result[2]);
    }

    @Test
    void blockOfOne_leavesTheImageUnchanged() {
        int[][] input = {{1, 2}, {3, 4}};
        int[][] result = new PixelateFilter(1).filter(input);
        assertArrayEquals(input[0], result[0]);
        assertArrayEquals(input[1], result[1]);
    }

    @Test
    void blockBiggerThanTheImage_fillsEverythingWithOneColour() {
        int[][] input = {{1, 2}, {3, 4}};
        int[][] result = new PixelateFilter(10).filter(input);
        assertArrayEquals(new int[] {1, 1}, result[0]);
        assertArrayEquals(new int[] {1, 1}, result[1]);
    }

    @Test
    void doesNotChangeItsInput() {
        int[][] input = {{1, 2}, {3, 4}};
        new PixelateFilter(2).filter(input);
        assertArrayEquals(new int[] {1, 2}, input[0]);
        assertArrayEquals(new int[] {3, 4}, input[1]);
    }

    @Test
    void blockSizeBelowOne_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new PixelateFilter(0));
    }

    @Test
    void name_includesTheBlockSize() {
        assertEquals("PixelateFilter_30", new PixelateFilter(30).name());
    }
}
