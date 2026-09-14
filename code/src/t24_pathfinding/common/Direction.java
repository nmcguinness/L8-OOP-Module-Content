package t24_pathfinding.common;

/**
 * The four moves a searcher may make.
 *
 * <p>Diagonals are deliberately absent. Allowing them changes which heuristics stay
 * admissible — Manhattan distance would start over-estimating, and A* would no longer be
 * guaranteed to find the cheapest path. See Exercise 08.
 */
public enum Direction {

    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowDelta;
    private final int colDelta;

    Direction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    public int rowDelta() {
        return rowDelta;
    }

    public int colDelta() {
        return colDelta;
    }
}
