---
title: "Pathfinding — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t24_pathfinding
description: "Eight exercises building breadth-first search, Dijkstra and A* over a grid, with a listener seam, a console visualisation, heuristic comparison, and a measured report."
created: 2026-09-13
last_updated: 2026-09-13
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, algorithms, pathfinding, bfs, dijkstra, a-star, priorityqueue, heuristics, exercises, year2, comp-c8z03]
---

# Pathfinding — Exercises

These build one search engine in eight steps. Exercises 01–02 prepare the ground, 03–05 are
the three algorithms, 06–07 make the search watchable and measurable, and 08 extends it.

Everything you need is in `code/src/t24_pathfinding/common/` — `Grid`, `Cell`, `Heuristic`,
`SearchListener` and `SearchResult` are given. **You write the algorithms.**

## How to run

Work in `code/src/t24_pathfinding/exercises/exNN/`. Each exercise has a `main`, or add a
`run()` and call it from `Main`.

```bash
cd code
mvn -o test -Dtest='t24_pathfinding.**'
```

The worked implementations are in `common/`. Attempt each exercise before reading them —
these algorithms are short enough that reading one feels like understanding it, and that
feeling is unreliable.

> :bulb: Two demos are provided. `demos.de01.Demo` animates a search in the console;
> `demos.de02.Demo` prints the comparison table. Run both before you start.

---

## Exercise 01 — Read the map

Write a class that loads a `Grid` from text and reports, without searching:

- the dimensions, and the start and goal cells;
- how many cells are passable, and how many of those are rough;
- the Manhattan distance from start to goal.

Then answer in a comment: is that Manhattan distance a lower bound on the true cost, an upper
bound, or neither? Justify it in one sentence.

**Package:** `t24_pathfinding.exercises.ex01`

**Deliverable:** the class plus your one-sentence answer.

**Pitfall:** `Grid.parse` rejects ragged rows and unknown tiles. Feed it a deliberately broken
map and read the exception message — you will need that behaviour in Exercise 08.

---

## Exercise 02 — Neighbours and bounds (no searching yet)

Implement, for a `Grid` and a `Cell`, a method returning the passable neighbours in a fixed
order:

```java
public static List<Cell> neighboursOf(Grid grid, Cell cell)
```

Handle the four edges and the four corners. Then write tests covering: a cell in open ground
(four neighbours), a cell against a wall, a corner cell, and a cell whose neighbours are all
walls (empty list, not `null`).

**Package:** `t24_pathfinding.exercises.ex02`

**Why this comes before the algorithms:** every search you are about to write calls this in
its inner loop. A bounds bug here looks like an algorithm bug later, and you will spend an
hour in the wrong file.

**Check your work:** a corner cell of an open grid has exactly two neighbours.

---

## Exercise 03 — Breadth-first search

Implement `Pathfinder` using an `ArrayDeque` as a FIFO queue. Return a `SearchResult`
carrying the path, its cost, and the number of cells expanded.

Rebuild the path by recording, for each cell, the cell you arrived from, then walking that
chain back from the goal and reversing it.

**Package:** `t24_pathfinding.exercises.ex03`

**Deliverable:** a working `BreadthFirstPathfinder` and tests that it finds a walkable path on
`Maps.openRoom()` and reports failure on `Maps.sealedGoal()`.

**Pitfall:** mark a cell as seen when you **queue** it, not when you expand it — otherwise the
same cell is queued many times over and the search does far more work than it should. This is
safe for BFS specifically; Exercise 04 explains why it stops being safe.

**Check your work:** every consecutive pair in your path must differ by exactly one row or one
column. Assert it.

---

## Exercise 04 — Dijkstra's algorithm

Replace the FIFO queue with a `PriorityQueue` ordered by cost-so-far, and allow a cell to be
improved when a cheaper route to it is found.

Run both your BFS and your Dijkstra on `Maps.marsh()` and print steps and cost for each.

**Package:** `t24_pathfinding.exercises.ex04`

**Deliverable:** the implementation, plus the two-row comparison you printed.

