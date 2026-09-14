---
title: "Collections II: LinkedList & iteration patterns — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t06_collections_2
description: "Twelve LinkedList and iterator exercises: safe removal, ListIterator traversal, job queues, undo and redo, deques, and the Josephus problem."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, collections, linkedlist, deque, iterator, exercises, year2, comp-c8z03]
---
# Collections II: LinkedList & iteration patterns — Exercises

> These activities match the lesson **Collections II: LinkedList & iteration patterns**.
> We’ll use **plain Java**. Keep lists short so you can easily read the output.

---

## How to run

Create a package for each exercise:

```text
t06_collections_2.exercises.exNN
```

Each package should contain an `Exercise` class with a method you can call from `Main`:

```java
package t06_collections_2.exercises.ex01;

public final class Exercise {
    public static void run() {
        // Build your data, call your sorts, and print short results here
    }
}
```

---

## Exercise 01 — Remove while you walk (Foundations)

**What you’ll practice:** Using a `ListIterator` to safely remove items during a scan.
**Why this matters:** Index-based removal on a `LinkedList` is clumsy and easy to break; the
iterator makes it simple and safe.

**Task:** Start with `[-2, -1, 0, 1, 2, 3, 4]`. Remove all **non-positive** numbers (≤ 0)
**while iterating**. No extra list.

**Steps:**
1) Put the numbers into a `LinkedList<Integer>`.
2) Get a `ListIterator<Integer> it = list.listIterator();`
3) While you have a next item, read it. If it’s `<= 0`, call `it.remove()`.
4) Print the final list.

**Quick check:** Output should be `[1, 2, 3, 4]`.
**Hint:** `remove()` acts on the **last returned** item from `next()`.

---

## Exercise 02 — Put markers between groups (Foundations)

**What you’ll practice:** Inserting during iteration with `it.add(...)`.
**Why this matters:** Linked lists are great when you want to change the structure *as you
traverse*.

**Task:** Turn `a a a b b c c c` into `a a a | b b | c c c` by placing a `'|'` when the letter
changes.

**Steps:**
1) Load characters into `LinkedList<Character>`.
2) Walk with a `ListIterator<Character>` and remember the `previous` char.
3) When the current char differs from `previous`, insert `'|'` using `it.add('|')`.
4) Print the list to confirm the markers appear at boundaries.

**Quick check:** Your printed list shows `|` between `a...b` and `b...c`.
**Hint:** After `add`, call `next()` again before another `remove/set`.

---

## Exercise 03 — Walk forward, then backward (Foundations)

**What you’ll practice:** Using `hasPrevious()/previous()` to move **backwards**.
**Why this matters:** `ListIterator` is bidirectional; this is a core linked-list skill.

**Task:** Given a list of words, print the sentence **forward**, then print the same words **in
reverse** using the same iterator.

**Steps:**
1) Build `LinkedList<String> words = List.of("Java","is","fun")` returns as a new
   `LinkedList<>(...)`.
2) Use `it.next()` to print `"Java is fun"`.
3) Without creating a new iterator, loop with `hasPrevious()/previous()` to print `"fun is Java"`.
4) (Optional) If the last word equals `"foo"`, replace it with `it.set("bar")` before the
   reverse print.

**Quick check:** You see both forward and reverse prints.
**Hint:** `previous()` returns the item **before** the cursor.

---

## Exercise 04 — A small job queue (Software Dev)

**What you’ll practice:** Using a `Deque<String>` as a **FIFO** queue.
**Why this matters:** Queues appear everywhere (build pipelines, server tasks).

**Scenario:** Jobs arrive in this order: `"build"`, `"test"`, `"package"`, `"deploy"`.

**Task:** Enqueue with `offer(...)`, process with `poll()`. After you dequeue `"test"`,
**insert** a `"lint"` job immediately after it (simulating a pipeline rule change).

