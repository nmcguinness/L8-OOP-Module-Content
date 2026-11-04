---
title: "Generics II — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t12_generics_2
description: "Eleven step-by-step exercises on wildcards, variance and PECS: invariance, reading any list, producer and consumer methods, copy, a supertype comparator, wildcard capture, and repairing a too-strict signature."
created: 2026-01-24
last_updated: 2026-11-03
version: 2.4
status: published
authors: ["OOP Teaching Team"]
tags: [java, generics, wildcards, pecs, variance, collections, exercises, year2, comp-c8z03]
---
# Generics II — Exercises

These exercises practise reading wildcard types and choosing the right one when you design a method, from the [t12 notes](../t12_generics_2_notes.md). Three ideas come up again and again:

- **Invariance:** a `List<Enemy>` is **not** a `List<Entity>`, even though an `Enemy` is an `Entity`.
- **Producer extends:** if a method only **reads** from a list, write `List<? extends T>`.
- **Consumer super:** if a method only **adds** to a list, write `List<? super T>`.

The last two together are called **PECS**: Producer Extends, Consumer Super.

Most exercises use a small class hierarchy: `Entity`, with subclasses `Enemy` and `Pickup`. Each has a name, which `toString()` returns.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution, and to its tests where there are some.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Prove invariance | why `List<Enemy>` is not a `List<Entity>` | Foundation |
| 02 Print any list | `List<?>` | Foundation |
| 03 Sum any numbers | `? extends Number` (a producer) | Foundation |
| 04 Fill a list | `? super T` (a consumer) | Intermediate |
| 05 Copy between lists | PECS: `extends` and `super` together | Intermediate |
| 06 Build the world list | PECS on your own class hierarchy | Intermediate |
| 07 Fix a too-strict signature | spotting a consumer parameter | Intermediate |
| 08 Sort with a supertype comparator | `Comparator<? super T>` | Intermediate |
| 09 Swap the first two | wildcard capture | Stretch |
| 10 What can I add, what can I read? | a written cheat sheet | Intermediate |
| 11 Merge two lists (optional) | two producers, one consumer | Stretch |

## Rules for every exercise

- Do not fix a wildcard error with a cast or a raw type.
- When an exercise asks you to show a compile error, write the line, read the error, then comment the line out so the program still builds.
- Use ordinary loops, not streams.

## How to run

Each exercise goes in its own package, `t12_generics_2.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`.

---

## Exercise 01 — Prove invariance

**Goal:** Show, with a compile error, that a list of enemies cannot be used as a list of entities, and explain why Java has to forbid it.

**You will need:**
[Core Idea 1](../t12_generics_2_notes.md#core-idea-1--invariance-the-root-problem) and [Step A](../t12_generics_2_notes.md#step-a--the-invariance-surprise).

**Steps**

1. Create a class `Entity` with a `name` field, a constructor, `getName()` and a `toString()` that returns the name.
2. Create `Enemy` and `Pickup`, which both extend `Entity`.
3. In `run()`, store a `new Enemy("Orc")` in a variable of type `Entity` and print it. Ordinary inheritance allows this.
4. Create a `List<Enemy>`, add a `Goblin`, and print it.
5. Write `List<Entity> entities = enemies;`. Read the compiler error, then comment the line out.
6. Under it, write a comment: what could go wrong if Java allowed that line?

**Example**

```text
An Enemy is an Entity: Orc
Enemies: [Goblin]
```

**Rules**

- The failing line stays in your code, commented out, with your explanation under it.

<details>
<summary>Hints</summary>

1. Imagine the line compiled. What would stop `entities.add(new Pickup("Coin"))`? And what would `enemies.get(1)` hand back?

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] Your comment explains how a `Pickup` could end up in a list of enemies.

**Package:** `t12_generics_2.exercises.ex01`

**Solution:** [t12 exercise 01 solution code](../../../../code/src/t12_generics_2/exercises/ex01/).

