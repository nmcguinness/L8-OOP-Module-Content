package t13_design_patterns_1.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t13 ex03 - Command wrapping a Strategy")
class ExerciseTest {

    /** The strategies report through System.out, so that is the observable behaviour. */
    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void safeExecution_validatesBeforeRunning() {
        Command safe = new ExecuteTaskCommand(new Task("Export"), new SafeExecution());
        String out = capture(safe::execute);

        assertTrue(out.indexOf("Validating: Export") >= 0, out);
        assertTrue(out.indexOf("Running: Export") >= 0, out);
        assertTrue(out.indexOf("Validating: Export") < out.indexOf("Running: Export"),
                "validation must come first: " + out);
    }

    @Test
    void fastExecution_skipsValidation() {
        Command fast = new ExecuteTaskCommand(new Task("Export"), new FastExecution());
        String out = capture(fast::execute);

        assertTrue(out.contains("Running unchecked: Export"), out);
        assertFalse(out.contains("Validating"), "fast execution must not validate: " + out);
    }

    @Test
    void sameTaskTwoStrategies_produceDifferentBehaviour() {
        Task task = new Task("Export");

        String fast = capture(new ExecuteTaskCommand(task, new FastExecution())::execute);
        String safe = capture(new ExecuteTaskCommand(task, new SafeExecution())::execute);

        // The command and the task are identical; only the strategy differs.
        assertEquals(1, fast.lines().count());
        assertEquals(2, safe.lines().count());
    }

    @Test
    void command_doesNotRunUntilExecuteIsCalled() {
        Task task = new Task("Export");
        String atConstruction = capture(() -> new ExecuteTaskCommand(task, new SafeExecution()));
        assertEquals("", atConstruction, "building a command must not perform the work");
    }
}
