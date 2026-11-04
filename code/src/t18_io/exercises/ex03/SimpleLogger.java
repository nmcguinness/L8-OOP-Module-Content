package t18_io.exercises.ex03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Appends timestamped lines to a log file, creating the file on first use.
 * Each line looks like: [2026-11-03 14:05:09] message
 */
public class SimpleLogger {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path logFile;

    public SimpleLogger(Path logFile) {
        if (logFile == null) {
            throw new IllegalArgumentException("logFile must not be null");
        }
        this.logFile = logFile;
    }

    public void log(String message) throws IOException {
        String line = "[" + LocalDateTime.now().format(FORMAT) + "] " + message
                + System.lineSeparator();
        // CREATE makes the file if it is missing; APPEND adds to the end instead of overwriting
        Files.writeString(logFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    // Every line logged so far; empty if nothing has been logged yet
    public List<String> readLog() throws IOException {
        if (Files.notExists(logFile)) {
            return List.of();
        }
        return Files.readAllLines(logFile);
    }
}
