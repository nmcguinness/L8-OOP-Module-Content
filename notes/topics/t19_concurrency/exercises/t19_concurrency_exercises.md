---
title: "Concurrency I — Threads and the ExecutorService — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t19_concurrency
description: "Five step-by-step exercises building a delivery dispatcher: a Runnable on raw threads, an ExecutorService, a deliberate race condition and its fix, Callable and Future, and a full simulation."
created: 2026-02-20
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, concurrency, threads, executorservice, runnable, callable, synchronization, exercises, year2, comp-c8z03]
---
# Concurrency I — Threads and the ExecutorService — Exercises

These exercises build a delivery dispatcher, in which several couriers work at the same time, each on its own thread. They follow the [t19 notes](../t19_concurrency_notes.md). Three words come up throughout:

- A **thread** is one path of execution. A program can run several at once.
- A **thread pool** (`ExecutorService`) keeps a few threads ready and hands them tasks, so you do not create a new thread for every job.
- A **race condition** is a bug where the result depends on which thread happens to run first.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution and its tests. Work through them in order.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Your first threads | `Runnable`, `Thread.start()` | Foundation |
| 02 A dispatcher with a thread pool | `ExecutorService`, shutting down cleanly | Foundation |
| 03 A race condition | seeing lost updates, fixing them with `synchronized` | Intermediate |
| 04 Getting a result back | `Callable` and `Future` | Intermediate |
| 05 A full dispatch simulation | everything together | Intermediate |

## Rules for every exercise

- Start a thread with `start()`, never `run()`. Calling `run()` does the work on the current thread.
- Call `shutdown()` on every pool once all its tasks are submitted.
- When you catch `InterruptedException`, call `Thread.currentThread().interrupt()` in the `catch` block, so the interruption is not lost.

## How to run

Each exercise goes in its own package, `t19_concurrency.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`.

> [!NOTE]
> Threads take turns in an order nobody controls. Your output will be in a slightly different order from the examples, and from your own previous run. That is expected. The checklists say what must be true however the lines are ordered.

---

## Exercise 01 — Your first threads

**Goal:** Write a delivery task that prints its progress, then run two deliveries at the same time on two threads, and watch their output mix together.

