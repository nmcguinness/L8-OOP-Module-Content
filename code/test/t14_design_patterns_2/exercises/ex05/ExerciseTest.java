package t14_design_patterns_2.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t14 e05 - Adapter over a legacy logger")
class ExerciseTest {

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
    void log_forwardsToTheLegacyMethodName() {
        Logger logger = new LegacyLoggerAdapter(new LegacyLogger());
        String out = capture(() -> logger.log("Server started"));
        assertTrue(out.contains("LEGACY: Server started"), out);
    }

    @Test
    void log_callsTheLegacyLoggerOncePerCall() {
        Logger logger = new LegacyLoggerAdapter(new LegacyLogger());
        String out = capture(() -> {
            logger.log("one");
            logger.log("two");
        });
        assertEquals(2, out.lines().count());
    }

    @Test
    void adapter_isUsableWhereverTheLoggerInterfaceIsExpected() {
        // The calling code never mentions LegacyLogger.
        Logger logger = new LegacyLoggerAdapter(new LegacyLogger());
        assertTrue(logger instanceof Logger);
    }

    @Test
    void constructor_null_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new LegacyLoggerAdapter(null));
    }

    @Test
    void log_passesTheMessageThroughUnchanged() {
        Logger logger = new LegacyLoggerAdapter(new LegacyLogger());
        String out = capture(() -> logger.log(""));
        assertTrue(out.startsWith("LEGACY: "), out);
    }
}
