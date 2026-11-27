package t09_interface.exercises.ex18;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import t09_interface.demos.de01.ImageHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t09 ex18 - BrightnessFilter")
class BrightnessFilterTest {

    private static int[][] onePixel(int alpha, int red, int green, int blue) {
        return new int[][] {{ImageHelper.toArgb(alpha, red, green, blue)}};
    }

    @Test
    void positiveAmount_addsToEachColourChannel() {
        int[][] result = new BrightnessFilter(60).filter(onePixel(255, 70, 130, 200));
        assertEquals(ImageHelper.toArgb(255, 130, 190, 255), result[0][0]);
    }

    @Test
    void negativeAmount_darkensAndClampsAtZero() {
        int[][] result = new BrightnessFilter(-60).filter(onePixel(255, 70, 130, 40));
        assertEquals(ImageHelper.toArgb(255, 10, 70, 0), result[0][0]);
    }

    @Test
    void keepsAlpha() {
        int[][] result = new BrightnessFilter(100).filter(onePixel(30, 0, 0, 0));
        assertEquals(30, ImageHelper.alpha(result[0][0]));
    }

    @Test
    void zeroAmount_leavesThePixelUnchanged() {
        int[][] input = onePixel(255, 1, 2, 3);
        int[][] result = new BrightnessFilter(0).filter(input);
        assertEquals(input[0][0], result[0][0]);
    }

    @Test
    void amountOutsideRange_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new BrightnessFilter(256));
        assertThrows(IllegalArgumentException.class, () -> new BrightnessFilter(-256));
    }

    @Test
    void name_includesTheAmount() {
        assertEquals("BrightnessFilter_-60", new BrightnessFilter(-60).name());
    }
}
