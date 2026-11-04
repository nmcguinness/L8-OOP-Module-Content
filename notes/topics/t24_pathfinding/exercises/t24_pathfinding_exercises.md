---
title: "Pathfinding — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t24_pathfinding
description: "Eight step-by-step exercises building breadth-first search, Dijkstra and A* over a grid, with a listener seam, a console visualisation, heuristic comparison, and one extension."
created: 2026-09-13
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, algorithms, pathfinding, bfs, dijkstra, a-star, priorityqueue, heuristics, exercises, year2, comp-c8z03]
---

# Pathfinding — Exercises

These exercises build one search engine in eight steps, following the [t24 notes](../t24_pathfinding_notes.md). Exercises 01–02 prepare the ground, 03–05 are the three algorithms, 06–07 make the search watchable and measurable, and 08 extends it. This topic is supplementary: work through it if you want a larger program that uses the collections, interfaces and testing from earlier topics.

Everything you need is in `code/src/t24_pathfinding/common/`. `Grid`, `Cell`, `Direction`, `Heuristic`, `Maps`, `SearchListener` and `SearchResult` are given. **You write the algorithms.** The worked versions of the three algorithms are in the same folder. Try each exercise before you read them: these algorithms are short enough that reading one feels like understanding it, and that feeling is unreliable.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution and its tests.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Read the map | the `Grid` API, Manhattan distance | Foundation |
| 02 Neighbours and bounds | edge cases, tests for each | Foundation |
| 03 Breadth-first search | `ArrayDeque` as a queue, rebuilding a path | Intermediate |
| 04 Dijkstra's algorithm | `PriorityQueue`, stale entries | Intermediate |
| 05 A\* | a heuristic, `g + h` | Intermediate |
| 06 Make the search watchable | `SearchListener` (Observer) | Intermediate |
| 07 Break the heuristic on purpose | measuring, admissibility | Intermediate |
| 08 Extend it (choose one) | design, tests, the contract test | Stretch |

## How to run

Work in `code/src/t24_pathfinding/exercises/exNN/`. Give each exercise a `run()` method and call it from `Main`, or add a `main`. To run the tests for this topic:

```bash
cd code
mvn -o test -Dtest='t24_pathfinding.**'
```

> **Watch out:** two demos are provided. `demos.de01.Demo` animates a search in the console and `demos.de02.Demo` prints a comparison table. Run both before you start, so you know what the finished searches look like.

---

## Exercise 01 — Read the map

**Goal:** Load a map and report some facts about it, without searching. This is how you get to know the `Grid` class before you rely on it.

**You will need:** [Representing the map](../t24_pathfinding_notes.md#representing-the-map) and [Heuristic](../t24_pathfinding_notes.md#heuristic).

**Steps**

1. Create a record `MapReport` with seven components: `rows`, `cols`, `start`, `goal`, `passable`, `rough` and `manhattan`.
2. Add a static factory method `of(Grid grid)` that works out each value.
3. Count the passable cells by looping over every row and column. Count the rough ones (tile `Grid.ROUGH`, which is `'~'`) among them.
4. Work out the Manhattan distance from start to goal: the difference in rows plus the difference in columns, both made positive with `Math.abs`.
5. In `run()`, print `MapReport.of(Maps.marsh())`.
6. Still in `run()`, pass `Grid.parse` a broken map with one row too short, catch the exception and print its message.

```java
public static MapReport of(Grid grid)
```

**Example**

```text
MapReport[rows=5, cols=13, start=(1,1), goal=(1,11), passable=24, rough=9, manhattan=10]
Broken map rejected: row 1 has length 4, expected 5
```

**Rules**

- Do not search. Every value comes from looking at the grid.
- Write a Javadoc comment on the record answering this: is the Manhattan distance a lower bound on the true cost, an upper bound, or neither? Justify it in one sentence.

<details>
<summary>Hints</summary>

1. `grid.isPassable(new Cell(r, c))` tells you whether a cell can be walked on, and `grid.tileAt(cell)` gives you its character.
2. For the Javadoc: every move changes the row or the column by one, and every move costs at least 1. So can any path cost less than the Manhattan distance?

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] The Javadoc answers the lower-or-upper-bound question with a reason.

