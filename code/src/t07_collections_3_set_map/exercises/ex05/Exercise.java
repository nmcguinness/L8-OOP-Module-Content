package t07_collections_3_set_map.exercises.ex05;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Exercise {

    // Elements present in both lists, without duplicates; the inputs are not changed
    public static Set<Integer> intersection(List<Integer> a, List<Integer> b) {
        if (a == null || b == null)
            throw new IllegalArgumentException("lists must not be null");

        Set<Integer> result = new HashSet<>(a);
        result.retainAll(new HashSet<>(b));
        return result;
    }

    public static void run() {
        System.out.println(intersection(List.of(1, 2, 2, 3, 4), List.of(2, 4, 4, 6)));
    }
}
