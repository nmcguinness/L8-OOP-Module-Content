---
title: "Interfaces — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t09_interface
description: "Nine step-by-step interface exercises: behaviour contracts, default methods, multiple interfaces, strategy, comparator factories, command history, simple XML, and a file-backed filter pipeline."
created: 2026-05-29
last_updated: 2026-11-03
version: 1.2
status: published
authors: ["OOP Teaching Team"]
tags: [java, interfaces, polymorphism, exercises, year2, comp-c8z03]
---
# Interfaces — Exercises

These exercises practise writing interfaces, implementing them, and using an interface type to hold objects of different classes, from the [t09 notes](../t09_interface_notes.md). They reuse `ArrayList`, `LinkedList`, `Comparator` and simple file reading from earlier topics.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution, and to its tests where there are some.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 A simple behaviour | declaring and implementing an interface | Foundation |
| 02 Interactable objects | a list of an interface type, default methods | Foundation |
| 03 Two interfaces at once | implementing several interfaces, `instanceof` | Foundation |
| 04 Swappable attacks | the Strategy pattern | Intermediate |
| 05 Comparator factories | methods that return a `Comparator` | Intermediate |
| 06 Command history | a `LinkedList` of commands | Intermediate |
| 07 Saving as XML | an interface for saving, string parsing | Intermediate |
| 08 A text filter pipeline | chaining objects through one interface | Intermediate |
| 09 Filtering words from a file | `Iterable<String>`, reading a file | Stretch |

## How to run

Each exercise goes in its own package, `t09_interface.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`:

```java
package t09_interface.exercises.ex01;

public class Exercise {

    public static void run() {
        // your code here
    }
}
```

---

## Exercise 01 — A simple behaviour

**Goal:** Declare a small interface and implement it in two classes, so that one variable can hold either and behave differently. This is polymorphism through an interface.

