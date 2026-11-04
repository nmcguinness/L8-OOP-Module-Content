package t24_pathfinding.exercises.ex01;

import t24_pathfinding.common.Cell;
import t24_pathfinding.common.Grid;

/**
 * Facts about a map, worked out without searching it.
 *
 * <p>The Manhattan distance is a lower bound on the true cost: every move goes one row or one
 * column, so a path needs at least that many moves, and every move costs at least 1.
 */
public record MapReport(int rows, int cols, Cell start, Cell goal,
                        int passable, int rough, int manhattan) {

    public static MapReport of(Grid grid) {
        int passable = 0;
        int rough = 0;
        for (int r = 0; r < grid.rows(); r++) {
            for (int c = 0; c < grid.cols(); c++) {
                Cell cell = new Cell(r, c);
                if (grid.isPassable(cell)) {
                    passable++;
                    if (grid.tileAt(cell) == Grid.ROUGH) {
                        rough++;
                    }
                }
            }
        }
        Cell s = grid.start();
        Cell g = grid.goal();
        int manhattan = Math.abs(s.row() - g.row()) + Math.abs(s.col() - g.col());
        return new MapReport(grid.rows(), grid.cols(), s, g, passable, rough, manhattan);
    }
}
