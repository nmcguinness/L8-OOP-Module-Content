---
title: "Design Patterns I — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t13_design_patterns_1
description: "Six exercises applying Strategy and Command, from swappable combat AI and pricing rules to macro commands and an undo stack."
created: 2026-02-03
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, design-patterns, strategy, command, decoupling, oop, exercises, year2, comp-c8z03]
---
# Design Patterns I — Exercises

> These exercises build directly on the **Design Patterns I** notes.
> This set focuses on **Strategy** and **Command**, including two exercises where you combine
> both patterns in the same mini-system.

## Ground rules

- Keep selection logic out of your “core” classes (no giant `if`/`switch` blocks in the wrong
  place).
- No `instanceof` checks to decide behaviour.
- Print small outputs in `run()` so you can quickly verify behaviour.

## How to run

Each exercise assumes a package like:

```java
t13_design_patterns_1.exercises.exNN
````

Create a class `Exercise` in that package with a static entry point:

```java
package t13_design_patterns_1.exercises.ex01;

public class Exercise {
    public static void run() {
        // call your methods here; print results for quick checks
    }
}
```

---

## Exercise 01 — Combat AI attack behaviour (Strategy)

**Objective:** Build a tiny combat model where an `Enemy` can attack at a given distance, but
the **rules for how damage is computed** live outside the `Enemy` class.

Your finished solution should let you:
- create multiple enemies using the **same** `Enemy` class
- give each enemy a different `AttackStrategy` (e.g., melee vs ranged)
- call `enemy.attack(distanceMeters)` and get damage back based on that strategy’s rules

In other words: `Enemy` should *not* contain “if melee then ... else if ranged then ...”.
Instead, the `Enemy` delegates damage calculation to the strategy object you supplied. In
`run()`, you should be able to show the *same distances* producing different results for
different enemy strategies.

**Context (software + games):**

- **Software dev:** avoid giant conditional logic for “modes” by encapsulating behaviour.
- **Games dev:** enemies often share the same base stats but differ in combat behaviour (melee,
  ranged, cautious, aggressive).

### What you are building

A tiny combat model where:

- an enemy has a **base damage**
- an enemy has an **AttackStrategy**
- the strategy decides the damage based on distance

### Required API

Implement these types:

```java
interface AttackStrategy {
    int computeDamage(int baseDamage, int distanceMeters);
}

class Enemy {
    public Enemy(String name, int baseDamage, AttackStrategy strategy) { }
    public String getName() { return ""; }
    public int attack(int distanceMeters) { return 0; }
}
```

### Tasks

1. Create an interface `AttackStrategy` with:
   `int computeDamage(int baseDamage, int distanceMeters)`
2. Implement two strategies:

   - `MeleeAttack`: returns `baseDamage + 5` only when `distanceMeters <= 2` (else return 0)
   - `RangedAttack`: returns `baseDamage` only when `distanceMeters <= 10` (else return 0)
3. Create an `Enemy` class that stores `name`, `baseDamage`, and an `AttackStrategy`.
4. Add `int attack(int distanceMeters)` on `Enemy` that delegates to the strategy.
5. In `run()`, create one melee enemy and one ranged enemy and print damage at distances `1`,
   `5`, `12`.

### Done when...

- Your `Enemy` class contains **no** “mode selection” logic (`if`/`switch` on enemy type).
- Damage gating (distance checks) is inside the strategies.
- You can swap strategy objects without changing `Enemy` code.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Solution
  </summary>

```java
package t13_design_patterns_1.exercises.ex01;

public class Exercise
{
    public static void run()
    {
        Enemy skeleton = new Enemy("Skeleton", 10, new MeleeAttack());
        Enemy archer = new Enemy("Archer", 10, new RangedAttack());

        int[] distances = new int[] { 1, 5, 12 };

        for (int d : distances)
        {
            System.out.println(skeleton.getName() + " at " + d + "m: " + skeleton.attack(d));
        }

        for (int d : distances)
        {
            System.out.println(archer.getName() + " at " + d + "m: " + archer.attack(d));
        }
    }
}

interface AttackStrategy
{
    int computeDamage(int baseDamage, int distanceMeters);
}

class MeleeAttack implements AttackStrategy
{
    @Override
    public int computeDamage(int baseDamage, int distanceMeters)
    {
        if (distanceMeters > 2)
            return 0;

        return baseDamage + 5;
    }
}

