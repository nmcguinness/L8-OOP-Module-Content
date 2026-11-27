package t09_interface.exercises.ex12;

import t09_interface.demos.de01.IImageFilter;

/** Mirrors the image left to right. Each row is copied in reverse order. */
public class FlipHorizontalFilter implements IImageFilter {

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // The last column of the input becomes the first column of the result
                result[row][col] = pixels[row][width - 1 - col];
            }
        }
        return result;
    }
}
