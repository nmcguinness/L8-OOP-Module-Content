# Style Guide

The house style for every piece of Java in this repository: notes, exercise sheets, demos, worked solutions and tests. It follows the [Oracle Code Conventions for the Java Programming Language](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html), with the additions listed under each heading. Where this guide and an older assessment brief disagree, this guide wins.

## Contents

- [Markdown prose](#markdown-prose)
- [Naming](#naming)
- [Layout](#layout)
- [Class member order](#class-member-order)
- [Comments](#comments)
- [Exceptions](#exceptions)
- [Shared names across topics](#shared-names-across-topics)
- [Exercises and solutions](#exercises-and-solutions)

## Markdown prose

**Never break a line inside a paragraph or list item.** Write each paragraph, and each list item, as one unbroken line in the source, however long it is. The editor or browser wraps it to fit the window.

A line break typed into prose looks jagged, especially next to a long link: the text stops early, and the next line starts somewhere unrelated. Those breaks also go stale, because every edit to a sentence leaves the lines around it uneven.

This is wrong:

```markdown
- Start with the official [module
  descriptor](https://example.com/a/very/long/link) to
  understand what we cover.
```

This is right:

```markdown
- Start with the official [module descriptor](https://example.com/a/very/long/link) to understand what we cover.
```

Keep line breaks only where they carry meaning:

- code blocks, Mermaid diagrams, tables and frontmatter;
- a bold label on its own line above its text, such as `**Diagram description**`;
- consecutive labelled lines, such as `**Solution:**` followed by `**Tests:**`.

To separate paragraphs, use a blank line. To show a set of items, use a list. In VS Code, turn on **View > Word Wrap** to read long lines comfortably.

## Naming

| Kind | Style | Example |
|:-|:-|:-|
| Class, interface, enum, record | PascalCase noun | `TaskService`, `RequestType` |
| Method | camelCase verb | `findById`, `isAlive` |
| Field, parameter, local variable | camelCase, **no prefix** | `score`, `playerName` |
| Constant (`static final`) | UPPER_SNAKE_CASE | `MAX_SCORE` |
| Enum constant | UPPER_SNAKE_CASE | `GET_ALL` |
| Package | all lower case | `t07_collections_3_set_map.exercises.ex01` |
| Type parameter | one capital letter | `T`, `K`, `V` |

Fields have **no** `f` or `_` prefix. When a constructor or setter parameter has the same name as a field, use `this`:

```java
private final int score;

public ScoreRecord(int score) {
    this.score = score;
}
```

Write numeric literals of 1 000 or more with underscores: `9_000`, `1_000_000`.

Mark a field `final` when it never changes after construction. Do not mark local variables or parameters `final`: it adds noise without catching many bugs. A class that exists only to hold static methods is a `final` class with a private constructor.

## Layout

- Indent with 4 spaces; never tabs.
- Keep Java code lines under 100 characters. This is longer than Oracle's 80.
- Opening braces go at the end of the line (K&R). `else`, `catch` and `finally` follow the closing brace on the same line: `} else {`.
- Always use braces for `if`, `else`, `for`, `while` and `do`, even around one statement.
- One declaration per line. Do not pad with spaces to line up `=` or names.

```java
if (score > MAX_SCORE) {
    throw new IllegalArgumentException("score too high");
} else {
    this.score = score;
}
```

## Class member order

Following Oracle's order:

1. `static` fields (constants first)
2. Instance fields
3. Constructors
4. Methods, grouped by what they do rather than by access level
5. Nested types

Short section comments such as `// === Fields ===` are allowed but not required.

## Comments

- Use Javadoc (`/** ... */`) on public classes and on public methods whose purpose is not obvious from the name. See [t25 Documenting a Project](../../topics/t25_documenting_a_project/t25_documenting_a_project_notes.md).
- Put a comment on the line **above** the code it explains, never at the end of the line (accessibility rule R6).
- Explain *why*, not *what*. Do not comment a getter.

## Exceptions

- Declare the specific checked exception a method can throw: `throws DaoException`, not `throws Exception`.
- `throws Exception` is allowed only on JUnit test methods and on the `main` method of a throwaway demo.
- Never write `catch (Exception e)` except at the top of a thread or request handler, and then log the exception object.
- When translating an exception, pass the original as the cause: `throw new DaoException("...", e)`.

## Shared names across topics

Use the same name for the same idea in every topic:

| Idea | Name |
|:-|:-|
| Response wrapper sent by a server | `ServerResponse<T>`: `status` is `"OK"` or `"ERROR"`, plus `message` and `data` |
| Request DTO received by a server | `ClientRequest`: `requestType` and `payload` |
| Kinds of request | `RequestType` enum |
| One handler per request type | `RequestHandler` |
| Routing table from type to handler | `RequestDispatcher` |
| Checked exception for storage failures | `DaoException` |
| Database settings loaded from a file | `DbConfig` |

## Exercises and solutions

- Each exercise lives in `code/src/<topic>/exercises/exNN/`, with its tests in `code/test/<topic>/exercises/exNN/`. Demos use `deNN` and challenges use `ceNN`.
- The worked solution exists **only** in `code/src`. An exercise sheet never pastes a copy of the solution code. It ends each exercise with a link:

  ```markdown
  **Solution:** [t15 exercise 02 solution code](../../../../code/src/t15_dao/exercises/ex02/).
  **Tests:** [t15 exercise 02 tests](../../../../code/test/t15_dao/exercises/ex02/).
  ```

- A written answer to a question, with no code, may stay in the sheet inside a collapsible "Answer" box.
