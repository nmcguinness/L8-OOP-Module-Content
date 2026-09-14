package t19_concurrency.exercises.ex01;

public class Exercise {

    public static void run() throws Exception {
        Thread t1 = new Thread(new DeliveryTask("ORD-001", "Galway", 3));
        Thread t2 = new Thread(new DeliveryTask("ORD-002", "Limerick", 2));

        t1.start();
        t2.start();

        System.out.println("Dispatcher: both threads started");
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
