---
title: "JSON in Java I: Format & Jackson Basics — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t20_json_1_jackson_basics
description: "Five exercises on JSON and Jackson: POJO round-trips, lists, a generic response wrapper, field mapping, and a JUnit 5 round-trip test."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, json, jackson, serialisation, objectmapper, generics, typereference, exercises, year2, comp-c8z03]
---
# JSON in Java I: Format & Jackson Basics — Exercises

## Exercise 01 — Serialise and deserialise a POJO

Define a `Player` class with fields `name` (String) and `score` (int).

Add a **no-arg constructor** and getters.

Write a `main` method that:
1. Creates a `Player("Alice", 95)`.
2. Serialises it to a JSON `String` using `ObjectMapper.writeValueAsString()`.
3. Prints the JSON string.
4. Deserialises the string back to a `Player` using `readValue(json, Player.class)`.
5. Prints the player's name and score.

**Package:** `t20_json_1_jackson_basics.exercises.ex01`

---

## Exercise 02 — Serialise a List

Extend Exercise 01: create a `List<Player>` with three players, serialise the list to JSON,
then deserialise it back using `TypeReference<List<Player>>`.

Assert (with `System.out.println`) that the first player's name after deserialisation matches
the original.

**Package:** `t20_json_1_jackson_basics.exercises.ex02`

---

## Exercise 03 — Generic Response wrapper

Define a generic class `Response<T>` with fields `boolean success` and `T data`.

Write a helper:

```java
public static <T> String toJson(Response<T> response, ObjectMapper mapper)
        throws JsonProcessingException
```

Wrap a `Player` in a `Response<Player>`, serialise it, print the JSON, then deserialise it back
using `TypeReference<Response<Player>>` and print `response.getData().getName()`.

**Package:** `t20_json_1_jackson_basics.exercises.ex03`

---

## Exercise 04 — `@JsonProperty` and field mapping

Create a `GameConfig` class with a field named `maxPlayers` in Java, but serialised as
`"max_players"` in JSON using `@JsonProperty("max_players")`.

Serialise a `GameConfig` and verify the JSON uses `max_players`. Deserialise the following JSON
string and verify the field is populated correctly:

```json
{"max_players": 8, "roundDuration": 60}
```

Use `@JsonIgnoreProperties(ignoreUnknown = true)` to silently ignore `roundDuration`.

**Package:** `t20_json_1_jackson_basics.exercises.ex04`

---

## Exercise 05 — Round-trip JUnit 5 test

Write a JUnit 5 test class `PlayerJsonTest` with two tests:

1. `serialise_player_producesExpectedJson` — assert the JSON string contains `"name":"Alice"`.
2. `roundTrip_player_returnsEqualObject` — serialise then deserialise and assert the resulting
   player equals the original (override `equals` on `Player`).

**Package:** `t20_json_1_jackson_basics.exercises.ex05`
