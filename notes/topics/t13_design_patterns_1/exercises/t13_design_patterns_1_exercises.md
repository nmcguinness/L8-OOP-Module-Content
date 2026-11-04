---
title: "Design Patterns I — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t13_design_patterns_1
description: "Seven step-by-step exercises applying Strategy and Command, from swappable combat AI and pricing rules to macro commands, an undo stack, and a two-part café ticket dispatcher."
created: 2026-02-03
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, design-patterns, strategy, command, decoupling, oop, exercises, year2, comp-c8z03]
---
# Design Patterns I — Exercises

These exercises practise the two patterns in the [t13 notes](../t13_design_patterns_1_notes.md):

- **Strategy** puts a rule that can vary (how much damage, what price) in its own object, so you can swap the rule without changing the class that uses it.
- **Command** turns a piece of work into an object, so it can be stored, queued, combined or undone before or after it runs.

Exercises 03, 06 and 07 use both patterns together.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution and its tests.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Combat attacks | Strategy | Foundation |
| 02 Pricing rules | Strategy | Foundation |
| 03 A task with a policy | Strategy inside a Command | Intermediate |
| 04 A macro command | a Command made of Commands | Intermediate |
| 05 An undo stack | `execute` and `undo`, `ArrayDeque` as a stack | Intermediate |
| 06 Café tickets, part 1 | policies, commands and a FIFO queue | Intermediate |
| 07 Café tickets, part 2 | choosing a policy in one place | Intermediate |

## Rules for every exercise

- The class that **uses** a strategy or command never checks which kind it has. No `if` or `switch` on a type, and no `instanceof`.
- Print a few short lines in `run()` so you can see the behaviour at a glance.

## How to run

Each exercise goes in its own package, `t13_design_patterns_1.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`.

---

## Exercise 01 — Combat attacks

**Goal:** Give two enemies different ways of attacking without writing "if melee ... else if ranged" anywhere. The `Enemy` class hands the damage calculation to a strategy object.

