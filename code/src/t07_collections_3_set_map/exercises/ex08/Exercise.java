package t07_collections_3_set_map.exercises.ex08;

import java.util.NavigableMap;
import java.util.TreeMap;

public class Exercise {

    // Each key is the lowest score that earns that band
    private static final NavigableMap<Integer, String> BANDS = new TreeMap<>();

    static {
        BANDS.put(0, "F");
        BANDS.put(40, "D");
        BANDS.put(55, "C");
        BANDS.put(70, "B");
        BANDS.put(85, "A");
    }

    public static String band(int score) {
        if (score < 0 || score > 100)
            throw new IllegalArgumentException("score must be 0 to 100: " + score);

        // floorEntry: the entry with the greatest key less than or equal to score
        return BANDS.floorEntry(score).getValue();
    }

    public static void run() {
        int[] scores = {0, 39, 40, 69, 70, 84, 85, 100};
        for (int s : scores) {
            System.out.println(s + " -> " + band(s));
        }
    }
}
