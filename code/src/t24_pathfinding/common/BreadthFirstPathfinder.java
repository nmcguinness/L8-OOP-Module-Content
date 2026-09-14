package t24_pathfinding.common;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Breadth-first search: explores in rings of equal <em>step count</em> from the start.
 *
 * <p>It uses a plain FIFO {@link Queue}, which is exactly why it behaves as it does —
 * every cell one step away is examined before any cell two steps away.
 *
 * <p><strong>The limitation worth learning.</strong> BFS counts steps, not cost. On a map
 * of open ground those are the same thing and BFS returns the cheapest path. Put rough
 * ground ({@code ~}, costing 5) on the map and they part company: BFS still returns the
 * path with the fewest moves, which may be far more expensive than a longer detour around
 * the mud. Dijkstra exists to fix precisely this.
 */
public final class BreadthFirstPathfinder implements Pathfinder {

    @Override
    public String name() {
        return "Breadth-first";
    }

    @Override
    public SearchResult findPath(Grid grid, SearchListener listener) {
        Cell start = grid.start();
        Cell goal = grid.goal();

        Queue<Cell> frontier = new ArrayDeque<>();
        Set<Cell> queued = new LinkedHashSet<>();
        Set<Cell> visited = new HashSet<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();

        frontier.add(start);
        queued.add(start);
        int expanded = 0;

        while (!frontier.isEmpty()) {
            Cell current = frontier.poll();
            queued.remove(current);
            visited.add(current);
            expanded++;
            listener.onExpand(current, new LinkedHashSet<>(queued), Set.copyOf(visited));

            if (current.equals(goal)) {
                List<Cell> path = Pathfinder.rebuildPath(cameFrom, start, goal);
                listener.onFound(path);
                return new SearchResult(path, Pathfinder.costOf(grid, path), expanded);
            }

            for (Cell next : grid.neighbours(current)) {
                // "Seen" is enough for BFS: the first arrival is always via fewest steps,
                // so a later arrival can never be an improvement.
                if (visited.contains(next) || queued.contains(next)) {
                    continue;
                }
                cameFrom.put(next, current);
                frontier.add(next);
                queued.add(next);
            }
        }

        listener.onExhausted();
        return SearchResult.notFound(expanded);
    }
}
