package t09_interface.exercises.ex16;

import java.io.IOException;

import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;

public class Exercise {

    private static final String INPUT_PATH = ImageHelper.dataFolder() + "input.png";
    private static final String PHOTO_PATH = ImageHelper.dataFolder() + "images/jelly_beans.png";
    private static final String OUTPUT_FOLDER = ImageHelper.dataFolder() + "output/";
    private static final int BLOCK_SIZE = 30;

    public static void run() {
        IImageFilter pixelate = new PixelateFilter(BLOCK_SIZE);

        try {
            int[][] original = ImageHelper.readPng(INPUT_PATH);
            int[][] result = pixelate.filter(original);
            String outputPath = OUTPUT_FOLDER + "ex16_" + pixelate.name() + ".png";
            ImageHelper.writePng(result, outputPath);

            int width = original[0].length;
            int height = original.length;
            int blocksAcross = (width + BLOCK_SIZE - 1) / BLOCK_SIZE;
            int blocksDown = (height + BLOCK_SIZE - 1) / BLOCK_SIZE;
            System.out.println("Image: " + width + " x " + height + " pixels");
            System.out.println("Blocks: " + blocksAcross + " across, " + blocksDown + " down");
            System.out.println("(200, 50) now copies (180, 30): "
                    + (result[200][50] == original[180][30]));
            System.out.println("Last block is " + (width - BLOCK_SIZE * (blocksAcross - 1))
                    + " pixels wide");
            System.out.println("Saved " + outputPath);

            // The same filter on a photograph
            int[][] photo = ImageHelper.readPng(PHOTO_PATH);
            String photoOutput = OUTPUT_FOLDER + "ex16_jelly_beans_" + pixelate.name() + ".png";
            ImageHelper.writePng(pixelate.filter(photo), photoOutput);
            System.out.println("Saved " + photoOutput);
        } catch (IOException e) {
            System.out.println("Could not process the image: " + e.getMessage());
        }
    }
}
