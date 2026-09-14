package t13_design_patterns_1.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t13 ex05 - undoable commands and an undo stack")
class ExerciseTest {

    @Test
    void execute_addsTheAmountToTheCounter() {
        Counter counter = new Counter();
        new AddNumberCommand(counter, 5).execute();
        assertEquals(5, counter.getValue());
    }

    @Test
    void undo_reversesExactlyThatCommand() {
        Counter counter = new Counter();
        AddNumberCommand command = new AddNumberCommand(counter, 5);

        command.execute();
        command.undo();

        assertEquals(0, counter.getValue());
    }

    @Test
    void counter_startsAtZero() {
        assertEquals(0, new Counter().getValue());
    }

    @Test
    void undoStack_undoesInReverseOrder() {
        Counter counter = new Counter();
        ArrayDeque<UndoableCommand> history = new ArrayDeque<>();

        for (int amount : new int[] {5, 2, 10}) {
            UndoableCommand c = new AddNumberCommand(counter, amount);
            c.execute();
            history.push(c);
        }
        assertEquals(17, counter.getValue());

        history.pop().undo();       // undoes the +10
        assertEquals(7, counter.getValue());

        history.pop().undo();       // undoes the +2
        assertEquals(5, counter.getValue());
    }

    @Test
    void undoingEverything_returnsToTheStartingValue() {
        Counter counter = new Counter();
        ArrayDeque<UndoableCommand> history = new ArrayDeque<>();

        for (int amount : new int[] {3, -7, 20}) {
            UndoableCommand c = new AddNumberCommand(counter, amount);
            c.execute();
            history.push(c);
        }
        while (!history.isEmpty()) {
            history.pop().undo();
        }

        assertEquals(0, counter.getValue());
    }

    @Test
    void undoWithoutExecute_stillSubtracts() {
        // undo() is not guarded - it simply applies the inverse. Worth knowing.
        Counter counter = new Counter();
        new AddNumberCommand(counter, 5).undo();
        assertEquals(-5, counter.getValue());
    }
}
