---
title: "Functional Interfaces — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t16_functional_interfaces
description: "Eight step-by-step exercises on Predicate, Function, Consumer, Supplier and BiConsumer, ending in a composed ticket-processing pipeline."
created: 2026-02-12
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, functional-interfaces, java.util.function, lambda, method-reference, exercises, year2, comp-c8z03]
---
# Functional Interfaces — Exercises

These exercises practise passing **behaviour** into a method, from the [t16 notes](../t16_functional_interfaces_notes.md). Instead of writing a new loop for every rule, you write the loop once and pass the rule in as a lambda.

| Interface | One method | Use it for |
| :-- | :-- | :-- |
| `Predicate<T>` | `boolean test(T t)` | A yes/no check |
| `Function<T, R>` | `R apply(T t)` | Turning a `T` into an `R` |
| `Consumer<T>` | `void accept(T t)` | Doing something with each item |
| `BiConsumer<T, U>` | `void accept(T t, U u)` | Doing something with a key and value |
| `Supplier<T>` | `T get()` | Making a new object on demand |

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist.

## How to set up

Put all eight exercises in **one** class called `Exercise`, in the package `t16_functional_interfaces.exercises`. Write each exercise as a method `exercise01()` to `exercise08()`, and call them all from `run()`:

```java
public class Exercise {
    public static void run() {
        exercise01();
        exercise02();
        // ... up to exercise08()
    }
}
```

**Ground rules**

- Use simple loops, not streams. Streams come in t17; here the point is the functional interface.
- Handle awkward input: a `null` list or a `null` rule gives an empty result, never an exception.

**Solution for every exercise:** [t16 worked solutions](../../../../code/src/t16_functional_interfaces/exercises/Exercise.java), one method per exercise, checked by [the t16 tests](../../../../code/test/t16_functional_interfaces/exercises/).

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Filter events | `Predicate` | Foundation |
| 02 Suspicious activity | `and`, `negate` on predicates | Foundation |
| 03 Extract fields | `Function`, method references | Foundation |
| 04 Act on each item | `Consumer` | Foundation |
| 05 Group tickets | `Function` as a key, `Supplier` | Intermediate |
| 06 Walk a map | `BiConsumer` | Intermediate |
| 07 Sort many ways | a comparator factory | Intermediate |
| 08 Ticket pipeline | all of the above | Extension |

---

## Exercise 01 — Filter events

**Goal:** Write **one** filter method that keeps whatever items match a rule you pass in. Then use it twice with two different rules, and no second loop.

