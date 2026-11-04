package t18_io.exercises.ex01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Exercise {

    // Number of lines that contain something other than spaces
    public static int countLines(Path path) throws IOException {
        // Files.lines reads lazily and holds the file open, so it must be closed
        try (Stream<String> lines = Files.lines(path)) {
            return (int) lines.filter(line -> !line.isBlank()).count();
        }
    }

    public static void run() throws IOException {
        Path file = Files.createTempFile("count", ".txt");
        Files.writeString(file, "first\n\nsecond\n   \nthird\n");
        System.out.println("Non-blank lines: " + countLines(file));
        Files.deleteIfExists(file);
    }
}
