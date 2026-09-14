package t09_interface.exercises.ex09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 ex09 - file-backed profanity filter")
class ExerciseTest {

    @Test
    void apply_redactsAKnownWordKeepingTheFirstLetter() {
        ProfanityFilter filter = new ProfanityFilter(List.of("darn"));
        assertEquals("well d***", filter.apply("well darn"));
    }

    @Test
    void apply_isCaseInsensitive() {
        ProfanityFilter filter = new ProfanityFilter(List.of("darn"));
        assertEquals("d***", filter.apply("DARN"));
    }

    @Test
    void apply_leavesCleanTextAlone() {
        ProfanityFilter filter = new ProfanityFilter(List.of("darn"));
        assertEquals("a clean sentence", filter.apply("a clean sentence"));
    }

    @Test
    void apply_blankAndNullEntriesInTheListAreSkipped() {
        ProfanityFilter filter = new ProfanityFilter(java.util.Arrays.asList(null, "  ", "darn"));
        assertEquals("d***", filter.apply("darn"));
    }

    @Test
    void apply_null_returnsNull() {
        assertNull(new ProfanityFilter(List.of("darn")).apply(null));
    }

    @Test
    void loadFromCsv_readsOneWordPerLineAndSkipsBlanks(@TempDir Path dir) throws IOException {
        Path csv = dir.resolve("words.csv");
        Files.writeString(csv, "darn\n\n  blast  \n");

        List<String> words = ProfanityLoader.loadFromCsv(csv.toString());

        assertEquals(List.of("darn", "blast"), words);
    }

    @Test
    void loadFromCsv_missingFile_returnsEmptyListRatherThanThrowing() {
        List<String> words = ProfanityLoader.loadFromCsv("no-such-file-12345.csv");
        assertTrue(words.isEmpty());
    }

    @Test
    void loadedWords_feedTheFilter(@TempDir Path dir) throws IOException {
        Path csv = dir.resolve("words.csv");
        Files.writeString(csv, "darn\nblast\n");

        ProfanityFilter filter = new ProfanityFilter(ProfanityLoader.loadFromCsv(csv.toString()));

        assertEquals("d*** and b****", filter.apply("darn and blast"));
    }
}
