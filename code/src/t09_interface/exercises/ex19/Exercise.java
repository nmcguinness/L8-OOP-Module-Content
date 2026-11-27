package t09_interface.exercises.ex19;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

public class Exercise {

    private static final String PHOTO_PATH = ImageHelper.dataFolder() + "images/jelly_beans.png";
    private static final String OUTPUT_FOLDER = ImageHelper.dataFolder() + "output/";

    public static void run() {
        IImageFilter posterize = new PosterizeFilter(4);

        try {
            int[][] original = ImageHelper.readPng(PHOTO_PATH);
            int[][] result = posterize.filter(original);
            String outputPath = OUTPUT_FOLDER + "ex19_jelly_beans_" + posterize.name() + ".png";
            ImageHelper.writePng(result, outputPath);

            System.out.println("Top-left before: " + ImageHelper.describe(original[0][0]));
            System.out.println("Top-left after:  " + ImageHelper.describe(result[0][0]));
            System.out.println("Different colours before: " + countColours(original));
            System.out.println("Different colours after:  " + countColours(result));
            System.out.println("Saved " + outputPath);
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }

    /** Counts how many different pixel values the image holds. */
    public static int countColours(int[][] pixels) {
        Set<Integer> colours = new HashSet<>();
        for (int[] row : pixels) {
            for (int pixel : row) {
                colours.add(pixel);
            }
        }
        return colours.size();
    }
}
