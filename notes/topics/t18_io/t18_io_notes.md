---
title: "Java I/O: Files, Paths & Streams"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t18_io
description: "Reading and writing files using NIO.2 (Path, Files), text I/O with BufferedReader/Writer, byte array I/O, basic CSV parsing, and try-with-resources for safe resource management."
created: 2026-05-27
last_updated: 2026-05-27
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, io, files, path, nio2, bufferedreader, csv, try-with-resources, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO1, MLO3]
previous_topic: t17_streams_api
prerequisites:
  - Exception handling (checked exceptions, try-with-resources)
  - Streams API (Stream<String>, collect, filter — for Files.lines())
  - Collections I: ArrayList
---

# Java I/O: Files, Paths & Streams

> **Prerequisites:**
> - Exception handling: checked exceptions, `try-with-resources` (`AutoCloseable`)
> - Streams API: you can use `Stream<String>` pipelines (needed for `Files.lines()`)
> - Collections: `List<String>`, basic loops

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Part 1: Path and Files — the NIO.2 API](#part-1-path-and-files--the-nio2-api)
- [Part 2: Reading text files](#part-2-reading-text-files)
- [Part 3: Writing text files](#part-3-writing-text-files)
- [Part 4: Binary files (byte arrays)](#part-4-binary-files-byte-arrays)
- [Part 5: CSV parsing](#part-5-csv-parsing)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Games example: load level config from file](#games-example-load-level-config-from-file)
- [Software example: append-only task log](#software-example-append-only-task-log)
- [Common mistakes](#common-mistakes)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Distinguish the legacy `File` API from the modern NIO.2 `Path`/`Files` API. |
| Use | Construct `Path` objects and resolve relative paths. |
| Use | Read all lines from a text file with `Files.readAllLines()`. |
| Use | Stream a file line-by-line with `Files.lines()` for large files. |
| Use | Write text to a file with `Files.write()` and `Files.writeString()`. |
| Use | Read and write raw bytes with `Files.readAllBytes()` and `Files.write(path, bytes)`. |
| Use | Open a `BufferedReader`/`BufferedWriter` inside try-with-resources. |
| Use | Parse a simple CSV file into a list of records. |
| Debug | Identify and fix `IOException`, `NoSuchFileException`, and encoding issues. |

---

## Why this matters

Most programs need to persist data: load a configuration, read a dataset, save results,
exchange files with other systems. Java's NIO.2 API (`java.nio.file`) provides a clean,
expressive way to do all of this — far safer and more readable than the older
`File`/`FileInputStream` approach.

Combined with `try-with-resources` (from the exception handling topic) and `Stream<String>`
(from the Streams topic), NIO.2 enables concise, safe file processing in very few lines.

---

## How this builds on previous content

| Earlier topic | Concept carried forward |
| :-- | :-- |
| Exception handling | `IOException` is checked — callers must handle or propagate; try-with-resources closes resources automatically |
| Streams API | `Files.lines(path)` returns a `Stream<String>` — plug directly into `.filter()`, `.map()`, `.collect()` |
| Collections | Results are usually collected into `List<String>` or `List<YourRecord>` |

---

## Part 1: Path and Files — the NIO.2 API

### Path

`Path` represents a file or directory location. It does not require the file to exist.

```java
import java.nio.file.Path;
import java.nio.file.Paths;

Path p1 = Path.of("data", "scores.txt");      // relative: data/scores.txt
Path p2 = Path.of("C:/data/scores.txt");      // absolute (Windows)
Path p3 = Paths.get("data/scores.txt");       // legacy factory — equivalent

Path dir = Path.of("data");
Path file = dir.resolve("scores.txt");        // data/scores.txt
Path parent = file.getParent();               // data
String name = file.getFileName().toString();  // scores.txt
```

### Files utility class

`Files` provides static methods for almost every file operation:

```java
import java.nio.file.Files;

boolean exists = Files.exists(p1);
boolean isFile = Files.isRegularFile(p1);
boolean isDir = Files.isDirectory(p1);
long size = Files.size(p1);              // bytes

Files.createDirectories(Path.of("data", "output")); // mkdir -p
Files.deleteIfExists(p1);
Files.copy(p1, Path.of("backup.txt"));
Files.move(p1, Path.of("archive", "scores.txt"));
```

---

## Part 2: Reading text files

### Read all lines at once

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;

public static List<String> readLines(Path path) throws IOException {
    return Files.readAllLines(path, StandardCharsets.UTF_8);
}
```

Use when the file fits comfortably in memory (< a few hundred MB).

---

### Stream lines lazily

```java
import java.util.stream.Stream;

public static long countNonEmpty(Path path) throws IOException {
    try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
        return lines.filter(l -> !l.isBlank()).count();
    }
}
```

> **Always** close the stream from `Files.lines()` — use try-with-resources. It holds an open
> file handle until closed.

---

### BufferedReader (lower-level)

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public static List<String> readWithBuffer(Path path) throws IOException {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
    }
    return lines;
}
```

Use `BufferedReader` when you need line-by-line control (e.g. skip header, stop early).

---

## Part 3: Writing text files

```java
import java.nio.file.StandardOpenOption;
import java.util.List;

// Write a list of strings (one per line)
public static void writeLines(Path path, List<String> lines) throws IOException {
    Files.write(path, lines, StandardCharsets.UTF_8);
    // Overwrites by default; add StandardOpenOption.APPEND to append
}

// Write a single string
public static void writeString(Path path, String content) throws IOException {
    Files.writeString(path, content, StandardCharsets.UTF_8);
}

// Append to existing file
public static void appendLine(Path path, String line) throws IOException {
    Files.writeString(path, line + System.lineSeparator(),
        StandardCharsets.UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
}
```

### BufferedWriter (lower-level)

```java
import java.io.BufferedWriter;
import java.io.IOException;

public static void writeWithBuffer(Path path, List<String> lines) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
    }
}
```

---

## Part 4: Binary files (byte arrays)

```java
// Read entire file as bytes
public static byte[] readBytes(Path path) throws IOException {
    return Files.readAllBytes(path);
}

// Write bytes to file
public static void writeBytes(Path path, byte[] data) throws IOException {
    Files.write(path, data);
}
```

```java
// Copy a binary file via byte array
Path src = Path.of("image.png");
Path dest = Path.of("copy.png");
Files.write(dest, Files.readAllBytes(src));

// Smarter: Files.copy handles streams internally
Files.copy(src, dest);
```

---

## Part 5: CSV parsing

Java has no built-in CSV library, but splitting on commas handles simple cases:

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record PlayerRecord(String name, int score) {}

public static List<PlayerRecord> parseCsv(Path path) throws IOException {
    List<String> lines = Files.readAllLines(path);
    List<PlayerRecord> records = new ArrayList<>();

    boolean firstLine = true;
    for (String line : lines) {
        if (firstLine) { firstLine = false; continue; } // skip header
        if (line.isBlank()) continue;

        String[] parts = line.split(",", -1);           // -1 keeps empty trailing fields
        String name = parts[0].trim();
        int score = Integer.parseInt(parts[1].trim());
        records.add(new PlayerRecord(name, score));
    }
    return records;
}
```

Example CSV:

```text
name,score
Alice,95
Ben,82
Clara,78
```

> For production CSV (quoted fields, embedded commas), use a library like **Apache Commons
> CSV** or **OpenCSV**.

---

## Progressive coding steps (A then B then C)

### Step A — Read and print a file

```java
Path path = Path.of("data.txt");
List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
lines.forEach(System.out::println);
```

### Step B — Filter lines with a stream

```java
long commentCount;
try (Stream<String> lines = Files.lines(Path.of("config.txt"))) {
    commentCount = lines.filter(l -> l.startsWith("#")).count();
}
System.out.println("Comment lines: " + commentCount);
```

### Step C — Read, transform, write

```java
Path input = Path.of("scores.csv");
Path output = Path.of("scores_upper.csv");

List<String> lines = Files.readAllLines(input, StandardCharsets.UTF_8);
List<String> upper = lines.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
Files.write(output, upper, StandardCharsets.UTF_8);
```

---

## Games example: load level config from file

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

// File format: key=value, one per line, # for comments
// width=20
// height=15
// difficulty=hard

public class LevelConfig {

    private final Map<String, String> _props = new HashMap<>();

    public static LevelConfig load(Path path) throws IOException {
        LevelConfig config = new LevelConfig();
        for (String line : Files.readAllLines(path)) {
            String trimmed = line.strip();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
            String[] parts = trimmed.split("=", 2);
            if (parts.length == 2) {
                config._props.put(parts[0].strip(), parts[1].strip());
            }
        }
        return config;
    }

    public String get(String key, String defaultValue) {
        return _props.getOrDefault(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String val = _props.get(key);
        if (val == null) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
```

Usage:

```java
LevelConfig cfg = LevelConfig.load(Path.of("levels", "level1.cfg"));
int width = cfg.getInt("width", 20);
int height = cfg.getInt("height", 15);
String diff = cfg.get("difficulty", "normal");
```

---

## Software example: append-only task log

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TaskLog {

    private final Path _logFile;

    public TaskLog(Path logFile) {
        _logFile = logFile;
    }

    public void log(String event) throws IOException {
        String entry = LocalDateTime.now() + " | " + event;
        Files.writeString(_logFile, entry + System.lineSeparator(),
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
    }

    public List<String> readAll() throws IOException {
        if (!Files.exists(_logFile)) return List.of();
        return Files.readAllLines(_logFile, StandardCharsets.UTF_8);
    }

    public List<String> search(String keyword) throws IOException {
        try (var lines = Files.lines(_logFile, StandardCharsets.UTF_8)) {
            return lines
                .filter(l -> l.contains(keyword))
                .collect(Collectors.toList());
        }
    }
}
```

---

## Common mistakes

| Pitfall | Problem | Fix |
| :-- | :-- | :-- |
| Forgetting to close `Files.lines()` | File handle leak — OS runs out of handles | Always wrap in try-with-resources |
| Using `File` instead of `Path` | Legacy API is more error-prone; returns `boolean` instead of throwing | Use `Path` + `Files` for all new code |
| Ignoring `StandardCharsets.UTF_8` | Default encoding varies by platform — files read on one machine may fail on another | Always specify charset explicitly |
| `split(",")` for CSV with quoted fields | Fails on `"Smith, Jr.",90` — splits inside the quotes | Use a CSV library for complex files |
| `Files.readAllLines()` on a 2 GB log | Loads entire file into heap | Use `Files.lines()` (lazy stream) instead |
| Constructing `Path` with OS-specific separators | `"data\\scores.txt"` fails on Linux | Use `Path.of("data", "scores.txt")` — handles separators |

---

## Reflective questions

- Why is `Files.lines()` preferred over `Files.readAllLines()` for large files?
- What would happen if you forgot the `try-with-resources` around `Files.lines()`?
- Why must `IOException` be handled or declared when calling `Files.readAllLines()`?
- How would you read only the first 10 lines of a large file efficiently?
- You need to write a file only if it does not already exist. Which `StandardOpenOption` values
  would you use?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**Why `Files.lines()` beats `Files.readAllLines()` on large files.**
`readAllLines` loads **every line into a `List<String>` in memory at once**. A 2 GB log file
needs more than 2 GB of heap (Java `String`s carry overhead) and you get `OutOfMemoryError` —
regardless of how few lines you actually wanted.

`Files.lines()` returns a lazy `Stream<String>` that reads a buffer at a time, so memory stays
roughly constant no matter how big the file is. It also composes with the whole Streams API and
short-circuits: with `.filter(...).findFirst()` you stop reading the moment you have an answer,
instead of reading 2 GB first.

Use `readAllLines` only when the file is known-small *and* you need random access or multiple
passes over the lines.

**Forgetting `try-with-resources` around `Files.lines()`.**
The underlying file handle is never closed — a **resource leak**. `Files.lines()` is one of the
few streams that is backed by an open file, so unlike a stream over a `List` it *must* be
closed. Symptoms build up rather than appearing at once: the process accumulates open
descriptors until it hits the OS limit and throws `IOException: Too many open files`, typically
far from the offending code. On Windows the more immediate symptom is that the file stays
**locked**, so a later attempt to delete, rename or rewrite it fails.

Because it is `AutoCloseable`, the fix is just to declare it in the `try`:

```java
try (Stream<String> lines = Files.lines(path)) {
    return lines.filter(l -> !l.isBlank()).count();
}   // file handle released here
```

**Why `IOException` must be handled or declared.**
It is a **checked** exception, so the compiler enforces it. That is deliberate: file operations
fail for reasons entirely outside your program's control — the file was deleted between check
and read, permissions changed, the disk filled, the network share dropped. No amount of correct
code prevents it, so the language forces you to make a conscious decision: handle it here, or
declare `throws IOException` and let a caller who has more context decide. Contrast with
`IllegalArgumentException`, which is unchecked because it signals a bug you should fix rather
than a condition you should tolerate. See
[t10](../t10_exception_handling/t10_exception_handling_notes.md).

**Reading only the first 10 lines efficiently.**
Stream it and `limit`, so you read only what you need:

```java
try (Stream<String> lines = Files.lines(path)) {
    List<String> head = lines.limit(10).toList();
}
```

Laziness is doing the work: `limit(10)` short-circuits, so the file is read only far enough to
produce ten lines and then closed — constant memory, near-instant on a huge file.
`Files.readAllLines(path).subList(0, 10)` returns the same ten lines but reads the entire file
first, which is the wrong answer here even though it looks equivalent. Guard `subList` anyway
if the file might have fewer than ten lines; `limit` handles that case without complaint.

**Writing only if the file does not exist.**
`StandardOpenOption.CREATE_NEW` (with `WRITE`, which `Files.newBufferedWriter` implies):

```java
try (BufferedWriter w = Files.newBufferedWriter(path, StandardOpenOption.CREATE_NEW)) {
    w.write("first run");
} catch (FileAlreadyExistsException e) {
    // the file was already there - nothing written
}
```

The distinction to remember is between the two CREATE options: **`CREATE`** makes the file if
absent and opens it if present; **`CREATE_NEW`** fails with `FileAlreadyExistsException` if it
already exists — which is exactly the requirement. `CREATE_NEW` is also **atomic**, so it is
the correct choice over `if (!Files.exists(path))` followed by a write: between that check and
the write, another process (or thread) could create the file, and you would overwrite it.
Checking first is a race condition; `CREATE_NEW` is not.

Related options worth knowing: `APPEND` to add to the end, and `TRUNCATE_EXISTING` to clear
before writing (the default alongside `CREATE` when writing).

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t17_streams_api
  domain_emphasis: Balanced

this_lesson:
  topic_code: t18_io
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO1, MLO3]
```
