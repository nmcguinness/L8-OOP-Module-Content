package t01_arrays.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t01 ex04 - grade histogram")
class ExerciseTest {

    @Test
    void histogram_tenBins_returnsBinCountPlusOneSlots() {
        // The extra slot holds the single score of exactly 100
        assertEquals(11, Exercise.histogram(new int[]{50}, 10).length);
    }

    @Test
    void histogram_scoresInDifferentDecades_countsEachInItsOwnBin() {
        int[] bins = Exercise.histogram(new int[]{7, 12, 15, 19, 33, 66}, 10);
        assertEquals(1, bins[0], "one score in 0-9");
        assertEquals(3, bins[1], "three scores in 10-19");
        assertEquals(1, bins[3], "one score in 30-39");
        assertEquals(1, bins[6], "one score in 60-69");
    }

    @Test
    void histogram_scoreOfExactly100_goesInTheFinalBin() {
        int[] bins = Exercise.histogram(new int[]{100}, 10);
        assertEquals(1, bins[10]);
    }

    @Test
    void histogram_outOfRangeScores_areIgnored() {
        int[] bins = Exercise.histogram(new int[]{-5, 150, 50}, 10);
        int total = 0;
        for (int b : bins) total += b;
        assertEquals(1, total, "only the in-range score should be counted");
    }

    @Test
    void histogram_nullScores_throwsNullPointer() {
        assertThrows(NullPointerException.class, () -> Exercise.histogram(null, 10));
    }

    @Test
    void histogram_emptyScores_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.histogram(new int[]{}, 10));
    }

    @Test
    void histogram_zeroBins_throwsArithmetic() {
        assertThrows(ArithmeticException.class, () -> Exercise.histogram(new int[]{50}, 0));
    }
}
