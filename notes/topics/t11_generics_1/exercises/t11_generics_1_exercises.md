---
title: "Generics I — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t11_generics_1
description: "Seven exercises introducing type parameters and type safety (week 1 of 2), from a legacy Object container to generic stacks, registries, and a message-bus refactor."
created: 2026-01-24
last_updated: 2026-09-10
version: 3.2
status: published
authors: ["OOP Teaching Team"]
tags: [java, generics, type-safety, collections, exercises, year2, comp-c8z03]
---
# Generics I — Exercises

> These exercises build directly on the **Generics I** notes.
> This week is about learning **why** generics exist (type-safety) and getting confident
> writing your own generic classes and methods.

## Ground rules

- No raw types (e.g., `ArrayList` to `ArrayList<T>`).
- Don’t use casts as a “fix” (casts are allowed only where the Java type system forces it, e.g.
  reflective array creation).
- Prefer simple loops (no streams) unless an exercise explicitly says otherwise.

## How to run

Each exercise assumes a package like:

```java
t11_generics_1.exercises.exNN
```

Create a class `Exercise` in that package with a static entry point:

```java
package t11_generics_1.exercises.ex01;

public class Exercise {
    public static void run() {
        // call your methods here; print results for quick checks
    }
}
```

---

## Exercise 01 — The crash you never want again (legacy Object container)

**Objective:** Feel the pain generics remove: casts + runtime crashes.

**Context (software + games):**
- **Software dev:** a legacy service layer returns `Object` and forces you to cast.
- **Games dev:** an inventory/container accidentally mixes item types.

### What you are building

You will create **two versions** of the same idea: “a container that stores items and returns
them by index”.

| Class | What it stores | Type safety |
| :- | :- | :- |
| `InventoryLegacy` | `Object` | Unsafe (casts required) |
| `Inventory<T>` | `T` | Safe (no casts) |

You will then demonstrate the key difference:

- the legacy container **compiles** even when you mix types, but you can crash later at runtime
- the generic container **prevents the mistake** at compile time

### Required API

Implement these two classes:

```java
class InventoryLegacy {
    public void add(Object item) { }
    public Object get(int index) { }
}

class Inventory<T> {
    public void add(T item) { }
    public T get(int index) { }
}
```

### Tasks

1. **Run the legacy container**
   - Create an `InventoryLegacy`.
   - Add **two different types** (e.g., `String`, `Integer`).
   - Retrieve the first value and call `toUpperCase()` (this forces a cast).
   - Add a **commented-out** cast line that *could* crash at runtime (and explain why in a
     short comment).

2. **Refactor to generics**
   - Create `class Inventory<T>`.
   - Use `ArrayList<T>` internally (not a raw `ArrayList`).
   - Implement `add` and `get`.

3. **Prove the benefit**
   - In `Exercise.run()`, demonstrate that `Inventory<String>` rejects adding an `Integer`
     (comment out the failing line).

### Done when...

- Your `Inventory<T>` usage contains **no casts**.
- You have **zero** raw types.
- You can explain, in 1–2 sentences, why the generic version is safer.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
class InventoryLegacy {
    private java.util.ArrayList _items = new java.util.ArrayList();

    public void add(Object item) {
        _items.add(item);
    }

    public Object get(int index) {
        return _items.get(index);
    }
}

class Inventory<T> {
    private java.util.ArrayList<T> _items = new java.util.ArrayList<>();

    public void add(T item) {
        _items.add(item);
    }

    public T get(int index) {
        return _items.get(index);
    }
}

