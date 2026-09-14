---
title: "OOP Fundamentals — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t00_oop_fundamentals
description: "Five exercises building a class from scratch: fields and behaviour, multiple constructors, object composition, and guard clauses."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, oop, classes, objects, encapsulation, constructors, access-modifiers, exercises, year2, comp-c8z03]
---
# OOP Fundamentals — Exercises

## Exercise 01 — Basic class

Define a `Book` class with private fields `title` (String) and `pageCount` (int).

- Constructor: validate that `title` is not blank and `pageCount` > 0; throw
  `IllegalArgumentException` otherwise.
- Getters for both fields.
- Override `toString()` to return `"Title (N pages)"`.

Write a `main` method that creates two `Book` objects, prints them, and demonstrates that an
invalid `Book` throws an exception.

**Package:** `t00_oop_fundamentals.exercises.ex01`

---

## Exercise 02 — State and behaviour

Define a `BankAccount` class with a private `double _balance` field.

- Constructor: sets initial balance (must be ≥ 0).
- `deposit(double amount)` — adds to balance; rejects negative amounts.
- `withdraw(double amount)` — deducts from balance; rejects negative amounts and amounts
  exceeding the balance.
- `getBalance()` — returns current balance.
- Override `toString()`.

Write a `main` method that deposits, withdraws, and prints the balance after each operation.

**Package:** `t00_oop_fundamentals.exercises.ex02`

---

## Exercise 03 — Multiple constructors

Define a `Rectangle` class with `width` and `height` fields (both doubles, both > 0).

- Full constructor: `Rectangle(double width, double height)`.
- Convenience constructor: `Rectangle(double side)` — creates a square using `this(side, side)`.
- Methods: `area()`, `perimeter()`, `isSquare()`.
- Override `toString()`.

**Package:** `t00_oop_fundamentals.exercises.ex03`

---

## Exercise 04 — Object composition

Define a `Student` class with fields `name` (String) and `grade` (int, 0–100).

Define a `Classroom` class that holds a list of `Student` objects.

- `add(Student s)` — adds to the list (reject null).
- `getAverage()` — returns the average grade as a double; returns 0.0 for an empty classroom.
- `getTopStudent()` — returns the `Student` with the highest grade (return `null` if empty).
- `printAll()` — prints every student.

**Package:** `t00_oop_fundamentals.exercises.ex04`

---

## Exercise 05 — Guard clauses

Define a `Temperature` class that stores a value in Celsius.

- Constructor: rejects values below −273.15 (absolute zero) with a descriptive message.
- `toCelsius()`, `toFahrenheit()`, `toKelvin()` methods.
- Override `toString()` to print `"X.XX °C"`.

Write a driver that creates valid and invalid temperatures and prints each conversion.

**Package:** `t00_oop_fundamentals.exercises.ex05`
