package t09_interface.exercises.ex17;

import java.io.IOException;
import java.util.Arrays;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

public class Exercise {

    private static final String INPUT_PATH = ImageHelper.dataFolder() + "input.png";
    private static final String OUTPUT_FOLDER = ImageHelper.dataFolder() + "output/";

    public static void run() {
        IImageFilter rotate = new Rotate90Filter();

        try {
            int[][] original = ImageHelper.readPng(INPUT_PATH);
            int[][] once = rotate.filter(original);
            String outputPath = OUTPUT_FOLDER + "ex17_Rotate90Filter.png";
            ImageHelper.writePng(once, outputPath);

            System.out.println("Before: " + original[0].length + " x " + original.length
                    + " pixels");
            System.out.println("After:  " + once[0].length + " x " + once.length + " pixels");
            System.out.println("New top-left is the old bottom-left: "
                    + (once[0][0] == original[original.length - 1][0]));

            int[][] fourTimes = original;
            for (int turn = 0; turn < 4; turn++) {
                fourTimes = rotate.filter(fourTimes);
            }
            System.out.println("Four turns give the original: " + sameImage(fourTimes, original));
            System.out.println("Saved " + outputPath);
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }

    /** True when both images have the same size and the same pixels. */
    public static boolean sameImage(int[][] a, int[][] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int row = 0; row < a.length; row++) {
            if (!Arrays.equals(a[row], b[row])) {
                return false;
            }
        }
        return true;
    }
}
