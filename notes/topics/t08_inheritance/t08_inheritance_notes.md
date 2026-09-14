---
title: "Inheritance"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t08_inheritance
description: "A focused introduction to inheritance, method overriding, constructor chaining, abstract classes, and polymorphism in Java."
created: 2025-11-20
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, inheritance, overriding, polymorphism, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO2]
previous_topic: t07_collections_3_set_map
prerequisites:
  - Classes, fields, constructors, methods
  - Encapsulation (private fields + getters/setters)
  - Arrays and ArrayList basics
  - Comparable & Comparator (ordering)
---

# Inheritance

> **Prerequisites:**
> - Understanding of fields, constructors, and methods
> - Experience writing small Java classes
> - Basic use of `ArrayList` and loops
> - Familiarity with `compareTo` / Comparator from earlier lessons

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Core Ideas / Concepts](#core-ideas--concepts)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Useful snippets (guards and helpers)](#useful-snippets-guards-and-helpers)
- [Games Example](#games-example)
- [Software Development Example](#software-development-example)
- [Debugging and pitfalls](#debugging-and-pitfalls)
- [Reflective questions](#reflective-questions)
- [Appendix A — Mermaid diagrams (UML-style)](#appendix-a--mermaid-diagrams-uml-style)

---

## What you'll learn

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Explain what inheritance is and when to use it (and when not to). |
| Use | Create subclasses with `extends` and call parent constructors using `super`. |
| Use | Override methods correctly using `@Override`. |
| Analyze | Distinguish clearly between method overriding and overloading. |
| Analyze | Decide when to use an abstract base class instead of a concrete class. |
| Debug | Identify and fix common inheritance issues (constructor chains, missing overrides, field hiding). |

---

## Why this matters

Real systems — games and software applications — rarely consist of just one kind of object.
You will often have **families of related types** that share some behaviour but differ in details:

- In a **game**, you might have players, enemies, projectiles, pickups, and triggers.
- In **software**, you might have different kinds of users, accounts, documents, or log entries.

Inheritance gives you a way to:

- Put **shared behaviour** in one place (a parent class)
- Create **specialised types** (subclasses) that reuse that behaviour and add their own
- Use **polymorphism**: treat many specific objects as instances of a more general type

Used well, inheritance reduces duplication and makes systems easier to extend.
Used poorly, it creates brittle hierarchies that are hard to change.
This lesson helps you use inheritance **deliberately and safely**.

---

## How this builds on previous content

- From **[Arrays](../t01_arrays/t01_arrays_notes.md)** and **Collections**
  ([t05](../t05_collections_1/t05_collections_1_notes.md),
  [t06](../t06_collections_2/t06_collections_2_notes.md),
  [t07](../t07_collections_3_set_map/t07_collections_3_set_map_notes.md)) you learned to store
  and process objects in lists. Now, you’ll store collections of *different but related*
  objects through a shared parent.
- From **[Ordering (Comparable & Comparator)](../t03_ordering/t03_ordering_notes.md)** you
  already saw specialisation of behaviour (different comparison rules). Inheritance generalises
  that idea to whole classes.
- From **[Equality & Hashing](../t04_equality_hashing/t04_equality_hashing_notes.md)** you
  learned that class structure affects correctness. Here, you’ll see how subclassing impacts
  behaviour and contracts.

---

## Core Ideas / Concepts

> For each core idea, we first explain the concept, then show a short code example, followed by
> a brief explanation of how the example illustrates the idea.

---

### Core Idea 1 — Basic inheritance with `extends` (the “is-a” relationship)

#### Explanation

Inheritance models an **“is-a”** relationship between classes. A subclass “is a” more specific
version of its parent class and therefore:

- Inherits its fields and methods.
- Can add new fields and methods.
- Can override inherited methods to change behaviour.

You should use inheritance when there is a clear, stable “is-a” relationship — e.g. a `Player`
**is an** `Entity`, or an `AdminUser` **is a** `User`.

```java
class Entity {
    private String name;

    public Entity(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public String describe() {
        return "Entity: " + name;
    }
}

class Player extends Entity {
    private int score;

    public Player(String name, int score) {
        super(name);           // call Entity(String) constructor
        this.score = score;
    }

    @Override
    public String describe() {
        return "Player " + name() + " (score=" + score + ")";
    }
}
```

#### Snippet explanation

This snippet introduces a simple inheritance hierarchy. `Entity` is the general base class that
stores a name and can describe itself. `Player` extends `Entity`, reuses the name handling via
`super(name)` and `name()`, and overrides `describe()` to print a more specific message that
includes the player’s score.

---

### Core Idea 2 — Method overriding vs method overloading

#### Explanation

Two similar-sounding concepts often cause confusion:

- **Overriding**: A subclass provides a **new implementation** of a method with the **same
  signature** as in the parent. This is central to polymorphism.
- **Overloading**: A class provides **multiple methods with the same name** but **different
  parameter lists**. This is about convenience, not polymorphism.

Overriding changes behaviour for inherited methods. Overloading just adds more ways to call a
method.

```java
class Enemy extends Entity {

    public Enemy(String name) {
        super(name);
    }

    // Overriding: same name, same parameters, different body
    @Override
    public String describe() {
        return "Enemy: " + name();
    }

    // Overloading: same name "move", different parameter lists
    public void move(int dx, int dy) {
        // move by offsets
    }

    public void move(java.awt.Point p) {
        // move to absolute position
    }
}
```

#### Snippet explanation

Here `Enemy` overrides `describe()` to change how an `Entity` is described when it is
specifically an enemy. The two `move` methods illustrate overloading: they share the same name
but accept different parameter types. Overriding (`describe`) is what enables polymorphism;
overloading (`move`) is just a convenience for callers.

---

### Core Idea 3 — Using `super` to extend parent behaviour

#### Explanation

Sometimes you don’t want to completely replace parent behaviour; you want to **build on it**.
The `super` keyword lets you:

- Call the parent constructor.
- Call the parent version of an overridden method.

This is useful when the parent method already does something important (e.g. logging, reuse of
formatting) and the subclass wants to add its own extra details.

```java
class BossEnemy extends Enemy {
    private int level;

    public BossEnemy(String name, int level) {
        super(name);                  // calls Enemy(String) → Entity(String)
        this.level = level;
    }

    @Override
    public String describe() {
        String base = super.describe();   // Enemy.describe()
        return base + " [Boss Level " + level + "]";
    }
}
```

#### Snippet explanation

`BossEnemy` reuses the constructor work already done by `Enemy` (and `Entity`) by calling
`super(name)`. In `describe()`, it first calls `super.describe()` to get the enemy description,
then appends boss-specific information. This shows how `super` can be used to extend inherited
behaviour without duplicating it.

---

### Core Idea 4 — Abstract classes as partially implemented templates

#### Explanation

An **abstract class** acts as a **template** for a set of related types. It lets you:

- Put shared fields and concrete methods in one place.
- Declare **abstract methods** that subclasses must implement.

Use an abstract class when:

- There is a strong “is-a” relationship between subclasses and the base type.
- You want to share implementation details (fields, helper methods).
- You want to enforce that all subclasses implement certain behaviours.

```java
abstract class Weapon {
    private final String id;

    public Weapon(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    // Abstract: subclasses MUST provide an implementation
    public abstract int damage();

    // Concrete: shared behaviour using the abstract method
    public String describe() {
        return id + " (damage=" + damage() + ")";
    }
}

class Sword extends Weapon {

    public Sword(String id) {
        super(id);
    }

    @Override
    public int damage() {
        return 25;
    }
}
```

#### Snippet explanation

`Weapon` defines the shared state (`id`) and a concrete `describe()` method, but leaves
`damage()` abstract so each weapon type can decide its own damage. `Sword` is a concrete
subclass that provides the `damage()` implementation. When you call `describe()` on a `Weapon`
reference, the actual damage value comes from the subclass.

---

### Core Idea 5 — Polymorphism via parent types

#### Explanation

Polymorphism lets you treat different subclasses **through their common parent type**.
You can write code that deals with general types (`Entity`, `Weapon`) but actually operates on
more specific objects (`Player`, `Enemy`, `Sword`).

This is especially useful when you have:

- Collections of mixed objects that share a base type.
- Generic methods that should work for all subclasses.
- Systems like update loops, rendering, or exporting that operate by calling a single method on
  many types.

```java
java.util.ArrayList<Entity> entities = new java.util.ArrayList<>();
entities.add(new Player("Zara", 100));
entities.add(new Enemy("Goblin"));
entities.add(new BossEnemy("Dragon", 5));

for (Entity e : entities) {
    System.out.println(e.describe());
}
```

#### Snippet explanation

The `entities` list is typed as `ArrayList<Entity>` but stores different concrete types
(`Player`, `Enemy`, `BossEnemy`). Inside the loop, calling `e.describe()` triggers the correct
overridden method based on the actual object type, not the reference type. This is core
inheritance-based polymorphism.

---

## Progressive coding steps (A then B then C)

### Step A — Creating a simple subclass

```java
Entity e = new Entity("Object");
Player p = new Player("Zara", 100);

System.out.println(e.describe());   // Entity: Object
System.out.println(p.describe());   // Player Zara (score=100)
```

#### Snippet explanation

This small demo contrasts the base type (`Entity`) with a subclass (`Player`). Both objects
support `describe()`, but the subclass version prints more detailed, player-specific
information.

---

### Step B — Using abstract base classes

```java
Weapon sword = new Sword("IronBlade");
System.out.println(sword.describe());   // IronBlade (damage=25)
```

#### Snippet explanation

Here we use the abstract type `Weapon` as the reference, but the actual object is a `Sword`.
Calling `describe()` goes through the abstract class method, which in turn calls the overridden
`damage()` in `Sword`. This shows that abstract classes can still be used as polymorphic types.

---

### Step C — Polymorphic collections of entities

```java
java.util.ArrayList<Entity> world = new java.util.ArrayList<>();
world.add(new Player("Zara", 100));
world.add(new Enemy("Goblin"));
world.add(new BossEnemy("Dragon", 5));

for (Entity entity : world) {
    System.out.println(entity.describe());
}
```

#### Snippet explanation

The `world` list holds different kinds of entities. The loop does not need to know which
specific subclass each item is; it simply calls `describe()`, relying on polymorphism to pick
the right implementation at runtime.

---

## Useful snippets (guards and helpers)

```java
// Defensive constructor in a base class
public Entity(String name) {
    if (name == null || name.isBlank())
        throw new IllegalArgumentException("name must not be blank");
    this.name = name.trim();
}
```

### Snippet explanation

This constructor guards against invalid names. It ensures that any subclass that calls
`super(name)` cannot create an `Entity` with a null or blank name. This kind of defensive logic
is often placed in base classes to keep all subclasses safe.

```java
// Null-safe helper for describing entities
public static void safeDescribe(Entity e) {
    if (e != null)
        System.out.println(e.describe());
}
```

### Snippet explanation

`safeDescribe` prevents a potential `NullPointerException` by checking for `null` before
calling `describe()`. It’s a small utility that makes code using `Entity` objects more robust,
especially when references may be missing.

---

## Games Example

Imagine a simple 2D game with:

- `Player`
- `Enemy`
- `BossEnemy`

All of them can be treated as `GameEntity` in the update loop.

```java
abstract class GameEntity {
    protected String name;

    public GameEntity(String name) {
        this.name = name;
    }

    public abstract void update(double dt);
    public abstract void render();
}

class PlayerEntity extends GameEntity {
    public PlayerEntity(String name) {
        super(name);
    }

    @Override
    public void update(double dt) {
        // handle input, movement
    }

    @Override
    public void render() {
        // draw player sprite
    }
}

class EnemyEntity extends GameEntity {
    public EnemyEntity(String name) {
        super(name);
    }

    @Override
    public void update(double dt) {
        // simple AI
    }

    @Override
    public void render() {
        // draw enemy sprite
    }
}

java.util.ArrayList<GameEntity> gameEntities = new java.util.ArrayList<>();
gameEntities.add(new PlayerEntity("Hero"));
gameEntities.add(new EnemyEntity("Slime"));

for (GameEntity ge : gameEntities) {
    ge.update(0.016);
    ge.render();
}
```

### Snippet explanation

`GameEntity` is an abstract base class that requires `update` and `render` methods.
`PlayerEntity` and `EnemyEntity` provide concrete versions of these methods. The `gameEntities`
list holds both types, and the game loop treats them uniformly — calling `update` and `render`
on each without caring which subclass they are.

---

## Software Development Example

Consider a logging system that writes different types of log entries:

```java
abstract class LogEntry {
    private final long timestamp;

    public LogEntry() {
        this.timestamp = System.currentTimeMillis();
    }

    public long timestamp() {
        return timestamp;
    }

    public abstract String format();
}

class InfoLogEntry extends LogEntry {
    private final String message;

    public InfoLogEntry(String message) {
        this.message = message;
    }

    @Override
    public String format() {
        return "[INFO] " + timestamp() + " - " + message;
    }
}

class ErrorLogEntry extends LogEntry {
    private final String message;

    public ErrorLogEntry(String message) {
        this.message = message;
    }

    @Override
    public String format() {
        return "[ERROR] " + timestamp() + " - " + message;
    }
}

public static void writeAll(java.util.List<LogEntry> logs) {
    for (LogEntry log : logs) {
        System.out.println(log.format());
    }
}
```

### Snippet explanation

`LogEntry` provides shared behaviour (timestamping) and defines an abstract `format()` method.
`InfoLogEntry` and `ErrorLogEntry` specialise the formatting for info vs error messages. The
`writeAll` method uses the base type `LogEntry` and can print any subclass without needing
`if`-`else` chains.

---

## Debugging and pitfalls

| Pitfall | Why it happens | How to fix / avoid |
| :-- | :-- | :-- |
| Missing `@Override` | Method signature doesn’t match parent method, so it *doesn’t* override | Always use `@Override` on overridden methods to let the compiler catch mistakes. |
| Field hiding (`int x` in parent and child) | Subclass redeclares a field with same name; parent field still exists but is hidden | Don’t reuse field names; use getters/setters or `protected` fields if access is needed. |
| Forgetting `super(...)` | Parent constructor with parameters is never called | Ensure subclass constructors call `super(...)` **first** when the parent has no no-arg constructor. |
| Overusing inheritance | Classes are not truly in an “is-a” relationship | Prefer composition (e.g. `has-a` relationship) or interfaces for shared behaviour without deep hierarchies. |
| Confusion about reference vs object type | Expecting subclass-only methods on a parent reference | Remember: reference type controls which methods you can call; object type controls which implementation runs. |

---

## Reflective questions

- When is inheritance the right tool, and when might composition or interfaces be a better choice?
- Think of an example from your own code or a game you enjoy — what might the inheritance
  hierarchy look like there?
- How would you refactor a deeply nested (3–4 levels) inheritance chain to make it easier to
  maintain?
- If you introduced a new subclass and something stopped working as expected, what would be
  your first three debugging steps?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**When inheritance is the right tool.**
Apply the **is-a** test, and be strict about it: `Dog` *is an* `Animal`, so inheritance fits.
`Car` *has an* `Engine` — that is composition, and modelling it as `Car extends Engine` is the
classic beginner error.

Use inheritance when subclasses genuinely share **state and implementation** and the
relationship is permanent. Prefer **interfaces** when you only need to share a *contract* —
many unrelated classes can be `Drawable` without sharing any fields, and a class can implement
several interfaces but extend only one class. Prefer **composition** when behaviour should be
swappable at runtime; `Player` holding a `WeaponBehaviour` field lets the weapon change
mid-game, which `extends` can never do. That is the Strategy pattern in
[t13](../t13_design_patterns_1/t13_design_patterns_1_notes.md).

The blunt heuristic: inheritance couples subclass to superclass permanently and at compile
time. Take that cost only for a real is-a relationship where shared implementation is the
point.

**Sketching a hierarchy.**
A good answer names a *shared abstraction* with real common state, not a category label. For a
game: `Entity` (id, position, health) then `Character` (inventory) then `Player` / `Enemy`. The
test at each level is whether the parent holds fields and behaviour that *every* child
genuinely needs. If a subclass inherits a field it never uses, or has to override a method to
throw `UnsupportedOperationException`, the abstraction is wrong — that override is the loudest
possible signal that the child is not really a kind of the parent.

**Refactoring a 3–4 level chain.**
Deep chains hurt because behaviour is smeared across four files and changing level 2 can break
level 4 invisibly. Practical moves, cheapest first:

1. **Flatten** — if a middle class adds almost nothing, delete it and merge its members upward.
2. **Replace a level with composition** — turn the varying behaviour into an injected object
   (`Strategy`) instead of a subclass. This usually collapses several sibling classes into one
   class plus several small strategies.
3. **Extract an interface** — if a level exists only to promise a capability, make it an
   interface and let classes implement it independently.
4. **Favour a shallow, wide hierarchy** — one abstract base and many direct children beats a
   four-deep chain almost every time.

**First three debugging steps for a misbehaving new subclass.**

1. **Check the override is real.** Add `@Override` to the method. If it does not compile, you
   have *overloaded* rather than overridden — a different parameter list, so the parent version
   keeps running. This is the single most common cause and the annotation finds it instantly.
2. **Check the constructor chain.** Does the subclass call `super(...)` with the right
   arguments? A parent field left at its default (`null`, `0`) because the subclass silently
   invoked a no-arg parent constructor produces a `NullPointerException` far from the real
   cause.
3. **Check what type is actually running.** Print `obj.getClass().getSimpleName()` or
   breakpoint the method. Confirm the object really is the new subclass, and that the parent
   method you expected to be reused is not being shadowed by a **field** — fields are resolved
   by the *declared* type, not the runtime type, and unlike methods they are not polymorphic.
   That asymmetry catches people out.

</details>

---

## Appendix A — Mermaid diagrams (UML-style)

```mermaid
classDiagram
    accTitle: Entity inheritance hierarchy
    accDescr: Entity is the base class holding a name and a describe method. Player and Enemy each extend Entity, add their own fields, and override describe with their own implementation.
    class Entity {
        -String name
        +Entity(String)
        +String name()
        +String describe()
    }

    class Player {
        -int score
        +Player(String, int)
        +String describe()
    }

    class Enemy {
        +Enemy(String)
        +String describe()
    }

    class BossEnemy {
        -int level
        +BossEnemy(String, int)
        +String describe()
    }

    Entity <|-- Player
    Entity <|-- Enemy
    Entity <|-- BossEnemy
```

**Diagram description**
`Entity` is the base class, holding a private `name` field, a constructor, a `name()`
accessor and a `describe()` method. `Player` and `Enemy` both extend `Entity`. `Player`
adds a `score` field and `Enemy` adds a `damage` field, and each overrides `describe()`
with its own implementation.

```mermaid
classDiagram
    accTitle: LogEntry abstract class hierarchy
    accDescr: LogEntry is an abstract base class with a timestamp and an abstract format method. InfoLogEntry and ErrorLogEntry extend it and each supply a concrete format implementation.
    class LogEntry {
        <<abstract>>
        -long timestamp
        +LogEntry()
        +long timestamp()
        +String format()*
    }

    class InfoLogEntry {
        -String message
        +InfoLogEntry(String)
        +String format()
    }

    class ErrorLogEntry {
        -String message
        +ErrorLogEntry(String)
        +String format()
    }

    LogEntry <|-- InfoLogEntry
    LogEntry <|-- ErrorLogEntry
```

**Diagram description**
`LogEntry` is an **abstract** base class holding a `timestamp` and declaring `format()`
as abstract, so it cannot be instantiated directly. `InfoLogEntry` and `ErrorLogEntry`
both extend it, each adding its own fields and supplying a concrete `format()`.

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t07_collections_3_set_map
  domain_emphasis: Balanced

this_lesson:
  topic_code: t08_inheritance
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO2]
```
