package t05_collections_1.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t05 ex03 - remove evens safely")
class ExerciseTest {

    @Test
    void removeEvens_mixedList_leavesOnlyOddNumbers() {
        ArrayList<Integer> xs = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        Exercise.removeEvens(xs);
        assertEquals(List.of(1, 3, 5), xs);
    }

    @Test
    void removeEvens_consecutiveEvens_removesAllOfThem() {
        // The classic bug: removing by index without adjusting skips the element
        // that shifts left, so consecutive evens survive.
        ArrayList<Integer> xs = new ArrayList<>(List.of(2, 4, 6, 7));
        Exercise.removeEvens(xs);
        assertEquals(List.of(7), xs);
    }

    @Test
    void removeEvens_allEven_emptiesTheList() {
        ArrayList<Integer> xs = new ArrayList<>(List.of(2, 4, 6));
        Exercise.removeEvens(xs);
        assertTrue(xs.isEmpty());
    }

    @Test
    void removeEvens_allOdd_leavesListUnchanged() {
        ArrayList<Integer> xs = new ArrayList<>(List.of(1, 3, 5));
        Exercise.removeEvens(xs);
        assertEquals(List.of(1, 3, 5), xs);
    }

    @Test
    void removeEvens_emptyList_staysEmpty() {
        ArrayList<Integer> xs = new ArrayList<>();
        Exercise.removeEvens(xs);
        assertTrue(xs.isEmpty());
    }
}
