# Style Guide

The house style for every piece of Java in this repository: notes, exercise sheets, demos, worked solutions and tests. It follows the [Oracle Code Conventions for the Java Programming Language](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html), with the additions listed under each heading. Where this guide and an older assessment brief disagree, this guide wins.

## Contents

- [Style Guide](#style-guide)
  - [Contents](#contents)
  - [Naming](#naming)
  - [Layout](#layout)
  - [Class member order](#class-member-order)
  - [Regions](#regions)
- [Comments](#comments)
  - [Exceptions](#exceptions)

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

## Regions

Use regions to group the members of a class, so that a reader can fold away what they do not need. IntelliJ shows a fold arrow beside each one. To add a region, select the code, press **Ctrl+Alt+T**, and choose **region...endregion Comments**.

```java
public class Player {

    //region Constants
    private static final int MAX_SCORE = 100;
    //endregion

    //region Fields
    private final String name;
    private int score;
    //endregion

    //region Constructors
    public Player(String name) {
        this.name = name;
    }
    //endregion

    //region Public API
    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score = Math.min(MAX_SCORE, score + points);
    }
    //endregion

    //region Overrides
    @Override
    public String toString() {
        return name + ": " + score;
    }
    //endregion
}
```

- Use these names, in this order, leaving out any you do not need: `Constants`, `Fields`, `Constructors`, `Public API`, `Helpers`, `Overrides`.
- Close every `//region` with an `//endregion`. Do not nest regions, and do not put one inside a method.
- Skip regions in a small class. If a record or a class with two short methods fits on one screen, they only add noise.
- A class that needs many regions may be doing too many jobs. Consider splitting it.

Code supplied with the module, such as demos and worked solutions, may use section comments such as `// === Fields ===`, or no sections at all. Use regions in the code you write.

## Comments

- Use Javadoc (`/** ... */`) on public classes and on public methods whose purpose is not obvious from the name. See [t25 Documenting a Project](../../topics/t25_documenting_a_project/t25_documenting_a_project_notes.md).
- Put a comment on the line **above** the code it explains, never at the end of the line.
- Explain *why*, not *what*. Do not comment a getter.

## Exceptions

- Declare the specific checked exception a method can throw: `throws DaoException`, not `throws Exception`.
- `throws Exception` is allowed only on JUnit test methods and on the `main` method of a throwaway demo.
- Never write `catch (Exception e)` except at the top of a thread or request handler, and then log the exception object.
- When translating an exception, pass the original as the cause: `throw new DaoException("...", e)`.

