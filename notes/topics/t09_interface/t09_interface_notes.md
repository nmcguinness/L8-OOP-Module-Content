---
title: "Interfaces"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t09_interface
description: "A clear, structured introduction to Java interfaces: contracts, polymorphism, multiple interfaces, default, static and private methods, functional interfaces, and why constants rarely belong in an interface — with Games and Software examples."
created: 2025-11-24
last_updated: 2026-11-04
version: 1.2
status: published
authors: ["OOP Teaching Team"]
tags: [java, interfaces, polymorphism, default-methods, functional-interface, year2, comp-c8z03]
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
> - You should be comfortable with inheritance, basic polymorphism, and using Java’s Collections API.

---

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Key terms](#key-terms)
- [Core Ideas / Concepts](#core-ideas--concepts)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Useful snippets (guards and helpers)](#useful-snippets-guards-and-helpers)
- [Games example — Interactable world objects with default methods](#games-example--interactable-world-objects-with-default-methods)
- [Software development example — A logger with an enum and default methods](#software-development-example--a-logger-with-an-enum-and-default-methods)
- [Common mistakes](#common-mistakes)
- [Debugging and pitfalls](#debugging-and-pitfalls)
- [Practice tasks](#practice-tasks)
- [Reflective questions](#reflective-questions)
- [Further reading](#further-reading)

---

## What you'll learn

| Skill Type | You will be able to... |
| :-- | :-- |
| **Conceptual Understanding** | Explain what an interface is, why Java uses them, and how they differ from classes and abstract classes. |
| **Design Skills** | Decide when to use interfaces in your own programs (games or software). |
| **Code Implementation** | Declare interfaces, implement them in classes, and use interface references to enable polymorphism. |
| **Code Implementation** | Add `default`, `static` and `private` methods to an interface, and resolve a default-method conflict. |
| **Code Implementation** | Recognise a functional interface, and implement one with a class, an anonymous class or a lambda. |
| **Design Skills** | Explain why constants rarely belong in an interface, and choose an `enum` or a constants class instead. |
| **Problem-Solving** | Use interfaces to simplify code, reduce duplication, and improve flexibility. |

---

## Why this matters

Interfaces solve a major design problem: **how do we express shared behaviour when inheritance alone isn’t enough?** In games, systems often need to treat different objects *the same way* (e.g., anything damageable, anything interactable). In software, interfaces help decouple logic from implementation, allowing plug-and-play replacements, test doubles, and modular design.

Interfaces make your code **more flexible**, **more reusable**, and **easier to extend** without rewriting everything.

---

## How this builds on previous content

You have already seen:
- How subclasses inherit structure/behaviour from a base class.
- How overriding enables polymorphism.
- How Collections store groups of objects.
- Interfaces themselves, without the name: `Comparable` and `Comparator` in [t03 Ordering](../t03_ordering/t03_ordering_notes.md), and `List`, `Set` and `Map` in t05 to t07, are all interfaces. `ArrayList` is one class that implements `List`.

Interfaces extend this by allowing:
- A **shared behavioural contract** without shared fields.
- **Multiple implementations** with different internal data.
- **Objects of different class hierarchies** to be treated uniformly.

---

## Key terms

### Interface

An **interface** is a contract: the methods a class promises to provide. A class can implement any number of them.

### Default method

A **default method** has a body in the interface. Implementing classes get it for free and may override it.

### Polymorphism

**Polymorphism** means one variable type can refer to objects of many classes, each responding in its own way.

### Functional interface

A **functional interface** has exactly one abstract method, so a lambda can implement it.

---

## Core Ideas / Concepts

> For each core idea, we explain the concept, provide a small Java snippet, and then a short explanation of what the snippet demonstrates.

---

### Core Idea 1 — Interfaces define behaviour, not structure

Interfaces list *what* an object must be able to do, not *how* it does it.

```java
public interface Interactable {
    void interact();
}
```

#### Snippet explanation

Any class implementing `Interactable` must provide an `interact()` method. No instance fields, no constructors — just a behavioural promise. Methods declared in an interface without a body are implicitly `public abstract`, so you do not write either keyword.

---

### Core Idea 2 — Constants in interfaces: legal, but rarely the right home

#### Explanation

Interfaces cannot have instance fields, but they **can** declare constants. Every field in an interface is implicitly `public static final`, even if you do not write those keywords.

```java
// Legal, but usually a poor design: see the explanation below
public interface PhysicsSettings {
    double GRAVITY = 9.81;
    int MAX_SPEED = 20;
}
```

This works, but it is a well-known anti-pattern, often called the **constant interface**. An interface is meant to describe what implementing classes **can do**. Constants describe no behaviour at all, and they cause three problems:

- A class that writes `implements PhysicsSettings` just to type `GRAVITY` instead of `PhysicsSettings.GRAVITY` now claims to *be* a `PhysicsSettings`. That claim is false, and it is permanent: every subclass inherits it.
- The constants become part of the public type of every implementer, so you cannot remove them later without breaking code.
- `String` or `int` constants for a fixed set of choices still let callers pass any value.

Use one of these instead:

```java
// A fixed set of related choices: an enum
public enum LogLevel { INFO, WARNING, ERROR }

// Shared numeric settings: a final class that cannot be created or extended
public final class Physics {
    public static final double GRAVITY = 9.81;
    public static final int MAX_SPEED = 20;

    private Physics() { }
}
```

#### Snippet explanation

`LogLevel` lets the compiler reject anything outside the three levels. `Physics` groups the numbers without pretending they are a behaviour, and its `private` constructor stops anyone creating a `Physics` object. Callers write `Physics.GRAVITY`, which says clearly where the value comes from. When a value is a **default that implementers might change**, such as an interaction range, use a default method instead; see Core Idea 7.

---

### Core Idea 3 — Classes implement interfaces

```java
public class Door implements Interactable {
    @Override
    public void interact() {
        System.out.println("You open the door.");
    }
}
```

#### Snippet explanation

`Door` is now an "Interactable thing." You can treat it as a `Door` or as an `Interactable`. Put `@Override` on every method that implements an interface method, exactly as you did for overridden methods in t08. If you misspell the name or get a parameter wrong, the compiler reports it instead of silently treating it as a new method. The implementing method must be `public`, because the interface method is.

---

### Core Idea 4 — Interface references enable polymorphism

```java
Interactable obj = new Door();
obj.interact();
```

#### Snippet explanation

Even though the variable's type is `Interactable`, Java calls the method from the actual object (`Door`). This is standard polymorphic behaviour. Declaring variables with the interface type, such as `List<String> names = new ArrayList<>();`, means you can swap the implementation later by changing one line.

---

### Core Idea 5 — A class can implement multiple interfaces

```java
public interface Damageable {
    void takeDamage(int amount);
}

public class Barrel implements Interactable, Damageable {
    @Override
    public void interact() {
        System.out.println("You tap the barrel.");
    }

    @Override
    public void takeDamage(int amount) {
        System.out.println("Barrel cracks!");
    }
}
```

#### Snippet explanation

Multiple behaviours, unrelated systems. A class can extend only one class, but it can implement any number of interfaces. A class can also do both: `class Goblin extends Enemy implements Damageable, Interactable`.

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

### Core Idea 7 — Default methods: behaviour with a fallback

#### Explanation

Since Java 8, an interface method can have a body if it is marked `default`. Every implementing class gets that body for free, and any class may override it. Default methods let an interface offer **convenience behaviour built on its abstract methods**, and let you add a method to an interface later without breaking every class that already implements it.

```java
public interface Interactable {
    void interact();

    // Implementers get 2 unless they override it
    default int interactionRange() {
        return 2;
    }

    // Built on the other methods: no implementer has to write this
    default boolean canInteract(int distanceToPlayer) {
        return distanceToPlayer >= 0 && distanceToPlayer <= interactionRange();
    }
}

public class Npc implements Interactable {
    @Override
    public void interact() {
        System.out.println("Hello, traveller!");
    }

    // Talking works from further away than opening a door
    @Override
    public int interactionRange() {
        return 4;
    }
}
```

#### Snippet explanation

A `Door` that does not override `interactionRange()` uses `2`; an `Npc` uses `4`. `canInteract` calls `interactionRange()` polymorphically, so it gives the right answer for both, and neither class had to write it. You have already used default methods: `list.forEach(...)`, `list.removeIf(...)` and `comparator.reversed()` are all defaults on library interfaces.

A default method still cannot touch instance fields, because interfaces have none. It can only call other methods of the interface.

#### When two defaults collide

If a class implements two interfaces that both provide a default method with the same signature, Java refuses to guess. The class must override the method, and it can call either version with `InterfaceName.super.method()`:

```java
interface Flyer {
    default String move() { return "flies"; }
}

interface Swimmer {
    default String move() { return "swims"; }
}

public class Duck implements Flyer, Swimmer {
    // Required: without this override the class does not compile
    @Override
    public String move() {
        return Flyer.super.move() + " and " + Swimmer.super.move();
    }
}
```

A method inherited from a **class** always wins over a default from an interface, so `class Duck extends Bird implements Swimmer` uses `Bird.move()` if `Bird` has one.

---

### Core Idea 8 — Static and private interface methods

#### Explanation

An interface can also hold `static` methods, which belong to the interface itself, and `private` methods, which are helpers shared by its default methods.

```java
public interface Shape {
    double area();

    default String describe() {
        return label() + " with area " + format(area());
    }

    default String label() {
        return getClass().getSimpleName();
    }

    // Private helper: shared by the defaults, invisible to implementers and callers
    private String format(double value) {
        return String.format("%.2f", value);
    }

    // Static factory: called as Shape.unitSquare(), never on an object
    static Shape unitSquare() {
        return () -> 1.0;
    }
}
```

#### Snippet explanation

`Shape.unitSquare()` is called on the interface name, like `List.of(...)` and `Comparator.comparingInt(...)`, which are static interface methods you have already used. Static methods are **not** inherited by implementing classes. The `private` method `format` keeps duplicated code out of the default methods without adding it to the public contract. `unitSquare` returns a lambda, which is possible because `Shape` has only one abstract method; Core Idea 9 explains why.

---

### Core Idea 9 — Functional interfaces, anonymous classes and lambdas

#### Explanation

An interface with **exactly one abstract method** is a **functional interface**. `Comparator`, `Runnable` and `Comparable` are examples; default and static methods do not count towards the one. You can mark your own with `@FunctionalInterface`, and the compiler then checks the rule for you.

There are three ways to supply an implementation:

```java
@FunctionalInterface
public interface DamageModifier {
    int apply(int baseDamage);
}

// 1. A named class: reusable, can hold fields, has a name in stack traces
public class Doubler implements DamageModifier {
    @Override
    public int apply(int baseDamage) {
        return baseDamage * 2;
    }
}

// 2. An anonymous class: a one-off implementation written where it is used
DamageModifier halve = new DamageModifier() {
    @Override
    public int apply(int baseDamage) {
        return baseDamage / 2;
    }
};

// 3. A lambda: the shortest form, allowed only for a functional interface
DamageModifier plusFive = baseDamage -> baseDamage + 5;

// prints 20, 5 and 15
System.out.println(new Doubler().apply(10));
System.out.println(halve.apply(10));
System.out.println(plusFive.apply(10));
```

#### Snippet explanation

All three objects are `DamageModifier`s and can go in the same `List<DamageModifier>`. The anonymous class is the form you met for `Comparator` in t03: it declares and creates an unnamed class in one expression. A lambda is a shorter way to write the same thing when the interface has one abstract method. Use a named class when the implementation is long, needs fields or is reused; use a lambda for short, one-off behaviour. Interfaces that represent behaviour as data are the subject of [t16 Functional Interfaces](../t16_functional_interfaces/t16_functional_interfaces_notes.md).

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
    @Override
    public void move() {
        System.out.println("Player walks.");
    }
}

public class Enemy implements Moveable {
    @Override
    public void move() {
        System.out.println("Enemy shuffles.");
    }
}
```

### Step C — Use polymorphically

```java
Moveable bat = () -> System.out.println("Bat flaps.");
List<Moveable> movers = List.of(new Player(), new Enemy(), bat);

for (Moveable m : movers) {
    m.move();
}
```

#### Snippet explanation

The calling code only cares that each object can move — not how it is implemented. `Moveable` has one abstract method, so the third element can be a lambda instead of a named class.

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

#### Snippet explanation

Calling a method on a `null` reference throws `NullPointerException`. This pattern checks first. Where you control the code that creates the objects, prefer rejecting `null` at the point it enters, for example in a constructor, so that `null` never reaches a loop like this.

---

## Games example — Interactable world objects with default methods

An earlier version of this example put `int DEFAULT_INTERACTION_RANGE = 2;` in the interface. That gave every object the same fixed range. A default method keeps the shared default and lets individual objects change it.

```java
public interface Interactable {
    void interact();

    default int interactionRange() {
        return 2;
    }

    default boolean canInteract(int distanceToPlayer) {
        return distanceToPlayer >= 0 && distanceToPlayer <= interactionRange();
    }
}

public class Chest implements Interactable {
    @Override
    public void interact() {
        System.out.println("Chest opens. Loot spills out.");
    }
}

public class Npc implements Interactable {
    @Override
    public void interact() {
        System.out.println("NPC: Hello, traveller!");
    }

    @Override
    public int interactionRange() {
        return 4;
    }
}
```

**Example usage:**

```java
for (Interactable target : nearby) {
    if (target.canInteract(distanceTo(target))) {
        target.interact();
    }
}
```

### Explanation

The game loop asks each object whether it can be reached, and never needs to know the rule for a particular type. A new object with a longer range, such as a lever you can shoot, overrides one method and the loop does not change. You practise this in exercise 02.

---

## Software development example — A logger with an enum and default methods

```java
public enum LogLevel { INFO, WARNING, ERROR }

public interface Logger {
    // The one method every logger must write
    void log(LogLevel level, String message);

    // Convenience methods for callers, built on log
    default void info(String message) {
        log(LogLevel.INFO, message);
    }

    default void error(String message) {
        log(LogLevel.ERROR, message);
    }
}

public class ConsoleLogger implements Logger {
    @Override
    public void log(LogLevel level, String message) {
        System.out.println("[" + level + "] " + message);
    }
}

public class MemoryLogger implements Logger {
    private final List<String> lines = new ArrayList<>();

    @Override
    public void log(LogLevel level, String message) {
        lines.add(level + ": " + message);
    }

    public List<String> lines() {
        return Collections.unmodifiableList(lines);
    }
}
```

### Explanation

Implementers write only `log`; callers get the shorter `info` and `error` for free. The level is an `enum`, not a set of `String` constants in the interface, so `log("OKAY", ...)` does not compile. `MemoryLogger` stores lines instead of printing them, which makes it a useful **test double**: a test can pass it to the code under test and then check `lines()`, with no console output to capture.

---

## Common mistakes

| Mistake | What happens | Fix |
| :- | :- | :- |
| Declaring variables with the concrete class | Code is tied to one implementation, so swapping it means editing every use | Declare `List<String> names`, not `ArrayList<String> names` |
| An interface with many unrelated methods | Classes must implement methods they do not need | Split it into small interfaces, one per ability |
| An interface with one implementation and no prospect of another | Extra files with no benefit | Add the interface when a second implementation or a test needs it |
| Casting back to the concrete class | The flexibility the interface gave you is lost, and the cast can fail at run time | Add the method you need to the interface, or use a second interface |

---

## Debugging and pitfalls

| Mistake | Why it happens | Fix |
| :-- | :-- | :-- |
| Trying to put instance fields in an interface | Confusion with abstract classes | Use an abstract class if shared state is needed |
| Forgetting to implement all interface methods | The class cannot compile | Implement all abstract methods, or mark the class `abstract` |
| Trying to instantiate an interface | Interfaces have no constructors | Instantiate a class that implements it, or use a lambda for a functional interface |
| Using an interface as a "constants dump" | Constants look shared, so the interface seems a natural home | Use an `enum` for a fixed set of choices or a `final` class for settings |
| Missing `@Override` on an implementing method | A typo creates a new method instead of implementing one | Always write `@Override` |
| Implementing method not `public` | Interface methods are implicitly `public` | Declare the implementation `public` |
| Two interfaces give the same default method | Java cannot choose between them | Override it and call `A.super.method()` |
| Calling a static interface method on an object | Static interface methods are not inherited | Call it on the interface name: `Shape.unitSquare()` |
| Mixing "is-a" and "can-do" thinking | Misunderstanding the purpose | Interface = ability; Class = identity + data |

---

## Practice tasks

1. Write an interface `Scorer` with `int score(String word)`. Implement it twice, then sort a list of words by each scorer.
2. Add a default method to an interface you have already implemented. Do the existing classes still compile? Why does that matter for a library?
3. Rewrite one of your `Scorer` classes as a lambda. What stops you doing the same for an interface with two abstract methods?

For step-by-step practice with worked solutions, see the [t09 exercises](exercises/t09_interface_exercises.md).

---

## Reflective questions

- When is an interface better than an abstract class?
- In your last assignment, which behaviours would make good interfaces?
- Why is putting constants in an interface usually a poor choice, and what would you use instead?
- Can you name a behaviour in your game engine project that deserves a shared interface?
- You add a new abstract method to an interface that ten classes already implement. What happens, and how would a default method change that?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**When an interface beats an abstract class.**
Choose an **interface** when you are describing a *capability* that unrelated types can have. `Drawable`, `Comparable`, `Runnable`, `AutoCloseable` — a `Player`, a `Menu` and a `Particle` share no ancestry but can all be drawn. Two decisive advantages: a class may implement **many** interfaces but extend only **one** class, and an interface imposes no state, so it never constrains how implementers store their data.

Choose an **abstract class** when subclasses share **state and implementation**, not just a contract — common fields, a constructor that validates them, and a template method that calls one abstract step. `Entity` with `id`, `position` and a concrete `move()` that calls abstract `speed()` is an abstract class, because that shared field and logic would have to be duplicated in every implementer otherwise. Default methods narrow the gap — an interface can now share *behaviour* — but it still cannot share *fields*.

Rule of thumb: **is-a with shared state then abstract class; can-do then interface.** In practice both appear together — an abstract base for the family, plus interfaces for capabilities that cut across families.

**Which behaviours make good interfaces.**
A good answer picks behaviours that appear in **more than one otherwise-unrelated class** and can be described in one or two methods. Strong candidates: `Persistable` (`save`/`load`), `Validatable` (`validate`), `Damageable` (`takeDamage`), `Serialisable`, a `Repository<T>` contract. Weak candidates: an interface with one implementer and no prospect of a second (it adds a file and buys nothing today), or a "kitchen-sink" interface with twelve unrelated methods that forces implementers to stub half of them.

Keep interfaces **small**. If implementers routinely leave methods empty or throwing, split it — several focused interfaces beat one broad one, and a class can implement as many as it needs.

**Why constants in an interface are usually a poor choice.**
Fields in an interface are implicitly `public static final`, so it is tempting to use an interface as a home for shared values. But an interface is a type that says what implementers *can do*. A class that implements a constants interface to save typing claims a capability it does not have, and that claim leaks into its public type and into every subclass, permanently.

Declaring a value **once** is still the right goal; it just belongs elsewhere:

- A fixed set of related choices, such as log levels or request types, belongs in an **`enum`**. The compiler then rejects any value outside the set; `String` constants would still allow `"OKAY"`. That is the exact failure that makes a protocol-string bug hard to find in [t22 Networking](../t22_networking/t22_networking_notes.md).
- Shared numeric settings belong in a **`final` class with a `private` constructor**, used as `Physics.GRAVITY`.
- A default that some implementers change belongs in a **default method**.

**A behaviour in your project deserving a shared interface.**
Look for a method you have written more than once in classes with no common parent, or an `if`/`instanceof` chain switching on type. Both are the same smell. Typical finds: `Updatable` with `update(double deltaTime)` implemented by every entity, particle and animation, so the game loop can hold one `List<Updatable>` and call `update` polymorphically; or `Collidable` with `getBounds()`. The payoff is concrete — the game loop stops knowing which concrete types exist, and adding a new entity type requires no change to the loop at all.

**Adding a method to an interface with ten implementers.**
A new **abstract** method breaks all ten classes: none of them compiles until each one implements it. A **default** method compiles straight away, because every class inherits the body; classes that need something different override it. This is why the Java library could add `forEach` and `removeIf` to `Collection` without breaking every collection ever written. The trade-off: a default must be correct for every implementer, or it hides a bug where a class should have overridden it.

</details>

---

## Further reading

- **Oracle Java Tutorials: Interfaces**
  <https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html>
  Defining and implementing interfaces, including default methods.

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
