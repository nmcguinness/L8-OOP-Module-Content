package t09_interface.exercises.ex15;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;
import t09_interface.exercises.ex10.InvertFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/** Exercise 15: tests for the InvertFilter from exercise 10, using tiny images. */
@DisplayName("t09 ex15 - testing InvertFilter")
class InvertFilterTest {

    @Test
    void invert_onePixel_eachChannelBecomes255Minus() {
        int[][] input = {{ImageHelper.toArgb(255, 70, 130, 200)}};
        int[][] result = new InvertFilter().filter(input);
        assertEquals(ImageHelper.toArgb(255, 185, 125, 55), result[0][0]);
    }

    @Test
    void invert_keepsAlpha() {
        int[][] input = {{ImageHelper.toArgb(100, 0, 0, 0)}};
        int[][] result = new InvertFilter().filter(input);
        assertEquals(100, ImageHelper.alpha(result[0][0]));
    }

    @Test
    void invert_twiceGivesTheOriginal() {
        int[][] input = {
            {ImageHelper.toArgb(255, 1, 2, 3), ImageHelper.toArgb(255, 250, 128, 0)}
        };
        IImageFilter invert = new InvertFilter();
        int[][] result = invert.filter(invert.filter(input));
        assertEquals(input[0][0], result[0][0]);
        assertEquals(input[0][1], result[0][1]);
    }

    @Test
    void invert_doesNotChangeItsInput() {
        int pixel = ImageHelper.toArgb(255, 10, 20, 30);
        int[][] input = {{pixel}};
        int[][] result = new InvertFilter().filter(input);
        assertNotSame(input, result);
        assertEquals(pixel, input[0][0]);
    }

    @Test
    void invert_keepsTheSize() {
        int[][] result = new InvertFilter().filter(new int[3][5]);
        assertEquals(3, result.length);
        assertEquals(5, result[0].length);
    }
}
