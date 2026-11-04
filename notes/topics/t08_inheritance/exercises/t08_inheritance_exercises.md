---
title: "Inheritance — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t08_inheritance
description: "Nine inheritance exercises: is-a versus has-a, constructor chaining with super, abstract classes, polymorphic collections, an override versus overload bug hunt, sealed classes with instanceof, composition, and equals across a hierarchy."
created: 2026-05-29
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, inheritance, overriding, polymorphism, exercises, year2, comp-c8z03]
---
# Inheritance — Exercises

> These exercises build directly on the **Inheritance** notes and follow **Collections III** in the sequence. Exercise 09 also draws on **Equality & Hashing**. Work from top to bottom.
>
> Most tasks include small, checkable outputs. Use the provided method signatures and add tiny tests in `main` or `Exercise.run()`.
>
> Hints are included where you may commonly get stuck (constructor chaining, `super`, and polymorphic lists).

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
**Description:** For each pair below, decide whether the relationship is a good candidate for inheritance (`is-a`) or is better expressed with **composition** (`has-a`). Then, pick **one** good inheritance example and sketch a tiny class skeleton.

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
If you can say “Every X **is a** Y” and it makes sense in plain English, inheritance might be okay. If it sounds more like “X **has a** Y”, prefer composition.

**Solution:** [t08 exercise 01 solution code](../../../../code/src/t08_inheritance/exercises/ex01/).
**Tests:** [t08 exercise 01 tests](../../../../code/test/t08_inheritance/exercises/ex01/).

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
Make sure you add `@Override` on the subclass `describe()` methods to let the compiler catch mistakes.

**Solution:** [t08 exercise 02 solution code](../../../../code/src/t08_inheritance/exercises/ex02/).
**Tests:** [t08 exercise 02 tests](../../../../code/test/t08_inheritance/exercises/ex02/).

---

## Exercise 03 — Constructor Chaining & `super` in Methods (Intermediate foundations)

**Objective:** Use `super(...)` in constructors and methods to extend behaviour.
**Description:** Add a new subclass `BossEnemy` that reuses `Enemy` logic but adds a `level` field and more detailed description.

**Requirements:**

