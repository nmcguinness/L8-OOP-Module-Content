package t12_generics_2.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t12 ex05 - PECS in one signature")
class ExerciseTest {

    @Test
    void copy_intoANumberList_copiesEveryElementInOrder() {
        ArrayList<Number> dst = new ArrayList<>();
        Copier.copy(List.of(1, 2, 3), dst);
        assertEquals(List.of(1, 2, 3), dst);
    }

    @Test
    void copy_intoAnObjectList_alsoWorks() {
        // src produces (extends), dst consumes (super) - the same call serves both.
        ArrayList<Object> dst = new ArrayList<>();
        Copier.copy(List.of(1, 2, 3), dst);
        assertEquals(List.of(1, 2, 3), dst);
    }

    @Test
    void copy_appendsToAnAlreadyPopulatedDestination() {
        ArrayList<Object> dst = new ArrayList<>(List.of("keep"));
        Copier.copy(List.of(1), dst);
        assertEquals(List.of("keep", 1), dst);
    }

    @Test
    void copy_emptySource_leavesTheDestinationUnchanged() {
        ArrayList<Object> dst = new ArrayList<>(List.of("keep"));
        Copier.copy(List.of(), dst);
        assertEquals(List.of("keep"), dst);
    }

    @Test
    void copy_nullSourceOrDestination_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Copier.copy(null, new ArrayList<>()));
        assertThrows(NullPointerException.class, () -> Copier.copy(List.of(1), null));
    }
}
