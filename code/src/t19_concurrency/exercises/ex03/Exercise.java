package t19_concurrency.exercises.ex03;

import java.util.concurrent.*;

public class Exercise {

    public static void run() throws Exception {

        DeliveryCounter unsafe = new DeliveryCounter();
        SynchronizedDeliveryCounter safe = new SynchronizedDeliveryCounter();

        int tasks = 5_000;

        ExecutorService poolA = Executors.newFixedThreadPool(8);
        for (int i = 0; i < tasks; i++)
            poolA.submit(unsafe::increment);
        poolA.shutdown();
        poolA.awaitTermination(10, TimeUnit.SECONDS);

        ExecutorService poolB = Executors.newFixedThreadPool(8);
        for (int i = 0; i < tasks; i++)
            poolB.submit(safe::increment);
        poolB.shutdown();
        poolB.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Unsafe total   (expected " + tasks + "): " + unsafe.getTotal());
        System.out.println("Safe total     (expected " + tasks + "): " + safe.getTotal());
    }
}

// NOT thread-safe: ++ is read-modify-write - two threads can read the same value,
// both add 1, and write back the same result, losing one increment.
class DeliveryCounter {

    private int _total = 0;

    public void increment() {
        _total++;
    }

    public int getTotal() {
        return _total;
    }
}

class SynchronizedDeliveryCounter {

    private int _total = 0;

    public synchronized void increment() {
        _total++;
    }

    public synchronized int getTotal() {
        return _total;
    }
}
