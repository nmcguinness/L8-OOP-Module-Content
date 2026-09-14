package t19_concurrency.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t19 e04 - Callable and Future return a value")
class ExerciseTest {

    private static final double TOLERANCE = 1e-9;

    @Test
    void constructor_blankOrNullOrderId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CostEstimateTask(null, 2.5, 10));
        assertThrows(IllegalArgumentException.class, () -> new CostEstimateTask("  ", 2.5, 10));
    }

    @Test
    void constructor_zeroOrNegativeBaseRate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CostEstimateTask("O1", 0.0, 10));
        assertThrows(IllegalArgumentException.class, () -> new CostEstimateTask("O1", -1.0, 10));
    }

    @Test
    void constructor_distanceBelowOne_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new CostEstimateTask("O1", 2.5, 0));
    }

    @Test
    void call_returnsBaseRateTimesDistance() throws Exception {
        assertEquals(112.5, new CostEstimateTask("ORD-001", 2.50, 45).call(), TOLERANCE);
    }

    @Test
    void future_get_returnsTheComputedValue() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<Double> f = pool.submit(new CostEstimateTask("ORD-002", 3.00, 29));
            assertEquals(87.0, f.get(), TOLERANCE);
            assertTrue(f.isDone(), "get() returning means the task has completed");
        } finally {
            pool.shutdown();
        }
    }

    @Test
    void future_get_waitsForTheResultRatherThanReturningEarly() throws Exception {
        // The task sleeps before returning, so a Future that did not block would
        // have no value to hand back.
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<Double> f = pool.submit(new CostEstimateTask("ORD-003", 1.0, 2));
            assertEquals(2.0, f.get(), TOLERANCE);
        } finally {
            pool.shutdown();
        }
    }

    @Test
    void future_get_onAFailingTask_throwsExecutionException() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        try {
            Future<Double> f = pool.submit(() -> {
                throw new IllegalStateException("estimate failed");
            });
            // A Callable's exception surfaces at get(), wrapped, not at submit().
            assertThrows(ExecutionException.class, f::get);
        } finally {
            pool.shutdown();
        }
    }
}
