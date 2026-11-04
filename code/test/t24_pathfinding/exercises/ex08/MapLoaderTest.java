package t24_pathfinding.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import t24_pathfinding.common.Cell;
import t24_pathfinding.common.Grid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t24 ex08 (d) - loading a map from a file")
class MapLoaderTest {

    @TempDir
    Path dir;

    private Path write(String content) throws IOException {
        Path file = dir.resolve("map.txt");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void load_validMap_givesTheGrid() throws IOException {
        Grid grid = MapLoader.load(write("#####\n#S.G#\n#####\n"));

        assertEquals(3, grid.rows());
        assertEquals(new Cell(1, 3), grid.goal());
    }

    @Test
    void load_shortLine_reportsItsLineNumber() throws IOException {
        Path file = write("#####\n#S.G\n#####\n");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> MapLoader.load(file));
        assertEquals("Line 2: expected 5 tiles but found 4", e.getMessage());
    }

    @Test
    void load_unknownTile_reportsLineAndColumn() throws IOException {
        Path file = write("#####\n#SxG#\n#####\n");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> MapLoader.load(file));
        assertEquals("Line 2, column 3: unknown tile 'x'", e.getMessage());
    }

    @Test
    void load_missingFile_throwsIOException() {
        assertThrows(IOException.class, () -> MapLoader.load(dir.resolve("missing.txt")));
    }
}
