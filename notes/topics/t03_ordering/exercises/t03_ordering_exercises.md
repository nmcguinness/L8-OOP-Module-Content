---
title: "Ordering — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t03_ordering
description: "Eight exercises on Comparable and Comparator, covering natural order, multi-field and chained comparators, sort stability, and null policy."
created: 2025-10-06
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, ordering, sorting, comparable, comparator, arraylist, exercises, year2, comp-c8z03]
---
# Ordering — Exercises

> These exercises match the lesson **Ordering in Java — Comparable & Comparator (No Lambdas)**.
> You will write **named Comparator classes** or **anonymous classes**.  Keep your lists short
> so you can quickly see the effect of sorting.

## How to run

Create a package for each exercise:

```text
t03_ordering.exercises.exNN
```

Each package should contain an `Exercise` class with a method you can call from `Main`:

```java
package t03_ordering.exercises.ex01;

public final class Exercise {
    public static void run() {
        // Build your data, call your sorts, and print short results here
    }
}
```

---

## Exercise 01 — Comparable Basics (Natural Order)

**What you’ll practice:** Implementing `Comparable<T>` and using `List.sort(null)`.

**Task:** Create a `Score(player:String, value:int)` class that implements `Comparable<Score>`.
Sorting rule: **value descending**, then **player name ascending**.

**Steps:**
1) Write the class with fields, constructor, getters, and `toString()`.
2) Implement `compareTo(Score other)` using the rule above.
3) Create an `ArrayList<Score>` with 4–6 scores (include a tie on value).
4) Call `scores.sort(null)` and print the list on one line.

**Quick checks (should be true):**

```java
System.out.println(scores.get(0).value() >= scores.get(1).value());
System.out.println(scores.get(scores.size()-1).value() <= scores.get(scores.size()-2).value());
```

**Hint:** Use `Integer.compare(a, b)` to avoid mistakes with subtraction.

---

## Exercise 02 — Simple Named Comparator

**What you’ll practice:** Writing a **named Comparator class** and using it with `list.sort(...)`.

**Task:** Define `Product(name:String, price:double, rating:double)`.
Create `NameAscComparator` that sorts products by `name` **A–Z**.

**Steps:**
1) Implement `Product` (fields, constructor, getters, `toString()`).
2) Implement `NameAscComparator implements Comparator<Product>` and compare by `name()`.
3) Build a short list (3–5 products) and call `items.sort(new NameAscComparator());`
4) Print the ordered list on one line.

**Quick check:** first name should come alphabetically before or equal to the second:

```java
System.out.println(items.get(0).name().compareTo(items.get(1).name()) <= 0);
```

**Hint:** `String.compareTo` already compares alphabetically.

---

## Exercise 03 — Anonymous Comparator

**What you’ll practice:** Creating an **anonymous** comparator and using it once.

**Task:** Sort the products by **price ascending** using an **anonymous** `Comparator<Product>`.

**Steps:**
1) Reuse your `Product` class and list from Exercise 02.
2) Create the comparator inline:

```java
java.util.Comparator<Product> priceAsc = new java.util.Comparator<Product>() {
    @Override public int compare(Product a, Product b) {
        return Double.compare(a.price(), b.price());
    }
};
```

1) Call `items.sort(priceAsc);` and print the result.
2) Compare this order to the name order from Exercise 02 in a short comment.

**Quick check:** the first price should be less than or equal to the second:

```java
System.out.println(items.get(0).price() <= items.get(1).price());
```

**Hint:** `Double.compare` handles decimals correctly.

---

## Exercise 04 — Multi-field Comparator

**What you’ll practice:** Designing a comparator with a clear **tie-breaker**.

**Task:** Create a class `RatingDescThenNameAsc` that sorts by `rating` **high to low**.
If ratings are equal, sort by `name` **A–Z**.

**Steps:**
1) Implement `RatingDescThenNameAsc implements Comparator<Product>` with two steps: rating
   first, then name.