public class Exercise {
    public static void run() {
        InventoryLegacy legacy = new InventoryLegacy();
        legacy.add("hi");
        legacy.add(123);

        String s = (String) legacy.get(0);
        System.out.println(s.toUpperCase());

        // Integer n = (Integer) legacy.get(0); // could crash at runtime (wrong cast)

        Inventory<String> safe = new Inventory<>();
        safe.add("hello");
        // safe.add(123); // does not compile

        System.out.println(safe.get(0).toUpperCase());
    }
}
```

</details>

---

## Exercise 02 — Generic `UndoStack<T>`

**Objective:** Implement a reusable generic structure (LIFO stack) with clear empty-case behaviour.

**Context (software + games):**
- **Software dev:** undo/redo in an editor, form builder, or admin tool.
- **Games dev:** command history for tools, or action history for input replay.

### What you are building

A last-in-first-out stack backed by an `ArrayList<T>`.

You must decide and document the empty behaviour:

- `pop()` on empty to returns `null`
- `peek()` on empty to returns `null`

### Required API

```java
class UndoStack<T> {
    public void push(T item) { }
    public T pop() { }     // null if empty
    public T peek() { }    // null if empty
    public int size() { }
}
```

### Rules/behaviour

- `push` should add to the “top” of the stack.
- `pop` removes and returns the top item, or returns `null` if empty.
- `peek` returns the top item without removing it, or returns `null` if empty.
- `size` returns the current count.

### Tasks

1. Implement `UndoStack<T>`.
2. In `Exercise.run()`:
   - create `UndoStack<String>`
   - push 3 strings
   - print `size()`
   - `pop()` once and print the returned value
   - `peek()` and print the new top
   - pop until empty and confirm you get `null`
3. Repeat quickly with `UndoStack<Integer>` (at least 2 pushes + one pop).

### Done when...

- No exceptions are thrown when popping/peeking an empty stack.
- The class works for at least two types without code changes.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
class UndoStack<T> {
    private java.util.ArrayList<T> _items = new java.util.ArrayList<>();

    public void push(T item) {
        _items.add(item);
    }

    public T pop() {
        if (_items.isEmpty())
            return null;
        return _items.remove(_items.size() - 1);
    }

    public T peek() {
        if (_items.isEmpty())
            return null;
        return _items.get(_items.size() - 1);
    }

    public int size() {
        return _items.size();
    }
}

public class Exercise {
    public static void run() {
        UndoStack<String> s = new UndoStack<>();
        s.push("MoveLeft");
        s.push("Jump");
        s.push("UsePotion");

        System.out.println(s.size()); // 3
        System.out.println(s.pop());  // UsePotion
        System.out.println(s.peek()); // Jump
        System.out.println(s.size()); // 2
    }
}
```

</details>

---

## Exercise 03 — Generic map (no streams)

**Objective:** Write a generic method where type parameters flow from input to output.

**Context (software + games):**
- **Software dev:** transform domain objects into UI labels/DTOs.
- **Games dev:** transform entities into debug strings, IDs, or HUD text.

### What you are building

A `map` utility that converts a `List<T>` into an `ArrayList<R>` using a transformer function.

### Required signature

```java
public static <T, R> java.util.ArrayList<R> map(
        java.util.List<T> items,
        java.util.function.Function<T, R> transform)
```

### Rules/behaviour

- If `transform` is `null` to throw `NullPointerException`.
- If `items` is `null` to return an **empty** `ArrayList<R>` (not `null`).
- Preserve order.
- Use a loop (no streams).

### Tasks

1. Implement the method in a utility class (e.g., `Maps`).
2. In `Exercise.run()` demonstrate:
- `["ant", "zebra"]` to `[3, 5]` (string length)
- `[10, 20]` to `["HP:10", "HP:20"]` (prefix formatting)
1. Print the outputs so you can visually confirm ordering.

### Done when...

