---
title: "Exception Handling — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t10_exception_handling
description: "Five exercises on exception handling: safe parsing, custom checked and unchecked exceptions, try-with-resources, and designing a hierarchy."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, exceptions, checked, unchecked, try-catch, custom-exception, exercises, year2, comp-c8z03]
---
# Exception Handling — Exercises

## Exercise 01 — Safe integer parsing

Write a method:

```java
public static Optional<Integer> parseIntSafe(String input)
```

that returns an `Optional` containing the parsed integer if `input` is valid, or an empty
`Optional` if parsing fails.
Do **not** use `throws`; catch `NumberFormatException` internally.

**Package:** `t10_exception_handling.exercises.ex01`

---

## Exercise 02 — Custom unchecked exception

Create an `InvalidScoreException extends RuntimeException` that stores the invalid score value.

Write a method:

```java
public static void setScore(int score)
```

that throws `InvalidScoreException` if `score < 0` or `score > 100`.

Write a driver that calls `setScore` with valid and invalid values, catching the exception and
printing the message.

**Package:** `t10_exception_handling.exercises.ex02`

---

## Exercise 03 — Checked exception for DAO lookup

Define a checked `PlayerNotFoundException extends Exception`.

Implement a `PlayerRepository` that holds a `Map<String, Integer>` of player name to score.

Add a method:

```java
public int getScore(String name) throws PlayerNotFoundException
```

that throws `PlayerNotFoundException` if the name is not in the map.

Write a caller that catches the exception and prints a friendly message.

**Package:** `t10_exception_handling.exercises.ex03`

---

## Exercise 04 — Try-with-resources file reading

Write a method:

```java
public static List<String> readLines(String path) throws IOException
```

using try-with-resources to open a `BufferedReader`, read all lines into a `List<String>`, and
return them. The caller in `main` should catch `IOException` and print the error.

**Package:** `t10_exception_handling.exercises.ex04`

---

## Exercise 05 — Exception hierarchy (extension)

Design a small exception hierarchy for a bank application:

| Exception | Extends | Carries |
|:-|:-|:-|
| `BankException` | `RuntimeException` (so it is unchecked) | Base type for the hierarchy |
| `InsufficientFundsException` | `BankException` | The amount requested and the current balance |
| `AccountLockedException` | `BankException` | The account ID |

Implement a `BankAccount` class with `deposit(double amount)` and `withdraw(double amount)`.

- `deposit` throws `IllegalArgumentException` for negative amounts.
- `withdraw` throws `InsufficientFundsException` if funds are insufficient.

Write a driver that exercises both happy-path and error paths.

**Package:** `t10_exception_handling.exercises.ex05`
