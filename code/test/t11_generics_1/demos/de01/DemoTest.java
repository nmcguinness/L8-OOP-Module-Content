package t11_generics_1.demos.de01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t11 de01 - the Object container this topic exists to replace")
class DemoTest {

    @Test
    void value_returnsObjectSoTheCallerMustCast() {
        BoxObject box = new BoxObject("hello");
        Object raw = box.value();
        assertEquals("hello", (String) raw);
    }

    @Test
    void wrongCast_compilesButFailsAtRuntime() {
        // This is the crash Box<T> makes impossible: nothing here is caught at
        // compile time, because value() is declared as Object.
        BoxObject box = new BoxObject("hello");
        assertThrows(ClassCastException.class, () -> {
            Integer wrong = (Integer) box.value();
            assertEquals(0, wrong);
        });
    }

    @Test
    void aSingleBoxObject_willHoldAnythingAtAll() {
        // No type parameter means no constraint, so unrelated types share one class.
        assertEquals("text", new BoxObject("text").value());
        assertEquals(42, new BoxObject(42).value());
    }
}