- The same method compiles for different type pairs (e.g., `String → Integer`, `Integer → String`).
- No casts and no raw types.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Maps {
    public static <T, R> java.util.ArrayList<R> map(
            java.util.List<T> items,
            java.util.function.Function<T, R> transform) {

        if (transform == null)
            throw new NullPointerException("transform must not be null");

        java.util.ArrayList<R> out = new java.util.ArrayList<>();

        if (items == null)
            return out;

        for (T item : items)
            out.add(transform.apply(item));

        return out;
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<Integer> lengths =
                Maps.map(java.util.List.of("ant", "zebra"), String::length);

        java.util.ArrayList<String> labels =
                Maps.map(java.util.List.of(10, 20), n -> "HP:" + n);

        System.out.println(lengths); // [3, 5]
        System.out.println(labels);  // [HP:10, HP:20]
    }
}
```

</details>

---

## Exercise 04 — Generic where/filter (no streams)

**Objective:** Use a generic predicate to select items safely.

**Context (software + games):**
- **Software dev:** filter users/orders/log entries.
- **Games dev:** filter active enemies/visible objects/valid pickups.

### What you are building

A `where` utility that returns only the items matching a `Predicate<T>`.

### Required signature

```java
public static <T> java.util.ArrayList<T> where(
        java.util.List<T> items,
        java.util.function.Predicate<T> predicate)
```

### Rules/behaviour

- If `predicate` is `null` to throw `NullPointerException`.
- If `items` is `null` to return an empty `ArrayList<T>`.
- Preserve order.
- Do not modify the original list.
- Use a loop (no streams).

### Tasks

1. Implement `where` in a utility class (e.g., `Filters`).
2. In `Exercise.run()` demonstrate:
   - filtering strings that start with `"A"`
   - filtering integers greater than `10`
3. Print the results.

### Done when...

- Your method returns a new list containing only matching items, in the original order.
- The original list is unchanged.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Filters {
    public static <T> java.util.ArrayList<T> where(
            java.util.List<T> items,
            java.util.function.Predicate<T> predicate) {

        if (predicate == null)
            throw new NullPointerException("predicate must not be null");

        java.util.ArrayList<T> out = new java.util.ArrayList<>();

        if (items == null)
            return out;

        for (T item : items) {
            if (predicate.test(item))
                out.add(item);
        }

        return out;
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<String> a =
                Filters.where(java.util.List.of("Amy", "Bob", "Ada"), s -> s.startsWith("A"));

        java.util.ArrayList<Integer> b =
                Filters.where(java.util.List.of(5, 11, 12, 3), n -> n > 10);

        System.out.println(a); // [Amy, Ada]
        System.out.println(b); // [11, 12]
    }
}
```

</details>

---

## Exercise 05 — `TopN<T extends Comparable<T>>` (Week 1 capstone)

**Objective:** Use a bounded type parameter to build a component with real behaviour.

**Context (software + games):**
- **Software dev:** top N sales totals / top N response times / top N search results.
- **Games dev:** top N leaderboard scores / top N damage events / top N loot rolls.

### What you are building

A class that keeps only the **best N** values seen so far, always stored in **descending
order** (largest first).

This is your first “mini data structure” built with generics and constraints.

### Required API

```java
class TopN<T extends Comparable<T>> {
    public TopN(int capacity) { }
    public void add(T item) { }
    public java.util.ArrayList<T> valuesDescending() { } // returns defensive copy
}
```

### Rules/behaviour

- `capacity` must be `>= 1` to otherwise throw `IllegalArgumentException`.
- `add(null)` should throw `NullPointerException`.
- `add(item)` must:
  - insert `item` into the correct position so the list remains **descending**
  - trim the list if it exceeds capacity
- `valuesDescending()` returns a **new** list (defensive copy).
- Use loops only (no `sort`, no streams).

### Tasks

1. Implement `TopN<T>`.
2. In `Exercise.run()`:
   - create `TopN<Integer>` with capacity `3`
   - add: `10, 7, 25, 3, 19`
   - print the result and verify: `[25, 19, 10]`
3. Also try `TopN<String>` with capacity `2` and confirm it orders lexicographically in
   descending order.

### Done when...

- Internal size never exceeds capacity.
- Order is always descending after every `add`.
- `valuesDescending()` cannot be used to mutate the internal list.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
class TopN<T extends Comparable<T>> {
    private int _capacity;
    private java.util.ArrayList<T> _values = new java.util.ArrayList<>();

    public TopN(int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException("capacity must be >= 1");
        _capacity = capacity;
    }

