package t24_pathfinding.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The observer seam, and the renderer that hangs off it.
 *
 * <p>Note what is <em>not</em> here: any capture of {@code System.out}. Because the
 * renderer separates building a frame from printing it, the interesting half is an
 * ordinary method returning a String.
 */
@DisplayName("t25 - search listeners and console rendering")
class ListenerAndRendererTest {

    // ---------- listener ----------

    @Test
    void countingListener_recordsEveryExpansionInOrder() {
        CountingListener listener = new CountingListener();
        new BreadthFirstPathfinder().findPath(Maps.tiny(), listener);

        assertEquals(List.of(new Cell(1, 1), new Cell(1, 2)), listener.expansionOrder());
        assertEquals(2, listener.expansions());
    }

    @Test
    void countingListener_capturesTheFinalPath() {
        CountingListener listener = new CountingListener();
        new BreadthFirstPathfinder().findPath(Maps.tiny(), listener);

        assertEquals(List.of(new Cell(1, 1), new Cell(1, 2)), listener.path());
        assertFalse(listener.exhausted());
    }

    @Test
    void countingListener_reportsExhaustionWhenNoPathExists() {
        CountingListener listener = new CountingListener();
        new BreadthFirstPathfinder().findPath(Maps.sealedGoal(), listener);

        assertTrue(listener.exhausted());
        assertTrue(listener.path().isEmpty());
    }

    @Test
    void expansionOrder_isACopyTheCallerCannotCorrupt() {
        CountingListener listener = new CountingListener();
        new BreadthFirstPathfinder().findPath(Maps.tiny(), listener);

        assertThrows(UnsupportedOperationException.class,
                () -> listener.expansionOrder().add(new Cell(9, 9)));
    }

    @Test
    void listenerNONE_swallowsEverythingWithoutThrowing() {
        SearchListener.NONE.onExpand(new Cell(0, 0), Set.of(), Set.of());
        SearchListener.NONE.onFound(List.of());
        SearchListener.NONE.onExhausted();
    }

    // ---------- renderer ----------

    private static final Grid TINY = Maps.tiny();

    @Test
    void render_showsWallsStartAndGoal() {
        String frame = new ConsoleRenderer(TINY).render(null, Set.of(), Set.of(), List.of());
        String[] lines = frame.lines().toArray(String[]::new);

        assertEquals("####", lines[0]);
        assertEquals("#SG#", lines[1]);
        assertEquals("####", lines[2]);
    }

    @Test
    void render_marksTheCurrentCellFrontierAndVisited() {
        Grid grid = Grid.parse("#####", "#S..#", "#..G#", "#####");
        String frame = new ConsoleRenderer(grid).render(
                new Cell(1, 2), Set.of(new Cell(1, 3)), Set.of(new Cell(2, 1)), List.of());

        String[] lines = frame.lines().toArray(String[]::new);
        assertEquals("#S@?#", lines[1]);
        assertEquals("#o.G#", lines[2]);
    }

    @Test
    void render_pathOverridesEverythingExceptTerrain() {
        Grid grid = Grid.parse("#####", "#S..#", "#..G#", "#####");
        String frame = new ConsoleRenderer(grid).render(
                null, Set.of(), Set.of(), List.of(new Cell(1, 1), new Cell(1, 2)));

        assertEquals("#S+.#", frame.lines().toArray(String[]::new)[1],
                "the start keeps its own glyph even when it is on the path");
    }

    @Test
    void render_leavesRoughGroundVisible() {
        Grid grid = Grid.parse("#####", "#S~G#", "#####");
        String frame = new ConsoleRenderer(grid).render(null, Set.of(), Set.of(), List.of());

        assertEquals("#S~G#", frame.lines().toArray(String[]::new)[1]);
    }

    @Test
    void constructor_rejectsNonsenseSettings() {
        assertThrows(IllegalArgumentException.class, () -> new ConsoleRenderer(null));
        assertThrows(IllegalArgumentException.class,
                () -> new ConsoleRenderer(TINY, ConsoleRenderer.Mode.PLAIN, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new ConsoleRenderer(TINY, ConsoleRenderer.Mode.PLAIN, 1, -1));
    }

    @Test
    void renderer_canBeAttachedToARealSearchWithoutAffectingIt() {
        // The listener must observe, never steer.
        Grid grid = Maps.corridorMaze();
        SearchResult watched = new BreadthFirstPathfinder()
                .findPath(grid, new ConsoleRenderer(grid, ConsoleRenderer.Mode.PLAIN, 10_000, 0));
        SearchResult unwatched = new BreadthFirstPathfinder().findPath(grid);

        assertEquals(unwatched.path(), watched.path());
        assertEquals(unwatched.expanded(), watched.expanded());
    }

    // ---------- SearchResult ----------

    @Test
    void searchResult_notFound_isEmptyAndZeroCost() {
        SearchResult result = SearchResult.notFound(12);

        assertFalse(result.found());
        assertEquals(0, result.cost());
        assertEquals(0, result.steps());
        assertEquals(12, result.expanded());
    }

    @Test
    void searchResult_stepsIsOneLessThanCellCount() {
        SearchResult result = new SearchResult(
                List.of(new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)), 2, 3);
        assertEquals(2, result.steps());
    }

    @Test
    void searchResult_pathIsImmutable() {
        SearchResult result = new SearchResult(List.of(new Cell(0, 0)), 0, 1);
        assertThrows(UnsupportedOperationException.class, () -> result.path().add(new Cell(1, 1)));
    }
}
