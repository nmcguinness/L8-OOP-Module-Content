---
title: "Functional Interfaces — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t16_functional_interfaces
description: "Eight exercises on Predicate, Function, Consumer, Supplier and BiConsumer, ending in a composed ticket-processing pipeline."
created: 2026-02-12
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, functional-interfaces, java.util.function, lambda, method-reference, exercises, year2, comp-c8z03]
---
# Functional Interfaces — Exercises

> These exercises build directly on the **Functional Interfaces** notes.
> You will practise using **Predicate**, **Function**, **Consumer**, **BiConsumer**, and
> **Supplier** with the collections you have studied.

## Ground rules

- Prefer simple loops over streams unless explicitly asked.
- Validate inputs defensively (`null`, empty lists, blank strings).
- Use the most appropriate functional interface for the job:
  - `Predicate<T>` for yes/no checks
  - `Function<T, R>` for transforming or extracting values
  - `Consumer<T>` / `BiConsumer<T, U>` for side effects
  - `Supplier<T>` for defaults and object creation
- Do not use `record` or `final`.

## How to run

Create a class called `Exercise` and implement each exercise as a separate static method:

- `exercise01()`
- `exercise02()`
- ...
- `exercise08()`

Then call them from a `run()` method:

```java
public class Exercise {
    public static void run() {
        exercise01();
        exercise02();
        // ...
        exercise08();
    }
}
```

---

## Exercise 01 — Filter player events using Predicate

**Objective:** Use `Predicate<T>` to keep only the events you care about.

Starter types:

```java
class PlayerEvent {
    private String _playerId;
    private String _eventType;
    private int _value;

    public PlayerEvent(String playerId, String eventType, int value) {
        _playerId = playerId;
        _eventType = eventType;
        _value = value;
    }

    public String getPlayerId() { return _playerId; }
    public String getEventType() { return _eventType; }
    public int getValue() { return _value; }

    @Override
    public String toString() {
        return _playerId + " | " + _eventType + " | " + _value;
    }
}
```

Task:
- Implement:

```java
static List<PlayerEvent> filterEvents(List<PlayerEvent> events, Predicate<PlayerEvent> rule)
```

- Test it with these rules:
  - keep only `"KILL"` events
  - keep only events where `value >= 10`

---

## Exercise 02 — Combine predicates to detect suspicious activity

**Objective:** Compose predicates using `and`, `or`, and `negate`.

Task:
- Create these predicates:
- `isKill` to eventType is `"KILL"`
- `isBig` to value is at least 15
- `isNotLoot` to eventType is not `"LOOT"`
- Combine them into one rule:
  - `suspicious = isKill.and(isBig).and(isNotLoot)`
- Filter and print suspicious events.

---

## Exercise 03 — Extract fields using Function

**Objective:** Use `Function<T, R>` to extract data from objects.

Task:
- Write:

```java
static <T, R> List<R> mapTo(List<T> items, Function<T, R> selector)
```

- Use it to extract:
  - player ids
  - event types

---

## Exercise 04 — Perform side effects using Consumer

**Objective:** Use `Consumer<T>` to perform an action for each item.

Task:
- Write:

```java
static <T> void forEach(List<T> items, Consumer<T> action)
```

- Test it with:
  - printing each item
  - counting items that match a rule using a tiny `Counter` class

---

## Exercise 05 — Group items using Supplier

**Objective:** Use `Supplier<List<T>>` when creating groups.

Task:
- Write:

```java
static <T, K> Map<K, List<T>> groupBy(List<T> items, Function<T, K> keySelector, Supplier<List<T>> listFactory)
```

- Use it to group `BugTicket` objects by priority.

---

## Exercise 06 — Update a map using BiConsumer

**Objective:** Use `BiConsumer<K, V>` with a map.

Task:
- Write:

```java
static <K, V> void applyToEach(Map<K, V> map, BiConsumer<K, V> action)
```

- Use it to print counts from your grouped map.
- Create a new map where keys are renamed (HIGH->P1, MEDIUM->P2, LOW->P3).

---

## Exercise 07 — Comparator factory to sort in multiple ways

**Objective:** Use a comparator factory to sort the same list in different ways.

Task:
- Create:

```java
static Comparator<StoreItem> itemComparator(String mode)
```

- Support at least:
  - `price_asc`
  - `price_desc`
  - `stock_desc`
  - `stock_desc_then_price_asc`

---

## Exercise 08 — Ticket pipeline

**Objective:** Combine Predicate, Function, Consumer, Supplier, and a comparator factory.

Task:
- Create 10 `SupportTicket` objects.
- Filter (severity >= 7).
- Sort (severity desc, id asc).
- Group by category.
- Print top 5 and grouped counts.
- Print each handled ticket using a `Consumer<SupportTicket>`.

---

## Appendix

Imports you will likely need:

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
```
