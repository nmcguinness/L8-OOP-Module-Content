package t24_pathfinding.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t25 - heuristics and admissibility")
class HeuristicTest {

    @Test
    void zero_isAlwaysZero() {
        assertEquals(0, Heuristic.zero().estimate(new Cell(0, 0), new Cell(9, 9)));
    }

    @Test
    void manhattan_countsRowsPlusColumns() {
        assertEquals(7, Heuristic.manhattan().estimate(new Cell(0, 0), new Cell(3, 4)));
    }

    @Test
    void manhattan_isSymmetricAndZeroAtTheSameCell() {
        Cell a = new Cell(2, 5);
        Cell b = new Cell(7, 1);
        assertEquals(Heuristic.manhattan().estimate(a, b),
                Heuristic.manhattan().estimate(b, a));
        assertEquals(0, Heuristic.manhattan().estimate(a, a));
    }

    @Test
    void scaledManhattan_multipliesTheEstimate() {
        assertEquals(21, Heuristic.scaledManhattan(3).estimate(new Cell(0, 0), new Cell(3, 4)));
    }

    @Test
    void scaledManhattan_negativeFactor_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Heuristic.scaledManhattan(-1));
    }

    /**
     * The property that makes A* safe: the estimate must never exceed the true remaining
     * cost. Checked here against the real optimal cost from every open cell of a map.
     */
    @Test
    void manhattan_neverOverEstimatesTheTrueCost() {
        Grid grid = Maps.marsh();
        Cell goal = grid.goal();

        for (int r = 0; r < grid.rows(); r++) {
            for (int c = 0; c < grid.cols(); c++) {
                Cell from = new Cell(r, c);
                if (!grid.isPassable(from) || from.equals(goal)) {
                    // At the goal both the estimate and the true cost are 0, and the
                    // helper below cannot mark one cell as start and goal at once.
                    continue;
                }
                int trueCost = optimalCostFrom(grid, from, goal);
                if (trueCost < 0) {
                    continue;               // unreachable, nothing to compare against
                }
                int estimate = Heuristic.manhattan().estimate(from, goal);
                assertTrue(estimate <= trueCost,
                        "Manhattan over-estimated from " + from + ": said " + estimate
                                + ", truth is " + trueCost);
            }
        }
    }

    @Test
    void scaledManhattan_withALargeFactor_doesOverEstimate() {
        // The counter-example that proves the previous test is testing something.
        Grid grid = Maps.marsh();
        Cell from = grid.start();
        int trueCost = optimalCostFrom(grid, from, grid.goal());

        assertTrue(Heuristic.scaledManhattan(10).estimate(from, grid.goal()) > trueCost);
    }

    /** Cheapest cost from an arbitrary cell to the goal, or -1 if unreachable. */
    private static int optimalCostFrom(Grid grid, Cell from, Cell goal) {
        String[] rows = grid.toRows();
        char[][] tiles = new char[rows.length][];
        for (int r = 0; r < rows.length; r++) {
            tiles[r] = rows[r].toCharArray();
        }
        // Re-mark the grid so the search starts where we want it to.
        clear(tiles, Grid.START);
        clear(tiles, Grid.GOAL);
        tiles[from.row()][from.col()] = Grid.START;
        tiles[goal.row()][goal.col()] = Grid.GOAL;

        String[] rebuilt = new String[tiles.length];
        for (int r = 0; r < tiles.length; r++) {
            rebuilt[r] = new String(tiles[r]);
        }
        SearchResult result = new DijkstraPathfinder().findPath(Grid.parse(rebuilt));
        return result.found() ? result.cost() : -1;
    }

    private static void clear(char[][] tiles, char marker) {
        for (char[] row : tiles) {
            for (int c = 0; c < row.length; c++) {
                if (row[c] == marker) {
                    row[c] = Grid.OPEN;
                }
            }
        }
    }
}
