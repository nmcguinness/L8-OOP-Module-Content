---
title: "Unit Testing — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t23_unit_testing
description: "Eight step-by-step testing exercises against the GCA2 reference code: validation, equality, response factories, payload accessors, JSON round trips, DAO integration tests, spotting worthless tests, and reading a coverage report."
created: 2026-05-29
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, junit5, testing, dao, json, coverage, intellij, exercises, year2, comp-c8z03]
---
# Unit Testing — Exercises

These exercises are different from every other topic's. You do not write code that *does* something: you write **tests for code that already exists**, and you find out what it really does.

The code under test is the GCA2 reference in [`code/src/assessments/gca/gca2/`](../../../../code/src/assessments/gca/gca2/). Open these classes before you start; you will test all of them:

| Class | What it is |
| :-- | :-- |
| `domain/Task` | A task with an id, title, description and completed flag |
| `server/ServerResponse<T>` | The reply envelope, with `ok(...)` and `error(...)` factories |
| `server/ClientRequest` | A request with a type and a `Map<String, Object>` payload |
| `dao/TaskDAO` | Insert, find and delete tasks in MySQL |

Your tests go in **`code/test/`**, in the same package as the class they test. Every exercise follows the same layout: a goal, the steps, the tests to write, the rules, hints you can open if you are stuck, and a checklist.

