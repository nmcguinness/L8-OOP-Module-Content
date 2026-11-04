---
title: "Design Patterns II — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t14_design_patterns_2
description: "Six step-by-step exercises applying Factory, Observer and Adapter: parsers chosen by file extension, enemies chosen by difficulty, button and sensor listeners, a legacy logger, and a payment gateway."
created: 2026-02-06
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, design-patterns, factory, observer, adapter, coordination, architecture, oop, exercises, year2, comp-c8z03]
---
# Design Patterns II — Exercises

These exercises practise the three patterns in the [t14 notes](../t14_design_patterns_2_notes.md), two exercises each:

- **Factory** keeps the decision "which class do I create?" in one place, so the rest of the program never writes `new` for those classes.
- **Observer** lets one object announce that something happened, and any number of listeners react, without the announcer knowing who they are.
- **Adapter** wraps a class whose methods do not match what your program expects, so the rest of the program can use it through your own interface.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution and its tests.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Parsers by file extension | Factory | Foundation |
| 02 Enemies by difficulty | Factory with an enum | Foundation |
| 03 Button click listeners | Observer | Foundation |
| 04 Temperature events | Observer with a value passed along | Intermediate |
| 05 A legacy logger | Adapter | Foundation |
| 06 A payment gateway | Adapter that converts data | Intermediate |

## Rules for every exercise

- The decision about which class to use lives in one place: the factory, or the listener.
- No `instanceof` checks to decide behaviour.
- Use ordinary loops, not streams.
- Print a few short lines in `run()` so you can see the behaviour at a glance.

## How to run

Each exercise goes in its own package, `t14_design_patterns_2.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`.

---

## Exercise 01 — Parsers by file extension

**Goal:** Build a factory that gives you the right parser for a file extension. The code that uses the parsers never creates one with `new`.

**You will need:** [Pattern 1: Factory](../t14_design_patterns_2_notes.md#pattern-1-factory) and [Concrete factory](../t14_design_patterns_2_notes.md#concrete-factory).

**Steps**

1. Create an interface `Parser` with one method, `int parseCount(String input)`. It returns how many records the input holds.
2. Create `CsvParser`. It counts the lines that are not blank.
3. Create `JsonParser`. It counts the `{` characters, treating each one as the start of a record.
4. Both parsers return 0 for `null` input.
5. Create `ParserFactory` with the method below. It ignores case, so `"CSV"` works too.
6. For any other extension, throw `IllegalArgumentException("Unsupported extension: " + extension)`.
7. In `run()`, ask the factory for a CSV parser and a JSON parser, and count the records in a small sample of each.

```java
public interface Parser {
    int parseCount(String input);
}

public Parser createFor(String extension)
```

**Example** (the CSV sample has three lines; the JSON sample has two objects)

```text
CSV count = 3
JSON count = 2
```

**Rules**

- `run()` never writes `new CsvParser()` or `new JsonParser()`.

<details>
<summary>Hints</summary>

1. `input.split("\n")` gives the lines, and `line.isBlank()` spots the empty ones.
2. Normalise the extension once: `String ext = extension.trim().toLowerCase();`

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] `createFor("xml")` throws an exception whose message names `xml`.

**Package:** `t14_design_patterns_2.exercises.ex01`

**Solution:** [t14 exercise 01 solution code](../../../../code/src/t14_design_patterns_2/exercises/ex01/).
**Tests:** [t14 exercise 01 tests](../../../../code/test/t14_design_patterns_2/exercises/ex01/).

---

## Exercise 02 — Enemies by difficulty

**Goal:** Let a factory decide which kind of slime to create for each difficulty, so the game code just asks for "a slime" and gets the right one.

