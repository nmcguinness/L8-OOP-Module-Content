package t19_concurrency.exercises.ex04;

import java.util.concurrent.*;

public class Exercise {

    public static void run() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Double> f1 = pool.submit(new CostEstimateTask("ORD-001", 2.50, 45));
        Future<Double> f2 = pool.submit(new CostEstimateTask("ORD-002", 3.00, 29));

        System.out.println("Main thread: estimates submitted");

        double cost1 = f1.get();
        double cost2 = f2.get();

        System.out.printf("ORD-001 cost estimate: \u20ac%.2f%n", cost1);
        System.out.printf("ORD-002 cost estimate: \u20ac%.2f%n", cost2);

        pool.shutdown();
    }
}

class CostEstimateTask implements Callable<Double> {

    private String _orderId;
    private double _baseRate;
    private int _distance;

    public CostEstimateTask(String orderId, double baseRate, int distance) {
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("orderId is required");
        if (baseRate <= 0)
            throw new IllegalArgumentException("baseRate must be > 0");
        if (distance < 1)
            throw new IllegalArgumentException("distance must be >= 1");

        _orderId  = orderId;
        _baseRate = baseRate;
        _distance = distance;
    }

    @Override
    public Double call() throws InterruptedException {
        Thread.sleep(500);
        System.out.println("[" + Thread.currentThread().getName() + "] Estimating " + _orderId + "...");
        return _baseRate * _distance;
    }
}
