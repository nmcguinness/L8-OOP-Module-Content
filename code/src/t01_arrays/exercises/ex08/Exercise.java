package t01_arrays.exercises.ex08;

public class Exercise {

    /**
     * Normalizes heat values (0..255) into [0.0 .. 1.0] by dividing by the
     * maximum value found in the entire grid. Returns a NEW double[][].
     * If all values are 0 (or grid is empty), returns all 0.0 to avoid /0.
     */
    static double[][] normalize(int[][] heat) {
        if (heat == null) return null;
        int rows = heat.length;
        int max = 0;
        for (int r = 0; r < rows; r++) {
            if (heat[r] == null) continue;
            for (int c = 0; c < heat[r].length; c++)
                if (heat[r][c] > max) max = heat[r][c];
        }
        double[][] out = new double[rows][];
        if (max == 0) { // degenerate case, avoid division by zero
            for (int r = 0; r < rows; r++)
                out[r] = (heat[r] == null) ? null : new double[heat[r].length];
            return out;
        }
        for (int r = 0; r < rows; r++) {
            if (heat[r] == null) { out[r] = null; continue; }
            out[r] = new double[heat[r].length];
            for (int c = 0; c < heat[r].length; c++)
                out[r][c] = heat[r][c] / (double) max;
        }
        return out;
    }

    public static void run() {
        int[][] h = {{0, 64, 128}, {255, 128, 0}};
        double[][] n = normalize(h);
        for (double[] row : n) {
            for (double v : row) System.out.printf("%.2f ", v);
            System.out.println();
        }
    }
}
