package t09_interface.exercises.ex13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import t09_interface.demos.de01.ImageHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t09 ex13 - SepiaFilter")
class SepiaFilterTest {

    @Test
    void sepia_midGrey_usesTheWeights() {
        // r = g = b = 100: red 135.1, green 120.3, blue 93.7, rounded
        int[][] input = {{ImageHelper.toArgb(255, 100, 100, 100)}};
        int[][] result = new SepiaFilter().filter(input);
        assertEquals(ImageHelper.toArgb(255, 135, 120, 94), result[0][0]);
    }

    @Test
    void sepia_white_isClampedTo255() {
        // Without clamping, red would be 344 and green 307, which do not fit in 8 bits
        int[][] input = {{ImageHelper.toArgb(255, 255, 255, 255)}};
        int[][] result = new SepiaFilter().filter(input);
        assertEquals(255, ImageHelper.red(result[0][0]));
        assertEquals(255, ImageHelper.green(result[0][0]));
        assertEquals(239, ImageHelper.blue(result[0][0]));
    }

    @Test
    void sepia_black_staysBlack() {
        int[][] input = {{ImageHelper.toArgb(255, 0, 0, 0)}};
        int[][] result = new SepiaFilter().filter(input);
        assertEquals(input[0][0], result[0][0]);
    }

    @Test
    void sepia_keepsAlpha() {
        int[][] input = {{ImageHelper.toArgb(60, 10, 20, 30)}};
        int[][] result = new SepiaFilter().filter(input);
        assertEquals(60, ImageHelper.alpha(result[0][0]));
    }
}
