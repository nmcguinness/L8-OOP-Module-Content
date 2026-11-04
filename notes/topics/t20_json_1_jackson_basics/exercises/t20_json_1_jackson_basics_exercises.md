---
title: "JSON in Java I: Format & Jackson Basics — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t20_json_1_jackson_basics
description: "Five step-by-step exercises on JSON and Jackson: a POJO round trip, a list with TypeReference, a generic ServerResponse wrapper, @JsonProperty, and a JUnit round-trip test."
created: 2026-05-29
last_updated: 2026-11-03
version: 1.2
status: published
authors: ["OOP Teaching Team"]
tags: [java, json, jackson, serialisation, objectmapper, generics, typereference, exercises, year2, comp-c8z03]
---
# JSON in Java I: Format & Jackson Basics — Exercises

These exercises practise turning Java objects into JSON text and back again with Jackson's `ObjectMapper`, from the [t20 notes](../t20_json_1_jackson_basics_notes.md). Three terms are used throughout:

- **Serialise** means object to JSON text (`writeValueAsString`).
- **Deserialise** means JSON text to object (`readValue`).
- A **round trip** does both, and checks that you get back what you started with.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution and its tests. Jackson is already in the project's `pom.xml`, so there is nothing to install.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Player round trip | `writeValueAsString`, `readValue` | Foundation |
| 02 A list of players | `TypeReference` for `List<Player>` | Foundation |
| 03 A response wrapper | a generic class, `TypeReference` again | Intermediate |
| 04 Renaming a JSON key | `@JsonProperty`, `@JsonIgnoreProperties` | Intermediate |
| 05 A round-trip test | JUnit 5 and `equals` | Intermediate |

---

## Exercise 01 — Player round trip

**Goal:** Turn a `Player` object into JSON text, then turn that text back into a `Player`. This is the round trip every client and server in t22 performs on each message.

**You will need:** [What Jackson needs from your class](../t20_json_1_jackson_basics_notes.md#what-jackson-needs-from-your-class) and [Section 4: String-based serialisation](../t20_json_1_jackson_basics_notes.md#section-4--string-based-serialisation).

**Steps**

1. Create a class `Player` with two private fields: `name` (`String`) and `score` (`int`).
2. Give it a **no-argument constructor**, a constructor that takes both values, and a getter and a setter for each field.
3. In a class `Exercise`, create one shared `ObjectMapper` as a `static final` field.
4. Write `toJson(Player)` using `writeValueAsString`, and `fromJson(String)` using `readValue(json, Player.class)`.
5. In `run()`, serialise `new Player("Alice", 95)`, print the JSON, deserialise it, and print the name and score.

```java
static String toJson(Player player) throws JsonProcessingException
static Player fromJson(String json) throws JsonProcessingException
```

**Example**

```text
JSON: {"name":"Alice","score":95}
Name: Alice, score: 95
```

**Rules**

- The JSON keys come from the **getter** names: `getName()` gives `"name"`.
- Create the `ObjectMapper` once, not inside each method. Creating one is slow.

<details>
<summary>Hints</summary>

1. Without the no-argument constructor, `readValue` fails with an exception that mentions "no Creators, like default constructor, exist".
2. `MAPPER.writeValueAsString(player)` and `MAPPER.readValue(json, Player.class)`.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] The player you get back has the same name and score.

**Package:** `t20_json_1_jackson_basics.exercises.ex01`

**Solution:** [t20 exercise 01 solution code](../../../../code/src/t20_json_1_jackson_basics/exercises/ex01/).
**Tests:** [t20 exercise 01 tests](../../../../code/test/t20_json_1_jackson_basics/exercises/ex01/).

---

## Exercise 02 — A list of players

**Goal:** Send a whole list of players as one JSON array, and read it back as a `List<Player>`. You will meet **type erasure**: Java forgets the `Player` in `List<Player>` at runtime, so you must tell Jackson about it with a `TypeReference`.

