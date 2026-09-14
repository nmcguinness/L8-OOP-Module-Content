package t19_concurrency.exercises.ex02;

import java.util.concurrent.*;

public class Exercise {

    public static void run() throws Exception {
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

    private ExecutorService _pool;

    public Dispatcher() {
        _pool = Executors.newCachedThreadPool();
    }

    public void dispatch(DeliveryTask task) {
        if (task == null)
            throw new IllegalArgumentException("task is required");
        _pool.submit(task);
    }

    public void shutdown() throws InterruptedException {
        _pool.shutdown();
        if (!_pool.awaitTermination(10, TimeUnit.SECONDS))
            _pool.shutdownNow();
    }
}

class DeliveryTask implements Runnable {

    private String _orderId;
    private String _destination;
    private int _steps;

    public DeliveryTask(String orderId, String destination, int steps) {
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("orderId is required");
        if (destination == null || destination.isBlank())
            throw new IllegalArgumentException("destination is required");
        if (steps < 1)
            throw new IllegalArgumentException("steps must be >= 1");

        _orderId     = orderId;
        _destination = destination;
        _steps       = steps;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();

        for (int i = 1; i <= _steps; i++) {
            System.out.println("[" + name + "] Order " + _orderId
                + ": step " + i + "/" + _steps + " \u2192 " + _destination);

            try {
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("[" + name + "] Order " + _orderId
            + ": DELIVERED to " + _destination);
    }
}
