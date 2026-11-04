package t18_io.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t18 ex02 - write a list to a file")
class ExerciseTest {

    @TempDir
    Path dir;

    @Test
    void writeNames_writesOneNamePerLine() throws IOException {
        Path file = dir.resolve("names.txt");

        Exercise.writeNames(file, List.of("Ana", "Ben", "Cara"));

        assertEquals(List.of("Ana", "Ben", "Cara"), Files.readAllLines(file));
    }

    @Test
    void writeNames_existingFile_isReplaced() throws IOException {
        Path file = dir.resolve("names.txt");
        Files.writeString(file, "old\ncontent\n");

        Exercise.writeNames(file, List.of("New"));

        assertEquals(List.of("New"), Files.readAllLines(file));
    }

    @Test
    void writeNames_missingFolder_throwsIOException() {
        Path file = dir.resolve("no-such-folder").resolve("names.txt");

        assertThrows(IOException.class, () -> Exercise.writeNames(file, List.of("Ana")));
    }
}
