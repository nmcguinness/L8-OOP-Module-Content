package t11_generics_1.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t11 ex06 - Registry")
class ExerciseTest {

    @Test
    void putThenGet_returnsTheValue() {
        Registry<Integer, String> names = new Registry<>();
        names.put(7, "Ana");

        assertEquals("Ana", names.get(7));
        assertTrue(names.containsKey(7));
    }

    @Test
    void get_missingKey_returnsNull() {
        assertNull(new Registry<String, Integer>().get("nobody"));
    }

    @Test
    void nullKey_isRejectedByEveryMethod() {
        Registry<String, Integer> registry = new Registry<>();

        assertThrows(NullPointerException.class, () -> registry.put(null, 1));
        assertThrows(NullPointerException.class, () -> registry.get(null));
        assertThrows(NullPointerException.class, () -> registry.containsKey(null));
    }
}
