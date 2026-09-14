package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileUtils {

    /**
     * Reads a UTF-8 text file and splits by the given single-character delimiter.
     * Newlines are ignored. Returns {@code null} on I/O error.
     */
    public static ArrayList<String> readDelimitedFile(String filePath, char delimiter) {

        //check the file path is valid
        if (filePath == null) throw new IllegalArgumentException("filePath is null");

        ArrayList<String> result = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        //read each character and stop when you hit the delimiter (i.e. a comma)
        try (BufferedReader br = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            int ch;
            while ((ch = br.read()) != -1) {
                char c = (char) ch;
                if (c == '\n' || c == '\r') continue;
                if (c == delimiter) {
                    result.add(token.toString().trim());
                    token.setLength(0);
                } else {
                    token.append(c);
                }
            }
            result.add(token.toString().trim());
            return result;
        } catch (IOException e) {
            return null;
        }
    }
    // Example usage
    public static void main(String[] args) {
        ArrayList<String> plates
                = readDelimitedFile("plates_10k.txt", ',');

        if(plates == null)
            return;

        if(plates.isEmpty())
            return;

        System.out.println("Read " + plates.size() + " entries.");
        System.out.println("First 5: " + plates.subList(0, Math.min(5, plates.size())));

    }
}
