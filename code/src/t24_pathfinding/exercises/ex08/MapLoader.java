package t24_pathfinding.exercises.ex08;

import t24_pathfinding.common.Grid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 08, option (d): load a map from a text file, reporting problems by line number.
 */
public final class MapLoader {

    private static final String TILES = "#.~SG";

    private MapLoader() {
    }

    public static Grid load(Path file) throws IOException {
        List<String> rows = new ArrayList<>();
        int width = -1;
        int lineNumber = 0;

        for (String line : Files.readAllLines(file)) {
            lineNumber++;
            if (line.isBlank()) {
                continue;
            }
            if (width == -1) {
                width = line.length();
            } else if (line.length() != width) {
                throw new IllegalArgumentException("Line " + lineNumber + ": expected "
                        + width + " tiles but found " + line.length());
            }
            for (int col = 0; col < line.length(); col++) {
                if (TILES.indexOf(line.charAt(col)) < 0) {
                    throw new IllegalArgumentException("Line " + lineNumber + ", column "
                            + (col + 1) + ": unknown tile '" + line.charAt(col) + "'");
                }
            }
            rows.add(line);
        }
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("the map file is empty");
        }
        // Grid.parse still checks the rules a line-by-line scan cannot, such as one start
        return Grid.parse(rows.toArray(new String[0]));
    }
}
