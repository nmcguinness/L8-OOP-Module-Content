---
title: "Generics II — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t12_generics_2
description: "Ten exercises on wildcards, variance and PECS (week 2 of 2), including producer and consumer methods, wildcard capture, and repairing a broken API signature."
created: 2026-01-24
last_updated: 2026-09-10
version: 2.3
status: published
authors: ["OOP Teaching Team"]
tags: [java, generics, wildcards, pecs, variance, collections, exercises, year2, comp-c8z03]
---
# Generics II — Exercises

> These exercises build directly on the **Generics II** notes.
> This week is about learning to *read* wildcard types and *choose* the right wildcard when
> you’re designing a method.

## Ground rules

- Don’t “fix” wildcard errors using casts.
- Don’t use raw types.
- When an exercise is compile-time focused, keep the “failing line” commented out.
- Prefer simple loops (no streams) unless an exercise explicitly says otherwise.

## How to run

Each exercise assumes a package like:

```java
t12_generics_2.exercises.exNN
```

Create a class `Exercise` in that package with a static entry point:

```java
package t12_generics_2.exercises.ex01;

public class Exercise {
    public static void run() {
        // call your methods here; print results for quick checks
    }
}
```

---

## Exercise 01 — Prove invariance (compile-time)

**Objective:** Prove (with a compile error) that `List<Child>` is not a `List<Parent>`.

**Context (software + games):**
- **Software dev:** `AdminUser extends User`, but `List<AdminUser>` still isn’t a `List<User>`.
- **Games dev:** `Enemy extends Entity`, but `List<Enemy>` still isn’t a `List<Entity>`.

### What you are building

A tiny class hierarchy plus a deliberately failing assignment that you keep commented-out.
Your job is to **understand** and **explain** why the compiler must reject it.

Create these types:

- `Entity`
- `Enemy extends Entity`
- `Pickup extends Entity`

Then create these variables:

- `java.util.List<Enemy> enemies`
- `java.util.List<Entity> entities`

...and attempt the assignment:

```java
// entities = enemies; // should not compile (leave commented out)
```

### Tasks

1. Implement the three classes (empty bodies are fine).
2. In `Exercise.run()`:
   - show regular inheritance works:
     - `Entity e = new Enemy();`
   - create `ArrayList<Enemy>` and `ArrayList<Entity>`
   - add the commented-out failing line above
3. Add a short explanation comment answering:
   - “What bad thing could happen if Java allowed this assignment?”

### Done when...

- You can explain the failure using this idea:
  - if `List<Enemy>` could be treated as `List<Entity>`, someone could add a `Pickup` into it.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
 class Entity { }

 class Enemy extends Entity { }

 class Pickup extends Entity { }

public class Exercise {
    public static void run() {
        Enemy e = new Enemy();
        Entity x = e; // normal inheritance works

        java.util.List<Enemy> enemies = new java.util.ArrayList<>();

        // java.util.List<Entity> entities = enemies;
        // Not allowed: List<Enemy> is not a List<Entity> (generics are invariant).
        System.out.println(x);
        System.out.println(enemies.size());
    }
}
```

</details>

---

## Exercise 02 — printAll(List<?>)

**Objective:** Accept “any list” safely and print its contents.

**Context (software + games):**
- **Software dev:** a logging helper that prints any list (users, orders, strings).
- **Games dev:** a debug helper that prints any list (entities, scores, item names).

### What you are building

A utility method that works with **any** `List<T>` and prints each element on a new line.

You must implement **exactly** this signature:

```java
public static void printAll(java.util.List<?> items)
```

Your method should behave like:

- if `items` is `null` to do nothing (no crash)
- otherwise to print each element using `System.out.println(element)`

### Tasks

1. Create a class `Lists` (or `DebugLists`) and add `printAll`.
2. In `Exercise.run()` create and print:
   - `List<String>` with at least 3 strings
   - `List<Integer>` with at least 3 numbers
   - `List<Entity>` containing `Enemy` and `Pickup`
3. Add one commented-out line that shows you cannot safely `add(...)` to `List<?>`.

### Done when...

- The same `printAll` method works for all three list types without overloads or casts.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java

class Entity { }

class Lists {
    public static void printAll(java.util.List<?> items) {
        if (items == null)
            return;

        for (Object obj : items)
            System.out.println(obj);
    }
}

public class Exercise {
    public static void run() {
        Lists.printAll(java.util.List.of("A", "B"));
        Lists.printAll(java.util.List.of(10, 20));

        java.util.List<Entity> entities = java.util.List.of(new Entity(), new Entity());
        Lists.printAll(entities);
    }
}
```

