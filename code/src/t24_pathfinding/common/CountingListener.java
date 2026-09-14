package t24_pathfinding.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Records a search instead of drawing it.
 *
 * <p>This is the listener the tests attach. Because the algorithms report through
 * {@link SearchListener} rather than printing, a test can assert on the exact order cells
 * were expanded without capturing {@code System.out} — which is both fragile and slow.
 */
public final class CountingListener implements SearchListener {

    private final List<Cell> expansionOrder = new ArrayList<>();
    private List<Cell> path = List.of();
    private boolean exhausted;

    @Override
    public void onExpand(Cell current, Set<Cell> frontier, Set<Cell> visited) {
        expansionOrder.add(current);
    }

    @Override
    public void onFound(List<Cell> path) {
        this.path = List.copyOf(path);
    }

    @Override
    public void onExhausted() {
        exhausted = true;
    }

    /** Every cell expanded, in the order it was expanded. */
    public List<Cell> expansionOrder() {
        return List.copyOf(expansionOrder);
    }

    public int expansions() {
        return expansionOrder.size();
    }

    public List<Cell> path() {
        return path;
    }

    /** True if the search ran out of frontier without reaching the goal. */
    public boolean exhausted() {
        return exhausted;
    }
}
