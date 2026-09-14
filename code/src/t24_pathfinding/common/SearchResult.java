package t24_pathfinding.common;

import java.util.List;

/**
 * What a search found, and how hard it worked to find it.
 *
 * <p>Keeping {@code expanded} alongside the path is what makes the three algorithms
 * comparable. All three return the same path on an open map; they differ in how many
 * cells they had to examine to get there, and that number is the entire point of A*.
 *
 * @param path     start to goal inclusive, or empty if the goal is unreachable
 * @param cost     total cost of entering every cell after the start
 * @param expanded how many cells were taken off the frontier and examined
 */
public record SearchResult(List<Cell> path, int cost, int expanded) {

    public SearchResult {
        path = List.copyOf(path);
    }

    /** An unreachable goal is a result, not an error. */
    public static SearchResult notFound(int expanded) {
        return new SearchResult(List.of(), 0, expanded);
    }

    public boolean found() {
        return !path.isEmpty();
    }

    /** Number of moves. A path of one cell is the start already being the goal: zero moves. */
    public int steps() {
        return path.isEmpty() ? 0 : path.size() - 1;
    }
}