class RangedAttack implements AttackStrategy
{
    @Override
    public int computeDamage(int baseDamage, int distanceMeters)
    {
        if (distanceMeters > 10)
            return 0;

        return baseDamage;
    }
}

class Enemy
{
    private String _name;
    private int _baseDamage;
    private AttackStrategy _strategy;

    public Enemy(String name, int baseDamage, AttackStrategy strategy)
    {
        _name = name;
        _baseDamage = baseDamage;
        _strategy = strategy;
    }

    public String getName()
    {
        return _name;
    }

    public int attack(int distanceMeters)
    {
        return _strategy.computeDamage(_baseDamage, distanceMeters);
    }
}
```

</details>

---

## Exercise 02 — Pricing rules (Strategy)

**Objective:** Build a checkout/pricing component where the **final price calculation** is
fully controlled by a `PriceStrategy`.

Your finished solution should let you:
- take a `basePrice` and produce a `finalPrice` using a chosen pricing rule
- swap pricing rules without changing the `Checkout` class (only swap the strategy object)
- keep “special business rules” (like a minimum price floor) inside the strategy that owns that rule

In `run()`, you should be able to compute prices for the same base value using multiple
strategies and clearly see different results (including a case that triggers the “never below
€5.00” behaviour).

**Context (software + games):**

- **Software dev:** pricing rules evolve constantly (discounts, promos, minimum price floors).
- **Games dev:** in-game stores often apply different pricing rules (membership discounts,
  events, bundles).

### What you are building

A mini checkout that can swap pricing rules by passing a different strategy object.

### Required API

```java
interface PriceStrategy {
    double finalPrice(double basePrice);
}

class Checkout {
    public Checkout(PriceStrategy pricing) { }
    public double price(double basePrice) { return 0.0; }
}
```

### Tasks

1. Create `PriceStrategy` with:
   `double finalPrice(double basePrice)`
2. Implement strategies:

   - `NoDiscount`: returns base price
   - `StudentDiscount`: 10% off
   - `BlackFridayDiscount`: 30% off, but never below €5.00
3. Create `Checkout` that stores a `PriceStrategy` and exposes:
   `double price(double basePrice)`
4. In `run()`, show results for base prices `10.00` and `4.00` using all three strategies.

### Done when...

- `Checkout` contains **no** `if`/`switch` logic for which discount to apply.
- Your “never below €5.00” clamp rule lives inside `BlackFridayDiscount`.
- You can add another pricing strategy without changing `Checkout`.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Solution
  </summary>

```java
package t13_design_patterns_1.exercises.ex02;

public class Exercise
{
    public static void run()
    {
        double[] basePrices = new double[] { 10.00, 4.00 };

        Checkout none = new Checkout(new NoDiscount());
        Checkout student = new Checkout(new StudentDiscount());
        Checkout bf = new Checkout(new BlackFridayDiscount());

        for (double p : basePrices)
        {
            System.out.println("Base " + p + " -> none: " + none.price(p));
            System.out.println("Base " + p + " -> student: " + student.price(p));
            System.out.println("Base " + p + " -> black friday: " + bf.price(p));
        }
    }
}

interface PriceStrategy
{
    double finalPrice(double basePrice);
}

class NoDiscount implements PriceStrategy
{
    @Override
    public double finalPrice(double basePrice)
    {
        return basePrice;
    }
}

class StudentDiscount implements PriceStrategy
{
    @Override
    public double finalPrice(double basePrice)
    {
        return basePrice * 0.9;
    }
}

class BlackFridayDiscount implements PriceStrategy
{
    @Override
    public double finalPrice(double basePrice)
    {
        double discounted = basePrice * 0.7;

        if (discounted < 5.0)
            return 5.0;

        return discounted;
    }
}

class Checkout
{
    private PriceStrategy _pricing;

    public Checkout(PriceStrategy pricing)
    {
        _pricing = pricing;
    }

    public double price(double basePrice)
    {
        return _pricing.finalPrice(basePrice);
    }
}
```

</details>

---

## Exercise 03 — Execute a task with a policy (Strategy + Command)

**Objective:** Build a mini job runner where:
- a **Command** represents “a piece of work that can be triggered”
- a **Strategy** represents “the policy for how that work is executed”

Your finished solution should let you:
- define one `Task` (the thing that ultimately runs)
- execute the *same* task under different policies (e.g., fast vs safe)
- keep the policy decision out of the task itself (the task should not decide whether it runs
  validated or unchecked)

In `run()`, you should execute the same task twice and show different behaviour/output
depending on whether you chose a “fast/unchecked” execution strategy or a “safe/validated”
execution strategy.

**Context (software + games):**

- **Software dev:** run jobs with different policies (fast vs safe, strict vs permissive).
- **Games dev:** run gameplay actions under different rules (e.g., debug mode vs release mode).

### What you are building

A task executor where the same “execute task” command can run under different execution strategies.

### Required API

```java
interface Command {
    void execute();
}

