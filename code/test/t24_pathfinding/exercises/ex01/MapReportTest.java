package t24_pathfinding.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t24_pathfinding.common.Cell;
import t24_pathfinding.common.Grid;
import t24_pathfinding.common.Maps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t24 ex01 - read the map")
class MapReportTest {

    @Test
    void of_marsh_reportsDimensionsCountsAndDistance() {
        MapReport report = MapReport.of(Maps.marsh());

        assertEquals(5, report.rows());
        assertEquals(13, report.cols());
        assertEquals(new Cell(1, 1), report.start());
        assertEquals(new Cell(1, 11), report.goal());
        assertEquals(24, report.passable());
        assertEquals(9, report.rough());
        assertEquals(10, report.manhattan());
    }

    @Test
    void parse_raggedMap_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> Grid.parse("#####", "#S.G", "#####"));
    }
}
