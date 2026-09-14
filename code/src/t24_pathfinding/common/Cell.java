package t24_pathfinding.common;

/**
 * One square on the grid, addressed by row then column.
 *
 * <p>This is a record because a cell is nothing but its coordinates: two cells with the
 * same row and column are the same cell. The generated {@code equals} and {@code hashCode}
 * are what let a {@code HashSet<Cell>} and {@code HashMap<Cell, ...>} work at all, and the
 * search algorithms lean on both constantly.
 */
public record Cell(int row, int col) {

    /** The cell one step in the given direction. */
    public Cell step(Direction direction) {
        return new Cell(row + direction.rowDelta(), col + direction.colDelta());
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
