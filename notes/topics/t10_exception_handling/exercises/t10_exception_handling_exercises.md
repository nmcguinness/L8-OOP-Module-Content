---
title: "Exception Handling — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t10_exception_handling
description: "Eight step-by-step exercises on exception handling: safe parsing, custom checked and unchecked exceptions, Optional versus exceptions, try-with-resources, an exception hierarchy, exception translation, a custom AutoCloseable, and fixing anti-patterns."
created: 2026-05-29
last_updated: 2026-11-03
version: 1.2
status: published
authors: ["OOP Teaching Team"]
tags: [java, exceptions, checked, unchecked, try-catch, try-with-resources, custom-exception, exercises, year2, comp-c8z03]
---
# Exception Handling — Exercises

These exercises practise throwing, catching and designing exceptions, from the [t10 notes](../t10_exception_handling_notes.md). Two words come up in almost every exercise:

- A **checked** exception must be caught or declared with `throws`. The compiler checks this. Use one for a problem the caller can sensibly recover from, such as a missing file.
- An **unchecked** exception extends `RuntimeException`. The compiler does not check it. Use one for a bug in the calling code, such as passing a negative amount.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution and its tests. The test names describe exactly what each method must do, so reading them is a good way to check you have understood the task.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Safe number parsing | catching an exception, returning `Optional` | Foundation |
| 02 A custom unchecked exception | `extends RuntimeException`, carrying data | Foundation |
| 03 Optional or exception? | choosing between them for a lookup | Foundation |
| 04 Reading a file safely | try-with-resources, passing `IOException` on | Foundation |
| 05 An exception hierarchy | a base exception and subtypes | Intermediate |
| 06 Exception translation | wrapping a low-level cause | Intermediate |
| 07 Your own AutoCloseable | try-with-resources, suppressed exceptions | Intermediate |
| 08 Fix the anti-patterns | refactoring broken handling | Stretch |

---

## Exercise 01 — Safe number parsing

**Goal:** Turn text into a number without letting a bad input crash the program. The method catches the exception itself and returns an empty `Optional` instead.

