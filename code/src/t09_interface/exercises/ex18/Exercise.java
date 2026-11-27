package t09_interface.exercises.ex18;

import java.io.IOException;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

public class Exercise {

    private static final String INPUT_PATH = ImageHelper.dataFolder() + "input.png";
    private static final String OUTPUT_FOLDER = ImageHelper.dataFolder() + "output/";

    public static void run() {
        IImageFilter brighter = new BrightnessFilter(60);
        IImageFilter darker = new BrightnessFilter(-60);

        try {
            int[][] original = ImageHelper.readPng(INPUT_PATH);
            int[][] light = brighter.filter(original);
            int[][] dark = darker.filter(original);
            ImageHelper.writePng(light, OUTPUT_FOLDER + "ex18_" + brighter.name() + ".png");
            ImageHelper.writePng(dark, OUTPUT_FOLDER + "ex18_" + darker.name() + ".png");

            // (0, 0) is in the sky; (50, 250) is in the sun, which is already bright
            System.out.println("Sky:     " + ImageHelper.describe(original[0][0]));
            System.out.println("  +60:   " + ImageHelper.describe(light[0][0]));
            System.out.println("  -60:   " + ImageHelper.describe(dark[0][0]));
            System.out.println("Sun:     " + ImageHelper.describe(original[50][250]));
            System.out.println("  +60:   " + ImageHelper.describe(light[50][250]));
            System.out.println("Saved " + OUTPUT_FOLDER + "ex18_" + brighter.name() + ".png");
            System.out.println("Saved " + OUTPUT_FOLDER + "ex18_" + darker.name() + ".png");
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }
}
