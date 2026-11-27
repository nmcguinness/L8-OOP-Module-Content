package t09_interface.demos.de01;

import java.util.List;

/**
 * Runs a list of image filters over an image. It knows only the {@link IImageFilter}
 * interface, so it works with any filter class, including ones written later.
 */
public final class ImageProcessor {

    private ImageProcessor() {
    }

    /** Runs every filter in the list, in order. Each filter gets the previous result. */
    public static int[][] applyAll(List<IImageFilter> filters, int[][] pixels) {
        int[][] result = pixels;
        for (IImageFilter filter : filters) {
            result = filter.filter(result);
        }
        return result;
    }
}
