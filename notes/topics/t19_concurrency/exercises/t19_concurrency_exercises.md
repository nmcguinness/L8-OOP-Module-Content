---
title: "Concurrency I — Threads and the ExecutorService — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t19_concurrency
description: "Five progressive exercises building a delivery dispatcher: Runnable, ExecutorService, a deliberate race condition, Callable and Future, and a full simulation."
created: 2026-02-20
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, concurrency, threads, executorservice, runnable, callable, synchronization, exercises, year2, comp-c8z03]
---
# Concurrency I — Threads and the ExecutorService — Exercises

These exercises reinforce **Concurrency I — Threads and the ExecutorService** by building a
**Delivery Dispatch** simulation, where a dispatcher assigns delivery orders to couriers
running on separate threads.

## Ground rules

- Never call `t.run()` directly — always `t.start()`.
- Always call `pool.shutdown()` after submitting all tasks.
- Handle `InterruptedException` by calling `Thread.currentThread().interrupt()` in the catch block.
- Print small outputs in `run()` so behaviour can be verified quickly.
- Keep business logic out of `run()` — put it in named methods or classes.

## How to run

Each exercise uses:

- **Folder:** `/exercises/topics/t19_concurrency/exercises/eXX/`
- **Filename:** `Exercise.java`
- **Package:** `t19_concurrency.exercises.eXX`

```java
package t19_concurrency.exercises.ex01;

public class Exercise {
    public static void run() throws Exception {
        // your code here
    }
}
```

From your `Main.java`, call:

```java
t19_concurrency.exercises.ex01.Exercise.run();
```

## Before you start

### Prerequisites checklist

- [ ] Completed the Concurrency I notes
- [ ] You understand interfaces and lambdas
- [ ] You can write defensive checks (null, fail fast)

### How to use these exercises

- Work **in order**. Each exercise builds on the previous.
- Let the output run — thread interleaving means order varies each time. That is expected.
- Solutions show one correct approach. Yours may differ and still be correct.

---

## Exercise 01 — First thread: `DeliveryTask` as `Runnable`

**Objective:** Implement a `DeliveryTask` that prints progress messages as it "delivers" an
order, then run two deliveries simultaneously using raw `Thread` objects.

**Context (software + games):**

- **Software dev:** Background tasks (email dispatch, report generation) use `Runnable` to
  avoid blocking the main thread.
- **Games dev:** NPC pathfinding, physics ticks, and audio streams often run on separate
  threads to keep the render loop responsive.

### What you are building

- A `DeliveryTask` class implementing `Runnable`
- Two threads running two deliveries at the same time
- Proof that the threads interleave (output from both appears mixed)

### Required API

```java
public class DeliveryTask implements Runnable {
    public DeliveryTask(String orderId, String destination, int steps);
    // run() prints one line per step then a completion message
}
```

### Tasks

1. Implement `DeliveryTask`:
   - Fields: `_orderId` (String), `_destination` (String), `_steps` (int)
   - Validate: `orderId` and `destination` required; `steps >= 1`
   - In `run()`: loop `_steps` times, printing:
     `"[thread-name] Order orderId: step N/steps → destination"`
     Then sleep 200ms between steps using `Thread.sleep(200)`.
   - After the loop print: `"[thread-name] Order orderId: DELIVERED to destination"`
2. In `Exercise.run()`:
   - Create two `DeliveryTask` instances (different IDs, destinations, step counts).
   - Wrap each in a `Thread` and call `start()` on both.
   - Print `"Dispatcher: both threads started"` immediately after starting them.

### Sample output

```text
Dispatcher: both threads started
[Thread-0] Order ORD-001: step 1/3 → Galway
[Thread-1] Order ORD-002: step 1/2 → Limerick
[Thread-0] Order ORD-001: step 2/3 → Galway
[Thread-1] Order ORD-002: step 2/2 → Limerick
[Thread-1] Order ORD-002: DELIVERED to Limerick
[Thread-0] Order ORD-001: step 3/3 → Galway
[Thread-0] Order ORD-001: DELIVERED to Galway
```

*(Output order varies — thread interleaving is non-deterministic.)*

