package t18_io.exercises.ex06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Loads key=value settings. Blank lines, lines starting with # and lines with no
 * '=' are ignored. Keys and values are trimmed.
 */
public class ConfigLoader {

    private final Map<String, String> settings = new LinkedHashMap<>();

    public ConfigLoader(Path file) throws IOException {
        for (String raw : Files.readAllLines(file)) {
            String line = raw.trim();
            int equals = line.indexOf('=');
            if (line.isEmpty() || line.startsWith("#") || equals <= 0) {
                continue;
            }
            settings.put(line.substring(0, equals).trim(), line.substring(equals + 1).trim());
        }
    }

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(settings);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(settings.get(key));
    }

    // The value as an int, or defaultValue if the key is missing or not a whole number
    public int getInt(String key, int defaultValue) {
        String value = settings.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