interface TaskExecutionStrategy {
    void execute(Task task);
}

class ExecuteTaskCommand implements Command {
    public ExecuteTaskCommand(Task task, TaskExecutionStrategy strategy) { }
}
```

### Tasks

1. Create a `Task` with:

   - `String name`
   - `void validate()` prints `Validating: <name>`
   - `void run()` prints `Running: <name>`
   - `void runUnchecked()` prints `Running unchecked: <name>`
2. Create `TaskExecutionStrategy` with `void execute(Task task)`.
3. Implement:

   - `FastExecution`: uses `task.runUnchecked()`
   - `SafeExecution`: uses `task.validate()` then `task.run()`
4. Create `Command` with `void execute()`.
5. Create `ExecuteTaskCommand(Task task, TaskExecutionStrategy strategy)`.
6. In `run()`, execute the same task twice (fast then safe).

### Done when...

- The command does not contain selection logic; it just delegates to its strategy.
- You can add another strategy (e.g., “DryRunExecution”) without changing the command.
- The same `Task` can be executed in multiple ways by swapping strategy objects.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Solution
  </summary>

```java
package t13_design_patterns_1.exercises.ex03;

public class Exercise
{
    public static void run()
    {
        Task task = new Task("Export leaderboard");

        Command fast = new ExecuteTaskCommand(task, new FastExecution());
        Command safe = new ExecuteTaskCommand(task, new SafeExecution());

        System.out.println("--- FAST ---");
        fast.execute();

        System.out.println("--- SAFE ---");
        safe.execute();
    }
}

interface Command
{
    void execute();
}

class ExecuteTaskCommand implements Command
{
    private Task _task;
    private TaskExecutionStrategy _strategy;

    public ExecuteTaskCommand(Task task, TaskExecutionStrategy strategy)
    {
        _task = task;
        _strategy = strategy;
    }

    @Override
    public void execute()
    {
        _strategy.execute(_task);
    }
}

interface TaskExecutionStrategy
{
    void execute(Task task);
}

class FastExecution implements TaskExecutionStrategy
{
    @Override
    public void execute(Task task)
    {
        task.runUnchecked();
    }
}

class SafeExecution implements TaskExecutionStrategy
{
    @Override
    public void execute(Task task)
    {
        task.validate();
        task.run();
    }
}

class Task
{
    private String _name;

    public Task(String name)
    {
        _name = name;
    }

    public void validate()
    {
        System.out.println("Validating: " + _name);
    }

    public void run()
    {
        System.out.println("Running: " + _name);
    }

    public void runUnchecked()
    {
        System.out.println("Running unchecked: " + _name);
    }
}
```

</details>

---

## Exercise 04 — MacroCommand (commands as building blocks)

**Objective:** Build a `MacroCommand` that behaves like a normal `Command`, but internally runs
**a list of commands in order**.

Your finished solution should let you:
- create small commands that each perform one step (here: printing a message)
- combine them into a macro that represents a larger workflow
- execute the macro with one call to `execute()`, producing the same order of steps you added

In `run()`, calling `macro.execute()` once should produce a clear multi-step output (e.g.,
`start → validate → execute → done`). The key requirement is that the macro is treated just
like any other command, meaning it implements the same interface.

**Context (software + games):**

- **Software dev:** “apply preset” or “run workflow” made of smaller steps.
- **Games dev:** combo actions, scripted sequences, or tool pipelines (export, bake, package).

### What you are building

A `MacroCommand` that contains multiple commands and executes them in order.

### Required API

```java
interface Command {
    void execute();
}

class MacroCommand implements Command {
    public void add(Command command) { }
}
```

### Tasks

1. Create `Command` with `void execute()`.
2. Implement `PrintCommand(String message)` that prints the message.
3. Implement `MacroCommand` that contains a `List<Command>`.
4. `MacroCommand.execute()` must execute all inner commands in order.
5. In `run()`, create a macro that prints `start`, then 2–3 steps, then `done`.

### Done when...

- `MacroCommand` implements the same `Command` interface as the commands it contains.
- You can pass a `MacroCommand` anywhere a `Command` is expected.
- The macro prints messages in the same order they were added.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Solution
  </summary>

```java
package t13_design_patterns_1.exercises.ex04;

