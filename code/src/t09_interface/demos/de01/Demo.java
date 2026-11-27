package t09_interface.demos.de01;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Applies one filter to each of four images, then two filters in series to a fifth image.
 * Used by the t09 image filters supplement.
 *
 * <p>Input PNG files go in {@code code/data/t09_image_filters/images/}. Results are saved in
 * {@code code/data/t09_image_filters/output/}. {@link ImageHelper#dataFolder()} finds these
 * folders whether the program runs from the project root or from the code folder.
 */
public class Demo {

    private static final String IMAGE_FOLDER = ImageHelper.dataFolder() + "images/";
    private static final String OUTPUT_FOLDER = ImageHelper.dataFolder() + "output/";

    public static void run() {
        System.out.println("Put the PNG files you want to process in:");
        System.out.println("  " + Path.of(IMAGE_FOLDER).toAbsolutePath());
        System.out.println("Results go in:");
        System.out.println("  " + Path.of(OUTPUT_FOLDER).toAbsolutePath());
        System.out.println();

        // One filter per image. Four different classes go through the same parameter type.
        applyFilter(new GreyscaleFilter(), "woman.png");
        applyFilter(new BoxBlurFilter(3), "peppers.png");
        applyFilter(new SobelFilter(), "airplane.png");
        applyFilter(new ScaleFilter(0.25), "airport.png");

        // Two filters in series: the output of the first is the input of the second
        try {
            IImageFilter blur = new BoxBlurFilter(2);
            IImageFilter edges = new SobelFilter();

            int[][] pixels = ImageHelper.readPng(IMAGE_FOLDER + "sailboat.png");
            pixels = blur.filter(pixels);
            pixels = edges.filter(pixels);

            String outputPath = OUTPUT_FOLDER + "sailboat_blur_then_sobel.png";
            ImageHelper.writePng(pixels, outputPath);
            System.out.println("sailboat.png: " + blur.name() + " then " + edges.name()
                    + " -> " + outputPath);
        } catch (IOException e) {
            System.out.println("Could not process sailboat.png: " + e.getMessage());
        }
    }

    /**
     * Loads one PNG from the image folder, applies one filter, and saves the result in the
     * output folder as {@code <image name>_<filter name>.png}.
     *
     * @param filter   any object that implements IImageFilter
     * @param fileName the file name only, for example {@code "peppers.png"}
     */
    public static void applyFilter(IImageFilter filter, String fileName) {
        String inputPath = IMAGE_FOLDER + fileName;
        String baseName = fileName.replaceFirst("(?i)\\.png$", "");
        String outputPath = OUTPUT_FOLDER + baseName + "_" + filter.name() + ".png";

        try {
            int[][] pixels = ImageHelper.readPng(inputPath);
            int[][] result = filter.filter(pixels);
            ImageHelper.writePng(result, outputPath);
            System.out.println(fileName + ": " + filter.name() + " -> " + outputPath
                    + " (" + result[0].length + " x " + result.length + ")");
        } catch (IOException e) {
            System.out.println("Could not process " + inputPath + ": " + e.getMessage());
        }
    }
}