---

## Exercise 02 — Print any list

**Goal:** Write one method that prints a list of anything: strings, numbers or entities. The wildcard `?` means "some type, I do not know which".

**You will need:** [Core Idea 2](../t12_generics_2_notes.md#core-idea-2--wildcards-some-type--and-why-list-is-read-only-for-adding).

**Steps**

1. Copy your `Entity`, `Enemy` and `Pickup` classes into this package.
2. Create a class `Lists` with a private constructor and the static method below.
3. If `items` is `null`, return without printing. Otherwise print each element on its own line, indented by two spaces.
4. In `run()`, print a `List<String>`, a `List<Integer>` and a `List<Entity>` with the same method.
5. Try to `add` an `Enemy` to a `List<?>`. Read the error, then comment the line out.

```java
public static void printAll(List<?> items)
```

**Example**

```text
Strings:
  sword
  shield
  bow
Integers:
  10
  20
  30
Entities:
  Orc
  Coin
```

**Rules**

- One method, no overloads, no casts.

<details>
<summary>Hints</summary>

1. Each element of a `List<?>` can be read as an `Object`: `for (Object item : items) { ... }`.
2. Why not `List<Object>`? Because, by Exercise 01, a `List<String>` is not a `List<Object>`.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] `printAll(null)` prints nothing and does not crash.

**Package:** `t12_generics_2.exercises.ex02`

**Solution:** [t12 exercise 02 solution code](../../../../code/src/t12_generics_2/exercises/ex02/).
**Tests:** [t12 exercise 02 tests](../../../../code/test/t12_generics_2/exercises/ex02/).

---

## Exercise 03 — Sum any numbers

**Goal:** Add up a list of integers or a list of doubles with the same method. The list **produces** numbers for you to read, so it uses `extends`.

