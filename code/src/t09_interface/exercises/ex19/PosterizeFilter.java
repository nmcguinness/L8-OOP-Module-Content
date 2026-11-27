package t09_interface.exercises.ex19;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

/**
 * Gives the image a flat, poster-like look by allowing only a few values per channel.
 * With 4 levels, each channel becomes 0, 85, 170 or 255: whichever is nearest. Alpha is kept.
 */
public class PosterizeFilter implements IImageFilter {

    private final int levels;
    private final double step;

    public PosterizeFilter(int levels) {
        if (levels < 2 || levels > 256) {
            throw new IllegalArgumentException("levels must be 2 to 256, got " + levels);
        }
        this.levels = levels;
        // The gap between two allowed values, for example 85.0 for 4 levels
        this.step = 255.0 / (levels - 1);
    }

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = pixels[row][col];
                result[row][col] = ImageHelper.toArgb(
                        ImageHelper.alpha(pixel),
                        nearestLevel(ImageHelper.red(pixel)),
                        nearestLevel(ImageHelper.green(pixel)),
                        nearestLevel(ImageHelper.blue(pixel)));
            }
        }
        return result;
    }

    /** Returns the allowed value nearest to a channel value. */
    int nearestLevel(int value) {
        long index = Math.round(value / step);
        return (int) Math.round(index * step);
    }

    @Override
    public String name() {
        return "PosterizeFilter_" + levels;
    }
}