**Conventions** (see the [t23 notes, Part 1](../t23_unit_testing_notes.md#part-1--anatomy-of-a-good-test)):

- One test class per class under test, named `XxxTest`.
- One `@Test` per scenario, named `method_scenario_expectedBehaviour`.
- Each test has three parts, Arrange, Act and Assert, separated by a blank line.

**How to run:** click the green arrow beside a test in IntelliJ, or from `code/` run `mvn test`. Tests that need MySQL run only with `mvn test -Pdb-tests`.

| Exercise | You test | Difficulty |
| :-- | :-- | :-- |
| 01 Validation | `Task`'s guard clauses | Foundation |
| 02 Equality | `Task.equals` and `hashCode` | Foundation |
| 03 Response factories | `ServerResponse.ok` and `error` | Foundation |
| 04 Payload accessors | what `ClientRequest` really returns | Intermediate |
| 05 JSON round trips | `Task` through Jackson and back | Intermediate |
| 06 DAO tests | `TaskDAO` against MySQL | Intermediate, needs MySQL |
| 07 Worthless tests | spotting tests that prove nothing | Foundation, no coding |
| 08 Coverage | reading a coverage report | Extension, no coding |

---

## Exercise 01 — Validation

**Goal:** Prove that `Task` rejects bad titles and tidies good ones. These "guard clauses" are the first line of defence, so they deserve the first tests.

**You will need:** [Part 2 — DTO / entity validation tests](../t23_unit_testing_notes.md#part-2--dto--entity-validation-tests).

**Steps**

1. Read `Task.setTitle` and `Task.setDescription`, and write down what each one should do.
2. Create `TaskTest` in `code/test/assessments/gca/gca2/domain/`.
3. Write the four tests below. Use `assertThrows` for the two that expect an exception.

```java
setTitle_validTitle_storesTrimmedValue()
setTitle_null_throwsIllegalArgumentException()
setTitle_blank_throwsIllegalArgumentException()
setDescription_null_storesEmptyString()
```

**Example**

```text
setTitle("  Fix login  ")   then getTitle()        gives "Fix login"
setTitle(null)                                     throws IllegalArgumentException
setTitle("   ")                                    throws IllegalArgumentException
setDescription(null)        then getDescription()  gives ""
```

**Rules**

- The valid-title test must pass a title **with spaces around it**. A test that uses `"Fix login"` already trimmed proves nothing about trimming.

<details>
<summary>Hints</summary>

1. `assertThrows(IllegalArgumentException.class, () -> task.setTitle(null));`
2. Start each test with `Task task = new Task();`. The no-argument constructor exists for Jackson, and is handy here too.

</details>

**Done when**

- [ ] All four tests pass.
- [ ] Deleting `.trim()` from `setTitle` makes the first test fail.

**Package:** `assessments.gca.gca2.domain`

**Solution:** [TaskTest.java](../../../../code/test/assessments/gca/gca2/domain/TaskTest.java).

---

## Exercise 02 — Equality

**Goal:** Prove that `Task.equals` and `hashCode` follow the rules from t04, so `Task` works correctly in a `HashSet` and in `assertEquals`.

**You will need:**
[t04 Equality and Hashing](../../t04_equality_hashing/t04_equality_hashing_notes.md).

**Steps**

1. Read `Task.equals` and `Task.hashCode`. Note which fields each one uses.
2. Create `TaskEqualityTest` in the same package.
3. Write one test for each line of the example.

**Example**

```text
Task(1, "Fix login", "desc", false) equals Task(1, "Fix login", "desc", false)   true
Task(1, ...) equals Task(2, ...), all other fields the same                     false
two equal tasks                                                         same hashCode
task.equals(null)                                                                false
task.equals("some string")                                                       false
add two equal tasks to a HashSet                                          set size is 1
```

**Rules**

- Name each test after the behaviour, for example `equals_differentTaskIdOnly_isFalse`.

<details>
<summary>Hints</summary>

1. Use `assertEquals(a, b)` and `assertNotEquals(a, b)`. Both call `equals` for you.
2. For `equals(null)`, call it directly: `assertFalse(a.equals(null))`.

</details>

**Done when**

- [ ] All six behaviours are tested and pass.

**Think about it:** `hashCode()` uses only `taskId`, but `equals` compares all four fields. Is that allowed? Which direction of the equals–hashCode rule must always hold, and does `Task` keep it?

**Package:** `assessments.gca.gca2.domain`

**Solution:**
[TaskEqualityTest.java](../../../../code/test/assessments/gca/gca2/domain/TaskEqualityTest.java).

---

## Exercise 03 — Response factories

**Goal:** Prove that `ServerResponse.ok(...)` and `ServerResponse.error(...)` build the replies the protocol promises. Every client relies on these two methods.

**You will need:** [`ServerResponse<T>` tests](../t23_unit_testing_notes.md#serverresponset-tests).

**Steps**

1. Create `ServerResponseTest` in `code/test/assessments/gca/gca2/server/`.
2. Write the four tests below.

```java
ok_withData_setsStatusOkAndKeepsData()
ok_withData_isOkReturnsTrue()
error_withMessage_setsStatusErrorAndNullData()
error_withMessage_isOkReturnsFalse()
```

**Example**

```text
ok("found", "payload")   status "OK",    message "found",     data "payload",  isOk() true
error("not found")       status "ERROR", message "not found", data null,       isOk() false
```

**Rules**

- Check the status, the message **and** the data in the first and third tests.

<details>
<summary>Hints</summary>

1. `assertNull(response.getData())` checks the error case's data.
2. Use a `String` as the data. The type does not matter to these tests.

</details>

**Done when**

- [ ] All four tests pass.

**Stretch:** `isOk()` is written `"OK".equals(status)`, not `status.equals("OK")`. Write a test that would fail if someone swapped them. (What happens to `status.equals("OK")` when `status` is `null`?)

**Package:** `assessments.gca.gca2.server`

**Solution:**
[ServerResponseTest.java](../../../../code/test/assessments/gca/gca2/server/ServerResponseTest.java), including the stretch test.

---

## Exercise 04 — Payload accessors

**Goal:** Find out what `ClientRequest.getString`, `getInt` and `getBoolean` *really* return when the client sends something unexpected, and record it in tests. Here, the tests document the code rather than the code you wish you had.

**You will need:** [`ClientRequest` tests](../t23_unit_testing_notes.md#clientrequest-tests).

**Steps**

1. Read the three method bodies first. Write down what you think each returns for a missing key and for a value of the wrong type.
2. Create `ClientRequestTest` in the `server` package.
3. For **each** of the three methods, write three tests: the key is present with the right type, the key is missing, and the key is present with the wrong type.
4. Build each request with `setPayload(map)`, using a `HashMap` you fill yourself.

**Example**

```text
getString("title")   "Fix login" gives "Fix login"   missing gives null    5 (a number) gives "5"
getInt("id")         42 gives 42                     missing gives -1      "42" (text) gives -1
getBoolean("done")   true gives true                 missing gives false   "true" (text) gives false
```

**Rules**

- Assert what the method **actually** does, even where that is surprising. If a missing key silently gives `-1`, your test asserts `-1`.

<details>
<summary>Hints</summary>

1. `getInt` accepts any `Number`. Try `4.9` as well: what comes back?
2. `getString` calls `toString()` on whatever it finds, so it never rejects a wrong type.

</details>

**Done when**

- [ ] Nine tests pass, three per method.

**Think about it:** a client sends `{"id":"42"}`, and `getInt("id")` returns `-1`. The server then looks up task `-1` and replies "not found". Is that a good contract? What would be better? (Compare t21 Exercise 03.)

**Package:** `assessments.gca.gca2.server`

**Solution:**
[ClientRequestTest.java](../../../../code/test/assessments/gca/gca2/server/ClientRequestTest.java).

---

## Exercise 05 — JSON round trips

**Goal:** Prove a `Task` survives being turned into JSON and back. If this fails, the client and the server no longer agree on what a task looks like.

**You will need:** [Part 4 — JSON round-trip tests](../t23_unit_testing_notes.md#part-4--json-round-trip-tests).

**Steps**

1. Create `TaskJsonTest` in the `domain` package, with one `ObjectMapper` field.
2. `task_roundTrip_equalsOriginal`: serialise a `Task`, deserialise it, and `assertEquals` the two.
3. `taskList_roundTrip_preservesSizeAndFirstElement`: do the same with a list of two tasks.

```java
task_roundTrip_equalsOriginal()
taskList_roundTrip_preservesSizeAndFirstElement()
```

**Example**

```text
Task(7, "Fix login", "Users cannot sign in", true)
  to JSON and back gives a Task equal to the original
[task 1, task 2]
  to JSON and back gives a list of size 2, whose first element equals task 1
```

**Rules**

- For the list, use `new TypeReference<List<Task>>() { }`. `List.class` loses the element type, and you get maps back instead of tasks.

<details>
<summary>Hints</summary>

1. `assertEquals(original, restored)` works only because `Task` overrides `equals`. That is why Exercise 02 comes first.
2. Comment out `Task.equals` for a moment: the first test now fails on correct code.

</details>

**Done when**

- [ ] Both tests pass.

**Package:** `assessments.gca.gca2.domain`

**Solution:**
[TaskJsonTest.java](../../../../code/test/assessments/gca/gca2/domain/TaskJsonTest.java).

---

## Exercise 06 — DAO tests (needs MySQL)

**Goal:** Test `TaskDAO` against a real database. These are **integration tests**: they check that the SQL and the row mapping work, which no unit test can.

**You will need:** [Part 3 — DAO integration tests](../t23_unit_testing_notes.md#part-3--dao-integration-tests).

**Steps**

1. Create a **separate test database**, never your development one. In phpMyAdmin run:

   ```sql
   CREATE DATABASE gca2_test_db;
   USE gca2_test_db;
   CREATE TABLE tasks (
       task_id     INT AUTO_INCREMENT PRIMARY KEY,
       title       VARCHAR(255) NOT NULL,
       description TEXT,
       completed   BOOLEAN      NOT NULL DEFAULT FALSE
   );
   ```

2. Create `TaskDAOTest` in the `dao` package, and annotate the class `@Tag("database")`, so a plain `mvn test` still passes on machines without MySQL.
3. Add a `@BeforeEach` method that empties the table with `TRUNCATE TABLE tasks`.
4. Write the six tests below.

```java
insert_validTask_returnsTaskWithPositiveId()
findById_existingId_returnsPresentOptional()
findById_unknownId_returnsEmptyOptional()
findAll_emptyTable_returnsEmptyListNotNull()
deleteById_existingTask_returnsTrueAndRemovesRow()
deleteById_unknownId_returnsFalse()
```

**Example**

```text
insert(new Task(0, "Fix login", "desc", false))   returns the task with taskId > 0
findById(that id)                                  gives a task equal to the saved one
findById(9999)                                     gives Optional.empty
findAll() on an empty table                        gives an empty list, not null
deleteById(that id)                                gives true, and findById then gives empty
deleteById(9999)                                   gives false
```

**Rules**

- Each test sets up its own data. No test may rely on another test having run first.
- `TaskDAO` has no update method, so there is no update test.

> **Watch out:** a test that asserts `findAll().size() == 3` passes only until someone leaves a row behind. The `@BeforeEach` reset is what makes every test independent of the others and of their order.

<details>
<summary>Hints</summary>

1. `TaskDAO` needs a `DatabaseConnection`: `new TaskDAO(new DatabaseConnection("jdbc:mysql://localhost:3306/gca2_test_db", "root", ""))`.
2. `insert` returns the same `Task` with its new id filled in. Use that id in the next call.

</details>

**Done when**

- [ ] All six tests pass with `mvn test -Pdb-tests`.
- [ ] A plain `mvn test` still passes, with these tests skipped.

**Package:** `assessments.gca.gca2.dao`

**Solution:** [TaskDAOTest.java](../../../../code/test/assessments/gca/gca2/dao/TaskDAOTest.java).

---

## Exercise 07 — Worthless tests (no coding)

**Goal:** Learn to recognise a test that passes but proves nothing. Each test below compiles and passes. For each one, explain in one sentence why it proves nothing, then rewrite it so that it would catch a real bug.

**You will need:**
[What makes a test worthless](../t23_unit_testing_notes.md#what-makes-a-test-worthless).

```java
@Test void testTask() {
    Task t = new Task(1, "Fix login", "desc", false);
    assertNotNull(t);
}

@Test void testGetTitle() {
    Task t = new Task(1, "Fix login", "desc", false);
    t.setTitle("Fix login");
    assertEquals("Fix login", t.getTitle());
}

@Test void testEverything() {
    Task t = new Task(1, "A", "b", false);
    assertEquals(1, t.getTaskId());
    assertTrue(ServerResponse.ok("m", t).isOk());
    assertFalse(ServerResponse.error("m").isOk());
}
```

**Hint for the third one:** it does check real things. The problem is what happens when one of its three unrelated checks fails: what does the failure report tell you?

<details>
<summary>Answer</summary>

1. **`testTask`**: `new` never returns `null`, so `assertNotNull` cannot fail. It would still pass if the constructor stored nothing. Better: assert the stored values, for example that `getTitle()` gives `"Fix login"`, or that a blank title throws.
2. **`testGetTitle`**: the title it sets is the title it already had, and it is already trimmed, so the test passes even if `setTitle` does nothing. Better: set `"  New title  "` and assert `getTitle()` gives `"New title"`.
3. **`testEverything`**: it tests two classes and three behaviours. When it fails, the name `testEverything` says nothing about what broke, and the first failing assert hides the others. Better: three tests, each named after the one behaviour it checks.

</details>

**Done when**

- [ ] You have one sentence and one rewritten test for each of the three.

---

## Exercise 08 — Coverage (extension, no coding)

**Goal:** Run your tests with coverage, and learn what the percentage does and does not tell you.

**You will need:** [Part 6 — What ≥70% coverage means](../t23_unit_testing_notes.md#part-6--what-70-coverage-means).

**Steps**

1. In IntelliJ, right-click `code/test/assessments` and choose **Run with Coverage**.
2. Open the coverage window and find the `assessments.gca.gca2` classes.
3. In a file `coverage-notes.md`, answer the three questions below, and add a screenshot of the coverage window.

**Questions**

1. Which class has the **lowest** line coverage, and which lines are not covered?
2. Are the missed lines mostly the normal path, or mostly error handling? Why is that typical?
3. Name one class where you could raise the percentage with tests that prove **nothing**, and say how you would spot such tests in someone else's work.

<details>
<summary>What a good answer covers</summary>

- The lowest-covered classes are usually the networking ones (`TaskServer`, `ClientHandler`), because their code needs a live socket, and the DAO when the database tests have not run.
- Missed lines are mostly **error handling**: `catch` blocks and guard clauses only run when something goes wrong, and tests often check only the normal path.
- A getter-only class such as `ServerResponse` can reach 100% with tests like Exercise 07's `testTask`, which call every method but assert nothing useful. Spot them by looking for weak asserts (`assertNotNull` on a new object) and tests whose name says nothing about a behaviour.

</details>

**Done when**

- [ ] `coverage-notes.md` answers all three questions and includes the screenshot.

> **Remember:** 70% coverage is the floor, not the goal. A fully green suite at 80% can still miss a real bug, as Exercise 07 shows.