**Steps:**
1) Use `Deque<String> q = new LinkedList<>();` then `offer(...)` jobs.
2) Process in a loop: take `String job = q.poll();` and print it.
3) Right after you process `"test"`, insert `"lint"` using a `ListIterator` positioned at the
   current spot.
4) Finish processing and print the final order you executed.

**Quick check:** Execution order shows `"build" → "test" → "lint" → "package" → "deploy"`.
**Hint:** Prefer `offer/poll/peek` (return `null` on empty) over `add/remove/element` (throw
exceptions).

---

## Exercise 05 — Undo & Redo (Software Dev)

**What you’ll practice:** Two-stack (two-deque) **undo/redo** pattern.
**Why this matters:** Editors, level tools, and UIs use this exact approach.

**Task:** Implement `type(text)`, `undo()`, and `redo()` methods. Try this sequence: type
`"a"`, then `"b"`, then `"c"` then undo twice then redo once then type `"Z"` then print the
final state.

**Steps:**
1) Keep a `String state`, and two `Deque<String>`: `undo` and `redo`.
2) `type(s)`: `undo.push(state)`, update `state += s`, then `redo.clear()`.
3) `undo()`: if `undo` not empty, `redo.push(state)`, `state = undo.pop()`.
4) `redo()`: if `redo` not empty, `undo.push(state)`, `state = redo.pop()`.
5) Run the sequence and print `state`.

**Quick check:** Final state prints `abZ`.
**Hint:** Any **new** typing clears the `redo` history.

---

## Exercise 06 — Game events with on-the-fly inserts (Games)

**What you’ll practice:** Modifying a list as you process it.
**Why this matters:** Real-time systems often inject or cancel actions during handling.

**Task:** Start with a `LinkedList<InputEvent>` containing `"Move"`, `"Attack"`, `"Heal"`.
While scanning:
- If you see `"Attack"`, insert a `"CameraShake"` **right after** it.
- If you see `"Heal"`, remove it.

**Steps:**
1) Define a tiny `InputEvent` class with a `type` field and `toString()`.
2) Iterate with `ListIterator<InputEvent>`.
3) On `"Attack"`, call `it.add(new InputEvent("CameraShake"))`.
4) On `"Heal"`, call `it.remove()`.
5) Print the final types in order.

**Quick check:** Output has `"CameraShake"` immediately after `"Attack"` and no `"Heal"`.
**Hint:** Add before/after is relative to the iterator’s cursor—test with a tiny list first.

---

## Exercise 07 — The Josephus game (Intermediate)

**What you’ll practice:** Circular traversal and removal with a linked list.
**Why this matters:** It forces you to manage iterator position carefully.

**Task:** `N` players sit in a circle. Remove every `k`-th player until one remains. For
example, `N=7`, `k=3` to winner is `4`.

**Steps:**
1) Build `LinkedList<Integer>` of players `1..N`.
2) Use a `ListIterator<Integer>` and a counter `step`.
3) Each time `step == k`, remove the current player and reset `step`.
4) When you hit the end, wrap by creating a **new** iterator at `list.listIterator()` and continue.
5) Print the last remaining ID.

**Quick check:** With `N=7, k=3`, output `4`.
**Hint:** Keep `step` going across wraps; only reset it on removals.

---

## Exercise 08 — Merge two sorted lists (Intermediate)

**What you’ll practice:** In-place ordered merge using **two iterators**.
**Why this matters:** Avoids extra lists and highlights iterator power.

**Task:** Merge sorted list `B` into sorted list `A` (ascending order), **without creating a
third list**.

**Steps:**
1) Make copies: `A = new LinkedList<>(List.of(...))`, `B = new LinkedList<>(List.of(...))`.
2) Hold `ListIterator<Integer> ai = A.listIterator();` and standard `Iterator<Integer> bi =
   B.iterator();` (or another `ListIterator`).
3) While `bi` has items, peek the next `b`. Move `ai` forward until `a >= b`, then `ai.add(b)`
   and advance only `bi`.
4) When `ai` hits the end, `add` the rest of `B`.
5) Print `A` to confirm order.

