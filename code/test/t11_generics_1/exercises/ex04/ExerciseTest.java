package t11_generics_1.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t11 ex04 - generic where")
class ExerciseTest {

    @Test
    void where_keepsMatchesInOrder() {
        assertEquals(List.of(11, 12), Filters.where(List.of(5, 11, 12, 3), n -> n > 10));
    }

    @Test
    void where_leavesTheOriginalListUnchanged() {
        List<String> names = new ArrayList<>(List.of("Amy", "Bob"));
        Filters.where(names, s -> s.startsWith("A"));

        assertEquals(List.of("Amy", "Bob"), names);
    }

    @Test
    void where_nullItems_givesEmptyList() {
        assertTrue(Filters.where(null, (String s) -> true).isEmpty());
    }

    @Test
    void where_nullPredicate_throws() {
        assertThrows(NullPointerException.class, () -> Filters.where(List.of(1), null));
    }
}
