package t09_interface.demos.de01;

/**
 * Blurs the image. Each output pixel is the average of the square of pixels around it.
 * A radius of 1 uses a 3 x 3 square, a radius of 2 uses a 5 x 5 square, and so on.
 * Near an edge, only the pixels inside the image count towards the average.
 */
public class BoxBlurFilter implements IImageFilter {

    private final int radius;

    public BoxBlurFilter(int radius) {
        if (radius < 1) {
            throw new IllegalArgumentException("radius must be 1 or more, got " + radius);
        }
        this.radius = radius;
    }

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = averageAround(pixels, row, col);
            }
        }
        return result;
    }

    @Override
    public String name() {
        return "BoxBlurFilter_r" + radius;
    }

    private int averageAround(int[][] pixels, int row, int col) {
        int sumA = 0;
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        int count = 0;

        for (int r = row - radius; r <= row + radius; r++) {
            for (int c = col - radius; c <= col + radius; c++) {
                if (r < 0 || r >= pixels.length || c < 0 || c >= pixels[0].length) {
                    continue;
                }
                int pixel = pixels[r][c];
                sumA += ImageHelper.alpha(pixel);
                sumR += ImageHelper.red(pixel);
                sumG += ImageHelper.green(pixel);
                sumB += ImageHelper.blue(pixel);
                count++;
            }
        }
        return ImageHelper.toArgb(sumA / count, sumR / count, sumG / count, sumB / count);
    }
}
