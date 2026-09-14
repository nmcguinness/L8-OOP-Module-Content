package t24_pathfinding.common;

import java.util.List;
import java.util.Set;

/**
 * Draws a search to the console, one frame at a time.
 *
 * <pre>
 *   S  start          o  already expanded
 *   G  goal           ?  on the frontier, not yet expanded
 *   #  wall           @  being expanded right now
 *   ~  rough ground   +  the final path
 * </pre>
 *
 * <p>Two modes, because one is not enough:
 *
 * <ul>
 *   <li>{@link Mode#PLAIN} reprints each frame below the last. It scrolls, but it works
 *       in every console including IntelliJ's Run window, and it can be read aloud.</li>
 *   <li>{@link Mode#ANSI} repaints in place using escape codes, which animates smoothly
 *       in a real terminal and prints visible gibberish in a console that does not
 *       support them.</li>
 * </ul>
 *
 * <p>PLAIN is the default deliberately: a first run that fills the screen with escape
 * codes teaches nothing.
 */
public final class ConsoleRenderer implements SearchListener {

    public enum Mode { PLAIN, ANSI }

    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    private final Grid grid;
    private final Mode mode;
    private final int framesEvery;
    private final long pauseMillis;
    private int expansions;

    public ConsoleRenderer(Grid grid) {
        this(grid, Mode.PLAIN, 1, 0);
    }

    /**
     * @param framesEvery draw one frame per this many expansions; 1 draws every step
     * @param pauseMillis sleep after each frame; 0 renders as fast as the console allows
     */
    public ConsoleRenderer(Grid grid, Mode mode, int framesEvery, long pauseMillis) {
        if (grid == null) {
            throw new IllegalArgumentException("grid is required");
        }
        if (framesEvery < 1) {
            throw new IllegalArgumentException("framesEvery must be >= 1");
        }
        if (pauseMillis < 0) {
            throw new IllegalArgumentException("pauseMillis must be >= 0");
        }
        this.grid = grid;
        this.mode = mode;
        this.framesEvery = framesEvery;
        this.pauseMillis = pauseMillis;
    }

    @Override
    public void onExpand(Cell current, Set<Cell> frontier, Set<Cell> visited) {
        if (expansions++ % framesEvery != 0) {
            return;
        }
        print(render(current, frontier, visited, List.of()),
              "expanded " + expansions + ", frontier " + frontier.size());
        pause();
    }

    @Override
    public void onFound(List<Cell> path) {
        print(render(null, Set.of(), Set.of(), path),
              "path found: " + path.size() + " cells, cost "
                      + Pathfinder.costOf(grid, path) + ", after " + expansions + " expansions");
    }

    @Override
    public void onExhausted() {
        print(render(null, Set.of(), Set.of(), List.of()),
              "no path exists - frontier emptied after " + expansions + " expansions");
    }

    /** Builds one frame as text. Separated from printing so it can be unit tested. */
    public String render(Cell current, Set<Cell> frontier, Set<Cell> visited, List<Cell> path) {
        StringBuilder out = new StringBuilder();
        for (int r = 0; r < grid.rows(); r++) {
            for (int c = 0; c < grid.cols(); c++) {
                out.append(glyphFor(new Cell(r, c), current, frontier, visited, path));
            }
            out.append(System.lineSeparator());
        }
        return out.toString();
    }

    private char glyphFor(Cell cell, Cell current, Set<Cell> frontier,
                          Set<Cell> visited, List<Cell> path) {
        char tile = grid.tileAt(cell);
        if (tile == Grid.WALL || tile == Grid.START || tile == Grid.GOAL) {
            return tile;
        }
        if (path.contains(cell)) {
            return '+';
        }
        if (cell.equals(current)) {
            return '@';
        }
        if (frontier.contains(cell)) {
            return '?';
        }
        if (visited.contains(cell)) {
            return 'o';
        }
        return tile;
    }

    private void print(String frame, String caption) {
        if (mode == Mode.ANSI) {
            System.out.print(CLEAR_SCREEN);
        }
        System.out.print(frame);
        System.out.println(caption);
        if (mode == Mode.PLAIN) {
            System.out.println();
        }
    }

    private void pause() {
        if (pauseMillis == 0) {
            return;
        }
        try {
            Thread.sleep(pauseMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