import java.util.ArrayList;
import java.util.List;

public class Exercise
{
    public static void run()
    {
        MacroCommand macro = new MacroCommand();
        macro.add(new PrintCommand("start"));
        macro.add(new PrintCommand("validate"));
        macro.add(new PrintCommand("execute"));
        macro.add(new PrintCommand("done"));

        macro.execute();
    }
}

interface Command
{
    void execute();
}

class PrintCommand implements Command
{
    private String _message;

    public PrintCommand(String message)
    {
        _message = message;
    }

    @Override
    public void execute()
    {
        System.out.println(_message);
    }
}

class MacroCommand implements Command
{
    private List<Command> _commands = new ArrayList<>();

    public void add(Command command)
    {
        _commands.add(command);
    }

    @Override
    public void execute()
    {
        for (Command c : _commands)
        {
            c.execute();
        }
    }
}
```

</details>

---

## Exercise 05 — Undoable commands (undo stack)

**Objective:** Build an undo system where each user action is represented as an object that can
both:
- apply the change (`execute()`)
- reverse the same change (`undo()`)

Your finished solution should let you:
- apply multiple changes to a shared receiver object (a `Counter`)
- push each executed command onto a history stack
- undo the *most recent* command(s) in reverse order (LIFO)
- verify that state returns to the correct previous values after each undo

In `run()`, you should demonstrate a sequence like “+5, +2, +10” and then undo the last two
actions, showing the counter stepping back correctly. The undo logic should not “guess” — it
should be the exact inverse of what was executed.

**Context (software + games):**

- **Software dev:** undo/redo in editors, admin tools, form builders.
- **Games dev:** tool history, level editor actions, replayable input/actions.

### What you are building

A `Counter` that can be changed via undoable commands and reverted using a history stack.

### Required API

```java
interface UndoableCommand {
    void execute();
    void undo();
}

class AddNumberCommand implements UndoableCommand {
    public AddNumberCommand(Counter counter, int amount) { }
}
```

### Tasks

1. Create `Counter` with:

   - private field `_value`
   - `void add(int amount)`
   - `int getValue()`
2. Create `UndoableCommand` with `execute()` and `undo()`.
3. Implement `AddNumberCommand(Counter counter, int amount)`:

   - `execute()` adds the amount
   - `undo()` subtracts the same amount
4. In `run()`:

   - Create `ArrayDeque<UndoableCommand> history`
   - Execute: add 5, add 2, add 10 (counter should be 17)
   - Undo last two (counter should be 5)
   - Print counter after each step

### Done when...

- Undo reverses exactly what execute did.
- Undo order is LIFO (stack behaviour).
- Your history structure does not throw exceptions for normal usage.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Solution
  </summary>

```java
package t13_design_patterns_1.exercises.ex05;

import java.util.ArrayDeque;

public class Exercise
{
    public static void run()
    {
        Counter counter = new Counter();
        ArrayDeque<UndoableCommand> history = new ArrayDeque<>();

        executeAndRecord(history, new AddNumberCommand(counter, 5));
        System.out.println("After +5: " + counter.getValue());

        executeAndRecord(history, new AddNumberCommand(counter, 2));
        System.out.println("After +2: " + counter.getValue());

        executeAndRecord(history, new AddNumberCommand(counter, 10));
        System.out.println("After +10: " + counter.getValue());

        undoLast(history);
        System.out.println("After undo: " + counter.getValue());

        undoLast(history);
        System.out.println("After undo: " + counter.getValue());
    }

    private static void executeAndRecord(ArrayDeque<UndoableCommand> history, UndoableCommand command)
    {
        command.execute();
        history.push(command);
    }

    private static void undoLast(ArrayDeque<UndoableCommand> history)
    {
        if (history.isEmpty())
            return;

        history.pop().undo();
    }
}

interface UndoableCommand
{
    void execute();
    void undo();
}

class AddNumberCommand implements UndoableCommand
{
    private Counter _counter;
    private int _amount;

    public AddNumberCommand(Counter counter, int amount)
    {
        _counter = counter;
        _amount = amount;
    }

    @Override
    public void execute()
    {
        _counter.add(_amount);
    }

    @Override
    public void undo()
    {
        _counter.add(-_amount);
    }
}

class Counter
{
    private int _value;

    public void add(int amount)
    {
        _value += amount;
    }

