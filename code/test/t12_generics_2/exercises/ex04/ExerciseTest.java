package t12_generics_2.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t12 ex04 - consumer method with ? super T")
class ExerciseTest {

    @Test
    void fill_objectList_appendsTheValueCountTimes() {
        ArrayList<Object> out = new ArrayList<>();
        Fillers.fill(out, "hi", 3);
        assertEquals(List.of("hi", "hi", "hi"), out);
    }

    @Test
    void fill_supertypeList_acceptsASubtypeValue() {
        // ? super T is what allows an Integer to be written into a List<Number>.
        ArrayList<Number> out = new ArrayList<>();
        Fillers.fill(out, 5, 2);
        assertEquals(List.of(5, 5), out);
    }

    @Test
    void fill_appendsRatherThanReplacing() {
        ArrayList<Object> out = new ArrayList<>(List.of("existing"));
        Fillers.fill(out, "x", 2);
        assertEquals(List.of("existing", "x", "x"), out);
    }

    @Test
    void fill_countOfZero_leavesTheListUntouched() {
        ArrayList<Object> out = new ArrayList<>();
        Fillers.fill(out, "x", 0);
        assertTrue(out.isEmpty());
    }

    @Test
    void fill_negativeCount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> Fillers.fill(new ArrayList<>(), "x", -1));
    }

    @Test
    void fill_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Fillers.fill(null, "x", 1));
    }
}
