package t01_arrays.exercises.ex11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t01 ex11 - steps data wrangling")
class ExerciseTest {

    @Test
    void parseSteps_commaSeparatedValues_trimsAndParsesEach() {
        assertArrayEquals(new int[]{12034, 9876, 0}, Exercise.parseSteps("12034, 9876, 0"));
    }

    @Test
    void parseSteps_emptyField_becomesZero() {
        assertArrayEquals(new int[]{1, 0, 3}, Exercise.parseSteps("1, ,3"));
    }

    @Test
    void parseSteps_nullOrBlank_returnsEmptyArray() {
        assertEquals(0, Exercise.parseSteps(null).length);
        assertEquals(0, Exercise.parseSteps("   ").length);
    }

    @Test
    void parseSteps_nonNumericField_throwsNumberFormat() {
        assertThrows(NumberFormatException.class, () -> Exercise.parseSteps("1,abc,3"));
    }

    @Test
    void summarize_typicalData_reportsMinMaxAndZeroCount() {
        String s = Exercise.summarize(new int[]{12034, 9876, 0, 4321, 4321});
        assertTrue(s.startsWith("min=0 max=12034"), "was: " + s);
        assertTrue(s.endsWith("zeros=1"), "was: " + s);
    }

    @Test
    void summarize_emptyOrNull_returnsTheZeroSummary() {
        assertEquals("min=0 max=0 avg=0.0 zeros=0", Exercise.summarize(new int[]{}));
        assertEquals("min=0 max=0 avg=0.0 zeros=0", Exercise.summarize(null));
    }
}