    public void add(T item) {
        if (item == null)
            throw new NullPointerException("item must not be null");

        int insertIndex = 0;

        while (insertIndex < _values.size()) {
            T current = _values.get(insertIndex);

            // Descending: place item before the first element that is smaller than it.
            if (item.compareTo(current) > 0)
                break;

            insertIndex++;
        }

        _values.add(insertIndex, item);

        while (_values.size() > _capacity)
            _values.remove(_values.size() - 1);
    }

    public java.util.ArrayList<T> valuesDescending() {
        return new java.util.ArrayList<>(_values);
    }
}

public class Exercise {
    public static void run() {
        TopN<Integer> best = new TopN<>(3);
        best.add(10);
        best.add(7);
        best.add(25);
        best.add(3);
        best.add(19);

        System.out.println(best.valuesDescending()); // [25, 19, 10]
    }
}
```

</details>

---

## Exercise 06 — Registry<K, V> (type-safe lookup)

**Objective:** Practise multiple type parameters (`K`, `V`) with a familiar API.

**Context (software + games):**
- **Software dev:** `userId → profile`, `configKey → value`, `email → account`.
- **Games dev:** `entityId → entity`, `itemId → definition`, `assetKey → asset`.

### What you are building

A tiny wrapper around `HashMap<K, V>` that enforces one rule:

- **keys must not be null**

(Values may be null.)

### Required API

```java
class Registry<K, V> {
    public void put(K key, V value) { }
    public V get(K key) { }
    public boolean containsKey(K key) { }
}
```

### Rules/behaviour

- If `key` is `null` in any method to throw `NullPointerException`.
- `get` returns `null` if the key is missing (standard `Map` behaviour).

### Tasks

1. Implement `Registry<K, V>`.
2. In `Exercise.run()`:
   - store and retrieve scores using `Registry<String, Integer>`
- store and retrieve an id to name mapping using `Registry<Integer, String>`
  - show `containsKey` working
  - show `get` returning `null` for a missing key

### Done when...

- You can use the same class with different key/value types without any changes.
- Null keys are rejected consistently.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
class Registry<K, V> {
    private java.util.HashMap<K, V> _map = new java.util.HashMap<>();

    public void put(K key, V value) {
        if (key == null)
            throw new NullPointerException("key must not be null");
        _map.put(key, value);
    }

    public V get(K key) {
        if (key == null)
            throw new NullPointerException("key must not be null");
        return _map.get(key);
    }

    public boolean containsKey(K key) {
        if (key == null)
            throw new NullPointerException("key must not be null");
        return _map.containsKey(key);
    }
}

public class Exercise {
    public static void run() {
        Registry<String, Integer> scores = new Registry<>();
        scores.put("Zara", 100);
        scores.put("Kai", 55);

        System.out.println(scores.containsKey("Zara")); // true
        System.out.println(scores.get("Kai"));          // 55
        System.out.println(scores.get("Nope"));         // null
    }
}
```

</details>

---

## Exercise 07 — Refactor `MessageBusLegacy` into `MessageBus<T>`

**Objective:** Refactor a realistic “bag of Objects” into a generic type.

**Context (software + games):**
- **Software dev:** event queue between UI and service layer.
- **Games dev:** event/message bus between gameplay systems (input then gameplay then audio).

### What you are building

A FIFO (first-in-first-out) message bus backed by `ArrayList<T>`.

### Required API

```java
class MessageBus<T> {
    public void enqueue(T msg) { }
    public T dequeue() { }   // null if empty
    public int size() { }
    public boolean isEmpty() { }
}
```

### Rules/behaviour

- `enqueue` adds to the end of the queue.
- `dequeue` removes from the front of the queue.
- If empty, `dequeue` returns `null`.
- `isEmpty` returns `true` when `size() == 0`.

> Note: `remove(0)` is O(n). That’s fine here — focus on type-safety and API behaviour.

### Tasks