1. Add:

   ```java
   class BossEnemy extends Enemy {
       private final int level;

       public BossEnemy(String name, int level) {
           // chain to Enemy(String) then Entity(String)
           super(name);
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
You see a line like: `Enemy: Dragon [Boss level 5]`

**Hint:**
Remember that **parent constructors always run before child constructors**. `super(...)` must be the first statement in the subclass constructor.

**Solution:** [t08 exercise 03 solution code](../../../../code/src/t08_inheritance/exercises/ex03/).
**Tests:** [t08 exercise 03 tests](../../../../code/test/t08_inheritance/exercises/ex03/).

---

## Exercise 04 — Abstract Weapon & Polymorphic List (Balanced domain)

**Objective:** Create an abstract class and use polymorphism in a list.
**Description:** Implement an abstract `Weapon` class and two concrete subclasses. Then, store them in an `ArrayList<Weapon>` and print descriptions.

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
You cannot write `new Weapon("W1")` because `Weapon` is abstract. You can still declare variables of type `Weapon` and store subclass instances in them.

**Solution:** [t08 exercise 04 solution code](../../../../code/src/t08_inheritance/exercises/ex04/).
**Tests:** [t08 exercise 04 tests](../../../../code/test/t08_inheritance/exercises/ex04/).

---

## Exercise 05 — Polymorphic Update Loop (Games-focused)

**Objective:** Use an abstract base class to drive a simple game loop.
**Description:** Model a tiny game “engine” where all entities share an abstract type with `update` and `render` methods.

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

4. Add a short comment explaining why this design makes it easy to add new entity types later (e.g. `DoorEntity`, `PickupEntity`).

**Quick check:**
You see an update + render message for each entity, with no `if` / `instanceof` checks in the loop.

**Hint:**
Notice how the loop only knows about the **base type** `GameEntity`, but the actual work is done by the subclasses.

**Solution:** [t08 exercise 05 solution code](../../../../code/src/t08_inheritance/exercises/ex05/).
**Tests:** [t08 exercise 05 tests](../../../../code/test/t08_inheritance/exercises/ex05/).

---

## Exercise 06 — Override vs Overload Bug (Debugging)

**Objective:** Recognise and fix a common overriding mistake.
**Description:** You are given a base class and a subclass that *tries* to override a method but actually overloads it instead. Fix the bug.

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
Method **signature** for overriding must match exactly: same name, same parameter list, same return type (or covariant).

**Solution:** [t08 exercise 06 solution code](../../../../code/src/t08_inheritance/exercises/ex06/).
**Tests:** [t08 exercise 06 tests](../../../../code/test/t08_inheritance/exercises/ex06/).

---

## Exercise 07 — Sealed Shapes and `instanceof` (Intermediate)

**Objective:** Close a hierarchy with `sealed`, and use pattern matching for a subtype-only feature.

**Requirements:**

1. Declare `public sealed abstract class Shape permits Circle, Square, Rectangle` with an abstract method `double area()`.
2. Make `Circle` and `Square` `final`. Give `Circle` a `radius()` accessor.
3. Make `Rectangle` `non-sealed`, with a width and a height.
4. Every constructor throws `IllegalArgumentException` for a size of zero or less.
5. Write two static methods in `Exercise`:

   ```java
   public static double totalArea(List<Shape> shapes)
   public static double largestRadius(List<Shape> shapes)
   ```

   `totalArea` must not use `instanceof`. `largestRadius` returns the largest circle radius, or `0` if there are no circles, and must use `instanceof Circle c`.

**Tasks:**

- Try adding `class Triangle extends Shape` in the same package. Write down the compiler error and explain it.
- Explain in one sentence why `Rectangle` has to say `final`, `sealed` or `non-sealed`.

**Quick check:**
For `[Circle(1), Square(2), Circle(3), Rectangle(2, 5)]`, `largestRadius` returns `3.0` and `totalArea` returns about `45.42`.

**Hint:**
`if (s instanceof Circle c && c.radius() > largest)` tests, casts and compares in one line.

**Package:** `t08_inheritance.exercises.ex07`

**Solution:** [t08 exercise 07 solution code](../../../../code/src/t08_inheritance/exercises/ex07/).
**Tests:** [t08 exercise 07 tests](../../../../code/test/t08_inheritance/exercises/ex07/).

---

## Exercise 08 — Composition: a Hero Has a Weapon (Balanced domain)

**Objective:** Replace "one subclass per combination" with composition and delegation.

**Description:** A designer wants `SwordHero`, `StaffHero`, `BowHero`, and so on. Instead, write one `Hero` class that **has a** `Weapon` and can change it at runtime. Reuse `Weapon`, `Sword` and `Staff` from Exercise 04.

**Required API:**

```java
public Hero(String name, Weapon startingWeapon)
public void equip(Weapon newWeapon)
public int attack()
public Weapon weapon()
public String describe()
```

**Rules:**

- `attack()` returns the current weapon's `damage()`. `Hero` does not extend `Weapon`.
- `describe()` returns `name + " wielding " + weapon.describe()`.
- The constructor and `equip` throw `IllegalArgumentException` for a `null` weapon; the constructor also rejects a blank name.

**Quick check:**

```text
Aria wielding IronBlade (damage=25) -> attack 25
Aria wielding OakStaff (damage=15) -> attack 15
```

**Tasks:**

- Count how many classes the "one subclass per weapon" design needs for 3 hero types and 4 weapons. Compare it with the composition design.

**Package:** `t08_inheritance.exercises.ex08`

**Solution:** [t08 exercise 08 solution code](../../../../code/src/t08_inheritance/exercises/ex08/).
**Tests:** [t08 exercise 08 tests](../../../../code/test/t08_inheritance/exercises/ex08/).

---

## Exercise 09 — `equals` Across a Hierarchy (Debugging)

**Objective:** Keep `equals` symmetric when a subclass adds a field.

**Given code:**

```java
public class Item {
    private final String id;

    public Item(String id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}

public class EnchantedItem extends Item {
    private final int level;

    public EnchantedItem(String id, int level) {
        super(id);
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EnchantedItem other)) return false;
        return super.equals(o) && level == other.level;
    }
}
```

**Tasks:**

1. **Predict** the output of:

   ```java
   Item sword = new Item("sword");
   Item enchanted = new EnchantedItem("sword", 3);
   System.out.println(sword.equals(enchanted));
   System.out.println(enchanted.equals(sword));
   ```

   Which rule of the `equals` contract is broken?
2. **Fix** `Item.equals` so that an `Item` is never equal to an object of a different class.
3. **Fix** `EnchantedItem` so that equal enchanted items also have equal hash codes.
4. Add a `toString()` to `Item` only that prints `ClassName[id]`, using `getClass().getSimpleName()`. Then override it in `EnchantedItem` to append `+level`.

**Quick check:**
After the fix, both lines in task 1 print `false`. A `HashSet<Item>` given `Item("sword")`, `EnchantedItem("sword", 3)` and another `EnchantedItem("sword", 3)` has size 2.

**Hint:**
Compare `getClass() != o.getClass()`. Then `EnchantedItem.equals` can start with `if (!super.equals(o)) return false;` and cast safely.

**Package:** `t08_inheritance.exercises.ex09`

**Solution:** [t08 exercise 09 solution code](../../../../code/src/t08_inheritance/exercises/ex09/).
**Tests:** [t08 exercise 09 tests](../../../../code/test/t08_inheritance/exercises/ex09/).

