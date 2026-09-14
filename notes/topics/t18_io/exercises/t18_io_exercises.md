---
title: "Java I/O: Files, Paths & Streams — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t18_io
description: "Six file I/O exercises: reading and counting, writing and appending, CSV parsing, binary copy, and a config loader."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, io, files, path, nio2, bufferedreader, csv, try-with-resources, exercises, year2, comp-c8z03]
---
# Java I/O: Files, Paths & Streams — Exercises

## Exercise 01 — Read and count

Write a method:

```java
public static int countLines(Path path) throws IOException
```

that returns the number of non-blank lines in a text file. Use `Files.lines()` inside a
try-with-resources block.

**Package:** `t18_io.exercises.ex01`

---

## Exercise 02 — Write a list to file

Write a method:

```java
public static void writeNames(Path path, List<String> names) throws IOException
```

that writes each name on its own line using `Files.write()`. A caller in `main` should catch
`IOException` and print the error.

**Package:** `t18_io.exercises.ex02`

---

## Exercise 03 — Append log entries

Implement a `SimpleLogger` class with:

- A `Path` field set in the constructor.
- `void log(String message) throws IOException` — appends `[timestamp] message` on a new line
  using `StandardOpenOption.APPEND` and `StandardOpenOption.CREATE`.
- `List<String> readLog() throws IOException` — returns all lines.

**Package:** `t18_io.exercises.ex03`

---

## Exercise 04 — CSV parser

Given a CSV file with header `name,score,level`:

```text
name,score,level
Alice,95,3
Ben,82,2
Clara,78,1
```

Write a method:

```java
public static List<String[]> parseCsv(Path path) throws IOException
```

that returns a list of string arrays (one per data row, header skipped), using
`Files.readAllLines()`.

Write a second method that returns only rows where `score` > 80.

**Package:** `t18_io.exercises.ex04`

---

## Exercise 05 — Binary copy

Write a method:

```java
public static void copyBinary(Path source, Path destination) throws IOException
```

that reads the source file as a `byte[]` using `Files.readAllBytes()` and writes it to the
destination using `Files.write()`.

Verify with a small PNG or any binary file — the destination must be identical to the source
(compare byte arrays).

**Package:** `t18_io.exercises.ex05`

---

## Exercise 06 — Config file loader (extension)

Implement a `ConfigLoader` class that:

- Reads a properties file in `key=value` format (lines starting with `#` are comments).
- Returns a `Map<String, String>`.
- Provides a `getInt(String key, int defaultValue)` helper.
- Throws `IOException` if the file cannot be read.

Test with a sample config file you create in the project resources folder.

**Package:** `t18_io.exercises.ex06`
