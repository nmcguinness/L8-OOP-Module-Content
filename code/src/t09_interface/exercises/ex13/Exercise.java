package t09_interface.exercises.ex13;

import java.io.IOException;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

public class Exercise {

    private static final String INPUT_PATH = ImageHelper.dataFolder() + "input.png";
    private static final String OUTPUT_PATH = ImageHelper.dataFolder() + "output/"
            + "ex13_SepiaFilter.png";

    public static void run() {
        IImageFilter sepia = new SepiaFilter();

        try {
            int[][] original = ImageHelper.readPng(INPUT_PATH);
            int[][] result = sepia.filter(original);
            ImageHelper.writePng(result, OUTPUT_PATH);

            // Row 50, column 250 is inside the yellow sun: bright enough to need clamping
            System.out.println("Sky before:  " + ImageHelper.describe(original[0][0]));
            System.out.println("Sky after:   " + ImageHelper.describe(result[0][0]));
            System.out.println("Sun before:  " + ImageHelper.describe(original[50][250]));
            System.out.println("Sun after:   " + ImageHelper.describe(result[50][250]));
            System.out.println("Saved " + OUTPUT_PATH);
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }
}
