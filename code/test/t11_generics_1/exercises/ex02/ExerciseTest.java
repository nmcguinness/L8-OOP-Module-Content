package t11_generics_1.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("t11 ex02 - UndoStack")
class ExerciseTest {

    @Test
    void pop_returnsItemsLastInFirstOut() {
        UndoStack<String> stack = new UndoStack<>();
        stack.push("a");
        stack.push("b");

        assertEquals("b", stack.pop());
        assertEquals("a", stack.pop());
    }

    @Test
    void peek_doesNotRemove() {
        UndoStack<Integer> stack = new UndoStack<>();
        stack.push(1);

        assertEquals(1, stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    void emptyStack_popAndPeekReturnNull() {
        UndoStack<String> stack = new UndoStack<>();

        assertNull(stack.pop());
        assertNull(stack.peek());
    }
}
