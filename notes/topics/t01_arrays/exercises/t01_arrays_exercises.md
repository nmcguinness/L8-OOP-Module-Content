---
title: "Arrays (1D & 2D) — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t01_arrays
description: "Eleven exercises on 1D and 2D arrays, from fill and search to jagged rows, heatmap normalisation, and a word-search challenge."
created: 2025-10-06
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, arrays, 1d, 2d, loops, indexing, debugging, exercises, year2, comp-c8z03]
---
# Arrays (1D & 2D) — Exercises

> These exercises build directly on the **Arrays (1D & 2D)** notes. Work from top to bottom.
> Most tasks include small, checkable outputs. Use the provided method signatures and add tiny
> tests in `main` or `Exercise.run()`.
>
> Hints and snippets you can reuse are in the notes (e.g., `inBounds`, `print2D`).

## How to run

Each exercise assumes a package like:

```text
t01_arrays.exercises.exNN
```

Create a class `Exercise` in that package with a static entry point:

```java
package t01_arrays.exercises.ex01;

public final class Exercise {
    public static void run() {
        // call your methods here; print results for quick checks
    }
}
```

Call `Exercise.run()` from your `Main`, as shown in the repo README.

---

## Exercise 01 — Fill, Sum, Average (1D warm-up)

**Objective:** Practice create then write then read then iterate.
**Description:** In this exercise you will create an integer array of a given length,
initialize all elements with the same value, and then derive basic statistics from it. You’ll
implement functions to build the array, compute its sum, and compute the average with
appropriate handling for `null` and empty arrays. Pay close attention to
integer-vs-floating-point division when computing the average, and make sure your
implementation is safe and predictable for edge cases. Use the quick checks to confirm your
methods behave as expected.
- Write three methods:

  ```java
  static int[] fillWith(int n, int value)               // length n, all value
  static int sum(int[] xs)                               // 0 for null
  static double average(int[] xs)                        // 0.0 for null/empty
  ```

- In `run()`, check:

  ```java
  int[] a = fillWith(4, 5);      // expect {5,5,5,5}
  System.out.println(sum(a));    // 20
  System.out.println(average(a));// 5.0
  System.out.println(average(new int[]{})); // 0.0
  ```

**Pitfalls:** Off-by-one in loops; integer division before casting.

---

## Exercise 02 — `indexOf` & `count` (search in 1D)

**Objective:** Implement simple linear searches.
**Description:** Here you’ll add two classic search utilities: one to find the first index of a
target value and one to count all occurrences. These are the building blocks for more advanced
algorithms and teach you how to traverse arrays in a safe, robust way. Think about when to stop
early (for `indexOf`) versus when to traverse the entire array (for `count`). Consider how your
code behaves for `null` and empty arrays and keep your contracts consistent across methods.
- Implement:

  ```java
  static int indexOf(int[] xs, int target)   // -1 if not found
  static int count(int[] xs, int target)     // 0 for null
  ```

- Quick checks:

  ```java
  System.out.println(indexOf(new int[]{3,7,7,2}, 7) == 1);
  System.out.println(count(new int[]{3,7,7,2}, 7) == 2);
  System.out.println(indexOf(new int[]{}, 9) == -1);
  ```

**Stretch:** Return **all** indices in a new array.

---

## Exercise 03 — Min/Max with Guards

**Objective:** Handle edge cases explicitly.
**Description:** You will compute the minimum and maximum values in an array, but this time you
must define a clear policy for invalid inputs. Instead of returning a sentinel like
`Integer.MAX_VALUE`, throw an `IllegalArgumentException` when given `null` or empty arrays.
This forces you to separate “happy path” logic from input validation and practice defensive
programming. Use tests that exercise normal, boundary, and failing cases.
- Implement:

  ```java
  static int min(int[] xs)  // throw IllegalArgumentException on null/empty
  static int max(int[] xs)  // same contract as min
  ```

- Quick checks (expect `true` for each):

  ```java
  System.out.println(min(new int[]{5,2,8,1}) == 1);
  System.out.println(max(new int[]{5,2,8,1}) == 8);
  ```

**Discussion:** Why throw on empty rather than return a sentinel?

---

## Exercise 04 — Grade Histogram (1D to frequency)

**Objective:** Use arrays as fixed-size counters.
**Description:** Convert a list of exam scores (0–100) into a frequency distribution spanning
11 bins (0–9, 10–19, ..., 100). This exercise emphasizes mapping raw values into categories
using integer arithmetic and array indexing. After producing the frequency array, render a
simple textual histogram so you can visually inspect distribution patterns. Consider
out-of-range inputs and decide how your function should respond.
- Implement:

  ```java
  static int[] histogram(int[] scores) // returns int[11]
  ```

- Print the bars like:

  ```text
  00–09: **
  10–19: ****
  ...
  100 : *
  ```

**Stretch:** Also return the **mode** (bin with highest frequency).

---

## Exercise 05 — Border & Center (2D basics)

**Objective:** Create and update a rectangular 2D grid.
**Description:** Build a `5x5` integer grid and set specific cells to create a recognizable
pattern: all border cells set to `1` and the exact center cell set to `9`. This will give you
practice with nested loops, row/column addressing, and the difference between `grid.length` and
`grid[r].length`. Finish by pretty-printing the grid so each row appears on its own line.
Confirm the last two rows match the expected pattern.
- Make a `5x5` `int[][] grid`.
- Set the **border** cells to `1` and the **center** cell to `9`.
- Pretty print one row per line.
- Expected last two rows (spaces optional):

  ```text
  1 0 0 0 1
  1 1 1 1 1
  ```

**Hint:** Use `grid[r].length` inside the row loop.

---

## Exercise 06 — Find & Count in 2D

