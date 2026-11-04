package t24_pathfinding.exercises.ex07;

import t24_pathfinding.common.AStarPathfinder;
import t24_pathfinding.common.Grid;
import t24_pathfinding.common.Heuristic;
import t24_pathfinding.common.Maps;
import t24_pathfinding.common.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public record Run(int factor, int cost, int expanded) { }

    // A* with the Manhattan estimate multiplied by each factor
    public static List<Run> scaledRuns(Grid grid, int... factors) {
        List<Run> runs = new ArrayList<>();
        for (int factor : factors) {
            SearchResult r = new AStarPathfinder(Heuristic.scaledManhattan(factor)).findPath(grid);
            runs.add(new Run(factor, r.cost(), r.expanded()));
        }
        return runs;
    }

    public static void run() {
        System.out.println("factor  cost  expanded");
        for (Run r : scaledRuns(Maps.marsh(), 1, 2, 3, 5, 10)) {
            System.out.printf("%6d  %4d  %8d%n", r.factor(), r.cost(), r.expanded());
        }
    }
}
