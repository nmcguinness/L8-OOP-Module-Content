---
title: "Streams API — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t17_streams_api
description: "Six step-by-step exercises on streams: filter and sort, map and join, flatMap, groupingBy, sum/average/max, and a record pipeline."
created: 2026-05-29
last_updated: 2026-11-03
version: 1.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, streams, functional, lambda, collectors, pipeline, exercises, year2, comp-c8z03]
---
# Streams API — Exercises

These exercises practise the stream pipeline from the [t17 notes](../t17_streams_api_notes.md): a **source**, then zero or more **intermediate operations**, then one **terminal operation**. Every exercise follows the same layout: a goal, the steps, a worked example, the rules for awkward input, hints you can open if you are stuck, and a checklist.

Write each method as a `public static` method in a class called `Exercise`, in the package named under the exercise. Try the steps on your own before opening the hints. Each exercise ends with a link to a worked solution and to the tests that check it.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Long names | `filter`, `sorted`, `toList` | Foundation |
| 02 Shout and join | `map`, `Collectors.joining` | Foundation |
| 03 Unique tags | `flatMap`, `distinct` | Intermediate |
| 04 Group by length | `Collectors.groupingBy` | Intermediate |
| 05 Score statistics | `mapToInt`, `sum`, `average`, `max` | Foundation |
| 06 Student report | records with `groupingBy` and `averagingInt` | Extension |

---

## Exercise 01 — Long names

**Goal:** Pick out the long product names from a list and sort them, using one stream pipeline instead of a loop. This filter-then-sort shape is the stream you will write most often.

