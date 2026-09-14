package t24_pathfinding.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Rules every pathfinder must obey, whatever algorithm it uses.
 *
 * <p>Each test runs once per implementation. When you add a fourth algorithm in
 * Exercise 08, add it to {@link #pathfinders()} and these checks come free.
 */
@DisplayName("t25 - the contract shared by every Pathfinder")
class PathfinderContractTest {

    static Stream<Pathfinder> pathfinders() {
        return Stream.of(
                new BreadthFirstPathfinder(),
                new DijkstraPathfinder(),
                new AStarPathfinder(Heuristic.manhattan(), "A* (Manhattan)"));
    }

    private static void assertWalkable(Grid grid, List<Cell> path) {
        assertEquals(grid.start(), path.get(0), "a path must begin at the start");
        assertEquals(grid.goal(), path.get(path.size() - 1), "a path must end at the goal");

        for (Cell cell : path) {
            assertFalse(grid.isWall(cell), "a path walked through a wall at " + cell);
        }
        for (int i = 1; i < path.size(); i++) {
            Cell a = path.get(i - 1);
            Cell b = path.get(i);
            int distance = Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
            assertEquals(1, distance, "path jumped from " + a + " to " + b);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_openRoom_returnsAWalkablePath(Pathfinder finder) {
        Grid grid = Maps.openRoom();
        SearchResult result = finder.findPath(grid);

        assertTrue(result.found());
        assertWalkable(grid, result.path());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_corridorMaze_returnsAWalkablePath(Pathfinder finder) {
        Grid grid = Maps.corridorMaze();
        SearchResult result = finder.findPath(grid);

        assertTrue(result.found());
        assertWalkable(grid, result.path());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_reportedCostMatchesTheActualPath(Pathfinder finder) {
        Grid grid = Maps.marsh();
        SearchResult result = finder.findPath(grid);

        assertEquals(Pathfinder.costOf(grid, result.path()), result.cost(),
                "the reported cost must be the cost of the path actually returned");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_sealedGoal_terminatesAndReportsNoPath(Pathfinder finder) {
        SearchResult result = finder.findPath(Maps.sealedGoal());

        assertFalse(result.found());
        assertTrue(result.path().isEmpty());
        assertTrue(result.expanded() > 0, "it must have looked before giving up");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_startAdjacentToGoal_isASingleStep(Pathfinder finder) {
        SearchResult result = finder.findPath(Maps.tiny());

        assertTrue(result.found());
        assertEquals(1, result.steps());
        assertEquals(2, result.path().size());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_startIsAlsoTheGoal_isZeroSteps(Pathfinder finder) {
        // Not reachable through Grid.parse, which requires distinct S and G, so the
        // degenerate case is built directly.
        Grid grid = Grid.parse("###", "#S#", "#G#", "###");
        SearchResult result = finder.findPath(grid);

        assertTrue(result.found());
        assertEquals(1, result.steps());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_isRepeatable(Pathfinder finder) {
        Grid grid = Maps.corridorMaze();
        assertEquals(finder.findPath(grid).path(), finder.findPath(grid).path(),
                "two runs over the same map must agree");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_withoutAListener_doesNotThrow(Pathfinder finder) {
        assertTrue(finder.findPath(Maps.tiny()).found());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_notifiesTheListenerOnceForEveryExpansion(Pathfinder finder) {
        CountingListener listener = new CountingListener();
        SearchResult result = finder.findPath(Maps.corridorMaze(), listener);

        assertEquals(result.expanded(), listener.expansions(),
                "the reported expansion count must match what the listener saw");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pathfinders")
    void findPath_expandsNoCellTwice(Pathfinder finder) {
        CountingListener listener = new CountingListener();
        finder.findPath(Maps.openRoom(), listener);

        List<Cell> order = listener.expansionOrder();
        assertEquals(order.size(), Set.copyOf(order).size(),
                "a cell expanded twice means wasted work, or a loop");
    }
}
