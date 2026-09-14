package t20_json_1_jackson_basics.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t20 ex03 - GameAsset validation and normalisation")
class ExerciseTest {

    private static final byte[] DATA = {1, 2, 3};

    private static GameAsset asset() {
        return new GameAsset(1, "Hero Sprite", "PNG", 3, DATA);
    }

    @Test
    void constructor_validArguments_storesEveryField() {
        GameAsset a = asset();
        assertEquals(1, a.getAssetId());
        assertEquals("Hero Sprite", a.getAssetName());
        assertEquals(3, a.getFileSize());
        assertArrayEquals(DATA, a.getAssetData());
    }

    @Test
    void constructor_lowercasesTheAssetType() {
        assertEquals("png", asset().getAssetType());
    }

    @Test
    void constructor_trimsNameAndType() {
        GameAsset a = new GameAsset(1, "  Hero  ", "  PNG  ", 3, DATA);
        assertEquals("Hero", a.getAssetName());
        assertEquals("png", a.getAssetType());
    }

    @Test
    void constructor_zeroIdAndZeroFileSize_areAccepted() {
        // The guards reject negatives, not zero.
        GameAsset a = new GameAsset(0, "n", "t", 0, DATA);
        assertEquals(0, a.getAssetId());
        assertEquals(0, a.getFileSize());
    }

    @Test
    void constructor_negativeId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameAsset(-1, "n", "t", 1, DATA));
    }

    @Test
    void constructor_negativeFileSize_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameAsset(1, "n", "t", -1, DATA));
    }

    @Test
    void constructor_blankNameOrType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameAsset(1, "   ", "t", 1, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> new GameAsset(1, "n", "   ", 1, DATA));
    }

    @Test
    void constructor_nullNameOrType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameAsset(1, null, "t", 1, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> new GameAsset(1, "n", null, 1, DATA));
    }

    @Test
    void constructor_nullOrEmptyData_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameAsset(1, "n", "t", 1, null));
        assertThrows(IllegalArgumentException.class,
                () -> new GameAsset(1, "n", "t", 1, new byte[0]));
    }

    @Test
    void fileSize_isNotDerivedFromTheDataArray() {
        // Worth knowing: nothing checks that fileSize matches assetData.length.
        GameAsset a = new GameAsset(1, "n", "t", 9999, DATA);
        assertEquals(9999, a.getFileSize());
        assertEquals(3, a.getAssetData().length);
    }
}
