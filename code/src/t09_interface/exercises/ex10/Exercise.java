package t09_interface.exercises.ex10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import t09_interface.demos.de01.GreyscaleFilter;
import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;
import t09_interface.demos.de01.ImageProcessor;

public class Exercise {

    private static final String INPUT_PATH = ImageHelper.dataFolder() + "input.png";
    private static final String OUTPUT_FOLDER = ImageHelper.dataFolder() + "output/";

    public static void run() {
        // The new filter goes in the same list as the supplied filters
        List<IImageFilter> filters = new ArrayList<>();
        filters.add(new InvertFilter());
        filters.add(new GreyscaleFilter());

        try {
            int[][] original = ImageHelper.readPng(INPUT_PATH);
            int[][] inverted = new InvertFilter().filter(original);
            ImageHelper.writePng(inverted, OUTPUT_FOLDER + "ex10_InvertFilter.png");

            int[][] both = ImageProcessor.applyAll(filters, original);
            ImageHelper.writePng(both, OUTPUT_FOLDER + "ex10_Invert_then_Greyscale.png");

            System.out.println("Top-left pixel before: " + ImageHelper.describe(original[0][0]));
            System.out.println("Top-left pixel after:  " + ImageHelper.describe(inverted[0][0]));
            System.out.println("Saved " + OUTPUT_FOLDER + "ex10_InvertFilter.png");
            System.out.println("Saved " + OUTPUT_FOLDER + "ex10_Invert_then_Greyscale.png");
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }
}
