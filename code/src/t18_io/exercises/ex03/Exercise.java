package t18_io.exercises.ex03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Exercise {

    public static void run() throws IOException {
        Path file = Files.createTempFile("app", ".log");
        Files.delete(file);

        SimpleLogger logger = new SimpleLogger(file);
        logger.log("Server started");
        logger.log("Client connected");

        for (String line : logger.readLog()) {
            System.out.println(line);
        }
        Files.deleteIfExists(file);
    }
}
