package t09_interface.exercises.ex09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ProfanityLoader {

    private ProfanityLoader() {
    }

    public static List<String> loadFromCsv(String fileName) {
        List<String> result = new ArrayList<>();
        Path path = Path.of(fileName);

        try {
            for (String line : Files.readAllLines(path)) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    result.add(trimmed);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load blacklist: " + e.getMessage());
        }

        return result;
    }
}
