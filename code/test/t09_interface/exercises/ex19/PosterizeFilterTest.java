package t09_interface.exercises.ex19;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import t09_interface.demos.de01.ImageHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t09 ex19 - PosterizeFilter")
class PosterizeFilterTest {

    @Test
    void fourLevels_roundToTheNearestAllowedValue() {
        PosterizeFilter posterize = new PosterizeFilter(4);
        // Allowed values: 0, 85, 170, 255
        assertEquals(0, posterize.nearestLevel(42));
        assertEquals(85, posterize.nearestLevel(43));
        assertEquals(170, posterize.nearestLevel(200));
        assertEquals(255, posterize.nearestLevel(255));
    }

    @Test
    void twoLevels_giveOnlyZeroOr255() {
        PosterizeFilter posterize = new PosterizeFilter(2);
        assertEquals(0, posterize.nearestLevel(127));
        assertEquals(255, posterize.nearestLevel(128));
    }

    @Test
    void filter_posterizesEachColourChannelAndKeepsAlpha() {
        int[][] input = {{ImageHelper.toArgb(90, 70, 130, 200)}};
        int[][] result = new PosterizeFilter(4).filter(input);
        assertEquals(ImageHelper.toArgb(90, 85, 170, 170), result[0][0]);
    }

    @Test
    void levels256_leavesTheImageUnchanged() {
        int[][] input = {{ImageHelper.toArgb(255, 1, 128, 254)}};
        int[][] result = new PosterizeFilter(256).filter(input);
        assertEquals(input[0][0], result[0][0]);
    }

    @Test
    void levelsOutsideRange_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new PosterizeFilter(1));
        assertThrows(IllegalArgumentException.class, () -> new PosterizeFilter(257));
    }

    @Test
    void countColours_countsDistinctPixels() {
        int[][] pixels = {{1, 2, 2}, {3, 1, 2}};
        assertEquals(3, Exercise.countColours(pixels));
    }
}