**You will need:** [Pattern 1: Strategy](../t13_design_patterns_1_notes.md#pattern-1-strategy) and [Strategy — AI behaviour that changes at runtime](../t13_design_patterns_1_notes.md#strategy--ai-behaviour-that-changes-at-runtime).

**Steps**

1. Create an interface `AttackStrategy` with the method below.
2. Create `MeleeAttack`. It returns `baseDamage + 5` when the distance is 2 metres or less, and 0 otherwise.
3. Create `RangedAttack`. It returns `baseDamage` when the distance is 10 metres or less, and 0 otherwise.
4. Create `Enemy`, which stores a name, a base damage and an `AttackStrategy`, all set in the constructor.
5. Give `Enemy` a method `attack(int distanceMeters)` that returns whatever its strategy calculates.
6. In `run()`, create a `Skeleton` (base damage 10, melee) and an `Archer` (base damage 10, ranged). Print each one's damage at 1, 5 and 12 metres.

```java
interface AttackStrategy {
    int computeDamage(int baseDamage, int distanceMeters);
}

public Enemy(String name, int baseDamage, AttackStrategy strategy)
public int attack(int distanceMeters)
```

**Example**

```text
Skeleton at 1m: 15
Skeleton at 5m: 0
Skeleton at 12m: 0
Archer at 1m: 10
Archer at 5m: 10
Archer at 12m: 0
```

**Rules**

- The distance checks live inside the strategies, not in `Enemy`.
- `Enemy.attack` is one line long.

<details>
<summary>Hints</summary>

1. `return strategy.computeDamage(baseDamage, distanceMeters);`
2. Loop over the distances with `for (int d : new int[] {1, 5, 12})`.

</details>

**Done when**

- [ ] `run()` prints the six lines shown.
- [ ] You could add a `MagicAttack` without changing `Enemy`.

**Package:** `t13_design_patterns_1.exercises.ex01`

**Solution:** [t13 exercise 01 solution code](../../../../code/src/t13_design_patterns_1/exercises/ex01/).
**Tests:** [t13 exercise 01 tests](../../../../code/test/t13_design_patterns_1/exercises/ex01/).

---

## Exercise 02 — Pricing rules

**Goal:** Build a checkout whose final price comes entirely from a pricing rule you plug in. Changing the rule means passing in a different object, not editing `Checkout`.

**You will need:** [Step A — Strategy interface](../t13_design_patterns_1_notes.md#step-a--strategy-interface) to [Step C — Context uses the strategy](../t13_design_patterns_1_notes.md#step-c--context-uses-the-strategy).

**Steps**

1. Create an interface `PriceStrategy` with the method below.
2. Create `NoDiscount`, which returns the base price unchanged.
3. Create `StudentDiscount`, which takes 10% off.
4. Create `BlackFridayDiscount`, which takes 30% off but never returns less than €5.00.
5. Create `Checkout`. Its constructor takes a `PriceStrategy`, and `price(double basePrice)` returns what the strategy calculates.
6. In `run()`, make one `Checkout` for each strategy, and print the price for base prices 10.00 and 4.00.

```java
interface PriceStrategy {
    double finalPrice(double basePrice);
}

public Checkout(PriceStrategy pricing)
public double price(double basePrice)
```

**Example**

```text
Base 10.0 -> none: 10.0
Base 10.0 -> student: 9.0
Base 10.0 -> black friday: 7.0
Base 4.0 -> none: 4.0
Base 4.0 -> student: 3.6
Base 4.0 -> black friday: 5.0
```

**Rules**

- The "never below €5.00" rule lives inside `BlackFridayDiscount` and nowhere else.
- `Checkout` contains no `if` or `switch`.

<details>
<summary>Hints</summary>

1. 30% off is `basePrice * 0.7`. Work that out first, then compare it with 5.0.
2. `Math.max(5.0, discounted)` does the comparison in one call.

</details>

**Done when**

- [ ] `run()` prints the six lines shown.
- [ ] You could add a `MemberDiscount` without changing `Checkout`.

**Think about it:** look at the last line. A €4.00 item costs €5.00 on Black Friday. Is that a bug in the code, or a badly written rule? Who should decide?

**Package:** `t13_design_patterns_1.exercises.ex02`

**Solution:** [t13 exercise 02 solution code](../../../../code/src/t13_design_patterns_1/exercises/ex02/).
**Tests:** [t13 exercise 02 tests](../../../../code/test/t13_design_patterns_1/exercises/ex02/).

---

## Exercise 03 — A task with a policy

**Goal:** Run the same task in two different ways, fast or safe. The command says **what** to run; a strategy inside it says **how**.

**You will need:** [Pattern 1: Strategy](../t13_design_patterns_1_notes.md#pattern-1-strategy) and [Pattern 2: Command](../t13_design_patterns_1_notes.md#pattern-2-command).

**Steps**

1. Create a class `Task` with a `name` and three methods: `validate()` prints `Validating: <name>`, `run()` prints `Running: <name>`, and `runUnchecked()` prints `Running unchecked: <name>`.
2. Create an interface `TaskExecutionStrategy` with `void execute(Task task)`.
3. Create `FastExecution`, which calls `task.runUnchecked()`.
4. Create `SafeExecution`, which calls `task.validate()` and then `task.run()`.
5. Create an interface `Command` with `void execute()`.
6. Create `ExecuteTaskCommand`. Its constructor takes a `Task` and a `TaskExecutionStrategy`, and `execute()` passes the task to the strategy.
7. In `run()`, make one task called `Export leaderboard`. Execute it once with each strategy.

```java
public ExecuteTaskCommand(Task task, TaskExecutionStrategy strategy)
```

**Example**

```text
--- FAST ---
Running unchecked: Export leaderboard
--- SAFE ---
Validating: Export leaderboard
Running: Export leaderboard
```

**Rules**

- `Task` does not decide whether to validate. Only the strategy does.
- `ExecuteTaskCommand.execute()` is one line long.

<details>
<summary>Hints</summary>

1. `strategy.execute(task);` is the whole body of `ExecuteTaskCommand.execute()`.
2. Two separate interfaces are both called `execute`. That is fine: one takes no arguments, the other takes a `Task`.

</details>

**Done when**

- [ ] `run()` prints the five lines shown.
- [ ] You could add a `DryRunExecution` without changing the command or the task.

**Package:** `t13_design_patterns_1.exercises.ex03`

**Solution:** [t13 exercise 03 solution code](../../../../code/src/t13_design_patterns_1/exercises/ex03/).
**Tests:** [t13 exercise 03 tests](../../../../code/test/t13_design_patterns_1/exercises/ex03/).

---

## Exercise 04 — A macro command

**Goal:** Combine several small commands into one bigger command. The macro is itself a `Command`, so anything that can run a command can run a whole sequence.

**You will need:** [Command interface](../t13_design_patterns_1_notes.md#command-interface) and [Concrete command](../t13_design_patterns_1_notes.md#concrete-command).

**Steps**

1. Create an interface `Command` with `void execute()`.
2. Create `PrintCommand`. Its constructor takes a message, and `execute()` prints it.
3. Create `MacroCommand`, which implements `Command` and holds a `List<Command>`.
4. Give `MacroCommand` a method `add(Command command)` that adds to the list.
5. Make `MacroCommand.execute()` execute every command in the list, in the order they were added.
6. In `run()`, build a macro of four print commands (`start`, `validate`, `execute`, `done`) and call `execute()` on it **once**.

```java
public void add(Command command)
public void execute()
```

**Example**

```text
start
validate
execute
done
```

**Rules**

- `MacroCommand` implements the same `Command` interface as the commands it holds.

<details>
<summary>Hints</summary>

1. `for (Command command : commands) { command.execute(); }`
2. Because a `MacroCommand` **is a** `Command`, you can add one macro to another.

</details>

**Done when**

- [ ] `run()` prints the four lines shown from a single `execute()` call.
- [ ] A macro added to another macro runs all of its steps in the right place.

**Package:** `t13_design_patterns_1.exercises.ex04`

**Solution:** [t13 exercise 04 solution code](../../../../code/src/t13_design_patterns_1/exercises/ex04/).
**Tests:** [t13 exercise 04 tests](../../../../code/test/t13_design_patterns_1/exercises/ex04/).

---

## Exercise 05 — An undo stack

**Goal:** Build undo. Each change is an object that knows how to apply itself and how to reverse itself, and a stack remembers the order so the newest change is undone first.

**You will need:** [Command — undoable text editor operations](../t13_design_patterns_1_notes.md#command--undoable-text-editor-operations).

**Steps**

1. Create a class `Counter` with a private `int value`, a method `add(int amount)` and a getter `getValue()`.
2. Create an interface `UndoableCommand` with `void execute()` and `void undo()`.
3. Create `AddNumberCommand`. Its constructor takes a `Counter` and an amount. `execute()` adds the amount and `undo()` subtracts it.
4. In `run()`, create an `ArrayDeque<UndoableCommand>` called `history`.
5. Execute three commands that add 5, 2 and 10. After each, `push` it onto `history` and print the counter.
6. Undo twice: `pop` a command from `history`, call `undo()`, and print the counter.

```java
public AddNumberCommand(Counter counter, int amount)
```

**Example**

```text
After +5: 5
After +2: 7
After +10: 17
After undo: 7
After undo: 5
```

**Rules**

- `undo()` reverses exactly what `execute()` did. It never guesses.
- The newest command is undone first (last in, first out).

<details>
<summary>Hints</summary>

1. `history.push(command)` adds to the front and `history.pop()` takes from the front, so an `ArrayDeque` works as a stack.
2. `pop()` throws on an empty stack. Check `history.isEmpty()` before you undo.

</details>

**Done when**

- [ ] `run()` prints the five lines shown.
- [ ] Undoing all three commands returns the counter to 0.

**Package:** `t13_design_patterns_1.exercises.ex05`

**Solution:** [t13 exercise 05 solution code](../../../../code/src/t13_design_patterns_1/exercises/ex05/).
**Tests:** [t13 exercise 05 tests](../../../../code/test/t13_design_patterns_1/exercises/ex05/).

---

## Exercises 06 and 07 — The café ticket dispatcher

A café takes orders at the counter, a kiosk and an app. Each order becomes a **ticket**, and the tickets wait in a queue until the barista is ready. Not every ticket is handled the same way:

- a small, ordinary order is made straight away;
- a large order, or one that looks wrong, is checked first, so stock is not wasted;
- a ticket entered by a new member of staff in **training** is only printed, never made.

Each ticket in the queue is a **Command**. The way it is handled is a **Strategy**. Exercise 06 builds the parts and chooses each ticket's policy by hand. Exercise 07 adds one class that chooses the policy automatically.

---

## Exercise 06 — Café tickets, part 1: policies and a queue

**Goal:** Build the ticket, three handling policies, a command that pairs a ticket with a policy, and a queue that runs the commands in the order they arrived.

**You will need:** [Pattern 2: Command](../t13_design_patterns_1_notes.md#pattern-2-command) and [Invoker](../t13_design_patterns_1_notes.md#invoker).

**Steps**

1. Create `OrderTicket` with four fields: `ticketId`, `description`, `totalEuro` and `itemCount`, a constructor and a getter for each.
2. Add `isValid()`. A ticket is valid when the description is not blank, the total is above 0, and there is at least one item.
3. Add `summary()`, which returns text like `201 1 cappuccino (€7.80)`.
4. Create an interface `OrderProcessingStrategy` with `void process(OrderTicket ticket)`.
5. Create three strategies:
   - `ImmediateProcess` prints `IMMEDIATE MAKE` and the summary.
   - `ValidatedProcess` prints `REJECT <id> invalid` for an invalid ticket, otherwise `VALIDATED MAKE` and the summary.
   - `DryRunTraining` prints `TRAINING would make` and the summary.
6. Create an interface `Command` with `void execute()`, and `ProcessTicketCommand`, which holds a ticket and a strategy and calls `strategy.process(ticket)`.
7. Create `TicketQueue`. It holds a `Queue<Command>` and has `add`, `size` and `processAll`. `processAll` runs and removes every command, oldest first.
8. In `run()`, queue four tickets, each with a policy you choose by hand, and process them.

```java
public interface OrderProcessingStrategy {
    void process(OrderTicket ticket);
}

public ProcessTicketCommand(OrderTicket ticket, OrderProcessingStrategy strategy)
public void processAll()
```

**Example**

```text
Queued: 4
IMMEDIATE MAKE 201 1 cappuccino, 1 croissant (€7.80)
REJECT 202 invalid
TRAINING would make 203 2 lattes, 1 muffin (€12.50)
VALIDATED MAKE 204 8 americanos, 2 toasties (€42.00)
Queued: 0
```

**Rules**

- Make each class `public`, in its own file. Exercise 07 imports them.
- `TicketQueue` never looks inside a command. It only calls `execute()`.
- A rejected ticket does not stop the queue.

<details>
<summary>Hints</summary>

1. Use an `ArrayDeque` as the queue: `offer` or `add` puts a command at the back, and `poll` takes one from the front.
2. `while (!queue.isEmpty()) { queue.poll().execute(); }`
3. `String.format("%d %s (€%.2f)", ticketId, description, totalEuro)` builds the summary.

</details>

**Done when**

- [ ] `run()` prints the six lines shown.
- [ ] The queue is empty after `processAll()`.

**Package:** `t13_design_patterns_1.exercises.ex06`

**Solution:** [t13 exercise 06 solution code](../../../../code/src/t13_design_patterns_1/exercises/ex06/).
**Tests:** [t13 exercise 06 tests](../../../../code/test/t13_design_patterns_1/exercises/ex06/).

---

## Exercise 07 — Café tickets, part 2: choosing the policy

**Goal:** Stop choosing policies by hand. One class looks at each ticket and picks its policy, so the rules for "which policy?" live in exactly one place.

**You will need:** [Trade-offs: Strategy](../t13_design_patterns_1_notes.md#trade-offs-strategy) and your classes from Exercise 06.

**Steps**

1. Create `ProcessingPolicySelector` with the method below.
2. If the description contains `training`, in any case, return a `DryRunTraining`.
3. Otherwise, if the ticket is not valid, or the total is €30 or more, or there are 6 or more items, return a `ValidatedProcess`.
4. Otherwise, return an `ImmediateProcess`.
5. In `run()`, create the four tickets shown in the example, add a command for each using the selector, and process the queue.

```java
public OrderProcessingStrategy select(OrderTicket ticket)
```

**Example**

```text
IMMEDIATE MAKE 201 1 cappuccino, 1 croissant (€7.80)
REJECT 202 invalid
TRAINING would make 203 training: 2 lattes, 1 muffin (€12.50)
VALIDATED MAKE 204 8 americanos, 2 toasties (€42.00)
```

**Rules**

- Import the Exercise 06 classes. Do not copy them.
- The selector is the **only** class with `if` statements about policies.
- Check for training first. A large training ticket is still only a practice run.

<details>
<summary>Hints</summary>

1. `description.toLowerCase(Locale.ROOT).contains("training")` ignores case. Check the description is not `null` first.
2. Create the three strategies once, as fields of the selector, and return the same objects each time. They hold no data, so they can be shared.

</details>

**Done when**

- [ ] `run()` prints the four lines shown.
- [ ] Ticket 202, which is blank, is rejected rather than made.
- [ ] Adding a new policy means changing the selector only, not the queue or the command.

**Think about it:** without the "not valid" check in step 3, ticket 202 (blank, €5.00, one item) would go to `ImmediateProcess`, and the café would make an empty order. Why does that check belong in the selector rather than in `ImmediateProcess`?

**Package:** `t13_design_patterns_1.exercises.ex07`

**Solution:** [t13 exercise 07 solution code](../../../../code/src/t13_design_patterns_1/exercises/ex07/).
**Tests:** [t13 exercise 07 tests](../../../../code/test/t13_design_patterns_1/exercises/ex07/).
