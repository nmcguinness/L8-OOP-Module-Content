package t12_generics_2.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t12 ex03 - producer method with ? extends Number")
class ExerciseTest {

    @Test
    void sumNumbers_integers_addsThemAsDoubles() {
        assertEquals(6.0, Numbers.sumNumbers(List.of(1, 2, 3)));
    }

    @Test
    void sumNumbers_doubles_addsThem() {
        assertEquals(4.0, Numbers.sumNumbers(List.of(0.5, 1.5, 2.0)));
    }

    @Test
    void sumNumbers_mixedNumericTypes_stillSums() {
        // The point of ? extends Number: one method serves every Number subtype.
        assertEquals(6.5, Numbers.sumNumbers(List.of(1, 2L, 3.5)));
    }

    @Test
    void sumNumbers_null_returnsZeroRatherThanThrowing() {
        assertEquals(0.0, Numbers.sumNumbers(null));
    }

    @Test
    void sumNumbers_emptyList_returnsZero() {
        assertEquals(0.0, Numbers.sumNumbers(List.of()));
    }
}