**You will need:** [The core set](../t16_functional_interfaces_notes.md#the-core-set).

**Starter type** — add this class inside `Exercise`:

```java
static class PlayerEvent {
    private String playerId;
    private String eventType;
    private int value;

    public PlayerEvent(String playerId, String eventType, int value) {
        this.playerId = playerId;
        this.eventType = eventType;
        this.value = value;
    }

    public String getPlayerId() { return playerId; }
    public String getEventType() { return eventType; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return playerId + " | " + eventType + " | " + value;
    }
}
```

**Steps**

1. Write the method below. It works for a list of **any** type `T`.
2. Loop over `items`. Call `rule.test(item)`, and keep the item if it returns `true`.
3. In `exercise01()`, make the four events in the example, and call `filterItems` twice: once keeping `"KILL"` events, and once keeping events with a value of 10 or more.

```java
static <T> List<T> filterItems(List<T> items, Predicate<T> rule)
```

**Example**

```text
events = p1 KILL 10, p2 LOOT 5, p3 KILL 20, p1 MOVE 1

rule e -> e.getEventType().equals("KILL")   keeps  p1 | KILL | 10,  p3 | KILL | 20
rule e -> e.getValue() >= 10                 keeps  p1 | KILL | 10,  p3 | KILL | 20
```

**Rules**

- Items keep their original order.
- A `null` list or a `null` rule gives an empty list.
- Return a new `ArrayList`, so the caller can sort it afterwards.

<details>
<summary>Hints</summary>

1. The method body is the same loop you would write for one fixed rule. Only the `if` changes.
2. `if (rule.test(item)) { result.add(item); }`

</details>

**Done when**

- [ ] Both rules give the two events shown.
- [ ] A rule that matches nothing gives an empty list.

---

## Exercise 02 — Suspicious activity

**Goal:** Build a complicated rule out of small, named rules joined with `and`. Each small rule is easy to read and test, and the combined rule reads like a sentence.

**You will need:**
[Predicate composition](../t16_functional_interfaces_notes.md#predicate-composition).

**Steps**

1. Create three predicates of type `Predicate<PlayerEvent>`:
   - `isKill`: the event type is `"KILL"`;
   - `isBig`: the value is 15 or more;
   - `isNotLoot`: the event type is not `"LOOT"`.
2. Combine them: `isKill.and(isBig).and(isNotLoot)`.
3. Pass the combined rule to `filterItems` from Exercise 01, and print the result.

**Example**

```text
events = p1 KILL 10, p2 KILL 20, p3 LOOT 15, p4 KILL 5
suspicious events: p2 | KILL | 20
```

p3 is big but is loot. p1 and p4 are kills but are not big.

**Rules**

- Write `isNotLoot` a second way too: as `isLoot.negate()`. Both must give the same answer.

<details>
<summary>Hints</summary>

1. Each predicate is one lambda, for example `Predicate<PlayerEvent> isBig = e -> e.getValue() >= 15;`
2. `and` returns a new `Predicate`. It does not change the predicates it joins.

</details>

**Done when**

- [ ] Only `p2 | KILL | 20` is printed.
- [ ] Swapping `isNotLoot` for `isLoot.negate()` gives the same output.

---

## Exercise 03 — Extract fields

**Goal:** Write **one** method that pulls any field out of every item in a list. Pass it a method reference to choose the field.

**You will need:** [Reading the types quickly](../t16_functional_interfaces_notes.md#reading-the-types-quickly) and [Method references](../t16_functional_interfaces_notes.md#method-references).

**Steps**

1. Write the method below. `T` is the type you start with; `R` is the type you get back.
2. Loop over `items`, call `selector.apply(item)`, and add the result to a new list.
3. Use it twice: once with `PlayerEvent::getPlayerId`, and once with `PlayerEvent::getEventType`.

```java
static <T, R> List<R> mapTo(List<T> items, Function<T, R> selector)
```

**Example**

```text
events = p1 KILL 10, p2 LOOT 5

mapTo(events, PlayerEvent::getPlayerId)    gives  [p1, p2]
mapTo(events, PlayerEvent::getEventType)   gives  [KILL, LOOT]
```

**Rules**

- The result has one entry per item, in the same order.
- A `null` list or a `null` selector gives an empty list.

<details>
<summary>Hints</summary>

1. `PlayerEvent::getPlayerId` is short for `e -> e.getPlayerId()`.
2. With that selector, Java works out that `R` is `String` for you.

</details>

**Done when**

- [ ] Both calls give the lists shown.
- [ ] A list of 3 items gives a result of 3 items.

---

## Exercise 04 — Act on each item

**Goal:** Write a `forEach` method that runs an action on every item. Use it to print, and then to count, without writing a second loop.

**You will need:** [`forEach` takes a `Consumer<T>`](../t16_functional_interfaces_notes.md#foreach-takes-a-consumert).

**Steps**

1. Write the method below. Call `action.accept(item)` for every item.
2. Add a tiny `Counter` class with an `int count`, an `increment()` method and a `getCount()` method.
3. Call `forEach` once to print every event.
4. Call it again with a lambda that calls `counter.increment()` for each `"KILL"` event, then print the count.

```java
static <T> void forEach(List<T> items, Consumer<T> action)
```

**Example**

```text
events = p1 KILL 10, p2 LOOT 5, p3 KILL 20
All events:
  p1 | KILL | 10
  p2 | LOOT | 5
  p3 | KILL | 20
KILL count: 2
```

**Rules**

- A `null` list or a `null` action does nothing.

<details>
<summary>Hints</summary>

1. Why a `Counter` object and not an `int` variable? A lambda can use a local variable only if it never changes. A `Counter` object never changes; only the number inside it does.
2. `forEach(events, e -> { if (e.getEventType().equals("KILL")) { counter.increment(); } });`

</details>

**Done when**

- [ ] The output matches the example.

---

## Exercise 05 — Group tickets

**Goal:** Write a general `groupBy` that sorts items into lists by a key you choose. A `Supplier` decides what kind of list each group gets, so the caller stays in control.

**You will need:** [`Map.computeIfAbsent` takes a `Function<K, V>`](../t16_functional_interfaces_notes.md#mapcomputeifabsent-takes-a-functionk-v).

**Starter type** — add this class inside `Exercise`:

```java
static class BugTicket {
    private String id;
    private String priority;

    public BugTicket(String id, String priority) {
        this.id = id;
        this.priority = priority;
    }

    public String getId() { return id; }
    public String getPriority() { return priority; }

    @Override
    public String toString() { return id + "(" + priority + ")"; }
}
```

**Steps**

1. Write the method below. `keySelector` picks the group; `listFactory` makes a new, empty list.
2. For each item, work out its key, and add the item to that key's list. Create the list with `listFactory.get()` the first time a key is seen.
3. Group the five tickets in the example by `BugTicket::getPriority`, passing `ArrayList::new` as the factory.

```java
static <T, K> Map<K, List<T>> groupBy(List<T> items,
                                      Function<T, K> keySelector,
                                      Supplier<List<T>> listFactory)
```

**Example**

```text
tickets = BUG-01 HIGH, BUG-02 LOW, BUG-03 HIGH, BUG-04 MEDIUM, BUG-05 LOW

HIGH -> [BUG-01(HIGH), BUG-03(HIGH)]
MEDIUM -> [BUG-04(MEDIUM)]
LOW -> [BUG-02(LOW), BUG-05(LOW)]
```

The groups may print in a different order, because a `HashMap` has no order.

**Rules**

- Inside each group, items keep their original order.
- A `null` list, selector or factory gives an empty map.

<details>
<summary>Hints</summary>

1. `ArrayList::new` is a constructor reference: a `Supplier` that makes a new `ArrayList` each time `get()` is called.
2. `result.computeIfAbsent(key, k -> listFactory.get()).add(item);`

</details>

**Done when**

- [ ] The three groups contain the tickets shown.

---

## Exercise 06 — Walk a map

**Goal:** Run an action on every key and value of a map. Use it twice: to print counts, and to build a new map with renamed keys.

**You will need:** [`Bi...` variants: when you need two inputs](../t16_functional_interfaces_notes.md#bi-variants-when-you-need-two-inputs).

**Steps**

1. Write the method below. Loop over `map.entrySet()` and call `action.accept(entry.getKey(), entry.getValue())`.
2. Group four tickets by priority with your Exercise 05 method.
3. Call `applyToEach` with a lambda that prints each priority and the size of its list.
4. Make a rename map: `HIGH` to `P1`, `MEDIUM` to `P2`, `LOW` to `P3`. Call `applyToEach` again to copy every group into a new map under its new name.

```java
static <K, V> void applyToEach(Map<K, V> map, BiConsumer<K, V> action)
```

**Example**

```text
tickets = BUG-01 HIGH, BUG-02 LOW, BUG-03 HIGH, BUG-04 MEDIUM

Counts:
  HIGH: 2
  MEDIUM: 1
  LOW: 1
Remapped keys: [P1, P2, P3]
```

The lines may come out in a different order.

**Rules**

- A `null` map or a `null` action does nothing.
- A key that is not in the rename map keeps its old name.

<details>
<summary>Hints</summary>

1. The lambda takes two parameters: `(k, v) -> System.out.println("  " + k + ": " + v.size())`.
2. `rename.getOrDefault(k, k)` gives the new name, or the old one if there is none.

</details>

**Done when**

- [ ] The counts match the example.
- [ ] The new map has the keys `P1`, `P2` and `P3`.

---

## Exercise 07 — Sort many ways

**Goal:** Write a **factory** method that returns a ready-made comparator for a sort order named by a string. Callers choose an order without knowing how comparators are built.

**You will need:** [`sort` takes a `Comparator<T>`](../t16_functional_interfaces_notes.md#sort-takes-a-comparatort-a-functional-interface) and [t03 Ordering](../../t03_ordering/t03_ordering_notes.md).

**Starter type:** a `StoreItem` with `name`, `price` (`double`) and `stock` (`int`), getters, and `toString()` returning, for example, `Potion(€5.0, stock=50)`.

**Steps**

1. Write the method below, using a `switch` on `mode`.
2. Support these four modes:

   | Mode | Order |
   | :-- | :-- |
   | `price_asc` | cheapest first |
   | `price_desc` | dearest first |
   | `stock_desc` | most stock first |
   | `stock_desc_then_price_asc` | most stock first; equal stock, cheapest first |

3. Sort the four items in the example with each mode, and print the list.

```java
static Comparator<StoreItem> itemComparator(String mode)
```

**Example**

```text
items = Shield €25 stock 10, Sword €45 stock 3, Potion €5 stock 50, Helmet €30 stock 10

price_asc:                 Potion, Shield, Helmet, Sword
stock_desc_then_price_asc: Potion, Shield, Helmet, Sword
```

Shield and Helmet both have 10 in stock, so the cheaper Shield comes first.

**Rules**

- An unknown mode throws `IllegalArgumentException` with the mode in the message.

<details>
<summary>Hints</summary>

1. `Comparator.comparingDouble(StoreItem::getPrice)` is cheapest first; add `.reversed()` for dearest first.
2. For the two-level order: `Comparator.comparingInt(StoreItem::getStock).reversed().thenComparingDouble(StoreItem::getPrice)`

</details>

**Done when**

- [ ] Each mode gives the order in its table row.
- [ ] `itemComparator("random")` throws `IllegalArgumentException`.

---

## Exercise 08 — Ticket pipeline (extension)

**Goal:** Combine everything from Exercises 01 to 07 into one small pipeline: filter, sort, group, and act on each ticket.

**You will need:** your methods from Exercises 01, 04, 05 and 06.

**Starter type:** a `SupportTicket` with `id` (`int`), `category` (`String`) and `severity` (`int`), getters, and `toString()` returning, for example, `T#1[NETWORK, sev=9]`.

**Steps**

1. Create these 10 tickets:

   ```text
   1 NETWORK 9    2 UI 5     3 DB 8     4 NETWORK 7    5 UI 6
   6 DB 9         7 AUTH 3   8 AUTH 8   9 NETWORK 2    10 DB 7
   ```

2. **Filter** with `filterItems`: keep tickets with a severity of 7 or more.
3. **Sort** the result: highest severity first, and for equal severity, lowest id first.
4. **Group** the sorted tickets by category with `groupBy`.
5. Print the first 5 tickets, then the count for each category, using `applyToEach`.
6. Use `forEach` with a `Consumer<SupportTicket>` that prints the word `Handled:` and then the ticket.

**Example**

```text
Top 5 high-severity tickets:
  T#1[NETWORK, sev=9]
  T#6[DB, sev=9]
  T#3[DB, sev=8]
  T#8[AUTH, sev=8]
  T#4[NETWORK, sev=7]
Grouped counts:
  NETWORK: 2
  AUTH: 1
  DB: 3
Handled tickets:
  Handled: T#1[NETWORK, sev=9]
  ... six tickets in all, ending with T#10[DB, sev=7]
```

**Rules**

- Six tickets pass the filter, so "top 5" shows five and the handler shows all six.
- If fewer than 5 tickets pass, print them all; do not crash.

<details>
<summary>Hints</summary>

1. `filterItems` returns an `ArrayList`, so you can call `.sort(...)` on its result directly.
2. Sort with `Comparator.comparingInt(SupportTicket::getSeverity).reversed().thenComparingInt(SupportTicket::getId)`.

</details>

**Done when**

- [ ] The top 5 and the counts match the example.
- [ ] All six filtered tickets are handled.
