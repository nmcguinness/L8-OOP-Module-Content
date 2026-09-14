package t24_pathfinding.common;

/**
 * Dijkstra's algorithm: always expand the cheapest-so-far cell.
 *
 * <p>Unlike breadth-first search it orders the frontier by accumulated <em>cost</em>
 * rather than step count, so rough ground is priced correctly and the path it returns is
 * genuinely the cheapest.
 *
 * <p><strong>It is A* with no heuristic.</strong> Setting {@code h = 0} everywhere reduces
 * {@code f = g + h} to {@code f = g}, which is the definition of Dijkstra. Rather than
 * copy ninety lines to prove the point, this class delegates — the relationship between
 * the two algorithms is the lesson, and duplicating the code would hide it.
 */
public final class DijkstraPathfinder implements Pathfinder {

    private final Pathfinder delegate = new AStarPathfinder(Heuristic.zero(), "Dijkstra");

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public SearchResult findPath(Grid grid, SearchListener listener) {
        return delegate.findPath(grid, listener);
    }
}