### Constraints

- Do not call `t.run()` — use `t.start()`.
- The main thread must not `join()` or wait — it prints its message and continues.

### Done when...

- Both threads start and the "Dispatcher" message appears before both deliveries finish.
- You can explain why output order varies each run.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
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

    // === Fields ===
    private String _orderId;
    private String _destination;
    private int _steps;

    // === Constructors ===
    // Creates: a delivery task for the given order, destination, and step count
    public DeliveryTask(String orderId, String destination, int steps) {
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("orderId is required");
        if (destination == null || destination.isBlank())
            throw new IllegalArgumentException("destination is required");
        if (steps < 1)
            throw new IllegalArgumentException("steps must be >= 1");

        _orderId = orderId;
        _destination = destination;
        _steps = steps;
    }

    // === Public API ===
    // Runs: the delivery loop — prints progress then completion
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
```

</details>

---

## Exercise 02 — Dispatch with an `ExecutorService`

**Objective:** Replace raw `Thread` objects with an `ExecutorService`. A `Dispatcher` class
submits any number of deliveries to a cached thread pool, then shuts down cleanly.

**Context (software + games):**

- **Software dev:** Web servers, job queues, and report generators all use thread pools to
  handle variable workloads without creating a thread per task.
- **Games dev:** Match-making servers, multiplayer session managers, and physics sub-systems
  often delegate work to a pool to cap resource use.

### What you are building

- A `Dispatcher` class that owns an `ExecutorService`
- A `dispatch(DeliveryTask task)` method that submits tasks to the pool
- A `shutdown()` method that waits cleanly for all deliveries to finish

### Required API

```java
public class Dispatcher {
    public Dispatcher();
    public void dispatch(DeliveryTask task);
    public void shutdown() throws InterruptedException;
}
```

### Tasks

1. Implement `Dispatcher`:
   - Creates a `Executors.newCachedThreadPool()` in the constructor.
   - `dispatch(task)`: validates task is not null, then calls `_pool.submit(task)`.
   - `shutdown()`: calls `_pool.shutdown()`, then `awaitTermination(10, TimeUnit.SECONDS)`.
2. Reuse `DeliveryTask` from Exercise 01.
3. In `Exercise.run()`:
   - Create a `Dispatcher`.
   - Dispatch five `DeliveryTask` instances (vary IDs, destinations, step counts 1–3).
   - Call `dispatcher.shutdown()`.
   - Print `"All deliveries complete"` after shutdown returns.

### Sample output

```text
[pool-1-thread-1] Order ORD-001: step 1/2 → Cork
[pool-1-thread-2] Order ORD-002: step 1/1 → Dublin
[pool-1-thread-2] Order ORD-002: DELIVERED to Dublin
[pool-1-thread-3] Order ORD-003: step 1/3 → Sligo
... (interleaved)
All deliveries complete
```

### Constraints

- Do not create a `Thread` directly in this exercise — use the pool.
- `"All deliveries complete"` must print only after all tasks finish.

### Done when...

- All five deliveries complete before `"All deliveries complete"` prints.
- Thread names include `pool-1-thread-N` (from the executor).
- No raw `Thread` objects appear in `Exercise.run()`.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t19_concurrency.exercises.ex02;

import java.util.concurrent.*;

public class Exercise {

    public static void run() throws Exception {
        Dispatcher dispatcher = new Dispatcher();

        dispatcher.dispatch(new DeliveryTask("ORD-001", "Cork", 2));
        dispatcher.dispatch(new DeliveryTask("ORD-002", "Dublin", 1));
        dispatcher.dispatch(new DeliveryTask("ORD-003", "Sligo", 3));
        dispatcher.dispatch(new DeliveryTask("ORD-004", "Galway", 2));
        dispatcher.dispatch(new DeliveryTask("ORD-005", "Limerick",1));

        dispatcher.shutdown();
        System.out.println("All deliveries complete");
    }
}

class Dispatcher {

    // === Fields ===
    private ExecutorService _pool;

    // === Constructors ===
    // Creates: a dispatcher backed by a cached thread pool
    public Dispatcher() {
        _pool = Executors.newCachedThreadPool();
    }

    // === Public API ===
    // Submits: a delivery task to the pool
    public void dispatch(DeliveryTask task) {
        if (task == null)
            throw new IllegalArgumentException("task is required");
        _pool.submit(task);
    }

    // Shuts: the pool down and waits up to 10 seconds for all tasks to finish
    public void shutdown() throws InterruptedException {
        _pool.shutdown();
        if (!_pool.awaitTermination(10, TimeUnit.SECONDS))
            _pool.shutdownNow();
    }
}

class DeliveryTask implements Runnable {

    // === Fields ===
    private String _orderId;
    private String _destination;
    private int _steps;

    // Creates: a delivery task
    public DeliveryTask(String orderId, String destination, int steps) {
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("orderId is required");
        if (destination == null || destination.isBlank())
            throw new IllegalArgumentException("destination is required");
        if (steps < 1)
            throw new IllegalArgumentException("steps must be >= 1");

        _orderId = orderId;
        _destination = destination;
        _steps = steps;
    }

    // Runs: the delivery — prints progress and completion
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
```

