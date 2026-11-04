package t24_pathfinding.exercises.ex01;

import t24_pathfinding.common.Grid;
import t24_pathfinding.common.Maps;

public class Exercise {

    public static void run() {
        System.out.println(MapReport.of(Maps.marsh()));

        // A ragged map: the second row is one tile short
        try {
            Grid.parse("#####", "#S.G", "#####");
        } catch (IllegalArgumentException e) {
            System.out.println("Broken map rejected: " + e.getMessage());
        }
    }
}
