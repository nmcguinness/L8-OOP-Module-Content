package t15_dao.exercises;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t15 DbConfig - settings from a properties file")
class DbConfigTest {

    @TempDir
    Path dir;

    private Path write(String content) throws IOException {
        Path file = dir.resolve("db.properties");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void load_allSettings_readsThem() throws IOException {
        DbConfig config = DbConfig.load(write(
                "db.url=jdbc:mysql://localhost:3306/car_rental\ndb.user=root\ndb.password=secret\n"));

        assertEquals("jdbc:mysql://localhost:3306/car_rental", config.url());
        assertEquals("root", config.user());
        assertEquals("secret", config.password());
    }

    @Test
    void load_noPassword_givesEmptyPassword() throws IOException {
        DbConfig config = DbConfig.load(write("db.url=jdbc:mysql://x\ndb.user=root\n"));

        assertEquals("", config.password());
    }

    @Test
    void load_missingRequiredSetting_namesTheSetting() throws IOException {
        Path file = write("db.user=root\n");

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> DbConfig.load(file));
        assertTrue(e.getMessage().contains("db.url"), e.getMessage());
    }

    @Test
    void load_missingFile_throwsIOException() {
        assertThrows(IOException.class, () -> DbConfig.load(dir.resolve("missing.properties")));
    }
}
