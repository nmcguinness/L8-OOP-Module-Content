package t24_pathfinding.common;

import java.util.ArrayList;
import java.util.List;

/**
 * A rectangular map of terrain, parsed from text.
 *
 * <pre>
 *   #   wall - impassable
 *   .   open ground - costs 1 to enter
 *   ~   rough ground - costs 5 to enter
 *   S   the start (open ground)
 *   G   the goal  (open ground)
 * </pre>
 *
 * <p>Entry cost is what separates the three algorithms in this topic. On a map of nothing
 * but {@code .}, breadth-first search finds the cheapest path. Add a single {@code ~} and
 * it stops doing so, because it counts steps rather than cost.
 */
public final class Grid {

    public static final char WALL = '#';
    public static final char OPEN = '.';
    public static final char ROUGH = '~';
    public static final char START = 'S';
    public static final char GOAL = 'G';

    private static final int OPEN_COST = 1;
    private static final int ROUGH_COST = 5;

    private final char[][] tiles;
    private final Cell start;
    private final Cell goal;

    private Grid(char[][] tiles, Cell start, Cell goal) {
        this.tiles = tiles;
        this.start = start;
        this.goal = goal;
    }

    /**
     * Builds a grid from one string per row.
     *
     * @throws IllegalArgumentException if the rows are empty, ragged, or do not contain
     *                                  exactly one start and one goal
     */
    public static Grid parse(String... rows) {
        if (rows == null || rows.length == 0) {
            throw new IllegalArgumentException("a grid needs at least one row");
        }

        int width = rows[0].length();
        char[][] tiles = new char[rows.length][];
        Cell start = null;
        Cell goal = null;

        for (int r = 0; r < rows.length; r++) {
            if (rows[r].length() != width) {
                throw new IllegalArgumentException(
                        "row " + r + " has length " + rows[r].length() + ", expected " + width);
            }
            tiles[r] = rows[r].toCharArray();

            for (int c = 0; c < width; c++) {
                char tile = tiles[r][c];
                if (tile == START) {
                    if (start != null) {
                        throw new IllegalArgumentException("more than one start");
                    }
                    start = new Cell(r, c);
                } else if (tile == GOAL) {
                    if (goal != null) {
                        throw new IllegalArgumentException("more than one goal");
                    }
                    goal = new Cell(r, c);
                } else if (tile != WALL && tile != OPEN && tile != ROUGH) {
                    throw new IllegalArgumentException(
                            "unknown tile '" + tile + "' at row " + r + ", column " + c);
                }
            }
        }

        if (start == null) {
            throw new IllegalArgumentException("no start cell marked 'S'");
        }
        if (goal == null) {
            throw new IllegalArgumentException("no goal cell marked 'G'");
        }
        return new Grid(tiles, start, goal);
    }

    public int rows() {
        return tiles.length;
    }

    public int cols() {
        return tiles[0].length;
    }

    public Cell start() {
        return start;
    }

    public Cell goal() {
        return goal;
    }

    public boolean inBounds(Cell cell) {
        return cell.row() >= 0 && cell.row() < rows()
                && cell.col() >= 0 && cell.col() < cols();
    }

    public char tileAt(Cell cell) {
        requireInBounds(cell);
        return tiles[cell.row()][cell.col()];
    }

    public boolean isWall(Cell cell) {
        return inBounds(cell) && tileAt(cell) == WALL;
    }

    public boolean isPassable(Cell cell) {
        return inBounds(cell) && tileAt(cell) != WALL;
    }

    /**
     * What it costs to step <em>into</em> this cell.
     *
     * @throws IllegalArgumentException if the cell is a wall - callers must filter first
     */
    public int costToEnter(Cell cell) {
        requireInBounds(cell);
        char tile = tileAt(cell);
        if (tile == WALL) {
            throw new IllegalArgumentException("a wall cannot be entered: " + cell);
        }
        return tile == ROUGH ? ROUGH_COST : OPEN_COST;
    }

    /** The passable neighbours of a cell, in a fixed order so searches are reproducible. */
    public List<Cell> neighbours(Cell cell) {
        requireInBounds(cell);
        List<Cell> found = new ArrayList<>(4);
        for (Direction direction : Direction.values()) {
            Cell next = cell.step(direction);
            if (isPassable(next)) {
                found.add(next);
            }
        }
        return found;
    }

    private void requireInBounds(Cell cell) {
        if (!inBounds(cell)) {
            throw new IllegalArgumentException("cell is outside the grid: " + cell);
        }
    }

    /** The original text, one string per row. */
    public String[] toRows() {
        String[] out = new String[rows()];
        for (int r = 0; r < rows(); r++) {
            out[r] = new String(tiles[r]);
        }
        return out;
    }
}
