---
title: "Design Patterns II — Structure, Coordination & Extension"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t14_design_patterns_2
description: "How creation and coordination become design problems at scale, and how Factory, Observer, and Adapter help systems evolve without collapsing into coupling and conditionals."
created: 2026-02-04
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, design-patterns, factory, observer, adapter, coordination, architecture, oop, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO2]
previous_topic: t13_design_patterns_1
prerequisites:
  - Design Patterns I (Strategy and Command)
  - Interfaces and composition
  - Recognising tight coupling and conditional logic smells
  - Collections (basic collections and queues)
---

# Design Patterns II — Structure, Coordination & Extension

> **Prerequisites:**
> - You understand Strategy and Command patterns
> - You can reason about interfaces and composition
> - You can identify tight coupling and conditional logic smells
> - You are comfortable with basic collections and queues

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Pattern 1: Factory](#pattern-1-factory)
- [Pattern 2: Observer](#pattern-2-observer)
- [Pattern 3: Adapter](#pattern-3-adapter)
- [Pattern comparison (important)](#pattern-comparison-important)
- [Reflective questions](#reflective-questions)
- [Further reading](#further-reading)

---

## What you'll learn

| Skill Type | You will be able to... |
| :- | :- |
| Understand | Explain why object creation and coordination become design problems at scale. |
| Apply | Use Factory to remove creation logic from clients. |
| Apply | Use Observer to decouple state change from reaction. |
| Apply | Use Adapter to integrate incompatible interfaces safely. |
| Analyse | Identify when multiple patterns must collaborate to solve a single problem. |
| Debug | Diagnose over-coupling caused by misplaced responsibilities. |

---

## Why this matters

In Design Patterns I, we learned how to:
- encapsulate behaviour (Strategy),
- encapsulate work (Command).

As systems grow, new problems appear:
- object creation logic spreads,
- components need to react to events,
- external APIs do not fit our design.

This lesson focuses on **structural and coordination pressure**: how parts of a system are
created, how they react to change, and how they evolve without collapsing into conditional
logic.

---

## How this builds on previous content

From last week, we already have:
- Commands representing work,
- Queues executing commands,
- Strategies defining execution policy.

That design works — **until it doesn’t**.
Today we fix the cracks.

---

## Pattern 1: Factory

### The pain: creation logic leakage

```java
Command cmd =
    new ExecuteTaskCommand(
        task,
        new ValidatedExecution()
    );
```

Problems:
- clients must know which strategy to use,
- object graphs grow complex,
- changes ripple outward.

Creation is becoming a **responsibility**, not a detail.

---

### Intent

Encapsulate object creation so clients depend on *what* they want, not *how* it is built.

### Pattern roles

- **Client**: requests an object
- **Factory**: decides how objects are created
- **Product**: interface of the created object

```kroki-plantuml
' alt: Factory pattern — Client asks Factory for a Command; Factory creates the right concrete type
@startuml
skinparam backgroundColor white
skinparam ClassFontName monospaced
skinparam ClassBackgroundColor #F8F8F8
skinparam ClassBorderColor #777
skinparam ArrowColor #444

class Client

interface Command {
    + execute()
    + undo()
}

interface TaskCommandFactory {
    + createFor(task : Task) : Command
}

class DefaultTaskCommandFactory {
    + createFor(task : Task) : Command
}

class ExecuteTaskCommand {
    + execute()
    + undo()
}

Client         -right-> TaskCommandFactory  : requests
TaskCommandFactory <|.. DefaultTaskCommandFactory
DefaultTaskCommandFactory ..> ExecuteTaskCommand : creates
Command <|.. ExecuteTaskCommand
@enduml
```

### Trade-offs

- Adds indirection and extra types
- Centralises responsibility (can become a bottleneck)
- Overkill for very small systems

---

### Factory interface

```java
public interface TaskCommandFactory {
    Command createFor(Task task);
}
```

---

### Concrete factory

```java
public final class DefaultTaskCommandFactory
        implements TaskCommandFactory {

    private final TaskExecutionStrategy _strategy;

    public DefaultTaskCommandFactory(TaskExecutionStrategy strategy) {
        _strategy = strategy;
    }

    @Override
    public Command createFor(Task task) {
        return new ExecuteTaskCommand(task, _strategy);
    }
}
```

---

<details style="background:#fffdf0; border:1px solid rgba(161,98,7,0.22); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Aside: Why Factory beats scattered <code>new</code>
  </summary>

Using `new` is not bad — **spreading creation logic everywhere is**.

Factory allows:
- configuration-based creation,
- testing with substitutes,
- future extension without client changes.

</details>

---

## Pattern 2: Observer

### The pain: hidden coordination

Suppose we want to:
- log task completion,
- update a UI,
- trigger follow-up work.

Embedding this logic directly into execution reintroduces **tight coupling**.

---

### Intent

Define a one-to-many dependency so that when one object changes state, all dependents are
notified automatically.

### Pattern roles

- **Subject**: publishes events
- **Observer**: reacts to changes
- **ConcreteObserver**: performs specific reactions

### Trade-offs

- Control flow becomes indirect
- Debugging event chains can be harder
- Poor naming leads to confusion

---

### Observer interface

```java
public interface TaskListener {
    void onCompleted(Task task);
}
```

---

### Subject (publisher)

```java
public final class ObservableTaskQueue {

    private final List<TaskListener> _listeners = new ArrayList<>();
    private final Queue<Task> _pending = new ArrayDeque<>();
    private final TaskCommandFactory _factory;

    public ObservableTaskQueue(TaskCommandFactory factory) {
        _factory = factory;
    }

    public void addListener(TaskListener listener) {
        _listeners.add(listener);
    }

    public void submit(Task task) {
        _pending.add(task);
    }

    public void processAll() {
        while (!_pending.isEmpty()) {
            Task task = _pending.poll();
            _factory.createFor(task).execute();
            notifyListeners(task);
        }
    }

    private void notifyListeners(Task completedTask) {
        for (TaskListener l : _listeners)
            l.onCompleted(completedTask);
    }
}
```

---

### Why this is non-trivial

This introduces **coordination without dependency**:
- the queue does not know who is listening,
- listeners evolve independently,
- reactions can be added without modification.

---

## Pattern 3: Adapter

### The pain: incompatible interfaces

Assume an external API:

```java
public interface ExternalJob {
    void runJob();
}
```

Your system expects `Command`.

---

### Intent

Convert the interface of a class into another interface clients expect.

### Pattern roles

- **Target**: expected interface (`Command`)
- **Adaptee**: existing incompatible class
- **Adapter**: bridges the two

### Trade-offs

- Adds an extra layer
- Can hide deeper design problems
- Should not patch poor internal design

---

### Adapter example

```java
public final class ExternalJobAdapter implements Command {

    private final ExternalJob _job;

    public ExternalJobAdapter(ExternalJob job) {
        _job = job;
    }

    @Override
    public void execute() {
        _job.runJob();
    }
}
```

---

<details style="background:#fff5f5; border:1px solid rgba(220,38,38,0.25); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Gotchas: Adapter vs rewrite
  </summary>

Adapter is appropriate when:
- you do not control the external API,
- rewriting would duplicate logic,
- isolation from change is required.

If you control the code, redesign may be better.

</details>

---

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Real-world: Coordinated task processing
  </summary>

#### Context

We now have:
- Commands representing work,
- Factories controlling creation,
- Observers reacting to execution,
- Adapters integrating foreign jobs.

**Patterns in play**
- Factory: controls creation of Commands
- Command: encapsulates work units
- Observer: reacts to execution events
- Adapter: integrates external jobs

**Why this matters**
- Creation, execution, and reaction are separated.
- New features attach around the system.
- This is the minimum structure needed before concurrency.

Next week, this design will **fail under parallel load** — and we will fix it with threads.

</details>

---

## Pattern comparison (important)

- **Factory vs Strategy** — creation vs behaviour
- **Observer vs Command** — reaction to state vs representation of work
- **Adapter vs Refactoring** — protection vs redesign

---

<details style="background:#fff5f5; border:1px solid rgba(220,38,38,0.25); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Anti-pattern: Pattern soup
  </summary>

Combining many patterns without real design pressure leads to unnecessary indirection,
harder debugging, and unclear ownership.

Patterns should remove complexity — not introduce it.

</details>

---

### Design prompt (no coding)

You need to add:
- task retries,
- failure logging,
- delayed execution.

Which patterns would you introduce or extend? Justify your choices.

---

### Pattern smell checklist

- Is object creation scattered across the system?
- Do components know too much about each other?
- Does adding behaviour require modifying many classes?

If yes, a pattern may be missing — or misused.

---

## Reflective questions

1. Why does Factory reduce coupling compared to direct construction?
2. What problem does Observer solve that Strategy does not?
3. When is Adapter preferable to refactoring existing code?
4. Which pattern would you introduce first in a growing system — and why?
5. How do these patterns prepare the system for concurrency?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**1. Why Factory reduces coupling.**
Calling `new JdbcTaskDao(url, user, pass)` binds the caller to a **concrete class** *and* to
the knowledge of how to build it. Every such call site must be edited if the constructor gains
a parameter or the implementation is swapped — and those call sites are usually scattered.

A factory moves that knowledge to one place. Callers depend only on the interface (`TaskDao`)
and on the factory; the concrete type is named exactly once. Two things become possible that
were not before: substituting an implementation for tests (an in-memory DAO), and choosing the
implementation at runtime from configuration. The measure of success is that `grep` for the
concrete class name returns one hit.

**2. What Observer solves that Strategy does not.**
Different axis entirely. Strategy answers *how do I perform this step?* — one plug-in, chosen
by the context, usually returning a result to it. Observer answers *who needs to know this
happened?* — **one-to-many notification**, where the publisher does not know or care who is
listening, and gets nothing back.

Concretely: without Observer, a `TaskService` that must update the UI, write an audit log and
send an email has to hold references to all three and call them in order — so it knows about
every consumer, and adding a fourth edits the service. With Observer it publishes one
`taskCreated` event; subscribers register themselves. Swapping Strategy in here does not help,
because Strategy still has the context calling *one* known collaborator.

**3. When Adapter beats refactoring.**
When you **cannot or should not change** the code you need to fit: a third-party library, a
legacy class other teams depend on, generated code, or something with no tests where a rewrite
is genuinely risky. The adapter is a small, isolated translation layer, and if the external API
later changes you fix one class.

Refactor instead when you own the code, have tests, and the mismatch is a design flaw worth
fixing properly — otherwise adapters accumulate and you end up translating between
translations. The pragmatic sequence is often: adapt now to unblock, refactor later behind the
adapter's stable interface.

**4. Which pattern first in a growing system.**
A defensible answer names one and justifies it by the pain observed. The strongest general case
is **Strategy/Factory first**, because the earliest and commonest symptom of growth is
conditional logic on type — `if`/`switch` chains and scattered `new` — and those directly block
extension.

What matters more than the choice is the reasoning: introduce a pattern **in response to actual
duplication or an actual change that hurt**, never speculatively. A pattern added before the
second use case is over-engineering; it adds indirection and files while solving nothing.

**5. How these patterns prepare for concurrency.**
Three ways, and they are what makes [t19](../t19_concurrency/t19_concurrency_notes.md) tractable:

- **Command bundles a unit of work into an object.** That is exactly what
  `ExecutorService.submit()` needs — a self-contained task with its state already captured,
  safe to hand to another thread and run later. Code with the work inline in a method has
  nothing to submit.
- **Factory centralises construction**, so per-thread or pooled resources (a `Connection`, a
  DAO) are created in one controlled place rather than wherever a `new` happened to appear.
  That is where you enforce "one connection per thread".
- **Interfaces and stateless strategies remove shared mutable state.** A stateless strategy is
  inherently thread-safe — nothing to corrupt. The residual risk is precisely what remains
  mutable and shared, which is where `synchronized` belongs.

The honest caveat: Observer is the one that gets *harder*. Notifying listeners from multiple
threads means the subscriber list itself is shared mutable state, and a listener may be invoked
concurrently — so you need a thread-safe collection and listeners that tolerate it.

</details>

---

## Further reading

- Refactoring Guru — Factory Method
  <https://refactoring.guru/design-patterns/factory-method>
- Refactoring Guru — Observer
  <https://refactoring.guru/design-patterns/observer>
- Refactoring Guru — Adapter
  <https://refactoring.guru/design-patterns/adapter>
- Martin Fowler — Patterns of Enterprise Application Architecture
  <https://martinfowler.com/books/eaa.html>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t13_design_patterns_1
  domain_emphasis: Balanced

this_lesson:
  topic_code: t14_design_patterns_2
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO2]
```
