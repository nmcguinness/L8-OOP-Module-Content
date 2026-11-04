# Tests

JUnit 5 tests for the exercise code in [`../src/`](../src/).

## Running them

From the `code/` directory:

```bash
mvn test                # all fast tests (no database needed)
mvn test -Pdb-tests     # ONLY the tests that need a running MySQL
```

In IntelliJ: right-click `code/test` → **Run All Tests**, or click the green arrow beside any test class or method.

## Why the tests are here and not beside the code

`code/test` is a **test source root**, mirroring the package structure of `code/src`. A test for `t01_arrays.exercises.ex01.Exercise` lives at:

```text
code/test/t01_arrays/exercises/ex01/ExerciseTest.java
```

Because the package is identical, a test can call **package-private** methods such as `static int sum(int[] xs)` without those methods being made `public` just to be tested.

## What these tests are for

The worked solutions in `code/src` already pass — so a green run is not the point. Use them in two ways:

1. **As a specification.** The test names and assertions state each method's contract exactly, including what should happen for `null`, empty input and boundary values. Read them before you write your own version.
2. **As a check on your own attempt.** Re-implement an exercise from scratch, point the test at your version, and you get an unambiguous answer instead of squinting at `println` output.

They also mean you meet JUnit from week 1 rather than first encountering it in t23, when it is being assessed.

## Adding a test

Follow the convention already in use:

- One test class per exercise package, named `ExerciseTest`.
- One `@Test` method per scenario, named `method_scenario_expectedBehaviour`.
- Arrange–Act–Assert, visually separated.
- Cover the **normal** case, at least one **boundary** (empty, single element, first/last), and the **invalid** case (`null`, out of range) asserting whatever the contract says — a value, or `assertThrows`.
- If the test needs a live database, annotate the class `@Tag("database")` so a plain `mvn test` still runs everywhere.

See [t23 Unit Testing](../../notes/topics/t23_unit_testing/t23_unit_testing_notes.md) for the reasoning behind these conventions.

## Current coverage

### Foundations

| Topic | Tested |
|:--|:--|
| t01 Arrays | 7 exercise packages |
| t03 Ordering | `Score` (Comparable), `NameAscComparator` |
| t04 Equality & Hashing | `Customer`, `Tag` (the mutable-key hazard) |
| t05 Collections I | 7 exercise packages |
| t06 Collections II | 3 exercise packages |
| t07 Collections III | all 9 exercises: HashSet, HashMap, PriorityQueue ranking, TreeMap, LRU cache, broken hashCode, EnumMap |

### Core

| Topic | Tested |
|:--|:--|
| t08 Inheritance | all 9 exercises: overriding, `super`, abstract types, override vs overload, sealed classes, composition, equals across a hierarchy |
| t09 Interfaces | ex02 (default methods), ex03 (`instanceof` on a second interface), ex04 (Strategy), ex05, ex07, ex08, ex09 and the ce02 formatter pipeline |
| t10 Exception Handling | all 8 exercises: Optional vs exceptions, custom hierarchies, translation with a cause, AutoCloseable and suppressed exceptions (file tests use `@TempDir`) |
| t11 Generics I | all 8 exercises, the de01–de05 demos and the ce03 challenge (`Weapon`, CSV and XML loading) |
| t12 Generics II | 9 of 11 exercises: wildcards, PECS, capture |

### Applied

| Topic | Tested |
|:--|:--|
| t13 Design Patterns I | all 7: Strategy, Command, MacroCommand, undo stack, the two-part ticket dispatcher |
| t14 Design Patterns II | all 6: Factory, Observer, Adapter |
| t15 DAO | the in-memory DAO, and the service rules against a stub — **no MySQL needed** |
| t16 Functional Interfaces | all 6 generic helpers and the comparator factory |
| t17 Streams API | all 6 exercises: filter, map/join, flatMap, groupingBy, statistics, a record pipeline |
| t18 Java I/O | all 6 exercises against `@TempDir` files: counting, writing, logging, CSV, binary copy, config |
| t19 Concurrency | all 5: Runnable, ExecutorService, the race, Callable/Future |
| t20 JSON I | all 5 exercises: POJO and list round trips, `TypeReference`, `@JsonProperty` |
| t21 JSON II | exercises ex02, ex03 and ex06 (the round-trip test class is the ex06 solution); demos de01 and de03 |
| t22 Networking | echo and multi-client servers over loopback; the ex04 and ex05 `RequestDispatcher` with no sockets at all; ex06 and ex07, a testable server and a stoppable one |
| GCA2 reference / t23 | the t23 solutions: `Task` validation, equality and JSON, `ServerResponse`, `ClientRequest`; `TaskDAO` and `DatabaseConnection` (tagged `database`) |

### Supplementary

| Topic | Tested |
|:--|:--|
| t24 Pathfinding | the worked `common` classes (a contract test run against every `Pathfinder`), and exercises ex01, ex02, ex04, ex07 and ex08 option (d) |

### Three techniques worth copying

- **Stub the interface, not the database.** `t15 ex04` tests every rule in `CarRentalService` against a hand-written `StubCarDao`. That is what the `CarDao` interface is *for*: a failure there is a business-rule bug, never a bad password.
- **Observer gives you a seam.** `t14 ex03` and `ex04` assert with a recording listener rather than capturing `System.out` — the pattern already provides the hook.
- **Never assert a race.** `t19 e03` asserts the synchronized counter is exactly 5000 and that the unsynchronized one is *at most* 5000. Asserting that it actually loses an update would pass or fail depending on the machine.

### What is deliberately not tested

Some exercises have no return value to assert against — their whole body is a `run()` that prints. Where printing *is* the behaviour under test, as in t08 ex05, the test captures `System.out`; elsewhere those packages are left alone rather than padded with assertions that prove nothing:

| Not covered | Why |
|:--|:--|
| t09 ex01, ex06 | `greet()` / `execute()` return `void` and only print |
| t11 de03 | a narrative demo of type safety, with no method to call |
| t12 ex01, ex10 | compile-time exercises: the lesson is the code that *fails* to compile |
| t15 ex01, ex03, ex05 | `JdbcCarDao` needs a live MySQL schema |
| t21 de02, t22 de01–de03 | JDBC and socket `main` methods, not units |