</details>

---

## Exercise 03 — Race condition: shared delivery counter

**Objective:** Reproduce a race condition on a shared `DeliveryCounter`, observe incorrect
results, then fix it with `synchronized`.

**Context (software + games):**

- **Software dev:** Any shared counter (page views, request totals, active sessions) is
  vulnerable to race conditions without synchronisation.
- **Games dev:** Score trackers, kill counters, and leaderboard updaters shared between threads
  exhibit the same problem.

### What you are building

- An **unsafe** `DeliveryCounter` that loses increments under concurrent access
- A **safe** `SynchronizedDeliveryCounter` that fixes the problem
- A test harness that submits 5,000 simultaneous increments and compares results

### Required API

```java
public class DeliveryCounter {
    public void increment();
    public int getTotal();
}

public class SynchronizedDeliveryCounter {
    public synchronized void increment();
    public synchronized int getTotal();
}
```

### Tasks

1. Implement `DeliveryCounter` with a plain `int _total` field — **do not** synchronise anything.
2. Implement `SynchronizedDeliveryCounter` with `synchronized` on both methods.
3. In `Exercise.run()`:
   - Create both counters.
   - Use a `newFixedThreadPool(8)` for each.
   - Submit 5_000 `increment()` calls to the unsafe counter's pool.
   - Submit 5_000 `increment()` calls to the safe counter's pool.
   - Call `shutdown()` and `awaitTermination` on both pools.
   - Print both totals.
4. Add a comment explaining why the unsafe counter gives the wrong answer.

### Sample output

```text
Unsafe total   (expected 5000): 4873
Safe total     (expected 5000): 5000
```

*(Unsafe total varies per run and is almost always less than 5,000.)*

### Constraints

- Use exactly 5,000 submitted tasks for each counter.
- Do not add `synchronized` to `DeliveryCounter` — the point is to see the bug.

### Done when...

- The unsafe counter consistently prints a value less than 5,000 (run several times).
- The safe counter always prints exactly 5,000.
- You can explain the read-modify-write problem in one sentence.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
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

// NOT thread-safe: ++ is read-modify-write — two threads can read the same value,
// both add 1, and both write back the same result, losing one increment.
class DeliveryCounter {

    // === Fields ===
    private int _total = 0;

    // === Public API ===
    // Increments: the counter — NOT thread-safe
    public void increment() {
        _total++;
    }

    // Gets: the current total
    public int getTotal() {
        return _total;
    }
}

class SynchronizedDeliveryCounter {

    // === Fields ===
    private int _total = 0;

    // === Public API ===
    // Increments: the counter — thread-safe via synchronized
    public synchronized void increment() {
        _total++;
    }

