package t24_pathfinding.exercises.ex08;

import t24_pathfinding.common.AStarPathfinder;
import t24_pathfinding.common.Grid;
import t24_pathfinding.common.Heuristic;
import t24_pathfinding.common.SearchResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Exercise {

    public static void run() throws IOException {
        Path good = Files.createTempFile("map", ".txt");
        Files.writeString(good, "#####\n#S.G#\n#####\n");
        Grid grid = MapLoader.load(good);
        SearchResult r = new AStarPathfinder(Heuristic.manhattan()).findPath(grid);
        System.out.println("Loaded " + grid.rows() + "x" + grid.cols() + ", path cost " + r.cost());

        Path bad = Files.createTempFile("map", ".txt");
        Files.writeString(bad, "#####\n#S.G\n#####\n");
        try {
            MapLoader.load(bad);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        Files.deleteIfExists(good);
        Files.deleteIfExists(bad);
    }
}