**You will need:** [Core Idea 3](../t12_generics_2_notes.md#core-idea-3--upper-bounded-wildcards--extends-t-producer-extends).

**Steps**

1. Create a class `Numbers` with a private constructor and the static method below.
2. If `nums` is `null`, return `0.0`.
3. Loop over the list and add up `n.doubleValue()` for each number.
4. In `run()`, sum `[1, 2, 3]`, `[0.5, 1.5, 2.0]` and `null`.
5. Try to `add(1)` to a `List<? extends Number>`. Read the error, then comment the line out.

```java
public static double sumNumbers(List<? extends Number> nums)
```

**Example**

```text
Integers: 6.0
Doubles: 4.0
Null: 0.0
```

**Rules**

- One method handles both list types.

<details>
<summary>Hints</summary>

1. Every element can be read as a `Number`, and every `Number` has `doubleValue()`.
2. The `add` fails because the list might really be a `List<Double>`. An `Integer` does not belong in that.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.

**Package:** `t12_generics_2.exercises.ex03`

**Solution:** [t12 exercise 03 solution code](../../../../code/src/t12_generics_2/exercises/ex03/).
**Tests:** [t12 exercise 03 tests](../../../../code/test/t12_generics_2/exercises/ex03/).

---

## Exercise 04 — Fill a list

**Goal:** Write a method that adds the same value to a list several times. The list **consumes** values, so it uses `super`, and it can be a list of a more general type.

**You will need:** [Core Idea 4](../t12_generics_2_notes.md#core-idea-4--lower-bounded-wildcards--super-t-consumer-super).

**Steps**

1. Create a class `Fillers` with a private constructor and the static method below.
2. If `out` is `null`, throw a `NullPointerException`. If `count` is negative, throw an `IllegalArgumentException`.
3. Add `value` to `out` exactly `count` times.
4. In `run()`, fill a `List<Object>` with `"hi"` three times, and a `List<Number>` with `5` twice.

```java
public static <T> void fill(List<? super T> out, T value, int count)
```

**Example**

```text
Objects: [hi, hi, hi]
Numbers: [5, 5]
```

**Rules**

- A count of 0 adds nothing and does not throw.

<details>
<summary>Hints</summary>

1. In the first call `T` is `String`, and a `List<Object>` is a list of a supertype of `String`, so `? super String` accepts it.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.

**Package:** `t12_generics_2.exercises.ex04`

**Solution:** [t12 exercise 04 solution code](../../../../code/src/t12_generics_2/exercises/ex04/).
**Tests:** [t12 exercise 04 tests](../../../../code/test/t12_generics_2/exercises/ex04/).

---

## Exercise 05 — Copy between lists

**Goal:** Copy items from one list into another, where the destination may hold a more general type. The source produces (`extends`); the destination consumes (`super`).

**You will need:**
[Core Idea 5](../t12_generics_2_notes.md#core-idea-5--pecs-the-rule-of-thumb) and [Core Idea 6](../t12_generics_2_notes.md#core-idea-6--the-classic-api-copy-uses-both-extends-and-super).

**Steps**

1. Create a class `Copier` with a private constructor and the static method below.
2. If either list is `null`, throw a `NullPointerException`.
3. Loop over `src` and add each item to `dst`. Do not clear `dst` first.
4. In `run()`, copy `[1, 2, 3]` into a `List<Number>` that already holds `0.5`, and into a `List<Object>` that already holds `"start"`.

```java
public static <T> void copy(List<? extends T> src, List<? super T> dst)
```

**Example**

```text
Numbers: [0.5, 1, 2, 3]
Objects: [start, 1, 2, 3]
```

**Rules**

- Keep the order. Anything already in `dst` stays at the front.

<details>
<summary>Hints</summary>

1. Inside the loop, each item from `src` can be read as a `T`, and a `T` can be added to `dst`.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.

**Package:** `t12_generics_2.exercises.ex05`

**Solution:** [t12 exercise 05 solution code](../../../../code/src/t12_generics_2/exercises/ex05/).
**Tests:** [t12 exercise 05 tests](../../../../code/test/t12_generics_2/exercises/ex05/).

---

## Exercise 06 — Build the world list

**Goal:** Use PECS on your own classes: put enemies and pickups into one list of entities, without a cast.

**You will need:** [Core Idea 5](../t12_generics_2_notes.md#core-idea-5--pecs-the-rule-of-thumb).

**Steps**

1. Copy your `Entity`, `Enemy` and `Pickup` classes into this package.
2. Create a class `Entities` with a private constructor and the static method below.
3. If either list is `null`, throw a `NullPointerException`. Otherwise add every entity from `src` to `dst`, in order.
4. In `run()`, make a `List<Enemy>` (`Orc`, `Goblin`), a `List<Pickup>` (`Coin`, `Potion`) and an empty `List<Entity>` called `world`.
5. Add both lists to `world`, print its size, and print each entity with its class name.

```java
public static void addAllEntities(List<? extends Entity> src, List<? super Entity> dst)
```

**Example**

```text
World size: 4
  Orc (Enemy)
  Goblin (Enemy)
  Coin (Pickup)
  Potion (Pickup)
```

**Rules**

- No casts.

<details>
<summary>Hints</summary>

1. `entity.getClass().getSimpleName()` gives the class name, such as `Enemy`.

</details>

**Done when**

- [ ] `run()` prints the five lines shown.

**Package:** `t12_generics_2.exercises.ex06`

**Solution:** [t12 exercise 06 solution code](../../../../code/src/t12_generics_2/exercises/ex06/).
**Tests:** [t12 exercise 06 tests](../../../../code/test/t12_generics_2/exercises/ex06/).

---

## Exercise 07 — Fix a too-strict signature

**Goal:** A method that adds enemies to a list only accepts a `List<Enemy>`. Change its signature so it also accepts a `List<Entity>` or a `List<Object>`.

**You will need:** [Step C](../t12_generics_2_notes.md#step-c--fix-write-methods-with-super).

The starting point:

```java
public static void addEnemies(List<Enemy> out)
```

**Steps**

1. Copy your `Entity` and `Enemy` classes into this package.
2. In a class `Spawner`, write `addEnemies` with the signature above. It adds two enemies, `Orc` and `Goblin`, and throws a `NullPointerException` if `out` is `null`.
3. In `run()`, call it with a new `ArrayList<Enemy>`, a new `ArrayList<Entity>` and a new `ArrayList<Object>`. Two of the three calls fail to compile.
4. Decide whether `out` is a producer or a consumer, and change the signature to match.
5. Print each list.

**Example**

```text
List<Enemy>: [Orc, Goblin]
List<Entity>: [Orc, Goblin]
List<Object>: [Orc, Goblin]
```

**Rules**

- Change the signature only. No overloads.

<details>
<summary>Hints</summary>

1. The method only **adds** to `out`. It never reads from it.
2. A list that can accept an `Enemy` is a list of `Enemy` or of any supertype of `Enemy`.

</details>

**Done when**

- [ ] All three calls compile, and `run()` prints the three lines shown.

**Package:** `t12_generics_2.exercises.ex07`

**Solution:** [t12 exercise 07 solution code](../../../../code/src/t12_generics_2/exercises/ex07/).
**Tests:** [t12 exercise 07 tests](../../../../code/test/t12_generics_2/exercises/ex07/).

---

## Exercise 08 — Sort with a supertype comparator

**Goal:** Sort a list of enemies with a comparator written for **entities**. This is why so many Java library methods take a `Comparator<? super T>`.

**You will need:** [Core Idea 7](../t12_generics_2_notes.md#core-idea-7--comparator-super-t-why-many-java-apis-use-super).

**Steps**

1. Copy your `Entity` and `Enemy` classes into this package.
2. Create a class `Sorters` with a private constructor and the static method below.
3. If either argument is `null`, throw a `NullPointerException`. Otherwise call `items.sort(cmp)`.
4. In `run()`, make a `List<Enemy>` holding `Zombie`, `Orc` and `Goblin`, and a `Comparator<Entity>` that orders by name.
5. Print the list, sort it with your method, and print it again.

```java
public static <T> void sortWith(List<T> items, Comparator<? super T> cmp)
```

**Example**

```text
Before: [Zombie, Orc, Goblin]
After: [Goblin, Orc, Zombie]
```

**Rules**

- The comparator's type is `Comparator<Entity>`, not `Comparator<Enemy>`.

<details>
<summary>Hints</summary>

1. `Comparator.comparing(Entity::getName)` builds a comparator that orders by name.
2. Anything that can compare two entities can compare two enemies, because every enemy is an entity.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.

**Package:** `t12_generics_2.exercises.ex08`

**Solution:** [t12 exercise 08 solution code](../../../../code/src/t12_generics_2/exercises/ex08/).
**Tests:** [t12 exercise 08 tests](../../../../code/test/t12_generics_2/exercises/ex08/).

---

## Exercise 09 — Swap the first two

**Goal:** Swap the first two items of a list of any type. You will hit a "capture of ?" compiler error, and fix it properly with a helper method.

**You will need:**
[Core Idea 8](../t12_generics_2_notes.md#core-idea-8--capture-of--errors-what-they-mean).

**Steps**

1. Create a class `Swaps` with a private constructor.
2. Write the public method below. If the list is `null` or has fewer than two items, return.
3. Try writing the swap directly inside it with `get` and `set`. Read the error.
4. Move the swap into the private generic helper below, and call the helper from the public method.
5. In `run()`, swap the first two of `["A", "B", "C"]` and of `[10, 20]`, printing before and after.

```java
public static void swapFirstTwo(List<?> items)
private static <T> void swapFirstTwoCaptured(List<T> items)
```

**Example**

```text
Before: [A, B, C]
After: [B, A, C]
Before: [10, 20]
After: [20, 10]
```

**Rules**

- No casts and no raw types.
- Write a comment above the helper explaining why it is needed.

<details>
<summary>Hints</summary>

1. With `List<?>`, the compiler cannot prove that what `get(0)` returns may be passed to `set(1, ...)`. Each `?` could be a different unknown type.
2. Calling the helper gives the unknown type a name, `T`, so the compiler can check that both items have the same type.

</details>

**Done when**

- [ ] `run()` prints the four lines shown.

**Package:** `t12_generics_2.exercises.ex09`

**Solution:** [t12 exercise 09 solution code](../../../../code/src/t12_generics_2/exercises/ex09/).
**Tests:** [t12 exercise 09 tests](../../../../code/test/t12_generics_2/exercises/ex09/).

---

## Exercise 10 — What can I add, what can I read?

**Goal:** Build your instincts for wildcards by writing a short cheat sheet, then check one line of it in code.

**You will need:** [Appendix A — Quick reference](../t12_generics_2_notes.md#appendix-a--quick-reference-wildcards).

**Steps**

1. In a block comment at the top of `Exercise`, write two short answers for each declaration below: what can you safely **add**, and what can you safely **read** it as?
2. In `run()`, read the first element of a `List<? extends Number>` into a `Number` variable and print it.
3. Add `42` to a `List<? super Integer>`, read it back into an `Object` variable, and print it.

```java
List<?> a;
List<? extends Number> b;
List<? super Integer> c;
```

**Example**

```text
Read as Number: 1
Read as Object: 42
```

**Rules**

- Keep each answer to two to four lines. Use phrases like "read as Object", "add null only" and "can add Integer".

<details>
<summary>Hints</summary>

1. For each one, ask: what is the most specific type **every** possible list behind this declaration could hand back? And what type would **every** possible list accept?

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] Your answers match what the compiler allows.

<details>
<summary>Answer</summary>

- `List<?> a`
  - Add: `null` only.
  - Read: as `Object`, because the element type is unknown.
- `List<? extends Number> b`
  - Add: `null` only. It might be a `List<Integer>` or a `List<Double>`.
  - Read: as `Number`.
- `List<? super Integer> c`
  - Add: `Integer`, and `null`.
  - Read: as `Object` only, because it might really be a `List<Number>` or a `List<Object>`.

</details>

**Package:** `t12_generics_2.exercises.ex10`

**Solution:** [t12 exercise 10 solution code](../../../../code/src/t12_generics_2/exercises/ex10/).

---

## Exercise 11 — Merge two lists (optional)

**Goal:** Combine what you have learned into one useful method: read from two producer lists and add everything to one consumer list, in order.

**You will need:** [Core Idea 5](../t12_generics_2_notes.md#core-idea-5--pecs-the-rule-of-thumb).

**Steps**

1. Create a class `Merge` with a private constructor and the static method below.
2. If `out` is `null`, throw a `NullPointerException`.
3. Treat a `null` input list as empty.
4. Add every item of `a`, then every item of `b`, to `out`. Do not clear `out`.
5. In `run()`, merge `[1, 2]` and `[3]` into a `List<Number>`. Then merge `null` and `[2.5]` into another.

```java
public static <T> void merge(List<? extends T> a, List<? extends T> b, List<? super T> out)
```

**Example**

```text
Merged: [1, 2, 3]
With a null first list: [2.5]
```

**Rules**

- No casts.

<details>
<summary>Hints</summary>

1. The two loops are the same apart from the list. Write a private helper that copies one list, and call it twice.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.

**Package:** `t12_generics_2.exercises.ex11`

**Solution:** [t12 exercise 11 solution code](../../../../code/src/t12_generics_2/exercises/ex11/).
**Tests:** [t12 exercise 11 tests](../../../../code/test/t12_generics_2/exercises/ex11/).
