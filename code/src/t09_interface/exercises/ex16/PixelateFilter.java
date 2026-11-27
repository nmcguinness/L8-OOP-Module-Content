package t09_interface.exercises.ex16;

import t09_interface.demos.de01.IImageFilter;

/**
 * Gives the image a blocky mosaic look. The image is split into squares of blockSize by
 * blockSize pixels, and every pixel in a square gets the colour of the square's top-left
 * pixel. Squares at the right and bottom edges may be smaller.
 */
public class PixelateFilter implements IImageFilter {

    private final int blockSize;

    public PixelateFilter(int blockSize) {
        if (blockSize < 1) {
            throw new IllegalArgumentException("blockSize must be 1 or more, got " + blockSize);
        }
        this.blockSize = blockSize;
    }

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int[][] result = new int[height][width];

        // Step from the top-left corner of one block to the next
        for (int top = 0; top < height; top += blockSize) {
            for (int left = 0; left < width; left += blockSize) {
                int colour = pixels[top][left];

                // Fill the block, stopping at the edge of the image
                int bottom = Math.min(top + blockSize, height);
                int right = Math.min(left + blockSize, width);
                for (int row = top; row < bottom; row++) {
                    for (int col = left; col < right; col++) {
                        result[row][col] = colour;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String name() {
        return "PixelateFilter_" + blockSize;
    }
}
