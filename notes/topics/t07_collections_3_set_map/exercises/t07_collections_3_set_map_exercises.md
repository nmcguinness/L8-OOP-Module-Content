---
title: "Collections III: Set, Map & PriorityQueue — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t07_collections_3_set_map
description: "Nine exercises on Set, Map and PriorityQueue: uniqueness, word frequency, a ranked leaderboard, grouping, intersection, a least-recently-used cache, fixing broken hashing, TreeMap range lookup and EnumMap counting."
created: 2026-05-29
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, collections, set, map, hashset, hashmap, treemap, treeset, enummap, priorityqueue, exercises, year2, comp-c8z03]
---
# Collections III: Set, Map & PriorityQueue — Exercises

Each exercise has a worked solution in `code/src/t07_collections_3_set_map/exercises/exNN/` and JUnit tests in `code/test/t07_collections_3_set_map/exercises/exNN/`. Read the tests before you start: their names state the exact contract, including the edge cases. Try your own version first, then run the tests against it.

| Exercise | Main collection | Difficulty |
| :-- | :-- | :-- |
| 01 Unique visitors | `HashSet` | Foundation |
| 02 Word frequency | `HashMap` with `merge` | Foundation |
| 03 Ranked leaderboard | `PriorityQueue` with a comparator | Intermediate |
| 04 Group by first letter | `TreeMap` with `computeIfAbsent` | Foundation |
| 05 Set intersection | `HashSet` with `retainAll` | Foundation |
| 06 Least-recently-used cache | `LinkedHashMap` | Extension |
| 07 Fix the broken set | your own `equals` and `hashCode` | Intermediate |
| 08 Grade bands | `TreeMap.floorEntry` | Intermediate |
| 09 Loot by rarity | `EnumMap` | Foundation |

---

## Exercise 01 — Unique visitors

A web server log contains one `String` per entry, in the format `"timestamp,ipAddress"`. Write a method:

```java
public static int countUniqueVisitors(List<String> logEntries)
```

that returns the number of **distinct IP addresses** seen across all entries.

**Rules:**

- Skip `null` entries, entries with no comma, and entries with nothing after the comma.
- Throw `IllegalArgumentException` if `logEntries` is `null`.

**Example:**

```text
Input:  ["09:00,10.0.0.1", "09:01,10.0.0.2", "09:02,10.0.0.1", "malformed line"]
Output: 2
```

**Hint:** `Set.add` ignores duplicates for you, so the answer is the size of the set at the end.

**Package:** `t07_collections_3_set_map.exercises.ex01`

**Solution:** [t07 exercise 01 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex01/). **Tests:** [t07 exercise 01 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex01/).

---

## Exercise 02 — Word frequency

Write a method:

```java
public static Map<String, Integer> wordFrequency(String sentence)
```

that splits `sentence` on whitespace, lowercases each word, and returns a map of word to count.

**Rules:**

- Treat any run of spaces or tabs as one separator, and ignore leading and trailing spaces.
- Return an empty map for a `null` or blank sentence.

**Example:**

```text
Input:  "The cat saw the other cat"
Output: {the=2, cat=2, saw=1, other=1}   (in any order)
```

**Hints:**

- `sentence.trim().split("\\s+")` splits on one or more whitespace characters.
- `map.merge(word, 1, Integer::sum)` does "insert 1 or add 1" in one call.

**Package:** `t07_collections_3_set_map.exercises.ex02`

**Solution:** [t07 exercise 02 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex02/). **Tests:** [t07 exercise 02 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex02/).

---

## Exercise 03 — Ranked leaderboard

Given a `Map<String, Integer>` of player names to scores, write a method:

```java
public static List<String> leaderboard(Map<String, Integer> scores)
```

that returns player names sorted **highest score first** using a `PriorityQueue`.

**Rules:**

- Players with equal scores are ordered by name, A to Z, so the result is the same every run.
- The comparator must work for any `int` score, including `Integer.MIN_VALUE` and `Integer.MAX_VALUE`.
- Return an empty list for an empty map. Throw `IllegalArgumentException` for `null`.

**Example:**

```text
Input:  {Ana=80, Ben=95, Cara=80, Dev=60}
Output: [Ben, Ana, Cara, Dev]
```

**Hints:**

- Store `Map.Entry<String, Integer>` objects in the queue, then `poll()` until it is empty.
- Do not use `b.getValue() - a.getValue()`. One of the tests is there to catch the overflow.
- `Map.Entry.comparingByValue(...)` and `Map.Entry.comparingByKey()` build the two halves.

**Package:** `t07_collections_3_set_map.exercises.ex03`

**Solution:** [t07 exercise 03 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex03/). **Tests:** [t07 exercise 03 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex03/).

---

## Exercise 04 — Group by first letter

Write a method:

```java
public static Map<Character, List<String>> groupByFirstLetter(List<String> words)
```

that groups words by their first character. `Map<Character, List<String>>` here means a map from a letter to the list of words that start with it.

**Rules:**

- Use the **lowercase** first letter as the key, so `"Plum"` and `"pear"` share key `'p'`.
- Keep each word exactly as given, in its original order within its group.
- The returned map has its keys in **alphabetical order**.
- Skip `null` and empty words. Return an empty map for a `null` list.

**Example:**

```text
Input:  ["pear", "apple", "Plum", "banana", "avocado"]
Output: {a=[apple, avocado], b=[banana], p=[pear, Plum]}
```

**Hint:** which `Map` implementation keeps keys sorted for you?

**Package:** `t07_collections_3_set_map.exercises.ex04`

**Solution:** [t07 exercise 04 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex04/). **Tests:** [t07 exercise 04 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex04/).

