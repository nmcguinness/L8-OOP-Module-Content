---
title: "Exception Handling"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t10_exception_handling
description: "Java exception class hierarchy, checked vs unchecked exceptions, try/catch/finally, multi-catch, custom exceptions, and when to throw vs when to return an Optional."
created: 2026-05-27
last_updated: 2026-05-27
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, exceptions, checked, unchecked, try-catch, custom-exception, year2, comp-c8z03]
difficulty_tier: Foundation
mlos: [MLO1, MLO2]
previous_topic: t09_interface
prerequisites:
  - Interfaces (interface types, method signatures)
  - Inheritance (extends, method overriding, polymorphism)
  - Collections I: ArrayList (basic usage)
---

# Exception Handling

> **Prerequisites:**
> - Interfaces: you can read an interface contract
> - Inheritance: you understand `extends` and polymorphism
> - Collections: you can use `ArrayList` and basic iteration

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [The Java exception hierarchy](#the-java-exception-hierarchy)
- [Part 1: try / catch / finally](#part-1-try--catch--finally)
- [Part 2: multiple catch blocks](#part-2-multiple-catch-blocks)
- [Part 3: checked exceptions and throws](#part-3-checked-exceptions-and-throws)
- [Part 4: custom exceptions](#part-4-custom-exceptions)
- [Progressive coding steps (A then B then C)](#progressive-coding-steps-a-then-b-then-c)
- [Games example: move validation](#games-example-move-validation)
- [Software example: DAO layer with checked
  exceptions](#software-example-dao-layer-with-checked-exceptions)
- [When to throw vs when to return Optional](#when-to-throw-vs-when-to-return-optional)
- [Common mistakes](#common-mistakes)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

A quick summary of what **you** should be able to do after this lesson:

| Skill Type | You will be able to... |
| :-- | :-- |
| Understand | Describe the Java exception hierarchy: `Throwable`, `Error`, `Exception`, `RuntimeException`. |
| Understand | Distinguish checked exceptions (must handle) from unchecked (runtime) exceptions. |
| Use | Write `try`/`catch`/`finally` blocks to handle and recover from errors. |
| Use | Use multi-catch (`catch (A \| B e)`) to reduce repetition. |
| Use | Define and throw custom checked and unchecked exception classes. |
| Use | Declare `throws` on a method signature to propagate checked exceptions. |
| Analyse | Decide when to throw, when to catch, and when to propagate an exception. |
| Debug | Identify exception anti-patterns: swallowing, over-broad catch, and exception as control flow. |

---

## Why this matters

Programs meet unexpected situations: a file is missing, a user enters invalid input, a network
call fails. Without a disciplined approach, the program either crashes with a cryptic stack
trace or silently produces wrong results.

Java's exception mechanism gives you:
- a standard vocabulary for describing what went wrong,
- a way to separate normal logic from error-handling logic,
- compile-time enforcement for errors that callers **must** acknowledge.

---

## How this builds on previous content

| Earlier topic | Concept carried forward |
| :- | :- |
| [t00 Guard clauses](../t00_oop_fundamentals/t00_oop_fundamentals_notes.md) | You have been throwing `IllegalArgumentException` since t00 — this topic explains *why* that one, and when a different choice is right |
| [t08 Inheritance](../t08_inheritance/t08_inheritance_notes.md) | Exceptions are an inheritance hierarchy, so catching a supertype catches every subtype — which is why catch order matters |
| [t09 Interfaces](../t09_interface/t09_interface_notes.md) | `AutoCloseable` is just an interface; implementing it is what makes a class usable in try-with-resources |

Where this goes next: from here on, almost every topic throws something. [t15
DAO](../t15_dao/t15_dao_notes.md) must decide whether "not found" is an exception or an
`Optional`; [t18 Java I/O](../t18_io/t18_io_notes.md) is built on the checked `IOException` and
on try-with-resources; and [t22 Networking](../t22_networking/t22_networking_notes.md) depends
on an exception never escaping `ClientHandler.run()`.

---

## The Java exception hierarchy

Every exception in Java descends from `Throwable`, which splits into two branches:
`Error` for JVM-level failures, and `Exception` for everything your code should care
about. `RuntimeException` is a subclass of `Exception`, and that one relationship is
what separates unchecked from checked.

| Type | Category | What it means |
|:-|:-|:-|
| `Error` | Never catch | JVM-level failure such as `OutOfMemoryError` |
| `IOException` | Checked | File, network or stream failure |
| `SQLException` | Checked | Database failure |
| `RuntimeException` | Unchecked | Programming error, to be fixed rather than caught |
| `NullPointerException` | Unchecked | A method was called on `null` |
| `ArrayIndexOutOfBoundsException` | Unchecked | Index outside the valid range |
| `IllegalArgumentException` | Unchecked | A caller passed an unacceptable value |
| `IllegalStateException` | Unchecked | The object is not in a state where this call makes sense |

Your own checked exceptions extend `Exception` directly; your own unchecked ones extend
`RuntimeException`.

- **Checked exceptions** — extend `Exception` (not `RuntimeException`). The compiler forces
  callers to either handle or declare them with `throws`.
- **Unchecked exceptions** — extend `RuntimeException`. No compiler enforcement; they indicate
  programming errors that should be fixed, not caught.
- **Errors** — extend `Error`. Represent JVM-level failures. Never catch these.

```kroki-plantuml
' alt: Java exception hierarchy — checked vs unchecked vs Error
@startuml
skinparam backgroundColor white
skinparam ClassFontName monospaced
skinparam ClassBackgroundColor #F8F8F8
skinparam ClassBorderColor #777
skinparam ArrowColor #444
skinparam NoteBackgroundColor #FFF8E1
skinparam NoteBorderColor #888

skinparam class {
    BackgroundColor<<error>>     #F3E5F5
    BackgroundColor<<checked>>   #E8F5E9
    BackgroundColor<<unchecked>> #FFEBEE
}

class Throwable

class Error <<error>>
class Exception

class IOException              <<checked>>
class SQLException             <<checked>>
class RuntimeException         <<unchecked>>
class NullPointerException     <<unchecked>>
class IllegalArgumentException <<unchecked>>
class ArrayIndexOutOfBoundsException <<unchecked>>
class IllegalStateException    <<unchecked>>

note right of Error            : JVM-level failure\nnever catch
note right of IOException      : checked: compiler forces\nhandle or declare
note right of RuntimeException : unchecked: programming\nerror — fix, don't catch

Throwable <|-- Error
Throwable <|-- Exception
Exception <|-- IOException
Exception <|-- SQLException
Exception <|-- RuntimeException
RuntimeException <|-- NullPointerException
RuntimeException <|-- IllegalArgumentException
RuntimeException <|-- ArrayIndexOutOfBoundsException
RuntimeException <|-- IllegalStateException
@enduml
```

---

## Part 1: try / catch / finally

```java
try {
    // Code that might throw
    int result = Integer.parseInt("abc"); // throws NumberFormatException
    System.out.println(result);
} catch (NumberFormatException e) {
    // Handle the specific exception
    System.out.println("Not a valid number: " + e.getMessage());
} finally {
    // Always runs — use for cleanup (close resources, log, reset state)
    System.out.println("Done");
}
```

Key rules:
- `catch` receives the exception object — use `e.getMessage()` and
  `e.getClass().getSimpleName()` for useful output.
- `finally` runs whether or not an exception was thrown, and whether or not it was caught.
- `finally` runs even if `catch` rethrows.

---

## Part 2: multiple catch blocks

```java
public static int readInt(String[] args, int index) {
    try {
        return Integer.parseInt(args[index]);
    } catch (ArrayIndexOutOfBoundsException e) {
        System.err.println("No argument at index " + index);
        return 0;
    } catch (NumberFormatException e) {
        System.err.println("Argument is not a number: " + args[index]);
        return 0;
    }
}
```

- Catch blocks are checked **top to bottom** — put more specific types first.
- A supertype catch (e.g. `catch (Exception e)`) placed first will absorb everything — usually
  wrong.

### Multi-catch (Java 7+)

```java
try {
    // ...
} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
    System.err.println("Input error: " + e.getMessage());
}
```

Use multi-catch when the handling logic is identical and the exception types are unrelated.

---

## Part 3: checked exceptions and throws

A method that calls code which throws a checked exception must either:
1. **Handle it** with `try`/`catch`, or
2. **Propagate it** by declaring `throws` in the signature.

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Option A: handle it here
public static String readFirstLine(String path) {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    } catch (IOException e) {
        System.err.println("Could not read file: " + e.getMessage());
        return null;
    }
}

// Option B: propagate — caller decides
public static String readFirstLineRaw(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
}
```

> **Try-with-resources** (`try (Resource r = ...)`) automatically closes `AutoCloseable`
> resources when the block exits — even on exception. Prefer it over `finally { r.close(); }`.

---

## Part 4: custom exceptions

### Unchecked (most common for domain errors)

```java
public class InvalidMoveException extends RuntimeException {

    private final int _row;
    private final int _col;

    public InvalidMoveException(int row, int col) {
        super("Invalid move at (" + row + ", " + col + ")");
        _row = row;
        _col = col;
    }

    public int getRow() { return _row; }
    public int getCol() { return _col; }
}
```

Throw it:

```java
public void makeMove(int row, int col) {
    if (row < 0 || row >= _size || col < 0 || col >= _size) {
        throw new InvalidMoveException(row, col);
    }
    // ...
}
```

---

### Checked (for recoverable errors that callers must acknowledge)

```java
public class TaskNotFoundException extends Exception {

    private final int _taskId;

    public TaskNotFoundException(int taskId) {
        super("Task not found: id=" + taskId);
        _taskId = taskId;
    }

    public int getTaskId() { return _taskId; }
}
```

```java
public Task findById(int id) throws TaskNotFoundException {
    for (Task t : _tasks) {
        if (t.getId() == id) return t;
    }
    throw new TaskNotFoundException(id);
}
```

---

## Progressive coding steps (A then B then C)

### Step A — Basic guard with unchecked exception

```java
public class BoundedStack<T> {

    private final Object[] _data;
    private int _top = 0;

    public BoundedStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive, got: " + capacity);
        }
        _data = new Object[capacity];
    }

    public void push(T item) {
        if (_top == _data.length) {
            throw new IllegalStateException("Stack is full");
        }
        _data[_top++] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (_top == 0) {
            throw new IllegalStateException("Stack is empty");
        }
        return (T) _data[--_top];
    }
}
```

---

### Step B — Propagate and handle a checked exception

```java
public interface ConfigLoader {
    Map<String, String> load(String path) throws IOException;
}

public class FileConfigLoader implements ConfigLoader {
    @Override
    public Map<String, String> load(String path) throws IOException {
        Map<String, String> config = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    config.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return config;
    }
}

// In main or a service:
ConfigLoader loader = new FileConfigLoader();
try {
    Map<String, String> cfg = loader.load("settings.properties");
    System.out.println(cfg);
} catch (IOException e) {
    System.err.println("Config load failed: " + e.getMessage());
}
```

---

### Step C — Custom exception hierarchy

```java
// Base domain exception
public class GameException extends RuntimeException {
    public GameException(String message) { super(message); }
    public GameException(String message, Throwable cause) { super(message, cause); }
}

// Specific subtypes
public class InvalidMoveException extends GameException {
    public InvalidMoveException(int row, int col) {
        super("Invalid move at (" + row + ", " + col + ")");
    }
}

public class GameOverException extends GameException {
    private final String _winner;
    public GameOverException(String winner) {
        super("Game over — winner: " + winner);
        _winner = winner;
    }
    public String getWinner() { return _winner; }
}
```

Callers can catch the base type `GameException` to handle any game error, or catch a specific
subtype for targeted recovery.

---

## Games example: move validation

```java
public class ChessBoard {

    private final char[][] _board;
    private final int _size = 8;

    public ChessBoard() {
        _board = new char[_size][_size];
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        validateCoordinate(fromRow, fromCol, "from");
        validateCoordinate(toRow, toCol, "to");

        if (_board[fromRow][fromCol] == 0) {
            throw new InvalidMoveException(fromRow, fromCol); // no piece there
        }

        _board[toRow][toCol] = _board[fromRow][fromCol];
        _board[fromRow][fromCol] = 0;
    }

    private void validateCoordinate(int row, int col, String label) {
        if (row < 0 || row >= _size || col < 0 || col >= _size) {
            throw new IllegalArgumentException(
                "Coordinate " + label + " (" + row + "," + col + ") out of bounds"
            );
        }
    }
}
```

Usage:

```java
ChessBoard board = new ChessBoard();
try {
    board.movePiece(0, 0, 8, 0);   // out of bounds
} catch (IllegalArgumentException e) {
    System.err.println("Bad coordinates: " + e.getMessage());
} catch (InvalidMoveException e) {
    System.err.println("No piece at: (" + e.getRow() + "," + e.getCol() + ")");
}
```

---

## Software example: DAO layer with checked exceptions

```java
public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(int id) { super("Task not found: " + id); }
}

public interface TaskDao {
    Task findById(int id) throws TaskNotFoundException;
    void save(Task task) throws IOException;
}

public class InMemoryTaskDao implements TaskDao {

    private final Map<Integer, Task> _store = new HashMap<>();

    @Override
    public Task findById(int id) throws TaskNotFoundException {
        Task t = _store.get(id);
        if (t == null) throw new TaskNotFoundException(id);
        return t;
    }

    @Override
    public void save(Task task) {
        _store.put(task.getId(), task);
    }
}

// Service layer catches and translates
public class TaskService {

    private final TaskDao _dao;

    public TaskService(TaskDao dao) { _dao = dao; }

    public Optional<Task> getTask(int id) {
        try {
            return Optional.of(_dao.findById(id));
        } catch (TaskNotFoundException e) {
            return Optional.empty();
        }
    }
}
```

---

## When to throw vs when to return Optional

| Situation | Prefer |
| :-- | :-- |
| Caller **must** acknowledge the error (e.g. file not found) | Checked exception (`throws`) |
| Programming error — invalid argument, violated invariant | Unchecked (`IllegalArgumentException`, etc.) |
| "Not found" is a normal, expected outcome | `Optional<T>` |
| Error in infrastructure (DB, network) that upper layers handle | Checked exception, or wrap in unchecked domain exception |
| Validating user input at the boundary | Return `false`/`Optional` or throw a domain `ValidationException` |

---

## Common mistakes

| Anti-pattern | Problem | Better approach |
| :-- | :-- | :-- |
| `catch (Exception e) {}` — swallowing | Error silently disappears; root cause lost | At minimum log `e.getMessage()` and rethrow or return a sentinel |
| `catch (Exception e)` — too broad | Catches `NullPointerException`, `StackOverflowError`, everything | Catch the specific type you can actually handle |
| Exception as control flow | `try { find() } catch (NotFoundException) { return default; }` in a tight loop | Use `containsKey`/`Optional` instead; exceptions are expensive |
| Losing the cause | `throw new MyException("message")` without passing original `e` | `throw new MyException("message", e)` — preserves the stack trace chain |
| Empty `finally` closing nothing | Forgetting to close resources | Use try-with-resources |

---

## Reflective questions

- What is the difference between a checked and an unchecked exception? Give one example of each
  from the Java standard library.
- When is it appropriate to define your own exception class instead of using a built-in one?
- Why is `catch (Exception e) {}` considered dangerous?
- Should a `findById` method in a DAO throw a checked exception or return an `Optional`? What
  factors influence the decision?
- How does try-with-resources work, and what interface must a resource implement to use it?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**Checked vs unchecked.**
A **checked** exception extends `Exception` but not `RuntimeException`, and the compiler forces
every caller to catch it or declare `throws`. It models a failure that is outside the program's
control and that a caller can reasonably be expected to handle — `IOException`, `SQLException`.

An **unchecked** exception extends `RuntimeException` and carries no compiler obligation. It
models a **programming error** — something that should be fixed in code rather than handled at
runtime: `IllegalArgumentException`, `NullPointerException`, `IndexOutOfBoundsException`.

The distinction is about *who is at fault*. A missing file is the world's fault, so the caller
must decide what to do. A null argument is the caller's fault, so it should crash loudly during
development.

**When to define your own exception class.**
When the failure is **specific to your domain** and a caller might want to catch *that* case
distinctly. `InsufficientFundsException` can be caught on its own; `IllegalArgumentException`
cannot be separated from every other bad argument in the call. Custom types also carry
structured data — `TaskNotFoundException` can hold the offending `id` as a field rather than
burying it in a message string.

Do **not** invent one when a standard exception says the same thing. A null argument is
`IllegalArgumentException` (or `NullPointerException`) and wrapping it in `MyNullException`
gains nothing while making your API harder to learn. Default to unchecked for domain errors
unless the caller genuinely has a recovery path.

**Why `catch (Exception e) {}` is dangerous.**
It is two separate mistakes at once. The **empty body** discards the error entirely: the
program continues in an unknown state, and the eventual symptom — corrupt data, a wrong total,
a `NullPointerException` three layers away — has no connection to the real cause. Debugging
this is miserable because the stack trace pointing at the true failure was thrown away.

The **broad type** compounds it: `Exception` catches `NullPointerException`,
`IllegalStateException` and every other bug you did not anticipate, so genuine programming
errors are silently swallowed along with the one case you meant to handle. Catch the narrowest
type you can actually do something about, and if you must catch broadly, at minimum log with
the exception attached (`log(e)` — passing the object, not just `e.getMessage()`, so the stack
trace survives) and then rethrow.

**Should DAO `findById` throw or return `Optional`?**
Return `Optional<Task>`. "Not found" is a **normal, expected outcome** of a lookup, not an
error — a user typing an ID that does not exist is routine. Exceptions should signal the
exceptional; using them for ordinary control flow is slow and makes callers write `try`/`catch`
around something that is not a failure. `Optional` also puts the absence in the *type*, so the
compiler pushes the caller to handle it, whereas returning `null` invites a
`NullPointerException`.

The factors that would change the answer: is absence expected or genuinely exceptional
(`findById` on an ID you just inserted *would* be exceptional); can the caller do something
useful; and is there a real failure underneath — a dropped database connection is an
`SQLException` and must not be flattened into an empty `Optional`. Distinguish "no such row"
from "could not ask".

**How try-with-resources works.**
Declare the resource in the parentheses of `try` and Java closes it automatically when the
block exits — normally, via `return`, or by exception:

```java
try (BufferedReader br = Files.newBufferedReader(path)) {
    return br.readLine();
}   // br.close() runs here, whatever happened
```

The resource must implement **`AutoCloseable`** (or `Closeable`, which extends it). Multiple
resources are separated by semicolons and closed in **reverse** order of declaration.

It is strictly better than `finally { r.close(); }` for two reasons students usually miss.
First, `close()` can itself throw, and in the manual version that second exception **replaces**
the original — you lose the real cause. Try-with-resources keeps the original and attaches the
close failure as a *suppressed* exception. Second, it cannot be forgotten or misplaced, which
is the usual outcome of hand-written cleanup.

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t09_interface
  domain_emphasis: Balanced

this_lesson:
  topic_code: t10_exception_handling
  primary_domain_emphasis: Balanced
  difficulty_tier: Foundation
mlos: [MLO1, MLO2]
```
