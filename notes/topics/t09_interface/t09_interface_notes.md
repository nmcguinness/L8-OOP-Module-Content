---
title: "Interfaces"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t09_interface
description: "A clear, structured introduction to Java interfaces, why they matter, how they support polymorphism, and how they’re used in Games and Software."
created: 2025-11-24
last_updated: 2026-04-14
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, interfaces, polymorphism, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO2]
previous_topic: t08_inheritance
prerequisites:
  - Inheritance basics (superclasses, method overriding)
  - Collections I–II (ArrayList, LinkedList, Iterators)
---

# Interfaces
>
> **Prerequisites:**
> - You should be comfortable with inheritance, basic polymorphism, and using Java’s
>   Collections API.

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Core Ideas / Concepts](#core-ideas--concepts)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Useful snippets (guards and helpers)](#useful-snippets-guards-and-helpers)
- [Games example — Interactable World Objects + Interface
  Constants](#games-example--interactable-world-objects--interface-constants)
- [Software development example — Logging System with Interface
  Constants](#software-development-example--logging-system-with-interface-constants)
- [Debugging and pitfalls](#debugging-and-pitfalls)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

| Skill Type | You will be able to... |
| :-- | :-- |
| **Conceptual Understanding** | Explain what an interface is, why Java uses them, and how they differ from classes and abstract classes. |
| **Design Skills** | Decide when to use interfaces in your own programs (games or software). |
| **Code Implementation** | Declare interfaces, implement them in classes, and use interface references to enable polymorphism. |
| **Problem-Solving** | Use interfaces to simplify code, reduce duplication, and improve flexibility. |

---

## Why this matters

Interfaces solve a major design problem: **how do we express shared behaviour when inheritance
alone isn’t enough?**
In games, systems often need to treat different objects *the same way* (e.g., anything
damageable, anything interactable).
In software, interfaces help decouple logic from implementation, allowing plug-and-play
replacements, test doubles, and modular design.

Interfaces make your code **more flexible**, **more reusable**, and **easier to extend**
without rewriting everything.

---

## How this builds on previous content

You have already seen:
- How subclasses inherit structure/behaviour from a base class.
- How overriding enables polymorphism.
- How Collections store groups of objects.

Interfaces extend this by allowing:
- A **shared behavioural contract** without shared fields.
- **Multiple implementations** with different internal data.
- **Objects of different class hierarchies** to be treated uniformly.

---

## Core Ideas / Concepts

> For each core idea, we explain the concept, provide a small Java snippet, and then a short
> explanation of what the snippet demonstrates.

---

### Core Idea 1 — Interfaces define behaviour, not structure

Interfaces list *what* an object must be able to do, not *how* it does it.

```java
public interface Interactable {
    void interact();
}
```

#### Snippet explanation

Any class implementing `Interactable` must provide an `interact()` method. No instance fields,
no constructors — just a behavioural promise.

---

### Core Idea 2 — Interfaces can define constants (`public static final`)

Interfaces cannot have normal instance fields, but they **can** declare constants.
All fields in an interface are implicitly `public static final`, even if you don’t write those
keywords.

```java
public interface PhysicsSettings {
    double GRAVITY = 9.81;   // public static final automatically
    int MAX_SPEED = 20;
}
```

#### Snippet explanation

You access constants as `PhysicsSettings.GRAVITY`.
These are compile-time constants, shared across the entire application.
Interfaces **still cannot store per-object state**.

---

### Core Idea 3 — Classes implement interfaces

```java
public class Door implements Interactable {
    public void interact() {
        System.out.println("You open the door.");
    }
}
```

#### Snippet explanation

`Door` is now an “Interactable thing.” You can treat it as a `Door` or as an `Interactable`.

---

### Core Idea 4 — Interface references enable polymorphism

```java
Interactable obj = new Door();
obj.interact();
```

#### Snippet explanation

Even though the variable's type is `Interactable`, Java calls the method from the actual object
(`Door`).
This is standard polymorphic behaviour.

---

### Core Idea 5 — A class can implement multiple interfaces

```java
public interface Damageable { void takeDamage(int amount); }

public class Barrel implements Interactable, Damageable {
    public void interact() { System.out.println("You tap the barrel."); }
    public void takeDamage(int amt) { System.out.println("Barrel cracks!"); }
}
```

#### Snippet explanation

Multiple behaviours, unrelated systems. Interfaces allow cross-cutting behaviours cleanly.

---

### Core Idea 6 — Interfaces + Collections = Power

```java
List<Interactable> interactables = new ArrayList<>();
interactables.add(new Door());
interactables.add(new Barrel());

for (Interactable i : interactables)
    i.interact();
```

#### Snippet explanation

Objects from unrelated hierarchies can be processed uniformly because they share the interface.

---

## Progressive coding steps (A then B then C)

### Step A — Create a behaviour

```java
public interface Moveable {
    void move();
}
```

### Step B — Implement twice

```java
public class Player implements Moveable {
    public void move() { System.out.println("Player walks."); }
}

public class Enemy implements Moveable {
    public void move() { System.out.println("Enemy shuffles."); }
}
```

### Step C — Use polymorphically

```java
Moveable m = new Enemy();
m.move();
```

**Observation:**
The calling code only cares that the object can move — not how it’s implemented.

---

## Useful snippets (guards and helpers)

### Null-safe usefulness wrapper

```java
public static void safeInteract(Interactable obj) {
    if (obj == null)
        System.out.println("Nothing to interact with.");
    else
        obj.interact();
}
```

**Why?**
Students frequently call methods on null references. This pattern avoids common crashes.

---

## Games example — Interactable World Objects + Interface Constants

Here we add constants to an interface to demonstrate **useful, non-contrived cases**.

```java
public interface Interactable {
    int DEFAULT_INTERACTION_RANGE = 2; // constant

    void interact();
}

public class Chest implements Interactable {
    public void interact() {
        System.out.println("Chest opens. Loot spills out.");
    }
}

public class NPC implements Interactable {
    public void interact() {
        System.out.println("NPC: Hello, traveller!");
    }
}
```

**Example usage:**

```java
if (distanceToPlayer < Interactable.DEFAULT_INTERACTION_RANGE)
    target.interact();
```

### Explanation

Game designers/systems engineers frequently want **shared constants** for ranges, layers, tags,
or cooldowns.
Interfaces can group these constants when they conceptually belong to a unified behaviour.

---

## Software development example — Logging System with Interface Constants

```java
public interface Logger {
    String INFO = "[INFO]";      // constants
    String ERROR = "[ERROR]";

    void log(String message);
}

public class ConsoleLogger implements Logger {
    public void log(String msg) {
        System.out.println(INFO + " " + msg);
    }
}

public class FileLogger implements Logger {
    public void log(String msg) {
        // (Pseudo) write ERROR/INFO-coded log to file
    }
}
```

### Explanation

The constants allow consistent formatting across all implementations.
In real applications, interface constants are commonly used for **log levels, format strings,
or system tags**.

---

## Debugging and pitfalls

| Mistake | Why it happens | Fix |
| :-- | :-- |:-|
| Trying to put instance fields in an interface | Confusion with abstract classes | Use an abstract class if shared state is needed |
| Forgetting to implement all interface methods | The class cannot compile | Implement all or mark class `abstract` |
| Trying to instantiate an interface | Interfaces have no constructors | Instantiate concrete implementing classes |
| Overusing interface constants | Students use interface as a “constants dump” | Prefer enums or config classes unless constants are tied to behaviour |
| Mixing “is-a” and “can-do” thinking | Misunderstanding the purpose | Interface = ability; Class = identity + data |

---

## Reflective questions

- When is an interface better than an abstract class?
- In your last assignment, which behaviours would make good interfaces?
- How do interface constants help with consistency in design?
- Can you name a behaviour in your game engine project that deserves a shared interface?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**When an interface beats an abstract class.**
Choose an **interface** when you are describing a *capability* that unrelated types can have.
`Drawable`, `Comparable`, `Runnable`, `AutoCloseable` — a `Player`, a `Menu` and a `Particle`
share no ancestry but can all be drawn. Two decisive advantages: a class may implement **many**
interfaces but extend only **one** class, and an interface imposes no state, so it never
constrains how implementers store their data.

Choose an **abstract class** when subclasses share **state and implementation**, not just a
contract — common fields, a constructor that validates them, and a template method that calls
one abstract step. `Entity` with `_id`, `_position` and a concrete `move()` that calls abstract
`speed()` is an abstract class, because that shared field and logic would have to be duplicated
in every implementer otherwise.

Rule of thumb: **is-a with shared state then abstract class; can-do then interface.** In
practice both appear together — an abstract base for the family, plus interfaces for
capabilities that cut across families.

**Which behaviours make good interfaces.**
A good answer picks behaviours that appear in **more than one otherwise-unrelated class** and
can be described in one or two methods. Strong candidates: `Persistable` (`save`/`load`),
`Validatable` (`validate`), `Damageable` (`takeDamage`), `Serialisable`, a `Repository<T>`
contract. Weak candidates: an interface with one implementer and no prospect of a second (it
adds a file and buys nothing today), or a "kitchen-sink" interface with twelve unrelated
methods that forces implementers to stub half of them.

Keep interfaces **small**. If implementers routinely leave methods empty or throwing, split it
— several focused interfaces beat one broad one, and a class can implement as many as it needs.

**How interface constants help consistency.**
Fields in an interface are implicitly `public static final`, so an interface can carry the
shared vocabulary of a subsystem — `MAX_HEALTH`, `DEFAULT_TIMEOUT`, status strings such as
`STATUS_OK`. The value is that the magic number or string is declared **once**. Every
implementer and caller refers to the same symbol, so a change happens in one place, and a typo
in `"STATUS_OK"` becomes a compile error rather than a silent runtime mismatch — the exact
failure mode that makes a protocol string bug hard to find in [t22
Networking](../t22_networking/t22_networking_notes.md).

Two honest caveats. Constants are part of your public API and cannot be hidden later. And for a
fixed set of related values an **`enum` is usually better** than a handful of `String`
constants, because the compiler then rejects any value outside the set — `String` constants
still allow someone to pass `"OKAY"`.

**A behaviour in your project deserving a shared interface.**
Look for a method you have written more than once in classes with no common parent, or an
`if`/`instanceof` chain switching on type. Both are the same smell. Typical finds: `Updatable`
with `update(double deltaTime)` implemented by every entity, particle and animation, so the
game loop can hold one `List<Updatable>` and call `update` polymorphically; or `Collidable`
with `getBounds()`. The payoff is concrete — the game loop stops knowing which concrete types
exist, and adding a new entity type requires no change to the loop at all.

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t08_inheritance
  domain_emphasis: Balanced

this_lesson:
  topic_code: t09_interface
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO2]
```
