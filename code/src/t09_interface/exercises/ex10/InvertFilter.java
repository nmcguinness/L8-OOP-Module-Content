package t09_interface.exercises.ex10;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

/** Makes a photo negative: each colour channel value v becomes 255 - v. Alpha is kept. */
public class InvertFilter implements IImageFilter {

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
                        255 - ImageHelper.red(pixel),
                        255 - ImageHelper.green(pixel),
                        255 - ImageHelper.blue(pixel));
            }
        }
        return result;
    }
}
