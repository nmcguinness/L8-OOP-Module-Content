package t24_pathfinding.demos.de02;

import t24_pathfinding.common.AStarPathfinder;
import t24_pathfinding.common.BreadthFirstPathfinder;
import t24_pathfinding.common.DijkstraPathfinder;
import t24_pathfinding.common.Grid;
import t24_pathfinding.common.Heuristic;
import t24_pathfinding.common.Maps;
import t24_pathfinding.common.Pathfinder;
import t24_pathfinding.common.SearchResult;

import java.util.List;

/**
 * The same three algorithms across four maps.
 *
 * <p>Read the table by column. The <em>cost</em> column says whether the answer is right;
 * the <em>expanded</em> column says how much work it took to get there. Those are separate
 * questions, and confusing them is the most common mistake when comparing algorithms.
 */
public class Demo {

    public static void run() {
        List<Pathfinder> finders = List.of(
                new BreadthFirstPathfinder(),
                new DijkstraPathfinder(),
                new AStarPathfinder(Heuristic.manhattan(), "A* (Manhattan)"));

        report("Open room", Maps.openRoom(), finders);
        report("Corridor maze", Maps.corridorMaze(), finders);
        report("Marsh (rough ground)", Maps.marsh(), finders);
        report("Sealed goal", Maps.sealedGoal(), finders);

        System.out.println("Note where the cost column disagrees. On the marsh, breadth-first");
        System.out.println("finds the path with the fewest steps, not the cheapest one.");
    }

    private static void report(String title, Grid grid, List<Pathfinder> finders) {
        System.out.println();
        System.out.println("== " + title + " ==");
        System.out.printf("%-16s %8s %8s %10s%n", "algorithm", "steps", "cost", "expanded");

        for (Pathfinder finder : finders) {
            SearchResult result = finder.findPath(grid);
            if (result.found()) {
                System.out.printf("%-16s %8d %8d %10d%n",
                        finder.name(), result.steps(), result.cost(), result.expanded());
            } else {
                System.out.printf("%-16s %8s %8s %10d%n",
                        finder.name(), "-", "no path", result.expanded());
            }
        }
    }

    public static void main(String[] args) {
        run();
    }
}
