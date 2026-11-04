package t24_pathfinding.exercises.ex02;

import t24_pathfinding.common.Cell;
import t24_pathfinding.common.Direction;
import t24_pathfinding.common.Grid;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    // Passable neighbours in the fixed order UP, DOWN, LEFT, RIGHT; never null
    public static List<Cell> neighboursOf(Grid grid, Cell cell) {
        if (!grid.inBounds(cell)) {
            throw new IllegalArgumentException("cell is outside the grid: " + cell);
        }
        List<Cell> result = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Cell next = cell.step(direction);
            // Check the edge first: tileAt would fail for a cell off the map
            if (grid.inBounds(next) && grid.isPassable(next)) {
                result.add(next);
            }
        }
        return result;
    }

    public static void run() {
        Grid open = Grid.parse("S..", "...", "..G");
        System.out.println("Corner (0,0): " + neighboursOf(open, new Cell(0, 0)));
        System.out.println("Centre (1,1): " + neighboursOf(open, new Cell(1, 1)));
    }
}
