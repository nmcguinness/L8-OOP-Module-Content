package t10_exception_handling.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t10 ex08 - fixing exception anti-patterns")
class ScoreFileReaderTest {

    @TempDir
    Path dir;

    private Path write(String content) throws IOException {
        Path file = dir.resolve("scores.txt");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void totalScore_validFile_sumsAndSkipsBlankLines() throws Exception {
        assertEquals(60, ScoreFileReader.totalScore(write("10\n20\n\n 30 \n")));
    }

    @Test
    void totalScore_emptyFile_isZero() throws Exception {
        assertEquals(0, ScoreFileReader.totalScore(write("")));
    }

    @Test
    void totalScore_badNumber_reportsLineAndKeepsCause() throws IOException {
        Path file = write("10\ntwenty\n30\n");

        ScoreFileException e = assertThrows(ScoreFileException.class,
                () -> ScoreFileReader.totalScore(file));

        assertEquals(2, e.getLineNumber());
        assertInstanceOf(NumberFormatException.class, e.getCause());
    }

    @Test
    void totalScore_missingFile_isReportedNotSwallowed() {
        Path missing = dir.resolve("missing.txt");

        ScoreFileException e = assertThrows(ScoreFileException.class,
                () -> ScoreFileReader.totalScore(missing));

        assertInstanceOf(IOException.class, e.getCause());
    }
}