---

## Exercise 05 — Set intersection

Write a method:

```java
public static Set<Integer> intersection(List<Integer> a, List<Integer> b)
```

that returns the elements present in **both** lists, with no duplicates.

**Rules:**

- Do not modify `a` or `b`.
- Throw `IllegalArgumentException` if either list is `null`.

**Example:**

```text
Input:  a = [1, 2, 2, 3, 4], b = [2, 4, 4, 6]
Output: {2, 4}
```

**Hint:** `retainAll` changes the collection you call it on. Call it on a copy.

**Package:** `t07_collections_3_set_map.exercises.ex05`

**Solution:** [t07 exercise 05 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex05/). **Tests:** [t07 exercise 05 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex05/).

---

## Exercise 06 — Least-recently-used cache (extension)

Implement a `SimpleCache` class that maps `String` keys to `String` values and holds at most a fixed number of entries. When it is full, it evicts the **least recently used** entry: the one that has gone longest without being read or written.

**Required API:**

```java
public SimpleCache(int capacity)
public void put(String key, String value)
public Optional<String> get(String key)
public int size()
public boolean containsKey(String key)
```

**Rules:**

- Both `put` and `get` count as a use of that key.
- `put` on a key that is already present replaces its value and **never** evicts anything.
- `get` on a missing key returns `Optional.empty()`; it never returns `null`.
- The constructor throws `IllegalArgumentException` if `capacity` is less than 1.
- `put` throws `IllegalArgumentException` for a `null` key.

**Example (capacity 2):**

```text
put("a", "apple")
put("b", "banana")
get("a")           -> Optional[apple]   ("b" is now the least recently used)
put("c", "cherry") -> evicts "b"
containsKey("b")   -> false
```

**Hints:**

- Back the cache with a `LinkedHashMap`. Its first key, from `keySet().iterator().next()`, is the oldest.
- To mark a key as recently used, `remove` it and `put` it back; it moves to the end.

> **Going further, after t08 Inheritance:** `LinkedHashMap` can do this for you. Construct it in access-order mode, `new LinkedHashMap<>(16, 0.75f, true)`, and override its `removeEldestEntry` method to return `size() > capacity`.

**Package:** `t07_collections_3_set_map.exercises.ex06`

**Solution:** [t07 exercise 06 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex06/). **Tests:** [t07 exercise 06 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex06/).

---

## Exercise 07 — Fix the broken set

A tile-based game records every cell a unit visits as a `GridPos`. A teammate wrote this class:

```java
public final class GridPos {
    private final int row;
    private final int col;

    public GridPos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GridPos other)) return false;
        return row == other.row && col == other.col;
    }
}
```

**Tasks:**

1. **Predict.** Before running anything, write down what this code prints, and why:

   ```java
   Set<GridPos> visited = new HashSet<>();
   visited.add(new GridPos(0, 0));
   visited.add(new GridPos(0, 0));
   System.out.println(visited.size());
   System.out.println(visited.contains(new GridPos(0, 0)));
   ```

2. **Fix** `GridPos` so equal positions behave as one element in a `HashSet`. Add `row()`, `col()` and a `toString()` that returns `"(row,col)"`.
3. **Write** `public static int countDistinct(List<GridPos> visited)` that returns the number of distinct cells. Throw `IllegalArgumentException` for `null`.
4. **Explain** in one sentence why the fields being `final` matters for a class used as a set element or map key.

**Hint:** `Objects.hash(row, col)` builds a hash code from the same fields as `equals`.

**Package:** `t07_collections_3_set_map.exercises.ex07`

**Solution:** [t07 exercise 07 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex07/). **Tests:** [t07 exercise 07 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex07/).

---

## Exercise 08 — Grade bands

Write a method:

```java
public static String band(int score)
```

that converts a score from 0 to 100 into a grade band using this table:

| Lowest score | Band |
| :-- | :-- |
| 0 | F |
| 40 | D |
| 55 | C |
| 70 | B |
| 85 | A |

**Rules:**

- Store the table in a `TreeMap<Integer, String>`. Do **not** use an `if` or `switch` chain.
- Throw `IllegalArgumentException` for a score below 0 or above 100.

**Example:**

```text
band(39) -> F     band(40) -> D     band(84) -> B     band(100) -> A
```

**Hint:** `floorEntry(score)` returns the entry with the greatest key that is less than or equal to `score`.

**Package:** `t07_collections_3_set_map.exercises.ex08`

**Solution:** [t07 exercise 08 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex08/). **Tests:** [t07 exercise 08 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex08/).

---

## Exercise 09 — Loot by rarity

Given this enum:

```java
public enum Rarity { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY }
```

write a method:

```java
public static Map<Rarity, Integer> countByRarity(List<Rarity> drops)
```

that counts how many drops there were of each rarity.

**Rules:**

- Return an `EnumMap`.
- **Every** rarity appears as a key, with a count of `0` if it never dropped.
- Keys iterate in declaration order, `COMMON` first.
- Skip `null` entries. A `null` list gives all counts `0`.

**Example:**

```text
Input:  [COMMON, RARE, COMMON, LEGENDARY, COMMON]
Output: {COMMON=3, UNCOMMON=0, RARE=1, EPIC=0, LEGENDARY=1}
```

**Hint:** `Rarity.values()` returns every constant, in order.

**Package:** `t07_collections_3_set_map.exercises.ex09`

**Solution:** [t07 exercise 09 solution code](../../../../code/src/t07_collections_3_set_map/exercises/ex09/). **Tests:** [t07 exercise 09 tests](../../../../code/test/t07_collections_3_set_map/exercises/ex09/).

