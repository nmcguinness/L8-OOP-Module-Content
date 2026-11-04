package t18_io.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t18 ex06 - a config file loader")
class ConfigLoaderTest {

    @TempDir
    Path dir;

    private ConfigLoader load(String content) throws IOException {
        Path file = dir.resolve("game.properties");
        Files.writeString(file, content);
        return new ConfigLoader(file);
    }

    @Test
    void asMap_skipsCommentsBlankAndMalformedLines_andTrims() throws IOException {
        ConfigLoader config = load("# settings\n\nmaxPlayers = 8\nnot a setting\nmapName=forest\n");

        assertEquals(Map.of("maxPlayers", "8", "mapName", "forest"), config.asMap());
    }

    @Test
    void getInt_number_missingKey_andNonNumber() throws IOException {
        ConfigLoader config = load("maxPlayers=8\nvolume=loud\n");

        assertEquals(8, config.getInt("maxPlayers", 4));
        assertEquals(1, config.getInt("difficulty", 1));
        assertEquals(50, config.getInt("volume", 50));
    }

    @Test
    void get_returnsOptional() throws IOException {
        ConfigLoader config = load("mapName=forest\n");

        assertEquals(Optional.of("forest"), config.get("mapName"));
        assertEquals(Optional.empty(), config.get("missing"));
    }

    @Test
    void constructor_missingFile_throwsIOException() {
        assertThrows(IOException.class, () -> new ConfigLoader(dir.resolve("missing.properties")));
    }
}
