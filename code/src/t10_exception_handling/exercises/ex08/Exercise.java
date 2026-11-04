package t10_exception_handling.exercises.ex08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Exercise {

    public static void run() {
        try {
            Path good = Files.createTempFile("scores", ".txt");
            Files.writeString(good, "10\n20\n\n30\n");
            Path bad = Files.createTempFile("scores", ".txt");
            Files.writeString(bad, "10\ntwenty\n30\n");

            report(good);
            report(bad);
            report(Path.of("missing-scores.txt"));

            Files.deleteIfExists(good);
            Files.deleteIfExists(bad);
        } catch (IOException e) {
            System.out.println("Could not create the demo files: " + e.getMessage());
        }
    }

    private static void report(Path file) {
        try {
            System.out.println("Total: " + ScoreFileReader.totalScore(file));
        } catch (ScoreFileException e) {
            System.out.println("Error: " + e.getMessage()
                    + " [cause: " + e.getCause().getClass().getSimpleName() + "]");
        }
    }
}
