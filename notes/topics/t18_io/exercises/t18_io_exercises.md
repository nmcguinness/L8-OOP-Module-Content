---
title: "Java I/O: Files, Paths & Streams — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t18_io
description: "Six step-by-step file I/O exercises: counting lines, writing a list, an append-only logger, CSV parsing, a binary copy, and a config loader."
created: 2026-05-29
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, io, files, path, nio2, bufferedreader, csv, try-with-resources, exercises, year2, comp-c8z03]
---
# Java I/O: Files, Paths & Streams — Exercises

These exercises practise reading and writing files with `Path` and `Files`, from the [t18 notes](../t18_io_notes.md). Every exercise follows the same layout: a goal, the steps, a worked example, the rules for awkward input, hints you can open if you are stuck, and a checklist.

Two habits apply to every exercise:

- **Let `IOException` through.** A file method usually declares `throws IOException`, and the code that *calls* it decides what a missing file means. This is the rule from [t10 Exception Handling](../../t10_exception_handling/t10_exception_handling_notes.md).
- **Test with temporary files.** Create files with `Files.createTempFile(...)` and delete them afterwards, so your tests do not depend on what is in your project folder.

Each exercise ends with a link to a worked solution and to the tests that check it.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Count lines | `Files.lines`, try-with-resources | Foundation |
| 02 Write a list | `Files.write`, handling `IOException` | Foundation |
| 03 Simple logger | appending, timestamps | Intermediate |
| 04 CSV parser | `Files.readAllLines`, `split`, validation | Intermediate |
| 05 Binary copy | `readAllBytes`, `write` | Foundation |
| 06 Config loader | parsing `key=value` files | Extension |

---

## Exercise 01 — Count lines

**Goal:** Count how many lines in a text file actually contain something. You will read the file lazily with `Files.lines`, which **must be closed**, so you will also use try-with-resources.

