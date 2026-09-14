---
title: "JSON in Java II: Protocol Design & Advanced Jackson — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t21_json_2_jackson_advanced
description: "Eight exercises on protocol design and advanced Jackson: request envelopes, routing, defensive payload reading, Base64, JDBC BLOBs, JSON over a socket, multi-client serving, and round-trip tests."
created: 2026-05-29
last_updated: 2026-09-11
version: 2.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, json, jackson, protocol, base64, blob, jdbc, sockets, testing, exercises, year2, comp-c8z03]
---
# JSON in Java II: Protocol Design & Advanced Jackson — Exercises

These exercises build the pieces of a small client-server protocol, in the order the notes
introduce them. Exercises 01–02 define the envelope, 03 makes it defensive, 04–05 move binary
data through it, 06–07 put it on a socket, and 08 tests the whole thing.

Exercises 05 and 08 need a running MySQL instance; the rest do not.

## How to run

Each exercise has its own package under `code/src/t21_json_2_jackson_advanced/exercises/`.
Add a `run()` method and call it from `Main`, or give the class a `main` method and use the
green arrow in IntelliJ.

Jackson is already on the classpath via `code/pom.xml` — you do not need to add a dependency.

> :bulb: Reuse one `ObjectMapper` per class. It is thread-safe once configured, and creating a
> new one per call is the single most common performance mistake with Jackson.

---

## Exercise 01 — Request envelope

Define a `ClientRequest` class with fields `type` (String) and `payload` (`JsonNode`).

Write a `main` method that:

1. Builds a JSON string: `{"type":"GET_PLAYER","payload":{"id":42}}`.
2. Deserialises it to `ClientRequest`.
3. Reads `payload.get("id").asInt()` and prints the result.

**Package:** `t21_json_2_jackson_advanced.exercises.ex01`

**Deliverable:** `ClientRequest.java` plus a runnable demonstration.

**Pitfall:** `JsonNode` is deliberately untyped. That is the point of an envelope — the server
reads `type` first, and only then knows how to interpret `payload`. Do not be tempted to
replace it with a concrete class; you would need a different envelope per request type.

---

## Exercise 02 — Request router

Define a `RequestHandler` functional interface with method `String handle(JsonNode payload)`.

Build a `Map<String, RequestHandler>` with two entries:

- `"PING"` returns `{"status":"ok"}` regardless of payload.
- `"ECHO"` returns the payload serialised back to a JSON string.

Write a dispatcher method and test both handler types.

**Package:** `t21_json_2_jackson_advanced.exercises.ex02`

**Deliverable:** the interface, the map, and a `dispatch(ClientRequest)` method.

**Discussion:** the notes reject an `if-else` chain over request types. Adding a third request
type to your map costs one line and touches no existing code. Adding it to an `if-else` chain
edits a method that already works. Name the design principle that difference illustrates.

---

## Exercise 03 — Defensive payload reading (no database)

A real client will eventually send you a payload that is missing a field, or has a string
where you expected a number. Write a small helper class with these methods:

```java
public static int requireInt(JsonNode payload, String field)
public static String optionalString(JsonNode payload, String field, String fallback)
```

`requireInt` throws `IllegalArgumentException` naming the missing field. `optionalString`
returns the fallback rather than throwing.

Test all four cases for each: field present and correct, field present but the wrong type,
field missing, and `payload` itself null.

**Package:** `t21_json_2_jackson_advanced.exercises.ex03`

**Pitfall:** `payload.get("missing")` returns **`null`**, but `payload.get("missing").asInt()`
throws `NullPointerException`, and `payload.path("missing").asInt()` silently returns `0`.
Those three behaviours are all different, and picking the wrong one is how a protocol ends up
storing zeros it was never sent.

**Check your work:** a request with `{"id":"42"}` — a string, not a number — should be
rejected by `requireInt`, not quietly coerced.

---

## Exercise 04 — Base64 encode/decode

Write two methods:

