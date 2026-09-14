package t24_pathfinding.demos.de01;

import t24_pathfinding.common.AStarPathfinder;
import t24_pathfinding.common.ConsoleRenderer;
import t24_pathfinding.common.Grid;
import t24_pathfinding.common.Heuristic;
import t24_pathfinding.common.Maps;
import t24_pathfinding.common.Pathfinder;
import t24_pathfinding.common.SearchResult;

/**
 * Watch A* work.
 *
 * <p>The search does not know it is being watched. It reports each expansion to a
 * {@code SearchListener}, and here that listener happens to draw. Swap in a
 * {@code CountingListener} and the identical search produces numbers instead of pictures.
 */
public class Demo {

    public static void run() {
        Grid grid = Maps.openRoom();

        // One frame every 20 expansions keeps the scrollback readable. Set it to 1 and
        // pauseMillis to about 40 for a smooth animation in a real terminal.
        ConsoleRenderer renderer = new ConsoleRenderer(grid, ConsoleRenderer.Mode.PLAIN, 20, 0);

        Pathfinder aStar = new AStarPathfinder(Heuristic.manhattan(), "A* (Manhattan)");
        SearchResult result = aStar.findPath(grid, renderer);

        System.out.println("Legend: S start  G goal  # wall  ~ rough(5)  "
                + "o expanded  ? frontier  @ current  + path");
        System.out.println(aStar.name() + " expanded " + result.expanded()
                + " cells for a path of cost " + result.cost() + ".");
    }

    public static void main(String[] args) {
        run();
    }
}
