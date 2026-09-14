package t24_pathfinding.common;

/**
 * Sample grids, each chosen to make one specific point.
 *
 * <p>The map decides the lesson. On a corridor maze the walls do most of the pruning, so
 * A* barely beats Dijkstra and the comparison looks unimpressive. On an open map with a
 * few obstacles the heuristic has room to work and the difference is obvious. Both are
 * here, because "it depends on the map" is itself worth measuring.
 */
public final class Maps {

    private Maps() {
    }

    /** Open ground with two blocks. A* should expand far fewer cells than Dijkstra. */
    public static Grid openRoom() {
        return Grid.parse(
            "##############################",
            "#S...........................#",
            "#............................#",
            "#.........########...........#",
            "#.........#......#...........#",
            "#.........#......#...........#",
            "#............................#",
            "#............................#",
            "#...........................G#",
            "##############################");
    }

    /** Narrow corridors. The walls prune the search, so the heuristic helps far less. */
    public static Grid corridorMaze() {
        return Grid.parse(
            "####################",
            "#S....#............#",
            "#.###.#.##########.#",
            "#.#...#.#........#.#",
            "#.#.###.#.######.#.#",
            "#.#.....#.#....#.#.#",
            "#.#######.#.##.#.#.#",
            "#.........#.#..#..G#",
            "#.#########.#.####.#",
            "#...........#......#",
            "####################");
    }

    /**
     * A short muddy route and a long dry one.
     *
     * <p>The direct line crosses rough ground costing 5 per cell. Breadth-first search
     * takes it, because it has fewer steps. Dijkstra and A* go around, because it is
     * cheaper. This is the map that shows BFS returning a legal but expensive answer.
     */
    public static Grid marsh() {
        return Grid.parse(
            "#############",
            "#S~~~~~~~~~G#",
            "#.#########.#",
            "#...........#",
            "#############");
    }

    /** The goal is sealed behind walls. Every search must terminate and report failure. */
    public static Grid sealedGoal() {
        return Grid.parse(
            "############",
            "#S.........#",
            "#....#######",
            "#....#....G#",
            "#....#.....#",
            "############");
    }

    /** The smallest interesting map: start and goal adjacent. */
    public static Grid tiny() {
        return Grid.parse(
            "####",
            "#SG#",
            "####");
    }
}