**Objective:** Traverse all cells correctly.
**Description:** Implement two helpers for 2D integer arrays: `find2D`, which returns the first
coordinates `{r,c}` for a target value (or `{-1,-1}` if absent), and `count2D`, which returns
the total number of matches in the grid. This exercise reinforces nested-loop traversal and
careful bounds usage for potentially jagged grids. Design small test grids to verify hits,
misses, and multiple occurrences.
- Implement:

  ```java
  static int[] find2D(int[][] g, int target) // {r,c} or {-1,-1}
  static int count2D(int[][] g, int target)
  ```

- Test on a small grid; verify both a hit and a miss.

---

## Exercise 07 — Jagged Rows: Build & Validate

**Objective:** Work with non-rectangular (jagged) arrays.
**Description:** Learn how to construct and reason about jagged arrays where rows can have
different lengths. Start by creating a 2D array with rows of lengths `{2,5,3}` and then
implement `isRectangular` to detect whether all non-null rows share the same length. This
clarifies the difference between rectangular matrices and general 2D arrays in Java. Add tests
that include `null` rows and rows of differing sizes to validate your logic.
- Create `int[][] jag = new int[3][];` and then rows of lengths `{2,5,3}`.
- Write:

  ```java
  static boolean isRectangular(int[][] g) // true if all non-null rows have same length
  ```

- Quick checks:

  ```java
  System.out.println(isRectangular(new int[][]{{1,2},{3,4}}));      // true
  System.out.println(isRectangular(new int[][]{{1,2},{3,4,5}}));    // false
  ```

**Stretch:** `static int[][] padToRect(int[][] g, int pad)` — return a **new** rectangular
copy, padding short rows with `pad`.

---

## Exercise 08 — Heatmap Normalization (2D compute)

**Objective:** Compute derived values across the grid.
**Description:** You are given a 2D integer heatmap with values in the range `0..255`. Your
task is to normalize each value to the range `0..1` by dividing by the **maximum** value found
in the entire grid. This requires two passes: one to find the max and another to compute the
normalized output. Handle the degenerate case where all values are `0` to avoid division by
zero, and return a `double[][]` with the normalized results.
- Implement:

  ```java
  static double[][] normalize(int[][] heat)
  ```

- Check: if all entries are `0`, the result is all `0.0` (avoid divide-by-zero).
**Stretch:** Return the coordinates of the hottest cell(s).

---

## Exercise 09 — Tic-Tac-Toe Checker (2D char)

**Objective:** Practice row/col/diag traversal.
**Description:** Represent a tic-tac-toe board using a `char[3][3]` containing `'X'`, `'O'`, or
`' '`. Implement `hasWon` to detect whether a given player has completed a row, column, or
diagonal. Implement `isFull` to check whether the board contains any empty spaces. Construct
several boards (win by row, win by column, no win) and confirm your functions return the
correct results. Consider how you might generalize this to larger board sizes.
- Board is `char[3][3]` with `'X'`, `'O'`, or `' '`.
- Implement:

  ```java
  static boolean hasWon(char[][] b, char player)
  static boolean isFull(char[][] b)
  ```

- Quick checks on a known winning board for `'X'` and a non-winning board for `'O'`.
**Stretch:** Write a `nextMove(char[][] b)` that returns the first free cell `{r,c}` or `{-1,-1}`.

---

## Exercise 10 — Word Search (2D char, optional challenge)

**Objective:** Multi-directional search using 2D traversal and helpers.
**Description:** Implement two search functions that scan a 2D grid of letters for the presence
of a word either left-to-right or top-to-bottom. You’ll need to write careful nested loops and
verify characters step-by-step without stepping outside the grid bounds. Keep your logic
modular by using small helpers such as `startsWithAtRow` or `startsWithAtCol`. Extend the
challenge by supporting reverse directions or diagonals once the basic version works.
- Given a grid of letters and a target word, return `true` if the word can be read left then
  right or top then bottom (no diagonals, no wrapping).

  ```java
  static boolean containsWordLR(char[][] g, String w)
  static boolean containsWordTB(char[][] g, String w)
  ```

- Starter:

  ```java
  char[][] g = {
      {'C','O','D','E'},
      {'A','R','R','A'},
      {'T','I','M','E'},
      {'H','A','S','H'}
  };
  System.out.println(containsWordLR(g,"CODE")); // true
  System.out.println(containsWordTB(g,"CAT"));  // true
  ```

**Stretch:** Support reverse directions and diagonals.

---

## Exercise 11 — Data Wrangling with Arrays (mini real-world)

**Objective:** Parse, compute, and report without collections.
**Description:** You’ll process a comma-separated list of daily step counts (e.g., data
exported from a basic fitness tracker). Parse the string into an `int[]`, compute min, max,
average, and the count of zero-days, then produce a compact textual summary. Avoid using
`ArrayList` or streams—focus on array fundamentals and robust parsing. For an extension, group
the data by week (7-day windows) and print a weekly report.
- Tasks:
  1) Parse into `int[]` (trim whitespace).
  2) Compute min, max, average, and count of zero-days.
  3) Print a one-line summary:

     ```text
     min=0 max=12034 avg=... zeros=1
     ```

- Method signatures:

  ```java
  static int[] parseSteps(String csv)
  static String summarize(int[] steps)
  ```

**Stretch:** Weekly report: split into 7-day chunks and print one line per week (pad last week).

---

## Reflective questions

- Where did you add **guards** (null/empty/bounds)? Which could be turned into helpers?
- What’s a bug you hit (or could hit), and what’s the **symptom** and **fix**?
- If you had to test `hasWon`, which **3 boards** would you start with and why?
- In `normalize`, how do you avoid **integer division** pitfalls?