```java
public static String encodeToBase64(byte[] data)
public static byte[] decodeFromBase64(String encoded)
```

Test with a short `byte[]` (e.g. the UTF-8 bytes of `"Hello, World!"`): encode, embed in a JSON
string as a field, extract the field value, decode, and verify the result matches the original
bytes.

**Package:** `t21_json_2_jackson_advanced.exercises.ex04`

**Deliverable:** both methods plus a round-trip demonstration.

**Pitfall:** compare byte arrays with `Arrays.equals(a, b)`, never `a.equals(b)` or `a == b` —
those compare references and will report failure on a perfect copy.

**Discussion:** Base64 grows the payload by roughly a third. Given that cost, why is it still
the standard way to carry binary data inside JSON?

---

## Exercise 05 — BLOB insert and retrieval (needs MySQL)

Given a `game_assets` table with a `MEDIUMBLOB` column `asset_data`:

1. Write a DAO method `void insertAsset(int id, String name, byte[] data)` using
   `PreparedStatement.setBytes()`.
2. Write a DAO method `byte[] getAsset(int id)` using `ResultSet.getBytes()`.
3. Write a metadata-only query method `List<AssetMetadata> getAllMetadata()` that selects `id`
   and `name` only — no BLOB column.

**Package:** `t21_json_2_jackson_advanced.exercises.ex05`

**Deliverable:** the DAO, plus the `CREATE TABLE` statement you used.

**Why the third method exists:** listing 200 assets with `SELECT *` pulls every blob across the
wire to render a list of names. Measure it if you can — the difference is not subtle.

**Pitfall:** `getBytes()` on a missing row returns `null`, not an empty array. Decide what your
method does in that case and document it.

---

## Exercise 06 — JSON over socket (round-trip)

Implement a single-threaded echo server that:

1. Accepts one connection.
2. Reads a JSON string from the client.
3. Deserialises it as a `ClientRequest`.
4. Responds with a `ServerResponse` serialised to JSON, with `success: true` and the original
   request type echoed back in the data field.

Write a matching client that sends a request and prints the response.

**Package:** `t21_json_2_jackson_advanced.exercises.ex06`

**Deliverable:** server and client classes, both runnable.

**Pitfall:** the protocol is line-delimited, so every message must end with a newline and be
flushed. `PrintWriter` with `autoFlush` set to `true` flushes on `println` but **not** on
`print` — a message sent with `print` will hang the reader forever.

---

## Exercise 07 — Serving several clients (extension)

Extend Exercise 06 so the server keeps accepting connections and handles each on a pool thread
with an `ExecutorService`, as the notes describe.

Then demonstrate it: start the server, connect two clients, and show both receiving correct
responses while both connections are open.

**Package:** `t21_json_2_jackson_advanced.exercises.ex07`

**Deliverable:** the server, plus a short note on what you observed with two clients connected.

**Discussion:** your single-threaded server from Exercise 06 does not reject a second client —
it accepts it late. Explain what the second client experiences while the first is still
connected, and why that is worse than an outright refusal.

---

## Exercise 08 — Round-trip tests (extension)

Everything above has been checked by reading `println` output. Replace that with JUnit 5 tests
in `code/test/`, mirroring the package.

Cover at least:

```java
clientRequest_roundTrip_preservesTypeAndPayload()
base64_roundTrip_returnsOriginalBytes()
requireInt_missingField_throwsIllegalArgumentException()
serverResponse_error_hasSuccessFalseAndNullData()
```

**Package:** `t21_json_2_jackson_advanced.exercises.ex08`

**Deliverable:** a test class that passes with `mvn test`.

**Why this is the last exercise:** a round-trip test is the cheapest way to catch a protocol
change that breaks an existing client. Serialise, deserialise, assert equality — if that fails,
the two ends of your protocol no longer agree.

**Pitfall:** `assertEquals(original, restored)` calls `equals`. If your DTO does not override
it, the test compares references and fails on correct code. Override `equals` and `hashCode`,
or assert field by field — but know which you are doing and why.
