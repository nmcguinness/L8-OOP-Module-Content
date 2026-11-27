package t09_interface.exercises.ex11;

import t09_interface.demos.de01.GreyscaleFilter;
import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

/**
 * Turns every pixel pure black or pure white. A pixel whose luminance is below the level
 * becomes black; any other pixel becomes white. Alpha is kept.
 */
public class ThresholdFilter implements IImageFilter {

    private final int level;

    public ThresholdFilter(int level) {
        if (level < 0 || level > 255) {
            throw new IllegalArgumentException("level must be 0 to 255, got " + level);
        }
        this.level = level;
    }

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = pixels[row][col];
                int value = GreyscaleFilter.luminance(pixel) < level ? 0 : 255;
                int alpha = ImageHelper.alpha(pixel);
                result[row][col] = ImageHelper.toArgb(alpha, value, value, value);
            }
        }
        return result;
    }

    @Override
    public String name() {
        return "ThresholdFilter_" + level;
    }
}