**You will need:** [Core Idea 1](../t09_interface_notes.md#core-idea-1--interfaces-define-behaviour-not-structure) and [Core Idea 4](../t09_interface_notes.md#core-idea-4--interface-references-enable-polymorphism).

**Steps**

1. Create an interface `Greeter` with one method, shown below.
2. Create a class `CasualGreeter` that implements `Greeter` and prints `Hey, <name>!`.
3. Create a class `FormalGreeter` that implements `Greeter` and prints `Good evening, <name>.`.
4. In `run()`, declare **one** variable of type `Greeter`. Assign a `CasualGreeter` and call `greet("Alex")`. Then assign a `FormalGreeter` to the same variable and call it again.

```java
public interface Greeter {
    void greet(String name);
}
```

**Example**

```text
Hey, Alex!
Good evening, Alex.
```

**Rules**

- The variable's type is `Greeter`, not `CasualGreeter` or `FormalGreeter`.
- Mark each implementing method with `@Override`.

<details>
<summary>Hints</summary>

1. `public class CasualGreeter implements Greeter { ... }`
2. If the compiler says your class "is not abstract and does not override abstract method greet", check the method's name, parameter type and `public` keyword.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] Only one variable is used for both greetings.

**Package:** `t09_interface.exercises.ex01`

**Solution:** [t09 exercise 01 solution code](../../../../code/src/t09_interface/exercises/ex01/).

---

## Exercise 02 — Interactable objects in a list

**Goal:** Store objects of three unrelated classes in one list, using the interface they share, then give the interface **default methods** so each class gets sensible behaviour for free.

**You will need:**
[Core Idea 6](../t09_interface_notes.md#core-idea-6--interfaces--collections--power) and [Core Idea 7](../t09_interface_notes.md#core-idea-7--default-methods-behaviour-with-a-fallback).

**Steps**

1. Create an interface `Interactable` with two methods: `void interact()` and `String getDescription()`.
2. Create three classes that implement it: `Door`, `Chest` and `NPC`. Each takes a name in its constructor and prints its own message from `interact()`.
3. In `run()`, create a `List<Interactable>`, add one of each, and loop over the list calling `interact()`.
4. Now add a default method `interactionRange()` to the interface. It returns `2`.
5. Add a second default method `canInteract(int distanceToPlayer)`. It returns `true` when the distance is between `0` and `interactionRange()`, inclusive.
6. Override `interactionRange()` in `NPC` only, returning `4`. You can talk to someone from further away than you can open a door.
7. Change the loop in `run()`: give the three objects distances 1, 3 and 3, and call `interact()` only when `canInteract(...)` returns `true`.

```java
default int interactionRange()
default boolean canInteract(int distanceToPlayer)
```

**Example**

```text
Player is 1 units from Wooden door (range 2)
You open the Wooden door.

Player is 3 units from Treasure chest (range 2)
Too far away to interact.

Player is 3 units from NPC Shopkeeper (range 4)
Shopkeeper says: "Hello, traveller!"
```

**Rules**

- The list's element type is `Interactable`.
- `Door` and `Chest` do not mention `interactionRange()` at all.

<details>
<summary>Hints</summary>

1. A default method has a body, and starts with the keyword `default`: `default int interactionRange() { return 2; }`.
2. `canInteract` calls `interactionRange()`. When the object is an `NPC`, Java calls the `NPC` version, so the NPC gets range 4 without `canInteract` changing.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] At distance 3, the chest is too far away but the NPC still answers.

**Think about it:** you could write `int DEFAULT_INTERACTION_RANGE = 2;` in the interface instead. Why would that stop `NPC` from having a different range? See [Core Idea 2](../t09_interface_notes.md#core-idea-2--constants-in-interfaces-legal-but-rarely-the-right-home).

**Package:** `t09_interface.exercises.ex02`

**Solution:** [t09 exercise 02 solution code](../../../../code/src/t09_interface/exercises/ex02/).
**Tests:** [t09 exercise 02 tests](../../../../code/test/t09_interface/exercises/ex02/).

---

## Exercise 03 — Two interfaces at once

**Goal:** Give one class two separate abilities by implementing two interfaces, and use `instanceof` to find out which objects in a list have the second ability.

**You will need:** [Core Idea 5](../t09_interface_notes.md#core-idea-5--a-class-can-implement-multiple-interfaces).

**Steps**

1. Create two interfaces: `Moveable` with `void move()`, and `Damageable` with `void takeDamage(int amount)`.
2. Create `Player`, which implements both. It starts at `x = 0` with health `100`, and `move()` adds 2 to `x`.
3. Create `Enemy`, which implements both. It starts with health `30`, and `move()` adds 1 to `x`.
4. Create `Cloud`, which implements `Moveable` **only**. Its `move()` adds 3 to `x`.
5. In each `takeDamage`, reject a negative amount with an `IllegalArgumentException`, and never let health go below 0.
6. In `Exercise`, write `moveAll` and `damageAll`. `damageAll` damages only the actors that are `Damageable`, and returns how many it hit.
7. In `run()`, put a `Player("Hero")`, an `Enemy("Slime")` and a `Cloud` in a `LinkedList<Moveable>`. Move them all, damage them all by 10, and print each actor.

```java
public static void moveAll(List<Moveable> actors)
public static int damageAll(List<Moveable> actors, int amount)
```

**Example**

```text
Hit 2 of 3 actors
  Hero at x=2, health=90
  Slime at x=1, health=20
  Cloud at x=3
```

**Rules**

- The list's element type is `Moveable`.
- Check with `instanceof` before you treat an actor as `Damageable`.

<details>
<summary>Hints</summary>

1. `if (actor instanceof Damageable d) { d.takeDamage(amount); }` checks and casts in one step.
2. `health = Math.max(0, health - amount);` stops health going below 0.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] The cloud is moved but never damaged.

**Package:** `t09_interface.exercises.ex03`

**Solution:** [t09 exercise 03 solution code](../../../../code/src/t09_interface/exercises/ex03/).
**Tests:** [t09 exercise 03 tests](../../../../code/test/t09_interface/exercises/ex03/).

---

## Exercise 04 — Swappable attacks

**Goal:** Let an enemy change **how** it attacks while the game runs, by giving it an object that does the attacking. This is the **Strategy pattern**, which t13 covers in full.

**You will need:** [Core Idea 4](../t09_interface_notes.md#core-idea-4--interface-references-enable-polymorphism) and [Core Idea 9](../t09_interface_notes.md#core-idea-9--functional-interfaces-anonymous-classes-and-lambdas).

**Steps**

1. Create an interface `AttackStrategy` with one method, shown below. It returns a description of the attack instead of printing it, so it is easy to test.
2. Create `MeleeAttack`, which returns `Swings sword at <target>!`, and `RangedAttack`, which returns `Shoots arrow at <target>!`.
3. Create a class `Enemy` with a private `AttackStrategy` field, set in the constructor and changed by `setStrategy`. Both reject `null`.
4. Give `Enemy` a method `performAttack(String targetName)` that asks its strategy to attack.
5. In `run()`, create an enemy with a `MeleeAttack` and attack `"Player"`. Switch to a `RangedAttack` and attack again.
6. Write `pickRandom`, which picks one strategy from a list. Use it for three turns with `new Random(42)`.

```java
public interface AttackStrategy {
    String attack(String targetName);
}

public static AttackStrategy pickRandom(List<AttackStrategy> strategies, Random random)
```

**Example**

```text
Swings sword at Player!
Shoots arrow at Player!
Turn 1: Shoots arrow at Player!
Turn 2: Swings sword at Player!
Turn 3: Shoots arrow at Player!
```

**Rules**

- `Enemy` never checks which kind of strategy it has. It just calls `attack`.
- Pass the `Random` in, rather than creating one inside `pickRandom`. A seeded `Random` gives the same "random" choices every run, so the output can be checked.

<details>
<summary>Hints</summary>

1. `strategies.get(random.nextInt(strategies.size()))` picks one element.
2. `AttackStrategy` has one abstract method, so a lambda works too: `enemy.setStrategy(target -> "Casts a spell at " + target + "!");`

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] `setStrategy(null)` throws an `IllegalArgumentException`.

**Package:** `t09_interface.exercises.ex04`

**Solution:** [t09 exercise 04 solution code](../../../../code/src/t09_interface/exercises/ex04/).
**Tests:** [t09 exercise 04 tests](../../../../code/test/t09_interface/exercises/ex04/).

---

## Exercise 05 — Comparator factories

**Goal:** Sort a mixed list of items and characters by anything that has a name. Write a class whose static methods hand out ready-made `Comparator` objects.

**You will need:**
[Core Idea 8](../t09_interface_notes.md#core-idea-8--static-and-private-interface-methods) and [Core Idea 9](../t09_interface_notes.md#core-idea-9--functional-interfaces-anonymous-classes-and-lambdas).

**Steps**

1. Create an interface `Named` with one method, `String getName()`.
2. Create two classes that implement it: `GameItem` and `NPC`.
3. Create a class `NamedComparators` with a private constructor, so nobody can create one.
4. Add two static methods that each return a `Comparator<Named>`: `byNameAscending()`, ignoring upper and lower case, and `byNameLength()`.
5. In `run()`, make a `List<Named>` holding the items `Sword` and `Health Potion` and the NPCs `Merchant` and `Guard`. Sort it each way and print the names.

```java
public static Comparator<Named> byNameAscending()
public static Comparator<Named> byNameLength()
```

**Example**

```text
=== Sort by name ascending ===
Guard
Health Potion
Merchant
Sword

=== Sort by name length ===
Guard
Sword
Merchant
Health Potion
```

**Rules**

- The list holds both `GameItem` and `NPC` objects, through the type `Named`.
- Each comparator is written as a lambda.

<details>
<summary>Hints</summary>

1. `(a, b) -> a.getName().compareToIgnoreCase(b.getName())`
2. `(a, b) -> Integer.compare(a.getName().length(), b.getName().length())`
3. Sort with `things.sort(NamedComparators.byNameAscending());`

</details>

**Done when**

- [ ] `run()` prints both lists in the order shown.

**Think about it:** `Guard` and `Sword` both have five letters. Why does `Guard` stay first after the second sort?

**Package:** `t09_interface.exercises.ex05`

**Solution:** [t09 exercise 05 solution code](../../../../code/src/t09_interface/exercises/ex05/).
**Tests:** [t09 exercise 05 tests](../../../../code/test/t09_interface/exercises/ex05/).

---

## Exercise 06 — Command history

**Goal:** Treat actions as objects. Each command is stored in a history list, so the program can replay everything that happened, in order.

**You will need:**
[Core Idea 6](../t09_interface_notes.md#core-idea-6--interfaces--collections--power).

**Steps**

1. Create an interface `Command` with one method, `void execute()`.
2. Create `PrintCommand`, which stores a message and prints it when executed.
3. Create `AddCommand`, which stores two numbers and prints their sum when executed.
4. In `run()`, create a `LinkedList<Command>` called `history` and add three commands: print `Hello, world!`, add 2 and 3, and print `Done.`.
5. Loop over the history, calling `execute()` on each command.

```java
public interface Command {
    void execute();
}
```

**Example**

```text
Hello, world!
2 + 3 = 5
Done.
```

**Rules**

- Creating a command does nothing visible. Only `execute()` prints.

<details>
<summary>Hints</summary>

1. Store the values in `private final` fields in the constructor, and use them in `execute()`.

</details>

**Done when**

- [ ] `run()` prints the three lines shown, in order.

**Extension:** add `void undo()` to the interface. Decide what undoing each command means (for example, print that it was undone), then replay the history backwards with `history.descendingIterator()`. The worked solution does not include this.

**Package:** `t09_interface.exercises.ex06`

**Solution:** [t09 exercise 06 solution code](../../../../code/src/t09_interface/exercises/ex06/).

---

## Exercise 07 — Saving as XML

**Goal:** Give a class a standard way to save itself as text, through an interface, and a way to load it back. You will write a very small XML format yourself, without a library.

**You will need:**
[Core Idea 3](../t09_interface_notes.md#core-idea-3--classes-implement-interfaces).

**Steps**

1. Create an interface `XmlSerializable` with one method, `String toXml()`.
2. Create a class `PlayerProfile` with `private final` fields `name` and `score`, a constructor and two getters. It implements `XmlSerializable`.
3. Make `toXml()` return text like `<player name="Alex" score="10" />`.
4. Write a static method `fromXml(String xml)` that finds the `name` and `score` values in the text and returns a new `PlayerProfile`.
5. In `run()`, create two profiles, print their XML, load each one back from its XML, and print the loaded name and score.

```java
public static PlayerProfile fromXml(String xml)
```

**Example**

```text
XML 1: <player name="Alex" score="10" />
XML 2: <player name="Morgan" score="25" />
Loaded 1 -> name=Alex, score=10
Loaded 2 -> name=Morgan, score=25
```

**Rules**

- Use `String` methods only: `indexOf`, `substring` and `Integer.parseInt`.
- If an attribute is missing, throw an `IllegalArgumentException` that names it.

<details>
<summary>Hints</summary>

1. Inside a Java string, write a double quote as `\"`: `"<player name=\"" + name + "\" score=\"" + score + "\" />"`.
2. To find a value, search for `name="`, step past it, then search for the next `"` after that. The value is the text in between.
3. Write that search once, as a private helper `extractAttribute(String xml, String attribute)`, and call it for both attributes.

</details>

**Done when**

- [ ] `run()` prints the four lines shown.
- [ ] `fromXml("<player score=\"3\" />")` throws an exception that mentions `name`.

**Extension:** save the XML to a file with `Files.writeString(Path.of("profile.xml"), xml)` and read it back with `Files.readString`. Both throw `IOException`, which t10 covers.

**Package:** `t09_interface.exercises.ex07`

**Solution:** [t09 exercise 07 solution code](../../../../code/src/t09_interface/exercises/ex07/).
**Tests:** [t09 exercise 07 tests](../../../../code/test/t09_interface/exercises/ex07/).

---

## Exercise 08 — A text filter pipeline

**Goal:** Clean up a piece of text by passing it through a list of filters, one after another. Each filter does one small job, and adding a new job means adding a new class.

**You will need:**
[Core Idea 6](../t09_interface_notes.md#core-idea-6--interfaces--collections--power).

**Steps**

1. Create an interface `TextFilter` with one method, shown below.
2. Create `TrimFilter`, which removes spaces from both ends, and `LowercaseFilter`, which makes the text lower case.
3. Create `RegexReplacementFilter`. Its constructor takes a regular expression and a replacement, and `apply` calls `input.replaceAll(regex, replacement)`.
4. Create a class `TextFilters` with a private constructor and a static method `applyAll`. It passes the text through each filter in turn and returns the result.
5. In `run()`, build a list of three filters: trim, lower case, then a regex filter that puts `<b>` and `</b>` around any run of seven or more digits. Filter the text in the example.

```java
public interface TextFilter {
    String apply(String input);
}

public static String applyAll(List<TextFilter> filters, String text)
```

**Example**

```text
Original: [   This is MY work number 0429470200, call me before 5PM   ]
Filtered: [this is my work number <b>0429470200</b>, call me before 5pm]
```

**Rules**

- Each filter returns a **new** string. Strings cannot be changed in place.
- A filter given `null` returns `null`.

<details>
<summary>Hints</summary>

1. In `applyAll`, keep a variable `result`, start it at `text`, and replace it with `filter.apply(result)` for each filter.
2. The regular expression `(\\d{7,})` matches seven or more digits. In the replacement, `$1` means "the text that matched", so use `"<b>$1</b>"`.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] Changing the order of the filters in the list changes the result in a way you can explain.

**Package:** `t09_interface.exercises.ex08`

**Solution:** [t09 exercise 08 solution code](../../../../code/src/t09_interface/exercises/ex08/).
**Tests:** [t09 exercise 08 tests](../../../../code/test/t09_interface/exercises/ex08/).

---

## Exercise 09 — Filtering words from a file

**Goal:** Add a filter to the Exercise 08 pipeline that hides unwanted words. The words come from a file, so the list can change without changing the code.

**You will need:**
[Core Idea 6](../t09_interface_notes.md#core-idea-6--interfaces--collections--power) and your filters from Exercise 08.

**Steps**

1. Find the file `data/exercises/profane_words.csv` in the `code` folder. It has one word per line: `foo`, `bar`, `doodle`, and so on.
2. Create a class `ProfanityLoader` with a static method `loadFromCsv(String fileName)`. It reads every line with `Files.readAllLines`, trims it, and skips blank lines.
3. If the file cannot be read, catch the `IOException`, print a short message and return an empty list.
4. Create `ProfanityFilter`, which implements `TextFilter`. Its constructor takes an `Iterable<String>` of words.
5. In `apply`, replace each word, in any case, with its first letter followed by one `*` for each remaining letter.
6. In `run()`, load the words, then filter the example text through trim, lower case and your new filter.

```java
public static List<String> loadFromCsv(String fileName)
public ProfanityFilter(Iterable<String> badWords)
```

**Example**

```text
Original: [   This is SOME foo bar text and I dont give a DOODLE who reads it, you doodle!   ]
Filtered: [this is some f** b** text and i dont give a d***** who reads it, you d*****!]
```

**Rules**

- The field type is `Iterable<String>`, not `List<String>`.
- Reuse your Exercise 08 classes by importing them. Do not copy them.

<details>
<summary>Hints</summary>

1. The path is relative to the folder the program runs in. Print `System.getProperty("user.dir")` if the file is not found.
2. `"(?i)"` at the start of a regular expression ignores case: `result.replaceAll("(?i)" + word, hidden)`.
3. `word.charAt(0) + "*".repeat(word.length() - 1)` builds the hidden version.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] Renaming the file makes the program print a message instead of crashing.

**Think about it:**

- Why does an `Iterable<String>` field make `ProfanityFilter` easier to reuse than a `List<String>` field?
- How could you change the design so the word list can be reloaded while the program runs?

**Package:** `t09_interface.exercises.ex09`

**Solution:** [t09 exercise 09 solution code](../../../../code/src/t09_interface/exercises/ex09/).
**Tests:** [t09 exercise 09 tests](../../../../code/test/t09_interface/exercises/ex09/).
