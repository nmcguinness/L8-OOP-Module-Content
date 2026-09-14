package t20_json_1_jackson_basics.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t20 ex01 - binary file round trip")
class ExerciseTest {

    private static byte[] synthetic() {
        byte[] data = new byte[256];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        return data;
    }

    @Test
    void writeThenRead_returnsTheSameBytes(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("asset.bin");
        byte[] original = synthetic();

        BinaryFileUtil.writeFile(file.toString(), original);

        assertArrayEquals(original, BinaryFileUtil.readFile(file.toString()));
    }

    @Test
    void roundTrip_preservesEveryByteValueIncludingNegatives(@TempDir Path dir) throws Exception {
        // Bytes above 127 are negative in Java; a broken copy usually mangles these.
        Path a = dir.resolve("a.bin");
        Path b = dir.resolve("b.bin");

        BinaryFileUtil.writeFile(a.toString(), synthetic());
        byte[] first = BinaryFileUtil.readFile(a.toString());
        BinaryFileUtil.writeFile(b.toString(), first);

        assertTrue(Arrays.equals(first, BinaryFileUtil.readFile(b.toString())));
        assertEquals((byte) 255, first[255]);
    }

    @Test
    void writeFile_createsMissingParentDirectories(@TempDir Path dir) throws Exception {
        Path nested = dir.resolve("deep/nested/asset.bin");

        BinaryFileUtil.writeFile(nested.toString(), new byte[] {1, 2, 3});

        assertTrue(Files.exists(nested));
    }

    @Test
    void writeFile_emptyArray_writesAnEmptyFile(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("empty.bin");
        BinaryFileUtil.writeFile(file.toString(), new byte[0]);
        assertEquals(0, BinaryFileUtil.readFile(file.toString()).length);
    }

    @Test
    void writeFile_overwritesAnExistingFile(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("asset.bin");
        BinaryFileUtil.writeFile(file.toString(), new byte[] {1, 2, 3});
        BinaryFileUtil.writeFile(file.toString(), new byte[] {9});

        assertArrayEquals(new byte[] {9}, BinaryFileUtil.readFile(file.toString()));
    }

    @Test
    void readFile_missingFile_throwsIllegalArgumentException(@TempDir Path dir) {
        assertThrows(IllegalArgumentException.class,
                () -> BinaryFileUtil.readFile(dir.resolve("absent.bin").toString()));
    }

    @Test
    void readFile_nullOrBlankPath_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> BinaryFileUtil.readFile(null));
        assertThrows(IllegalArgumentException.class, () -> BinaryFileUtil.readFile("   "));
    }

    @Test
    void writeFile_nullPathOrNullData_throwsIllegalArgumentException(@TempDir Path dir) {
        assertThrows(IllegalArgumentException.class,
                () -> BinaryFileUtil.writeFile(null, new byte[] {1}));
        assertThrows(IllegalArgumentException.class,
                () -> BinaryFileUtil.writeFile(dir.resolve("x.bin").toString(), null));
    }
}
