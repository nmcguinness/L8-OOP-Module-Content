package t01_arrays.exercises.ex11;

public class Exercise {

    static int[] parseSteps(String csv) {
        if (csv == null || csv.isBlank()) return new int[0];
        String[] parts = csv.split(",");
        int[] out = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].trim();
            out[i] = p.isEmpty() ? 0 : Integer.parseInt(p);
        }
        return out;
    }

    /**
     * Summarizes: min, max, average (double), and count of zeros.
     * Uses simple array loops (no collections/streams).
     */
    static String summarize(int[] steps) {
        if (steps == null || steps.length == 0)
            return "min=0 max=0 avg=0.0 zeros=0";

        int min = steps[0], max = steps[0], sum = 0, zeros = 0;
        for (int x : steps) {
            if (x < min) min = x;
            if (x > max) max = x;
            if (x == 0) zeros++;
            sum += x;
        }
        double avg = (double) sum / steps.length;
        return String.format("min=%d max=%d avg=%.2f zeros=%d", min, max, avg, zeros);
    }

    public static void run() {
        int[] steps = parseSteps("12034, 9876, 0, 4321, 4321");
        System.out.println(summarize(steps)); // e.g., min=0 max=12034 avg=... zeros=1
    }
}
