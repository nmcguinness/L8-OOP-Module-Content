---
title: "Ordering — Comparable & Comparator"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t03_ordering
description: "A practical introduction using explicit Comparator classes and anonymous classes. ArrayList-only; ends with chaining user-defined comparators."
created: 2025-10-06
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, ordering, sorting, comparable, comparator, arraylist, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO2, MLO3]
previous_topic: t02_recursion
prerequisites:
  - Classes, objects, and Strings
  - Basic ArrayList usage
  - Loops
---

# Ordering — Comparable & Comparator

> **Prerequisites:**
> - Classes/objects; Strings; basic ArrayList usage; loops

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Comparable — natural order inside your class](#comparable--natural-order-inside-your-class)
- [Comparator — defining comparison externally](#comparator--defining-comparison-externally)
- [Comparator - Using factories](#comparator---using-factories)
- [Sorting notes](#sorting-notes)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Explain Comparable (natural order inside the class) vs Comparator (separate ordering strategy). |
| Use | Implement `Comparable<T>` with a clear tie-break. |
| Use | Write explicit Comparator classes and anonymous classes (no lambdas). |
| Use | Sort `ArrayList<T>` with `List.sort(...)` using your comparators. |
| Use | Chain multiple user-defined comparators into a single ordering. |
| Use | Using `comparing` factory to sort by single and tie-breaker fields. |
| Debug | Avoid common issues (missing tie-breakers, nulls, inconsistent rules). |

---

## Why this matters

Almost every program that shows a user a list has to decide what order to show it in, and
that order is rarely the order the data arrived. Sorting is where you first separate
**what** you want (this before that) from **how** it is achieved (the sort algorithm).

This is also your first encounter with **behaviour as a value**: a comparator is a rule you
can name, store in a variable, pass to a method and swap at runtime. That idea returns in
full with functional interfaces and streams, so it is worth getting comfortable here.

---

## How this builds on previous content

| Earlier topic | Concept carried forward |
| :- | :- |
| [t00 OOP Fundamentals](../t00_oop_fundamentals/t00_oop_fundamentals_notes.md) | Sorting operates on **objects with fields**, so a comparator reads the same getters you wrote there |
| [t01 Arrays](../t01_arrays/t01_arrays_notes.md) | `Arrays.sort` orders an array; the same ordering rules apply to lists |
| [t02 Recursion](../t02_recursion/t02_recursion_notes.md) | The library's sort is a recursive merge sort — you supply the comparison, it supplies the algorithm |

The new idea is that **the comparison itself becomes a value** you can name, store and pass
around. That is your first taste of behaviour-as-data, which returns in full in [t16 Functional
Interfaces](../t16_functional_interfaces/t16_functional_interfaces_notes.md).

Where this goes next: `compareTo` must stay **consistent with `equals`**
([t04](../t04_equality_hashing/t04_equality_hashing_notes.md)), because `TreeSet` and `TreeMap`
([t07](../t07_collections_3_set_map/t07_collections_3_set_map_notes.md)) use it — not `equals`
— to decide what counts as a duplicate.

---

## Comparable — natural order inside your class

```java
import java.util.ArrayList;
import java.util.List;

 class Score implements Comparable<Score> {
    private String player;
    private int value;

    public Score(String player, int value) {
        this.player = player;
        this.value = value;
    }
    public String player() { return player; }
    public int value() { return value; }

    @Override
    public int compareTo(Score other) {
        // Primary: higher value first (descending)
        int byValueDesc = Integer.compare(other.value, this.value);
        if (byValueDesc != 0) return byValueDesc;
        // Tie-break: player name ascending (A->Z)
        return this.player.compareTo(other.player);
    }

    @Override
    public String toString() {
        return player + ":" + value;
    }
}

class DemoComparable {
    public static void main(String[] args) {
        var scores = new ArrayList<Score>();
        scores.add(new Score("Zara", 20));
        scores.add(new Score("Alan", 20));
        scores.add(new Score("Mia", 50));
        scores.add(new Score("Bea", 10));

        // Use natural order (compareTo)
        scores.sort(null); // or Collections.sort(scores)
        System.out.println(scores);
    }
}
```

---

## Comparator — defining comparison externally

We keep the class unchanged and supply an external ordering.

### Example A — Simple named Comparator class (name ascending)

```java
import java.util.Comparator;

 class NameAscComparator implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        return a.name().compareTo(b.name());
    }
}

 class Product {
    private String name;
    private double price;
    private double rating;

    public Product(String name, double price, double rating) {
        this.name = name; this.price = price; this.rating = rating;
    }
    public String name() { return name; }
    public double price() { return price; }
    public double rating() { return rating; }

    @Override public String toString() {
        return name + " (EUR " + price + ", " + rating + " stars)";
    }
}

class DemoComparatorA {
    public static void main(String[] args) {
        var items = new java.util.ArrayList<Product>();
        items.add(new Product("Mouse", 15.0, 4.2));
        items.add(new Product("Keyboard", 45.0, 4.5));
        items.add(new Product("Monitor", 120.0, 4.1));

        items.sort(new NameAscComparator());
        System.out.println(items);
    }
}
```

### Example B — Anonymous Comparator (price ascending)

```java
class DemoComparatorB {
    public static void main(String[] args) {
        var items = new java.util.ArrayList<Product>();
        items.add(new Product("Mouse", 15.0, 4.2));
        items.add(new Product("Keyboard", 45.0, 4.5));
        items.add(new Product("Monitor", 120.0, 4.1));

        java.util.Comparator<Product> priceAsc = new java.util.Comparator<Product>() {
            @Override
            public int compare(Product a, Product b) {
                return Double.compare(a.price(), b.price());
            }
        };

        items.sort(priceAsc);
        System.out.println(items);
    }
}
```

### Example C — Named Comparator with multi-field tie-breaks

Order by rating descending, then by name ascending.

```java
 class RatingDescThenNameAsc implements java.util.Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        // rating descending
        int byRating = Double.compare(b.rating(), a.rating());
        if (byRating != 0) return byRating;
        // tie-break by name ascending
        return a.name().compareTo(b.name());
    }
}

class DemoComparatorC {
    public static void main(String[] args) {
        var items = new java.util.ArrayList<Product>();
        items.add(new Product("Mouse", 15.0, 4.2));
        items.add(new Product("Keyboard", 45.0, 4.5));
        items.add(new Product("Monitor", 120.0, 4.1));
        items.add(new Product("Mat", 12.0, 4.5)); // same rating as Keyboard

        items.sort(new RatingDescThenNameAsc());
        System.out.println(items);
    }
}
```

---

## Comparator - Using factories

> Goal: sort an `ArrayList<T>` by one field, then break ties by another, using tiny, readable
> helpers from `Comparator`.

**Key API (java.util.Comparator):**
- `Comparator.comparing(keyExtractor)`
- `Comparator.comparingInt/Long/Double(...)` (avoid boxing)
- `thenComparing(...)` (tie-breakers)
- `reversed()` (flip order)
- `nullsFirst(...)` / `nullsLast(...)` (handle missing data)

### Example A — Products sorted by price (asc), then rating (desc), then name (asc)

```java
import java.util.ArrayList;
import java.util.Comparator;

final class Product {
    private final String name;
    private final double price;
    private final double rating;

    public Product(String name, double price, double rating) {
        this.name = name; this.price = price; this.rating = rating;
    }
    public String name() { return name; }
    public double price() { return price; }
    public double rating() { return rating; }

    @Override public String toString() {
        return name + " (EUR " + price + ", " + rating + " stars)";
    }
}

class DemoComparing {
    public static void main(String[] args) {
        var items = new ArrayList<Product>();
        items.add(new Product("Mouse", 15.0, 4.2));
        items.add(new Product("Keyboard", 45.0, 4.5));
        items.add(new Product("Monitor", 120.0, 4.1));
        items.add(new Product("Mat", 15.0, 4.5));

        // Primary: price ?, Tie1: rating ?, Tie2: name ?
        Comparator<Product> cmp =
            Comparator.comparingDouble(Product::price)
                      .thenComparing(Comparator.comparingDouble(Product::rating).reversed())
                      .thenComparing(Product::name);

        items.sort(cmp);
        System.out.println(items);
    }
}
```

### Example B — Students by surname (case-insensitive), then first name, then ID

```java
import java.util.Comparator;

// Case-insensitive surname, then firstname, then id
Comparator<Student> byName =
    Comparator.comparing((Student s) -> s.surname().toLowerCase())
              .thenComparing(s -> s.firstname().toLowerCase())
              .thenComparing(Student::id);
```

### Handling `null` safely (optional)

```java
Comparator<Product> byNameNullSafe =
    Comparator.comparing(Product::name,
        Comparator.nullsLast(String::compareToIgnoreCase));
```

**Why this matters here:** you already know how to store things in an `ArrayList`; now you can
produce ordered views of that list **without** changing the class itself. The code reads like
English and is easy to extend with more tie-breakers.

---

### Mini-aside: Lambdas in 90 seconds (so `comparing(...)` makes sense)

A **lambda** is a tiny function you can pass inline. Think: “an argument that is a function.”

**Shape:** `(parameters) -> expression` or `(parameters) -> { statements; return x; }`

**Examples used above:**

```java
// Extract a key for comparing(...)
s -> s.firstname()                  // takes Student s, returns firstname
(Product p) -> p.price()            // explicit parameter type (often optional)
String::length                      // method reference (equivalent to s -> s.length())
Product::name                       // instance method reference as key extractor
```

**When to use here:** `Comparator.comparing(...)` needs a *key extractor* function to pull the
field you want to sort by; lambdas (or method references) are the neatest way to provide it.

> Tip: Prefer the primitive versions `comparingInt/Long/Double` when your key is numeric to
> avoids boxing and is a touch faster.

---

### Quick practice

1) **Backlog tasks**: sort by priority then (int), tie-break by due date then (long epoch),
   tie-break by title A?Z.
2) **Game scores**: value ?, then player name A?Z, and treat `null` names as last.

Starter:

```java
Comparator<Task> byPriThenDueThenTitle =
    Comparator.comparingInt(Task::priority)
              .thenComparingLong(Task::dueEpoch)
              .thenComparing(Task::title, String::compareToIgnoreCase);
```

---

## Sorting notes

- `list.sort(comparator)` is the modern, preferred API.
- Sorting lists is stable: elements that compare equal keep their relative order.
- Keep rules simple and consistent; always include tie-breakers when needed.
- Handle `null` explicitly (e.g., treat unknown values as lowest or filter them out first).

---

## Reflective questions

- For one of your project classes, write a named Comparator class. What is the primary field
  and at least one tie-breaker?
- What class in a project (if any) should define a natural order using Comparable?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

These are open questions about *your* code, so there is no single right answer — but a good one
has the following shape.

**Writing a named Comparator.**
Name the class after the ordering, not the type: `TaskByDueDateThenPriority`, not
`TaskComparator2`. Pick a **primary field** that is the ordering users would expect by default
(due date for tasks, score for leaderboard entries), then at least one **tie-breaker** that is
*stable and unique* — an ID is ideal, because it guarantees a deterministic order when
everything else is equal. Without a unique final tie-break, two runs over the same data can
print rows in different orders and your tests will flicker.

A good answer also states the direction explicitly, since reversing is where most marks are lost:

```java
Comparator<Task> byDueThenPriority =
        Comparator.comparing(Task::getDueDate)                 // primary, ascending
                  .thenComparing(Task::getPriority, reverseOrder())  // highest priority first
                  .thenComparingInt(Task::getId);              // deterministic tie-break
```

**Which class should implement `Comparable`.**
Use `Comparable` only when there is *one* ordering that is genuinely the natural, obvious
meaning of "sorted" for that type, and it will not change. Good candidates: `Version`, `Money`,
`Date`, an exam `Grade` — orderings intrinsic to the value itself. Poor candidates: `Task`,
`Player`, `Product`, because those have several equally reasonable orderings (by name, by
price, by date added) and baking one into the class privileges it arbitrarily and makes it
awkward to change later.

The deciding question is: *would two reasonable developers agree on what "sorted" means here
without being told?* If yes, `Comparable`. If no, supply `Comparator`s instead. It is also
perfectly normal for a class to have neither, or to have `Comparable` **and** several
`Comparator`s for alternative views.

One rule worth remembering: if you implement `Comparable`, keep `compareTo` **consistent with
`equals`** — `a.compareTo(b) == 0` should mean `a.equals(b)`. `TreeSet` and `TreeMap` use
`compareTo`, not `equals`, to decide duplicates, so an inconsistent implementation silently
swallows elements. That link is picked up in [t04 Equality &
Hashing](../t04_equality_hashing/t04_equality_hashing_notes.md) and [t07 Collections
III](../t07_collections_3_set_map/t07_collections_3_set_map_notes.md).

</details>

## Lesson Context

```yaml
previous_lesson:
  topic_code: t02_recursion
  domain_emphasis: Balanced

this_lesson:
  topic_code: t03_ordering
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO2, MLO3]
```
