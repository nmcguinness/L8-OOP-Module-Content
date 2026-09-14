package t03_ordering.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the natural order declared by Score.compareTo:
 * value descending, then player name ascending.
 */
@DisplayName("t03 ex01 - Score natural order")
class ScoreTest {

    @Test
    void compareTo_higherValue_sortsFirst() {
        assertTrue(new Score("Amy", 95).compareTo(new Score("Zara", 90)) < 0,
                   "a higher value must come first");
    }

    @Test
    void compareTo_equalValues_breaksTieByNameAscending() {
        assertTrue(new Score("Amy", 95).compareTo(new Score("Liam", 95)) < 0,
                   "on a tie, the earlier name comes first");
    }

    @Test
    void compareTo_sameValueAndName_returnsZero() {
        assertEquals(0, new Score("Amy", 95).compareTo(new Score("Amy", 95)));
    }

    @Test
    void compareTo_isAntisymmetric() {
        Score a = new Score("Amy", 95);
        Score b = new Score("Zara", 90);
        assertTrue(a.compareTo(b) < 0 && b.compareTo(a) > 0);
    }

    @Test
    void sort_usesNaturalOrder_valueDescThenNameAsc() {
        List<Score> scores = new ArrayList<>(List.of(
                new Score("Zara", 90),
                new Score("Amy", 95),
                new Score("Liam", 95),
                new Score("Noah", 70)));

        Collections.sort(scores);

        assertEquals(List.of("Amy", "Liam", "Zara", "Noah"),
                     scores.stream().map(Score::getPlayer).toList());
    }
}
