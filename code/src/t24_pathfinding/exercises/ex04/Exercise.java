package t24_pathfinding.exercises.ex04;

import t24_pathfinding.common.BreadthFirstPathfinder;
import t24_pathfinding.common.DijkstraPathfinder;
import t24_pathfinding.common.Grid;
import t24_pathfinding.common.Maps;
import t24_pathfinding.common.Pathfinder;
import t24_pathfinding.common.SearchResult;

import java.util.List;

public class Exercise {

    // One line per algorithm: steps, cost and cells expanded
    static String compare(Grid grid, List<Pathfinder> finders) {
        StringBuilder out = new StringBuilder();
        for (Pathfinder finder : finders) {
            SearchResult r = finder.findPath(grid);
            out.append(String.format("%-10s steps=%2d cost=%2d expanded=%3d%n",
                    finder.name(), r.steps(), r.cost(), r.expanded()));
        }
        return out.toString();
    }

    public static void run() {
        System.out.print(compare(Maps.marsh(),
                List.of(new BreadthFirstPathfinder(), new DijkstraPathfinder())));
    }
}