**Think about it:** you will load maps from files in Exercise 08 (d). Read the exception message carefully now: is it good enough to help someone fix a 40-line map file?

**Package:** `t24_pathfinding.exercises.ex01`

**Solution:** [t24 exercise 01 solution code](../../../../code/src/t24_pathfinding/exercises/ex01/).
**Tests:** [t24 exercise 01 tests](../../../../code/test/t24_pathfinding/exercises/ex01/).

---

## Exercise 02 — Neighbours and bounds

**Goal:** Write the method every search calls in its inner loop: given a cell, return the cells you can move to next. No searching yet.

**You will need:** [Representing the map](../t24_pathfinding_notes.md#representing-the-map) and [The shape every search shares](../t24_pathfinding_notes.md#the-shape-every-search-shares).

**Steps**

1. In a class `Exercise`, write the method below.
2. If `cell` is not inside the grid, throw an `IllegalArgumentException`.
3. Loop over `Direction.values()`. For each direction, work out the next cell with `cell.step(direction)`.
4. Add the next cell to the result only if it is inside the grid **and** passable.
5. Write a test for each of these four cases: a cell in open ground (four neighbours), a cell against a wall, a corner cell (two neighbours), and a cell whose neighbours are all walls.

```java
public static List<Cell> neighboursOf(Grid grid, Cell cell)
```

**Example** (on the open grid `"S..", "...", "..G"`)

```text
Corner (0,0): [(1,0), (0,1)]
Centre (1,1): [(0,1), (2,1), (1,0), (1,2)]
```

**Rules**

- The order is always `UP`, `DOWN`, `LEFT`, `RIGHT`, the order of `Direction.values()`.
- A cell with no passable neighbours returns an empty list, never `null`.

<details>
<summary>Hints</summary>

1. Check `grid.inBounds(next)` **before** `grid.isPassable(next)`. Looking up a tile off the map throws an exception.
2. `&&` stops at the first `false`, so `grid.inBounds(next) && grid.isPassable(next)` never looks off the map.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] Your four tests pass, and the all-walls test checks for an empty list.
- [ ] Your result matches `grid.neighbours(cell)` from the given `Grid` class.

> **Watch out:** a bounds bug here looks like an algorithm bug later, and you will spend an hour in the wrong file. Test this method properly before you move on.

**Package:** `t24_pathfinding.exercises.ex02`

**Solution:** [t24 exercise 02 solution code](../../../../code/src/t24_pathfinding/exercises/ex02/).
**Tests:** [t24 exercise 02 tests](../../../../code/test/t24_pathfinding/exercises/ex02/).

---

## Exercise 03 — Breadth-first search

**Goal:** Write your first complete search. Breadth-first search explores the map in rings, nearest cells first, so it finds the path with the fewest **steps**.

