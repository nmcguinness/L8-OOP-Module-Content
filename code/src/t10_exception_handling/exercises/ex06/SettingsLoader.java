package t10_exception_handling.exercises.ex06;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reads "key=value" lines. Blank lines and lines starting with # are ignored.
 * Every failure reaches the caller as one checked type, ConfigException.
 */
public final class SettingsLoader {

    private SettingsLoader() { }

    public static Map<String, String> load(Path file) throws ConfigException {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        Map<String, String> settings = new LinkedHashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                parseLine(line.trim(), lineNumber, settings);
            }
        } catch (IOException e) {
            // Translate the low-level exception, and pass it on as the cause
            throw new ConfigException("Cannot read settings file " + file, e);
        }
        return settings;
    }

    private static void parseLine(String line, int lineNumber, Map<String, String> settings)
            throws ConfigException {
        if (line.isEmpty() || line.startsWith("#")) {
            return;
        }

        int equals = line.indexOf('=');
        if (equals <= 0) {
            throw new ConfigException("Expected key=value but found '" + line + "'", lineNumber);
        }

        String key = line.substring(0, equals).trim();
        String value = line.substring(equals + 1).trim();
        if (settings.containsKey(key)) {
            throw new ConfigException("Duplicate key '" + key + "'", lineNumber);
        }

        settings.put(key, value);
    }
}