    public int getValue()
    {
        return _value;
    }
}
```

</details>

---

## Exercise 06 — Café order tickets dispatcher (Command + Strategy)

**Objective:** Build a simple order-ticket dispatcher where:
- each ticket is processed via a **Command** (so tickets can be queued and executed later)
- the processing behaviour is selected via a **Strategy** (so different tickets can be handled
  differently)

In `run()`, you should enqueue multiple tickets that trigger each policy and show distinct
outputs (`IMMEDIATE MAKE ...`, `VALIDATED MAKE ...`, `REJECT ...`, `TRAINING ...`). The
important part is that the queue does not contain the selection logic — it simply runs commands
— and the strategy selection is done in one place (the selector).

### Context

You are building a tiny “order processing” system for a café. Orders arrive from different
places (counter staff, self-service kiosk, mobile app). The café wants a consistent way to
handle these orders without copying logic everywhere.

Instead of processing orders immediately, we represent each order as a ticket and add it to a
queue. Later, the kitchen/barista station processes tickets in order.

To keep behaviour flexible, the café uses different processing policies:
- Some tickets can be processed immediately.
- Big / expensive tickets must be validated first (to reduce mistakes).
- Training-mode tickets should not create real orders — they should only print what would happen.

This problem is perfect for Command + Strategy together:

Command = “this is a unit of work that can be queued”
Strategy = “this is the policy for how that work should run”

Typical use cases include the following:
- Queueing: During a rush, tickets pile up and are processed FIFO.
- Validation: Large orders should be double-checked (e.g., “12 coffees + 6 sandwiches”) so you
  don’t waste stock.
- Training mode: New staff can practise entering orders without actually sending them to the
  kitchen.

### What you are building

A tiny order-ticket system for a café:

- Orders arrive from the counter, kiosk, or app.
- Each order becomes a **ticket** and is queued FIFO.
- A selector chooses how each ticket is processed:

  - normal tickets process immediately
  - big orders are validated first
  - training tickets do a dry-run print only

### Required API

```java
interface Command {
    void execute();
}

interface OrderProcessingStrategy {
    void process(OrderTicket ticket);
}

class ProcessingPolicySelector {
    public OrderProcessingStrategy select(OrderTicket ticket) { return null; }
}

class TicketQueue {
    public void add(Command command) { }
    public void processAll() { }
}
```

### Tasks

#### Part A — model the ticket

1. Create `OrderTicket` with:

   - `int ticketId`
   - `String description` (e.g., `"2 lattes, 1 muffin"`)
   - `double totalEuro`
   - `int itemCount`
2. Add `boolean isValid()` with rules:

   - description must not be blank
   - `totalEuro > 0`
   - `itemCount > 0`

#### Part B — define processing strategies

1. Create `OrderProcessingStrategy` with: `void process(OrderTicket ticket)`
2. Implement:

   - `ImmediateProcess`: prints `MAKE <id> <description> (€<total>)`
   - `ValidatedProcess`: if invalid prints `REJECT <id> invalid` and returns; else prints `MAKE ...`
   - `DryRunTraining`: prints `TRAINING <id> would make: <description> (€<total>)`

#### Part C — command + queue

1. Create `Command` with `void execute()`.
2. Create `ProcessTicketCommand(OrderTicket ticket, OrderProcessingStrategy strategy)`.
3. Create `TicketQueue` using `Queue<Command>` (`ArrayDeque`) with `add(...)` and `processAll()`.

#### Part D — strategy selection

1. Create `ProcessingPolicySelector` with: `OrderProcessingStrategy select(OrderTicket ticket)`
2. Rules:

   - If description contains `"training"` (case-insensitive), use `DryRunTraining`
   - Else if `totalEuro >= 30` or `itemCount >= 6`, use `ValidatedProcess`
   - Else use `ImmediateProcess`

#### Part E — demo in `run()`

 1. Create 4 tickets:

- normal small order
- invalid order (blank description or total 0)
- training-mode ticket (description contains “training”)
- large/high-risk order (forces validated)

Enqueue a command for each ticket, then process the queue.

### Done when...

- Strategy selection happens in `ProcessingPolicySelector`, not inside the queue or command.
- The queue keeps processing even after one ticket is rejected.
- You can add another processing strategy without changing the queue.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Solution
  </summary>

```java
package t13_design_patterns_1.exercises.ex06;

import java.util.ArrayDeque;
import java.util.Queue;

