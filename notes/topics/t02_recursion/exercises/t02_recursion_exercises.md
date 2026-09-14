---
title: "Recursion — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t02_recursion
description: "Six recursive exercises: power, counting in a string, array maximum, palindromes, binary search, and flood fill."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, recursion, call-stack, base-case, factorial, fibonacci, binary-search, exercises, year2, comp-c8z03]
---
# Recursion — Exercises

## Exercise 01 — Power function

Write a recursive static method `power(int base, int exp)` that returns `base` raised to the
power `exp`.

- Guard: if `exp < 0`, throw `IllegalArgumentException`.
- Base case: `exp == 0` returns `1`.
- Recursive case: `base * power(base, exp - 1)`.

Write a `main` method that tests:
- `power(2, 10)` returns `1024`
- `power(3, 0)` returns `1`
- `power(5, 1)` returns `5`
- `power(2, -1)` throws

**Package:** `t02_recursion.exercises.ex01`

---

## Exercise 02 — Count occurrences in a string

Write a recursive static method `countOccurrences(String s, char c)` that counts how many times
character `c` appears in string `s`.

- Base case: empty string returns `0`.
- Recursive case: check first character; add 1 if it matches, then recurse on `s.substring(1)`.

Write a `main` method that tests:
- `countOccurrences("banana", 'a')` returns `3`
- `countOccurrences("hello", 'z')` returns `0`
- `countOccurrences("", 'x')` returns `0`

**Package:** `t02_recursion.exercises.ex02`

---

## Exercise 03 — Recursive array maximum

Write a recursive static method `max(int[] xs, int i)` that returns the maximum value in `xs`
from index `i` to the end.

- Guard (public wrapper): if `xs` is null or empty, throw `IllegalArgumentException`.
- Base case: `i == xs.length - 1` returns `xs[i]`.
- Recursive case: `Math.max(xs[i], max(xs, i + 1))`.

Provide a public one-argument wrapper `max(int[] xs)` that starts the recursion at index `0`.

Test:
- `max(new int[]{3,1,7,2,5})` returns `7`
- `max(new int[]{-4,-1,-9})` returns `-1`

**Package:** `t02_recursion.exercises.ex03`

---

## Exercise 04 — Palindrome checker

Write a recursive static method `isPalindrome(String s)` that returns `true` if `s` reads the
same forwards and backwards (case-sensitive).

- Base case: length ≤ 1 to `true`.
- Short-circuit: first and last characters differ to `false`.
- Recursive case: check the substring with first and last characters removed.

Test:
- `isPalindrome("racecar")` returns `true`
- `isPalindrome("hello")` returns `false`
- `isPalindrome("a")` returns `true`
- `isPalindrome("")` returns `true`
- `isPalindrome("Racecar")` returns `false` (case-sensitive)

**Package:** `t02_recursion.exercises.ex04`

---

## Exercise 05 — Recursive binary search

Implement recursive binary search:

```java
static int binarySearch(int[] xs, int target, int lo, int hi)
```

- Base case: `lo > hi` to return `-1` (not found).
- Calculate `mid`; return `mid` if match; recurse left or right.

Provide a public wrapper `binarySearch(int[] xs, int target)`.

Test with a sorted array `{1, 3, 5, 7, 9, 11, 13}`:
- `binarySearch(xs, 7)` returns `3`
- `binarySearch(xs, 1)` returns `0`
- `binarySearch(xs, 13)` returns `6`
- `binarySearch(xs, 6)` returns `-1`

**Package:** `t02_recursion.exercises.ex05`

---

## Exercise 06 — Flood fill

Implement the flood-fill algorithm on an `int[][]` grid:

```java
static void fill(int[][] grid, int row, int col, int colour)
```

- Base case: out of bounds or cell is not `0` (wall or already filled).
- Mark the cell with `colour`; recurse in four directions (up, down, left, right).

Create a test grid, call `fill`, and print the grid before and after using a helper:

```java
static void printGrid(int[][] g) { /* row-by-row */ }
```

Verify that only the connected region of `0`s reachable from the start is coloured.

**Package:** `t02_recursion.exercises.ex06`
