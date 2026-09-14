package t24_pathfinding.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * What actually separates the three algorithms.
 *
 * <p>Each test here pins down one claim the notes make, so that a claim cannot quietly
 * stop being true.
 */
@DisplayName("t25 - how the three algorithms differ")
class AlgorithmComparisonTest {

    private static final Pathfinder BFS = new BreadthFirstPathfinder();
    private static final Pathfinder DIJKSTRA = new DijkstraPathfinder();
    private static final Pathfinder A_STAR =
            new AStarPathfinder(Heuristic.manhattan(), "A* (Manhattan)");

    // ---------- on flat ground they agree ----------

    @Test
    void openRoom_allThreeFindTheSameCost() {
        Grid grid = Maps.openRoom();
        int bfs = BFS.findPath(grid).cost();

        assertEquals(bfs, DIJKSTRA.findPath(grid).cost());
        assertEquals(bfs, A_STAR.findPath(grid).cost());
    }

    @Test
    void openRoom_aStarExpandsFarFewerCellsThanDijkstra() {
        Grid grid = Maps.openRoom();
        int dijkstra = DIJKSTRA.findPath(grid).expanded();
        int aStar = A_STAR.findPath(grid).expanded();

        assertTrue(aStar < dijkstra / 2,
                "expected A* to expand less than half of Dijkstra's " + dijkstra
                        + " cells, but it expanded " + aStar);
    }

    @Test
    void corridorMaze_theHeuristicHelpsLess() {
        // Walls already prune the search, so A* has less to contribute. Worth pinning:
        // "A* is faster" is map-dependent, not a law.
        Grid grid = Maps.corridorMaze();
        int dijkstra = DIJKSTRA.findPath(grid).expanded();
        int aStar = A_STAR.findPath(grid).expanded();

        assertTrue(aStar < dijkstra, "A* should still not be worse");
        assertTrue(aStar > dijkstra / 2,
                "in a corridor maze the saving should be modest, not dramatic");
    }

    // ---------- on rough ground they do not ----------

    @Test
    void marsh_breadthFirstTakesTheShortestRouteNotTheCheapest() {
        Grid grid = Maps.marsh();
        SearchResult bfs = BFS.findPath(grid);
        SearchResult dijkstra = DIJKSTRA.findPath(grid);

        assertTrue(bfs.steps() < dijkstra.steps(), "BFS should find fewer steps");
        assertTrue(bfs.cost() > dijkstra.cost(), "yet pay more to walk them");
    }

    @Test
    void marsh_breadthFirstPathCrossesRoughGround() {
        Grid grid = Maps.marsh();
        boolean crossesMud = BFS.findPath(grid).path().stream()
                .anyMatch(cell -> grid.tileAt(cell) == Grid.ROUGH);

        assertTrue(crossesMud, "the cheap-looking straight line runs through the marsh");
    }

    @Test
    void marsh_dijkstraAndAStarGoAroundTheMud() {
        Grid grid = Maps.marsh();

        for (Pathfinder finder : new Pathfinder[] {DIJKSTRA, A_STAR}) {
            boolean crossesMud = finder.findPath(grid).path().stream()
                    .anyMatch(cell -> grid.tileAt(cell) == Grid.ROUGH);
            assertTrue(!crossesMud, finder.name() + " should avoid the marsh entirely");
        }
    }

    @Test
    void marsh_costOrderingIsBfsWorstThenTheOthersEqual() {
        Grid grid = Maps.marsh();
        assertEquals(DIJKSTRA.findPath(grid).cost(), A_STAR.findPath(grid).cost());
        assertNotEquals(BFS.findPath(grid).cost(), DIJKSTRA.findPath(grid).cost());
    }

    // ---------- Dijkstra is A* with no heuristic ----------

    @Test
    void dijkstra_isExactlyAStarWithAZeroHeuristic() {
        Grid grid = Maps.corridorMaze();

        CountingListener viaDijkstra = new CountingListener();
        DIJKSTRA.findPath(grid, viaDijkstra);

        CountingListener viaAStar = new CountingListener();
        new AStarPathfinder(Heuristic.zero()).findPath(grid, viaAStar);

        assertEquals(viaDijkstra.expansionOrder(), viaAStar.expansionOrder(),
                "same algorithm, so the cells must be expanded in the same order");
    }

    // ---------- admissibility ----------

    @Test
    void marsh_anOverEstimatingHeuristicReturnsAWorsePath() {
        Grid grid = Maps.marsh();
        SearchResult honest = A_STAR.findPath(grid);
        SearchResult greedy = new AStarPathfinder(Heuristic.scaledManhattan(5), "A* x5")
                .findPath(grid);

        assertTrue(greedy.cost() > honest.cost(),
                "an inadmissible heuristic should cost more: " + greedy.cost()
                        + " vs " + honest.cost());
    }

    @Test
    void marsh_theOverEstimatingHeuristicIsFasterWhichIsWhyItIsTempting() {
        Grid grid = Maps.marsh();
        int honest = A_STAR.findPath(grid).expanded();
        int greedy = new AStarPathfinder(Heuristic.scaledManhattan(5), "A* x5")
                .findPath(grid).expanded();

        assertTrue(greedy < honest,
                "it wins on speed - which is exactly how this bug survives review");
    }

    @Test
    void scaledManhattan_withFactorOne_isPlainManhattan() {
        Cell a = new Cell(0, 0);
        Cell b = new Cell(3, 4);
        assertEquals(Heuristic.manhattan().estimate(a, b),
                Heuristic.scaledManhattan(1).estimate(a, b));
    }
}
