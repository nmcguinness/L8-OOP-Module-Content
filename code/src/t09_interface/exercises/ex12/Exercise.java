package t09_interface.exercises.ex12;

import java.io.IOException;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

public class Exercise {

    private static final String INPUT_PATH = ImageHelper.dataFolder() + "input.png";
    private static final String OUTPUT_PATH = ImageHelper.dataFolder() + "output/"
            + "ex12_FlipHorizontalFilter.png";

    public static void run() {
        IImageFilter flip = new FlipHorizontalFilter();

        try {
            int[][] original = ImageHelper.readPng(INPUT_PATH);
            int[][] flipped = flip.filter(original);
            ImageHelper.writePng(flipped, OUTPUT_PATH);

            int width = original[0].length;
            int row = 200;
            int col = 50;
            int mirrorCol = width - 1 - col;
            System.out.println("Before: (" + row + ", " + col + ") "
                    + ImageHelper.describe(original[row][col]));
            System.out.println("After:  (" + row + ", " + mirrorCol + ") "
                    + ImageHelper.describe(flipped[row][mirrorCol]));
            System.out.println("Saved " + OUTPUT_PATH);
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }
}
