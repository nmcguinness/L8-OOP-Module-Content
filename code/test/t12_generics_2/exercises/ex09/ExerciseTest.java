package t12_generics_2.exercises.ex09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t12 ex09 - wildcard capture")
class ExerciseTest {

    @Test
    void swapFirstTwo_strings_swapsThem() {
        ArrayList<String> items = new ArrayList<>(List.of("A", "B"));
        Swaps.swapFirstTwo(items);
        assertEquals(List.of("B", "A"), items);
    }

    @Test
    void swapFirstTwo_integers_swapsThem() {
        ArrayList<Integer> items = new ArrayList<>(List.of(10, 20));
        Swaps.swapFirstTwo(items);
        assertEquals(List.of(20, 10), items);
    }

    @Test
    void swapFirstTwo_longerList_leavesTheTailUntouched() {
        ArrayList<String> items = new ArrayList<>(List.of("A", "B", "C", "D"));
        Swaps.swapFirstTwo(items);
        assertEquals(List.of("B", "A", "C", "D"), items);
    }

    @Test
    void swapFirstTwo_singleElement_isANoOp() {
        ArrayList<String> items = new ArrayList<>(List.of("only"));
        Swaps.swapFirstTwo(items);
        assertEquals(List.of("only"), items);
    }

    @Test
    void swapFirstTwo_emptyOrNull_doesNotThrow() {
        assertDoesNotThrow(() -> Swaps.swapFirstTwo(new ArrayList<String>()));
        assertDoesNotThrow(() -> Swaps.swapFirstTwo(null));
    }
}