**You will need:** [Factory — spawning pickups from a level file](../t14_design_patterns_2_notes.md#factory--spawning-pickups-from-a-level-file).

**Steps**

1. Create an enum `Difficulty` with `EASY` and `HARD`.
2. Create an interface `Enemy` with three methods: `name()`, `hp()` and `damage()`.
3. Create `Slime` (hp 20, damage 3) and `ArmouredSlime` (hp 45, damage 6, name `Armoured Slime`).
4. Create `EnemyFactory` with the method below. `EASY` gives a `Slime`; `HARD` gives an `ArmouredSlime`. A `null` difficulty throws an `IllegalArgumentException`.
5. In `run()`, create one slime for each difficulty and print its name, hp and damage.

```java
public Enemy createSlime(Difficulty difficulty)
```

**Example**

```text
EASY -> Slime (hp=20, dmg=3)
HARD -> Armoured Slime (hp=45, dmg=6)
```

**Rules**

- The choice of class happens in the factory only. `run()` treats both as an `Enemy`.

<details>
<summary>Hints</summary>

1. A `switch` on the enum is a tidy way to choose: `return switch (difficulty) { case EASY -> new Slime(); case HARD -> new ArmouredSlime(); };`
2. Write one small `format(Enemy e)` method in `Exercise` for the printing.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] Adding a `MEDIUM` difficulty would mean changing the factory only.

**Package:** `t14_design_patterns_2.exercises.ex02`

**Solution:** [t14 exercise 02 solution code](../../../../code/src/t14_design_patterns_2/exercises/ex02/).
**Tests:** [t14 exercise 02 tests](../../../../code/test/t14_design_patterns_2/exercises/ex02/).

---

## Exercise 03 — Button click listeners

**Goal:** Build a button that tells every interested listener when it is clicked. Listeners can sign up and leave at any time, and the button never needs to change.