2) Build a list with at least two products sharing the same rating.
3) Sort and print. Manually verify that tied ratings are ordered by name.

**Quick checks:**

```java
// top item has rating >= second
System.out.println(items.get(0).rating() >= items.get(1).rating());
// if ratings tie, check lexicographic name order
if (items.get(0).rating() == items.get(1).rating()) {
    System.out.println(items.get(0).name().compareTo(items.get(1).name()) <= 0);
}
```

**Hint:** Write comparisons step-by-step. If the first comparison is non-zero, return it
immediately.

---

## Exercise 05 — Stability Demonstration (Two-pass Sort)

**What you’ll practice:** Seeing how **stable** sorting preserves earlier order for “ties”.

**Task:** Show that two-pass sorting is stable on lists.
First sort by `name` A–Z, then by `price` low to high.

**Steps:**
1) Sort by `name` using `NameAscComparator`. Print.
2) Sort by `price` using your anonymous comparator from Exercise 03. Print.
3) Pick two items with the same price and explain (in a comment) whether their **name order**
   from step 1 is preserved.

**Hint:** Stability means that items considered “equal” by the current comparator keep their
previous relative order.

---

## Exercise 06 — Null Handling Policy

**What you’ll practice:** Making a deliberate choice about `null` values during sorting.

**Task:** Allow `rating` to be `null` in `Product`. Write `RatingNullLast` that puts `null`
ratings **after** all non-null ratings, and then ties break by `name` A–Z.

**Steps:**
1) Add `rating` as `Double` and allow `null`.
2) In the comparator, write a helper `safeCompare(Double a, Double b)` that orders `null` as lowest.
3) Compare ratings in “descending with nulls last” order (compare b vs a to flip direction).
4) If ratings tie (including both null), compare names A–Z.

**Quick check:** all non-null ratings appear before any null ratings.

**Hint:** Test with a small list: include at least one null and one non-null rating.

---

## Exercise 07 — Design Choice: Comparable vs Comparator

**What you’ll practice:** Choosing between **natural order** and **external orderings**.

**Task:** Pick a class from your project (e.g., `Task`, `Level`, `Product`). Decide whether it
should implement `Comparable` (one sensible default) or whether you should keep several named
`Comparator` classes instead.

**Steps:**
1) Write 3–5 lines in comments explaining your decision.
2) Implement the chosen approach (either `compareTo` or a named comparator).
3) Demonstrate with a short `ArrayList` and a call to `list.sort(...)`.

**Hint:** If different screens or reports need different orders, prefer comparators over a
single natural order.

---

## Exercise 08 — Stretch Exercise: Chaining Your Own Comparators

**What you’ll practice:** Combining simple comparators into one **reusable** order.

**Task:** Implement a generic `ChainComparator<T>` that accepts an ordered list of comparators
and tries each in turn until one decides. Then compose a chain:
**price asc then rating desc then name asc**.

**Steps:**
1. Define a simple Product class.
2. Write three small, named comparators: PriceAsc, RatingDesc, NameAsc.
3. Implement a reusable `ChainComparator<T>` that tries each comparator in order.
4. Put it all together in a short demo you can run immediately.

This is a **stretch exercise** meant to challenge you so you're not supposed to find it "easy".
Below you can see a worked solution to the exercise.

### 1) Product class

```java
// File: Product.java
public final class Product {
    private final String name;
    private final double price;
    private final double rating; // e.g., 0.0 to 5.0

    public Product(String name, double price, double rating) {
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public String name() { return name; }
    public double price() { return price; }
    public double rating() { return rating; }

    @Override
    public String toString() {
        return name + " (EUR " + price + ", " + rating + " stars)";
    }
}
```

Notes
- Keep fields final so objects are immutable once created.
- Provide small getters and a helpful toString() for quick printing.

### 2) Three small comparators

