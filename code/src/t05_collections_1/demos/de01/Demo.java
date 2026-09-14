package t05_collections_1.demos.de01;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Demo class for profiling different "prettify" implementations
 * using System.nanoTime on an ArrayList<String> of names.
 * <p>
 * The goal is to show students how to:
 * <ul>
 *   <li>Measure elapsed time with nanoTime</li>
 *   <li>Compare different loop styles fairly</li>
 *   <li>Avoid common benchmarking mistakes (e.g., mutating shared data)</li>
 * </ul>
 */
public class Demo {

    /**
     * Public entry point used by the rest of the application.
     * <p>
     * We keep this method very small so that students can see that
     * the real work happens in start(), which is easier to step through.
     */
    public static void run() {
        new Demo().start();
    }

    /**
     * Loads the master list of names, runs multiple timing trials
     * for each version of the prettify method, and prints the
     * average time in milliseconds.
     * <p>
     * This method is mainly about demonstrating:
     * <ul>
     *   <li>How to set up a simple timing experiment</li>
     *   <li>Why we create fresh copies of the data per trial</li>
     *   <li>How to calculate average timings over many runs</li>
     * </ul>
     */
    private void start() {
        // Load the original list of names once.
        ArrayList<String> master = FileUtils.readDelimitedFile(
                "first_names_10001.csv",
                ',');

        if (master == null || master.isEmpty()) {
            System.out.println("No names loaded.");
            return;
        }

        int trials = 500;
        double ver1TotalMs = 0.0;
        double ver2TotalMs = 0.0;
        double ver3TotalMs = 0.0;

        // IMPORTANT: each version gets a fresh copy of the same data
        // so that the comparison is fair.
        ArrayList<String> list1 = new ArrayList<>(master);
        ArrayList<String> list2 = new ArrayList<>(master);
        ArrayList<String> list3 = new ArrayList<>(master);

        for (int i = 0; i < trials; i++) {
            ver1TotalMs += timeVer1(list1);
            ver2TotalMs += timeVer2(list2);
            ver3TotalMs += timeVer3(list3);

            if (i % 50 == 0) {
                System.out.println("Trial " + i + " completed...");
            }
        }

        System.out.printf("ver1 avg time (ms): %.2f%n", ver1TotalMs);
        System.out.printf("ver2 avg time (ms): %.2f%n", ver2TotalMs);
        System.out.printf("ver3 avg time (ms): %.2f%n", ver3TotalMs);
    }

    /**
     * Times the first prettify implementation using System.nanoTime.
     * <p>
     * This version uses an indexed for-loop. We return the duration
     * in milliseconds as a double so that sub-millisecond differences
     * are visible to students.
     *
     * @param list input list of raw names (copied from master)
     * @return elapsed time in milliseconds
     */
    private double timeVer1(ArrayList<String> list) {
        long startNanos = System.nanoTime();
        ArrayList<String> result = prettifyList_ver1(list);
        long durationNanos = System.nanoTime() - startNanos;

        // Tiny use of result to discourage the JIT from treating
        // the entire method as "dead code" in a more artificial example.
        if (!result.isEmpty() && result.get(0).length() == 0) {
            System.out.println("This is unlikely, but it keeps the compiler honest.");
        }

        return durationNanos / 1_000_000.0;    //5/9 = 0
    }

    /**
     * Times the second prettify implementation using System.nanoTime.
     * <p>
     * This version uses an enhanced for-loop. Students can compare
     * its performance against ver1 to see if the loop style makes
     * any noticeable difference for this workload.
     *
     * @param list input list of raw names (copied from master)
     * @return elapsed time in milliseconds
     */
    private double timeVer2(ArrayList<String> list) {
        long startNanos = System.nanoTime();
        ArrayList<String> result = prettifyList_ver2(list);
        long durationNanos = System.nanoTime() - startNanos;

        return durationNanos / 1_000_000.0;
    }

    /**
     * Times the third prettify implementation using System.nanoTime.
     * <p>
     * This version uses a ListIterator to update the list in place.
     * It is useful for discussions about when in-place updates might
     * be preferred over allocating a new list.
     *
     * @param list input list of raw names (copied from master)
     * @return elapsed time in milliseconds
     */
    private double timeVer3(ArrayList<String> list) {
        long startNanos = System.nanoTime();
        ArrayList<String> result = prettifyList_ver3(list);
        long durationNanos = System.nanoTime() - startNanos;

        return durationNanos / 1_000_000.0;
    }

    /**
     * Prettifies a list of names using an indexed for-loop.
     * <p>
     * For each name, we:
     * <ol>
     *   <li>Trim whitespace</li>
     *   <li>Uppercase the first character</li>
     *   <li>Lowercase the remaining characters</li>
     * </ol>
     * The method returns a new ArrayList and does not modify the input list.
     *
     * @param list input list of raw names
     * @return a new list containing prettified names
     */
    public ArrayList<String> prettifyList_ver1(ArrayList<String> list) {
        ArrayList<String> result = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            result.add(prettify(list.get(i)));
        }
        return result;
    }

    /**
     * Prettifies a list of names using an enhanced for-loop.
     * <p>
     * This has the same behaviour as ver1, but uses the enhanced
     * for-loop syntax. It is a good example for students when
     * discussing readability vs. low-level control.
     *
     * @param list input list of raw names
     * @return a new list containing prettified names
     */
    public ArrayList<String> prettifyList_ver2(ArrayList<String> list) {
        ArrayList<String> result = new ArrayList<>(list.size());
        for (String name : list) {   //read-only
            result.add(prettify(name));
        }
        return result;
    }

    /**
     * Prettifies a list of names using a ListIterator and in-place updates.
     * <p>
     * Instead of creating a new list, this method walks through the
     * existing list and replaces each element with its prettified version.
     * It then returns the same list reference for convenience.
     * <p>
     * This version is helpful for introducing:
     * <ul>
     *   <li>ListIterator and its set() method</li>
     *   <li>In-place modification vs. creating new collections</li>
     * </ul>
     *
     * @param list input list of raw names
     * @return the same list instance, after being updated in place
     */
    public ArrayList<String> prettifyList_ver3(ArrayList<String> list) {
        ListIterator<String> it = list.listIterator();
        while (it.hasNext()) {
            String raw = it.next();
            it.set(prettify(raw));  //get + set in original list => mutating
        }
        return list;
    }

    /**
     * Converts a single raw name into a "pretty" version.
     * <p>
     * Rules:
     * <ul>
     *   <li>null becomes an empty string</li>
     *   <li>leading and trailing spaces are removed</li>
     *   <li>one-character names are uppercased</li>
     *   <li>longer names have first letter uppercased and the rest lowercased</li>
     * </ul>
     * This small helper keeps the formatting logic in one place so that the
     * three list-based methods are easy to compare.
     *
     * @param raw the raw name string (may be null or contain extra spaces)
     * @return the prettified name, or an empty string for null/empty input
     */
    private String prettify(String raw) {
        if (raw == null) {
            return "";
        }

        String s = raw.trim();
        if (s.isEmpty()) {
            return "";
        }

        if (s.length() == 1) {
            return s.toUpperCase();
        }

        return s.substring(0, 1).toUpperCase()
                + s.substring(1).toLowerCase();
    }
}
