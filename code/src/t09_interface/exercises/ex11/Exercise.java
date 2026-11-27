package t09_interface.exercises.ex11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import t09_interface.demos.de01.BoxBlurFilter;
import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;
import t09_interface.demos.de01.ImageProcessor;
import t09_interface.demos.de01.SobelFilter;

public class Exercise {

    private static final String INPUT_PATH = ImageHelper.dataFolder() + "input.png";
    private static final String OUTPUT_FOLDER = ImageHelper.dataFolder() + "output/";

    public static void run() {
        List<IImageFilter> filters = new ArrayList<>();
        filters.add(new BoxBlurFilter(1));
        filters.add(new SobelFilter());
        filters.add(new ThresholdFilter(80));

        try {
            int[][] pixels = ImageHelper.readPng(INPUT_PATH);
            int[][] result = ImageProcessor.applyAll(filters, pixels);

            String outputPath = OUTPUT_FOLDER + "ex11_" + filters.getLast().name() + ".png";
            ImageHelper.writePng(result, outputPath);

            int white = countWhite(result);
            int total = result.length * result[0].length;
            System.out.println("White pixels: " + white + " of " + total);
            System.out.println("Saved " + outputPath);
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }

    /** Counts pixels whose red channel is 255. After ThresholdFilter, that means white. */
    public static int countWhite(int[][] pixels) {
        int count = 0;
        for (int[] row : pixels) {
            for (int pixel : row) {
                if (ImageHelper.red(pixel) == 255) {
                    count++;
                }
            }
        }
        return count;
    }
}
