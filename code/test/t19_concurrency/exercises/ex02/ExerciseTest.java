package t19_concurrency.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t19 e02 - dispatching through an ExecutorService")
class ExerciseTest {

    @Test
    void dispatch_null_throwsIllegalArgumentException() throws Exception {
        Dispatcher dispatcher = new Dispatcher();
        try {
            assertThrows(IllegalArgumentException.class, () -> dispatcher.dispatch(null));
        } finally {
            dispatcher.shutdown();
        }
    }

    @Test
    void deliveryTask_constructorGuardsStillApply() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask(null, "Cork", 1));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask("O1", "  ", 1));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTask("O1", "Cork", 0));
    }

    @Test
    void shutdown_returnsOnlyAfterEverySubmittedTaskHasFinished() throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        try {
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.dispatch(new DeliveryTask("ORD-001", "Cork", 1));
            dispatcher.dispatch(new DeliveryTask("ORD-002", "Dublin", 1));
            dispatcher.shutdown();
        } finally {
            System.setOut(original);
        }

        // awaitTermination inside shutdown() is what makes this deterministic.
        String out = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("ORD-001: DELIVERED to Cork"), out);
        assertTrue(out.contains("ORD-002: DELIVERED to Dublin"), out);
    }

    @Test
    void dispatcher_runsTasksWithoutBlockingTheCaller() throws Exception {
        // dispatch() hands the task to the pool and returns immediately; the work
        // is only guaranteed complete once shutdown() has returned.
        Dispatcher dispatcher = new Dispatcher();
        long before = System.nanoTime();
        dispatcher.dispatch(new DeliveryTask("ORD-003", "Sligo", 3));
        long elapsedMillis = (System.nanoTime() - before) / 1_000_000;

        assertTrue(elapsedMillis < 500,
                "dispatch() should not wait for the 3 sleeping steps, took " + elapsedMillis + "ms");

        dispatcher.shutdown();
    }
}
