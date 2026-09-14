package t11_generics_1.demos.de05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("t11 de05 - a bounded type parameter, T extends Comparable<T>")
class DemoTest {

    @Test
    void max_strings_usesAlphabeticalOrder() {
        assertEquals("Z", Demo.max(List.of("A", "B", "C", "Z", "D")));
    }

    @Test
    void max_integers_usesNumericOrder() {
        // The bound is what lets max() call compareTo at all.
        assertEquals(20, Demo.max(List.of(10, 20, 5, 12)));
    }

    @Test
    void max_maximumAtTheStart_isStillFound() {
        assertEquals(99, Demo.max(List.of(99, 5, 12)));
    }

    @Test
    void max_singleElement_returnsIt() {
        assertEquals(7, Demo.max(List.of(7)));
    }

    @Test
    void max_duplicatesOfTheMaximum_returnThatValue() {
        assertEquals(9, Demo.max(List.of(9, 3, 9)));
    }

    @Test
    void max_emptyOrNull_returnsNull() {
        // An empty list gives the compiler nothing to infer T from, and T is bound
        // to Comparable<T>, so the type witness Demo.<String>max(...) is required.
        assertNull(Demo.<String>max(List.of()));
        assertNull(Demo.<String>max(null));
    }
}