</details>

---

## Exercise 03 — Producer method: sumNumbers(List<? extends Number>)

**Objective:** Use an `extends` wildcard to read numbers from a list of *some* numeric subtype.

**Context (software + games):**
- **Software dev:** sum invoices, analytics counters, or response times.
- **Games dev:** sum damage events, XP ticks, or score deltas.

### What you are building

A method that sums values from a list that produces `Number` values.

Implement **exactly** this signature:

```java
public static double sumNumbers(java.util.List<? extends Number> nums)
```

Rules/behaviour:

- `nums == null` to return `0.0`
- `nums.isEmpty()` returns return `0.0`
- otherwise to sum all values using `n.doubleValue()`

### Tasks

1. Implement `sumNumbers` in a utility class (e.g., `Numbers`).
2. In `Exercise.run()` verify results with:
- `List<Integer>` to `[1, 2, 3]` should produce `6.0`
- `List<Double>` to `[0.5, 1.5, 2.0]` should produce `4.0`
1. Add a commented-out line that demonstrates you cannot add `Integer` to `List<? extends Number>`.

### Done when...

- The method compiles and runs for both integer and double lists using the same implementation.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Numbers {
    public static double sumNumbers(java.util.List<? extends Number> nums) {
        if (nums == null || nums.isEmpty())
            return 0.0;

        double sum = 0.0;

        for (Number n : nums)
            sum += n.doubleValue();

        return sum;
    }
}

public class Exercise {
    public static void run() {
        System.out.println(Numbers.sumNumbers(java.util.List.of(1, 2, 3)));      // 6.0
        System.out.println(Numbers.sumNumbers(java.util.List.of(0.5, 1.5, 2.0))); // 4.0
        System.out.println(Numbers.sumNumbers(null));                             // 0.0
    }
}
```

</details>

---

## Exercise 04 — Consumer method: fill(List<? super T>, T, int)

**Objective:** Use a `super` wildcard so a method can **add** values into a more general
destination list.

**Context (software + games):**
- **Software dev:** generate placeholder rows/test data quickly.
- **Games dev:** fill a spawn queue or placeholder debug events.

### What you are building

A generic `fill` method that repeatedly adds the same value into a list.

Implement **exactly** this signature:

```java
public static <T> void fill(java.util.List<? super T> out, T value, int count)
```

Rules/behaviour:

- `out == null` to throw `NullPointerException`
- `count < 0` to throw `IllegalArgumentException`
- `count == 0` to do nothing
- otherwise to add `value` exactly `count` times

### Tasks

1. Implement `fill`.
2. In `Exercise.run()` demo **two** call sites:
   - `List<Object> out = new ArrayList<>(); fill(out, "hi", 3);`
   - `List<Number> out2 = new ArrayList<>(); fill(out2, 5, 2);`
3. Print both lists and confirm the counts.

### Done when...

- Your method supports destinations that are *supertypes* of the inserted value type.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Fillers {
    public static <T> void fill(java.util.List<? super T> out, T value, int count) {
        if (out == null)
            throw new NullPointerException("out must not be null");

        if (count < 0)
            throw new IllegalArgumentException("count must be >= 0");

        for (int i = 0; i < count; i++)
            out.add(value);
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<Object> out = new java.util.ArrayList<>();
        Fillers.fill(out, "hi", 3);

        System.out.println(out); // [hi, hi, hi]
    }
}
```

</details>

---

## Exercise 05 — PECS in action: copy(src extends T, dst super T)

