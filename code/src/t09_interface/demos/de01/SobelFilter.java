package t09_interface.demos.de01;

/**
 * Finds edges. The output is a grey image: white where brightness changes sharply,
 * black where the image is flat.
 *
 * <p>For each pixel, two 3 x 3 kernels measure the change in brightness across (gx) and
 * down (gy). The edge strength is the length of the vector (gx, gy). Off the edge of the
 * image, the nearest edge pixel is used again.
 */
public class SobelFilter implements IImageFilter {

    private static final int[][] KERNEL_X = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };

    private static final int[][] KERNEL_Y = {
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}
    };

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;

        // Sobel works on brightness, so work out one grey value per pixel first
        int[][] grey = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grey[row][col] = GreyscaleFilter.luminance(pixels[row][col]);
            }
        }

        int[][] result = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int gx = 0;
                int gy = 0;
                for (int kr = -1; kr <= 1; kr++) {
                    for (int kc = -1; kc <= 1; kc++) {
                        int r = Math.max(0, Math.min(height - 1, row + kr));
                        int c = Math.max(0, Math.min(width - 1, col + kc));
                        gx += KERNEL_X[kr + 1][kc + 1] * grey[r][c];
                        gy += KERNEL_Y[kr + 1][kc + 1] * grey[r][c];
                    }
                }
                int edge = (int) Math.round(Math.sqrt(gx * gx + gy * gy));
                int alpha = ImageHelper.alpha(pixels[row][col]);
                result[row][col] = ImageHelper.toArgb(alpha, edge, edge, edge);
            }
        }
        return result;
    }
}