**Quick checks:** `A` is still sorted and contains all items of both.
**Hint:** Duplicates are fine—don’t skip them.

---

## Exercise 09 — Sliding-window maximum (Intermediate • Software Dev)

**What you’ll practice:** Using a deque of **indices** to track a window maximum in O(n).
**Why this matters:** The pattern appears in streaming analytics and UIs.

**Task:** Implement `int[] maxWindow(int[] a, int w)` using a `Deque<Integer>` (back holds
**potential** max indices in **descending** value order).

**Steps:**
1) Walk the array `i=0..n-1`.
2) Drop indices at the **back** while `a[i]` is bigger than those values.
3) Drop the index at the **front** if it slid out of the window `(i - w + 1)`.
4) The **front** index is the max for the current window.
5) Record each max in the result array.

**Quick check:** Test on `[1,3,-1,-3,5,3,6,7]`, `w=3` to `[3,3,5,5,6,7]`.
**Hint:** Use `LinkedList<Integer>` as the deque implementation.

---

## Exercise 10 — Turn scheduler with priorities (Games)

**What you’ll practice:** Multiple `Deque`s and simple scheduling rules.
**Why this matters:** Many games and servers process work in priority bands.

**Task:** Maintain three queues: `hi`, `mid`, `lo`. Each “tick”: process **one** action—the
head of the **highest non-empty** deque.

**Steps:**
1) Create three `Deque<Action>` (use `LinkedList<>`).
2) Enqueue some actions across the three deques.
3) Write `Action next()` that returns from `hi`, else `mid`, else `lo`.
4) (Stretch) After executing a high-priority action, **demote** any stale remaining high
   actions to `mid` by walking `hi` with an iterator and moving nodes.

**Quick check:** Your log shows the scheduler always prefers `hi`, then `mid`, then `lo`.
**Hint:** Moving nodes = `remove()` on one deque, then `addLast()` on another.

---

## Exercise 11 — Palindrome with a deque (Foundations+)

**What you’ll practice:** `addLast/removeFirst/removeLast` symmetry checks.
**Why this matters:** Nice hands-on with both ends of a deque.

**Task:** Read a string, keep only letters and digits, and lowercase it. Put chars into a
`Deque<Character>`. While `size()>1`, compare and remove from **both ends**. Return `true` only
if all pairs match.

**Steps:**
1) Normalise input (letters/digits only, lowercase).
2) Build the deque by `addLast` each char.
3) While `size()>1`, compare `removeFirst()` vs `removeLast()`.
4) If any pair differs, it’s not a palindrome.

**Quick checks:** `""` and `"a"` are palindromes; `"abca"` is **not**.
**Hint:** `Character.isLetterOrDigit(c)` helps with filtering.

---

## Exercise 12 — Time-ordered command buffer (Games)

**What you’ll practice:** Keeping a linked list **sorted** by timestamp while inserting and
draining.
**Why this matters:** Many systems buffer future commands/events and release them when due.

**Task:** Store `(timestampMillis, command)` tuples in ascending time order. Implement:
- `insertInOrder(tuple)` returns walk until `next.timestamp > t` and insert **before** it.
- `List<Tuple> drainUntil(now)` returns remove and collect all entries where `timestamp <= now`.

**Steps:**
1) Make `LinkedList<Tuple>` and a tiny `Tuple` class with `long timestamp` + `String cmd`.
2) For insert, use `ListIterator<Tuple>` and `it.add(...)` at the correct spot.
3) For drain, iterate from the head removing due items, stop at the first future item.
4) Return the drained items (preserve their original order).

**Quick check:** After inserting out-of-order times, a full print of the list shows
**ascending** timestamps.
**Hint:** Keep both operations **linear** in the number of items touched—no full resort needed.

---

## Reflection

- Pick one exercise where `ListIterator` felt **clearer** than indexes. Why?
- Which tasks were **easier** as queues/deques than as lists?
- For one solution, write a one-line comment stating the **big-O** for the main loop.