**You will need:** [What a thread looks like in code](../t19_concurrency_notes.md#what-a-thread-looks-like-in-code) and [Thread.sleep() — pausing a thread](../t19_concurrency_notes.md#threadsleep--pausing-a-thread).

**Steps**

1. Create `DeliveryTask`, which implements `Runnable`. Its constructor takes an `orderId`, a `destination` and a number of `steps`.
2. In the constructor, reject a blank `orderId` or `destination`, and a `steps` value below 1.
3. In `run()`, loop once per step. Print the thread's name, the order, the step number and the destination, then sleep for 200 milliseconds.
4. After the loop, print that the order was delivered.
5. In `Exercise.run()`, create two tasks: `ORD-001` to Galway in 3 steps, and `ORD-002` to Limerick in 2 steps.
6. Wrap each in a `Thread`, call `start()` on both, and straight away print `Dispatcher: both threads started`.

```java
public DeliveryTask(String orderId, String destination, int steps)
public void run()
```

**Example** (one possible order)

```text
Dispatcher: both threads started
[Thread-1] Order ORD-002: step 1/2 → Limerick
[Thread-0] Order ORD-001: step 1/3 → Galway
[Thread-0] Order ORD-001: step 2/3 → Galway
[Thread-1] Order ORD-002: step 2/2 → Limerick
[Thread-0] Order ORD-001: step 3/3 → Galway
[Thread-1] Order ORD-002: DELIVERED to Limerick
[Thread-0] Order ORD-001: DELIVERED to Galway
```

**Rules**

- The main thread does not wait for the deliveries. It prints its message and carries on.

<details>
<summary>Hints</summary>

1. `Thread.currentThread().getName()` gives names such as `Thread-0`.
2. `Thread.sleep(200)` throws `InterruptedException`, which `run()` cannot declare. Catch it, call `Thread.currentThread().interrupt()`, and `return`.
3. `new Thread(task).start();`

</details>

**Done when**

- [ ] `Dispatcher: both threads started` appears before either delivery finishes.
- [ ] Lines from the two orders are mixed together.
- [ ] You can explain why the order of lines changes from run to run.

**Package:** `t19_concurrency.exercises.ex01`

**Solution:** [t19 exercise 01 solution code](../../../../code/src/t19_concurrency/exercises/ex01/).
**Tests:** [t19 exercise 01 tests](../../../../code/test/t19_concurrency/exercises/ex01/).

---

## Exercise 02 — A dispatcher with a thread pool

**Goal:** Stop creating threads by hand. A `Dispatcher` hands tasks to a thread pool, then shuts the pool down and waits for every delivery to finish.

**You will need:**
[Part 2: The ExecutorService](../t19_concurrency_notes.md#part-2-the-executorservice) and [Shutting down cleanly](../t19_concurrency_notes.md#shutting-down-cleanly).

**Steps**

1. Reuse `DeliveryTask` from Exercise 01, either by importing it or by copying it into this package.
2. Create `Dispatcher`. Its constructor creates a pool with `Executors.newCachedThreadPool()`.
3. `dispatch(task)` rejects `null`, then passes the task to `pool.submit(task)`.
4. `shutdown()` calls `pool.shutdown()`, then `pool.awaitTermination(10, TimeUnit.SECONDS)`.
5. In `Exercise.run()`, dispatch five deliveries of 1 to 3 steps each, call `shutdown()`, then print `All deliveries complete`.

```java
public Dispatcher()
public void dispatch(DeliveryTask task)
public void shutdown() throws InterruptedException
```

**Example** (one possible order, shortened)

```text
[pool-1-thread-5] Order ORD-005: step 1/1 → Limerick
[pool-1-thread-1] Order ORD-001: step 1/2 → Cork
[pool-1-thread-2] Order ORD-002: step 1/1 → Dublin
[pool-1-thread-3] Order ORD-003: step 1/3 → Sligo
...
[pool-1-thread-3] Order ORD-003: DELIVERED to Sligo
All deliveries complete
```

**Rules**

- No `new Thread(...)` anywhere in this exercise.

<details>
<summary>Hints</summary>

1. `shutdown()` stops the pool accepting new tasks, but does not wait. `awaitTermination` is what waits.
2. Thread names such as `pool-1-thread-3` come from the pool, which shows it is doing the work.

</details>

**Done when**

- [ ] All five deliveries finish before `All deliveries complete` is printed.
- [ ] Thread names start with `pool-1-thread-`.

**Package:** `t19_concurrency.exercises.ex02`

**Solution:** [t19 exercise 02 solution code](../../../../code/src/t19_concurrency/exercises/ex02/).
**Tests:** [t19 exercise 02 tests](../../../../code/test/t19_concurrency/exercises/ex02/).

---

## Exercise 03 — A race condition

**Goal:** Watch a counter shared by several threads lose updates, then fix it. This bug is easy to write and hard to spot, so it is worth seeing it happen once.

**You will need:** [Race condition](../t19_concurrency_notes.md#race-condition) and [Part 4: Race conditions and synchronisation](../t19_concurrency_notes.md#part-4-race-conditions-and-synchronisation).

**Steps**

1. Create `DeliveryCounter` with a private `int total`, `increment()` (which does `total++`) and `getTotal()`. Do **not** synchronise anything.
2. Create `SynchronizedDeliveryCounter`, the same but with `synchronized` on both methods.
3. In `run()`, create a pool with `Executors.newFixedThreadPool(8)`. Submit 5,000 calls to the unsafe counter's `increment`, then shut the pool down and wait.
4. Do the same with a second pool for the safe counter.
5. Print both totals.
6. Above `DeliveryCounter`, write a comment explaining why it gives the wrong answer.

```java
public synchronized void increment()
public synchronized int getTotal()
```

**Example** (the unsafe total changes every run)

```text
Unsafe total   (expected 5000): 4979
Safe total     (expected 5000): 5000
```

**Rules**

- Exactly 5,000 tasks for each counter.

<details>
<summary>Hints</summary>

1. `pool.submit(unsafe::increment)` submits one call.
2. `total++` is really three steps: read `total`, add 1, write it back. What happens if two threads both read 41 before either writes?

</details>

**Done when**

- [ ] The unsafe total is usually below 5,000. Run it a few times.
- [ ] The safe total is always exactly 5,000.
- [ ] You can explain the read-add-write problem in one sentence.

> [!CAUTION]
> On some machines the unsafe total occasionally comes out at exactly 5,000. That does not mean the code is correct. A race condition is a bug that only shows up sometimes, which is what makes it dangerous.

**Package:** `t19_concurrency.exercises.ex03`

**Solution:** [t19 exercise 03 solution code](../../../../code/src/t19_concurrency/exercises/ex03/).
**Tests:** [t19 exercise 03 tests](../../../../code/test/t19_concurrency/exercises/ex03/).

---

## Exercise 04 — Getting a result back

**Goal:** Run two slow cost calculations at the same time and collect both answers. A `Callable` is like a `Runnable` that returns a value, and a `Future` is the receipt you use to collect it.

**You will need:** [Part 3: Callable and Future](../t19_concurrency_notes.md#part-3-callable-and-future-brief).

**Steps**

1. Create `CostEstimateTask`, which implements `Callable<Double>`. Its constructor takes an `orderId`, a `baseRate` and a `distance`.
2. Reject a blank `orderId`, a `baseRate` of 0 or less, and a `distance` below 1.
3. In `call()`, sleep for 500 milliseconds, print `[thread-name] Estimating <orderId>...`, and return `baseRate * distance`.
4. In `run()`, create a pool of 2 threads. Submit two tasks and keep both `Future<Double>` results.
5. Print `Main thread: estimates submitted`.
6. Call `get()` on each future and print the cost with two decimal places. Then shut the pool down.

```java
public CostEstimateTask(String orderId, double baseRate, int distance)
public Double call() throws InterruptedException
```

**Example** (the tasks use 2.50 × 45 and 3.00 × 29)

```text
Main thread: estimates submitted
[pool-1-thread-1] Estimating ORD-001...
[pool-1-thread-2] Estimating ORD-002...
ORD-001 cost estimate: €112.50
ORD-002 cost estimate: €87.00
```

**Rules**

- Submit **both** tasks before you call `get()` on either.

<details>
<summary>Hints</summary>

1. `Future<Double> first = pool.submit(new CostEstimateTask(...));`
2. `get()` waits until the result is ready. Calling it on the first future before submitting the second would make the tasks run one after the other.
3. Unlike `run()`, `call()` may throw a checked exception, so `Thread.sleep` needs no `try`.

</details>

**Done when**

- [ ] `Main thread: estimates submitted` is printed before either estimate.
- [ ] The whole run takes about half a second, not a second, because the two waits overlap.

**Package:** `t19_concurrency.exercises.ex04`

**Solution:** [t19 exercise 04 solution code](../../../../code/src/t19_concurrency/exercises/ex04/).
**Tests:** [t19 exercise 04 tests](../../../../code/test/t19_concurrency/exercises/ex04/).

---

## Exercise 05 — A full dispatch simulation

**Goal:** Put it all together: submit six delivery jobs to a pool of three couriers, collect every result, and print a summary once they are all in.

**You will need:** [Full example: simulated client connections](../t19_concurrency_notes.md#full-example-simulated-client-connections) and [Part 3: Callable and Future](../t19_concurrency_notes.md#part-3-callable-and-future-brief).

**Steps**

1. Create `DeliveryResult`, a value class with `orderId`, `destination`, `cost` and `success`. Its fields are `private final`, with a getter for each and no setters.
2. Create `DeliveryJob`, which implements `Callable<DeliveryResult>`. In `call()`, sleep for 300 milliseconds, print `[thread-name] Completing <orderId> → <destination>`, and return a successful result.
3. Create `DispatchSimulation` with a `run()` method. It creates a pool of 3 threads and submits six jobs, keeping every `Future` in a list.
4. Shut the pool down. Then loop over the futures, call `get()` on each, and print one line per result.
5. Print a summary: the number of jobs, the number of successes, and the total of all the costs.
6. In `Exercise.run()`, call `new DispatchSimulation().run()`.

```java
public DeliveryResult(String orderId, String destination, double cost, boolean success)
public DeliveryJob(String orderId, String destination, double cost)
```

**Example** (the first six lines may appear in a different order)

```text
[pool-1-thread-1] Completing ORD-001 → Cork
[pool-1-thread-2] Completing ORD-002 → Dublin
[pool-1-thread-3] Completing ORD-003 → Galway
[pool-1-thread-2] Completing ORD-004 → Limerick
[pool-1-thread-1] Completing ORD-005 → Sligo
[pool-1-thread-3] Completing ORD-006 → Kilkenny
ORD-001    → Cork         €45.00    [OK]
ORD-002    → Dublin       €62.50    [OK]
ORD-003    → Galway       €38.75    [OK]
ORD-004    → Limerick     €51.00    [OK]
ORD-005    → Sligo        €55.25    [OK]
ORD-006    → Kilkenny     €45.00    [OK]
--- Summary ---
Jobs: 6  |  Successes: 6  |  Revenue: €297.50
```

**Rules**

- Submit all six jobs before calling `get()` on any future.
- The pool has exactly 3 threads, so jobs 4 to 6 wait until a courier is free.
- The summary is printed only after every result has been collected.

<details>
<summary>Hints</summary>

1. Keep the futures in a `List<Future<DeliveryResult>>`, in the order you submitted the jobs. The results then print in that order, whichever job finished first.
2. `System.out.printf("%-10s → %-12s €%-8.2f %s%n", ...)` lines the columns up.

</details>

**Done when**

- [ ] `run()` prints all six result lines in order, then the summary.
- [ ] The revenue equals the sum of the six costs.

**Think about it:** only three thread names appear, but six jobs ran. What did the pool do with jobs 4 to 6 while the first three were running?

**Package:** `t19_concurrency.exercises.ex05`

**Solution:** [t19 exercise 05 solution code](../../../../code/src/t19_concurrency/exercises/ex05/).
**Tests:** [t19 exercise 05 tests](../../../../code/test/t19_concurrency/exercises/ex05/).
