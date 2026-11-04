package t19_concurrency.exercises.ex05;

import java.util.*;
import java.util.concurrent.*;

public class Exercise {

    public static void run() throws InterruptedException, ExecutionException {
        new DispatchSimulation().run();
    }
}

class DispatchSimulation {

    public void run() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        List<DeliveryJob> jobs = List.of(
            new DeliveryJob("ORD-001", "Cork",     45.00),
            new DeliveryJob("ORD-002", "Dublin",   62.50),
            new DeliveryJob("ORD-003", "Galway",   38.75),
            new DeliveryJob("ORD-004", "Limerick", 51.00),
            new DeliveryJob("ORD-005", "Sligo",    55.25),
            new DeliveryJob("ORD-006", "Kilkenny", 45.00)
        );

        List<Future<DeliveryResult>> futures = new ArrayList<>();
        for (DeliveryJob job : jobs)
            futures.add(pool.submit(job));

        pool.shutdown();
        pool.awaitTermination(15, TimeUnit.SECONDS);

        List<DeliveryResult> results = new ArrayList<>();
        for (Future<DeliveryResult> f : futures)
            results.add(f.get());

        int successes  = 0;
        double revenue = 0.0;

        for (DeliveryResult r : results) {
            String status = r.success() ? "[OK]" : "[FAIL]";
            System.out.printf("%-10s \u2192 %-12s \u20ac%-8.2f %s%n",
                r.orderId(), r.destination(), r.cost(), status);

            if (r.success()) successes++;
            revenue += r.cost();
        }

        System.out.println("--- Summary ---");
        System.out.printf("Jobs: %d  |  Successes: %d  |  Revenue: \u20ac%.2f%n",
            results.size(), successes, revenue);
    }
}

class DeliveryJob implements Callable<DeliveryResult> {

    private final String orderId;
    private final String destination;
    private final double cost;

    public DeliveryJob(String orderId, String destination, double cost) {
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("orderId is required");
        if (destination == null || destination.isBlank())
            throw new IllegalArgumentException("destination is required");
        if (cost <= 0)
            throw new IllegalArgumentException("cost must be > 0");

        this.orderId     = orderId;
        this.destination = destination;
        this.cost        = cost;
    }

    @Override
    public DeliveryResult call() throws InterruptedException {
        Thread.sleep(300);
        System.out.println("[" + Thread.currentThread().getName() + "] Completing "
            + orderId + " \u2192 " + destination);
        return new DeliveryResult(orderId, destination, cost, true);
    }
}

class DeliveryResult {

    private final String orderId;
    private final String destination;
    private final double cost;
    private final boolean success;

    public DeliveryResult(String orderId, String destination, double cost, boolean success) {
        this.orderId     = orderId;
        this.destination = destination;
        this.cost        = cost;
        this.success     = success;
    }

    public String orderId()     { return orderId; }
    public String destination() { return destination; }
    public double cost()        { return cost; }
    public boolean success()    { return success; }
}
