---
title: "Generics I — Type Parameters & Type Safety"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t11_generics_1
description: "Why generics exist, how to write generic classes and methods, and how generics make Java APIs safer and easier to reuse — week 1 of 2, before wildcards and PECS."
created: 2026-01-22
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, generics, type-safety, collections, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO2, MLO3]
previous_topic: t10_exception_handling
prerequisites:
  - Arrays & ArrayList basics
  - Collections & iteration patterns
  - Inheritance and interfaces (polymorphism)
---

# Generics I — Type Parameters & Type Safety

> **Prerequisites:**
> - You can create classes with fields, constructors, and methods
> - You can use `ArrayList` / `LinkedList` with loops
> - You understand inheritance and interface references (polymorphism)

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Core Ideas / Concepts](#core-ideas--concepts)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Useful snippets (guards and helpers)](#useful-snippets-guards-and-helpers)
- [Games Example — A type-safe inventory slot](#games-example--a-type-safe-inventory-slot)
- [Software Development Example — A reusable repository result
  wrapper](#software-development-example--a-reusable-repository-result-wrapper)
- [Debugging and pitfalls](#debugging-and-pitfalls)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

| Skill Type | You will be able to... |
|:-|:-|
| Understand | Explain the problem generics solve (casts, runtime type errors, reusable code). |
| Use | Write a simple generic class (e.g., `Box<T>`) and use it safely. |
| Use | Use Java Collections with generics (`List<T>`, `Map<K, V>`) and the diamond operator `<>`. |
| Analyze | Trace how type parameters flow through a method and stop invalid calls at compile time. |
| Analyze | Recognise when you need a *bounded* type parameter (e.g., “must be comparable”). |
| Debug | Interpret raw-type warnings and fix them by adding correct type arguments. |

## Why this matters

Most real programs need **reusable containers and reusable algorithms**.

- In **games**, you might store things like: inventory items, pooled objects, UI widgets, event
  payloads, path nodes, or AI “blackboard” values.
- In **software**, you might store: database records, API responses, cached values, log
  entries, or validation errors.

Without generics, you quickly end up with:
- code that stores everything as `Object`
- lots of casts (`(Player) x`)
- bugs that only show up at runtime (e.g., `ClassCastException`)

Generics let you build **reusable** code that is also **type-safe**.

## How this builds on previous content

- From **Collections**, you learned that lists and maps store *many* objects. Generics let
  those collections store objects **of a specific type** safely.
- From **Inheritance / Interfaces**, you learned polymorphism. Generics let you write
  containers and methods that work across *many* types while still being checked by the
  compiler.
- This topic prepares you for later work where libraries heavily use generics (e.g.,
  `Comparator<T>`, `Optional<T>`, streams, test helpers).

## Core Ideas / Concepts

> For each core idea, we first explain the concept, then show a short code example, followed by
> a brief explanation of how the example illustrates the idea.

### Core Idea 1 — The “Object + cast” problem (why generics exist)

#### Explanation

Before generics (or when you avoid them), a common workaround is: “just store everything as
`Object`”.
That forces the caller to **cast** back to the expected type.

```java
 class BoxObject {
    private Object value;

    public BoxObject(Object value) {
        this.value = value;
    }

    public Object value() {
        return value;
    }
}

public static void demo() {
    BoxObject box = new BoxObject("hi");

    String s = (String) box.value(); // OK at runtime
    System.out.println(s.toUpperCase());

    // This compiles, but can crash at runtime:
    Integer n = (Integer) box.value(); // ClassCastException at runtime
    System.out.println(n + 1);
}
```

#### Snippet explanation

`BoxObject` can store anything, but the caller has no compile-time guarantee about what’s inside.
The code compiles, but one wrong cast becomes a runtime crash.

### Core Idea 2 — Generic classes: `Box<T>` (type parameter = “hole you fill in later”)

#### Explanation

A generic class uses a **type parameter** (like `T`) to represent “the type this class works with”.

- `T` is not a real runtime type
- it’s a placeholder that gets replaced with a real type when you use the class

```java
 class Box<T> {
    private T value;

    public Box(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }
}

public static void demo() {
    Box<String> a = new Box<>("hi");
    System.out.println(a.value().toUpperCase()); // no cast needed

    Box<Integer> b = new Box<>(42);
    System.out.println(b.value() + 1);

    // This does NOT compile (and that's the point):
    // Box<Integer> c = new Box<>("oops");
}
```

#### Snippet explanation

`Box<String>` is a “box that holds Strings”. `Box<Integer>` is a “box that holds Integers”.
If you try to put the wrong type in, the compiler stops you.

### Core Idea 3 — Generics in Collections (`List<T>`, `Map<K, V>`) and the diamond operator

#### Explanation

The Collections API uses generics everywhere:

- `List<T>`: a list of T
- `Map<K, V>`: keys of K, values of V

The **diamond operator** `<>` lets Java infer the type arguments on the right-hand side.

```java
java.util.ArrayList<String> names = new java.util.ArrayList<>();
names.add("Zara");
names.add("Kai");
// names.add(123); // does not compile

for (String n : names) {
    System.out.println(n.toUpperCase());
}

java.util.HashMap<String, Integer> scores = new java.util.HashMap<>();
scores.put("Zara", 100);
scores.put("Kai", 55);

int z = scores.get("Zara"); // auto-unboxing from Integer to int
System.out.println(z);
```

#### Snippet explanation

`names` can only contain strings, so you don’t need casts when looping.
`scores` guarantees that keys are `String` and values are `Integer`.

### Core Idea 4 — Generic methods (write one algorithm, reuse for many types)

#### Explanation

A **generic method** has its own type parameter list, written before the return type.

```java
public static <T> T first(java.util.List<T> items) {
    if (items == null || items.isEmpty())
        return null;
    return items.get(0);
}

public static void demo() {
    java.util.List<String> a = java.util.List.of("A", "B");
    java.util.List<Integer> b = java.util.List.of(10, 20);

    String s = first(a);
    Integer n = first(b);

    System.out.println(s);
    System.out.println(n);
}
```

#### Snippet explanation

The same `first` method works for `List<String>`, `List<Integer>`, and any other `List<T>`.
The caller gets back the correct type without casting.

### Core Idea 5 — Bounded type parameters (intro): “T must have a capability”

#### Explanation

Sometimes your algorithm needs the type to support a specific operation.
Example: “find the max” requires comparing elements.

You express that using `extends` in the type parameter:

```java
public static <T extends Comparable<T>> T max(java.util.List<T> items) {
    if (items == null || items.isEmpty())
        return null;

    T best = items.get(0);

    for (int i = 1; i < items.size(); i++) {
        T candidate = items.get(i);
        if (candidate.compareTo(best) > 0)
            best = candidate;
    }

    return best;
}
```

#### Snippet explanation

`T extends Comparable<T>` means: “T must be comparable to other Ts”.
Now the compiler allows `compareTo` and prevents types that can’t be compared.

> Week 2 will expand this idea using **wildcards** (`? extends ...`, `? super ...`) for more
> flexible APIs.

### Core Idea 6 — A practical mental model: generics are compile-time only (type erasure)

#### Explanation

In Java, generics are implemented using **type erasure**: type parameters don’t exist as real
runtime types.

This explains several rules you’ll meet in practice:
- you can’t do `new T()`
- you can’t do `T.class`
- you can’t create `new T[10]`
- you can’t use primitives as type arguments (use `Integer`, `Double`, etc.)

```java
 class Factory<T> {

    // This is NOT allowed:
    // public T make() { return new T(); }

    // One common workaround is to pass in a supplier:
    private java.util.function.Supplier<T> _supplier;

    public Factory(java.util.function.Supplier<T> supplier) {
        _supplier = supplier;
    }

    public T make() {
        return _supplier.get();
    }
}
```

#### Snippet explanation

The generic parameter `T` is a compile-time guarantee, but you can’t “construct a T” unless you
also provide a construction strategy (here, a `Supplier<T>`).

## Progressive coding steps (A then B then C)

### Step A — Start with the problem: `Object` storage

```java
BoxObject box = new BoxObject("hi");

// safe cast (because we happen to know it's a String)
String s = (String) box.value();

// unsafe cast (compiles, may crash)
Integer n = (Integer) box.value();
```

#### Snippet explanation

Step A illustrates why “Object containers” are dangerous: the compiler can’t protect you.

### Step B — Replace with a generic container

```java
Box<String> a = new Box<>("hi");
Box<Integer> b = new Box<>(42);

// No casts needed:
System.out.println(a.value().toUpperCase());
System.out.println(b.value() + 1);
```

#### Snippet explanation

Step B shows the basic win: type-safe reuse without casts.

### Step C — Write a generic algorithm with a constraint

```java
java.util.List<Integer> nums = java.util.List.of(10, 3, 25);
System.out.println(max(nums)); // 25

java.util.List<String> words = java.util.List.of("ant", "zebra", "cat");
System.out.println(max(words)); // zebra (lexicographic)
```

#### Snippet explanation

Step C shows how generics scale: you can write “max” once and reuse it across many types—*as
long as* they meet the bound.

## Useful snippets (guards and helpers)

```java
// Quick “require not null” helper (reuses Java’s standard exception)
public static <T> T requireNotNull(T value, String message) {
    if (value == null)
        throw new NullPointerException(message);
    return value;
}
```

### Snippet explanation

This generic helper works for any reference type and returns the same type it receives.
It’s a tiny example of how generics let you write one helper and reuse it everywhere.

```java
// Generic swap for arrays (note: arrays are reifiable; generics are erased)
public static <T> void swap(T[] arr, int i, int j) {
    T tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
}
```

### Snippet explanation

`swap` works for `String[]`, `Integer[]`, and any `T[]`. The method never needs to know what T is.

## Games Example — A type-safe inventory slot

In a game, you often want an inventory slot that holds “one thing”, but that “thing” could be
different kinds of items.

```java
interface Item {
    String name();
}

 class Potion implements Item {
    private String _name;

    public Potion(String name) {
        _name = name;
    }

    @Override
    public String name() {
        return _name;
    }
}

 class Sword implements Item {
    private String _name;

    public Sword(String name) {
        _name = name;
    }

    @Override
    public String name() {
        return _name;
    }
}

 class Slot<T extends Item> {
    private T _item; // can be null for “empty”

    public void put(T item) {
        _item = item;
    }

    public T take() {
        T out = _item;
        _item = null;
        return out;
    }
}

public static void demo() {
    Slot<Potion> potions = new Slot<>();
    potions.put(new Potion("Health"));

    // potions.put(new Sword("Iron")); // does not compile

    Potion p = potions.take();
    System.out.println(p.name());
}
```

### Snippet explanation

`Slot<Potion>` guarantees that only potions can be put into that slot.
This removes an entire category of bugs (“wrong item type”) without any runtime checks.

## Software Development Example — A reusable repository result wrapper

In software systems, you often return “a result + maybe an error” from many different operations.

```java
 class Result<T> {
    private T _value;
    private String _error;

    private Result(T value, String error) {
        _value = value;
        _error = error;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> fail(String error) {
        return new Result<>(null, error);
    }

    public boolean isOk() {
        return _error == null;
    }

    public T value() {
        return _value;
    }

    public String error() {
        return _error;
    }
}

public static void demo() {
    Result<Integer> r = Result.ok(10);
    if (r.isOk())
        System.out.println(r.value() + 5);
}
```

### Snippet explanation

`Result<T>` is a pattern you can reuse across dozens of methods.
The caller gets strong typing for the success value (`T`) while still having a place for an
error message.

## Debugging and pitfalls

| Pitfall | Why it happens | How to fix / avoid |
| :- | :- | :- |
| Using raw types (e.g., `List` instead of `List<String>`) | Older Java examples, or you omit the type argument | Always specify type arguments. Treat raw-type warnings as “future bug” warnings. |
| “I had to cast anyway” | The reference was typed as `Object` or you used a raw type somewhere | Fix the *first* place the type info was lost. Type safety propagates from there. |
| Trying to use primitives (e.g., `List<int>`) | Generics only work with reference types | Use wrapper classes: `Integer`, `Double`, `Boolean`, etc. |
| Trying to `new T()` or `new T[]` | Type erasure removes runtime access to `T` | Pass a factory (`Supplier<T>`), or redesign the API. |
| Confusing bounds syntax (`extends` used for interfaces too) | In generics, `extends` means “is a subtype of” | Remember: `T extends Runnable` is valid even though `Runnable` is an interface. |
| “Why won’t `List<Enemy>` fit where `List<Entity>` is expected?” | Generic types are invariant in Java | Week 2 covers wildcards (`? extends ...`) to model “read-only” vs “write-only” lists. |

## Reflective questions

- Where in your current projects do you still use `Object` and casts? Could generics remove them?
- Why is the compiler refusing `Box<Integer> c = new Box<>("oops")`? What bug did it prevent?
- If you were designing an API for students, what generic types would you expose, and what
  would you hide?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**Where `Object` and casts still lurk.**
Look for three signatures: a field or collection declared as `Object` (or a raw `List` with no
type argument), a method returning `Object` that every caller immediately casts, and any
`(Cast)` paired with an `instanceof` check. All three are the same smell — the type was known
when the value went in, thrown away in the middle, and reconstructed by hand on the way out.

Generics remove them in the common case, because the type parameter carries that knowledge
through for you. They are *not* a fit when the type genuinely varies per element and is unknown
at compile time — reading heterogeneous JSON, or reflection. There, `Object` plus `instanceof`
is honest; the modern form is pattern matching, `if (o instanceof String s)`, which at least
fuses the test and the cast so they cannot disagree.

**Why `Box<Integer> c = new Box<>("oops")` is rejected.**
`Box<Integer>` promises the box holds `Integer`s, and the diamond `<>` infers its argument from
that declared type — so the constructor is `Box<Integer>(Integer)` and a `String` does not fit.
The compiler is enforcing the promise at the point the object is built.

The bug it prevents is a `ClassCastException` **later, somewhere else**. Without generics the
`String` goes in happily and the failure surfaces at the eventual `(Integer) box.get()` —
possibly in different code, written by someone else, long after the mistake. That is the whole
argument for generics: the same bug, moved from a runtime crash at an arbitrary location to a
compile error at the exact line that is wrong. Compile-time failures are cheap; production
`ClassCastException`s are not.

**Designing a generic API: what to expose, what to hide.**
Expose a type parameter when the **caller's type genuinely flows through** your API and they
would otherwise have to cast. `Repository<T>`, `Response<T>`, `Cache<K, V>` — the caller gets
`Task` back from a `Repository<Task>` with no cast, and passing the wrong type is a compile
error.

Hide it when the type is an **internal implementation detail**. If a class stores results in a
`HashMap<String, List<Task>>` internally, callers should see `List<Task>
findByCategory(String)`, not the map's parameters — otherwise the storage choice becomes part
of your public contract and you can never change it.

Two design rules worth stating explicitly:

- **Be liberal in what you accept, specific in what you return.** Take `Collection<? extends
  T>` as a parameter so any subtype collection works; return a concrete `List<T>` so the caller
  needs no wildcard gymnastics. That asymmetry is PECS, developed in
  [t12](../t12_generics_2/t12_generics_2_notes.md).
- **Bound a parameter only when you use the capability.** Write `<T extends Comparable<T>>` if
  you call `compareTo`; leaving it unbounded when you need ordering forces a cast back, and
  bounding it when you do not need it needlessly narrows who can use your API.

Keep the number of parameters small. `Cache<K, V>` is readable; `Handler<T, R, E, C>` is a sign
the class is doing too much.

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t10_exception_handling
  domain_emphasis: Balanced

this_lesson:
  topic_code: t11_generics_1
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO2, MLO3]
```
