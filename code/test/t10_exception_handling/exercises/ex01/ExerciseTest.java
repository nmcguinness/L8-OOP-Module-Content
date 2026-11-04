package t10_exception_handling.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t10 ex01 - safe integer parsing with Optional")
class ExerciseTest {

    @Test
    void parseIntSafe_validNumber_returnsValue() {
        assertEquals(Optional.of(42), Exercise.parseIntSafe("42"));
        assertEquals(Optional.of(-7), Exercise.parseIntSafe("-7"));
    }

    @Test
    void parseIntSafe_surroundingWhitespace_isTrimmed() {
        assertEquals(Optional.of(5), Exercise.parseIntSafe("  5 "));
    }

    @Test
    void parseIntSafe_notANumber_returnsEmpty() {
        assertEquals(Optional.empty(), Exercise.parseIntSafe("abc"));
        assertEquals(Optional.empty(), Exercise.parseIntSafe(""));
    }

    @Test
    void parseIntSafe_tooLargeForInt_returnsEmpty() {
        assertEquals(Optional.empty(), Exercise.parseIntSafe("99999999999"));
    }

    @Test
    void parseIntSafe_null_returnsEmpty() {
        assertEquals(Optional.empty(), Exercise.parseIntSafe(null));
    }
}
