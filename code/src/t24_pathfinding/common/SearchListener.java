package t24_pathfinding.common;

import java.util.List;
import java.util.Set;

/**
 * Reports what a search is doing, one step at a time.
 *
 * <p>This interface exists so that no algorithm in this topic ever calls
 * {@code System.out.println}. The search reports; something else decides what that means.
 * A {@link ConsoleRenderer} draws it, a {@link CountingListener} tallies it, and a test
 * asserts on it — none of which requires changing the search.
 *
 * <p>This is the Observer pattern from t14, put to work. If you find yourself wanting to
 * print from inside an algorithm, you want a listener instead.
 */
public interface SearchListener {

    /** A listener that ignores everything. The default when nobody is watching. */
    SearchListener NONE = new SearchListener() { };

    /**
     * Called each time a cell is taken off the frontier and examined.
     *
     * @param current  the cell being expanded now
     * @param frontier cells queued but not yet examined
     * @param visited  cells already expanded
     */
    default void onExpand(Cell current, Set<Cell> frontier, Set<Cell> visited) {
    }

    /** Called once, when the goal has been reached and the path rebuilt. */
    default void onFound(List<Cell> path) {
    }

    /** Called once, when the frontier empties without reaching the goal. */
    default void onExhausted() {
    }
}
