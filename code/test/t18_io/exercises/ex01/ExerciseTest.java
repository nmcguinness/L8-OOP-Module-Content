package t18_io.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t18 ex01 - count non-blank lines")
class ExerciseTest {

    @TempDir
    Path dir;

    @Test
    void countLines_skipsBlankAndSpaceOnlyLines() throws IOException {
        Path file = dir.resolve("a.txt");
        Files.writeString(file, "first\n\nsecond\n   \nthird\n");

        assertEquals(3, Exercise.countLines(file));
    }

    @Test
    void countLines_emptyFile_isZero() throws IOException {
        Path file = dir.resolve("empty.txt");
        Files.writeString(file, "");

        assertEquals(0, Exercise.countLines(file));
    }

    @Test
    void countLines_missingFile_throwsIOException() {
        assertThrows(IOException.class, () -> Exercise.countLines(dir.resolve("missing.txt")));
    }
}
