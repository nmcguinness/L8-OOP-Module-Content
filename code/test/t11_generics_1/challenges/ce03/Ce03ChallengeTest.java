package t11_generics_1.challenges.ce03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests for the t11 generics challenge (ce03). */
@DisplayName("t11 ce03 - Weapon, CSV loading and XML loading")
class Ce03ChallengeTest {

    // ---------- Weapon ----------

    @Test
    void equals_sameNameAndStrength_areEqual() {
        assertEquals(new Weapon("Axe", 10), new Weapon("Axe", 10));
    }

    @Test
    void equals_differingStrength_areNotEqual() {
        assertNotEquals(new Weapon("Axe", 10), new Weapon("Axe", 11));
    }

    @Test
    void equals_otherType_isFalseRatherThanThrowing() {
        assertNotEquals("Axe", new Weapon("Axe", 10));
    }

    @Test
    void hashCode_equalWeapons_collapseInAHashSet() {
        Set<Weapon> set = new HashSet<>();
        set.add(new Weapon("Axe", 10));
        set.add(new Weapon("Axe", 10));
        assertEquals(1, set.size());
    }

    @Test
    void toString_includesNameAndStrength() {
        String text = new Weapon("Axe", 10).toString();
        assertTrue(text.contains("Axe"), text);
        assertTrue(text.contains("10"), text);
    }

    // ---------- FileHelper: CSV ----------

    @Test
    void readStringsFromCsv_collectsEveryTokenNotJustTheFirstColumn(@TempDir Path dir)
            throws IOException {
        Path csv = dir.resolve("tokens.csv");
        Files.writeString(csv, "alpha,beta,gamma\ndelta,epsilon\n");

        List<String> values = FileHelper.readStringsFromCsv(csv, false);

        assertEquals(List.of("alpha", "beta", "gamma", "delta", "epsilon"), values);
    }

    @Test
    void readStringsFromCsv_withHeader_skipsTheFirstLine(@TempDir Path dir) throws IOException {
        Path csv = dir.resolve("tokens.csv");
        Files.writeString(csv, "name,other\nalpha,beta\n");

        assertEquals(List.of("alpha", "beta"), FileHelper.readStringsFromCsv(csv, true));
    }

    @Test
    void readStringsFromCsv_trimsTokensAndSkipsBlankLines(@TempDir Path dir) throws IOException {
        Path csv = dir.resolve("tokens.csv");
        Files.writeString(csv, "  alpha ,  beta  \n\n   \ngamma\n");

        assertEquals(List.of("alpha", "beta", "gamma"), FileHelper.readStringsFromCsv(csv, false));
    }

    @Test
    void readStringsFromCsv_missingFile_throwsIOException(@TempDir Path dir) {
        assertThrows(IOException.class,
                () -> FileHelper.readStringsFromCsv(dir.resolve("absent.csv"), false));
    }

    @Test
    void readStringsFromCsv_nullPath_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> FileHelper.readStringsFromCsv(null, false));
    }

    // ---------- FileHelper: XML ----------

    @Test
    void readWeaponsFromXml_readsEveryWeaponElement(@TempDir Path dir) throws Exception {
        Path xml = dir.resolve("weapons.xml");
        Files.writeString(xml, """
                <weapons>
                  <weapon><name>Axe</name><strength>10</strength></weapon>
                  <weapon><name>Bow</name><strength>7</strength></weapon>
                </weapons>
                """);

        List<Weapon> weapons = FileHelper.readWeaponsFromXml(xml);

        assertEquals(List.of(new Weapon("Axe", 10), new Weapon("Bow", 7)), weapons);
    }

    @Test
    void readWeaponsFromXml_blankName_throwsIllegalStateException(@TempDir Path dir)
            throws IOException {
        Path xml = dir.resolve("weapons.xml");
        Files.writeString(xml, """
                <weapons>
                  <weapon><name>  </name><strength>10</strength></weapon>
                </weapons>
                """);

        assertThrows(IllegalStateException.class, () -> FileHelper.readWeaponsFromXml(xml));
    }

    @Test
    void readWeaponsFromXml_nonNumericStrength_throwsIllegalStateException(@TempDir Path dir)
            throws IOException {
        Path xml = dir.resolve("weapons.xml");
        Files.writeString(xml, """
                <weapons>
                  <weapon><name>Axe</name><strength>heavy</strength></weapon>
                </weapons>
                """);

        assertThrows(IllegalStateException.class, () -> FileHelper.readWeaponsFromXml(xml));
    }

    @Test
    void readWeaponsFromXml_noWeaponElements_returnsEmptyList(@TempDir Path dir) throws Exception {
        Path xml = dir.resolve("weapons.xml");
        Files.writeString(xml, "<weapons></weapons>");

        assertTrue(FileHelper.readWeaponsFromXml(xml).isEmpty());
    }

    @Test
    void readWeaponsFromXml_missingFile_throwsIOException(@TempDir Path dir) {
        assertThrows(IOException.class,
                () -> FileHelper.readWeaponsFromXml(dir.resolve("absent.xml")));
    }
}
