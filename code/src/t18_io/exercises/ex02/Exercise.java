package t18_io.exercises.ex02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Exercise {

    // Writes one name per line, replacing anything already in the file
    public static void writeNames(Path path, List<String> names) throws IOException {
        if (names == null) {
            throw new IllegalArgumentException("names must not be null");
        }
        Files.write(path, names);
    }

    public static void run() {
        Path good = Path.of("names.txt");
        Path bad = Path.of("no-such-folder", "names.txt");

        for (Path target : List.of(good, bad)) {
            try {
                writeNames(target, List.of("Ana", "Ben", "Cara"));
                System.out.println("Wrote " + target.toAbsolutePath());
            } catch (IOException e) {
                // The caller decides what a failure means: here, report it and carry on
                System.out.println("Could not write " + target + ": "
                        + e.getClass().getSimpleName());
            }
        }

        try {
            Files.deleteIfExists(good);
        } catch (IOException e) {
            System.out.println("Could not tidy up: " + e.getMessage());
        }
    }
}
