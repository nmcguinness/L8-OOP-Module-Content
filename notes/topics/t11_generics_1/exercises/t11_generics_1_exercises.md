---
title: "Generics I — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t11_generics_1
description: "Eight step-by-step exercises introducing type parameters and type safety, from a legacy Object container to generic stacks, map and filter methods, a bounded TopN, a registry, a message bus, and an object pool."
created: 2026-01-24
last_updated: 2026-11-03
version: 3.3
status: published
authors: ["OOP Teaching Team"]
tags: [java, generics, type-safety, collections, exercises, year2, comp-c8z03]
---
# Generics I — Exercises

These exercises practise writing your own generic classes and methods, from the [t11 notes](../t11_generics_1_notes.md). The aim is to see **why** generics exist: they let the compiler catch a wrong type before the program runs, and they remove the casts you would otherwise need.

A **type parameter** such as `T` is a placeholder for a type. You choose the real type when you use the class: in `UndoStack<String>`, `T` means `String`.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution and its tests.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 The crash generics prevent | `Object` and casts versus a generic class | Foundation |
| 02 A generic undo stack | a generic class backed by a list | Foundation |
| 03 A generic map method | a generic method with two type parameters | Foundation |
| 04 A generic filter method | `Predicate<T>`, returning a new list | Foundation |
| 05 Keep the top N | a bounded type, `T extends Comparable<T>` | Intermediate |
| 06 A registry | two type parameters, `K` and `V` | Intermediate |
| 07 A message bus | refactoring to a generic queue | Intermediate |
| 08 An object pool (optional) | `Supplier<T>`, reusing objects | Stretch |

## Rules for every exercise

- No raw types. Write `ArrayList<T>`, never plain `ArrayList`.
- No casts as a quick fix. Exercise 01 uses one on purpose, to show the problem.
- Use ordinary loops, not streams.

## How to run

Each exercise goes in its own package, `t11_generics_1.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`.

---

## Exercise 01 — The crash generics prevent

**Goal:** Write the same container twice: once storing `Object`, once generic. Then see that the first lets a mistake through to run time, while the second stops it at compile time.

