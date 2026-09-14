package t24_pathfinding.common;

/**
 * An estimate of the remaining cost from one cell to another.
 *
 * <p>A heuristic is <strong>admissible</strong> if it never over-estimates. That single
 * property is what makes A* safe: an admissible heuristic guarantees the path it returns
 * is still the cheapest one. Over-estimate, and A* becomes fast but wrong.
 *
 * <p>This is a functional interface, so a heuristic can be a lambda, a method reference,
 * or a named class — the search does not care which. Swapping it is the whole difference
 * between Dijkstra and A*.
 */
@FunctionalInterface
public interface Heuristic {

    int estimate(Cell from, Cell to);

    /**
     * Always zero — the honest "I have no idea" estimate.
     *
     * <p>It is trivially admissible, and A* with this heuristic <em>is</em> Dijkstra's
     * algorithm. That is not a coincidence worth glossing over: one is a special case
     * of the other.
     */
    static Heuristic zero() {
        return (from, to) -> 0;
    }

    /**
     * Steps needed ignoring walls and rough ground.
     *
     * <p>Admissible here because the cheapest possible move costs 1, so the true remaining
     * cost is always at least the number of remaining steps.
     */
    static Heuristic manhattan() {
        return (from, to) -> Math.abs(from.row() - to.row()) + Math.abs(from.col() - to.col());
    }

    /**
     * Manhattan distance multiplied by a factor.
     *
     * <p>With {@code factor > 1} this over-estimates and is <strong>not</strong> admissible.
     * Exercise 07 uses it to show A* returning a path that is fast to find and not the
     * cheapest — a bug you cannot see without measuring the cost.
     */
    static Heuristic scaledManhattan(int factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("factor must be >= 0");
        }
        return (from, to) -> factor * manhattan().estimate(from, to);
    }
}
