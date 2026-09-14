package t19_concurrency.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t19 e03 - the race condition on a shared counter")
class ExerciseTest {

    private static final int TASKS = 5_000;

    private static void runConcurrently(Runnable increment) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        for (int i = 0; i < TASKS; i++) {
            pool.submit(increment);
        }
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS), "pool did not finish in time");
    }

    @Test
    void synchronizedCounter_isExactUnderConcurrency() {
        SynchronizedDeliveryCounter safe = new SynchronizedDeliveryCounter();
        assertEquals(0, safe.getTotal());
    }

    @Test
    void synchronizedCounter_after5000ConcurrentIncrements_totalIsExactly5000()
            throws InterruptedException {
        SynchronizedDeliveryCounter safe = new SynchronizedDeliveryCounter();
        runConcurrently(safe::increment);
        assertEquals(TASKS, safe.getTotal());
    }

    @Test
    void unsafeCounter_singleThreaded_isCorrect() {
        // Without concurrency there is no interleaving, so ++ is perfectly fine.
        DeliveryCounter counter = new DeliveryCounter();
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }
        assertEquals(1000, counter.getTotal());
    }

    @Test
    void unsafeCounter_concurrently_canOnlyEverUndercount() throws InterruptedException {
        // A lost update discards an increment, so the total can fall short but never
        // exceed. Asserting that it *does* lose one would be a flaky test - whether
        // the race is observed depends on the machine and the scheduler.
        DeliveryCounter unsafe = new DeliveryCounter();
        runConcurrently(unsafe::increment);

        int total = unsafe.getTotal();
        assertTrue(total <= TASKS, "an unsynchronized counter can never overcount, got " + total);
        assertTrue(total > 0, "some increments must have landed, got " + total);
    }

    @Test
    void counters_startAtZero() {
        assertEquals(0, new DeliveryCounter().getTotal());
        assertEquals(0, new SynchronizedDeliveryCounter().getTotal());
    }
}
