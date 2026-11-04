package t15_dao.exercises;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Database settings, read from a file that is kept out of Git.
 */
public record DbConfig(String url, String user, String password) {

    /** Relative to the folder the program runs in, which is {@code code}. */
    public static final Path DEFAULT_FILE = Path.of("config", "db.properties");

    public static DbConfig load(Path file) throws IOException {
        Properties props = new Properties();
        try (Reader in = Files.newBufferedReader(file)) {
            props.load(in);
        }
        return new DbConfig(
                require(props, "db.url"),
                require(props, "db.user"),
                props.getProperty("db.password", ""));
    }

    private static String require(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing setting '" + key + "' in db.properties");
        }
        return value.trim();
    }
}
