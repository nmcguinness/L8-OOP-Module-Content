package t09_interface.exercises.ex18;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

/**
 * Makes the image brighter or darker by adding the same amount to the red, green and blue
 * channels. A negative amount darkens. Values past 0 or 255 are clamped. Alpha is kept.
 */
public class BrightnessFilter implements IImageFilter {

    private final int amount;

    public BrightnessFilter(int amount) {
        if (amount < -255 || amount > 255) {
            throw new IllegalArgumentException("amount must be -255 to 255, got " + amount);
        }
        this.amount = amount;
    }

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = pixels[row][col];
                // toArgb clamps each channel to 0 to 255
                result[row][col] = ImageHelper.toArgb(
                        ImageHelper.alpha(pixel),
                        ImageHelper.red(pixel) + amount,
                        ImageHelper.green(pixel) + amount,
                        ImageHelper.blue(pixel) + amount);
            }
        }
        return result;
    }

    @Override
    public String name() {
        return "BrightnessFilter_" + amount;
    }
}
