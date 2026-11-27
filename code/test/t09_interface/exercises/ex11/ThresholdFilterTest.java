package t09_interface.exercises.ex11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import t09_interface.demos.de01.ImageHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t09 ex11 - ThresholdFilter")
class ThresholdFilterTest {

    private static final int BLACK = ImageHelper.toArgb(255, 0, 0, 0);
    private static final int WHITE = ImageHelper.toArgb(255, 255, 255, 255);

    private static int grey(int value) {
        return ImageHelper.toArgb(255, value, value, value);
    }

    @Test
    void belowLevel_becomesBlack() {
        int[][] result = new ThresholdFilter(80).filter(new int[][] {{grey(79)}});
        assertEquals(BLACK, result[0][0]);
    }

    @Test
    void atLevel_becomesWhite() {
        int[][] result = new ThresholdFilter(80).filter(new int[][] {{grey(80)}});
        assertEquals(WHITE, result[0][0]);
    }

    @Test
    void usesLuminance_notTheRedChannel() {
        // Pure blue has luminance round(0.114 * 255) = 29, so it is below 80
        int[][] result = new ThresholdFilter(80)
                .filter(new int[][] {{ImageHelper.toArgb(255, 0, 0, 255)}});
        assertEquals(BLACK, result[0][0]);
    }

    @Test
    void keepsAlpha() {
        int[][] result = new ThresholdFilter(80)
                .filter(new int[][] {{ImageHelper.toArgb(40, 200, 200, 200)}});
        assertEquals(ImageHelper.toArgb(40, 255, 255, 255), result[0][0]);
    }

    @Test
    void levelOutsideRange_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new ThresholdFilter(-1));
        assertThrows(IllegalArgumentException.class, () -> new ThresholdFilter(256));
    }

    @Test
    void name_includesTheLevel() {
        assertEquals("ThresholdFilter_80", new ThresholdFilter(80).name());
    }

    @Test
    void countWhite_countsOnlyWhitePixels() {
        int[][] pixels = {{WHITE, BLACK}, {WHITE, WHITE}};
        assertEquals(3, Exercise.countWhite(pixels));
    }
}