**You will need:** [Part 2: Reading text files](../t18_io_notes.md#part-2-reading-text-files).

**Steps**

1. Write the method below.
2. Open the lines of the file with `Files.lines(path)` inside the brackets of a `try` statement.
3. Keep only the lines that are not blank.
4. Count them and return the count as an `int`.

```java
public static int countLines(Path path) throws IOException
```

**Example**

```text
File contents:        Result:
first                 3
(empty line)
second
(three spaces)
third
```

A line that holds only spaces counts as blank.

**Rules**

- An empty file gives `0`.
- A file that does not exist: let the `IOException` escape. Do not catch it.

<details>
<summary>Hints</summary>

1. `String.isBlank()` is `true` for an empty line **and** for a line of spaces.
2. `try (Stream<String> lines = Files.lines(path)) { return (int) lines.filter(l -> !l.isBlank()).count(); }`

</details>

**Done when**

- [ ] The example above gives `3`.
- [ ] An empty file gives `0`.
- [ ] A missing file throws `IOException`.

**Package:** `t18_io.exercises.ex01`

**Solution:** [t18 exercise 01 solution code](../../../../code/src/t18_io/exercises/ex01/).
**Tests:** [t18 exercise 01 tests](../../../../code/test/t18_io/exercises/ex01/).

---

## Exercise 02 — Write a list

**Goal:** Save a list of names to a file, one per line. Then call your method from code that handles the failure, so you see both halves of the "let it through, handle it above" habit.

**You will need:** [Part 3: Writing text files](../t18_io_notes.md#part-3-writing-text-files).

**Steps**

1. Write `writeNames`, below. Use `Files.write(path, names)`, which writes each element on its own line.
2. Write a `run()` method that calls `writeNames` twice: once to a normal path, and once to a path inside a folder that does not exist.
3. In `run()`, catch `IOException` and print a short message that names the file.

```java
public static void writeNames(Path path, List<String> names) throws IOException
```

**Example**

```text
writeNames(Path.of("names.txt"), List.of("Ana", "Ben", "Cara"))

names.txt now contains:
Ana
Ben
Cara
```

**Rules**

- If the file already exists, replace its contents. (`Files.write` does this by default.)
- A `null` list is a programming error: throw `IllegalArgumentException`.
- A folder that does not exist: `writeNames` throws `IOException`, and `run()` reports it.

<details>
<summary>Hints</summary>

1. `Files.write` already throws `IOException`, so `writeNames` needs no `try` at all.
2. In `run()`: `try { writeNames(...); } catch (IOException e) { System.out.println(...); }`

</details>

**Done when**

- [ ] The example above writes three lines.
- [ ] Writing to an existing file replaces what was there.
- [ ] The bad path is reported by `run()`, and the program carries on.

**Package:** `t18_io.exercises.ex02`

**Solution:** [t18 exercise 02 solution code](../../../../code/src/t18_io/exercises/ex02/).
**Tests:** [t18 exercise 02 tests](../../../../code/test/t18_io/exercises/ex02/).

---

## Exercise 03 — Simple logger

**Goal:** Build a small logger that **adds** a timestamped line to a file each time you call it, without wiping what is already there. Servers in t22 keep logs in exactly this way.

**You will need:** [Part 3: Writing text files](../t18_io_notes.md#part-3-writing-text-files) and [Software example: append-only task log](../t18_io_notes.md#software-example-append-only-task-log).

**Steps**

1. Create a class `SimpleLogger` with one `private final Path` field, set in the constructor.
2. Write `log(String message)`. It adds one line to the end of the file, in this format:

   ```text
   [2026-11-03 14:05:09] message
   ```

3. Open the file with **both** `StandardOpenOption.CREATE` and `StandardOpenOption.APPEND`.
4. Write `readLog()`, which returns every line in the file.

```java
public SimpleLogger(Path logFile)
public void log(String message) throws IOException
public List<String> readLog() throws IOException
```

**Example**

```text
logger.log("Server started");
logger.log("Client connected");
logger.readLog() gives:
[2026-11-03 11:40:56] Server started
[2026-11-03 11:40:56] Client connected
```

**Rules**

- The timestamp is the current date and time in the pattern `yyyy-MM-dd HH:mm:ss`.
- `CREATE` makes the file if it is missing; `APPEND` adds to the end instead of overwriting.
- If nothing has been logged yet and the file does not exist, `readLog()` returns an empty list.
- A `null` path in the constructor throws `IllegalArgumentException`.

<details>
<summary>Hints</summary>

1. Format the time with `LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))`.
2. `Files.writeString(file, line + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)`

</details>

**Done when**

- [ ] Two calls to `log` give two lines, in order.
- [ ] Each line starts with a timestamp in the agreed format.
- [ ] `readLog()` on a new logger gives an empty list.

**Package:** `t18_io.exercises.ex03`

**Solution:** [t18 exercise 03 solution code](../../../../code/src/t18_io/exercises/ex03/).
**Tests:** [t18 exercise 03 tests](../../../../code/test/t18_io/exercises/ex03/).

---

## Exercise 04 — CSV parser

**Goal:** Read a comma-separated file of players into rows you can work with, then pick out the high scorers. You will also report a badly formed line clearly, by its line number.

**You will need:** [Part 5: CSV parsing](../t18_io_notes.md#part-5-csv-parsing).

**Steps**

1. Write `parseCsv`. Read every line with `Files.readAllLines(path)`.
2. Skip the first line: it is the header `name,score,level`.
3. Skip blank lines.
4. Split each remaining line on `","`, trim each field, and add the array to the result.
5. Write `scoresAbove`, which keeps only the rows whose score (the second field) is **greater than** `minScore`.

```java
public static List<String[]> parseCsv(Path path) throws IOException
public static List<String[]> scoresAbove(List<String[]> rows, int minScore)
```

**Example**

```text
File:                     parseCsv gives 3 rows.
name,score,level
Alice,95,3                scoresAbove(rows, 80) gives:
Ben,82,2                    Alice | 95 | 3
Clara,78,1                  Ben | 82 | 2
```

**Rules**

- Every data line must have exactly 3 fields. If one does not, throw `IllegalArgumentException` with a message such as `Line 3: expected 3 fields but found 2`. Count lines from 1, including the header.
- A score equal to `minScore` is **not** above it.
- This simple parser does not handle commas inside quotes, such as `"Smith, Jr."`.

<details>
<summary>Hints</summary>

1. A normal `for` loop starting at index `1` skips the header and gives you the line number.
2. `Integer.parseInt(row[1]) > minScore` decides whether a row is kept.

</details>

**Done when**

- [ ] The example file gives 3 rows, and 2 rows above 80.
- [ ] Spaces around fields are removed.
- [ ] A short line reports its line number.

**Package:** `t18_io.exercises.ex04`

**Solution:** [t18 exercise 04 solution code](../../../../code/src/t18_io/exercises/ex04/).
**Tests:** [t18 exercise 04 tests](../../../../code/test/t18_io/exercises/ex04/).

---

## Exercise 05 — Binary copy

**Goal:** Copy any file, such as an image, byte for byte. Binary files are not text, so you read and write **bytes**, never lines.

**You will need:** [Part 4: Binary files](../t18_io_notes.md#part-4-binary-files-byte-arrays).

**Steps**

1. Write the method below.
2. Read the whole source file into a `byte[]` with `Files.readAllBytes`.
3. Write that array to the destination with `Files.write`.
4. To check it, compare the two files' bytes with `Arrays.equals`.

```java
public static void copyBinary(Path source, Path destination) throws IOException
```

**Example**

```text
source contains the 256 byte values 0, 1, 2, ... 255
after copyBinary(source, copy):
Arrays.equals(Files.readAllBytes(source), Files.readAllBytes(copy))  gives  true
```

**Rules**

- If the destination already exists, replace it.
- A missing source: let the `IOException` escape.
- Do not read the file as text. Most byte values are not valid characters, so a text round trip would change them.

<details>
<summary>Hints</summary>

1. The whole method is two lines.
2. To make test data, fill an array with `original[i] = (byte) i;` for `i` from 0 to 255.

</details>

**Done when**

- [ ] All 256 byte values arrive unchanged.
- [ ] A missing source throws `IOException`.

**Package:** `t18_io.exercises.ex05`

**Solution:** [t18 exercise 05 solution code](../../../../code/src/t18_io/exercises/ex05/).
**Tests:** [t18 exercise 05 tests](../../../../code/test/t18_io/exercises/ex05/).

---

## Exercise 06 — Config loader (extension)

**Goal:** Load game settings from a `key=value` file, so values such as the number of players can change without recompiling. Missing or bad values fall back to a default instead of crashing.

**You will need:** [Games example: load level config from file](../t18_io_notes.md#games-example-load-level-config-from-file).

**Steps**

1. Create a class `ConfigLoader` whose constructor takes a `Path` and reads the file straight away.
2. For each line: trim it, and skip it if it is blank, starts with `#`, or has no `=`.
3. Split the line at the **first** `=`. Trim the key and the value, and store them in a map.
4. Add the three methods below.

```java
public ConfigLoader(Path file) throws IOException
public Map<String, String> asMap()
public Optional<String> get(String key)
public int getInt(String key, int defaultValue)
```

**Example**

```text
File:                       Results:
# Game settings             asMap()                  {maxPlayers=8, mapName=forest, volume=loud}
maxPlayers = 8              getInt("maxPlayers", 4)  8
mapName=forest              getInt("volume", 50)     50   (not a number)
volume=loud                 getInt("difficulty", 1)  1    (missing)
```

**Rules**

- `getInt` returns `defaultValue` when the key is missing **or** its value is not a whole number.
- `asMap()` returns a map the caller cannot change.
- A missing file: the constructor throws `IOException`.

<details>
<summary>Hints</summary>

1. `line.indexOf('=')` gives the position of the first `=`. A result of `-1` means there is none.
2. Wrap `Integer.parseInt(value)` in a `try` and return `defaultValue` from the `catch (NumberFormatException e)`.

</details>

**Done when**

- [ ] The example file gives the results shown.
- [ ] Comment lines and lines without `=` are ignored.
- [ ] A missing file throws `IOException`.

**Package:** `t18_io.exercises.ex06`

**Solution:** [t18 exercise 06 solution code](../../../../code/src/t18_io/exercises/ex06/).
**Tests:** [t18 exercise 06 tests](../../../../code/test/t18_io/exercises/ex06/).