**Objective:** Apply PECS (**P**roducer **E**xtends, **C**onsumer **S**uper) to copy items
between lists.

**Context (software + games):**
- **Software dev:** copy `List<Integer>` into `List<Number>` when preparing chart data.
- **Games dev:** copy `List<Enemy>` into `List<Entity>` when building a scene list.

### What you are building

A generic `copy` method that preserves order and compiles for “widening” destination lists.

Implement **exactly** this signature:

```java
public static <T> void copy(java.util.List<? extends T> src,
                            java.util.List<? super T> dst)
```

Rules/behaviour:

- `src == null` or `dst == null` to throw `NullPointerException`
- copy in order using a loop
- do not clear destination (append)

### Tasks

1. Implement `copy`.
2. In `Exercise.run()`:
   - create `List<Integer> src = List.of(1, 2, 3);`
   - copy into `ArrayList<Number>` and print
   - copy into `ArrayList<Object>` and print
3. Also test appending by:
   - putting something already in the destination and verifying it remains.

### Done when...

- Both destination lists contain previous contents plus the copied values, in order.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Copier {
    public static <T> void copy(java.util.List<? extends T> src, java.util.List<? super T> dst) {
        if (src == null)
            throw new NullPointerException("src must not be null");

        if (dst == null)
            throw new NullPointerException("dst must not be null");

        for (T item : src)
            dst.add(item);
    }
}

public class Exercise {
    public static void run() {
        java.util.List<Integer> src = java.util.List.of(1, 2, 3);

        java.util.ArrayList<Number> dst1 = new java.util.ArrayList<>();
        java.util.ArrayList<Object> dst2 = new java.util.ArrayList<>();

        Copier.copy(src, dst1);
        Copier.copy(src, dst2);

        System.out.println(dst1); // [1, 2, 3]
        System.out.println(dst2); // [1, 2, 3]
    }
}
```

</details>

---

## Exercise 06 — addAllEntities (domain-specific: extends to super)

**Objective:** Use wildcards on a class hierarchy in a way that feels like real code.

**Context (software + games):**
- **Software dev:** merge subtype lists into one base-type list.
- **Games dev:** merge enemies + pickups into one “world entities” list.

### What you are building

A domain-specific copy helper for the `Entity` hierarchy.

Implement **exactly** this signature:

```java
public static void addAllEntities(java.util.List<? extends Entity> src,
                                  java.util.List<? super Entity> dst)
```

Rules/behaviour:

- `src == null` or `dst == null` to throw `NullPointerException`
- append all items from `src` into `dst` in order

### Tasks

1. Create:
   - `ArrayList<Enemy> enemies`
   - `ArrayList<Pickup> pickups`
   - `ArrayList<Entity> world`
2. Add at least 2 enemies and 2 pickups.
3. Call `addAllEntities(enemies, world)` and then `addAllEntities(pickups, world)`.
4. Print:
   - `world.size()`
   - `world.get(0).getClass().getSimpleName()` (or similar) to show mixed types.

### Done when...

- `world` contains both enemies and pickups without casts.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Entities {
    public static void addAllEntities(java.util.List<? extends Entity> src,
                                      java.util.List<? super Entity> dst) {

        if (src == null)
            throw new NullPointerException("src must not be null");

        if (dst == null)
            throw new NullPointerException("dst must not be null");

        for (Entity e : src)
            dst.add(e);
    }
}

public class Exercise {
    public static void run() {
        java.util.List<Enemy> enemies = java.util.List.of(new Enemy(), new Enemy());
        java.util.List<Pickup> pickups = java.util.List.of(new Pickup());

        java.util.ArrayList<Entity> all = new java.util.ArrayList<>();
        Entities.addAllEntities(enemies, all);
        Entities.addAllEntities(pickups, all);

        System.out.println(all.size()); // 3
    }
}
```

</details>

---

## Exercise 07 — Fix a broken API signature (consumer list)

**Objective:** Identify a *consumer* parameter and loosen the signature so more callers can use it.

**Context (software + games):**
- **Software dev:** a method that adds audit events should accept `List<BaseEvent>` or even
  `List<Object>`.
