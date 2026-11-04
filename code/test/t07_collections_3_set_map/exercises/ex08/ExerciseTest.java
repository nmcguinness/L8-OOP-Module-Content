package t07_collections_3_set_map.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t07 ex08 - grade bands with TreeMap.floorEntry")
class ExerciseTest {

    @Test
    void band_exactBoundary_returnsThatBand() {
        assertEquals("D", Exercise.band(40));
        assertEquals("A", Exercise.band(85));
    }

    @Test
    void band_justBelowBoundary_returnsLowerBand() {
        assertEquals("F", Exercise.band(39));
        assertEquals("B", Exercise.band(84));
    }

    @Test
    void band_extremes_returnFAndA() {
        assertEquals("F", Exercise.band(0));
        assertEquals("A", Exercise.band(100));
    }

    @Test
    void band_outOfRange_throws() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.band(-1));
        assertThrows(IllegalArgumentException.class, () -> Exercise.band(101));
    }
}
