package t09_interface.exercises.ex14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import t09_interface.demos.de01.BoxBlurFilter;
import t09_interface.demos.de01.GreyscaleFilter;
import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;
import t09_interface.demos.de01.ImageProcessor;
import t09_interface.demos.de01.SobelFilter;

/**
 * Runs the same three filters in two orders and compares the results.
 *
 * <p>Why they differ: in the first order, the blur runs before Sobel, so Sobel sees a
 * smoothed image and marks thin, clean outlines. In the reversed order, Sobel runs first
 * and the blur then softens and widens its edge lines. Greyscale last changes nothing,
 * because Sobel already outputs grey. Greyscale first changes nothing either, because
 * Sobel works on luminance anyway. So only the blur's position matters.
 */
public class Exercise {

    private static final String INPUT_PATH = ImageHelper.dataFolder() + "input.png";
    private static final String OUTPUT_FOLDER = ImageHelper.dataFolder() + "output/";

    public static void run() {
        List<IImageFilter> firstOrder = new ArrayList<>();
        firstOrder.add(new GreyscaleFilter());
        firstOrder.add(new BoxBlurFilter(1));
        firstOrder.add(new SobelFilter());

        // Same objects, reverse order
        List<IImageFilter> reversed = new ArrayList<>(firstOrder.reversed());

        try {
            int[][] original = ImageHelper.readPng(INPUT_PATH);
            int[][] a = ImageProcessor.applyAll(firstOrder, original);
            int[][] b = ImageProcessor.applyAll(reversed, original);
            ImageHelper.writePng(a, OUTPUT_FOLDER + "ex14_first_order.png");
            ImageHelper.writePng(b, OUTPUT_FOLDER + "ex14_reversed_order.png");

            System.out.println("First order:    " + names(firstOrder));
            System.out.println("Reversed order: " + names(reversed));
            System.out.println("Pixels that differ: " + countDifferent(a, b)
                    + " of " + (a.length * a[0].length));
            System.out.println("Bright pixels (red > 128), first order:    " + countBright(a));
            System.out.println("Bright pixels (red > 128), reversed order: " + countBright(b));
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }

    private static String names(List<IImageFilter> filters) {
        List<String> names = new ArrayList<>();
        for (IImageFilter filter : filters) {
            names.add(filter.name());
        }
        return String.join(" -> ", names);
    }

    /** Counts positions where the two images hold different pixels. Same size assumed. */
    public static int countDifferent(int[][] a, int[][] b) {
        int count = 0;
        for (int row = 0; row < a.length; row++) {
            for (int col = 0; col < a[0].length; col++) {
                if (a[row][col] != b[row][col]) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int countBright(int[][] pixels) {
        int count = 0;
        for (int[] row : pixels) {
            for (int pixel : row) {
                if (ImageHelper.red(pixel) > 128) {
                    count++;
                }
            }
        }
        return count;
    }
}
