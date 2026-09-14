---
title: "Inheritance — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t08_inheritance
description: "Six inheritance exercises: is-a versus has-a, constructor chaining with super, abstract classes, polymorphic collections, and an override versus overload bug hunt."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, inheritance, overriding, polymorphism, exercises, year2, comp-c8z03]
---
# Inheritance — Exercises

> These exercises build directly on the **Inheritance** notes and follow the topic **Equality &
> Hashing** in the sequence. Work from top to bottom.
>
> Most tasks include small, checkable outputs. Use the provided method signatures and add tiny
> tests in `main` or `Exercise.run()`.
>
> Hints are included where you may commonly get stuck (constructor chaining, `super`, and
> polymorphic lists).

---

## How to run

Each exercise assumes a package like:

```java
t08_inheritance.exercises.exNN
```

Create a class `Exercise` in that package with a static entry point:

```java
package t08_inheritance.exercises.ex01;

public final class Exercise {
    public static void run() {
        // call your methods here; print results for quick checks
    }
}
```

Call `Exercise.run()` from your `Main`, as shown in the repo README.

---

## Exercise 01 — “Is-a” or “Has-a”? (warm-up, conceptual)

**Objective:** Decide when inheritance is appropriate.
**Description:** For each pair below, decide whether the relationship is a good candidate for
inheritance (`is-a`) or is better expressed with **composition** (`has-a`). Then, pick **one**
good inheritance example and sketch a tiny class skeleton.

Pairs:

1. `Player` and `Entity`
2. `Customer` and `Order`
3. `Sword` and `Weapon`
4. `Team` and `Player`
5. `AdminUser` and `User`

- Tasks:
  - Label each pair as **“is-a”** or **“has-a”** (1–2 words each).
  - Choose one **“is-a”** pair and write a tiny skeleton **in your Exercise class file**:

    ```java
    class Base { /* ... */ }
    class Sub extends Base { /* ... */ }
    ```

  - Write 2–3 short sentences explaining *why* inheritance is appropriate in your chosen example.

**Hint:**
If you can say “Every X **is a** Y” and it makes sense in plain English, inheritance might be
okay. If it sounds more like “X **has a** Y”, prefer composition.

---

## Exercise 02 — First Hierarchy: Entity + Player + Enemy (Foundations)

**Objective:** Implement a basic inheritance hierarchy and method overriding.
**Description:** Implement three classes in the same package as `Exercise`:

- `Entity` — base class with a `name` and a `describe()` method.
- `Player` — subclass that adds `score` and overrides `describe()`.
- `Enemy` — subclass that overrides `describe()`.

**Requirements:**

1. Implement:

   ```java
   class Entity {
       private final String name;

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
   ```

2. Implement:

   ```java
   class Player extends Entity {
       private int score;

       public Player(String name, int score) {
           super(name);
           this.score = score;
       }

       @Override
       public String describe() {
           return "Player " + name() + " (score=" + score + ")";
       }
   }
   ```

3. Implement:

   ```java
   class Enemy extends Entity {

       public Enemy(String name) {
           super(name);
       }

       @Override
       public String describe() {
           return "Enemy: " + name();
       }
   }
   ```

4. In `Exercise.run()`:

   ```java
   public static void run() {
       Entity e = new Entity("Rock");
       Player p = new Player("Zara", 100);
       Enemy g = new Enemy("Goblin");

       System.out.println(e.describe());
       System.out.println(p.describe());
       System.out.println(g.describe());
   }
   ```

**Quick check:**
You see three lines describing a generic entity, a player with a score, and an enemy with a name.

**Hint:**
Make sure you add `@Override` on the subclass `describe()` methods to let the compiler catch
mistakes.

---

## Exercise 03 — Constructor Chaining & `super` in Methods (Intermediate foundations)

**Objective:** Use `super(...)` in constructors and methods to extend behaviour.
**Description:** Add a new subclass `BossEnemy` that reuses `Enemy` logic but adds a `level`
field and more detailed description.

**Requirements:**

1. Add:

   ```java
   class BossEnemy extends Enemy {
       private final int level;

       public BossEnemy(String name, int level) {
           super(name);   // chain to Enemy(String) → Entity(String)
           this.level = level;
       }

       @Override
       public String describe() {
           String base = super.describe(); // Enemy.describe()
           return base + " [Boss level " + level + "]";
       }
   }
   ```

2. Update `Exercise.run()` to include:

   ```java
   BossEnemy dragon = new BossEnemy("Dragon", 5);
   System.out.println(dragon.describe());
   ```

3. In a short comment in `Exercise.run()`, explain in 1–2 sentences:
   - What `super(name)` does in the constructor chain.
   - Why calling `super.describe()` is useful here instead of rebuilding the whole string.

**Quick check:**
You see a line like:
`Enemy: Dragon [Boss level 5]`

