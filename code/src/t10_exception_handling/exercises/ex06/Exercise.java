package t10_exception_handling.exercises.ex06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Exercise {

    private static final Map<String, String> DEFAULTS = Map.of("volume", "50", "difficulty", "normal");

    // The caller decides the recovery: here, fall back to defaults and say why
    static Map<String, String> loadOrDefaults(Path file) {
        try {
            return SettingsLoader.load(file);
        } catch (ConfigException e) {
            System.out.println("Using defaults. Reason: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("  caused by " + e.getCause().getClass().getSimpleName());
            }
            return DEFAULTS;
        }
    }

    public static void run() {
        try {
            Path good = Files.createTempFile("settings", ".txt");
            Files.writeString(good, "# audio\nvolume=80\ndifficulty = hard\n");

            Path bad = Files.createTempFile("settings", ".txt");
            Files.writeString(bad, "volume=80\nthis line is wrong\n");

            System.out.println(loadOrDefaults(good));
            System.out.println(loadOrDefaults(bad));
            System.out.println(loadOrDefaults(Path.of("missing-settings.txt")));

            Files.deleteIfExists(good);
            Files.deleteIfExists(bad);
        } catch (IOException e) {
            System.out.println("Could not create the demo files: " + e.getMessage());
        }
    }
}
