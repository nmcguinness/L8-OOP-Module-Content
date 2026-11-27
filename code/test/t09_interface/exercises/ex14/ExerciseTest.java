package t09_interface.exercises.ex14;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import t09_interface.demos.de01.BoxBlurFilter;
import t09_interface.demos.de01.GreyscaleFilter;
import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;
import t09_interface.demos.de01.ImageProcessor;
import t09_interface.demos.de01.SobelFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 ex14 - filter order matters")
class ExerciseTest {

    private static final int BLACK = ImageHelper.toArgb(255, 0, 0, 0);
    private static final int WHITE = ImageHelper.toArgb(255, 255, 255, 255);

    /** A 6 x 6 image: left half black, right half white. */
    private static int[][] edgeImage() {
        int[][] pixels = new int[6][6];
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                pixels[row][col] = col < 3 ? BLACK : WHITE;
            }
        }
        return pixels;
    }

    @Test
    void countDifferent_countsMismatchedPositions() {
        int[][] a = {{1, 2}, {3, 4}};
        int[][] b = {{1, 0}, {0, 4}};
        assertEquals(2, Exercise.countDifferent(a, b));
    }

    @Test
    void reversedOrder_givesADifferentImage() {
        List<IImageFilter> firstOrder =
                List.of(new GreyscaleFilter(), new BoxBlurFilter(1), new SobelFilter());
        List<IImageFilter> reversed = firstOrder.reversed();

        int[][] a = ImageProcessor.applyAll(firstOrder, edgeImage());
        int[][] b = ImageProcessor.applyAll(reversed, edgeImage());
        assertTrue(Exercise.countDifferent(a, b) > 0);
    }

    @Test
    void greyscaleBeforeSobel_changesNothing() {
        // Sobel works on luminance anyway, so greyscale first makes no difference
        int[][] withGrey = ImageProcessor.applyAll(
                List.of(new GreyscaleFilter(), new SobelFilter()), edgeImage());
        int[][] without = new SobelFilter().filter(edgeImage());
        assertEquals(0, Exercise.countDifferent(withGrey, without));
    }
}