**You will need:** [Part 2: Intermediate operations](../t17_streams_api_notes.md#part-2-intermediate-operations).

**Steps**

1. Write the method below.
2. Start a stream from `products`.
3. Keep only the names whose length is **greater than** `minLength`.
4. Sort what is left into alphabetical order.
5. Collect the result into a list.

```java
public static List<String> longNames(List<String> products, int minLength)
```

**Example**

```text
products  = ["Lamp", "Bookshelf", "Desk", "Armchair"]
minLength = 4
result    = ["Armchair", "Bookshelf"]
```

"Lamp" and "Desk" have exactly 4 letters. That is not *greater than* 4, so they are left out.

**Rules**

- An empty list or `null` gives an empty list.
- Do not change the list you were given.

<details>
<summary>Hints</summary>

1. Which operation keeps some elements and drops the rest? Which one puts them in order?
2. `products.stream().filter(p -> p.length() > minLength).sorted().toList()`

</details>

**Done when**

- [ ] The example above gives `["Armchair", "Bookshelf"]`.
- [ ] A name of exactly `minLength` letters is left out.
- [ ] An empty list gives an empty list.

**Package:** `t17_streams_api.exercises.ex01`

**Solution:** [t17 exercise 01 solution code](../../../../code/src/t17_streams_api/exercises/ex01/).
**Tests:** [t17 exercise 01 tests](../../../../code/test/t17_streams_api/exercises/ex01/).

---

## Exercise 02 — Shout and join

**Goal:** Turn every word into capitals and join them into one string with a separator. You will use `map`, which changes each element, and a collector, which combines them into one result.

**You will need:** [Part 2](../t17_streams_api_notes.md#part-2-intermediate-operations) for `map` and [Part 4: Collectors](../t17_streams_api_notes.md#part-4-collectors) for `joining`.

**Steps**

1. Write the method below.
2. Start a stream from `words`.
3. Change each word to upper case.
4. Join the words with `" | "` (space, bar, space) between them.

```java
public static String upperJoined(List<String> words)
```

**Example**

```text
words  = ["red", "green", "blue"]
result = "RED | GREEN | BLUE"
```

**Rules**

- A list with one word gives that word in capitals, with no separator.
- An empty list or `null` gives an empty string, `""`.

<details>
<summary>Hints</summary>

1. `String::toUpperCase` is a method reference you can pass to `map`.
2. Finish with `.collect(Collectors.joining(" | "))`.

</details>

**Done when**

- [ ] The example above gives `"RED | GREEN | BLUE"`.
- [ ] One word gives no separator.
- [ ] An empty list gives `""`.

**Package:** `t17_streams_api.exercises.ex02`

**Solution:** [t17 exercise 02 solution code](../../../../code/src/t17_streams_api/exercises/ex02/).
**Tests:** [t17 exercise 02 tests](../../../../code/test/t17_streams_api/exercises/ex02/).

---

## Exercise 03 — Unique tags

**Goal:** Each line of text holds several tags separated by commas. Produce one sorted list of every different tag. You will use `flatMap`, which turns **one** element into **many**.

**You will need:** [flatMap — one-to-many](../t17_streams_api_notes.md#flatmap--one-to-many).

**Steps**

1. Write the method below.
2. Start a stream from `tagLines`.
3. Split each line on `","` and flatten the pieces into one stream of tags.
4. Trim spaces from each tag, and drop any tag that is empty.
5. Remove duplicates, sort, and collect into a list.

```java
public static List<String> allUniqueTags(List<String> tagLines)
```

**Example**

```text
tagLines = ["java,oop,design", "oop, testing", "design"]
result   = ["design", "java", "oop", "testing"]
```

`" testing"` has a space before it; trimming makes it `"testing"`.

**Rules**

- An empty tag, such as the gap in `"a,,b"`, is ignored.
- An empty list or `null` gives an empty list.

<details>
<summary>Hints</summary>

1. `map` would give you a stream of **arrays**. You need a stream of **strings**. Which operation flattens?
2. `.flatMap(line -> Arrays.stream(line.split(",")))`, then `.map(String::trim)`, `.filter(t -> !t.isEmpty())`, `.distinct()`, `.sorted()`.

</details>

**Done when**

- [ ] The example above gives the four tags shown.
- [ ] `" a ,, b ,"` gives `["a", "b"]`.
- [ ] An empty list gives an empty list.

**Package:** `t17_streams_api.exercises.ex03`

**Solution:** [t17 exercise 03 solution code](../../../../code/src/t17_streams_api/exercises/ex03/).
**Tests:** [t17 exercise 03 tests](../../../../code/test/t17_streams_api/exercises/ex03/).

---

## Exercise 04 — Group by length

**Goal:** Sort words into groups by how many letters they have. `Collectors.groupingBy` builds the map for you, so there is no "check if the key exists" code.

**You will need:** [Part 4: Collectors](../t17_streams_api_notes.md#part-4-collectors).

**Steps**

1. Write the method below. The result maps a **length** to the **list of words** with that length.
2. Stream the words and collect them with `Collectors.groupingBy`, using the word's length as the key.
3. Make the keys come out shortest first, by asking `groupingBy` for a `TreeMap`.

```java
public static Map<Integer, List<String>> groupByLength(List<String> words)
```

**Example**

```text
words  = ["cat", "horse", "dog", "mouse", "ox"]
result = {2=[ox], 3=[cat, dog], 5=[horse, mouse]}
```

Inside each group, the words stay in the order they were given.

**Rules**

- An empty list or `null` gives an empty map.

<details>
<summary>Hints</summary>

1. The key is `String::length`. The simplest form is `Collectors.groupingBy(String::length)`, but its keys come out in no particular order.
2. The three-argument form fixes the order: `Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList())`.

</details>

**Done when**

- [ ] The example above gives the three groups shown.
- [ ] The keys come out in increasing order.
- [ ] An empty list gives an empty map.

**Package:** `t17_streams_api.exercises.ex04`

**Solution:** [t17 exercise 04 solution code](../../../../code/src/t17_streams_api/exercises/ex04/).
**Tests:** [t17 exercise 04 tests](../../../../code/test/t17_streams_api/exercises/ex04/).

---

## Exercise 05 — Score statistics

**Goal:** Work out the total, the average and the highest of a list of scores, with no loops. Two of the answers can be "nothing" when the list is empty, and the return types say so.

**You will need:**
[Primitive specialisations](../t17_streams_api_notes.md#primitive-specialisations) and [Part 3: Terminal operations](../t17_streams_api_notes.md#part-3-terminal-operations).

**Steps**

1. Write the three methods below.
2. For `sum` and `average`, convert to an `IntStream` with `mapToInt(Integer::intValue)`. It has `sum()` and `average()` built in.
3. For `max`, use the stream's `max` with a comparator.

```java
public static int sum(List<Integer> scores)
public static OptionalDouble average(List<Integer> scores)
public static Optional<Integer> max(List<Integer> scores)
```

**Example**

```text
scores  = [70, 85, 90, 55]
sum     = 300
average = OptionalDouble[75.0]
max     = Optional[90]
```

**Rules**

- An empty list or `null`: `sum` is `0`, and `average` and `max` are **empty**.
- There is no average of nothing, which is why `average` returns `OptionalDouble`, not `double`.

<details>
<summary>Hints</summary>

1. `IntStream.average()` already returns `OptionalDouble`, so you can return it directly.
2. `scores.stream().max(Integer::compare)` returns `Optional<Integer>`.

</details>

**Done when**

- [ ] The example above gives 300, 75.0 and 90.
- [ ] An empty list gives `0`, `OptionalDouble.empty` and `Optional.empty`.

**Package:** `t17_streams_api.exercises.ex05`

**Solution:** [t17 exercise 05 solution code](../../../../code/src/t17_streams_api/exercises/ex05/).
**Tests:** [t17 exercise 05 tests](../../../../code/test/t17_streams_api/exercises/ex05/).

---

## Exercise 06 — Student report (extension)

**Goal:** Build two small reports from a list of student records: the average grade in each subject, and the names of everyone who passed. This combines grouping, averaging and filtering.

**You will need:** [Part 4: Collectors](../t17_streams_api_notes.md#part-4-collectors) and [Software example: task report pipeline](../t17_streams_api_notes.md#software-example-task-report-pipeline).

**Steps**

1. Declare the record `public record Student(String name, String subject, int grade) { }`.
2. Write `avgGradeBySubject`: group by `subject` and average the `grade` in each group. Return the subjects in alphabetical order.
3. Write `passedNames`: keep students with a grade of **40 or more**, take their names, and sort them.

```java
public static Map<String, Double> avgGradeBySubject(List<Student> students)
public static List<String> passedNames(List<Student> students)
```

**Example**

```text
students = [Ana, Maths, 72], [Ben, Maths, 38], [Cara, Art, 40], [Dev, Art, 90]

avgGradeBySubject = {Art=65.0, Maths=55.0}
passedNames       = [Ana, Cara, Dev]
```

Cara has exactly 40, which is a pass.

**Rules**

- The pass mark is 40, and 40 itself passes.
- An empty list or `null` gives an empty map and an empty list.

<details>
<summary>Hints</summary>

1. `groupingBy` takes a third argument that says what to do with each group. You want an average of an `int` field.
2. `Collectors.groupingBy(Student::subject, TreeMap::new, Collectors.averagingInt(Student::grade))`

</details>

**Done when**

- [ ] The example above gives `{Art=65.0, Maths=55.0}` and `[Ana, Cara, Dev]`.
- [ ] A grade of exactly 40 counts as a pass.
- [ ] An empty list gives empty results.

**Package:** `t17_streams_api.exercises.ex06`

**Solution:** [t17 exercise 06 solution code](../../../../code/src/t17_streams_api/exercises/ex06/).
**Tests:** [t17 exercise 06 tests](../../../../code/test/t17_streams_api/exercises/ex06/).
