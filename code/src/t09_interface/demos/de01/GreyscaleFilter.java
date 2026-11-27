package t09_interface.demos.de01;

/**
 * Turns each pixel grey. Uses the brightness weights from the ITU-R BT.601 standard,
 * because the eye sees green as brighter than red, and red as brighter than blue.
 */
public class GreyscaleFilter implements IImageFilter {

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = pixels[row][col];
                int grey = luminance(pixel);
                result[row][col] = ImageHelper.toArgb(ImageHelper.alpha(pixel), grey, grey, grey);
            }
        }
        return result;
    }

    /** Returns the brightness of one pixel, 0 to 255. */
    public static int luminance(int pixel) {
        double grey = 0.299 * ImageHelper.red(pixel)
                + 0.587 * ImageHelper.green(pixel)
                + 0.114 * ImageHelper.blue(pixel);
        return (int) Math.round(grey);
    }
}