    // Gets: the current total — thread-safe
    public synchronized int getTotal() {
        return _total;
    }
}
```

</details>

---

## Exercise 04 — `Callable` and `Future`: getting a result back

**Objective:** Use `Callable<T>` and `Future<T>` to run a delivery cost estimate in the
background and retrieve the result on the main thread.

**Context (software + games):**

- **Software dev:** Background jobs that compute results (price calculations, report
  generation, API calls) return values via `Future`.
- **Games dev:** Pathfinding algorithms, AI decision trees, and world-gen tasks run in the
  background and deliver results when ready.

### What you are building

- A `CostEstimateTask` implementing `Callable<Double>` that simulates a slow cost calculation
- A demo that submits two estimates simultaneously and collects both results

### Required API

```java
public class CostEstimateTask implements Callable<Double> {
    public CostEstimateTask(String orderId, double baseRate, int distance);
    // call() returns baseRate * distance after a simulated 500ms delay
}
```

### Tasks

1. Implement `CostEstimateTask`:
   - Fields: `_orderId` (String), `_baseRate` (double), `_distance` (int)
   - Validate: orderId required; baseRate > 0; distance >= 1
   - `call()`: sleeps 500ms, prints `"[thread-name] Estimating orderId..."`, returns `_baseRate
     - _distance`
2. In `Exercise.run()`:
   - Create a `newFixedThreadPool(2)`.
   - Submit two `CostEstimateTask` instances, capturing their `Future<Double>` references.
   - Print `"Main thread: estimates submitted"`.
   - Call `future.get()` on each and print the results.
   - Shut down the pool cleanly.

### Sample output

```text
Main thread: estimates submitted
[pool-1-thread-1] Estimating ORD-001...
[pool-1-thread-2] Estimating ORD-002...
ORD-001 cost estimate: €112.50
ORD-002 cost estimate: €87.00
```

### Constraints

- Both estimates must run concurrently (submit both before calling `get()` on either).
- Do not call `get()` on `future1` before submitting `future2`.

### Done when...

- Both tasks run simultaneously (the 500ms delays overlap).
- The main thread prints `"estimates submitted"` before the estimates complete.
- Both cost values are computed correctly.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
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

    // === Fields ===
    private String _orderId;
    private double _baseRate;
    private int _distance;

    // === Constructors ===
    // Creates: a cost estimate task for the given order
    public CostEstimateTask(String orderId, double baseRate, int distance) {
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("orderId is required");
        if (baseRate <= 0)
            throw new IllegalArgumentException("baseRate must be > 0");
        if (distance < 1)
            throw new IllegalArgumentException("distance must be >= 1");

        _orderId = orderId;
        _baseRate = baseRate;
        _distance = distance;
    }

    // === Public API ===
    // Calls: the cost calculation — simulates a slow remote call
    @Override
    public Double call() throws InterruptedException {
        Thread.sleep(500);
        System.out.println("[" + Thread.currentThread().getName() + "] Estimating " + _orderId + "...");
        return _baseRate * _distance;
    }
}
```

</details>

---

## Exercise 05 — Full dispatch simulation

**Objective:** Combine everything — `ExecutorService`, `Callable`, and a
`SynchronizedDeliveryCounter` — into a `DispatchSimulation` that runs a full batch of
deliveries, collects all results, and prints a summary.

**Context (software + games):**

- **Software dev:** End-to-end job pipelines (ingest then process then report) require
  coordination between pool submission, result collection, and aggregate reporting.
- **Games dev:** Match-result processing (simulate matches, collect outcomes, update standings)
  follows the same pattern.

### What you are building

- A `DeliveryJob` implementing `Callable<DeliveryResult>`
- A `DeliveryResult` value type (orderId, destination, cost, success flag)
- A `DispatchSimulation` that submits jobs, collects results, counts successes, and prints a summary

### Required API

```java
public class DeliveryResult {
    public DeliveryResult(String orderId, String destination, double cost, boolean success);
    public String orderId();
    public String destination();
    public double cost();
    public boolean success();
}

public class DeliveryJob implements Callable<DeliveryResult> {
    public DeliveryJob(String orderId, String destination, double cost);
    // call(): sleeps 300ms, returns a DeliveryResult with success=true
}

public class DispatchSimulation {
    public void run() throws Exception;
    // submits 6 jobs, collects results, prints per-job and summary
}
```

### Tasks

