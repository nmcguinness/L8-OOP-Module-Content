---
title: "Collections I: ArrayList essentials — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t05_collections_1
description: "Ten ArrayList exercises: core API, iterator-safe filtering, de-duplication, merging, grouping, and a scoreboard challenge."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, collections, arraylist, iteration, exercises, year2, comp-c8z03]
---
# Collections I: ArrayList essentials — Exercises

> These exercises build directly on **Collections I: ArrayList essentials**. Work from top to
> bottom; each task is small and testable.
>
> Use **Java 17**, no streams/lambdas. Prefer tiny helper methods and quick checks printed to
> console.

## How to run

Each exercise assumes a package like:

```text
t05_collections_1.exercises.exNN
```

Create a class `Exercise` in that package with a static entry point:

```java
package t05_collections_1.exercises.ex01;

public class Exercise {
    public static void run() {
        // your tests here
    }
    public static void main(String[] args) { run(); }
}
```

---

## Exercise 01 — Warm-up: add/get/set/remove

**Objective:** Practice core `ArrayList<T>` CRUD.

**Task:** Create an `ArrayList<String>` of names. Append three names, insert one at index 0,
replace the last, then remove by value.

**Steps:**
1) Start with `ArrayList<String> names = new ArrayList<>();`
2) Use `add`, `add(index, value)`, `set`, `remove(value)`.
3) Print size and all items at the end with a simple loop.

**Quick checks (expect `true` each):**

```java
System.out.println(names.size() == 3);
System.out.println(!names.contains("—the one you removed—"));
```

---

## Exercise 02 — Arrays ↔ ArrayList conversion

**Objective:** Move data safely between arrays and lists.

**Task:** Write:

```java
static ArrayList<Integer> toList(int[] xs);     // copy contents
static int[] toArray(ArrayList<Integer> list);  // return new array
```

**Rules:**
- Handle `null` input by returning empty outputs (not null).
- Prove with quick checks:

```java
int[] a = {1,2,3};
ArrayList<Integer> L = toList(a);
int[] b = toArray(L);
System.out.println(b.length == 3 && b[0]==1 && b[2]==3);
```

---

## Exercise 03 — Safe filtering with Iterator

**Objective:** Remove while iterating without `ConcurrentModificationException`.

**Task:** Given an `ArrayList<Integer>`, remove all even numbers **in place** using
`Iterator.remove()`.

**Skeleton:**

```java
static void removeEvens(ArrayList<Integer> xs) {
    // use Iterator<Integer> it = xs.iterator();
}
```

**Quick checks:**

```java
ArrayList<Integer> xs = toList(new int[]{1,2,3,4,5,6});
removeEvens(xs);
System.out.println(xs.toString().equals("[1, 3, 5]"));
```

---

## Exercise 04 — De-duplication (preserve first occurrence)

**Objective:** Build a unique list using only `ArrayList` APIs.

**Task:** Write:

```java
static ArrayList<String> uniqueOrderPreserving(ArrayList<String> input);
```

Keep the **first** time each string appears; drop later duplicates. (We’ll use sets later in
*Collections III*—here, do it list-only.)

**Hint:** Create an empty `ArrayList<String> out` and scan `input` with `contains`.

**Checks:**

```java
ArrayList<String> in = new ArrayList<>();
in.add("a"); in.add("b"); in.add("a"); in.add("c"); in.add("b");
System.out.println(uniqueOrderPreserving(in).toString().equals("[a, b, c]"));
```

---

## Exercise 05 — Insert vs append (tiny benchmark)

**Objective:** Feel performance differences.

**Task:** Measure time (rough, nano/micro) for:
- appending 50,000 integers,
- inserting 10,000 integers at index 0.

**Skeleton:**

```java
static long timeAppend(int n) { /* ... */ }
static long timeInsertFront(int n) { /* ... */ }
public static void run() {
    System.out.printf("append=%dµs, front-insert=%dµs%n",
        timeAppend(50_000)/1000, timeInsertFront(10_000)/1000);
}
```

**Discuss:** Why is front-insert slower? Summarize in a comment.

---

## Exercise 06 — Find & update objects

**Objective:** Store simple objects and update them by key.

**Task:** Implement:

