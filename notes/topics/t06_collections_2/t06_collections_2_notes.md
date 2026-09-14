---
title: "Collections II: LinkedList & iteration patterns"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t06_collections_2
description: "Year-2 exploration of LinkedList as a List and as a Deque; safe iteration/removal with ListIterator; queue/stack patterns; performance trade-offs; balanced Games/Software examples."
created: 2025-10-07
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, collections, linkedlist, deque, iterator, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO3]
previous_topic: t05_collections_1
prerequisites:
  - Arrays (1D & 2D)
  - Equality & Hashing — Foundations
  - Collections I: ArrayList essentials
---

# Collections II: LinkedList & iteration patterns
>
> **Prerequisites:**
> - Arrays (1D & 2D): indexing & traversal
> - equals/hashCode basics (reading only)
> - Collections I: ArrayList (APIs, iteration, iterator-removal)

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Core ideas — `LinkedList<T>` as a List](#core-ideas--linkedlistt-as-a-list)
- [Core ideas — `ListIterator<T>` (bidirectional + safe
  mutation)](#core-ideas--listiteratort-bidirectional--safe-mutation)
- [Core ideas — `Deque<T>` patterns with
  `LinkedList<T>`](#core-ideas--dequet-patterns-with-linkedlistt)
- [Performance and trade-offs](#performance-and-trade-offs)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Useful snippets (guards and helpers)](#useful-snippets-guards-and-helpers)
- [Games example — Event stream & input buffer](#games-example--event-stream--input-buffer)
- [Software Dev example — Job queue with priority buckets
  (lightweight)](#software-dev-example--job-queue-with-priority-buckets-lightweight)
- [Debugging and pitfalls](#debugging-and-pitfalls)
- [Reflective questions](#reflective-questions)
- [Appendix A — Mini reference (APIs you’ll use
  most)](#appendix-a--mini-reference-apis-youll-use-most)
- [Appendix B — Choosing a collection (quick
  heuristics)](#appendix-b--choosing-a-collection-quick-heuristics)

---

## What you'll learn

A quick summary of what **you** should be able to do after this lesson:

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Explain when a **linked list** is preferable to a **dynamic array** (inserts/removals via iterator vs random access). |
| Use | Construct and manipulate `LinkedList<T>` as a **List** and as a **Deque** (queue/stack). |
| Use | Traverse with `ListIterator<T>` (forward/backward), and **insert/remove during iteration** safely. |
| Use | Apply **queue**, **deque**, and **stack** patterns with `offer/poll/peek`, `addFirst/addLast`, `push/pop`. |
| Analyze | Compare asymptotic **costs** of key operations vs `ArrayList` and reason about memory overheads. |
| Debug | Avoid common pitfalls (O(n) random access, iterator invalidation, null handling). |

---

## Why this matters

Many real problems involve **frequent inserts/removals in the middle or ends** (task queues,
undo/redo, event streams). `LinkedList<T>` trades **random access speed** for **cheap
structural changes**—especially when you already hold an iterator at the change point.

> Rule of thumb: If you’re **walking** the structure and **mutating as you go**, `LinkedList` +
> `ListIterator` is often simpler and can be efficient enough. If you’re **index-hopping**,
> prefer `ArrayList`.

---

## How this builds on previous content

- From **Collections I**, you know list APIs, iteration patterns, and iterator-safe removal.
- We now add: **bidirectional iteration**, **mid-scan insertion**, and **deque/stack** usage in
  one type.

---

## Core ideas — `LinkedList<T>` as a List

```java
import java.util.LinkedList;
import java.util.List;

List<String> names = new LinkedList<>();
names.add("Alex");          // append (tail)
names.add(0, "Aoife");      // insert at head
names.remove("Alex");       // remove by value
names.add("Ben");
System.out.println(names.get(1)); // O(n) walk under the hood
```

**Notes**
- `get(i)`, `set(i, v)`: **O(n)** (node walk).
- `add(v)`, `add(0, v)`, `remove(0)`: can be **O(1)** at ends.
- Prefer **iterators** over index arithmetic when changing structure.

---

## Core ideas — `ListIterator<T>` (bidirectional + safe mutation)

```java
import java.util.LinkedList;
import java.util.ListIterator;

var list = new LinkedList<String>();
list.add("A"); list.add("C"); list.add("D");

ListIterator<String> it = list.listIterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("A")) it.add("B");     // insert right after A
    if (s.equals("D")) it.set("Delta"); // replace current element
}
// Walk backward
while (it.hasPrevious()) {
    System.out.println(it.previous());
}
```

**Key rules**
- `it.remove()` removes the **last returned** element.
- `it.add(x)` inserts **before** the cursor position; subsequent `previous()` will see `x`.
- After `add` or `remove`, call `next/previous` again before another `remove/set`.

---

## Core ideas — `Deque<T>` patterns with `LinkedList<T>`

`LinkedList<T>` implements `Deque<T>`, so you get **queue** and **stack** behavior.

### Queue (FIFO)

```java
import java.util.Deque;
import java.util.LinkedList;

Deque<String> tasks = new LinkedList<>();
tasks.offer("load");       // enqueue (tail)
tasks.offer("parse");
String t1 = tasks.poll();  // dequeue (head) → "load"
String t2 = tasks.peek();  // look at head → "parse"
```

### Deque (double-ended) & Stack (LIFO)

```java
Deque<Integer> dq = new LinkedList<>();
dq.addFirst(1); dq.addLast(2); // [1,2]
dq.addFirst(0);                // [0,1,2]
int head = dq.removeFirst();   // 0

Deque<String> stack = new LinkedList<>();
stack.push("state1");
stack.push("state2");
String undo = stack.pop();     // "state2"
```

> Prefer `offer/poll/peek` over `add/remove/element` to avoid exceptions on empty queues.

---

## Performance and trade-offs

| Operation (n = size)                         | ArrayList | LinkedList |
|---|---:|---:|
| `get(i)` / `set(i)`                          | O(1)      | O(n) |
| Append at end                                | Amortized O(1) | O(1) |
| Insert/remove at **front**                   | O(n)      | O(1) |
| Insert/remove at **middle** (with iterator)  | O(n) to find + O(1) change | O(n) to find + O(1) change |
| Remove while iterating (iterator.remove)     | O(1) change | O(1) change |
| Memory overhead per element                  | lower     | higher (node objects + links) |

**Takeaway:** Linked lists shine when **you’re already there** (you hold an iterator/node) and
then mutate nearby.

---

## Progressive coding steps (A then B then C)

### Step A — Filter *while walking* (keep odd numbers)

```java
var xs = new LinkedList<Integer>();
for (int i = 0; i < 10; i++) xs.add(i);
var it = xs.listIterator();
while (it.hasNext()) if (it.next() % 2 == 0) it.remove();
// Expect: 1,3,5,7,9
```

### Step B — Insert markers between runs (simple run-length marking)

```java
var letters = new LinkedList<Character>();
for (char c : "aaabbcc".toCharArray()) letters.add(c);

var it = letters.listIterator();
char prev = 0;
while (it.hasNext()) {
    char cur = it.next();
    if (prev != 0 && cur != prev) {
        it.previous();    // move back to insertion point
        it.add('|');     // mark boundary by inserting bar character
        it.next();
    }
    prev = cur;
}
// Example output list: a a a | b b | c c
```

### Step C — Double-ended queue for undo/redo

```java
import java.util.Deque;
import java.util.LinkedList;

class Editor {
    private final Deque<String> undo = new LinkedList<>();
    private final Deque<String> redo = new LinkedList<>();
    private String state = "";

    void type(String s){ undo.push(state); state += s; redo.clear(); }
    boolean canUndo(){ return !undo.isEmpty(); }
    boolean canRedo(){ return !redo.isEmpty(); }
    void doUndo(){ if (canUndo()){ redo.push(state); state = undo.pop(); } }
    void doRedo(){ if (canRedo()){ undo.push(state); state = redo.pop(); } }
    String get(){ return state; }
}
```

---

## Useful snippets (guards and helpers)

```java
static <T> T safeHead(java.util.Deque<T> dq) {
    return dq.isEmpty() ? null : dq.peekFirst();
}
```

```java
// Convert List<T> implementation *without* copying references (shallow copy of elements)
static <T> java.util.LinkedList<T> toLinked(java.util.List<T> src) {
    return new java.util.LinkedList<>(src); // O(n) copy of element references
}
```

---

## Games example — Event stream & input buffer

- **Problem:** You receive frequent input events, and sometimes need to **inject** or
  **cancel** the **next** event mid-processing (e.g., a combo move, or pausing).
- **Approach:** Keep a `LinkedList<InputEvent>`; process with a `ListIterator<InputEvent>`.
  When a condition triggers, `it.add(new PauseEvent())` to insert just-in-time; use
  `it.remove()` to cancel the current event.

```java
class InputEvent { final String type; InputEvent(String t){type=t;} }
var queue = new LinkedList<InputEvent>();
queue.add(new InputEvent("Move"));
queue.add(new InputEvent("Attack"));

var it = queue.listIterator();
while (it.hasNext()) {
    InputEvent e = it.next();
    if (e.type.equals("Attack")) it.add(new InputEvent("CameraShake"));
    // handle e...
}
```

## Software Dev example — Job queue with priority buckets (lightweight)

- **Problem:** Background jobs arrive with **low/normal/high** priority.
- **Approach:** Three `Deque<Job>` queues (one `LinkedList` each). Push into the matching
  deque; always poll from the **highest non-empty** deque.

```java
class Job { final String id; Job(String id){this.id=id;} }
java.util.Deque<Job> hi = new LinkedList<>(), mid = new LinkedList<>(), lo = new LinkedList<>();
// enqueue
hi.offer(new Job("reindex")); lo.offer(new Job("email"));
// scheduler
Job next = !hi.isEmpty()? hi.poll() : !mid.isEmpty()? mid.poll() : lo.poll();
```

---

## Debugging and pitfalls

- **Slow random access:** Avoid `list.get(i)` loops with `LinkedList`. Use an iterator.
- **Iterator state errors:** After `add/remove`, call `next/previous` before another `remove/set`.
- **Empty deque operations:** Prefer `offer/poll/peek` (null-returning) over
  `add/remove/element` (throwing).
- **Mixing roles:** If you find yourself using many indexes to consider switching to `ArrayList`.

---

## Reflective questions

- In your own words, when does `LinkedList` beat `ArrayList` for clarity or performance?
- Rewrite a recent **“remove while scanning”** task using `ListIterator`. What changed?
- For an **undo/redo** feature, why might two `Deque`s be simpler than a single list with an index?
- If a method needs **random access** sometimes and **mid-scan mutation** other times, how
  would you design its API to stay flexible?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**When `LinkedList` beats `ArrayList`.**
Honestly: less often than the textbook comparison suggests, and the reason matters more than
the rule.

`ArrayList` stores elements in one contiguous block. Inserting or removing in the middle shifts
everything after it — O(n). `LinkedList` stores each element in its own node with pointers to
its neighbours, so splicing is O(1) *once you are holding the node*.

The catch is that last clause. `linkedList.add(500, x)` must walk 500 nodes to reach the
position first, so it is O(n) anyway — no better than `ArrayList`, and slower in practice
because each node is a separate object scattered across memory, which defeats CPU caching.
`ArrayList`'s "slow" shift is a single bulk memory move and is often faster than walking a
chain.

So `LinkedList` genuinely wins when you are already positioned: **removing or inserting during
a scan via `ListIterator`**, and **queue/deque work at the two ends** (`addFirst`,
`removeFirst`, `addLast`, `removeLast`), all O(1) with no shifting. For clarity rather than
speed, `LinkedList` also wins when you want the *type* to announce intent — declaring
`Deque<Task> queue = new LinkedList<>()` tells a reader the ends are what matter.

**Rewriting a "remove while scanning" with `ListIterator`.**
What changes is *who* does the removing. Instead of the loop asking the list to remove an
element behind the iterator's back — which throws `ConcurrentModificationException` — the
iterator removes it itself and keeps its own bookkeeping consistent:

```java
ListIterator<Task> it = tasks.listIterator();
while (it.hasNext()) {
    Task t = it.next();
    if (t.isDone()) {
        it.remove();              // safe: the iterator knows
    } else if (t.isOverdue()) {
        it.set(t.escalate());     // replace in place - no index arithmetic
    }
}
```

Two things improve beyond correctness: there is no index variable to get wrong, and no
"decrement `i` after removing" trick, which is the classic source of skipped elements.
`ListIterator` also adds `set` and `add`, which a plain `Iterator` does not have, plus
`hasPrevious`/`previous` for walking backwards.

**Why two `Deque`s for undo/redo.**
The operations map exactly onto the data structure, so there is no bookkeeping left to get
wrong. Undo means "take the most recent action" — that is a stack. Redo is a second stack. An
action is pushed to `undo`; undoing pops it from `undo` and pushes it to `redo`; redoing does
the reverse. A new action clears `redo`, which is the correct behaviour and is one line.

The single-list-plus-index alternative has to maintain an invariant *by hand*: everything
before the index is undone, everything after is redone, and a new action must truncate the
tail. Every operation must update the index consistently, and an off-by-one silently corrupts
history. The two-stack version has no index to corrupt — the structure enforces the rule. This
pairs naturally with the Command pattern in
[t13](../t13_design_patterns_1/t13_design_patterns_1_notes.md), where each stack entry is a
`Command` that knows how to undo itself.

**Designing for both random access and mid-scan mutation.**
Declare the parameter as the **most general interface that supports what you actually need**,
and let the caller choose the implementation:

```java
// Needs indexing -> ask for List, not ArrayList
public void promote(List<Task> tasks, int index) { ... }

// Only iterates -> ask for even less
public int countOverdue(Collection<Task> tasks) { ... }
```

Take `List<Task>`, not `ArrayList<Task>`, so an `ArrayList` or a `LinkedList` both work and you
can swap later without touching callers. If a method genuinely needs *both* heavy random access
and heavy mid-scan mutation, that is usually a sign it is doing two jobs — split it into one
method that scans-and-mutates via an iterator and one that indexes, rather than picking a
collection that is mediocre at both.

</details>

## Appendix A — Mini reference (APIs you’ll use most)

**ListIterator**
- `hasNext()/next()` / `hasPrevious()/previous()`
- `add(E e)` • `remove()` • `set(E e)` • `nextIndex()/previousIndex()`

**Deque (on LinkedList)**
- Queue: `offer(e)`, `poll()`, `peek()`
- Ends: `addFirst/Last`, `removeFirst/Last`, `peekFirst/Last`
- Stack: `push(e)`, `pop()`

## Appendix B — Choosing a collection (quick heuristics)

- **Need indexes & random reads?** — `ArrayList`.
- **Mutating while scanning?** — `LinkedList` + `ListIterator`.
- **Uniqueness / membership tests?** — (Next lesson) **Sets**.
- **Key?value lookups?** — (Next lesson) **Maps**.

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t05_collections_1
  domain_emphasis: Balanced

this_lesson:
  topic_code: t06_collections_2
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO3]
```
