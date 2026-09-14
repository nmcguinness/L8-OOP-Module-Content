package t11_generics_1.demos.de04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("t11 de04 - a generic method infers its type from the argument")
class DemoTest {

    @Test
    void first_stringList_returnsAString() {
        assertEquals("A", Demo.first(List.of("A", "B")));
    }

    @Test
    void first_integerList_returnsAnInteger() {
        assertEquals(10, Demo.first(List.of(10, 20)));
    }

    @Test
    void first_singleElement_returnsThatElement() {
        assertEquals("only", Demo.first(List.of("only")));
    }

    @Test
    void first_emptyList_returnsNull() {
        assertNull(Demo.first(List.of()));
    }

    @Test
    void first_null_returnsNullRatherThanThrowing() {
        assertNull(Demo.first(null));
    }
}
