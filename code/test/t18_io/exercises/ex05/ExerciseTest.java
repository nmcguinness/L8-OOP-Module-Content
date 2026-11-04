package t18_io.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t18 ex05 - copy a binary file")
class ExerciseTest {

    @TempDir
    Path dir;

    @Test
    void copyBinary_everyByteValue_isCopiedExactly() throws IOException {
        byte[] original = new byte[256];
        for (int i = 0; i < original.length; i++) {
            original[i] = (byte) i;
        }
        Path source = dir.resolve("source.bin");
        Files.write(source, original);

        Path copy = dir.resolve("copy.bin");
        Exercise.copyBinary(source, copy);

        assertArrayEquals(original, Files.readAllBytes(copy));
    }

    @Test
    void copyBinary_missingSource_throwsIOException() {
        assertThrows(IOException.class,
                () -> Exercise.copyBinary(dir.resolve("missing.bin"), dir.resolve("copy.bin")));
    }
}
