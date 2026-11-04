package t18_io.exercises.ex04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static final int FIELDS = 3;

    // One String[] per data row (name, score, level); the header and blank lines are skipped
    public static List<String[]> parseCsv(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<String[]> rows = new ArrayList<>();

        // Start at 1: line 0 is the header
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            String[] fields = line.split(",");
            if (fields.length != FIELDS) {
                throw new IllegalArgumentException("Line " + (i + 1) + ": expected " + FIELDS
                        + " fields but found " + fields.length);
            }
            for (int f = 0; f < fields.length; f++) {
                fields[f] = fields[f].trim();
            }
            rows.add(fields);
        }
        return rows;
    }

    // Only the rows whose score (column 1) is greater than minScore
    public static List<String[]> scoresAbove(List<String[]> rows, int minScore) {
        List<String[]> result = new ArrayList<>();
        for (String[] row : rows) {
            if (Integer.parseInt(row[1]) > minScore) {
                result.add(row);
            }
        }
        return result;
    }

    public static void run() throws IOException {
        Path file = Files.createTempFile("scores", ".csv");
        Files.writeString(file, "name,score,level\nAlice,95,3\nBen,82,2\nClara,78,1\n");

        List<String[]> rows = parseCsv(file);
        System.out.println("Rows: " + rows.size());
        for (String[] row : scoresAbove(rows, 80)) {
            System.out.println(String.join(" | ", row));
        }
        Files.deleteIfExists(file);
    }
}
