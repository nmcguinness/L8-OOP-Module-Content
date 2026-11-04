package t10_exception_handling.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t10 ex06 - exception translation and chaining")
class SettingsLoaderTest {

    @TempDir
    Path dir;

    private Path write(String content) throws IOException {
        Path file = dir.resolve("settings.txt");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void load_validFile_skipsCommentsAndBlankLines() throws Exception {
        Path file = write("# audio\nvolume = 80\n\ndifficulty=hard\n");

        assertEquals(Map.of("volume", "80", "difficulty", "hard"), SettingsLoader.load(file));
    }

    @Test
    void load_missingFile_throwsConfigExceptionWithIOExceptionCause() {
        Path missing = dir.resolve("missing.txt");

        ConfigException e = assertThrows(ConfigException.class, () -> SettingsLoader.load(missing));

        assertInstanceOf(IOException.class, e.getCause());
        assertEquals(0, e.getLineNumber());
    }

    @Test
    void load_lineWithoutEquals_reportsLineNumber() throws IOException {
        Path file = write("volume=80\n# fine\nthis line is wrong\n");

        ConfigException e = assertThrows(ConfigException.class, () -> SettingsLoader.load(file));

        assertEquals(3, e.getLineNumber());
        assertNull(e.getCause());
    }

    @Test
    void load_duplicateKey_isRejected() throws IOException {
        Path file = write("volume=80\nvolume=20\n");

        ConfigException e = assertThrows(ConfigException.class, () -> SettingsLoader.load(file));

        assertEquals(2, e.getLineNumber());
    }

    @Test
    void loadOrDefaults_badFile_fallsBackToDefaults() throws IOException {
        Path file = write("=no key\n");

        assertEquals("normal", Exercise.loadOrDefaults(file).get("difficulty"));
    }
}
