package t11_generics_1.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t11 ex07 - MessageBus")
class ExerciseTest {

    @Test
    void dequeue_returnsMessagesFirstInFirstOut() {
        MessageBus<String> bus = new MessageBus<>();
        bus.enqueue("A");
        bus.enqueue("B");

        assertEquals("A", bus.dequeue());
        assertEquals("B", bus.dequeue());
    }

    @Test
    void emptyBus_dequeueReturnsNull() {
        MessageBus<Integer> bus = new MessageBus<>();

        assertTrue(bus.isEmpty());
        assertNull(bus.dequeue());
    }
}