1. Implement `MessageBus<T>`.
2. In `Exercise.run()`:
   - create `MessageBus<String>`
   - enqueue at least 3 messages
   - dequeue until you get `null`
   - print each dequeued value
3. Add one commented-out line that shows the compiler rejects the wrong type.

### Done when...

- Your queue returns messages in the same order they were enqueued.
- No casts and no raw types.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
class MessageBus<T> {
    private java.util.ArrayList<T> _queue = new java.util.ArrayList<>();

    public void enqueue(T msg) {
        _queue.add(msg);
    }

    public T dequeue() {
        if (_queue.isEmpty())
            return null;
        return _queue.remove(0);
    }

    public int size() {
        return _queue.size();
    }

    public boolean isEmpty() {
        return _queue.isEmpty();
    }
}

public class Exercise {
    public static void run() {
        MessageBus<String> bus = new MessageBus<>();
        bus.enqueue("A");
        bus.enqueue("B");
        bus.enqueue("C");

        System.out.println(bus.size());    // 3
        System.out.println(bus.dequeue()); // A
        System.out.println(bus.dequeue()); // B
        System.out.println(bus.dequeue()); // C
        System.out.println(bus.dequeue()); // null

        // bus.enqueue(123); // does not compile
    }
}
```

</details>

---

## Optional challenge — Generic `ObjectPool<T>`

**Objective:** Build a generic object pool that reduces allocations by reusing instances.

**Context (software + games):**
- **Software dev:** reuse expensive objects (buffers/builders) to reduce GC pressure.
- **Games dev:** pool bullets/particles/events to avoid frame spikes.

### What you are building

A pool with two operations:

- `acquire()`:
  - returns an existing pooled object if available
  - otherwise creates a new one using a factory (`Supplier<T>`)
- `release(obj)`:
  - returns an object back to the pool for reuse

### Required API

```java
class ObjectPool<T> {
    public ObjectPool(java.util.function.Supplier<T> factory) { }
    public T acquire() { }
    public void release(T obj) { }
}
```

### Rules/behaviour

- `factory == null` to throw `NullPointerException`.
- `release(null)` returns throw `NullPointerException` (keep the pool clean).
- Prefer LIFO reuse (take from the end of an internal list).

### Tasks

1. Implement `ObjectPool<T>` using an `ArrayList<T>` as the internal storage.
2. In `Exercise.run()`:
   - create `ObjectPool<StringBuilder>` using `StringBuilder::new`
   - acquire one builder, append text, then release it
   - acquire again and show you got a usable instance back (print something to prove it works)
3. Add a short comment describing why pooling can help performance (1–2 lines).

### Done when...

- Acquiring returns a new object when the pool is empty.
- Releasing and re-acquiring reuses objects.
- Your pool is generic and works for any type `T`.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
class ObjectPool<T> {
    private java.util.function.Supplier<T> _factory;
    private java.util.ArrayList<T> _pool = new java.util.ArrayList<>();

    public ObjectPool(java.util.function.Supplier<T> factory) {
        if (factory == null)
            throw new NullPointerException("factory must not be null");
        _factory = factory;
    }

    public T acquire() {
        if (_pool.isEmpty())
            return _factory.get();
        return _pool.remove(_pool.size() - 1);
    }

    public void release(T obj) {
        // Choice: we will NOT allow null releases
        if (obj == null)
            throw new NullPointerException("obj must not be null");
        _pool.add(obj);
    }
}

public class Exercise {
    public static void run() {
        ObjectPool<StringBuilder> pool = new ObjectPool<>(StringBuilder::new);

        StringBuilder a = pool.acquire();
        a.append("hi");
        pool.release(a);

        StringBuilder b = pool.acquire();
        System.out.println(a == b); // true (reused)
    }
}
```

</details>

---

## Lesson Context

```yaml
linked_lesson:
  topic_code: "t11_generics_1"
  week_in_topic: "1_of_2"
  primary_domain_emphasis: "Balanced"
  difficulty_tier: "Intermediate"
```
