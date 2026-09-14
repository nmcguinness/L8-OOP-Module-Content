package t24_pathfinding.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Finds a route from a grid's start to its goal.
 *
 * <p>Three implementations, one interface — so a caller can swap the algorithm without
 * changing a line. That is Strategy (t13) applied to something with real consequences:
 * the choice changes how much work is done and, on rough ground, whether the answer is
 * even correct.
 */
public interface Pathfinder {

    /** A short name for reports and tables, e.g. {@code "A* (Manhattan)"}. */
    String name();

    SearchResult findPath(Grid grid, SearchListener listener);

    /** Runs the search with nobody watching. */
    default SearchResult findPath(Grid grid) {
        return findPath(grid, SearchListener.NONE);
    }

    /**
     * Walks the {@code cameFrom} chain backwards from the goal and reverses it.
     *
     * <p>Shared by all three implementations: every one of them records where it first
     * arrived at each cell, and the path is recovered the same way afterwards.
     */
    static List<Cell> rebuildPath(Map<Cell, Cell> cameFrom, Cell start, Cell goal) {
        List<Cell> path = new ArrayList<>();
        Cell at = goal;
        while (at != null) {
            path.add(at);
            if (at.equals(start)) {
                break;
            }
            at = cameFrom.get(at);
        }
        if (path.isEmpty() || !path.get(path.size() - 1).equals(start)) {
            return List.of();
        }
        Collections.reverse(path);
        return path;
    }

    /** Total cost of entering every cell after the first. */
    static int costOf(Grid grid, List<Cell> path) {
        int total = 0;
        for (int i = 1; i < path.size(); i++) {
            total += grid.costToEnter(path.get(i));
        }
        return total;
    }
}