**Discussion:** your BFS should report **10 steps costing 46**; Dijkstra should report
**14 steps costing 14**. Explain to someone who has not done this exercise how a path with
more steps can be cheaper, and why BFS was not wrong to return what it did.

**Pitfall:** Java's `PriorityQueue` has no "decrease key". When you find a cheaper route,
add the cell again and skip the stale copy when it surfaces — which means checking whether a
cell has already been expanded at the *top* of the loop, not only in the neighbour loop.

---

## Exercise 05 — A\*

Add a `Heuristic` to your Dijkstra and order the frontier by `g + h`.

Run it on `Maps.openRoom()` with `Heuristic.manhattan()` and with `Heuristic.zero()`, and
report the expansions for each.

**Package:** `t24_pathfinding.exercises.ex05`

**Deliverable:** the implementation and the two expansion counts.

**Check your work:** with `Heuristic.zero()` your A* must behave **exactly** like your
Dijkstra — not merely the same path, the same number of expansions. If it does not, one of
the two has a bug.

**Discussion:** you should see roughly 35 expansions with Manhattan against roughly 212 with
zero, for the same path cost. Which of those two numbers would you quote in a report, and
which would you quote to justify the extra complexity?

---

## Exercise 06 — Make the search watchable

Refactor your three algorithms so none of them prints anything. Have them report through
`SearchListener` instead, then write two implementations:

1. `CountingListener` — records the expansion order and the final path.
2. A console renderer that draws each frame using the glyph table from the notes.

**Package:** `t24_pathfinding.exercises.ex06`

**Deliverable:** the refactored searches plus both listeners.

**Why this is worth the effort:** once the search reports rather than prints, your tests stop
capturing `System.out` and start asserting on a `List<Cell>`. Rewording a caption then stops
breaking a test, which it absolutely should not have done in the first place.

**Check your work:** run the same search twice, once with a renderer attached and once with
nothing attached, and assert the results are identical. A listener that changes the outcome is
not an observer.

---

## Exercise 07 — Break the heuristic on purpose

Using `Heuristic.scaledManhattan(factor)`, run A* over `Maps.marsh()` for factors 1, 2, 3, 5
and 10. For each, record the cost of the path found and the number of cells expanded.

**Package:** `t24_pathfinding.exercises.ex07`

**Deliverable:** a small table of your five runs, and a short answer to: at which factor does
the path stop being optimal, and what happened to the expansion count at that point?

**What you should find:** the expansion count falls as the factor rises — the search gets
*faster* — and beyond a certain factor the cost jumps sharply. On the marsh map the cost goes
from 14 to 46, which is the same route breadth-first search took.

**Discussion:** a colleague submits this change with the message "optimised pathfinding, 30%
fewer nodes expanded". Every test passes. What test was missing, and what would you name it?

---

## Exercise 08 — Extend it (choose one)

Pick **one** and implement it, with tests.

**(a) Diagonal movement.** Add four diagonal directions to `Direction`. Decide the cost of a
diagonal move and justify it. Then answer: is Manhattan distance still admissible? If not,
implement the heuristic that is, and show a case where Manhattan now returns a suboptimal
path.

**(b) Greedy best-first search.** Order the frontier by `h` alone, ignoring `g`. Compare it
with A* on all four maps for cost and expansions. Explain when you would accept it.

**(c) Multiple goals.** Given several goal cells, find the cheapest route to the nearest one.
Do it with a single search, not one search per goal, and explain why that is possible.

**(d) Map loading.** Read a grid from a `.txt` file with `Files.readAllLines` (t18) and report
parse failures with the line number. Include a test using `@TempDir`.

**Package:** `t24_pathfinding.exercises.ex08`

**Deliverable:** the implementation, tests, and a short note on what you chose and why.

**Whichever you pick:** add your new `Pathfinder` to the parameterised contract test from the
worked solution. Every rule it already checks — walkable path, correct reported cost,
terminates on an unreachable goal — then applies to your implementation for free.
