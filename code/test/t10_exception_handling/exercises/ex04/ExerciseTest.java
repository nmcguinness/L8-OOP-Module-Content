package t10_exception_handling.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t10 ex04 - try-with-resources file reading")
class ExerciseTest {

    @TempDir
    Path dir;

    @Test
    void readLines_existingFile_returnsEveryLineInOrder() throws IOException {
        Path file = dir.resolve("lines.txt");
        Files.writeString(file, "first\nsecond\n\nfourth\n");

        assertEquals(List.of("first", "second", "", "fourth"), Exercise.readLines(file.toString()));
    }

    @Test
    void readLines_emptyFile_returnsEmptyList() throws IOException {
        Path file = dir.resolve("empty.txt");
        Files.writeString(file, "");

        assertTrue(Exercise.readLines(file.toString()).isEmpty());
    }

    @Test
    void readLines_missingFile_throwsIOException() {
        String missing = dir.resolve("missing.txt").toString();

        assertThrows(IOException.class, () -> Exercise.readLines(missing));
    }
}
