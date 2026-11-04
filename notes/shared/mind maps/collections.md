# Collections — Choosing the Right One

A cross-topic revision aid. The per-topic mindmaps cover each collection in detail — [t05 ArrayList](revision_t00_t09.md#t05--collections-i-arraylist), [t06 LinkedList](revision_t00_t09.md#t06--collections-ii-linkedlist-and-iterators) and [t07 Set, Map and PriorityQueue](revision_t00_t09.md#t07--collections-iii-set-map-and-priorityqueue). This page answers the question that spans all three: **which one do I actually pick?**

> [!IMPORTANT]
> This revision document is not a substitute for reading, understanding, and learning the content covered in the related notes.

---

## The decision in one picture

```mermaid
%%{init: { "theme": "forest" }}%%
flowchart TD
    accTitle: Choosing a Java collection
    accDescr: A decision tree. First ask whether you need lookup by key: if so choose TreeMap for sorted keys, LinkedHashMap for insertion order, otherwise HashMap. If not, ask whether elements must be unique: if so choose TreeSet, LinkedHashSet or HashSet by the order you need. If neither, choose PriorityQueue for smallest-first processing, a Deque for work at the ends, or ArrayList otherwise.
    A[What do<br>you need?] --> B{Lookup by key?}
    B -->|Yes| C{Need keys<br>in sorted order?}
    C -->|Yes| D["TreeMap<br/>O(log n),<br>range queries"]
    C -->|No| E{Need<br>insertion order?}
    E -->|Yes| F["LinkedHashMap<br/>predictable<br>iteration"]
    E -->|No| G["HashMap<br/>O(1) average<br>is the default"]

    B -->|No| H{Must elements<br>be unique?}
    H -->|Yes| I{Need an order?}
    I -->|Sorted| J["TreeSet<br/>O(log n)"]
    I -->|Insertion| K["LinkedHashSet"]
    I -->|Don't care| L["HashSet<br/>O(1) average"]

    H -->|No| M{Always process<br>smallest first?}
    M -->|Yes| N["PriorityQueue<br/>poll returns smallest"]
    M -->|No| O{Work mainly<br>at the ends?}
    O -->|Yes| P["ArrayDeque / LinkedList<br/>as a Deque"]
    O -->|No| Q["ArrayList<br/>the default List"]
    classDef startQ fill:#EEF0F3,stroke:#94A3B8,color:#1F2937
    class A,B,H startQ
    classDef mapsQ fill:#EEF5FC,stroke:#7FA8D1,color:#1F2937
    classDef mapsA fill:#D6E6F7,stroke:#7FA8D1,color:#1F2937
    class C,E mapsQ
    class D,F,G mapsA
    classDef setsQ fill:#EDF7F0,stroke:#7DB591,color:#1F2937
    classDef setsA fill:#D3ECDB,stroke:#7DB591,color:#1F2937
    class I setsQ
    class J,K,L setsA
    classDef listsQ fill:#FDF3EA,stroke:#D9A06F,color:#1F2937
    classDef listsA fill:#FAE0CA,stroke:#D9A06F,color:#1F2937
    class M,O listsQ
    class N,P,Q listsA
    linkStyle default stroke:#64748B,stroke-width:1.5px
```

---

## The shape of the whole family

```mermaid
---
title: "Collections"
accDescr: "Mind map of Collections. Branches: List ordered allows duplicates; Set unique elements; Map key to value; Queue and Deque; What drives the choice."
---
%%{init: { "theme": "forest", "markdownAutoWrap": false, "mindmap": { "maxNodeWidth": 300 } }}%%
mindmap
  root((Collections))
    What drives the choice
      n10["`Uniqueness needed
      choose a Set`"]
      n11["`Lookup by key
      choose a Map`"]
      n12["`Order matters choose
      Tree or LinkedHash`"]
      Otherwise ArrayList

    Set unique elements
      HashSet fastest no order
      n2["`LinkedHashSet keeps
      insertion order`"]
      n3["`TreeSet sorted
      costs log n`"]
      n4["`Needs equals
      and hashCode`"]

    n1["`List ordered
    allows duplicates`"]
      ArrayList the default
        Contiguous memory
        Fast get by index
        Shifts on middle insert
      LinkedList
        Fast at the two ends
        Slow to reach an index
        Use as a Deque

    Queue and Deque
      n8["`ArrayDeque for
      stack or queue`"]
      n9["`PriorityQueue
      smallest first`"]
      push pop for LIFO
      offer poll for FIFO

    Map key to value
      HashMap the default
      n5["`LinkedHashMap
      insertion order`"]
      n6["`TreeMap sorted
      keys and ranges`"]
      n7["`Keys unique
      values may repeat`"]
```

## Cost at a glance

Averages, and the reason each one matters more than the number itself.

**The two List types**

| Operation | ArrayList | LinkedList |
|:--|:--|:--|
| Get by index | **O(1)** | O(n) |
| Add at end | O(1)* | O(1) |
| Add / remove at start | O(n) | **O(1)** |
| Insert / remove in middle | O(n) | O(n)† |
| `contains` / lookup | O(n) | O(n) |

**Hashed versus sorted**

| Operation | HashSet / HashMap | TreeSet / TreeMap |
|:--|:--|:--|
| `contains` / lookup | **O(1)** | O(log n) |
| Iterate in sorted order | O(n log n) | **O(n)** |
| Get by index | Not supported | Not supported |

\* Amortised — resizing copies to a bigger array, but rarely. † O(1) *once you are positioned* with a `ListIterator`; reaching the position is the O(n) part. This is why `LinkedList` wins far less often than its reputation suggests.

---

## Choices that trip people up

**"I need fast membership tests"** — `HashSet`, not `List`. `list.contains(x)` is a linear scan, so 10 000 lookups over a 10 000-element list is 100 million comparisons. A `HashSet` makes each lookup O(1).

**"I need uniqueness *and* insertion order"** — `LinkedHashSet`. Reaching for a `List` plus a manual duplicate check re-introduces the O(n) scan you were avoiding.

**"I printed my `PriorityQueue` and it isn't sorted"** — it is not broken. A `PriorityQueue` guarantees only that **`poll()`** returns the smallest remaining element. Its internal heap is partially ordered, so iteration and `toString()` look wrong.

**"My `HashSet` contains duplicates"** — the element type overrides `equals` but not `hashCode`, or overloads `equals(MyType)` instead of overriding `equals(Object)`. See [t04](revision_t00_t09.md#t04--equality-and-hashing).

**"`contains` returns false for an object I definitely added"** — a field used by `hashCode` was mutated after insertion, so the object now sits in the wrong bucket. Make key fields final.

**"`ConcurrentModificationException` while removing"** — you removed through the collection during a `for-each`. Use `removeIf`, or an explicit `Iterator` and call `it.remove()`.

---

## Declaring them well

```java
// Declare the interface, instantiate the implementation.
// Callers depend on List, so the implementation can change freely.
List<Task> tasks = new ArrayList<>();
Set<String> seen = new HashSet<>();
Map<String, Integer> counts = new HashMap<>();
Deque<Command> undo = new ArrayDeque<>();

// Ask for the least specific type that supports what you need
public int countOverdue(Collection<Task> tasks) { ... }   // only iterates
public void promote(List<Task> tasks, int index) { ... }  // needs indexing
```

---

## Self-Assessment Prompts

1. **You need uniqueness, insertion order, and fast membership tests. Which collection?** *(Two of the three rule out most options)*

2. **When does `LinkedList` genuinely beat `ArrayList`, and why is "frequent middle insertion" not the answer?** *(What has to happen before the O(1) splice can occur?)*

3. **What does `HashMap` need from a key type, and what must be true of those fields afterwards?**
   *(Two method overrides, and one property)*

4. **`TreeMap` costs O(log n) against `HashMap`'s O(1). What do you get for that?** *(Name two operations `HashMap` cannot offer at all)*

5. **Why declare `List<Task> tasks` rather than `ArrayList<Task> tasks`?** *(What can you change later in each case?)*