**Hint:**
Remember that **parent constructors always run before child constructors**. `super(...)` must
be the first statement in the subclass constructor.

---

## Exercise 04 — Abstract Weapon & Polymorphic List (Balanced domain)

**Objective:** Create an abstract class and use polymorphism in a list.
**Description:** Implement an abstract `Weapon` class and two concrete subclasses. Then, store
them in an `ArrayList<Weapon>` and print descriptions.

**Requirements:**

1. Implement:

   ```java
   abstract class Weapon {
       private final String id;

       public Weapon(String id) {
           this.id = id;
       }

       public String id() {
           return id;
       }

       public abstract int damage();

       public String describe() {
           return id + " (damage=" + damage() + ")";
       }
   }
   ```

2. Add two concrete weapons, e.g.:

   ```java
   class Sword extends Weapon {
       public Sword(String id) {
           super(id);
       }

       @Override
       public int damage() {
           return 25;
       }
   }

   class Staff extends Weapon {
       public Staff(String id) {
           super(id);
       }

       @Override
       public int damage() {
           return 15;
       }
   }
   ```

3. In `Exercise.run()` (you can reuse or copy into a new exercise package, e.g. `ex04`):

   ```java
   java.util.ArrayList<Weapon> weapons = new java.util.ArrayList<>();
   weapons.add(new Sword("IronSword"));
   weapons.add(new Staff("OakStaff"));

   for (Weapon w : weapons) {
       System.out.println(w.describe());
   }
   ```

4. Add a brief comment above the loop explaining in 1–2 lines how **polymorphism** is used here.

**Quick check:**
Both weapons print different damage values but are treated as `Weapon` references in the loop.

**Hint:**
You cannot write `new Weapon("W1")` because `Weapon` is abstract. You can still declare
variables of type `Weapon` and store subclass instances in them.

---

## Exercise 05 — Polymorphic Update Loop (Games-focused)

**Objective:** Use an abstract base class to drive a simple game loop.
**Description:** Model a tiny game “engine” where all entities share an abstract type with
`update` and `render` methods.

**Requirements:**

1. Create an abstract base:

   ```java
   abstract class GameEntity {
       protected final String name;

       public GameEntity(String name) {
           this.name = name;
       }

       public abstract void update(double dt);
       public abstract void render();
   }
   ```

2. Implement two subclasses:

   ```java
   class PlayerEntity extends GameEntity {
       public PlayerEntity(String name) {
           super(name);
       }

       @Override
       public void update(double dt) {
           System.out.println("Updating player " + name + " with dt=" + dt);
       }

       @Override
       public void render() {
           System.out.println("Rendering player " + name);
       }
   }

   class EnemyEntity extends GameEntity {
       public EnemyEntity(String name) {
           super(name);
       }

       @Override
       public void update(double dt) {
           System.out.println("Updating enemy " + name + " with dt=" + dt);
       }

       @Override
       public void render() {
           System.out.println("Rendering enemy " + name);
       }
   }
   ```

3. In `Exercise.run()`:

   ```java
   java.util.ArrayList<GameEntity> entities = new java.util.ArrayList<>();
   entities.add(new PlayerEntity("Hero"));
   entities.add(new EnemyEntity("Slime"));

   double dt = 0.016; // 16 ms frame
   for (GameEntity ge : entities) {
       ge.update(dt);
       ge.render();
   }
   ```

4. Add a short comment explaining why this design makes it easy to add new entity types later
   (e.g. `DoorEntity`, `PickupEntity`).

**Quick check:**
You see an update + render message for each entity, with no `if` / `instanceof` checks in the loop.

**Hint:**
Notice how the loop only knows about the **base type** `GameEntity`, but the actual work is
done by the subclasses.

---

## Exercise 06 — Override vs Overload Bug (Debugging)

**Objective:** Recognise and fix a common overriding mistake.
**Description:** You are given a base class and a subclass that *tries* to override a method
but actually overloads it instead. Fix the bug.

**Given code:**

```java
class BaseFormatter {
    public String format() {
        return "Base format";
    }
}

class FancyFormatter extends BaseFormatter {
    // BUG: this does not override format()
    public String format(String prefix) {
        return prefix + " Fancy format";
    }
}
```

**Tasks:**

1. Fix `FancyFormatter` so that:
   - It **properly overrides** `format()`.
   - It still provides an extra overloaded method that accepts a `prefix`.

2. Add an `Exercise.run()` that shows:

   ```java
   BaseFormatter a = new BaseFormatter();
   BaseFormatter b = new FancyFormatter();

   System.out.println(a.format()); // should print: Base format
   System.out.println(b.format()); // should print something like: Fancy format
   ```

3. Add a comment explaining:
   - Why the original `format(String prefix)` did **not** override `BaseFormatter.format()`.
   - How `@Override` would have helped you spot this bug.

**Hint:**
Method **signature** for overriding must match exactly: same name, same parameter list, same
return type (or covariant).

---
