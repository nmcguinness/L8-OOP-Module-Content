package t09_interface.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("t09 ex08 - a pipeline built from a list of filters")
class ExerciseTest {

    @Test
    void trimFilter_removesSurroundingWhitespaceOnly() {
        assertEquals("a b", new TrimFilter().apply("   a b   "));
    }

    @Test
    void lowercaseFilter_lowercasesEverything() {
        assertEquals("mixed case", new LowercaseFilter().apply("Mixed CASE"));
    }

    @Test
    void regexReplacementFilter_replacesEveryMatch() {
        TextFilter f = new RegexReplacementFilter("\\d", "#");
        assertEquals("a#b#", f.apply("a1b2"));
    }

    @Test
    void applyAll_runsTheFiltersInOrder() {
        List<TextFilter> filters = List.of(new TrimFilter(), new LowercaseFilter());
        assertEquals("hello", TextFilters.applyAll(filters, "   HELLO   "));
    }

    @Test
    void applyAll_orderMatters_lowercaseBeforeReplacementChangesTheResult() {
        // Uppercase A survives a lowercase-only pattern unless it is lowercased first.
        List<TextFilter> lowercaseFirst =
                List.of(new LowercaseFilter(), new RegexReplacementFilter("a", "_"));
        List<TextFilter> replaceFirst =
                List.of(new RegexReplacementFilter("a", "_"), new LowercaseFilter());

        assertEquals("_bc", TextFilters.applyAll(lowercaseFirst, "Abc"));
        assertEquals("abc", TextFilters.applyAll(replaceFirst, "Abc"));
    }

    @Test
    void applyAll_emptyFilterList_returnsTheInputUnchanged() {
        assertEquals("  Untouched  ", TextFilters.applyAll(List.of(), "  Untouched  "));
    }

    @Test
    void filters_passNullStraightThrough() {
        assertNull(new TrimFilter().apply(null));
        assertNull(new LowercaseFilter().apply(null));
        assertNull(new RegexReplacementFilter("a", "b").apply(null));
        assertNull(TextFilters.applyAll(List.of(new TrimFilter()), null));
    }
}