**You will need:** [Deserialising a JSON array — and the type erasure problem](../t20_json_1_jackson_basics_notes.md#deserialising-a-json-array--and-the-type-erasure-problem).

**Steps**

1. Reuse the `Player` class from Exercise 01. Import it; do not copy it.
2. Write `toJson(List<Player>)`. It is the same call as before.
3. Write `fromJson(String)`, which returns `List<Player>`. Pass `new TypeReference<List<Player>>() { }` as the second argument to `readValue`.
4. In `run()`, make a list of three players, serialise it, deserialise it, and print whether the first player's name matches the original.

```java
static String toJson(List<Player> players) throws JsonProcessingException
static List<Player> fromJson(String json) throws JsonProcessingException
```

**Example**

```text
JSON: [{"name":"Alice","score":95},{"name":"Ben","score":80},{"name":"Clara","score":92}]
First name matches: true
```

**Rules**

- The players come back in the same order.
- Each element must be a real `Player`, not a map.
- `"[]"` gives an empty list.

<details>
<summary>Hints</summary>

1. Try `readValue(json, List.class)` first and print `back.get(0).getClass()`. You get a `LinkedHashMap`, because Jackson did not know the element type.
2. The `{ }` after `new TypeReference<List<Player>>()` is needed. It makes a tiny anonymous class that remembers the full type.

</details>

**Done when**

- [ ] `run()` prints `First name matches: true`.
- [ ] `back.get(0)` is a `Player`.
- [ ] `"[]"` gives an empty list.

**Package:** `t20_json_1_jackson_basics.exercises.ex02`

**Solution:** [t20 exercise 02 solution code](../../../../code/src/t20_json_1_jackson_basics/exercises/ex02/).
**Tests:** [t20 exercise 02 tests](../../../../code/test/t20_json_1_jackson_basics/exercises/ex02/).

---

## Exercise 03 — A response wrapper

**Goal:** Wrap any data in a `ServerResponse<T>` that also says whether the request succeeded. A server sends every reply in a wrapper like this, so the client always knows where to look.

**You will need:** [Section 5: Generic wrapper types and type erasure](../t20_json_1_jackson_basics_notes.md#section-5--generic-wrapper-types-and-type-erasure).

**Steps**

1. Create a generic class `ServerResponse<T>` with three private fields: `String status`, `String message` and `T data`.
2. Give it a no-argument constructor, getters and setters, and two static factory methods: `ok(message, data)` sets the status to `"OK"`, and `error(message)` sets it to `"ERROR"`.
3. Add `isOk()`, which returns `"OK".equals(status)`, and mark it `@JsonIgnore`.
4. Write the `toJson` helper below. It works for **any** `T`.
5. Write `fromJson(String)`, which returns `ServerResponse<Player>`, using `new TypeReference<ServerResponse<Player>>() { }`.
6. In `run()`, wrap `new Player("Alice", 95)`, serialise it, print the JSON, deserialise it, and print the status and `getData().getName()`.

```java
public static <T> ServerResponse<T> ok(String message, T data)
public static <T> ServerResponse<T> error(String message)
public static <T> String toJson(ServerResponse<T> response, ObjectMapper mapper)
        throws JsonProcessingException
static ServerResponse<Player> fromJson(String json) throws JsonProcessingException
```

**Example**

```text
JSON: {"status":"OK","message":"Player found","data":{"name":"Alice","score":95}}
Status: OK
Name: Alice
```

**Rules**

- `toJson` throws `IllegalArgumentException` if `response` or `mapper` is `null`.
- After deserialising, `getData()` must return a `Player`, not a map.
- The JSON has exactly three keys. There is no `"ok"` key.

<details>
<summary>Hints</summary>

1. Serialising needs no `TypeReference`: Jackson can see the real object. Only deserialising needs one, because the JSON text says nothing about Java types.
2. `MAPPER.readValue(json, new TypeReference<ServerResponse<Player>>() { })`
3. Jackson treats any public `isSomething()` method as a getter. `@JsonIgnore` tells it not to.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.
- [ ] `back.getData()` is a `Player`.
- [ ] `toJson(null, mapper)` throws `IllegalArgumentException`.

**Package:** `t20_json_1_jackson_basics.exercises.ex03`

**Solution:** [t20 exercise 03 solution code](../../../../code/src/t20_json_1_jackson_basics/exercises/ex03/).
**Tests:** [t20 exercise 03 tests](../../../../code/test/t20_json_1_jackson_basics/exercises/ex03/).

---

## Exercise 04 — Renaming a JSON key

**Goal:** Use a different name in the JSON than in Java. Java style is `maxPlayers`, but another system may insist on `max_players`. You will also make Jackson ignore keys it does not recognise.

**You will need:** [What Jackson needs from your class](../t20_json_1_jackson_basics_notes.md#what-jackson-needs-from-your-class), which covers `@JsonProperty` and `@JsonIgnoreProperties`.

**Steps**

1. Create a class `GameConfig` with one private field, `int maxPlayers`, and the usual constructors, getter and setter.
2. Put `@JsonProperty("max_players")` on **both** the getter and the setter.
3. Put `@JsonIgnoreProperties(ignoreUnknown = true)` on the class.
4. In `run()`, serialise `new GameConfig(4)` and check the JSON uses `max_players`.
5. Deserialise the JSON below and print `getMaxPlayers()`.

```json
{"max_players": 8, "roundDuration": 60}
```

**Example**

```text
Serialised: {"max_players":4}
Uses max_players: true
maxPlayers after reading: 8
```

**Rules**

- The JSON must not contain `maxPlayers` anywhere.
- `roundDuration` has no field in `GameConfig`. It must be ignored, not cause an exception.

<details>
<summary>Hints</summary>

1. Remove `@JsonIgnoreProperties` and run it again. You will see `UnrecognizedPropertyException`, which is why the annotation exists.
2. If you annotate only the getter, writing works but reading does not, so annotate both.

</details>

**Done when**

- [ ] The serialised JSON contains `"max_players":4`.
- [ ] Reading the JSON above gives `8`, with no exception.

**Package:** `t20_json_1_jackson_basics.exercises.ex04`

**Solution:** [t20 exercise 04 solution code](../../../../code/src/t20_json_1_jackson_basics/exercises/ex04/).
**Tests:** [t20 exercise 04 tests](../../../../code/test/t20_json_1_jackson_basics/exercises/ex04/).

---

## Exercise 05 — A round-trip test

**Goal:** Instead of printing results and checking them by eye, write two JUnit tests that check them automatically. A round-trip test is the standard way to prove a class works with Jackson.

**You will need:** the [JUnit asserts cheat sheet](../../../shared/cheat%20sheets/cheatsheet_junit_asserts_by_parameter_type.md) and the [JUnit in IntelliJ cheat sheet](../../../shared/cheat%20sheets/cheatsheet_junit_in_intellij.md).

**Steps**

1. Copy `Player` from Exercise 01 into this package, and add `equals` and `hashCode` that compare `name` and `score`.
2. Create a test class `PlayerJsonTest` under `code/test`, in the package below.
3. Write the first test, `serialise_player_producesExpectedJson`: serialise `new Player("Alice", 95)` and assert that the JSON contains `"name":"Alice"`.
4. Write the second test, `roundTrip_player_returnsEqualObject`: serialise, deserialise, and assert the result equals the original with `assertEquals`.
5. Run both tests and check that they pass.

**Example**

```text
PlayerJsonTest
  passed  serialise_player_producesExpectedJson
  passed  roundTrip_player_returnsEqualObject
```

**Rules**

- Test method names follow the pattern `method_scenario_expectedBehaviour`.
- Without `equals`, `assertEquals` compares object identity and the round-trip test fails. Try it once to see.

<details>
<summary>Hints</summary>

1. A test method may declare `throws Exception`, so you do not need `try`/`catch` inside it.
2. `assertTrue(json.contains("\"name\":\"Alice\""), json)`. The second argument is printed if the test fails, which shows you the actual JSON.

</details>

**Done when**

- [ ] Both tests pass.
- [ ] The round-trip test fails if you delete `equals`, and passes again when you put it back.

**Package:** `t20_json_1_jackson_basics.exercises.ex05`

**Solution:** this exercise's solution *is* the test class: [PlayerJsonTest.java](../../../../code/test/t20_json_1_jackson_basics/exercises/ex05/PlayerJsonTest.java), with the [Player class that it tests](../../../../code/src/t20_json_1_jackson_basics/exercises/ex05/Player.java).
