package t09_interface.demos.de01;

/**
 * One image filter. A filter takes a 2D array of pixels and returns a new 2D array.
 *
 * <p>Each pixel is one {@code int} in ARGB form, indexed {@code pixels[row][col]}.
 * Use {@link ImageHelper} to read the four channels out of a pixel and to pack them back in.
 * A filter must not change the array it is given.
 */
public interface IImageFilter {

    /** Returns a new filtered image. The input array is not changed. */
    int[][] filter(int[][] pixels);

    /** A short name for this filter, used in output file names. */
    default String name() {
        return getClass().getSimpleName();
    }
}
