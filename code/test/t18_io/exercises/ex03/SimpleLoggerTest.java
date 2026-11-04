package t18_io.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t18 ex03 - append timestamped log lines")
class SimpleLoggerTest {

    @TempDir
    Path dir;

    @Test
    void log_twice_appendsTwoLinesInOrder() throws IOException {
        SimpleLogger logger = new SimpleLogger(dir.resolve("app.log"));

        logger.log("Server started");
        logger.log("Client connected");

        List<String> lines = logger.readLog();
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).endsWith("] Server started"));
        assertTrue(lines.get(1).endsWith("] Client connected"));
    }

    @Test
    void log_lineStartsWithTimestampInTheAgreedFormat() throws IOException {
        SimpleLogger logger = new SimpleLogger(dir.resolve("app.log"));

        logger.log("hello");

        String line = logger.readLog().get(0);
        assertTrue(line.matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}] hello"), line);
    }

    @Test
    void readLog_beforeAnyLogging_isEmpty() throws IOException {
        assertTrue(new SimpleLogger(dir.resolve("none.log")).readLog().isEmpty());
    }
}