**You will need:**
[Core Idea 2](../t10_exception_handling_notes.md#core-idea-2--try--catch--finally) and [Core Idea 8](../t10_exception_handling_notes.md#core-idea-8--when-to-throw-and-when-to-return-optional).

**Steps**

1. In a class `Exercise`, write the method below.
2. If `input` is `null`, return `Optional.empty()` straight away.
3. Inside a `try`, call `Integer.parseInt(input.trim())` and return the result wrapped in `Optional.of(...)`.
4. Catch `NumberFormatException` and return `Optional.empty()`.
5. In `run()`, call it on each input in the example and print the result.

```java
public static Optional<Integer> parseIntSafe(String input)
```

**Example**

```text
"42" -> Optional[42]
" -7 " -> Optional[-7]
"abc" -> Optional.empty
"" -> Optional.empty
"99999999999" -> Optional.empty
"null" -> Optional.empty
```

**Rules**

- The method has no `throws` clause. Nothing escapes it.
- Spaces at either end are ignored.
- An empty string, and a number too large for an `int`, both give an empty `Optional`.

<details>
<summary>Hints</summary>

1. Catch only `NumberFormatException`. Catching `Exception` would also hide a real bug, such as a `NullPointerException` you did not expect.
2. `parseInt` already throws `NumberFormatException` for `""` and for `"99999999999"`, so you do not need separate checks for them.

</details>

**Done when**

- [ ] `run()` prints the six lines shown.

**Package:** `t10_exception_handling.exercises.ex01`

**Solution:** [t10 exercise 01 solution code](../../../../code/src/t10_exception_handling/exercises/ex01/).
**Tests:** [t10 exercise 01 tests](../../../../code/test/t10_exception_handling/exercises/ex01/).

---

## Exercise 02 — A custom unchecked exception

**Goal:** Write your own exception class that carries the bad value with it, and throw it when a score is out of range.

**You will need:** [Core Idea 6](../t10_exception_handling_notes.md#core-idea-6--custom-exceptions).

**Steps**

1. Create `InvalidScoreException`, which extends `RuntimeException`. Its constructor takes the bad score, stores it in a field, and passes a message to `super(...)`.
2. Add a getter `getScore()` that returns the stored value.
3. Create a class `ScoreRecord` with a private `score` field and the two methods below.
4. In `setScore`, throw `InvalidScoreException` if the score is below 0 or above 100. Check **before** you change the field.
5. In `run()`, set the score to 75, then try 101 and -5, catching the exception each time, then set it to 100.

```java
public void setScore(int score)
public int score()
```

**Example**

```text
Set score to 75
Rejected 101: Score must be 0 to 100, got 101 (score stays 75)
Rejected -5: Score must be 0 to 100, got -5 (score stays 75)
Set score to 100
```

**Rules**

- A rejected call leaves the previous score unchanged.
- The message names the bad value: `Score must be 0 to 100, got 101`.

<details>
<summary>Hints</summary>

1. `super("Score must be 0 to 100, got " + score);` sets the message that `getMessage()` returns.
2. Because the exception is unchecked, `setScore` does not need a `throws` clause.

</details>

**Done when**

- [ ] `run()` prints the four lines shown.
- [ ] Catching the exception, you can read the bad value back with `getScore()`.

**Think about it:** why is an unchecked exception the right choice here, rather than a checked one? Who made the mistake when a score of 101 is passed in?

**Package:** `t10_exception_handling.exercises.ex02`

**Solution:** [t10 exercise 02 solution code](../../../../code/src/t10_exception_handling/exercises/ex02/).
**Tests:** [t10 exercise 02 tests](../../../../code/test/t10_exception_handling/exercises/ex02/).

---

## Exercise 03 — Optional or exception?

**Goal:** Write two lookups for the same data, for two different situations. One treats "not found" as normal and returns an `Optional`. The other treats it as a real problem and throws.

**You will need:** [Core Idea 4](../t10_exception_handling_notes.md#core-idea-4--checked-exceptions-and-throws) and [Core Idea 8](../t10_exception_handling_notes.md#core-idea-8--when-to-throw-and-when-to-return-optional).

**Steps**

1. Create a **checked** exception `PlayerNotFoundException`, which extends `Exception`. It stores the missing name and returns it from `getName()`.
2. Create `PlayerRepository`. Its constructor takes a `Map<String, Integer>` of names to scores and stores a **copy** of it.
3. Write `findScore`, for a search box where the user may type any name. It returns `Optional.empty()` for an unknown name.
4. Write `requireScore`, for a name taken from your own records, which must exist. It throws `PlayerNotFoundException` for an unknown name.
5. In `run()`, create a repository with `ana` (90) and `ben` (75). Search for `zoe` with `findScore`, then call `requireScore` for `ana` and `zoe` inside one `try`/`catch`.

```java
public Optional<Integer> findScore(String name)
public int requireScore(String name) throws PlayerNotFoundException
```

**Example**

```text
Search 'zoe': no such player
Ana's score: 90
Data problem: No player named 'zoe'
```

**Rules**

- `findScore` never throws for an unknown name.
- Changing the caller's map after creating the repository does not change the repository.

<details>
<summary>Hints</summary>

1. `new HashMap<>(scores)` makes a copy, so later changes to the original do not reach it.
2. `Optional.ofNullable(scores.get(name))` gives an empty `Optional` when the name is missing.
3. To print the search result: `repo.findScore("zoe").map(String::valueOf).orElse("no such player")`.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.
- [ ] The line for Zoe's score from `requireScore` is never printed. Can you say why?

**Think about it:**

1. Why is "not found" an exception for `requireScore` but not for `findScore`?
2. If you only had time to write one of the two methods, which would you choose, and why?

**Package:** `t10_exception_handling.exercises.ex03`

**Solution:** [t10 exercise 03 solution code](../../../../code/src/t10_exception_handling/exercises/ex03/).
**Tests:** [t10 exercise 03 tests](../../../../code/test/t10_exception_handling/exercises/ex03/).

---

## Exercise 04 — Reading a file safely

**Goal:** Read a file in a way that always closes it, even if reading fails, and let the caller decide what a missing file means.

**You will need:** [Core Idea 5](../t10_exception_handling_notes.md#core-idea-5--try-with-resources-and-autocloseable).

**Steps**

1. In a class `Exercise`, write the method below.
2. If `path` is `null`, throw an `IllegalArgumentException`.
3. Open a reader in a try-with-resources statement: `Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)`.
4. Read lines with `readLine()` until it returns `null`, adding each one to a list. Return the list.
5. In `run()`, call `readLines` on `data/exercises/profane_words.csv` and on `no/such/file.txt`. Catch `IOException` and print the exception's class name and message.

```java
public static List<String> readLines(String path) throws IOException
```

**Example**

```text
data/exercises/profane_words.csv: 10 lines
no/such/file.txt: could not read (NoSuchFileException: no\such\file.txt)
```

**Rules**

- Do not catch `IOException` inside `readLines`. Declare it with `throws`.
- Keep blank lines. An empty file gives an empty list.

<details>
<summary>Hints</summary>

1. Try-with-resources looks like `try (BufferedReader reader = ...) { ... }`. The reader is closed when the block ends, however it ends.
2. A `try` with resources needs no `catch` and no `finally`.
3. `e.getClass().getSimpleName()` gives the class name, such as `NoSuchFileException`.

</details>

**Done when**

- [ ] `run()` prints two lines like those shown. On macOS or Linux the path uses `/`.

**Package:** `t10_exception_handling.exercises.ex04`

**Solution:** [t10 exercise 04 solution code](../../../../code/src/t10_exception_handling/exercises/ex04/).
**Tests:** [t10 exercise 04 tests](../../../../code/test/t10_exception_handling/exercises/ex04/).

---

## Exercise 05 — An exception hierarchy

**Goal:** Design a small family of exceptions for a bank account, so a caller can catch one specific problem or all bank problems at once.

**You will need:** [Core Idea 1](../t10_exception_handling_notes.md#core-idea-1--the-java-exception-hierarchy) and [Step C — Custom exception hierarchy](../t10_exception_handling_notes.md#step-c--custom-exception-hierarchy).

**Steps**

1. Create the three exceptions in the table below.
2. Create `BankAccount` with the methods below. It stores an id, a balance and whether it is locked.
3. `deposit` and `withdraw` throw `IllegalArgumentException` for an amount of zero or less.
4. `withdraw` throws `InsufficientFundsException` if the amount is more than the balance.
5. After `lock()`, `deposit` and `withdraw` both throw `AccountLockedException`.
6. In `run()`, open an account `IE-001` with 100, deposit 50, then try to withdraw 500, deposit -10, and withdraw 10 after locking. Catch the last one with `catch (BankException e)`.

| Exception | Extends | Carries |
| :-- | :-- | :-- |
| `BankException` | `RuntimeException` | nothing extra; the base type |
| `InsufficientFundsException` | `BankException` | the amount requested and the balance |
| `AccountLockedException` | `BankException` | the account id |

```java
public BankAccount(String id, double openingBalance)
public void deposit(double amount)
public void withdraw(double amount)
public void lock()
public double balance()
```

**Example**

```text
Balance after deposit: 150.0
Cannot withdraw 500.00: balance is 150.00
Bad input: amount must be positive: -10.0
AccountLockedException: Account IE-001 is locked
```

**Rules**

- A failed `withdraw` leaves the balance unchanged.
- A bad amount uses the standard `IllegalArgumentException`, not a bank exception. It is the caller's bug, not a bank rule.

<details>
<summary>Hints</summary>

1. Check the amount first, then the lock, then the balance. Each check throws before anything changes.
2. `String.format("Cannot withdraw %.2f: balance is %.2f", amount, balance)` gives two decimal places.
3. `catch (BankException e)` catches both subtypes, because each one **is a** `BankException`.

</details>

**Done when**

- [ ] `run()` prints the four lines shown.
- [ ] One `catch (BankException e)` would handle either bank error.

> **Watch out:** real banking code never stores money in a `double`, because a `double` cannot hold most decimal amounts exactly. It uses whole cents in a `long`, or `BigDecimal`.

**Package:** `t10_exception_handling.exercises.ex05`

**Solution:** [t10 exercise 05 solution code](../../../../code/src/t10_exception_handling/exercises/ex05/).
**Tests:** [t10 exercise 05 tests](../../../../code/test/t10_exception_handling/exercises/ex05/).

---

## Exercise 06 — Exception translation

**Goal:** Read a settings file and report every problem as one exception type of your own. A low-level `IOException` is wrapped inside it, so the caller does not need to know how the settings are stored.

**You will need:** [Core Idea 7](../t10_exception_handling_notes.md#core-idea-7--exception-chaining-and-translation).

**Steps**

1. Create a **checked** exception `ConfigException` with the two constructors below. `getLineNumber()` returns the line number, or `0` when the problem is not tied to one line.
2. Create `SettingsLoader` with a static method `load(Path file)`. It reads lines of the form `key=value` into a map.
3. Skip blank lines and lines that start with `#`. Trim each key and value.
4. A line with no `=`, or nothing before the `=`, throws `ConfigException` with its line number.
5. A key that appears twice throws `ConfigException` with the line number of the second one.
6. Wrap the reading in `catch (IOException e)` and throw `new ConfigException("...", e)`.
7. Write `loadOrDefaults(Path file)`. It catches `ConfigException`, prints why, and returns a map of default settings: `volume=50` and `difficulty=normal`.
8. In `run()`, try it on a good file, a file with a bad line, and a missing file.

```java
public ConfigException(String message, int lineNumber)
public ConfigException(String message, Throwable cause)
public static Map<String, String> load(Path file) throws ConfigException
```

**Example** (the good file holds `# audio`, `volume=80` and `difficulty = hard`)

```text
{volume=80, difficulty=hard}
Using defaults. Reason: Expected key=value but found 'this line is wrong' (line 2)
{volume=50, difficulty=normal}
Using defaults. Reason: Cannot read settings file missing-settings.txt
  caused by NoSuchFileException
{volume=50, difficulty=normal}
```

**Rules**

- The `IOException` is kept as the **cause**, never thrown away.
- Keep the keys in the order they appear in the file.

<details>
<summary>Hints</summary>

1. `line.indexOf('=')` gives `-1` when there is no `=`, and `0` when nothing comes before it.
2. A `LinkedHashMap` keeps keys in the order you add them.
3. `super(message, cause)` stores the cause. The caller reads it with `e.getCause()`.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] For the missing file, `e.getCause()` is a `NoSuchFileException`.

**Package:** `t10_exception_handling.exercises.ex06`

**Solution:** [t10 exercise 06 solution code](../../../../code/src/t10_exception_handling/exercises/ex06/).
**Tests:** [t10 exercise 06 tests](../../../../code/test/t10_exception_handling/exercises/ex06/).

---

## Exercise 07 — Your own AutoCloseable

**Goal:** Write a class that try-with-resources can close for you, and see that it is closed even when the code inside the block throws.

**You will need:** [Core Idea 5](../t10_exception_handling_notes.md#core-idea-5--try-with-resources-and-autocloseable).

**Steps**

1. Create `AuditLog`, which implements `AutoCloseable`. Its constructor takes a `List<String>` (the "sink") and adds `"opened"` to it.
2. `write(String event)` adds the event to the sink. After the log is closed, it throws `IllegalStateException`.
3. `close()` adds `"closed"` to the sink. Calling it a second time does nothing.
4. In `Exercise`, write `runSession`. It opens an `AuditLog` in try-with-resources and writes `"login"`. If `failMidway` is `true` it throws `IllegalStateException("session crashed")`, otherwise it writes `"logout"`.
5. In `run()`, call `runSession` once with `false` and once with `true`, printing the sink each time.

```java
public AuditLog(List<String> sink)
public void write(String event)
public boolean isClosed()
public void close()
public static void runSession(List<String> sink, boolean failMidway)
```

**Example**

```text
Normal session: [opened, login, logout, closed]
Crashed session: [opened, login, closed] (session crashed)
Primary: work failed
Suppressed: close failed
```

The last two lines come from the extension below.

**Rules**

- `close()` does not declare `throws Exception`. Leave it off, so callers need no `catch`.
- `runSession` does not catch the exception. It reaches the caller, but only after the log closes.

<details>
<summary>Hints</summary>

1. Keep a `boolean closed` field. In `close()`, `if (closed) { return; }` comes first.
2. Notice that `"closed"` is in the crashed sink even though the block never reached its end.

</details>

**Done when**

- [ ] `run()` prints the first two lines shown.
- [ ] Calling `close()` twice adds `"closed"` only once.

**Extension:** write `FlakyResource`, whose `work()` throws `IllegalStateException("work failed")` and whose `close()` throws `IllegalStateException("close failed")`. Use it in try-with-resources, catch the exception, and print its message and each of `e.getSuppressed()`. Which message is the main one, and why is that the right choice?

**Package:** `t10_exception_handling.exercises.ex07`

**Solution:** [t10 exercise 07 solution code](../../../../code/src/t10_exception_handling/exercises/ex07/).
**Tests:** [t10 exercise 07 tests](../../../../code/test/t10_exception_handling/exercises/ex07/).

---

## Exercise 08 — Fix the anti-patterns

**Goal:** Find what is wrong with some badly written exception handling, then rewrite it so that every problem is reported clearly to the caller.

**You will need:** [Common mistakes](../t10_exception_handling_notes.md#common-mistakes) and [Core Idea 7](../t10_exception_handling_notes.md#core-idea-7--exception-chaining-and-translation).

A teammate wrote this method to add up the scores in a file, one whole number per line:

```java
public static int totalScore(String path) {
    int total = 0;
    BufferedReader reader = null;
    try {
        reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            total += Integer.parseInt(line);
        }
    } catch (Exception e) {
        // ignore
    } finally {
        try {
            reader.close();
        } catch (IOException e) {
        }
    }
    return total;
}
```

**Steps**

1. Find at least four problems. For each one, name the mistake from the notes and say what goes wrong.
2. Create a **checked** exception `ScoreFileException` with `getLineNumber()`, and constructors that take a cause.
3. In a class `ScoreFileReader`, rewrite the method with the signature below, using try-with-resources.
4. Skip blank lines and trim each line.
5. A line that is not a whole number throws `ScoreFileException` with its line number and the `NumberFormatException` as its cause.
6. An `IOException` throws `ScoreFileException` with the `IOException` as its cause.
7. In `run()`, total a good file, a file whose second line is `twenty`, and a missing file.

```java
public static int totalScore(Path file) throws ScoreFileException
```

**Example** (the good file holds `10`, `20`, a blank line and `30`)

```text
Total: 60
Error: Not a whole number: 'twenty' (line 2) [cause: NumberFormatException]
Error: Cannot read missing-scores.txt [cause: NoSuchFileException]
```

**Rules**

- No `catch (Exception e)` anywhere.
- No `finally` block. Try-with-resources does the closing.

<details>
<summary>Hints</summary>

1. Ask: what happens if the file does not exist? Which variable is still `null` in the `finally` block?
2. Ask: what does the caller see if line 2 says `twenty`? Can it tell that from a file of zeros?
3. Count lines with an `int lineNumber` that you add one to each time round the loop.

</details>

**Done when**

- [ ] You have listed four problems, each with a name and a consequence.
- [ ] `run()` prints the three lines shown.

<details>
<summary>Answer: the problems in the original</summary>

1. **Swallowed exception:** `catch (Exception e) { }` hides every failure, so a broken file looks like a total of 0.
2. **Catching too broadly:** `Exception` also catches bugs, such as a `NullPointerException`.
3. **NullPointerException in `finally`:** if the file does not exist, `reader` is still `null`, and `reader.close()` throws.
4. **Manual closing:** the `finally` block is what try-with-resources does for you, correctly.
5. **Lost information:** the caller cannot tell which line was bad, or why.

</details>

**Package:** `t10_exception_handling.exercises.ex08`

**Solution:** [t10 exercise 08 solution code](../../../../code/src/t10_exception_handling/exercises/ex08/).
**Tests:** [t10 exercise 08 tests](../../../../code/test/t10_exception_handling/exercises/ex08/).
