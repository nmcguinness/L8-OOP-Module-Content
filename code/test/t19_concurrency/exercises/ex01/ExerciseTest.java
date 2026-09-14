package t19_concurrency.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t19 e01 - DeliveryTask as a Runnable")
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
    void constructor_blankOrNullOrderId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask(null, "Cork", 1));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask("  ", "Cork", 1));
    }

    @Test
    void constructor_blankOrNullDestination_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask("ORD-1", null, 1));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask("ORD-1", "  ", 1));
    }

    @Test
    void constructor_zeroOrNegativeSteps_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask("ORD-1", "Cork", 0));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask("ORD-1", "Cork", -1));
    }

    @Test
    void run_reportsEveryStepThenDelivery() {
        // Called directly, so this runs on the test thread - no scheduling involved.
        String out = capture(new DeliveryTask("ORD-1", "Cork", 2));

        assertTrue(out.contains("step 1/2"), out);
        assertTrue(out.contains("step 2/2"), out);
        assertTrue(out.contains("DELIVERED to Cork"), out);
    }

    @Test
    void run_deliveryLineComesLast() {
        String out = capture(new DeliveryTask("ORD-1", "Cork", 1));
        assertTrue(out.indexOf("step 1/1") < out.indexOf("DELIVERED"), out);
    }

    @Test
    void runOnAThread_completesBeforeJoinReturns() throws Exception {
        // join() is the guarantee: after it returns, the task has finished.
        DeliveryTask task = new DeliveryTask("ORD-1", "Cork", 1);
        StringBuilder log = new StringBuilder();

        Thread t = new Thread(() -> log.append(capture(task)));
        t.start();
        t.join();

        assertTrue(log.toString().contains("DELIVERED"), log.toString());
        assertEquals(Thread.State.TERMINATED, t.getState());
    }
}
