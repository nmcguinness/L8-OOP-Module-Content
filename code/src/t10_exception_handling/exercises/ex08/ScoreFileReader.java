package t10_exception_handling.exercises.ex08;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The refactored version of the broken code on the exercise sheet. Each fix is
 * labelled with the anti-pattern it removes.
 */
public final class ScoreFileReader {

    private ScoreFileReader() { }

    // Fix 1 (swallowing): failures are reported to the caller, never hidden behind a 0
    public static int totalScore(Path file) throws ScoreFileException {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        // Fix 2 (manual close in finally): try-with-resources closes the reader
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            int total = 0;
            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.isBlank()) {
                    total += parseScore(line.trim(), lineNumber);
                }
            }
            return total;
        } catch (IOException e) {
            // Fix 3 (losing the cause): the IOException is passed on as the cause
            throw new ScoreFileException("Cannot read " + file, 0, e);
        }
        // Fix 4 (over-broad catch): no catch (Exception e), so real bugs are not hidden
    }

    private static int parseScore(String text, int lineNumber) throws ScoreFileException {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new ScoreFileException("Not a whole number: '" + text + "'", lineNumber, e);
        }
    }
}
