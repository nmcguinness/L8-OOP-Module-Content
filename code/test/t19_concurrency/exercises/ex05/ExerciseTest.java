package t19_concurrency.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t19 e05 - the full dispatch simulation")
class ExerciseTest {

    private static final double TOLERANCE = 1e-9;

    @Test
    void constructor_rejectsBlankIdBlankDestinationAndNonPositiveCost() {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryJob("  ", "Cork", 45.0));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryJob("O1", "  ", 45.0));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryJob("O1", "Cork", 0.0));
    }

    @Test
    void call_returnsASuccessfulResultCarryingTheJobDetails() throws Exception {
        DeliveryResult r = new DeliveryJob("ORD-001", "Cork", 45.00).call();

        assertEquals("ORD-001", r.orderId());
        assertEquals("Cork", r.destination());
        assertEquals(45.00, r.cost(), TOLERANCE);
        assertTrue(r.success());
    }

    @Test
    void pool_returnsResultsInSubmissionOrderRegardlessOfCompletionOrder() throws Exception {
        List<DeliveryJob> jobs = List.of(
                new DeliveryJob("ORD-001", "Cork", 45.00),
                new DeliveryJob("ORD-002", "Dublin", 62.50),
                new DeliveryJob("ORD-003", "Galway", 38.75));

        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<Future<DeliveryResult>> futures = new ArrayList<>();
        for (DeliveryJob job : jobs) {
            futures.add(pool.submit(job));
        }
        pool.shutdown();
        assertTrue(pool.awaitTermination(15, TimeUnit.SECONDS));

        List<String> ids = new ArrayList<>();
        for (Future<DeliveryResult> f : futures) {
            ids.add(f.get().orderId());
        }

        // The jobs may finish in any order, but the Future list preserves order.
        assertEquals(List.of("ORD-001", "ORD-002", "ORD-003"), ids);
    }

    @Test
    void summary_totalsAcrossAllResultsAreCorrect() throws Exception {
        List<DeliveryJob> jobs = List.of(
                new DeliveryJob("ORD-001", "Cork", 45.00),
                new DeliveryJob("ORD-002", "Dublin", 62.50));

        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<Future<DeliveryResult>> futures = new ArrayList<>();
        for (DeliveryJob job : jobs) {
            futures.add(pool.submit(job));
        }
        pool.shutdown();
        assertTrue(pool.awaitTermination(15, TimeUnit.SECONDS));

        int successes = 0;
        double revenue = 0.0;
        for (Future<DeliveryResult> f : futures) {
            DeliveryResult r = f.get();
            if (r.success()) {
                successes++;
            }
            revenue += r.cost();
        }

        assertEquals(2, successes);
        assertEquals(107.50, revenue, TOLERANCE);
    }

    @Test
    void deliveryResult_isAPlainCarrierOfTheFourValues() {
        DeliveryResult r = new DeliveryResult("ORD-9", "Sligo", 10.0, false);
        assertEquals("ORD-9", r.orderId());
        assertEquals("Sligo", r.destination());
        assertEquals(10.0, r.cost(), TOLERANCE);
        assertEquals(false, r.success());
    }
}
