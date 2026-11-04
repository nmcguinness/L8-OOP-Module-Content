package t24_pathfinding.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t24_pathfinding.common.Cell;
import t24_pathfinding.common.Grid;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t24 ex02 - neighbours and bounds")
class ExerciseTest {

    private final Grid open = Grid.parse("S..", "...", "..G");

    @Test
    void neighboursOf_openGround_fourInFixedOrder() {
        assertEquals(List.of(new Cell(0, 1), new Cell(2, 1), new Cell(1, 0), new Cell(1, 2)),
                Exercise.neighboursOf(open, new Cell(1, 1)));
    }

    @Test
    void neighboursOf_corner_two() {
        assertEquals(2, Exercise.neighboursOf(open, new Cell(0, 0)).size());
    }

    @Test
    void neighboursOf_againstAWall_wallIsSkipped() {
        Grid grid = Grid.parse("S#G", "...");

        assertEquals(List.of(new Cell(1, 0)), Exercise.neighboursOf(grid, new Cell(0, 0)));
    }

    @Test
    void neighboursOf_surroundedByWalls_emptyListNotNull() {
        Grid grid = Grid.parse("#####", "#S#G#", "#####");

        assertTrue(Exercise.neighboursOf(grid, new Cell(1, 1)).isEmpty());
    }

    @Test
    void neighboursOf_matchesGridNeighbours() {
        Cell centre = new Cell(1, 1);

        assertEquals(open.neighbours(centre), Exercise.neighboursOf(open, centre));
    }

    @Test
    void neighboursOf_cellOffTheMap_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> Exercise.neighboursOf(open, new Cell(5, 5)));
    }
}
