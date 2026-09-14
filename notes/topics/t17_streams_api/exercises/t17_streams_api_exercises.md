---
title: "Streams API — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t17_streams_api
description: "Six Streams API exercises: filter, map, flatMap, groupingBy, reduction and summary statistics, and a record-based pipeline."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, streams, functional, lambda, collectors, pipeline, exercises, year2, comp-c8z03]
---
# Streams API — Exercises

## Exercise 01 — Filter and collect

Given a `List<String>` of product names, write a method:

```java
public static List<String> longNames(List<String> products, int minLength)
```

that returns names longer than `minLength`, sorted alphabetically, using a stream pipeline.

**Package:** `t17_streams_api.exercises.ex01`

---

## Exercise 02 — Map and join

Given a `List<String>` of words, write a method:

```java
public static String upperJoined(List<String> words)
```

that uppercases each word and joins them with `" | "` using `Collectors.joining()`.

**Package:** `t17_streams_api.exercises.ex02`

---

## Exercise 03 — FlatMap

Given a `List<String>` where each string is a comma-separated list of tags (e.g.
`"java,oop,design"`), write a method:

```java
public static List<String> allUniqueTags(List<String> tagLines)
```

that returns all unique tags as a sorted list. Use `flatMap` and `distinct`.

**Package:** `t17_streams_api.exercises.ex03`

---

## Exercise 04 — GroupingBy

Given a `List<String>` of words, write a method:

```java
public static Map<Integer, List<String>> groupByLength(List<String> words)
```

that groups words by their length using `Collectors.groupingBy`.

**Package:** `t17_streams_api.exercises.ex04`

---

## Exercise 05 — Reduce and statistics

Given a `List<Integer>` of scores, write methods:

```java
public static int sum(List<Integer> scores)
public static OptionalDouble average(List<Integer> scores)
public static Optional<Integer> max(List<Integer> scores)
```

using stream operations only (no loops).

**Package:** `t17_streams_api.exercises.ex05`

---

## Exercise 06 — Record pipeline (extension)

Define a `record Student(String name, String subject, int grade)`.

Given a `List<Student>`, write methods:

```java
// Average grade per subject
public static Map<String, Double> avgGradeBySubject(List<Student> students)

// Names of students who passed (grade >= 40), sorted alphabetically
public static List<String> passedNames(List<Student> students)
```

**Package:** `t17_streams_api.exercises.ex06`
