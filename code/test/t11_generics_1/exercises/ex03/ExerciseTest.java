package t11_generics_1.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t11 ex03 - generic map")
class ExerciseTest {

    @Test
    void map_transformsEachItemInOrder() {
        assertEquals(List.of(3, 5), Maps.map(List.of("ant", "zebra"), String::length));
    }

    @Test
    void map_nullItems_givesEmptyList() {
        assertTrue(Maps.map(null, (String s) -> s).isEmpty());
    }

    @Test
    void map_nullTransform_throws() {
        assertThrows(NullPointerException.class, () -> Maps.map(List.of(1), null));
    }
}
