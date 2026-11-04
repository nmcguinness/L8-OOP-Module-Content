package t19_concurrency.exercises.ex02;

import java.util.concurrent.*;

public class Exercise {

    public static void run() throws InterruptedException {
        Dispatcher dispatcher = new Dispatcher();

        dispatcher.dispatch(new DeliveryTask("ORD-001", "Cork",     2));
        dispatcher.dispatch(new DeliveryTask("ORD-002", "Dublin",   1));
        dispatcher.dispatch(new DeliveryTask("ORD-003", "Sligo",    3));
        dispatcher.dispatch(new DeliveryTask("ORD-004", "Galway",   2));
        dispatcher.dispatch(new DeliveryTask("ORD-005", "Limerick", 1));

        dispatcher.shutdown();
        System.out.println("All deliveries complete");
    }
}

class Dispatcher {

    private final ExecutorService pool;

    public Dispatcher() {
        pool = Executors.newCachedThreadPool();
    }

    public void dispatch(DeliveryTask task) {
        if (task == null)
            throw new IllegalArgumentException("task is required");
        pool.submit(task);
    }

    public void shutdown() throws InterruptedException {
        pool.shutdown();
        if (!pool.awaitTermination(10, TimeUnit.SECONDS))
            pool.shutdownNow();
    }
}

class DeliveryTask implements Runnable {

    private final String orderId;
    private final String destination;
    private final int steps;

    public DeliveryTask(String orderId, String destination, int steps) {
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("orderId is required");
        if (destination == null || destination.isBlank())
            throw new IllegalArgumentException("destination is required");
        if (steps < 1)
            throw new IllegalArgumentException("steps must be >= 1");

        this.orderId     = orderId;
        this.destination = destination;
        this.steps       = steps;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();

        for (int i = 1; i <= steps; i++) {
            System.out.println("[" + name + "] Order " + orderId
                + ": step " + i + "/" + steps + " \u2192 " + destination);

            try {
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("[" + name + "] Order " + orderId
            + ": DELIVERED to " + destination);
    }
}