**You will need:** [Pattern 2: Observer](../t14_design_patterns_2_notes.md#pattern-2-observer) and [Subject (publisher)](../t14_design_patterns_2_notes.md#subject-publisher).

**Steps**

1. Create an interface `ClickListener` with one method, `void onClick()`.
2. Create `SoundListener`, which prints `SFX: click`, and `AnalyticsListener`, which prints `ANALYTICS: click`.
3. Create `Button` with a private `List<ClickListener>`.
4. `addListener` adds to the list and `removeListener` removes from it. Both reject `null`.
5. `click()` calls `onClick()` on every listener, in the order they were added.
6. In `run()`, add both listeners and click twice. Then remove the sound listener and click once more.

```java
public void addListener(ClickListener listener)
public boolean removeListener(ClickListener listener)
public void click()
```

**Example**

```text
SFX: click
ANALYTICS: click
SFX: click
ANALYTICS: click
ANALYTICS: click
```

**Rules**

- `Button` contains no code about any particular kind of listener.

<details>
<summary>Hints</summary>

1. Keep a reference to the sound listener in a variable, so you can pass the same object to `removeListener` later.
2. `list.remove(object)` returns `true` if it found and removed the object.

</details>

**Done when**

- [ ] `run()` prints the five lines shown.
- [ ] Each click notifies each current listener exactly once.

**Package:** `t14_design_patterns_2.exercises.ex03`

**Solution:** [t14 exercise 03 solution code](../../../../code/src/t14_design_patterns_2/exercises/ex03/).
**Tests:** [t14 exercise 03 tests](../../../../code/test/t14_design_patterns_2/exercises/ex03/).

---

## Exercise 04 — Temperature events

**Goal:** Build a sensor that tells its listeners the new temperature each time it changes. This time the event carries a value, and each listener decides what to do with it.

**You will need:** [Observer interface](../t14_design_patterns_2_notes.md#observer-interface) and [Observer — reacting to the player's health](../t14_design_patterns_2_notes.md#observer--reacting-to-the-players-health).

**Steps**

1. Create an interface `TemperatureListener` with `void onTemperatureChanged(double celsius)`.
2. Create `ConsoleDisplayListener`, which prints `TEMP: <value>C` every time.
3. Create `OverheatAlarmListener`. Its constructor takes a threshold, and it prints `ALARM: <value>C` only when the value is at or above the threshold.
4. Create `TemperatureSensor` with `addListener`, `removeListener` and `setTemperature`. `setTemperature` passes the new value to every listener.
5. In `run()`, add both listeners with a threshold of 55.0, then set the temperature to 35.0, 60.0, 54.9 and 55.0.

```java
public void setTemperature(double celsius)
```

**Example**

```text
TEMP: 35.0C
TEMP: 60.0C
ALARM: 60.0C
TEMP: 54.9C
TEMP: 55.0C
ALARM: 55.0C
```

**Rules**

- The sensor does not know what an alarm is. It only passes values on.

<details>
<summary>Hints</summary>

1. The alarm's check is `if (celsius >= threshold)`. Notice the `>=`: exactly 55.0 sounds the alarm.

</details>

**Done when**

- [ ] `run()` prints the six lines shown.
- [ ] You could add a `LoggingListener` without changing `TemperatureSensor`.

**Package:** `t14_design_patterns_2.exercises.ex04`

**Solution:** [t14 exercise 04 solution code](../../../../code/src/t14_design_patterns_2/exercises/ex04/).
**Tests:** [t14 exercise 04 tests](../../../../code/test/t14_design_patterns_2/exercises/ex04/).

---

## Exercise 05 — A legacy logger

**Goal:** Your program logs through a `Logger` interface, but the only logger available is an old class with a different method name. Write an adapter so the program can use it unchanged.

**You will need:** [Pattern 3: Adapter](../t14_design_patterns_2_notes.md#pattern-3-adapter) and [Adapter example](../t14_design_patterns_2_notes.md#adapter-example).

**Steps**

1. Create an interface `Logger` with `void log(String msg)`. This is what your program uses.
2. Create `LegacyLogger` with `void logMessage(String msg)`, which prints `LEGACY: <msg>`. Treat this class as code you are not allowed to change.
3. Create `LegacyLoggerAdapter`, which implements `Logger`. Its constructor takes a `LegacyLogger`, and `log` passes the message to `logMessage`.
4. In `run()`, declare a variable of type `Logger`, assign an adapter to it, and log two messages.

```java
public LegacyLoggerAdapter(LegacyLogger legacy)
public void log(String msg)
```

**Example**

```text
LEGACY: Server started
LEGACY: Player connected
```

**Rules**

- Do not rename or change `LegacyLogger`'s method.
- After creating the adapter, `run()` only uses the `Logger` variable.

<details>
<summary>Hints</summary>

1. The whole of `log` is one line: `legacy.logMessage(msg);`

</details>

**Done when**

- [ ] `run()` prints the two lines shown.

**Package:** `t14_design_patterns_2.exercises.ex05`

**Solution:** [t14 exercise 05 solution code](../../../../code/src/t14_design_patterns_2/exercises/ex05/).
**Tests:** [t14 exercise 05 tests](../../../../code/test/t14_design_patterns_2/exercises/ex05/).

---

## Exercise 06 — A payment gateway

**Goal:** Your shop works in whole cents, but a third-party payment gateway wants a euro amount as text, such as `"12.99"`. Write an adapter that converts between the two.

**You will need:** [Adapter — a legacy payment gateway](../t14_design_patterns_2_notes.md#adapter--a-legacy-payment-gateway).

**Steps**

1. Create an interface `CheckoutService` with `boolean checkout(int cents)`.
2. Create `ThirdPartyGateway` with `boolean makePayment(String euroAmount)`. It returns `false` for `"0.00"`. Otherwise it prints `PAID: <euroAmount>` and returns `true`.
3. Create `GatewayCheckoutAdapter`, which implements `CheckoutService`. Its constructor takes the gateway.
4. In `checkout`, reject a negative amount, convert the cents to a euro string, and pass it to `makePayment`.
5. In `run()`, check out 0, 199, 1299 and 1205 cents, printing what `checkout` returns each time.

```java
public GatewayCheckoutAdapter(ThirdPartyGateway gateway)
public boolean checkout(int cents)
```

**Example**

```text
checkout(0) -> false
PAID: 1.99
checkout(199) -> true
PAID: 12.99
checkout(1299) -> true
PAID: 12.05
checkout(1205) -> true
```

**Rules**

- The conversion lives in the adapter, not in the gateway or in `run()`.
- 1205 cents is `12.05`, not `12.5`.

<details>
<summary>Hints</summary>

1. `cents / 100` gives the euros and `cents % 100` gives the cents left over.
2. `String.format("%d.%02d", euros, remainder)` pads the cents to two digits.

</details>

**Done when**

- [ ] `run()` prints the seven lines shown.

**Think about it:** the shop stores money as whole cents in an `int`, not as a `double`. Why? (t10 Exercise 05 gives a clue.)

**Package:** `t14_design_patterns_2.exercises.ex06`

**Solution:** [t14 exercise 06 solution code](../../../../code/src/t14_design_patterns_2/exercises/ex06/).
**Tests:** [t14 exercise 06 tests](../../../../code/test/t14_design_patterns_2/exercises/ex06/).