**You will need:** [Core Idea 1](../t11_generics_1_notes.md#core-idea-1--the-object--cast-problem-why-generics-exist) and [Core Idea 2](../t11_generics_1_notes.md#core-idea-2--generic-classes-boxt-type-parameter--hole-you-fill-in-later).

**Steps**

1. Create `InventoryLegacy`. It stores items in a `List<Object>` and has `add(Object item)` and `Object get(int index)`.
2. Create `Inventory<T>`. It stores items in a `List<T>` and has `add(T item)` and `T get(int index)`.
3. In `run()`, add the text `"sword"` and the number `123` to an `InventoryLegacy`.
4. Get item 0, cast it to `String`, and print it in capitals.
5. Get item 1 and cast it to `String` too, inside a `try`. Catch `ClassCastException` and print a message.
6. Add `"shield"` to an `Inventory<String>` and print it in capitals without a cast.
7. Write `safe.add(123);` and see the compiler error. Then comment the line out.

```java
public void add(T item)
public T get(int index)
```

**Example**

```text
Legacy item 0: SWORD
Legacy item 1 crashed: ClassCastException
Generic item 0: SHIELD
```

**Rules**

- The `Inventory<T>` code in `run()` contains no casts.

<details>
<summary>Hints</summary>

1. Inside `Inventory<T>`, write `T` wherever the legacy class says `Object`.
2. The compiler error for step 7 says something like "int cannot be converted to String". That is the mistake caught before the program runs.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.
- [ ] You can explain in one or two sentences why the generic version is safer.

**Package:** `t11_generics_1.exercises.ex01`

**Solution:** [t11 exercise 01 solution code](../../../../code/src/t11_generics_1/exercises/ex01/).
**Tests:** [t11 exercise 01 tests](../../../../code/test/t11_generics_1/exercises/ex01/).

---

## Exercise 02 — A generic undo stack

**Goal:** Write a stack that works for any type: the last item pushed is the first one popped. An empty stack returns `null` instead of throwing.

**You will need:** [Core Idea 2](../t11_generics_1_notes.md#core-idea-2--generic-classes-boxt-type-parameter--hole-you-fill-in-later).

**Steps**

1. Create `UndoStack<T>` with a private `List<T>` field. The end of the list is the top of the stack.
2. `push` adds an item to the end.
3. `pop` removes and returns the last item, or returns `null` if the stack is empty.
4. `peek` returns the last item without removing it, or `null` if the stack is empty.
5. `size` returns how many items there are.
6. In `run()`, push `MoveLeft`, `Jump` and `UsePotion`. Print the size, pop once, peek, then pop until empty and print one more pop.
7. Show it works for another type: push 10 and 20 onto an `UndoStack<Integer>` and pop once.

```java
public void push(T item)
public T pop()
public T peek()
public int size()
```

**Example**

```text
Size: 3
Pop: UsePotion
Peek: Jump
Pop when empty: null
Integer pop: 20
```

**Rules**

- Popping or peeking an empty stack never throws.

<details>
<summary>Hints</summary>

1. The last item is at index `items.size() - 1`.
2. `items.remove(index)` removes **and** returns the item.

</details>

**Done when**

- [ ] `run()` prints the five lines shown.
- [ ] The same class works for `String` and `Integer` without changes.

**Package:** `t11_generics_1.exercises.ex02`

**Solution:** [t11 exercise 02 solution code](../../../../code/src/t11_generics_1/exercises/ex02/).
**Tests:** [t11 exercise 02 tests](../../../../code/test/t11_generics_1/exercises/ex02/).

---

## Exercise 03 — A generic map method

**Goal:** Write one method that turns a list of one type into a list of another type: words into their lengths, or numbers into labels. The types flow from the input to the output.

**You will need:** [Core Idea 4](../t11_generics_1_notes.md#core-idea-4--generic-methods-write-one-algorithm-reuse-for-many-types).

**Steps**

1. Create a class `Maps` with a private constructor.
2. Add the static method below. `T` is the type of the items going in, and `R` is the type coming out.
3. If `transform` is `null`, throw a `NullPointerException`.
4. If `items` is `null`, return an empty list.
5. Otherwise, loop over the items, call `transform.apply(item)` on each, and add the result to a new list.
6. In `run()`, map `["ant", "zebra"]` to their lengths and `[10, 20]` to `HP:` labels. Also map a `null` list.

```java
public static <T, R> ArrayList<R> map(List<T> items, Function<T, R> transform)
```

**Example**

```text
Lengths: [3, 5]
Labels: [HP:10, HP:20]
From null: []
```

**Rules**

- Keep the items in their original order.
- Never return `null`.

<details>
<summary>Hints</summary>

1. The `<T, R>` before the return type is what makes this a generic method.
2. `Maps.map(List.of("ant", "zebra"), word -> word.length())` gives a `List<Integer>`. Java works out `T` and `R` from the arguments.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.
- [ ] One method handles `String` to `Integer` and `Integer` to `String`.

**Package:** `t11_generics_1.exercises.ex03`

**Solution:** [t11 exercise 03 solution code](../../../../code/src/t11_generics_1/exercises/ex03/).
**Tests:** [t11 exercise 03 tests](../../../../code/test/t11_generics_1/exercises/ex03/).

---

## Exercise 04 — A generic filter method

**Goal:** Write one method that keeps only the items that pass a test, for any type. The original list is never changed.

**You will need:** [Core Idea 4](../t11_generics_1_notes.md#core-idea-4--generic-methods-write-one-algorithm-reuse-for-many-types).

**Steps**

1. Create a class `Filters` with a private constructor.
2. Add the static method `where` below.
3. If `predicate` is `null`, throw a `NullPointerException`. If `items` is `null`, return an empty list.
4. Loop over the items. Add each one for which `predicate.test(item)` is `true` to a new list.
5. In `run()`, keep the names in `["Amy", "Bob", "Ada"]` that start with `A`, and the numbers in `[5, 11, 12, 3]` that are over 10. Print the original names list afterwards.

```java
public static <T> ArrayList<T> where(List<T> items, Predicate<T> predicate)
```

**Example**

```text
Starts with A: [Amy, Ada]
Over 10: [11, 12]
Original names: [Amy, Bob, Ada]
```

**Rules**

- Return a **new** list. Do not remove anything from the list you were given.

<details>
<summary>Hints</summary>

1. A `Predicate<T>` is an object with one method, `boolean test(T item)`. A lambda such as `name -> name.startsWith("A")` is one.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.
- [ ] The original list is unchanged.

**Package:** `t11_generics_1.exercises.ex04`

**Solution:** [t11 exercise 04 solution code](../../../../code/src/t11_generics_1/exercises/ex04/).
**Tests:** [t11 exercise 04 tests](../../../../code/test/t11_generics_1/exercises/ex04/).

---

## Exercise 05 — Keep the top N

**Goal:** Build a class that remembers only the N biggest values it has seen, biggest first: a leaderboard. It works for any type that can be compared.

**You will need:** [Core Idea 5](../t11_generics_1_notes.md#core-idea-5--bounded-type-parameters-intro-t-must-have-a-capability) and [Step C](../t11_generics_1_notes.md#step-c--write-a-generic-algorithm-with-a-constraint).

**Steps**

1. Create `TopN<T extends Comparable<T>>`. The bound means "T must have a `compareTo` method".
2. The constructor takes a capacity. If it is less than 1, throw an `IllegalArgumentException`.
3. In `add`, throw a `NullPointerException` for `null`.
4. Still in `add`, find where the new item belongs. Step past every value that is bigger than or equal to it, then insert it there.
5. If the list is now longer than the capacity, remove the last (smallest) value.
6. `valuesDescending()` returns a **copy** of the list.
7. In `run()`, add 10, 7, 25, 3 and 19 to a `TopN<Integer>` of capacity 3, printing after each. Then try a `TopN<String>` of capacity 2.

```java
public TopN(int capacity)
public void add(T item)
public ArrayList<T> valuesDescending()
```

**Example**

```text
Added 10: [10]
Added 7: [10, 7]
Added 25: [25, 10, 7]
Added 3: [25, 10, 7]
Added 19: [25, 19, 10]
Strings: [zucchini, pear]
```

**Rules**

- Use a loop to find the position. Do not call `sort`.
- The list is in descending order after **every** `add`.

<details>
<summary>Hints</summary>

1. `a.compareTo(b)` is negative when `a` comes before `b`, zero when they are equal, and positive when `a` comes after `b`.
2. `while (index < values.size() && item.compareTo(values.get(index)) <= 0) { index++; }`
3. `values.add(index, item)` inserts at a position and moves the rest along.

</details>

**Done when**

- [ ] `run()` prints the six lines shown.
- [ ] Clearing the list returned by `valuesDescending()` does not change the `TopN`.

**Think about it:** why does `new TopN<Object>(3)` not compile?

**Package:** `t11_generics_1.exercises.ex05`

**Solution:** [t11 exercise 05 solution code](../../../../code/src/t11_generics_1/exercises/ex05/).
**Tests:** [t11 exercise 05 tests](../../../../code/test/t11_generics_1/exercises/ex05/).

---

## Exercise 06 — A registry

**Goal:** Wrap a `HashMap` in a class with two type parameters, one for keys and one for values, that adds one rule: keys must not be `null`.

**You will need:** [Core Idea 3](../t11_generics_1_notes.md#core-idea-3--generics-in-collections-listt-mapk-v-and-the-diamond-operator).

**Steps**

1. Create `Registry<K, V>` with a private `Map<K, V>` field.
2. Write `put`, `get` and `containsKey`. Each one passes the work to the map.
3. In each method, throw a `NullPointerException` if the key is `null`. Write that check once, in a private helper, and call it three times.
4. In `run()`, store two scores in a `Registry<String, Integer>` and look them up, including a name that is not there.
5. Store a name under the id 7 in a `Registry<Integer, String>`.
6. Try to `put` a `null` key, catch the exception, and print its message.

```java
public void put(K key, V value)
public V get(K key)
public boolean containsKey(K key)
```

**Example**

```text
Has Zara: true
Kai: 55
Nope: null
Id 7: Ana
Null key rejected: key must not be null
```

**Rules**

- A missing key gives `null` from `get`, as a normal `Map` does.
- A value may be `null`. Only the key is checked.

<details>
<summary>Hints</summary>

1. The helper can take an `Object`, because it only checks for `null`: `private static void requireKey(Object key)`.

</details>

**Done when**

- [ ] `run()` prints the five lines shown.
- [ ] All three methods reject a `null` key.

**Package:** `t11_generics_1.exercises.ex06`

**Solution:** [t11 exercise 06 solution code](../../../../code/src/t11_generics_1/exercises/ex06/).
**Tests:** [t11 exercise 06 tests](../../../../code/test/t11_generics_1/exercises/ex06/).

---

## Exercise 07 — A message bus

**Goal:** Write a generic queue that passes messages from one part of a game to another, in the order they were sent: first in, first out.

**You will need:** [Core Idea 2](../t11_generics_1_notes.md#core-idea-2--generic-classes-boxt-type-parameter--hole-you-fill-in-later).

**Steps**

1. Create `MessageBus<T>` with a private `List<T>` field.
2. `enqueue` adds a message to the end of the list.
3. `dequeue` removes and returns the message at the front, or returns `null` if there is none.
4. Add `size()` and `isEmpty()`.
5. In `run()`, enqueue `PlayerJumped`, `CoinCollected` and `LevelComplete` on a `MessageBus<String>`. Print the size, then dequeue and print messages until you get `null`.
6. Write `bus.enqueue(123);`, see the compiler error, and comment it out.

```java
public void enqueue(T message)
public T dequeue()
public int size()
public boolean isEmpty()
```

**Example**

```text
Size: 3
Handled: PlayerJumped
Handled: CoinCollected
Handled: LevelComplete
Empty: true
```

**Rules**

- Messages come out in the order they went in.

<details>
<summary>Hints</summary>

1. The front of the list is index 0, so `queue.remove(0)` takes the oldest message.
2. A loop that stops on `null`: read one message before the loop, and read the next one at the end of each pass.

</details>

**Done when**

- [ ] `run()` prints the five lines shown.

> [!NOTE]
> `remove(0)` moves every other item along one place, so it gets slower as the list grows. That is fine here. The `ArrayDeque` from t07 does not have this problem.

**Package:** `t11_generics_1.exercises.ex07`

**Solution:** [t11 exercise 07 solution code](../../../../code/src/t11_generics_1/exercises/ex07/).
**Tests:** [t11 exercise 07 tests](../../../../code/test/t11_generics_1/exercises/ex07/).

---

## Exercise 08 — An object pool (optional)

**Goal:** Build a pool that hands out objects and takes them back for reuse, so the program creates fewer new objects. Games pool bullets and particles this way to avoid pauses.

**You will need:** [Core Idea 2](../t11_generics_1_notes.md#core-idea-2--generic-classes-boxt-type-parameter--hole-you-fill-in-later). A `Supplier<T>` is an object with one method, `T get()`, that makes a new `T`.

**Steps**

1. Create `ObjectPool<T>`. Its constructor takes a `Supplier<T>` called `factory`, and throws a `NullPointerException` if it is `null`.
2. Keep the spare objects in a private `List<T>`.
3. `acquire()` removes and returns the last spare object. If there are none, it calls `factory.get()` to make a new one, and adds one to a `created` counter.
4. `release(T object)` adds the object back to the list. It rejects `null`.
5. In `run()`, make an `ObjectPool<StringBuilder>` with `StringBuilder::new`. Acquire a builder, append `hello`, release it, and acquire again.
6. Print whether you got the same object back, and what it holds. Then clear it, release it, and acquire twice.

```java
public ObjectPool(Supplier<T> factory)
public T acquire()
public void release(T object)
public int created()
```

**Example**

```text
Same object reused: true
Still holds: hello
Objects created: 2
```

**Rules**

- Reuse the most recently released object first.

<details>
<summary>Hints</summary>

1. `==` on two objects checks whether they are the **same** object, which is what you want here.
2. `builder.setLength(0)` empties a `StringBuilder`.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.
- [ ] Acquiring from an empty pool makes a new object.

**Think about it:** the reused builder still held `hello`. Whose job is it to reset an object: the code that releases it, or the pool? What could go wrong either way?

**Package:** `t11_generics_1.exercises.ex08`

**Solution:** [t11 exercise 08 solution code](../../../../code/src/t11_generics_1/exercises/ex08/).
**Tests:** [t11 exercise 08 tests](../../../../code/test/t11_generics_1/exercises/ex08/).
