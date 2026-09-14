package t24_pathfinding.common;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A* search: expands whichever frontier cell looks most promising.
 *
 * <p>"Most promising" means the smallest {@code f = g + h}, where {@code g} is the known
 * cost of reaching the cell and {@code h} is the {@link Heuristic}'s estimate of what
 * remains. The frontier is a {@link PriorityQueue} ordered by {@code f} — the single
 * structural difference from breadth-first search, which uses a plain FIFO queue.
 *
 * <p><strong>Why it is correct.</strong> With an admissible heuristic — one that never
 * over-estimates — the first time A* expands the goal, no cheaper route to it can exist,
 * because any such route would have been sitting in the frontier with a smaller {@code f}.
 * Feed it an over-estimating heuristic and that argument collapses; see
 * {@link Heuristic#scaledManhattan(int)}.
 *
 * <p>With {@link Heuristic#zero()} this class <em>is</em> Dijkstra's algorithm, which is
 * why {@link DijkstraPathfinder} delegates here rather than repeating the code.
 */
public final class AStarPathfinder implements Pathfinder {

    private final Heuristic heuristic;
    private final String name;

    public AStarPathfinder(Heuristic heuristic) {
        this(heuristic, "A*");
    }

    public AStarPathfinder(Heuristic heuristic, String name) {
        if (heuristic == null) {
            throw new IllegalArgumentException("heuristic is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        this.heuristic = heuristic;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SearchResult findPath(Grid grid, SearchListener listener) {
        Cell start = grid.start();
        Cell goal = grid.goal();

        Map<Cell, Integer> costSoFar = new HashMap<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Set<Cell> visited = new HashSet<>();
        costSoFar.put(start, 0);

        // Ties are broken by position so that a given map always produces the same
        // expansion order. Without this, two correct runs could disagree and the
        // expansion-order tests would flicker.
        Comparator<Cell> byEstimatedTotal = Comparator
                .<Cell>comparingInt(c -> costSoFar.get(c) + heuristic.estimate(c, goal))
                .thenComparingInt(c -> heuristic.estimate(c, goal))
                .thenComparingInt(Cell::row)
                .thenComparingInt(Cell::col);

        PriorityQueue<Cell> frontier = new PriorityQueue<>(byEstimatedTotal);
        frontier.add(start);
        int expanded = 0;

        while (!frontier.isEmpty()) {
            Cell current = frontier.poll();

            // A cheaper route may have been queued after this entry; the stale copy is
            // simply skipped. Cheaper than removing from the middle of a heap.
            if (!visited.add(current)) {
                continue;
            }
            expanded++;
            listener.onExpand(current, new LinkedHashSet<>(frontier), Set.copyOf(visited));

            if (current.equals(goal)) {
                List<Cell> path = Pathfinder.rebuildPath(cameFrom, start, goal);
                listener.onFound(path);
                return new SearchResult(path, costSoFar.get(goal), expanded);
            }

            for (Cell next : grid.neighbours(current)) {
                if (visited.contains(next)) {
                    continue;
                }
                int newCost = costSoFar.get(current) + grid.costToEnter(next);
                if (newCost < costSoFar.getOrDefault(next, Integer.MAX_VALUE)) {
                    costSoFar.put(next, newCost);
                    cameFrom.put(next, current);
                    frontier.add(next);
                }
            }
        }

        listener.onExhausted();
        return SearchResult.notFound(expanded);
    }
}
