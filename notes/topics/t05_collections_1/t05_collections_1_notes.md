---
title: "Collections I: ArrayList"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t05_collections_1
description: "Year-2 introduction to Java ArrayList with iteration patterns, common APIs, pitfalls, and balanced Games/Software examples."
created: 2025-10-07
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, collections, arraylist, iteration, year2, comp-c8z03]
difficulty_tier: Foundation
mlos: [MLO3]
previous_topic: t04_equality_hashing
prerequisites:
  - Arrays (1D & 2D)
  - equals/hashCode basics
  - Ordering basics (reading only)
---

# Collections I: ArrayList
>
> **Prerequisites:**
> - Arrays (1D & 2D): indexing, iteration, basic algorithms (search, simple transforms)
> - equals/hashCode basics: what identity means for objects
> - Ordering basics (reading only): idea of natural order vs custom order (using Comparing and
>   Lambda functions)

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Core Ideas 1](#core-ideas-1)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Useful snippets (guards and helpers)](#useful-snippets-guards-and-helpers)
- [Performance and trade-offs](#performance-and-trade-offs)
- [Core ideas 2](#core-ideas-2)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

A quick summary of what **you** should be able to do after this lesson:

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Distinguish fixed arrays vs `ArrayList<T>` and justify when `ArrayList` is the better fit. |
| Use | Construct and manipulate `ArrayList<T>` (`add`, `get`, `set`, `remove`, `addAll`, `toArray`). |
| Use | Iterate with index `for`, enhanced `for`, and safely remove using an `Iterator`. |
| Use | Convert between arrays and `ArrayList` and apply simple bulk operations. |
| Use | Use `Comparing` and lambda functions to sort and sort by tie-breakers |
| Debug | Prevent and fix common issues (`IndexOutOfBoundsException`, null handling). |

---

## Why this matters

Arrays are great when the size never changes. Real programs rarely stay that tidy.
**`ArrayList<T>`** gives you a resizable, indexable list with familiar bracket-style thinking,
but with safer APIs. It underpins many tasks we’ll do this semester: loading CSVs, sorting
records, and building simple reports.

> If you can reason about a 1D array, you can master `ArrayList`—just trade fixed length for
> automatic resizing and a rich set of methods.

---

## How this builds on previous content

- Backed by a dynamically resized array (capacity grows automatically).
- **Index-based** random access is O(1) average.
- Inserting/removing near the **end** is cheap; near the **front** can be costly (elements shift).

> Rule of thumb: lots of **random reads** + **append at end** — `ArrayList` is a strong default.

---

## Core Ideas 1

```java
import java.util.ArrayList;
import java.util.Iterator;

ArrayList<String> names = new ArrayList<>();          // empty
ArrayList<Integer> scores = new ArrayList<>(128);     // with initial capacity

// Add
names.add("Aoife");           // append
names.add(0, "Alex");        // insert at index (shifts right)

// Read / Update
String first = names.get(0);   // read
names.set(1, "Maya");         // replace element at index

// Remove
names.remove(0);               // by index
names.remove("Maya");         // by value (first match)

int n = names.size();
boolean empty = names.isEmpty();
boolean hasAlex = names.contains("Alex");
int firstIndex = names.indexOf("Aoife");
int lastIndex = names.lastIndexOf("Aoife");

// Bulk ops
ArrayList<String> more = new ArrayList<>();
more.add("Cara"); more.add("Ben");
names.addAll(more);            // extend

// Iteration patterns
for (int i = 0; i < names.size(); i++) { System.out.println(i + ": " + names.get(i)); }
for (String s : names) { System.out.println(s); }      // read-only iteration

// Safe removal during iteration (use Iterator)
Iterator<String> it = names.iterator();
while (it.hasNext()) {
    if (it.next().startsWith("A")) it.remove();
}
```

### Useful utilities

```java
String[] arr = {"red","green","blue"};
ArrayList<String> list = new ArrayList<>();
for (String s : arr) list.add(s);                      // array → list (simple & safe)

// list → array
String[] out = list.toArray(new String[0]);            // size-aware copy

// Clearing
list.clear();
```

> Avoid `Arrays.asList` for a resizable list: it creates a fixed-size view; `add/remove` will throw.

---

### Iteration patterns & when to use each

- **Index `for`**: when you need positions, random access, or to write back with `set(i,...)`.
- **Enhanced `for`**: clean read-only traversal.
- **`Iterator`**: when **removing** elements as you scan. Don’t remove from the list inside an
  enhanced `for` to `ConcurrentModificationException`.

---

## Progressive coding steps (A then B then C)

### Step A — Append vs insert benchmark (tiny diagnostic)

```java
ArrayList<Integer> a = new ArrayList<>();
long t0 = System.nanoTime();
for (int i = 0; i < 50_000; i++) a.add(i);             // append
long t1 = System.nanoTime();
for (int i = 0; i < 10_000; i++) a.add(0, i);          // insert at front
long t2 = System.nanoTime();
System.out.printf("append: %d µs, front-insert: %d µs\n", (t1-t0)/1000, (t2-t1)/1000);
```

*Observation:* front inserts shift many elements. Prefer appending + sorting later if order is
flexible.

### Step B — Filtering

```java
ArrayList<Integer> nums = new ArrayList<>();
for (int i = 0; i < 20; i++) nums.add(i);
Iterator<Integer> it = nums.iterator();
while (it.hasNext()) {
    if (it.next() % 2 == 0) it.remove();               // remove evens
}
```

### Step C — De-duplication (preserve first occurrence)

```java
ArrayList<String> input = new ArrayList<>();
// ... fill with values
ArrayList<String> unique = new ArrayList<>();
for (String s : input) {
    if (!unique.contains(s)) unique.add(s);            // O(n^2) in worst case
}
```

> Later we’ll prefer a `HashSet` for O(n) de-dup (see *Collections III*), but this version
> shows list-only logic.

---

## Useful snippets (guards and helpers)

- `IndexOutOfBoundsException`: check `0 <= i && i < list.size()` before `get/set/remove(i)`.
- `NullPointerException`: avoid storing `null` unless it’s a real “no value” signal.
- Document **ownership**: who creates/modifies the list? Pass as `List<T>` where possible.

---

## Performance and trade-offs

| Operation                         | Typical cost |
|----------------------------------|--------------|
| Read `get(i)` / write `set(i)`   | O(1) |
| Append `add(x)`                   | Amortized O(1) |
| Insert/remove at middle/front     | O(n) |
| `contains(x)` / `indexOf(x)`      | O(n) |

> “Amortized” means occasional capacity growth is paid for by many cheap appends.

---

## Core ideas 2

### 9.1 Games: Player inventory (stackable items)

```java
class Item { final String id; int qty; Item(String id,int q){this.id=id;this.qty=q;} }
class Inventory {
    private final ArrayList<Item> items = new ArrayList<>();
    public void addItem(String id, int q){
        int idx = indexOf(id);
        if (idx >= 0) items.get(idx).qty += q; else items.add(new Item(id,q));
    }
    public boolean removeItem(String id, int q){
        int idx = indexOf(id);
        if (idx < 0) return false;
        Item it = items.get(idx);
        if (it.qty < q) return false;
        it.qty -= q; if (it.qty == 0) items.remove(idx);
        return true;
    }
    private int indexOf(String id){
        for (int i=0;i<items.size();i++) if (items.get(i).id.equals(id)) return i;
        return -1;
    }
}
```

### 9.2 Software Dev: To-do list with priority buckets

```java
class Task { final String title; final int pri; Task(String t,int p){title=t;pri=p;} }
class Backlog {
    private final ArrayList<Task> tasks = new ArrayList<>();
    public void add(String title, int pri){ tasks.add(new Task(title, pri)); }
    public ArrayList<Task> byPriority(int pri){
        ArrayList<Task> out = new ArrayList<>();
        for (Task t : tasks) if (t.pri == pri) out.add(t);
        return out;
    }
}
```

## Reflective questions

- When would you still prefer a raw array over `ArrayList`?
- Why is removing while using `for-each` problematic?
- What’s the trade-off between `contains` on a list vs a set?
- How could you test that your list-manipulation methods behave for empty lists?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**When a raw array still wins.**
Three honest cases. **Fixed, known size** where the length is part of the meaning — a chess
board, an RGB triple, a lookup table of 12 month lengths. **Primitives at scale**:
`int[1_000_000]` stores raw ints, whereas `List<Integer>` boxes every element into an object,
costing several times the memory and adding pointer-chasing. **APIs that demand one** —
`main(String[] args)`, `String.split()`, `Files.readAllBytes()`.

Outside those, `ArrayList` is the better default: it grows, it carries its own size, and it
gives you `contains`, `remove`, `indexOf` rather than hand-written loops. Note that `ArrayList`
*is* an array internally — you are choosing a managed wrapper, not a different data structure.

**Why removing during `for-each` is a problem.**
The enhanced `for` uses an `Iterator` behind the scenes. That iterator records the list's
`modCount` when it starts; calling `list.remove(...)` directly changes the list without the
iterator's knowledge, and the next `next()` detects the mismatch and throws
`ConcurrentModificationException`. The name misleads — no second thread is involved.

It is worse than a plain crash, because it is only *usually* detected. Removing the
second-to-last element can make the iterator's index checks pass and the loop exit silently one
element early, so you get wrong output and no exception at all:

```java
// Throws ConcurrentModificationException
for (String s : list) { if (s.isBlank()) list.remove(s); }

// Correct - the iterator does the removing
list.removeIf(String::isBlank);

// Also correct, when you need more control
Iterator<String> it = list.iterator();
while (it.hasNext()) { if (it.next().isBlank()) it.remove(); }
```

**`contains` on a list vs a set.**
`List.contains` is a linear scan, O(n) — it compares against elements one by one until it finds
a match. `HashSet.contains` hashes the value and checks one bucket, O(1) on average. For a
handful of elements the difference is irrelevant and the list is simpler; inside a loop over
many items it is the difference between instant and unusable — 10 000 lookups over a 10
000-element list is 100 million comparisons.

The trade-off is what you give up: a `Set` discards duplicates and (for `HashSet`) insertion
order. If you need "unique, and I only ever ask *is it in there?*", use a `Set`. If you need
order and duplicates, keep the `List` — and if you need *both* fast lookup and order, keep a
`List` for iteration plus a `HashSet` for membership, or use `LinkedHashSet`.

**Testing behaviour on empty lists.**
Call each method with `new ArrayList<>()` and assert the *documented* result rather than
whatever the code happens to do — that is the point of the test. A good set:

```java
@Test void filterEvens_emptyList_returnsEmptyList() {
    assertTrue(filterEvens(new ArrayList<>()).isEmpty());
}

@Test void filterEvens_emptyList_returnsEmptyNotNull() {
    assertNotNull(filterEvens(new ArrayList<>()));   // empty list, never null
}

@Test void max_emptyList_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> max(new ArrayList<>()));
}
```

Note the deliberate contrast: a *filter* over nothing sensibly returns an empty list, but a
*maximum* of nothing has no sensible answer, so it should throw rather than invent one. Decide
which case each method is, write it down, then test it. Also test the single-element list — it
is where off-by-one bugs in index loops actually surface.

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t04_equality_hashing
  domain_emphasis: Balanced

this_lesson:
  topic_code: t05_collections_1
  primary_domain_emphasis: Balanced
  difficulty_tier: Foundation
mlos: [MLO3]
```
