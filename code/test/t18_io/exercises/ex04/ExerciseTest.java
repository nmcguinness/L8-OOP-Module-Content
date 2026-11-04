package t18_io.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t18 ex04 - parse a CSV file")
class ExerciseTest {

    @TempDir
    Path dir;

    private Path write(String content) throws IOException {
        Path file = dir.resolve("scores.csv");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void parseCsv_skipsHeader_andTrimsFields() throws IOException {
        List<String[]> rows = Exercise.parseCsv(write("name,score,level\nAlice, 95 ,3\nBen,82,2\n"));

        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"Alice", "95", "3"}, rows.get(0));
    }

    @Test
    void parseCsv_blankLines_areSkipped() throws IOException {
        assertEquals(1, Exercise.parseCsv(write("name,score,level\n\nAlice,95,3\n\n")).size());
    }

    @Test
    void parseCsv_wrongFieldCount_reportsLineNumber() throws IOException {
        Path file = write("name,score,level\nAlice,95,3\nBen,82\n");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> Exercise.parseCsv(file));
        assertEquals("Line 3: expected 3 fields but found 2", e.getMessage());
    }

    @Test
    void scoresAbove_example_keepsOnlyScoresOver80() throws IOException {
        List<String[]> rows = Exercise.parseCsv(
                write("name,score,level\nAlice,95,3\nBen,82,2\nClara,78,1\nDev,80,1\n"));

        List<String[]> high = Exercise.scoresAbove(rows, 80);

        assertEquals(2, high.size());
        assertEquals("Alice", high.get(0)[0]);
        assertEquals("Ben", high.get(1)[0]);
    }
}
