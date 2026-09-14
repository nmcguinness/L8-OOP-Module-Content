package t20_json_1_jackson_basics.exercises.ex01;

import java.nio.file.*;

public class BinaryFileUtil {

    // === Public API ===
    // Reads: a file from disk and returns its bytes
    public static byte[] readFile(String path) throws Exception {
        if (path == null || path.isBlank())
            throw new IllegalArgumentException("path is required");

        Path p = Path.of(path);
        if (!Files.exists(p))
            throw new IllegalArgumentException("file not found: " + path);

        return Files.readAllBytes(p);
    }

    // Writes: a byte array to disk at the given path
    public static void writeFile(String path, byte[] data) throws Exception {
        if (path == null || path.isBlank())
            throw new IllegalArgumentException("path is required");
        if (data == null)
            throw new IllegalArgumentException("data is required");

        Path p = Path.of(path);
        if (p.getParent() != null)
            Files.createDirectories(p.getParent());

        Files.write(p, data);
    }
}