- **Games dev:** a method that adds enemies should accept `List<Entity>` or even `List<Object>`.

### What you are building

A method that *adds enemies* into a list parameter.

Starting point (too strict):

```java
public static void addEnemies(java.util.List<Enemy> out)
```

You must change the signature so callers can pass:

- `List<Enemy>`
- `List<Entity>`
- `List<Object>`

Rules/behaviour:

- `out == null` to throw `NullPointerException`
- add at least **two** `Enemy` instances to the list

### Tasks

1. Update the signature (no overloads).
2. Implement the body (add 2 enemies).
3. In `Exercise.run()` call it three times with:
   - `new ArrayList<Enemy>()`
   - `new ArrayList<Entity>()`
   - `new ArrayList<Object>()`
4. Print the size of each list after the call.

### Done when...

- All three calls compile, and each list ends up with at least 2 items.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Spawner {
    public static void addEnemies(java.util.List<? super Enemy> out) {
        if (out == null)
            throw new NullPointerException("out must not be null");

        out.add(new Enemy());
        out.add(new Enemy());
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<Enemy> a = new java.util.ArrayList<>();
        java.util.ArrayList<Entity> b = new java.util.ArrayList<>();
        java.util.ArrayList<Object> c = new java.util.ArrayList<>();

        Spawner.addEnemies(a);
        Spawner.addEnemies(b);
        Spawner.addEnemies(c);

        System.out.println(a.size()); // 2
        System.out.println(b.size()); // 2
        System.out.println(c.size()); // 2
    }
}
```

</details>

---

## Exercise 08 — Comparator<? super T> practice

**Objective:** Understand why Java libraries often take `Comparator<? super T>`.

**Context (software + games):**
- **Software dev:** sort `List<Employee>` using a `Comparator<Person>`.
- **Games dev:** sort `List<Enemy>` using a `Comparator<Entity>`.

### What you are building

A helper that sorts a list using a comparator defined on a **supertype**.

Implement **exactly** this signature:

```java
public static <T> void sortWith(java.util.List<T> items,
                                java.util.Comparator<? super T> cmp)
```

Rules/behaviour:

- if `items` or `cmp` is null to throw `NullPointerException`
- sort in-place using `items.sort(cmp)`

### Tasks

1. Implement `sortWith`.
2. In `Exercise.run()`:
   - create a `List<Enemy>` with at least 3 enemies
   - create a `Comparator<Entity>` (it can return 0 if you don’t have a field to compare yet)
   - call `sortWith(enemies, entityComparator)`
3. Print the list before and after (even if order doesn’t change, compilation is the main win).

### Done when...

- Your method compiles with `Comparator<Entity>` + `List<Enemy>`.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Sorters {
    public static <T> void sortWith(java.util.List<T> items, java.util.Comparator<? super T> cmp) {
        if (items == null)
            throw new NullPointerException("items must not be null");

        if (cmp == null)
            throw new NullPointerException("cmp must not be null");

        items.sort(cmp);
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<Enemy> enemies = new java.util.ArrayList<>();
        enemies.add(new Enemy());
        enemies.add(new Enemy());

        java.util.Comparator<Entity> cmp = (a, b) -> 0;
        Sorters.sortWith(enemies, cmp);

        System.out.println(enemies.size()); // 2
    }
}
```

</details>

---

## Exercise 09 — Wildcard capture: swapFirstTwo(List<?>)

**Objective:** Fix “capture of ?” properly using a helper method that captures the wildcard.

**Context (software + games):**
- **Software dev:** swap the first two entries in a generic “recent items” list.
- **Games dev:** swap the first two inventory slots regardless of item type.

### What you are building

A `swapFirstTwo` operation that works for `List<String>`, `List<Integer>`, `List<Entity>` ...
any list.

Required API:

```java
public static void swapFirstTwo(java.util.List<?> items)
```

...and a helper method that captures the unknown type:

```java
private static <T> void swapFirstTwoCaptured(java.util.List<T> items)
```

Rules/behaviour:

- if `items == null` to do nothing
- if `items.size() < 2` returns do nothing
- otherwise to swap index 0 and 1