Each comparator does one job clearly. Later, we can chain them.

```java
// File: PriceAsc.java
import java.util.Comparator;

public final class PriceAsc implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        // lower price should come first
        return Double.compare(a.price(), b.price());
    }
}
```

```java
// File: RatingDesc.java
import java.util.Comparator;

public final class RatingDesc implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        // higher rating should come first
        return Double.compare(b.rating(), a.rating());
    }
}
```

```java
// File: NameAsc.java
import java.util.Comparator;

public final class NameAsc implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        // alphabetical order A->Z
        return a.name().compareTo(b.name());
    }
}
```

Why separate classes?
- Clear, testable units.
- Easy to reuse in different chains.
- No new syntax to learn.

### 3) A reusable ChainComparator

The chain tries PriceAsc, then RatingDesc, then NameAsc.
If the first comparator declares two items different, we stop. If not, we try the next, and so on.

```java
// File: ChainComparator.java
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class ChainComparator<T> implements Comparator<T> {
    private final List<Comparator<T>> chain;

    @SafeVarargs
    public ChainComparator(Comparator<T>... comparators) {
        this.chain = Arrays.asList(comparators);
    }

    @Override
    public int compare(T a, T b) {
        for (Comparator<T> c : chain) {
            int result = c.compare(a, b);
            if (result != 0) return result; // as soon as we have a decision, return it
        }
        return 0; // completely equal under all comparators
    }
}
```

> Each comparator is a "layer." The chain checks them left-to-right. Keep each comparator small
> and single-purpose; the chain handles the combination logic.

### 4) Putting it together (runnable demo)

```java
// File: DemoChaining.java
import java.util.ArrayList;
import java.util.List;

public final class DemoChaining {
    public static void main(String[] args) {
        // 1) Build a small list so we can eye-check the output
        var items = new ArrayList<Product>();
        items.add(new Product("Monitor", 120.0, 4.1));
        items.add(new Product("Keyboard", 45.0, 4.5));
        items.add(new Product("Mouse", 15.0, 4.2));
        items.add(new Product("Mat", 12.0, 4.5)); // same rating as Keyboard
        items.add(new Product("Microphone", 45.0, 4.0)); // same price as Keyboard

        // 2) Build our chain: price asc -> rating desc -> name asc
        var chain = new ChainComparator<Product>(
            new PriceAsc(),
            new RatingDesc(),
            new NameAsc()
        );

        // 3) Apply the chain
        items.sort(chain);

        // 4) Show result
        System.out.println("Sorted by price asc, then rating desc, then name asc:");
        for (Product p : items) {
            System.out.println("  " + p);
        }

        // 5) Quick sanity check: prices should not decrease
        boolean nonDecreasing = true;
        for (int i = 1; i < items.size(); i++) {
            if (items.get(i-1).price() > items.get(i).price()) {
                nonDecreasing = false;
                break;
            }
        }
        System.out.println("Prices non-decreasing? " + nonDecreasing);
    }
}
```

What to look for in the output
- All items should be ordered from lowest price to highest.
- When two products share a price, the one with higher rating appears first.
- If price and rating both tie, sort by name A->Z (helps keep the output predictable).

### 5) Where to place the files

Suggested structure (follow your existing repo conventions):

```text
/src/main/java/...
  t03_ordering/exercises/ex05_chaining_demo/
    Product.java
    PriceAsc.java
    RatingDesc.java
    NameAsc.java
    ChainComparator.java
    DemoChaining.java
```

Then call t03_ordering.exercises.ex05_chaining_demo.DemoChaining.main() from your Main or run
it directly in your IDE.

### 6) Extension ideas (optional)

- Make RatingNullLast to treat unknown ratings as lowest priority in the chain.
- Add a ReleaseDateAsc comparator and extend the chain to four layers.
- Write a small JUnit test to verify the chain’s decisions on a few pairs.

---
