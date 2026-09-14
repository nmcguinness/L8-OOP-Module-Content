---
title: "Unit Testing — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t23_unit_testing
description: "Eight exercises writing JUnit 5 tests against the GCA2 reference: validation, equality, factories, JSON round-trips, tagged DAO tests, and coverage."
created: 2026-09-09
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, junit5, testing, dao, json, coverage, intellij, exercises, year2, comp-c8z03]
---
# Unit Testing — Exercises

These exercises are different from every other topic's: you are not writing code that
does something, you are writing **tests for code that already exists**.

Everything you need is already in the repository. The classes under test live in
[`code/src/assessments/gca/gca2/`](../../../../code/src/assessments/gca/gca2/) — the
GCA2 N-tier reference — and your tests go in **`code/test/`**, mirroring the package.

## How to run your tests

From the `code/` directory:

```bash
mvn test                # all fast tests
mvn test -Pdb-tests     # only the tests that need MySQL running
```

Or in IntelliJ, click the green arrow beside a test class or method.

> :bulb: Read the existing tests in [`code/test/t01_arrays/`](../../../../code/test/t01_arrays/)
> first. They show the conventions these exercises expect: one class per unit named
> `XxxTest`, one `@Test` per scenario named `method_scenario_expectedBehaviour`, and
> Arrange–Act–Assert visually separated.

---

## Exercise 01 — Entity validation tests

Test the guard clauses on `Task`. Create `TaskTest` and cover:

```java
// in code/test/assessments/gca/gca2/domain/TaskTest.java
setTitle_validTitle_storesTrimmedValue()
setTitle_null_throwsIllegalArgumentException()
setTitle_blank_throwsIllegalArgumentException()
setDescription_null_storesEmptyString()
```

Use `assertThrows` for the two rejection cases. Note that `setTitle` **trims** — so
`"  Fix login  "` must be stored as `"Fix login"`, and a test that only checks the happy
path with an already-trimmed string proves nothing about that behaviour.

**Package:** `assessments.gca.gca2.domain`

**Pitfall:** `assertNotNull(new Task())` is not a test. It passes even if every field
assignment is deleted.

---

## Exercise 02 — Equality and hashing tests

`Task` overrides `equals` and `hashCode`. Write `TaskEqualityTest` covering:

- Two tasks with identical field values are equal.
- Two tasks differing only by `taskId` are **not** equal.
- Equal tasks produce equal hash codes.
- `equals(null)` returns `false`, and `equals("some string")` returns `false`.
- Adding two equal tasks to a `HashSet` results in a set of size 1.

**Package:** `assessments.gca.gca2.domain`

**Discussion:** `hashCode()` uses only `_taskId`, but `equals` compares all four fields.
Is that legal? Which direction of the contract must hold, and is it satisfied here?

---

## Exercise 03 — `ServerResponse<T>` factory tests

`ServerResponse` has two static factories. Write `ServerResponseTest` asserting that:

```java
ok_withData_setsStatusOkAndKeepsData()
ok_withData_isOkReturnsTrue()
error_withMessage_setsStatusErrorAndNullData()
error_withMessage_isOkReturnsFalse()
```

**Package:** `assessments.gca.gca2.server`

**Stretch:** `isOk()` is written as `"OK".equals(_status)` rather than
`_status.equals("OK")`. Write a test that would fail if the operands were swapped.

---

## Exercise 04 — `ClientRequest` payload accessor tests

`ClientRequest` reads values out of a `Map<String, Object>` payload. Write
`ClientRequestTest` covering, for `getString`, `getInt` and `getBoolean`:

- the key is present and the right type — the value comes back;
- the key is **missing** — assert whatever the method actually does;
- the key is present but the **wrong type**.

**Package:** `assessments.gca.gca2.server`

**Important:** read the method bodies before writing the assertions. Your job is to
document the real behaviour, not the behaviour you assume. If a missing key silently
returns `0`, that is what your test should assert — and then ask yourself in the
oral defence whether that is a good contract.

---

## Exercise 05 — JSON round-trip tests

Write `TaskJsonTest` that serialises a `Task` with Jackson and deserialises it back:

```java
task_roundTrip_equalsOriginal()
taskList_roundTrip_preservesSizeAndFirstElement()
```

Use a single `ObjectMapper`. For the list, remember `List.class` loses the element type —
use `new TypeReference<List<Task>>() {}`.

**Package:** `assessments.gca.gca2.domain`

**Why this works at all:** `assertEquals(original, restored)` calls `equals`. Comment out
`Task.equals` and this test fails on perfectly correct code — which is exactly why
Exercise 02 comes first.

---

## Exercise 06 — DAO integration tests (needs MySQL)

Write `TaskDAOTest` against a **dedicated test database** — never your development one.
Annotate the class `@Tag("database")` so a plain `mvn test` still passes for everyone.

Use `@BeforeEach` to reset the table to a known state, then cover:

```java
insert_validTask_returnsPositiveId()
findById_existingId_returnsPresentOptional()
findById_unknownId_returnsEmptyOptional()
findAll_emptyTable_returnsEmptyListNotNull()
update_existingTask_returnsTrue()
deleteById_unknownId_returnsFalse()
```

**Package:** `assessments.gca.gca2.dao`

**Pitfall:** a test that asserts `findAll().size() == 3` passes only until someone leaves
a row behind. Reset state in `@BeforeEach` so every test is independent of the others and
of the order they run in.

---

## Exercise 07 — Find the worthless tests (no coding)

Each of these compiles and passes. Explain in one sentence why each proves nothing, then
rewrite it into a test that would actually catch a bug.

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

**Deliverable:** a short markdown answer, no package required.

**Hint for the third one:** it does assert real things. The problem is what happens to
your ability to diagnose a failure when one of three unrelated assertions breaks.

---

## Exercise 08 — Coverage report (extension)

Run your tests with coverage in IntelliJ (**Run to Run with Coverage**) against
`assessments.gca.gca2`. Then answer, in a short markdown file:

1. Which class has the **lowest** line coverage, and which specific lines are missed?
2. Are the missed lines mostly happy path, or mostly error handling? Why is that typical?
3. Name one class where you could raise the percentage with tests that prove **nothing**,
   and say how you would spot such tests in someone else's submission.

**Deliverable:** `coverage-notes.md` plus a screenshot of the coverage window.

**Remember:** ≥70% is the floor, not the goal. A green suite at 80% is entirely
compatible with a bug in production — Exercise 07 shows why.
