package t09_interface.exercises.ex13;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

/**
 * Gives the image a warm brown, old-photo tone. Each new channel is a weighted sum of the
 * old red, green and blue. The weights in each row add up to more than 1, so a bright
 * pixel can go past 255; ImageHelper.toArgb clamps it back to 255.
 */
public class SepiaFilter implements IImageFilter {

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = pixels[row][col];
                int r = ImageHelper.red(pixel);
                int g = ImageHelper.green(pixel);
                int b = ImageHelper.blue(pixel);

                int newRed = (int) Math.round(0.393 * r + 0.769 * g + 0.189 * b);
                int newGreen = (int) Math.round(0.349 * r + 0.686 * g + 0.168 * b);
                int newBlue = (int) Math.round(0.272 * r + 0.534 * g + 0.131 * b);

                result[row][col] = ImageHelper.toArgb(
                        ImageHelper.alpha(pixel), newRed, newGreen, newBlue);
            }
        }
        return result;
    }
}
