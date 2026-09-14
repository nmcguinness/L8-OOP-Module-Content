---
title: "Collections III: Set, Map & PriorityQueue"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t07_collections_3_set_map
description: "HashSet, LinkedHashSet, TreeSet, HashMap, LinkedHashMap, TreeMap, and PriorityQueue — when uniqueness and keyed lookup matter more than ordered indexing."
created: 2026-05-27
last_updated: 2026-05-27
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, collections, set, map, hashset, hashmap, treemap, treeset, priorityqueue, year2, comp-c8z03]
difficulty_tier: Foundation
mlos: [MLO3]
previous_topic: t06_collections_2
prerequisites:
  - Collections I: ArrayList (add, remove, contains, iteration)
  - Collections II: LinkedList and queues
  - Equality and hashing (equals/hashCode contract)
---

# Collections III: Set, Map & PriorityQueue

> **Prerequisites:**
> - Collections I: `ArrayList` — add, remove, contains, index-based access
> - Collections II: `LinkedList` as a list and queue
> - Equality and hashing: why `equals()` and `hashCode()` must agree, what modulo hashing does

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Part 1: Set — unique elements only](#part-1-set--unique-elements-only)
- [Core Ideas 1: HashSet](#core-ideas-1-hashset)
- [Core Ideas 2: LinkedHashSet and TreeSet](#core-ideas-2-linkedhashset-and-treeset)
- [Set operations (union, intersection, difference)](#set-operations-union-intersection-difference)
- [Part 2: Map — keyed lookup](#part-2-map--keyed-lookup)
- [Core Ideas 3: HashMap](#core-ideas-3-hashmap)
- [Core Ideas 4: compute patterns](#core-ideas-4-compute-patterns)
- [Part 3: PriorityQueue — ordered processing](#part-3-priorityqueue--ordered-processing)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Games example: entity registry and ability
  tracking](#games-example-entity-registry-and-ability-tracking)
- [Software example: caching and category grouping](#software-example-caching-and-category-grouping)
- [Performance summary](#performance-summary)
- [Common mistakes](#common-mistakes)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

A quick summary of what **you** should be able to do after this lesson:

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Explain why `Set` and `Map` require a correct `equals`/`hashCode` implementation. |
| Use | Choose between `HashSet`, `LinkedHashSet`, and `TreeSet` for a given problem. |
| Use | Store, look up, and remove entries in `HashMap`, `LinkedHashMap`, and `TreeMap`. |
| Use | Drain a `PriorityQueue` in priority order using `poll()`. |
| Analyse | Compare the time complexity of key operations across `List`, `Set`, and `Map`. |
| Debug | Identify broken uniqueness caused by a missing or incorrect `equals`/`hashCode`. |

---

## Why this matters

`ArrayList` is excellent when you need **ordered, indexed access**. But two other access
patterns are just as common:

- **"Is X already in this collection?"** — fast membership check without scanning every element.
- **"Given this key, what is the value?"** — keyed lookup (like a lookup table or dictionary).

`Set` and `Map` are the Java answers to these questions. Both rely on hashing — which is why
the equality and hashing topic comes first.

---

## How this builds on previous content

- `equals()` and `hashCode()` determine where an element lands in a `HashSet` or `HashMap`.
- Correct contracts mean `contains(x)` finds an element that is **equal** to `x`, not just
  identical.
- `Comparable`/`Comparator` (from [t03 Ordering](../t03_ordering/t03_ordering_notes.md)) drives
  the ordering in `TreeSet` and `TreeMap`.

---

## Part 1: Set — unique elements only

A `Set<E>` is a collection that **cannot contain duplicates**. It has no index-based access —
you cannot call `get(i)`.

### Three Set implementations

| Implementation | Order | Performance | When to use |
| :-- | :-- | :-- | :-- |
| `HashSet<E>` | None (arbitrary) | O(1) add/remove/contains | Default choice for uniqueness |
| `LinkedHashSet<E>` | Insertion order | O(1) with slight overhead | When insertion order matters |
| `TreeSet<E>` | Natural / Comparator | O(log n) | When sorted order is required |

---

## Core Ideas 1: HashSet

```java
import java.util.HashSet;
import java.util.Set;

Set<String> tags = new HashSet<>();

tags.add("java");         // returns true — added
tags.add("oop");          // returns true — added
tags.add("java");         // returns false — duplicate, ignored

System.out.println(tags.size());         // 2
System.out.println(tags.contains("oop")); // true

tags.remove("oop");
System.out.println(tags.contains("oop")); // false

// Iteration — order not guaranteed
for (String tag : tags) {
    System.out.println(tag);
}
```

> **Warning:** `HashSet` requires correct `equals()` and `hashCode()` on your objects. Without
> them, two logically equal objects are stored as separate entries.

---

## Core Ideas 2: LinkedHashSet and TreeSet

```java
import java.util.LinkedHashSet;
import java.util.TreeSet;

// LinkedHashSet — preserves insertion order
LinkedHashSet<String> ordered = new LinkedHashSet<>();
ordered.add("banana");
ordered.add("apple");
ordered.add("cherry");
ordered.add("apple");          // duplicate ignored
System.out.println(ordered);   // [banana, apple, cherry]

// TreeSet — sorted order (natural ordering via Comparable)
TreeSet<String> sorted = new TreeSet<>();
sorted.add("banana");
sorted.add("apple");
sorted.add("cherry");
System.out.println(sorted);    // [apple, banana, cherry]
System.out.println(sorted.first());  // apple
System.out.println(sorted.last());   // cherry

// TreeSet with custom Comparator
TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
byLength.add("cat");
byLength.add("elephant");
byLength.add("ox");
System.out.println(byLength);  // [ox, cat, elephant]
```

---

## Set operations (union, intersection, difference)

```java
Set<String> a = new HashSet<>(Set.of("x", "y", "z"));
Set<String> b = new HashSet<>(Set.of("y", "z", "w"));

// Union
Set<String> union = new HashSet<>(a);
union.addAll(b);              // {x, y, z, w}

// Intersection
Set<String> intersection = new HashSet<>(a);
intersection.retainAll(b);   // {y, z}

// Difference (a minus b)
Set<String> diff = new HashSet<>(a);
diff.removeAll(b);           // {x}
```

---

## Part 2: Map — keyed lookup

A `Map<K, V>` stores **key–value pairs**. Each key maps to exactly one value. Keys are unique;
values may repeat.

### Three Map implementations

| Implementation | Key order | Performance | When to use |
| :-- | :-- | :-- | :-- |
| `HashMap<K,V>` | None | O(1) get/put/remove | Default — fastest lookup |
| `LinkedHashMap<K,V>` | Insertion order | O(1) with overhead | When insertion order of keys matters |
| `TreeMap<K,V>` | Natural / Comparator | O(log n) | When sorted key iteration is needed |

---

## Core Ideas 3: HashMap

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Integer> scores = new HashMap<>();

// Store
scores.put("Alice", 95);
scores.put("Ben", 80);
scores.put("Clara", 92);

// Retrieve
int aliceScore = scores.get("Alice");   // 95
int unknown = scores.getOrDefault("Dave", 0); // 0 — safe default

// Check
System.out.println(scores.containsKey("Ben"));    // true
System.out.println(scores.containsValue(80));     // true

// Update
scores.put("Ben", 85);    // replaces old value

// Remove
scores.remove("Clara");

// Size
System.out.println(scores.size());  // 2

// Iterate entries
for (Map.Entry<String, Integer> entry : scores.entrySet()) {
    System.out.println(entry.getKey() + " -> " + entry.getValue());
}

// Keys only / values only
for (String key : scores.keySet()) { System.out.println(key); }
for (int val : scores.values()) { System.out.println(val); }
```

---

## Core Ideas 4: compute patterns

```java
Map<String, Integer> wordCount = new HashMap<>();
String[] words = {"cat", "dog", "cat", "bird", "dog", "cat"};

for (String w : words) {
    // If key absent, put 0, then add 1
    wordCount.put(w, wordCount.getOrDefault(w, 0) + 1);
}
System.out.println(wordCount); // {cat=3, dog=2, bird=1}

// Equivalent using merge
Map<String, Integer> count2 = new HashMap<>();
for (String w : words) {
    count2.merge(w, 1, Integer::sum);
}

// computeIfAbsent — initialise only when key is missing
Map<String, List<String>> groups = new HashMap<>();
groups.computeIfAbsent("even", k -> new ArrayList<>()).add("2");
groups.computeIfAbsent("even", k -> new ArrayList<>()).add("4");
groups.computeIfAbsent("odd", k -> new ArrayList<>()).add("1");
System.out.println(groups); // {even=[2, 4], odd=[1]}
```

---

## Part 3: PriorityQueue — ordered processing

`PriorityQueue<E>` is a **min-heap by default**: `poll()` always returns the smallest element
according to natural ordering or a supplied `Comparator`. It does **not** implement `List` — no
random access.

```java
import java.util.PriorityQueue;

PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.add(30);
pq.add(10);
pq.add(20);

System.out.println(pq.peek());   // 10 — inspect without removing
System.out.println(pq.poll());   // 10 — removes smallest
System.out.println(pq.poll());   // 20
System.out.println(pq.poll());   // 30
```

```java
// Max-heap via reverse comparator
PriorityQueue<Integer> maxPQ = new PriorityQueue<>(Comparator.reverseOrder());
maxPQ.add(30);
maxPQ.add(10);
maxPQ.add(20);
System.out.println(maxPQ.poll()); // 30
```

```java
// Custom priority on objects
record Task(String name, int priority) {}
PriorityQueue<Task> tasks = new PriorityQueue<>(
    Comparator.comparingInt(Task::priority)
);
tasks.add(new Task("backup", 3));
tasks.add(new Task("alert", 1));
tasks.add(new Task("report", 2));

while (!tasks.isEmpty()) {
    Task t = tasks.poll();
    System.out.println(t.name()); // alert, report, backup
}
```

---

## Progressive coding steps (A then B then C)

### Step A — De-duplication with HashSet

```java
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public static List<String> deduplicate(List<String> input) {
    Set<String> seen = new HashSet<>();
    List<String> result = new ArrayList<>();
    for (String s : input) {
        if (seen.add(s)) {      // add() returns true if element was new
            result.add(s);
        }
    }
    return result;
}
```

*`seen.add(s)` returns `false` if the element was already present — a useful idiom.*

---

### Step B — Word frequency with HashMap

```java
import java.util.HashMap;
import java.util.Map;

public static Map<String, Integer> frequency(String[] words) {
    Map<String, Integer> freq = new HashMap<>();
    for (String w : words) {
        freq.merge(w.toLowerCase(), 1, Integer::sum);
    }
    return freq;
}
```

---

### Step C — Top-N items with PriorityQueue

```java
import java.util.Map;
import java.util.PriorityQueue;

// Return the 3 most frequent words
public static List<String> topN(Map<String, Integer> freq, int n) {
    PriorityQueue<Map.Entry<String, Integer>> pq =
        new PriorityQueue<>((a, b) -> b.getValue() - a.getValue()); // max-heap
    pq.addAll(freq.entrySet());
    List<String> result = new ArrayList<>();
    for (int i = 0; i < n && !pq.isEmpty(); i++) {
        result.add(pq.poll().getKey());
    }
    return result;
}
```

---

## Games example: entity registry and ability tracking

```java
import java.util.*;

// Track which entity IDs are currently alive (fast membership check)
public class EntityRegistry {

    private final Set<Integer> _aliveIds = new HashSet<>();
    private final Map<Integer, String> _names = new HashMap<>();

    public void spawn(int id, String name) {
        _aliveIds.add(id);
        _names.put(id, name);
    }

    public void despawn(int id) {
        _aliveIds.remove(id);
        _names.remove(id);
    }

    public boolean isAlive(int id) {
        return _aliveIds.contains(id);   // O(1)
    }

    public String getName(int id) {
        return _names.getOrDefault(id, "Unknown");
    }

    public Set<Integer> aliveIds() {
        return Collections.unmodifiableSet(_aliveIds);
    }
}
```

```java
// Priority queue for enemy turn order (lower initiative = goes first)
public class TurnQueue {

    private record Combatant(String name, int initiative) {}

    private final PriorityQueue<Combatant> _queue =
        new PriorityQueue<>(Comparator.comparingInt(Combatant::initiative));

    public void add(String name, int initiative) {
        _queue.add(new Combatant(name, initiative));
    }

    public String nextTurn() {
        Combatant c = _queue.poll();
        return c != null ? c.name() : null;
    }
}
```

---

## Software example: caching and category grouping

```java
import java.util.*;

// Simple in-memory cache with insertion-order eviction potential
public class ResponseCache {

    private final Map<String, String> _cache = new LinkedHashMap<>();
    private final int _capacity;

    public ResponseCache(int capacity) {
        _capacity = capacity;
    }

    public void store(String url, String response) {
        if (_cache.size() >= _capacity) {
            // Remove oldest entry (first key in insertion order)
            _cache.remove(_cache.keySet().iterator().next());
        }
        _cache.put(url, response);
    }

    public Optional<String> get(String url) {
        return Optional.ofNullable(_cache.get(url));
    }
}
```

```java
// Group tasks by category
public class TaskGrouper {

    public Map<String, List<String>> groupByCategory(List<String[]> tasks) {
        Map<String, List<String>> groups = new TreeMap<>(); // sorted keys
        for (String[] task : tasks) {
            String category = task[0];
            String title = task[1];
            groups.computeIfAbsent(category, k -> new ArrayList<>()).add(title);
        }
        return groups;
    }
}
```

---

## Performance summary

**Cost**

| Operation | ArrayList | Hashed (HashSet / HashMap) |
| :-- | :-- | :-- |
| Access by index | O(1) | Not supported |
| Contains / lookup | O(n) | **O(1)** |
| Add / insert | O(1) amortised | **O(1)** |

| Operation | Sorted (TreeSet / TreeMap) |
| :-- | :-- |
| Contains / lookup | O(log n) |
| Add / insert | O(log n) |
| Access by index | Not supported |

**Behaviour**

| Structure | Ordered iteration | Duplicates allowed |
| :-- | :-- | :-- |
| ArrayList | Yes, by index | Yes |
| HashSet | No | No |
| HashMap | No, keys unordered | Keys: no |
| TreeSet / TreeMap | **Yes, sorted** | No |

---

## Common mistakes

| Pitfall | Why it breaks | Fix |
| :-- | :-- | :-- |
| Using mutable key in `HashMap` and mutating it | Hash changes — entry becomes unreachable | Use immutable keys (String, Integer, records) |
| Missing `hashCode()` when overriding `equals()` | Logically equal objects land in different buckets | Always override both together |
| Calling `get(key)` without null check | Returns `null` for missing keys — NPE later | Use `getOrDefault` or `containsKey` first |
| Modifying a `Set`/`Map` while iterating | `ConcurrentModificationException` | Collect keys to remove first, or use `Iterator.remove()` |
| Expecting `TreeSet` to sort by insertion | `TreeSet` sorts by element order, not insertion | Use `LinkedHashSet` for insertion order |

---

## Reflective questions

- Why does `HashSet` require `equals()` and `hashCode()` but `ArrayList` does not?
- You have 1 million strings and need to check membership millions of times. Which collection
  do you choose and why?
- When would you prefer `TreeMap` over `HashMap`?
- What happens if two objects have the same `hashCode()` but are not equal?
- How would you implement a "seen before" filter in a stream of incoming messages?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**1. Why `HashSet` needs `equals`/`hashCode` but `ArrayList` does not.**
They answer different questions. `ArrayList` only has to *store in order*; it never needs to
know whether two elements mean the same thing, so `add` never compares anything. (Its
`contains` and `remove` *do* call `equals` — but they are a linear scan, and if you never call
them the methods are irrelevant.)

`HashSet` exists precisely to answer "is this already here?", and it must answer in O(1). It
does that by using `hashCode()` to pick a bucket and `equals()` to compare within it. Without
correct implementations it cannot do its one job — it will happily store two objects you
consider identical.

**2. One million strings, millions of membership checks.**
`HashSet<String>`. Membership is O(1) average, versus O(n) for a `List`, so a million lookups
over a million items goes from ~10¹² comparisons to ~10⁶ — the difference between hours and
milliseconds. `String` already has well-distributed `hashCode`/`equals`, so it works out of the
box. Choose `HashSet` over `TreeSet` here because you need *membership*, not order: `TreeSet`
is O(log n) and only earns that cost if you also want sorted iteration or range queries.

**3. When `TreeMap` beats `HashMap`.**
When you need the keys **in order**, not just present. `TreeMap` keeps keys sorted (by
`Comparable` or a supplied `Comparator`), so iteration is ordered and you get navigation
methods `HashMap` cannot offer: `firstKey`, `lastKey`, `headMap`, `tailMap`, `subMap`,
`floorKey`, `ceilingKey`. Reach for it for date-range queries, leaderboards printed in rank
order, or "the largest key ≤ x". The price is O(log n) instead of O(1) and a requirement that
keys be orderable. If you only need *insertion* order rather than sorted order, `LinkedHashMap`
is cheaper than both.

**4. Same `hashCode()`, not equal.**
That is a **collision**, and it is entirely legal — `hashCode` returns one of ~4 billion ints
for an unlimited number of objects, so collisions are unavoidable. Both objects land in the
same bucket, and the set uses `equals()` to distinguish them, keeping both. Correctness is
unaffected; only speed suffers, because that bucket now needs a short scan. Java handles heavy
collisions by converting a bucket to a balanced tree.

The contract runs in one direction only: **equal objects must have equal hash codes**, but
equal hash codes do *not* imply equality. Getting that backwards is the classic exam error. The
genuinely broken case is `hashCode()` returning a constant — legal, but it puts everything in
one bucket and silently degrades your O(1) map to an O(n) list.

**5. A "seen before" filter.**
A `HashSet` of identifiers, relying on the fact that `add` returns `false` when the element was
already present — so you test and record in one operation:

```java
private final Set<String> _seen = new HashSet<>();

// Returns: true the first time this id arrives, false for every repeat
public boolean isNew(String messageId) {
    return _seen.add(messageId);
}
```

Key on a stable identifier (a message ID), not on the whole message object, unless that object
has a correct `equals`/`hashCode`. Two practical cautions: the set grows without bound on a
long-running server, so cap it — a `LinkedHashMap` in access-order mode with
`removeEldestEntry` gives you a fixed-size LRU, which is exactly the extension exercise in this
topic. And if several threads consume the stream, plain `HashSet` is not thread-safe: use
`ConcurrentHashMap.newKeySet()`. That concern reappears in [t19
Concurrency](../t19_concurrency/t19_concurrency_notes.md).

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t06_collections_2
  domain_emphasis: Balanced

this_lesson:
  topic_code: t07_collections_3_set_map
  primary_domain_emphasis: Balanced
  difficulty_tier: Foundation
mlos: [MLO3]
```
