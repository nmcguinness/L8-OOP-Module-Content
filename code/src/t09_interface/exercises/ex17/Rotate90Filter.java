package t09_interface.exercises.ex17;

import t09_interface.demos.de01.IImageFilter;

/**
 * Turns the image a quarter turn clockwise. A W x H image becomes an H x W image:
 * the result has as many rows as the input has columns.
 */
public class Rotate90Filter implements IImageFilter {

    @Override
    public int[][] filter(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;

        // Swapped: the new image is width rows tall and height columns wide
        int[][] result = new int[width][height];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                // The bottom-left of the input becomes the top-left of the result
                result[row][col] = pixels[height - 1 - col][row];
            }
        }
        return result;
    }
}
