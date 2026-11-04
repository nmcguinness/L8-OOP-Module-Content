package t10_exception_handling.exercises.ex04;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Exercise {

    // Propagates IOException: only the caller knows what a missing file should mean
    public static List<String> readLines(String path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static void run() {
        // Paths are relative to the working directory, which is code/ when run from Main
        String[] paths = {"data/exercises/profane_words.csv", "no/such/file.txt"};
        for (String p : paths) {
            try {
                System.out.println(p + ": " + readLines(p).size() + " lines");
            } catch (IOException e) {
                System.out.println(p + ": could not read ("
                        + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
            }
        }
    }
}
