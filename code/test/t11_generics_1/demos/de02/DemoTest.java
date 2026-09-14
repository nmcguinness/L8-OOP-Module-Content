package t11_generics_1.demos.de02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("t11 de02 - Box<T> returns its element type without a cast")
class DemoTest {

    @Test
    void value_stringBox_comesBackAsAString() {
        Box<String> box = new Box<>("hello");
        // No cast, and no ClassCastException is possible here.
        String value = box.value();
        assertEquals("hello", value);
    }

    @Test
    void value_integerBox_comesBackAsAnInteger() {
        Box<Integer> box = new Box<>(42);
        int value = box.value();
        assertEquals(42, value);
    }

    @Test
    void value_holdsNull() {
        assertNull(new Box<String>(null).value());
    }

    @Test
    void box_ofBoxes_nestsWithoutLosingTypeInformation() {
        Box<Box<String>> outer = new Box<>(new Box<>("inner"));
        assertEquals("inner", outer.value().value());
    }
}