```java
class Task { final String title; int priority; Task(String t,int p){title=t;priority=p;} }

static int indexOfTitle(ArrayList<Task> tasks, String title); // -1 if not found
static boolean updatePriority(ArrayList<Task> tasks, String title, int newPri);
```

**Checks:**

```java
ArrayList<Task> tasks = new ArrayList<>();
tasks.add(new Task("Login",2)); tasks.add(new Task("Save",1));
System.out.println(indexOfTitle(tasks,"Save")==1);
System.out.println(updatePriority(tasks,"Save",3));
System.out.println(tasks.get(1).priority==3);
```

---

## Exercise 07 — Stable partition (two lists)

**Objective:** Split a list into two new lists based on a predicate.

**Task:** Write:

```java
static ArrayList<Integer> evens(ArrayList<Integer> xs);
static ArrayList<Integer> odds(ArrayList<Integer> xs);
```

**Constraints:** Original list unchanged. Use a single pass.

**Checks:**

```java
ArrayList<Integer> xs = toList(new int[]{1,2,3,4,5,6});
System.out.println(evens(xs).toString().equals("[2, 4, 6]"));
System.out.println(odds(xs).toString().equals("[1, 3, 5]"));
```

---

## Exercise 08 — Merge two sorted lists (no streams)

**Objective:** Classic two-pointer merge, returning a new sorted list.

**Task:** Given two **individually sorted** `ArrayList<Integer>` (ascending), produce a single
sorted `ArrayList<Integer>`.

**Signature:**

```java
static ArrayList<Integer> mergeSorted(ArrayList<Integer> a, ArrayList<Integer> b);
```

**Checks:**

```java
ArrayList<Integer> a = toList(new int[]{1,3,5});
ArrayList<Integer> b = toList(new int[]{2,4,6});
System.out.println(mergeSorted(a,b).toString().equals("[1, 2, 3, 4, 5, 6]"));
```

---

## Exercise 09 — Group by simple key (list-of-lists)

**Objective:** Organise items into buckets without a `Map` (yet).

**Task:** Given `ArrayList<String> names`, group by **first letter** into
`ArrayList<ArrayList<String>>`, where bucket index 0 = ‘A’, 1 = ‘B’, ... 25 = ‘Z’. Ignore
non-letters.

**Hints:**
- Pre-create 26 empty buckets.
- Normalize with `Character.toUpperCase(c)`.

**Checks (order within buckets preserved):**

```java
ArrayList<String> names = new ArrayList<>();
names.add("alice"); names.add("bob"); names.add("amy");
ArrayList<ArrayList<String>> g = groupByFirstLetter(names);
System.out.println(g.get(0).toString().equals("[alice, amy]")); // A
System.out.println(g.get(1).toString().equals("[bob]"));        // B
```

---

## Exercise 10 — Mini challenge: scoreboard list

**Objective:** A small multi-method exercise using only `ArrayList`.

**Types & API:**

```java
class Score { final String player; final int value; Score(String p,int v){player=p;value=v;} }

static void addScore(ArrayList<Score> list, String player, int value); // append
static boolean removeBelow(ArrayList<Score> list, int threshold);      // in-place, return true if any removed
static int bestScore(ArrayList<Score> list);                           // 0 if list empty
static ArrayList<Score> topN(ArrayList<Score> list, int n);            // new list, best-first, stable for ties
```

**Rules:**
- `topN` may scan repeatedly (simple, no comparator yet).
- Keep original list order for equal scores.

**Checks:**

```java
ArrayList<Score> S = new ArrayList<>();
addScore(S,"Alex",10); addScore(S,"Ben",25); addScore(S,"Cara",25); addScore(S,"Dee",5);
System.out.println(bestScore(S) == 25);
System.out.println(removeBelow(S,10));                 // removes Dee
System.out.println(S.size()==3);
System.out.println(topN(S,2).toString().equals("[Ben:25, Cara:25]"));
```

---

## (Optional) Support snippets you can copy

```java
static ArrayList<Integer> toList(int[] xs){
    ArrayList<Integer> out = new ArrayList<>();
    if (xs != null) for (int x : xs) out.add(x);
    return out;
}
static void printList(ArrayList<?> xs){
    for (int i=0;i<xs.size();i++) System.out.print((i>0?", ":"") + xs.get(i));
    System.out.println();
}
```