public class Exercise
{
    public static void run()
    {
        ProcessingPolicySelector selector = new ProcessingPolicySelector();
        TicketQueue queue = new TicketQueue();

        OrderTicket t1 = new OrderTicket(201, "1 cappuccino, 1 croissant", 7.80, 2);
        OrderTicket t2 = new OrderTicket(202, "   ", 5.00, 1);
        OrderTicket t3 = new OrderTicket(203, "training: 2 lattes, 1 muffin", 12.50, 3);
        OrderTicket t4 = new OrderTicket(204, "8 americanos, 2 toasties", 42.00, 10);

        enqueue(queue, selector, t1);
        enqueue(queue, selector, t2);
        enqueue(queue, selector, t3);
        enqueue(queue, selector, t4);

        queue.processAll();
    }

    private static void enqueue(TicketQueue queue, ProcessingPolicySelector selector, OrderTicket ticket)
    {
        OrderProcessingStrategy strategy = selector.select(ticket);
        queue.add(new ProcessTicketCommand(ticket, strategy));
    }
}

interface Command
{
    void execute();
}

class ProcessTicketCommand implements Command
{
    private OrderTicket _ticket;
    private OrderProcessingStrategy _strategy;

    public ProcessTicketCommand(OrderTicket ticket, OrderProcessingStrategy strategy)
    {
        _ticket = ticket;
        _strategy = strategy;
    }

    @Override
    public void execute()
    {
        _strategy.process(_ticket);
    }
}

interface OrderProcessingStrategy
{
    void process(OrderTicket ticket);
}

class ImmediateProcess implements OrderProcessingStrategy
{
    @Override
    public void process(OrderTicket ticket)
    {
        System.out.println("IMMEDIATE MAKE " + ticket.getTicketId() + " " + ticket.getDescription() + " (€" + ticket.getTotalEuro() + ")");
    }
}

class ValidatedProcess implements OrderProcessingStrategy
{
    @Override
    public void process(OrderTicket ticket)
    {
        if (!ticket.isValid())
        {
            System.out.println("REJECT " + ticket.getTicketId() + " invalid");
            return;
        }

        System.out.println("VALIDATED MAKE " + ticket.getTicketId() + " " + ticket.getDescription() + " (€" + ticket.getTotalEuro() + ")");
    }
}

class DryRunTraining implements OrderProcessingStrategy
{
    @Override
    public void process(OrderTicket ticket)
    {
        System.out.println("TRAINING " + ticket.getTicketId() + " would make: " + ticket.getDescription() + " (€" + ticket.getTotalEuro() + ")");
    }
}

class ProcessingPolicySelector
{
    private OrderProcessingStrategy _immediate = new ImmediateProcess();
    private OrderProcessingStrategy _validated = new ValidatedProcess();
    private OrderProcessingStrategy _training = new DryRunTraining();

    public OrderProcessingStrategy select(OrderTicket ticket)
    {
        String desc = ticket.getDescription();

        if (desc != null && desc.toLowerCase().contains("training"))
            return _training;

        if (ticket.getTotalEuro() >= 30.0 || ticket.getItemCount() >= 6)
            return _validated;

        return _immediate;
    }
}

class TicketQueue
{
    private Queue<Command> _queue = new ArrayDeque<>();

    public void add(Command command)
    {
        _queue.add(command);
    }

    public void processAll()
    {
        while (!_queue.isEmpty())
        {
            _queue.poll().execute();
        }
    }
}

class OrderTicket
{
    private int _ticketId;
    private String _description;
    private double _totalEuro;
    private int _itemCount;

    public OrderTicket(int ticketId, String description, double totalEuro, int itemCount)
    {
        _ticketId = ticketId;
        _description = description;
        _totalEuro = totalEuro;
        _itemCount = itemCount;
    }

    public int getTicketId()
    {
        return _ticketId;
    }

    public String getDescription()
    {
        return _description;
    }

    public double getTotalEuro()
    {
        return _totalEuro;
    }

    public int getItemCount()
    {
        return _itemCount;
    }

    public boolean isValid()
    {
        if (_description == null)
            return false;

        if (_totalEuro <= 0)
            return false;

        if (_itemCount <= 0)
            return false;

        return !_description.trim().isEmpty();
    }
}
```

</details>

---

## Lesson Context

```yaml
linked_lesson:
  topic_code: "t13_design_patterns_1"
  patterns: ["strategy", "command"]
  primary_domain_emphasis: "Balanced"
  difficulty_tier: "Intermediate → Advanced"
```