### Tasks

1. Implement the public method and helper.
2. In `Exercise.run()`:
   - swap a `List<String>` and print before/after
   - swap a `List<Integer>` and print before/after
3. Add a short comment explaining why the helper is needed (“capture of ?”).

### Done when...

- Both demos compile without casts or raw types.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Swaps {
    public static void swapFirstTwo(java.util.List<?> items) {
        if (items == null || items.size() < 2)
            return;

        swapFirstTwoCaptured(items);
    }

    private static <T> void swapFirstTwoCaptured(java.util.List<T> items) {
        T a = items.get(0);
        T b = items.get(1);

        items.set(0, b);
        items.set(1, a);
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<String> a = new java.util.ArrayList<>();
        a.add("A");
        a.add("B");

        Swaps.swapFirstTwo(a);
        System.out.println(a); // [B, A]

        java.util.ArrayList<Integer> b = new java.util.ArrayList<>();
        b.add(10);
        b.add(20);

        Swaps.swapFirstTwo(b);
        System.out.println(b); // [20, 10]
    }
}
```

</details>

---

## Exercise 10 — What can I add? What can I read?

**Objective:** Build your “wildcard instincts”: what is safe to add and what is safe to read.

**Context (software + games):**
- **Software dev:** you’re debugging wildcard-heavy APIs and need quick rules.
- **Games dev:** you’re wiring systems together and want to avoid type mistakes.

### What you are building

A short written “cheat sheet” inside your `Exercise` class as a block comment.

For each declaration below, you must write:

- what you can safely **add**
- what you can safely **read**

```java
java.util.List<?> a;
java.util.List<? extends Number> b;
java.util.List<? super Integer> c;
```

### Requirements

- Keep each answer 2–4 lines.
- Use phrases like:
  - “read as Object”
  - “add null only”
  - “read as Number”
  - “can add Integer”

### Done when...

- Your answers match what the compiler would allow.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

- `List<?> a`
  - Add: `null` only
  - Read: `Object` (because element type is unknown)

- `List<? extends Number> b`
  - Add: `null` only
  - Read: `Number` (safe upper bound)

- `List<? super Integer> c`
  - Add: `Integer` (and its subclasses), plus `null`
  - Read: `Object` (because the list might actually be `List<Object>`)

</details>

---

## Optional challenge — merge two producers into one consumer

**Objective:** Combine PECS constraints into one “real utility” method.

**Context (software + games):**
- **Software dev:** merge cached results + live results into one output list.
- **Games dev:** merge base content + DLC content into one output list.

### What you are building

A method that merges two producer lists into one consumer list, preserving order.

Implement **exactly** this signature:

```java
public static <T> void merge(java.util.List<? extends T> a,
                             java.util.List<? extends T> b,
                             java.util.List<? super T> out)
```

Rules/behaviour:

- `out == null` to throw `NullPointerException`
- treat `a == null` as empty
- treat `b == null` as empty
- append `a` then append `b`
- do not clear `out`

### Done when...

- You can merge two `List<Integer>` into an `ArrayList<Number>` without casts.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
public class Merge {
    public static <T> void merge(java.util.List<? extends T> a,
                                 java.util.List<? extends T> b,
                                 java.util.List<? super T> out) {

        if (out == null)
            throw new NullPointerException("out must not be null");

        if (a != null) {
            for (T x : a)
                out.add(x);
        }

        if (b != null) {
            for (T x : b)
                out.add(x);
        }
    }
}

public class Exercise {
    public static void run() {
        java.util.List<Integer> a = java.util.List.of(1, 2);
        java.util.List<Integer> b = java.util.List.of(3);

        java.util.ArrayList<Number> out = new java.util.ArrayList<>();
        Merge.merge(a, b, out);

        System.out.println(out); // [1, 2, 3]
    }
}
```

</details>

---

## Lesson Context

```yaml
linked_lesson:
  topic_code: "t12_generics_2"
  week_in_topic: "2_of_2"
  primary_domain_emphasis: "Balanced"
  difficulty_tier: "Intermediate"
```
