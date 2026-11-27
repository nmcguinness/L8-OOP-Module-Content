package t09_interface.demos.de01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 de01 - image filters behind one interface")
class ImageFiltersTest {

    private static final int RED = ImageHelper.toArgb(255, 255, 0, 0);
    private static final int WHITE = ImageHelper.toArgb(255, 255, 255, 255);
    private static final int BLACK = ImageHelper.toArgb(255, 0, 0, 0);

    private static int[][] filled(int height, int width, int pixel) {
        int[][] pixels = new int[height][width];
        for (int[] row : pixels) {
            Arrays.fill(row, pixel);
        }
        return pixels;
    }

    @Test
    void channels_packAndUnpack() {
        int pixel = ImageHelper.toArgb(10, 20, 30, 40);
        assertEquals(10, ImageHelper.alpha(pixel));
        assertEquals(20, ImageHelper.red(pixel));
        assertEquals(30, ImageHelper.green(pixel));
        assertEquals(40, ImageHelper.blue(pixel));
    }

    @Test
    void toArgb_clampsOutOfRangeValues() {
        assertEquals(ImageHelper.toArgb(255, 255, 0, 0), ImageHelper.toArgb(300, 999, -5, 0));
    }

    @Test
    void greyscale_redBecomesItsLuminance() {
        int[][] result = new GreyscaleFilter().filter(filled(1, 1, RED));
        // 0.299 * 255 = 76.2, rounded to 76
        assertEquals(ImageHelper.toArgb(255, 76, 76, 76), result[0][0]);
    }

    @Test
    void filter_doesNotChangeItsInput() {
        int[][] input = filled(3, 3, RED);
        int[][] result = new GreyscaleFilter().filter(input);
        assertNotSame(input, result);
        assertEquals(RED, input[1][1]);
    }

    @Test
    void boxBlur_leavesAFlatImageUnchanged() {
        int[][] input = filled(4, 5, RED);
        int[][] result = new BoxBlurFilter(1).filter(input);
        for (int row = 0; row < input.length; row++) {
            assertArrayEquals(input[row], result[row]);
        }
    }

    @Test
    void boxBlur_averagesTheNeighbourhood() {
        // One white pixel in the middle of a 3 x 3 black square
        int[][] input = filled(3, 3, BLACK);
        input[1][1] = WHITE;
        int[][] result = new BoxBlurFilter(1).filter(input);
        // Centre: 255 / 9 = 28. Corner: 255 / 4 = 63 (only 4 pixels are inside the image)
        assertEquals(ImageHelper.toArgb(255, 28, 28, 28), result[1][1]);
        assertEquals(ImageHelper.toArgb(255, 63, 63, 63), result[0][0]);
    }

    @Test
    void boxBlur_rejectsRadiusBelowOne() {
        assertThrows(IllegalArgumentException.class, () -> new BoxBlurFilter(0));
    }

    @Test
    void sobel_flatImageHasNoEdges() {
        int[][] result = new SobelFilter().filter(filled(4, 4, RED));
        assertEquals(BLACK, result[2][2]);
    }

    @Test
    void sobel_findsAVerticalEdge() {
        // Left half black, right half white
        int[][] input = filled(3, 4, BLACK);
        for (int row = 0; row < 3; row++) {
            input[row][2] = WHITE;
            input[row][3] = WHITE;
        }
        int[][] result = new SobelFilter().filter(input);
        assertEquals(WHITE, result[1][1]);
        assertEquals(BLACK, result[1][0]);
    }

    @Test
    void scale_halvesEachSide() {
        int[][] result = new ScaleFilter(0.5).filter(filled(10, 20, RED));
        assertEquals(5, result.length);
        assertEquals(10, result[0].length);
    }

    @Test
    void scale_rejectsZeroFactor() {
        assertThrows(IllegalArgumentException.class, () -> new ScaleFilter(0));
    }

    @Test
    void applyAll_runsTheFiltersInOrder() {
        List<IImageFilter> filters = List.of(new ScaleFilter(2.0), new GreyscaleFilter());
        int[][] result = ImageProcessor.applyAll(filters, filled(2, 3, RED));
        assertEquals(4, result.length);
        assertEquals(6, result[0].length);
        assertEquals(ImageHelper.toArgb(255, 76, 76, 76), result[3][5]);
    }

    @Test
    void name_defaultsToTheClassName() {
        assertEquals("GreyscaleFilter", new GreyscaleFilter().name());
        assertEquals("BoxBlurFilter_r2", new BoxBlurFilter(2).name());
    }

    @Test
    void png_writeThenReadGivesTheSamePixels(@TempDir Path folder) throws IOException {
        int[][] input = filled(2, 3, RED);
        input[1][2] = ImageHelper.toArgb(128, 10, 20, 30);
        String path = folder.resolve("sub/test.png").toString();

        ImageHelper.writePng(input, path);
        int[][] back = ImageHelper.readPng(path);

        assertTrue(Files.exists(Path.of(path)));
        for (int row = 0; row < input.length; row++) {
            assertArrayEquals(input[row], back[row]);
        }
    }

    @Test
    void readPng_missingFileThrowsIOException() {
        assertThrows(IOException.class, () -> ImageHelper.readPng("no/such/file.png"));
    }
}