1. Implement `DeliveryResult` as a simple value class (fields + getters, no setters).
2. Implement `DeliveryJob`:
   - Sleeps 300ms in `call()`.
   - Prints `"[thread] Completing orderId → destination"`.
   - Returns a `DeliveryResult` with the given fields and `success=true`.
3. Implement `DispatchSimulation.run()`:
   - Creates a `newFixedThreadPool(3)`.
   - Submits 6 `DeliveryJob` instances with varied orders, destinations, and costs.
   - Collects all `Future<DeliveryResult>` results.
   - Shuts down the pool.
   - Prints each result: `"orderId → destination  €cost  [OK]"`.
   - Prints total jobs, total successes, and total revenue (sum of costs).
4. In `Exercise.run()`: create and call `new DispatchSimulation().run()`.

### Sample output

```text
[pool-1-thread-1] Completing ORD-001 → Cork
[pool-1-thread-2] Completing ORD-002 → Dublin
[pool-1-thread-3] Completing ORD-003 → Galway
[pool-1-thread-1] Completing ORD-004 → Limerick
...
ORD-001 → Cork       €45.00  [OK]
ORD-002 → Dublin     €62.50  [OK]
...
--- Summary ---
Jobs: 6  |  Successes: 6  |  Revenue: €297.50
```

### Constraints

- Submit all 6 jobs before calling `get()` on any `Future`.
- The pool must have exactly 3 threads — jobs 4–6 wait until a thread is free.
- Summary must be printed only after all results are collected.

### Done when...

- All 6 results are printed correctly.
- The summary totals match the per-job lines.
- The pool is shut down cleanly before the summary prints.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t19_concurrency.exercises.ex05;

import java.util.*;
import java.util.concurrent.*;

public class Exercise {

    public static void run() throws Exception {
        new DispatchSimulation().run();
    }
}

class DispatchSimulation {

    // Runs: the full dispatch batch — submits jobs, collects results, prints summary
    public void run() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        List<DeliveryJob> jobs = List.of(
            new DeliveryJob("ORD-001", "Cork", 45.00),
            new DeliveryJob("ORD-002", "Dublin", 62.50),
            new DeliveryJob("ORD-003", "Galway", 38.75),
            new DeliveryJob("ORD-004", "Limerick", 51.00),
            new DeliveryJob("ORD-005", "Sligo", 55.25),
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

        int successes = 0;
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

    // === Fields ===
    private String _orderId;
    private String _destination;
    private double _cost;

    // === Constructors ===
    // Creates: a delivery job with the given order details
    public DeliveryJob(String orderId, String destination, double cost) {
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("orderId is required");
        if (destination == null || destination.isBlank())
            throw new IllegalArgumentException("destination is required");
        if (cost <= 0)
            throw new IllegalArgumentException("cost must be > 0");

        _orderId = orderId;
        _destination = destination;
        _cost = cost;
    }

    // === Public API ===
    // Calls: simulates completing the delivery and returns the result
    @Override
    public DeliveryResult call() throws InterruptedException {
        Thread.sleep(300);
        System.out.println("[" + Thread.currentThread().getName() + "] Completing "
            + _orderId + " \u2192 " + _destination);
        return new DeliveryResult(_orderId, _destination, _cost, true);
    }
}

class DeliveryResult {

    // === Fields ===
    private String _orderId;
    private String _destination;
    private double _cost;
    private boolean _success;

    // === Constructors ===
    // Creates: an immutable delivery result
    public DeliveryResult(String orderId, String destination, double cost, boolean success) {
        _orderId = orderId;
        _destination = destination;
        _cost = cost;
        _success = success;
    }

    // === Public API ===
    // Gets: the order ID
    public String orderId() { return _orderId; }

    // Gets: the destination
    public String destination() { return _destination; }

    // Gets: the delivery cost
    public double cost() { return _cost; }

    // Gets: whether the delivery succeeded
    public boolean success() { return _success; }
}
```

</details>

---

## Lesson Context

```yaml
linked_lesson:
  topic_code: "t19_concurrency"
  lesson_path: "/notes/topics/t19_concurrency/t19_concurrency_notes.md"
  primary_domain_emphasis: "Balanced"
  difficulty_tier: "Foundation → Intermediate"
```
