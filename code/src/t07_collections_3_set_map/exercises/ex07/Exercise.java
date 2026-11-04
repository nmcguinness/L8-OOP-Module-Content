package t07_collections_3_set_map.exercises.ex07;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Exercise {

    // Number of distinct cells a unit has visited
    public static int countDistinct(List<GridPos> visited) {
        if (visited == null)
            throw new IllegalArgumentException("visited must not be null");
        Set<GridPos> unique = new HashSet<>(visited);
        return unique.size();
    }

    public static void run() {
        List<GridPos> path = List.of(
                new GridPos(0, 0), new GridPos(0, 1), new GridPos(0, 0), new GridPos(1, 1));
        System.out.println("Distinct cells: " + countDistinct(path));
    }
}
