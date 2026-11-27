package t09_interface.demos.de01;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import common.ProjectPaths;

/**
 * Moves images between PNG files on disk and 2D {@code int} arrays, and reads and packs
 * the four channels of one pixel.
 *
 * <p>Each pixel is one {@code int} in ARGB form: 8 bits each of alpha, red, green and blue,
 * from the high bits to the low bits. Each channel is 0 to 255.
 */
public final class ImageHelper {

    private ImageHelper() {
    }

    /**
     * Returns the folder that holds the t09 image data, ending in "/". It works whether the
     * program runs from the project root, as IntelliJ does, or from the code folder.
     */
    public static String dataFolder() {
        return ProjectPaths.path("data/t09_image_filters/");
    }

    /**
     * Reads a PNG file and returns its pixels as {@code pixels[row][col]}.
     *
     * @throws IOException if the file is missing or is not an image
     */
    public static int[][] readPng(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        if (image == null) {
            throw new IOException("Not a readable image: " + path);
        }

        int height = image.getHeight();
        int width = image.getWidth();
        int[][] pixels = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // getRGB takes x (column) first, then y (row)
                pixels[row][col] = image.getRGB(col, row);
            }
        }
        return pixels;
    }

    /**
     * Writes {@code pixels[row][col]} to a PNG file. Creates missing parent folders.
     *
     * @throws IOException if the file cannot be written
     */
    public static void writePng(int[][] pixels, String path) throws IOException {
        if (pixels == null || pixels.length == 0 || pixels[0].length == 0) {
            throw new IllegalArgumentException("pixels must have at least one row and column");
        }

        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                image.setRGB(col, row, pixels[row][col]);
            }
        }

        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        if (!ImageIO.write(image, "png", file)) {
            throw new IOException("No PNG writer available");
        }
    }

    /** Returns the alpha channel (0 = transparent, 255 = opaque). */
    public static int alpha(int argb) {
        return (argb >> 24) & 0xFF;
    }

    /** Returns the red channel, 0 to 255. */
    public static int red(int argb) {
        return (argb >> 16) & 0xFF;
    }

    /** Returns the green channel, 0 to 255. */
    public static int green(int argb) {
        return (argb >> 8) & 0xFF;
    }

    /** Returns the blue channel, 0 to 255. */
    public static int blue(int argb) {
        return argb & 0xFF;
    }

    /** Packs four channels into one pixel. Values outside 0 to 255 are clamped. */
    public static int toArgb(int alpha, int red, int green, int blue) {
        return (clamp(alpha) << 24) | (clamp(red) << 16) | (clamp(green) << 8) | clamp(blue);
    }

    /** Returns one pixel as text, for example {@code "a=255 r=70 g=130 b=200"}. */
    public static String describe(int argb) {
        return "a=" + alpha(argb) + " r=" + red(argb) + " g=" + green(argb) + " b=" + blue(argb);
    }

    /** Limits a channel value to 0 to 255. */
    public static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
