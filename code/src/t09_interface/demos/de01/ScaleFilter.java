package t09_interface.demos.de01;

/**
 * Resizes the image by a factor: 0.5 halves each side, 2.0 doubles it.
 * Uses nearest-neighbour sampling: each output pixel copies the closest input pixel.
 * This is the only filter here that returns an array of a different size.
 */
public class ScaleFilter implements IImageFilter {

    private final double factor;

    public ScaleFilter(double factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("factor must be more than 0, got " + factor);
        }
        this.factor = factor;
    }

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int newHeight = Math.max(1, (int) Math.round(height * factor));
        int newWidth = Math.max(1, (int) Math.round(width * factor));
        int[][] result = new int[newHeight][newWidth];

        for (int row = 0; row < newHeight; row++) {
            int sourceRow = Math.min(height - 1, (int) (row / factor));
            for (int col = 0; col < newWidth; col++) {
                int sourceCol = Math.min(width - 1, (int) (col / factor));
                result[row][col] = pixels[sourceRow][sourceCol];
            }
        }
        return result;
    }

    @Override
    public String name() {
        return "ScaleFilter_x" + factor;
    }
}
