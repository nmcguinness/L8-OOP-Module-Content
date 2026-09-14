package t09_interface.challenges.ce02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for reading contacts from CSV and writing HTML.
 *
 * CSV header:
 *   id,name,email,phone,department,country,company,follow_up,turnover_k
 */
public final class ContactUtility {

    private ContactUtility() { }

    public static List<Contact> readContactsCsv(String strPath) {
        return readContactsCsv(Path.of(strPath));
    }
    public static List<Contact> readContactsCsv(Path csvPath) {
        List<Contact> contacts = new ArrayList<Contact>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String header = reader.readLine(); // skip header
            if (header == null) {
                return contacts;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] cols = line.split(",", -1);
                if (cols.length < 9) {
                    continue;
                }

                String id = cols[0];
                String name = cols[1];
                String email = cols[2];
                String phone = cols[3];
                String department = cols[4];
                String country = cols[5];
                String company = cols[6];
                String followUpRaw = cols[7];
                String turnoverRaw = cols[8];

                boolean followUp = "true".equalsIgnoreCase(followUpRaw.trim());
                int turnoverK = parseIntSafe(turnoverRaw);

                Contact c = new Contact(
                    id,
                    name,
                    email,
                    phone,
                    department,
                    country,
                    company,
                    followUp,
                    turnoverK
                );
                contacts.add(c);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV " + csvPath + ": " + e.getMessage());
        }

        return contacts;
    }

    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static void writeHtml(String strPath, String htmlContent) {
        writeHtml(Path.of(strPath), htmlContent);
    }
    public static void writeHtml(Path outputPath, String htmlContent) {
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write(htmlContent);
        } catch (IOException e) {
            System.err.println("Error writing HTML to " + outputPath + ": " + e.getMessage());
        }
    }
}
