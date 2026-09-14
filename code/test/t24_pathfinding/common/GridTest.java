package t24_pathfinding.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t25 - Grid parsing, terrain cost and neighbours")
class GridTest {

    private static Grid small() {
        return Grid.parse(
            "#####",
            "#S.~#",
            "#.#G#",
            "#####");
    }

    // ---------- parsing ----------

    @Test
    void parse_findsStartAndGoal() {
        Grid grid = small();
        assertEquals(new Cell(1, 1), grid.start());
        assertEquals(new Cell(2, 3), grid.goal());
    }

    @Test
    void parse_reportsItsOwnDimensions() {
        Grid grid = small();
        assertEquals(4, grid.rows());
        assertEquals(5, grid.cols());
    }

    @Test
    void parse_raggedRows_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> Grid.parse("#####", "#S.G#", "###"));
    }

    @Test
    void parse_missingStartOrGoal_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Grid.parse("###", "#G#", "###"));
        assertThrows(IllegalArgumentException.class, () -> Grid.parse("###", "#S#", "###"));
    }

    @Test
    void parse_duplicateStartOrGoal_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Grid.parse("####", "#SS#", "#.G#", "####"));
        assertThrows(IllegalArgumentException.class, () -> Grid.parse("####", "#SG#", "#.G#", "####"));
    }

    @Test
    void parse_unknownTile_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Grid.parse("####", "#SxG", "####"));
    }

    @Test
    void parse_nullOrEmpty_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Grid.parse((String[]) null));
        assertThrows(IllegalArgumentException.class, Grid::parse);
    }

    // ---------- terrain ----------

    @Test
    void costToEnter_openGroundCostsOne() {
        assertEquals(1, small().costToEnter(new Cell(1, 2)));
    }

    @Test
    void costToEnter_roughGroundCostsFive() {
        assertEquals(5, small().costToEnter(new Cell(1, 3)));
    }

    @Test
    void costToEnter_startAndGoalAreOrdinaryOpenGround() {
        Grid grid = small();
        assertEquals(1, grid.costToEnter(grid.start()));
        assertEquals(1, grid.costToEnter(grid.goal()));
    }

    @Test
    void costToEnter_wall_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> small().costToEnter(new Cell(0, 0)));
    }

    // ---------- bounds ----------

    @Test
    void inBounds_rejectsEveryDirectionOfOverrun() {
        Grid grid = small();
        assertTrue(grid.inBounds(new Cell(0, 0)));
        assertFalse(grid.inBounds(new Cell(-1, 0)));
        assertFalse(grid.inBounds(new Cell(0, -1)));
        assertFalse(grid.inBounds(new Cell(4, 0)));
        assertFalse(grid.inBounds(new Cell(0, 5)));
    }

    @Test
    void tileAt_outsideTheGrid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> small().tileAt(new Cell(-1, 0)));
    }

    @Test
    void isWall_outsideTheGrid_isFalseRatherThanThrowing() {
        // Searches probe outwards constantly; making this throw would force a bounds
        // check at every call site.
        assertFalse(small().isWall(new Cell(-1, 0)));
        assertFalse(small().isPassable(new Cell(-1, 0)));
    }

    // ---------- neighbours ----------

    @Test
    void neighbours_excludeWalls() {
        // (1,1) is the start: open ground below, open ground right, walls above and left.
        List<Cell> found = small().neighbours(new Cell(1, 1));
        assertEquals(List.of(new Cell(2, 1), new Cell(1, 2)), found);
    }

    @Test
    void neighbours_includeRoughGround() {
        assertTrue(small().neighbours(new Cell(1, 2)).contains(new Cell(1, 3)),
                "rough ground is expensive, not impassable");
    }

    @Test
    void neighbours_areReturnedInAStableOrder() {
        // Reproducible ordering is what makes expansion-order assertions possible.
        Grid open = Grid.parse("#####", "#...#", "#.S.#", "#..G#", "#####");
        assertEquals(open.neighbours(new Cell(2, 2)), open.neighbours(new Cell(2, 2)));
    }

    @Test
    void neighbours_neverIncludeDiagonals() {
        Grid open = Grid.parse("#####", "#...#", "#.S.#", "#..G#", "#####");
        assertFalse(open.neighbours(new Cell(2, 2)).contains(new Cell(1, 1)));
        assertEquals(4, open.neighbours(new Cell(2, 2)).size());
    }

    @Test
    void toRows_roundTripsTheOriginalText() {
        String[] rows = {"#####", "#S.~#", "#.#G#", "#####"};
        assertEquals(List.of(rows), List.of(Grid.parse(rows).toRows()));
    }
}