**You will need:**
[Part 2 — Breadth-first search](../t24_pathfinding_notes.md#part-2--breadth-first-search) and [Steps versus cost](../t24_pathfinding_notes.md#steps-versus-cost).

**Steps**

1. Create a class `BreadthFirstPathfinder` that implements `Pathfinder`.
2. Put the start cell in an `ArrayDeque<Cell>` used as a queue: add with `offer`, take with `poll`.
3. Keep a `Map<Cell, Cell>` called `cameFrom`. When you first see a neighbour, record the cell you reached it from.
4. Each time you take a cell off the queue, add one to an `expanded` counter. If the cell is the goal, stop.
5. To rebuild the path, start at the goal and follow `cameFrom` back to the start, then reverse the list.
6. Return a `SearchResult` with the path, its cost and the number of cells expanded. If the queue empties first, return a result with an empty path.
7. Write two tests: one on `Maps.openRoom()` that finds a path, and one on `Maps.sealedGoal()` that reports no path.

```java
public SearchResult findPath(Grid grid, SearchListener listener)
```

**Example** (on `Maps.marsh()`)

```text
Breadth-first steps=10 cost=46 expanded= 21
```

**Rules**

- Mark a cell as seen when you **add it to the queue**, not when you take it off.
- The path includes both the start and the goal.

<details>
<summary>Hints</summary>

1. `cameFrom.containsKey(next)` doubles as your "already seen" check. Put the start in it first, mapped to `null`.
2. The cost of a path is the sum of `grid.costToEnter(cell)` for every cell after the start.

</details>

**Done when**

- [ ] Both tests pass.
- [ ] A test checks that every pair of cells next to each other in your path differs by exactly one row or one column.

> **Watch out:** if you mark cells as seen only when you take them off the queue, the same cell is queued many times and the search does far more work than it should. Marking on add is safe for breadth-first search. Exercise 04 shows why it stops being safe.

**Package:** `t24_pathfinding.exercises.ex03`

**Solution:**
[BreadthFirstPathfinder](../../../../code/src/t24_pathfinding/common/BreadthFirstPathfinder.java).
**Tests:** [t24 pathfinder contract tests](../../../../code/test/t24_pathfinding/common/PathfinderContractTest.java).

---

## Exercise 04 — Dijkstra's algorithm

**Goal:** Find the **cheapest** path rather than the shortest. Swap the queue for a priority queue ordered by the cost so far, and see a longer path turn out cheaper.

**You will need:**
[Part 3 — Dijkstra's algorithm](../t24_pathfinding_notes.md#part-3--dijkstras-algorithm) and [Re-finding a cheaper route](../t24_pathfinding_notes.md#re-finding-a-cheaper-route).

**Steps**

1. Copy your breadth-first search into a new class `DijkstraPathfinder`.
2. Replace the `ArrayDeque` with a `PriorityQueue` ordered by cost so far. Keep that cost in a `Map<Cell, Integer>` called `costSoFar`.
3. For each neighbour, work out `newCost = costSoFar.get(current) + grid.costToEnter(next)`.
4. If the neighbour is new, or `newCost` is lower than the cost you had for it, record the new cost and `cameFrom`, and add it to the queue.
5. At the top of the loop, if the cell you took off the queue has already been expanded, skip it.
6. In a class `Exercise`, write `compare`, which runs each pathfinder on the grid and returns one line per pathfinder. Run it on `Maps.marsh()` with your breadth-first search and your Dijkstra.

```java
static String compare(Grid grid, List<Pathfinder> finders)
```

**Example**

```text
Breadth-first steps=10 cost=46 expanded= 21
Dijkstra   steps=14 cost=14 expanded= 17
```

**Rules**

- Use `PriorityQueue` from `java.util`; do not write your own.
- Keep a `Set<Cell>` of cells already expanded.

<details>
<summary>Hints</summary>

1. Java's `PriorityQueue` cannot lower the priority of an entry already in it. When you find a cheaper route, add the cell again. The old copy is now **stale**.
2. The stale copy comes off the queue later. The `expanded` check at the top of the loop is what throws it away.
3. Store pairs in the queue, such as a small record `Entry(Cell cell, int priority)`, and order with `Comparator.comparingInt(Entry::priority)`.

</details>

**Done when**

- [ ] `compare` prints the two lines shown.
- [ ] Both of your Exercise 03 tests also pass against `DijkstraPathfinder`.

**Think about it:** Dijkstra's path has 14 steps and costs 14; breadth-first's has 10 steps and costs 46. Explain to someone who has not done this exercise how a path with more steps can be cheaper, and why breadth-first search was not wrong to return what it did.

**Package:** `t24_pathfinding.exercises.ex04`

**Solution:** [t24 exercise 04 solution code](../../../../code/src/t24_pathfinding/exercises/ex04/) and [DijkstraPathfinder](../../../../code/src/t24_pathfinding/common/DijkstraPathfinder.java). The worked `DijkstraPathfinder` hands its work to [AStarPathfinder](../../../../code/src/t24_pathfinding/common/AStarPathfinder.java) with a zero heuristic. Exercise 05 shows why that works.
**Tests:** [t24 exercise 04 tests](../../../../code/test/t24_pathfinding/exercises/ex04/).

---

## Exercise 05 — A\*

**Goal:** Make Dijkstra faster by giving it a sense of direction. A\* adds an estimate of the distance still to go, so it explores towards the goal first.

**You will need:** [Part 4 — A\* search](../t24_pathfinding_notes.md#part-4--a-search).

**Steps**

1. Copy your Dijkstra into a new class `AStarPathfinder`. Give it a constructor that takes a `Heuristic`.
2. Change the priority of each queue entry from `g` (the cost so far) to `g + h`, where `h = heuristic.estimate(next, grid.goal())`.
3. Leave everything else as it was. `costSoFar` still stores `g` only.
4. In `run()`, run A\* on `Maps.openRoom()` with `Heuristic.manhattan()` and with `Heuristic.zero()`. Run your Dijkstra too, and print the cost and expansions for all three.

```java
public AStarPathfinder(Heuristic heuristic)
```

**Example**

```text
A* Manhattan: cost=34 expanded=35
A* zero:      cost=34 expanded=212
Dijkstra:     cost=34 expanded=212
```

**Rules**

- `h` only changes the order of the queue. It is never added to `costSoFar` or to the path cost.

<details>
<summary>Hints</summary>

1. With `Heuristic.zero()`, `g + h` is just `g`, so A\* should behave **exactly** like Dijkstra: the same path and the same number of expansions.
2. If the zero-heuristic count differs from your Dijkstra, one of the two has a bug. Compare them line by line.

</details>

**Done when**

- [ ] `run()` prints three lines with the same cost.
- [ ] A\* with `Heuristic.zero()` expands exactly as many cells as Dijkstra.

**Think about it:** 35 expansions against 212, for the same path cost. Which number would you quote in a report, and which would you quote to justify the extra complexity of A\*?

**Package:** `t24_pathfinding.exercises.ex05`

**Solution:** [t24 exercise 05 solution code](../../../../code/src/t24_pathfinding/exercises/ex05/) and [AStarPathfinder](../../../../code/src/t24_pathfinding/common/AStarPathfinder.java).
**Tests:** [t24 pathfinder contract tests](../../../../code/test/t24_pathfinding/common/PathfinderContractTest.java).

---

## Exercise 06 — Make the search watchable

**Goal:** Take every `System.out.println` out of your three searches. They report what they are doing to a `SearchListener` instead, and separate classes decide what to do with it. This is the Observer pattern from t14.

**You will need:**
[Reporting without printing](../t24_pathfinding_notes.md#reporting-without-printing) and [Part 5 — Watching a search work](../t24_pathfinding_notes.md#part-5--watching-a-search-work).

**Steps**

1. In each search, call `listener.onExpand(current, frontier, visited)` each time you take a cell off the queue.
2. Call `listener.onFound(path)` when you reach the goal, or `listener.onExhausted()` when the queue empties.
3. Delete every print statement from the three searches.
4. Write `CountingListener`. It records the order cells were expanded in, and the final path.
5. Write a console renderer. Its `render(...)` method returns one frame as a `String`, using the glyph table from the notes. A separate method prints it.

```java
public interface SearchListener {
    default void onExpand(Cell current, Set<Cell> frontier, Set<Cell> visited) { }
    default void onFound(List<Cell> path) { }
    default void onExhausted() { }
}
```

**Example** (one frame part-way through a search, and the finished path)

```text
#Soooo#            #          #S    #            #
#o###o# ########## #          #+### # ########## #
#o#ooo#@#oooooo? # #          #+#   # #++++++++# #
```

**Rules**

- No search prints anything. Only listeners decide what is shown.
- A listener only watches. It must not change what the search does.

<details>
<summary>Hints</summary>

1. The interface methods are `default` and empty, so `SearchListener.NONE` works when nobody is watching, and a listener only overrides the calls it cares about.
2. Keep building a frame separate from printing it. Then you can test `render(...)` with an ordinary `assertEquals`, without capturing `System.out`.

</details>

**Done when**

- [ ] A test runs the same search with a renderer and with `SearchListener.NONE`, and checks the two results are equal.
- [ ] A test uses `CountingListener` to check that the first cell expanded is the start.

**Think about it:** once the search reports instead of printing, your tests check a `List<Cell>` instead of captured output. Why should changing the wording of a caption never break a test of the algorithm?

**Package:** `t24_pathfinding.exercises.ex06`

**Solution:** [CountingListener](../../../../code/src/t24_pathfinding/common/CountingListener.java) and [ConsoleRenderer](../../../../code/src/t24_pathfinding/common/ConsoleRenderer.java).
**Tests:** [t24 listener and renderer tests](../../../../code/test/t24_pathfinding/common/ListenerAndRendererTest.java).

---

## Exercise 07 — Break the heuristic on purpose

**Goal:** See what happens when the heuristic **overestimates**. The search gets faster, and then it quietly stops finding the cheapest path.

**You will need:** [Admissible](../t24_pathfinding_notes.md#admissible) and [Admissibility, and what breaking it costs](../t24_pathfinding_notes.md#admissibility-and-what-breaking-it-costs).

**Steps**

1. Create a record `Run(int factor, int cost, int expanded)` inside a class `Exercise`.
2. Write `scaledRuns`. For each factor, run A\* with `Heuristic.scaledManhattan(factor)` and record the path cost and the number of cells expanded.
3. In `run()`, call it on `Maps.marsh()` with factors 1, 2, 3, 5 and 10, and print a table.

```java
public static List<Run> scaledRuns(Grid grid, int... factors)
```

**Example**

```text
factor  cost  expanded
     1    14        16
     2    14        17
     3    14        19
     5    46        11
    10    46        11
```

**Rules**

- Use the same map for every run, so only the factor changes.

<details>
<summary>Hints</summary>

1. `int... factors` is a varargs parameter. Inside the method, `factors` is an ordinary `int[]`.
2. Look for the first factor where the cost changes. 46 is the same cost breadth-first search found in Exercise 04.

</details>

**Done when**

- [ ] `run()` prints the table shown.
- [ ] You can say at which factor the path stops being the cheapest, and what happened to the expansion count at that point.

**Think about it:** a colleague submits this change with the message "optimised pathfinding, fewer nodes expanded". Every test passes. What test was missing, and what would you name it?

**Package:** `t24_pathfinding.exercises.ex07`

**Solution:** [t24 exercise 07 solution code](../../../../code/src/t24_pathfinding/exercises/ex07/).
**Tests:** [t24 exercise 07 tests](../../../../code/test/t24_pathfinding/exercises/ex07/).

---

## Exercise 08 — Extend it (choose one)

**Goal:** Add one feature of your own choice to the search engine, with tests, and explain the decision you made.

**You will need:** [Part 6 — Measuring the difference](../t24_pathfinding_notes.md#part-6--measuring-the-difference) and, for option (d), the file reading from t18.

**Choose one option**

- **(a) Diagonal movement.** Add four diagonal directions to `Direction`. Choose a cost for a diagonal move and justify it. Is Manhattan distance still admissible? If not, write a heuristic that is, and show a map where Manhattan now gives a path that is not the cheapest.
- **(b) Greedy best-first search.** Order the queue by `h` alone, ignoring `g`. Compare it with A\* on all the maps in `Maps`, for cost and expansions. When would you accept it?
- **(c) Several goals.** Given several goal cells, find the cheapest route to the nearest one. Do it with **one** search, not one per goal, and explain why that works.
- **(d) Loading a map from a file.** Write `MapLoader.load(Path)`, which reads a map with `Files.readAllLines` and reports problems with the line number. Test it with `@TempDir`.

**Steps**

1. Pick an option. Write down, in two or three sentences, what you will build and how you will know it works.
2. Write the code in the package below.
3. Add your new `Pathfinder` (options a–c) to the parameterised contract test in `PathfinderContractTest`.
4. Write at least two tests of your own for the new behaviour.

**Example** (option d)

```text
Loaded 3x5, path cost 2
Rejected: Line 2: expected 5 tiles but found 4
```

**Rules**

- Every test that passed before still passes.
- For option (d), the error message names the line, and for an unknown tile the column too.

<details>
<summary>Hints</summary>

1. The contract test is parameterised over a list of pathfinders. Adding yours to that list means every rule it checks (a walkable path, the correct cost, stopping on an unreachable goal) now applies to your code for free.
2. For option (d), count lines as you read them. Let `Grid.parse` check the rules a line-by-line scan cannot, such as there being exactly one start.

</details>

**Done when**

- [ ] Your new code has at least two tests, and they pass.
- [ ] Options a–c: your pathfinder passes the contract test.
- [ ] You have written a short note on what you chose and why.

**Package:** `t24_pathfinding.exercises.ex08`

**Solution (option d):**
[t24 exercise 08 solution code](../../../../code/src/t24_pathfinding/exercises/ex08/).
**Tests:** [t24 exercise 08 tests](../../../../code/test/t24_pathfinding/exercises/ex08/).
