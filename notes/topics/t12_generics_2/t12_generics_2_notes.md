---
title: "Generics II — Wildcards, Variance & PECS"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t12_generics_2
description: "Invariance, wildcards (? extends / ? super), the PECS rule, and how to design flexible, type-safe generic APIs."
created: 2026-01-22
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, generics, wildcards, pecs, variance, collections, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO2, MLO3]
previous_topic: t11_generics_1
prerequisites:
  - Generics I (type parameters, generic methods, bounded type params)
  - Collections (List, Map, iteration)
  - Comparable / Comparator basics
---

# Generics II — Wildcards, Variance & PECS

> **Prerequisites:**
> - You understand `Box<T>`, generic methods, and bounds like `T extends Comparable<T>`
> - You can use `List<T>` and basic loops

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Core Ideas / Concepts](#core-ideas--concepts)
- [Progressive examples (A then B then C)](#progressive-examples-a-then-b-then-c)
- [Games Example — Event bus payloads (producer vs
  consumer)](#games-example--event-bus-payloads-producer-vs-consumer)
- [Debugging and pitfalls](#debugging-and-pitfalls)
- [Reflective questions](#reflective-questions)
- [Appendix A — Quick reference (wildcards)](#appendix-a--quick-reference-wildcards)

---

## What you'll learn

| Skill Type | You will be able to... |
| :- | :- |
| Understand | Explain *invariance* and why `List<Enemy>` is not a `List<Entity>`. |
| Use | Choose between `List<T>`, `List<?>`, `List<? extends T>`, and `List<? super T>`. |
| Apply | Use the **PECS** rule to design method parameters correctly. |
| Analyze | Predict what operations are allowed on an `extends` list vs a `super` list (read vs write). |
| Create | Implement a type-safe `copy` / `addAll` style method using wildcards. |
| Debug | Fix compile errors caused by wildcard misuse and interpret “capture of ?” messages. |

## Why this matters

Java generics are **invariant** by default. That surprises people and causes real friction when
you want APIs to work across type hierarchies.

Example: If `Enemy` extends `Entity`, many students expect:

```java
java.util.List<Enemy> enemies = new java.util.ArrayList<>();
java.util.List<Entity> entities = enemies; // expected? but NOT allowed
```

Week 2 gives you the tools to express *variance* safely using **wildcards**.

## How this builds on previous content

| Earlier topic | Concept carried forward |
| :- | :- |
| [t11 Generics I](../t11_generics_1/t11_generics_1_notes.md) | Type parameters, bounded types and **erasure** — this topic is the second half of the same story |
| [t08 Inheritance](../t08_inheritance/t08_inheritance_notes.md) | `Enemy extends Entity` is exactly the relationship that wildcards let you exploit — and that invariance blocks by default |
| [t05](../t05_collections_1/t05_collections_1_notes.md)–[t07 Collections](../t07_collections_3_set_map/t07_collections_3_set_map_notes.md) | Every method here takes or returns a collection; PECS is about writing those signatures well |
| [t03 Ordering](../t03_ordering/t03_ordering_notes.md) | `Comparator<? super T>` finally explains a wildcard you have already been using without noticing |

Where this goes next: PECS is what makes the generic wrappers in [t20 JSON
I](../t20_json_1_jackson_basics/t20_json_1_jackson_basics_notes.md) and the `ServerResponse<T>`
of [t22 Networking](../t22_networking/t22_networking_notes.md) flexible enough to be reused
across every entity in a project.

---

## Core Ideas / Concepts

### Core Idea 1 — Invariance (the root problem)

#### Explanation

Even if `Enemy` is an `Entity`, a `List<Enemy>` is *not* a `List<Entity>`.

Why? Because if it were allowed, you could do this:

```java
class Entity { }
class Enemy extends Entity { }
class Pickup extends Entity { }

public static void demo() {
    java.util.List<Enemy> enemies = new java.util.ArrayList<>();

    // If this were allowed, we'd break type safety:
    // java.util.List<Entity> entities = enemies;
    // entities.add(new Pickup());      // legal for List<Entity>
    // Enemy e = enemies.get(0);        // would crash later
}
```

#### Snippet explanation

If `List<Enemy>` could be treated as `List<Entity>`, then someone could add a `Pickup` into a
list that is meant to contain only `Enemy`.
So Java forbids it: **generic types are invariant**.

### Core Idea 2 — Wildcards: “some type” (`?`) and why `List<?>` is read-only for adding

#### Explanation

`?` means “an unknown type”.
If you have `List<?>`, the compiler doesn’t know what element type it really holds.

```java
public static void demo(java.util.List<?> items) {
    Object first = items.get(0); // always safe

    // Not allowed (except null):
    // items.add("hi");
    // items.add(10);

    items.add(null); // the only safe value to add
}
```

#### Snippet explanation

You can **read** from a `List<?>` as `Object`.
You can’t safely **add** a non-null value, because you don’t know the list’s true element type.

### Core Idea 3 — Upper-bounded wildcards: `? extends T` (Producer Extends)

#### Explanation

`List<? extends T>` means: “a list of **some subtype** of `T`”.
This is used when you want to **read T values out** (the list *produces* values).

```java
public static double sumNumbers(java.util.List<? extends Number> nums) {
    double sum = 0.0;

    for (Number n : nums)
        sum += n.doubleValue();

    return sum;
}

public static void demo() {
    java.util.List<Integer> a = java.util.List.of(1, 2, 3);
    java.util.List<Double> b = java.util.List.of(0.5, 1.5);

    System.out.println(sumNumbers(a));
    System.out.println(sumNumbers(b));
}
```

#### Snippet explanation

Both `List<Integer>` and `List<Double>` can be passed as `List<? extends Number>` because they
are lists of something that *is a Number*.

#### Important rule

With `? extends T`, you generally **cannot add** `T` values (except `null`):

```java
public static void notAllowed(java.util.List<? extends Number> nums) {
    // nums.add(10);      // not allowed
    // nums.add(10.0);    // not allowed
    nums.add(null);       // allowed
}
```

Because the list might actually be `List<Integer>`, and adding a `Double` would be unsafe.

### Core Idea 4 — Lower-bounded wildcards: `? super T` (Consumer Super)

#### Explanation

`List<? super T>` means: “a list of `T` or any **supertype** of `T`”.
This is used when you want to **add T values into** the list (the list *consumes* values).

```java
class Entity { }
class Enemy extends Entity { }

public static void addEnemies(java.util.List<? super Enemy> out) {
    out.add(new Enemy());
    out.add(new Enemy());
}

public static void demo() {
    java.util.List<Enemy> enemies = new java.util.ArrayList<>();
    java.util.List<Entity> entities = new java.util.ArrayList<>();
    java.util.List<Object> objects = new java.util.ArrayList<>();

    addEnemies(enemies);
    addEnemies(entities);
    addEnemies(objects);
}
```

#### Snippet explanation

All three lists can *consume* `Enemy` values because they are typed to accept `Enemy` or
something more general.

#### Important rule

With `? super T`, when you read back, you only safely get `Object`:

```java
public static void readBack(java.util.List<? super Enemy> out) {
    Object x = out.get(0); // safe
    // Enemy e = out.get(0); // not safe without a cast
}
```

### Core Idea 5 — PECS (the rule-of-thumb)

**PECS** stands for:

- **P**roducer **E**xtends
- **C**onsumer **S**uper

| Intent | Use | Why |
| :- | :- | :- |
| “I only need to read items as T” | `? extends T` | The list produces T values. |
| “I need to add items of type T” | `? super T` | The list consumes T values. |

### Core Idea 6 — The classic API: `copy` (uses both extends and super)

#### Explanation

Copying means:
- you **read** from the source (producer) to `extends`
- you **write** into the destination (consumer) to `super`

```java
public static <T> void copy(java.util.List<? extends T> src, java.util.List<? super T> dst) {
    if (src == null || dst == null)
        throw new NullPointerException("src and dst must not be null");

    for (T item : src)
        dst.add(item);
}

public static void demo() {
    java.util.List<Integer> src = java.util.List.of(1, 2, 3);

    java.util.List<Number> dst1 = new java.util.ArrayList<>();
    java.util.List<Object> dst2 = new java.util.ArrayList<>();

    copy(src, dst1); // Integer -> Number
    copy(src, dst2); // Integer -> Object
}
```

#### Snippet explanation

`src` produces values of type `T` (or subtype), so we use `? extends T`.
`dst` consumes values of type `T`, so we use `? super T`.

This is PECS in practice.

### Core Idea 7 — `Comparator<? super T>` (why many Java APIs use `super`)

#### Explanation

A `Comparator<Animal>` can compare `Dog` objects too. So APIs often accept `Comparator<? super T>`.

```java
class Animal { }
class Dog extends Animal { }

class AnimalNameComparator implements java.util.Comparator<Animal> {
    @Override
    public int compare(Animal a, Animal b) {
        return 0;
    }
}

public static void demo() {
    java.util.List<Dog> dogs = new java.util.ArrayList<>();
    java.util.Comparator<Animal> cmp = new AnimalNameComparator();

    // This works because Comparator is a consumer of T in sort:
    dogs.sort(cmp); // effectively Comparator<? super Dog>
}
```

#### Snippet explanation

Sorting “consumes” elements for comparison.
So allowing a comparator of a *supertype* is safe and useful.

### Core Idea 8 — “Capture of ?” errors (what they mean)

#### Explanation

Sometimes the compiler says things like “capture of ?” when the wildcard type can’t be named
directly.

Example:

```java
public static void swapFirstTwo(java.util.List<?> items) {
    // items.add(items.get(0)); // not allowed
}
```

A common pattern is to **capture** the wildcard by delegating to a helper method with a type
parameter:

```java
public static void swapFirstTwo(java.util.List<?> items) {
    swapFirstTwoCaptured(items);
}

private static <T> void swapFirstTwoCaptured(java.util.List<T> items) {
    if (items == null || items.size() < 2)
        return;

    T a = items.get(0);
    T b = items.get(1);

    items.set(0, b);
    items.set(1, a);
}
```

#### Snippet explanation

`List<?>` can’t be safely written to (except `null`), but once we capture the unknown type as
`T`, we can operate consistently on that same `T`.

## Progressive examples (A then B then C)

### Step A — The invariance “surprise”

```java
// Enemy extends Entity
java.util.List<Enemy> enemies = new java.util.ArrayList<>();

// Not allowed:
// java.util.List<Entity> entities = enemies;
```

#### Snippet explanation

This is the core reason wildcards exist.

### Step B — Fix read-only methods with `extends`

```java
public static int totalHealth(java.util.List<? extends Number> healthValues) {
    int sum = 0;
    for (Number n : healthValues)
        sum += n.intValue();
    return sum;
}
```

#### Snippet explanation

Any numeric list can be used: integers, doubles, etc. The list is a producer.

### Step C — Fix write methods with `super`

```java
public static void spawnEnemies(java.util.List<? super Enemy> out, int count) {
    for (int i = 0; i < count; i++)
        out.add(new Enemy());
}
```

#### Snippet explanation

Any list that can accept `Enemy` objects is valid, including `List<Entity>` and `List<Object>`.

## Games Example — Event bus payloads (producer vs consumer)

Imagine an event system where listeners read events and handlers push events into a queue.

```java
interface GameEvent { }
class DamageEvent implements GameEvent { }
class PickupEvent implements GameEvent { }

public static void publishAll(java.util.List<? extends GameEvent> events,
                              java.util.List<? super GameEvent> outQueue) {
    for (GameEvent e : events)
        outQueue.add(e);
}
```

### Snippet explanation

- `events` is a producer of `GameEvent` values to `extends`
- `outQueue` consumes `GameEvent` values to `super`

This pattern appears constantly in real code (queues, pipelines, logging, batching).

## Debugging and pitfalls

| Pitfall | What you did | Why it fails | Fix |
| :- | :- | :- | :- |
| “I used `extends` but can’t add!” | `List<? extends T>` then `add(...)` | The list might be a more specific subtype list | Use `? super T` for consumers |
| “I used `super` but reads are only Object” | `List<? super T>` then `get(...)` as T | The list might be `List<Object>` | Treat reads as `Object` or redesign |
| “I wrote `List<T>` and now it rejects `List<SubT>`” | invariant parameter type | `List<SubT>` is not `List<T>` | Use `List<? extends T>` if read-only |
| Raw types creep back in | `List items = ...` | Type info is lost | Always use type arguments; avoid raw types |
| Confusing `extends` for “inheritance only” | `T extends Runnable` | In generics `extends` means “is a subtype of” | It works for interfaces too |

## Reflective questions

- Why is `List<Enemy>` not a `List<Entity>` even though `Enemy` is an `Entity`?
- For each parameter below, decide if it’s a producer or consumer, then choose the wildcard:
  - `sum(____ nums)` reads values to compute a total
  - `fill(____ out, T value)` adds values to a list
- In the `copy` method, what bug would happen if both parameters were `List<T>`?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**Why `List<Enemy>` is not a `List<Entity>`.**
Generics are **invariant**: `Enemy extends Entity` says nothing about the relationship between
`List<Enemy>` and `List<Entity>`. That looks unhelpful until you see what it prevents.

If the assignment were allowed, this would compile:

```java
List<Enemy> enemies = new ArrayList<>();
List<Entity> entities = enemies;    // imagine this were legal
entities.add(new Player());         // Player is an Entity - looks fine
Enemy e = enemies.get(0);           // ClassCastException at runtime
```

Both names refer to **one list**. Widening the reference to `List<Entity>` would let you insert
any `Entity`, including a `Player`, into a list everyone else believes contains only `Enemy`.
The crash lands on the innocent `get`, far from the `add` that caused it. Invariance rejects
line 2 instead, at compile time.

Worth contrasting: **arrays are covariant** (`Enemy[]` *is* an `Entity[]`), which is exactly
why they throw `ArrayStoreException` at runtime — Java made the unsafe choice for arrays and
the safe one for generics.

**Producer or consumer?**

- `sum(List<? extends Number> nums)` — it **reads** values to total them, so it is a
  **producer**: `extends`. This lets you pass `List<Integer>`, `List<Double>` or `List<Long>`;
  with a plain `List<Number>` only an exact `List<Number>` would be accepted. You cannot `add`
  to it (except `null`), which is fine — summing does not add.
- `fill(List<? super T> out, T value)` — it **writes** values in, so it is a **consumer**:
  `super`. `fill(List<? super Integer>, Integer)` accepts a `List<Integer>`, a `List<Number>`
  or a `List<Object>`, because an `Integer` is safe to store in any of them. Reading gives back
  only `Object`, which is fine — filling does not read.

That is **PECS**: Producer `extends`, Consumer `super`.

**The `copy` bug if both parameters were `List<T>`.**
The method still works, but only for the case where source and destination hold *exactly* the
same type — so it becomes far less useful than it should be:

```java
// Too rigid
static <T> void copy(List<T> dest, List<T> src)

// Flexible - the standard form
static <T> void copy(List<? super T> dest, List<? extends T> src)
```

With the rigid signature, `copy(numbers, integers)` where `dest` is `List<Number>` and `src` is
`List<Integer>` **fails to compile**, because `T` cannot simultaneously be `Number` and
`Integer`. Yet the operation is perfectly safe — every `Integer` *is* a `Number`, so storing
them in a `List<Number>` can never break anything.

So the "bug" is not a crash, it is a **needlessly rejected safe program**: the compiler forces
callers into casts, raw types, or copying the list first — reintroducing exactly the unsafety
generics existed to remove. The wildcard version states the real requirement — *read something
at least as specific as `T`, write into something at least as general as `T`* — which is why
`Collections.copy` in the standard library is declared precisely that way.

</details>

## Appendix A — Quick reference (wildcards)

| Pattern | Means | Typical use |
| :- | :- | :- |
| `List<T>` | exactly T | when caller and callee agree on exact type |
| `List<?>` | unknown | read as `Object`, no adds (except null) |
| `List<? extends T>` | some subtype of T | read-only producer |
| `List<? super T>` | T or supertype of T | write-focused consumer |

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t11_generics_1
  domain_emphasis: Balanced

this_lesson:
  topic_code: t12_generics_2
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO2, MLO3]
```
