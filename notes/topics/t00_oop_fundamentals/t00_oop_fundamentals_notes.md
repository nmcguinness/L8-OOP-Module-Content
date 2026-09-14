---
title: "OOP Fundamentals: Classes, Objects & Encapsulation"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t00_oop_fundamentals
description: "Java class anatomy, constructors, fields, methods, access modifiers, and the four pillars of OOP — the conceptual foundation every subsequent topic builds on."
created: 2026-05-27
last_updated: 2026-05-27
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, oop, classes, objects, encapsulation, constructors, access-modifiers, year2, comp-c8z03]
difficulty_tier: Foundation
mlos: [MLO2]
previous_topic: null
prerequisites:
  - Basic programming in any language (variables, conditionals, loops, methods/functions)
---

# OOP Fundamentals: Classes, Objects & Encapsulation

> **Prerequisites:**
> - You can write variables, `if` statements, loops, and functions/methods in at least one language
> - No prior Java or OOP experience is assumed beyond that

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [The four pillars of OOP](#the-four-pillars-of-oop)
- [Part 1: Anatomy of a Java class](#part-1-anatomy-of-a-java-class)
- [Part 2: Access modifiers](#part-2-access-modifiers)
- [Part 3: Creating and using objects](#part-3-creating-and-using-objects)
- [Part 4: Guard clauses (defensive constructors)](#part-4-guard-clauses-defensive-constructors)
- [Part 5: Multiple constructors and `this(...)`](#part-5-multiple-constructors-and-this)
- [Games example: `Player` and `Inventory`](#games-example-player-and-inventory)
- [Software example: `Task` and `TaskList`](#software-example-task-and-tasklist)
- [Common mistakes](#common-mistakes)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Describe what a class is and how it differs from an object. |
| Understand | Name and explain the four pillars of OOP at a high level. |
| Use | Write a Java class with private fields, a constructor, and public methods. |
| Use | Apply access modifiers (`private`, `public`) to fields and methods. |
| Use | Create objects with `new` and call methods on them. |
| Use | Write a basic constructor with argument validation (guard clauses). |
| Debug | Identify `NullPointerException` when a reference is not initialised. |

---

## Why this matters

Every topic in this module builds on one idea: organising code around **objects** that combine
state (data) and behaviour (methods) into a single unit.

Before diving into arrays, collections, inheritance, or design patterns, you need a solid
answer to two questions:

1. **What is a class?** — A blueprint that describes what data an object holds and what it can do.
2. **What is an object?** — A specific instance of a class, created at runtime, with its own
   copy of the data.

Everything else in OOP is about composing, extending, or coordinating objects. Get this right
and the rest follows naturally.

---

## How this builds on previous content

This is the first topic of the module, so it builds on **Year 1 programming** rather than on an
earlier note.

| What you already know | How it reappears here |
| :- | :- |
| Variables and types | Become **fields** — state that belongs to an object and outlives a single method call |
| Methods and parameters | Become the class's **public API** — the only sanctioned way to change that state |
| `if` statements | Become **guard clauses** at the top of a constructor, rejecting invalid objects outright |
| Calling a method on a `String` or `Scanner` | You have been *using* objects all along; here you start **designing** them |

Everything after this topic assumes these ideas. [t01
Arrays](../t01_arrays/t01_arrays_notes.md) stores objects, [t04 Equality &
Hashing](../t04_equality_hashing/t04_equality_hashing_notes.md) decides when two of them count
as the same, and [t08 Inheritance](../t08_inheritance/t08_inheritance_notes.md) relates one
class to another.

---

## The four pillars of OOP

| Pillar | One-line definition | Where you'll see it |
| :-- | :-- | :-- |
| **Encapsulation** | Hide internal state; expose only what is needed | `private` fields + `public` methods; this topic |
| **Abstraction** | Work with *what* something does, not *how* | Abstract classes ([t08](../t08_inheritance/t08_inheritance_notes.md)), interfaces ([t09](../t09_interface/t09_interface_notes.md)) |
| **Inheritance** | A class can extend another, inheriting its behaviour | `extends` ([t08](../t08_inheritance/t08_inheritance_notes.md)) |
| **Polymorphism** | One type reference, many possible implementations | Method overriding ([t08](../t08_inheritance/t08_inheritance_notes.md)), interface types ([t09](../t09_interface/t09_interface_notes.md)) |

This topic covers **encapsulation** in full and introduces the vocabulary needed to understand
the others.

---

## Part 1: Anatomy of a Java class

```java
public class Player {                          // class declaration

    // --- Fields (state) ---
    private String _name;                      // private: only accessible inside this class
    private int _score;

    // --- Constructor ---
    public Player(String name) {               // called when you write: new Player("Alice")
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        _name = name;
        _score = 0;                            // sensible default
    }

    // --- Methods (behaviour) ---
    public String getName() { return _name; }
    public int getScore() { return _score; }

    public void addPoints(int points) {
        if (points < 0) throw new IllegalArgumentException("Points must be non-negative");
        _score += points;
    }

    public void resetScore() {
        _score = 0;
    }

    @Override
    public String toString() {
        return _name + " (score: " + _score + ")";
    }
}
```

Key vocabulary:

| Term | What it means |
| :-- | :-- |
| **Field** | A variable that belongs to the object; holds its state |
| **Constructor** | Special method that runs when `new` is called; initialises fields |
| **Method** | A function that belongs to the object; defines its behaviour |
| **`this`** | A reference to the current object instance |

---

## Part 2: Access modifiers

| Modifier | Visible to... | Use for... |
| :-- | :-- | :-- |
| `private` | This class only | Fields (almost always); internal helpers |
| `public` | Any class | Constructors; methods that form the API |
| `protected` | This class + subclasses + same package | Inheritance hooks (covered in [t08](../t08_inheritance/t08_inheritance_notes.md)) |
| *(none)* | Same package | Rarely used deliberately |

**Rule of thumb:** make fields `private`, make the constructor and useful methods `public`.

```java
public class Counter {
    private int _count;          // hidden — callers cannot directly set or read this

    public Counter() { _count = 0; }
    public void increment() { _count++; }
    public void decrement() { if (_count > 0) _count--; }
    public int getCount() { return _count; }
}
```

Callers never see `_count` directly. They can only use `increment()`, `decrement()`, and
`getCount()`. That means you can change how counting works internally without breaking any
caller — this is encapsulation at work.

---

## Part 3: Creating and using objects

```java
// Create objects with 'new'
Player alice = new Player("Alice");
Player bob = new Player("Bob");

// Each object has its own copy of the fields
alice.addPoints(10);
bob.addPoints(5);

System.out.println(alice);  // Alice (score: 10)
System.out.println(bob);    // Bob (score: 5)

// Call methods
alice.resetScore();
System.out.println(alice.getScore()); // 0

// Object reference vs object
Player ref1 = alice;        // ref1 and alice point to the SAME object
ref1.addPoints(20);
System.out.println(alice.getScore()); // 20 — same object was modified
```

> **NullPointerException:** If you declare a variable but never assign it, it holds `null`.
> Calling any method on `null` crashes at runtime. Always initialise references before use.

```java
Player ghost = null;
ghost.getName();    // NullPointerException — ghost is not pointing at any object
```

---

## Part 4: Guard clauses (defensive constructors)

A **guard clause** is a check at the top of a method or constructor that rejects invalid input
immediately.

```java
public class Task {

    private final String _title;
    private final int _priority;      // 1 (highest) to 5 (lowest)

    public Task(String title, int priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Priority must be 1–5, got: " + priority);
        }
        _title = title.strip();       // normalise whitespace
        _priority = priority;
    }

    public String getTitle() { return _title; }
    public int getPriority() { return _priority; }

    @Override
    public String toString() {
        return "[P" + _priority + "] " + _title;
    }
}
```

Guard clauses mean an object is **always valid** once constructed. You never need to check
inside methods whether the state makes sense.

---

## Part 5: Multiple constructors and `this(...)`

A class can have more than one constructor — each with a different parameter list.

```java
public class Item {

    private final String _name;
    private int _quantity;

    // Full constructor
    public Item(String name, int quantity) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        _name = name.strip();
        _quantity = quantity;
    }

    // Convenience constructor — delegates to the full one
    public Item(String name) {
        this(name, 0);      // calls Item(String, int) with default quantity
    }

    public String getName() { return _name; }
    public int getQuantity() { return _quantity; }

    public void add(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be non-negative");
        _quantity += amount;
    }
}
```

`this(name, 0)` calls the other constructor in the same class. This keeps validation in one
place and avoids duplication.

---

## Games example: `Player` and `Inventory`

```java
public class GamePlayer {

    private final String _username;
    private int _health;
    private int _level;

    public GamePlayer(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username required");
        }
        _username = username.strip();
        _health = 100;
        _level = 1;
    }

    public String getUsername() { return _username; }
    public int getHealth() { return _health; }
    public int getLevel() { return _level; }
    public boolean isAlive() { return _health > 0; }

    public void takeDamage(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Damage must be non-negative");
        _health = Math.max(0, _health - amount);
    }

    public void heal(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Heal amount must be non-negative");
        _health = Math.min(100, _health + amount);
    }

    public void levelUp() {
        _level++;
        _health = 100;      // full heal on level-up
    }

    @Override
    public String toString() {
        return _username + " [Lv." + _level + ", HP:" + _health + "]";
    }
}
```

Usage:

```java
GamePlayer hero = new GamePlayer("Riona");
hero.takeDamage(30);
hero.heal(10);
System.out.println(hero);          // Riona [Lv.1, HP:80]
System.out.println(hero.isAlive()); // true

hero.takeDamage(200);
System.out.println(hero.isAlive()); // false — health clamped to 0
```

---

## Software example: `Task` and `TaskList`

```java
public class TodoTask {

    private final String _title;
    private final String _category;
    private boolean _done;

    public TodoTask(String title, String category) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title required");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("Category required");
        _title = title.strip();
        _category = category.strip();
        _done = false;
    }

    public String getTitle() { return _title; }
    public String getCategory() { return _category; }
    public boolean isDone() { return _done; }
    public void markDone() { _done = true; }

    @Override
    public String toString() {
        return (_done ? "[x] " : "[ ] ") + _title + " (" + _category + ")";
    }
}
```

```java
import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private final List<TodoTask> _tasks = new ArrayList<>();

    public void add(TodoTask task) {
        if (task == null) throw new IllegalArgumentException("Task must not be null");
        _tasks.add(task);
    }

    public int totalCount() { return _tasks.size(); }

    public int doneCount() {
        int count = 0;
        for (TodoTask t : _tasks) if (t.isDone()) count++;
        return count;
    }

    public void printAll() {
        for (TodoTask t : _tasks) System.out.println(t);
    }
}
```

Usage:

```java
TaskList list = new TaskList();
list.add(new TodoTask("Set up database", "Backend"));
list.add(new TodoTask("Write unit tests", "Testing"));
list.add(new TodoTask("Create README", "Docs"));

list.printAll();
// [ ] Set up database (Backend)
// [ ] Write unit tests (Testing)
// [ ] Create README (Docs)

list.add(new TodoTask("Set up database", "Backend")).markDone();  // illustration only
```

---

## Common mistakes

| Pitfall | Symptom | Fix |
| :-- | :-- | :-- |
| `NullPointerException` | Crash at runtime when calling a method on a variable | Initialise every variable before use; guard against null in constructors |
| Public fields | Other classes modify state directly, bypassing any validation | Always `private` fields; expose state through methods |
| No guard clauses | Object constructed with invalid state; method fails later with a cryptic error | Validate in the constructor; throw `IllegalArgumentException` early |
| Mutable `public` field | Callers bypass `addPoints` and set score directly | Use `private final` where possible; use `private` otherwise |
| Forgetting `toString()` | `System.out.println(obj)` prints `Player@1b6d3586` — the memory address | Override `toString()` for readable output |

---

## Reflective questions

1. What is the difference between a **class** and an **object**? Give a concrete example.
2. Why should fields nearly always be `private`? What goes wrong if they are `public`?
3. What is a guard clause and why should it be at the top of a constructor rather than at the
   point where the field is used?
4. If `ref1` and `ref2` both refer to the same object, what happens when you call
   `ref1.setName("Dave")`? What does `ref2.getName()` return?
5. When would you add a second constructor to a class rather than just using the main one?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**1. Class vs object.**
A class is the *description*; an object is one *instance* built from it and holding its own
field values.
`class Player { private String _name; }` is written once. `new Player("Ana")` and `new
Player("Bo")` are two objects with separate `_name` values. Compare it to a form and filled-in
copies of that form.

**2. Why fields are `private`.**
`private` means the class controls every change to its own state, so guard clauses cannot be
bypassed. If `_health` is `public`, any code can write `p._health = -500;` and your validation
never runs. You also lose the freedom to change how the field is stored later — every caller is
already depending on it directly.

**3. Guard clauses at the top.**
Two reasons. First, **fail fast**: the object is never half-built, so it can never exist in an
invalid state. If you validate at point of use instead, a bad object may be stored, copied and
passed around for a long time before anything complains — and by then the stack trace points at
the wrong place. Second, **readability**: all preconditions sit together, so a reader sees the
contract in one glance.

**4. Aliasing.**
`ref2.getName()` returns `"Dave"`. Both variables hold the *same reference*, so there is only
one object; changing it through one name changes what you see through the other. This is the
single most common source of "but I never touched that object" bugs. Two objects that merely
hold equal data would behave differently — that distinction is the whole of [t04 Equality &
Hashing](../t04_equality_hashing/t04_equality_hashing_notes.md).

**5. A second constructor.**
When a sensible default exists and you want to save callers from repeating it — `new
Task(title)` meaning "priority MEDIUM". Keep exactly one constructor that does the real work
and validation, and have the others delegate to it with `this(...)`. If you copy the guard
clauses into both, they *will* drift apart.

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: null
  domain_emphasis: Balanced

this_lesson:
  topic_code: t00_oop_fundamentals
  primary_domain_emphasis: Balanced
  difficulty_tier: Foundation
mlos: [MLO2]
```
