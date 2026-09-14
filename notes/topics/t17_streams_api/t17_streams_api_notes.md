---
title: "Streams API"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t17_streams_api
description: "Java Streams pipeline model: creation, intermediate operations (filter, map, flatMap, sorted), terminal operations (collect, reduce, count, findFirst, anyMatch), and Collectors for grouping and joining."
created: 2026-05-27
last_updated: 2026-05-27
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, streams, functional, lambda, collectors, pipeline, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO3]
previous_topic: t16_functional_interfaces
prerequisites:
  - Functional interfaces (Consumer, Predicate, Function, Supplier)
  - Collections I & III (List, Set, Map)
  - Generics basics (T, bounded types)
---

# Streams API

> **Prerequisites:**
> - Functional interfaces: you can write lambdas for `Predicate<T>`, `Function<T,R>`, `Consumer<T>`
> - Collections: you can create and iterate `List`, `Set`, and `Map`
> - Generics basics: you can read type parameters like `Stream<T>`, `Optional<T>`

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [The pipeline model](#the-pipeline-model)
- [Part 1: Creating streams](#part-1-creating-streams)
- [Part 2: Intermediate operations](#part-2-intermediate-operations)
- [Part 3: Terminal operations](#part-3-terminal-operations)
- [Part 4: Collectors](#part-4-collectors)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Games example: entity processing pipeline](#games-example-entity-processing-pipeline)
- [Software example: task report pipeline](#software-example-task-report-pipeline)
- [Laziness and short-circuiting](#laziness-and-short-circuiting)
- [Common mistakes](#common-mistakes)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Describe the Stream pipeline model: source then zero or more intermediate ops then one terminal op. |
| Understand | Explain why streams are lazy and what that means for execution order. |
| Use | Create streams from collections, arrays, and factory methods. |
| Use | Apply intermediate operations: `filter`, `map`, `flatMap`, `distinct`, `sorted`, `limit`, `skip`, `peek`. |
| Use | Apply terminal operations: `forEach`, `collect`, `toList`, `count`, `reduce`, `findFirst`, `anyMatch`, `min`, `max`. |
| Use | Collect results with `Collectors.toList()`, `toSet()`, `joining()`, `groupingBy()`, `counting()`, `toMap()`. |
| Use | Chain operations into a single readable pipeline instead of nested loops. |
| Debug | Identify the "stream already consumed" error and avoid re-using a stream. |

---

## Why this matters

Loops work — but they mix **what** you want with **how** to iterate. A stream pipeline
separates those concerns:

```java
// Loop
List<String> result = new ArrayList<>();
for (String s : names) {
    if (s.startsWith("A")) result.add(s.toUpperCase());
}

// Stream
List<String> result = names.stream()
    .filter(s -> s.startsWith("A"))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

The stream version reads as a description of the transformation, not a recipe for how to do it.
It also composes and reuses cleanly, and can be parallelised with a single keyword change.

---

## How this builds on previous content

- `filter()` takes a `Predicate<T>` — exactly the functional interface from [t16 Functional
  Interfaces](../t16_functional_interfaces/t16_functional_interfaces_notes.md).
- `map()` takes a `Function<T,R>`.
- `forEach()` takes a `Consumer<T>`.
- `sorted()` takes a `Comparator<T>` — from [t03 Ordering](../t03_ordering/t03_ordering_notes.md).
- Collections expose `.stream()` — so everything from
  [t05](../t05_collections_1/t05_collections_1_notes.md),
  [t06](../t06_collections_2/t06_collections_2_notes.md) and
  [t07](../t07_collections_3_set_map/t07_collections_3_set_map_notes.md) is a stream source.

---

## The pipeline model

A stream pipeline has three parts:

```text
Source → [Intermediate ops] → Terminal op
```

- **Source**: where elements come from — a collection, an array, a generator.
- **Intermediate operations**: transform the stream; they are **lazy** — nothing runs until a
  terminal op is called.
- **Terminal operation**: triggers evaluation and produces a result (or side effect). A stream
  can only have one terminal op and cannot be reused after it.

---

## Part 1: Creating streams

```java
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.List;

// From a Collection
List<String> names = List.of("Alice", "Ben", "Clara");
Stream<String> s1 = names.stream();

// From an array
String[] arr = {"x", "y", "z"};
Stream<String> s2 = Arrays.stream(arr);

// From values
Stream<Integer> s3 = Stream.of(1, 2, 3, 4, 5);

// Infinite: generate (take first 5)
Stream<Double> randoms = Stream.generate(Math::random).limit(5);

// Infinite: iterate (0, 2, 4, 6, ...)
Stream<Integer> evens = Stream.iterate(0, n -> n + 2).limit(10);

// Empty stream
Stream<String> empty = Stream.empty();
```

### Primitive specialisations

Avoid boxing overhead for numeric work:

```java
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.DoubleStream;

IntStream range = IntStream.range(0, 10);       // 0..9
IntStream closed = IntStream.rangeClosed(1, 5);  // 1..5

int sum = IntStream.rangeClosed(1, 100).sum();   // 5050
```

---

## Part 2: Intermediate operations

All intermediate ops return a new `Stream` — they do not modify the source.

```java
List<String> words = List.of("apple", "ant", "banana", "avocado", "cherry", "apricot");

// filter — keep elements matching a Predicate
List<String> aWords = words.stream()
    .filter(w -> w.startsWith("a"))
    .collect(Collectors.toList());
// [apple, ant, avocado, apricot]

// map — transform each element (T → R)
List<Integer> lengths = words.stream()
    .map(String::length)
    .collect(Collectors.toList());
// [5, 3, 6, 7, 6, 7]

// sorted — natural order
List<String> sorted = words.stream()
    .sorted()
    .collect(Collectors.toList());

// sorted with Comparator
List<String> byLength = words.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList());

// distinct — remove duplicates (uses equals/hashCode)
List<Integer> unique = Stream.of(1, 2, 2, 3, 1, 4).distinct()
    .collect(Collectors.toList());
// [1, 2, 3, 4]

// limit and skip
List<String> first3 = words.stream().limit(3).collect(Collectors.toList());
List<String> skip2 = words.stream().skip(2).collect(Collectors.toList());

// peek — inspect without consuming (useful for debugging)
List<String> debug = words.stream()
    .filter(w -> w.length() > 4)
    .peek(w -> System.out.println("  after filter: " + w))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### flatMap — one-to-many

When each element maps to **multiple** elements, use `flatMap` to flatten the result:

```java
List<List<Integer>> nested = List.of(
    List.of(1, 2, 3),
    List.of(4, 5),
    List.of(6)
);

List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
// [1, 2, 3, 4, 5, 6]
```

```java
// Split sentences into individual words
List<String> sentences = List.of("hello world", "foo bar baz");
List<String> allWords = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList());
// [hello, world, foo, bar, baz]
```

---

## Part 3: Terminal operations

```java
List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);

// forEach — side effect
numbers.stream().forEach(n -> System.out.println(n));

// count
long count = numbers.stream().filter(n -> n > 3).count(); // 4

// findFirst — Optional
Optional<Integer> first = numbers.stream().filter(n -> n > 5).findFirst();
first.ifPresent(n -> System.out.println("First > 5: " + n)); // 9

// anyMatch / allMatch / noneMatch
boolean anyGt8 = numbers.stream().anyMatch(n -> n > 8);   // true
boolean allPos = numbers.stream().allMatch(n -> n > 0);   // true
boolean noneNeg = numbers.stream().noneMatch(n -> n < 0);  // true

// min / max
Optional<Integer> min = numbers.stream().min(Comparator.naturalOrder()); // 1
Optional<Integer> max = numbers.stream().max(Comparator.naturalOrder()); // 9

// reduce — combine all elements into one value
int product = numbers.stream().reduce(1, (a, b) -> a * b);
int sum = numbers.stream().mapToInt(Integer::intValue).sum(); // prefer for ints

// toList() — Java 16+ shorthand
List<Integer> collected = numbers.stream().filter(n -> n % 2 == 0).toList();
```

---

## Part 4: Collectors

```java
import java.util.stream.Collectors;

List<String> names = List.of("Alice", "Ben", "Clara", "Dave", "Eve");

// toList / toSet
List<String> list = names.stream().collect(Collectors.toList());
Set<String> set = names.stream().collect(Collectors.toSet());

// joining — concatenate strings
String joined = names.stream().collect(Collectors.joining(", ", "[", "]"));
// [Alice, Ben, Clara, Dave, Eve]

// groupingBy — Map<K, List<V>>
Map<Integer, List<String>> byLength = names.stream()
    .collect(Collectors.groupingBy(String::length));
// {3=[Ben, Eve], 4=[Dave], 5=[Alice, Clara]}

// groupingBy with downstream collector
Map<Integer, Long> countByLength = names.stream()
    .collect(Collectors.groupingBy(String::length, Collectors.counting()));
// {3=2, 4=1, 5=2}

// toMap
Map<String, Integer> nameLengths = names.stream()
    .collect(Collectors.toMap(n -> n, String::length));
// {Alice=5, Ben=3, Clara=5, Dave=4, Eve=3}

// partitioningBy — Map<Boolean, List<V>>
Map<Boolean, List<String>> partition = names.stream()
    .collect(Collectors.partitioningBy(n -> n.length() > 3));
// {false=[Ben, Eve], true=[Alice, Clara, Dave]}
```

---

## Progressive coding steps (A then B then C)

### Step A — Filter and count

```java
List<String> items = List.of("sword", "shield", "staff", "bow", "axe");

long longNames = items.stream()
    .filter(s -> s.length() > 3)
    .count();
System.out.println("Items with length > 3: " + longNames); // 3
```

### Step B — Transform and collect

```java
List<String> upperLong = items.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
System.out.println(upperLong); // [SHIELD, STAFF, SWORD]
```

### Step C — Group and summarise

```java
Map<Integer, List<String>> byLength = items.stream()
    .collect(Collectors.groupingBy(String::length));

byLength.forEach((len, words) ->
    System.out.println(len + " letters: " + words));
```

---

## Games example: entity processing pipeline

```java
import java.util.*;
import java.util.stream.*;

public record Enemy(String name, int health, String type) {}

public class EnemyProcessor {

    private final List<Enemy> _enemies;

    public EnemyProcessor(List<Enemy> enemies) {
        _enemies = enemies;
    }

    // All alive enemies (health > 0), sorted by health ascending
    public List<Enemy> aliveByHealth() {
        return _enemies.stream()
            .filter(e -> e.health() > 0)
            .sorted(Comparator.comparingInt(Enemy::health))
            .collect(Collectors.toList());
    }

    // Names of enemies of a given type, uppercased
    public List<String> namesOfType(String type) {
        return _enemies.stream()
            .filter(e -> e.type().equalsIgnoreCase(type))
            .map(Enemy::name)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    }

    // Count enemies by type
    public Map<String, Long> countByType() {
        return _enemies.stream()
            .collect(Collectors.groupingBy(Enemy::type, Collectors.counting()));
    }

    // Is any enemy below a health threshold?
    public boolean anyBelow(int threshold) {
        return _enemies.stream().anyMatch(e -> e.health() < threshold);
    }

    // Total health of all enemies
    public int totalHealth() {
        return _enemies.stream().mapToInt(Enemy::health).sum();
    }
}
```

---

## Software example: task report pipeline

```java
import java.util.*;
import java.util.stream.*;

public record Task(String title, String category, int priority, boolean done) {}

public class TaskReporter {

    private final List<Task> _tasks;

    public TaskReporter(List<Task> tasks) {
        _tasks = tasks;
    }

    // Pending tasks sorted by priority descending
    public List<Task> pendingByPriority() {
        return _tasks.stream()
            .filter(t -> !t.done())
            .sorted(Comparator.comparingInt(Task::priority).reversed())
            .collect(Collectors.toList());
    }

    // Group tasks by category
    public Map<String, List<Task>> byCategory() {
        return _tasks.stream()
            .collect(Collectors.groupingBy(Task::category));
    }

    // Summary: category → count of pending tasks
    public Map<String, Long> pendingCountByCategory() {
        return _tasks.stream()
            .filter(t -> !t.done())
            .collect(Collectors.groupingBy(Task::category, Collectors.counting()));
    }

    // Comma-separated list of done task titles
    public String doneTitles() {
        return _tasks.stream()
            .filter(Task::done)
            .map(Task::title)
            .collect(Collectors.joining(", "));
    }
}
```

---

## Laziness and short-circuiting

Streams are **lazy**: intermediate operations build a pipeline description; execution happens
only when a terminal op is called.

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "Dave");

// Only processes until the first match — stops early
Optional<String> first = names.stream()
    .filter(n -> {
        System.out.println("  testing: " + n);
        return n.length() > 4;
    })
    .findFirst();
// Output: testing: Alice (stops — Alice has length 5 > 4)
```

Short-circuiting terminal ops: `findFirst()`, `findAny()`, `anyMatch()`, `allMatch()`,
`noneMatch()`, `limit()`.

---

## Common mistakes

| Pitfall | Problem | Fix |
| :-- | :-- | :-- |
| Reusing a stream | `IllegalStateException: stream has already been operated upon or closed` | Call `.stream()` again from the source |
| Side effects in `map()` | `map()` should be pure — mutating state inside it creates subtle bugs | Use `forEach()` for side effects |
| Using `peek()` in production | `peek()` is for debugging — terminal ops can short-circuit it | Remove before production code |
| Forgetting `toList()` / `collect()` | Intermediate ops return a stream, not a List — easy to forget the terminal op | Always end with a terminal op |
| Boxing overhead in numeric pipelines | `Stream<Integer>` boxes every value | Use `IntStream`, `LongStream`, `DoubleStream` |

---

## Reflective questions

- Why can a stream only be consumed once? What would go wrong if it could be iterated multiple
  times?
- What is the difference between `map()` and `flatMap()`? Give an example where `map()` is
  wrong and `flatMap()` is right.
- When would you use `reduce()` instead of `sum()` or `count()`?
- `Collectors.groupingBy()` returns a `Map<K, List<V>>`. How would you get a `Map<K, Long>`
  (count per group) instead?
- A stream pipeline has `.filter()`, `.map()`, and `.findFirst()`. How many elements does each
  intermediate op process if the first element passes the filter?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**Why a stream is consumed once.**
A stream is not a collection — it holds no elements. It is a *pipeline description* over a
source, and running a terminal operation pulls elements through it once, consuming the source's
iterator and leaving the stream in a "used" state. Touching it again throws
`IllegalStateException: stream has already been operated upon or closed`.

Allowing re-iteration would mean either buffering everything (destroying the memory advantage
of laziness, and impossible for an infinite or IO-backed stream) or re-reading a source that
may have moved on — a file already read to the end, or a network socket. Note the fix is
trivial: re-create the stream. `list.stream()` is cheap, so store the *collection* and call
`.stream()` each time, never store a `Stream` in a field.

**`map` vs `flatMap`.**
`map` is one-to-one: n elements in, n out, each transformed. `flatMap` is one-to-many: each
element becomes a *stream*, and those are concatenated into one flat stream.

Use `flatMap` when the transformation naturally produces multiple values, and the tell is a
nested type:

```java
List<Player> players;  // each has List<String> getItems()

// WRONG - a stream of lists, not a stream of items
Stream<List<String>> nested = players.stream().map(Player::getItems);

// RIGHT - one flat stream of every item
List<String> allItems = players.stream()
        .flatMap(p -> p.getItems().stream())
        .distinct()
        .toList();
```

If you find yourself with `Stream<List<T>>`, `List<List<T>>` or `Optional<Optional<T>>`, you
wanted `flatMap`. Conversely `map` is right when each input yields exactly one output —
`Task::getTitle`.

**When `reduce` beats `sum`/`count`.**
`sum` and `count` are specialised reductions that only exist for numeric streams and only do
that one thing. Use `reduce` when the combining operation is your own: multiplying (a factorial
or a compound rate), concatenating with custom logic, folding objects into an accumulator, or
finding a max by a rule that is not a simple comparison.

```java
// No built-in for product
int product = nums.stream().reduce(1, (a, b) -> a * b);
```

If a built-in exists, prefer it — `mapToInt(...).sum()` is clearer and avoids boxing. The
reduce operation must be **associative** and side-effect-free, or parallel execution silently
produces the wrong answer.

**Getting `Map<K, Long>` instead of `Map<K, List<V>>`.**
Supply a **downstream collector** to `groupingBy`. The one-argument form defaults to
`toList()`; the two-argument form replaces that with whatever you want:

```java
Map<String, List<Task>> byCategory = tasks.stream()
        .collect(groupingBy(Task::getCategory));                        // default: toList()

Map<String, Long> countByCategory = tasks.stream()
        .collect(groupingBy(Task::getCategory, counting()));            // Long counts

Map<String, Integer> pointsPerCat = tasks.stream()
        .collect(groupingBy(Task::getCategory, summingInt(Task::getPoints)));
```

The same slot takes `mapping(...)`, `averagingDouble(...)`, `toSet()` and so on — worth
remembering, because it turns "group then loop over the lists to count" into one line. Note
`counting()` yields `Long`, not `Integer`.

**How many elements each op processes.**
`filter` processes **1**, `map` processes **1**, and `findFirst` returns immediately.

This is **laziness plus short-circuiting**, and it is the answer people most often get wrong —
the intuitive guess is that `filter` runs over the whole list, then `map` over the survivors.
It does not. Nothing runs until the terminal operation asks, and then elements are pulled **one
at a time, all the way through the pipeline**: element 1 enters `filter`, passes, goes straight
to `map`, arrives at `findFirst`, which has what it needs and stops. Elements 2..n are never
touched.

Two consequences worth keeping: an expensive `map` costs nothing on elements you never reach,
and a pipeline with no terminal operation does *nothing at all* — which is why `peek` on its
own prints nothing and is a classic debugging trap.

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t16_functional_interfaces
  domain_emphasis: Balanced

this_lesson:
  topic_code: t17_streams_api
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO3]
```
