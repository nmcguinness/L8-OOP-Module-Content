package t01_arrays.exercises.ex04;

public class Exercise {

    /**
     * Builds an 11-bin histogram for scores in [0,100].
     * Bins: 0-9, 10-19, ..., 90-99, 100.
     */
    static int[] histogram(int[] scores, int binCount) {
        if(scores == null )
            throw new NullPointerException("scores cannot be null!");

        if(scores.length == 0)
            throw new IllegalArgumentException("score length is zero, please populate!");

        if(binCount == 0)
            throw new ArithmeticException("binCount cannot be zero!");

        int[] bins = new int[binCount + 1];
        int binSize = 100 / binCount;
        if (scores == null)
            return bins;
        for (int s : scores) {
            if (s < 0 || s > 100)
                continue;  // ignore out-of-range safely
            int bin = (s == 100) ? binCount : (s / binSize);
            bins[bin]++;
        }
        return bins;
    }

    static void printHistogram(int[] bins) {
        for (int i = 0; i < bins.length; i++) {
            String label = (i == 10) ? "100  " : String.format("%02d-%02d", i * 10, i * 10 + 9);
            System.out.print(label + ": ");
            for (int k = 0; k < bins[i]; k++) System.out.print("*");
            System.out.println();
        }
    }

    public static void run() {
        int[] scores = {66, 7, 12, 15, 19, 33};

        try {
            int[] bins = histogram(scores, 0);
            printHistogram(bins);
        }
        catch(ArithmeticException e)
        {
            System.out.println("Arithmetic: " + e.getMessage());
        }
        finally
        {
            System.out.println("Something bad happened!");
        }
    }
}
