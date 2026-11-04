---
title: "JSON in Java II: Protocol Design & Advanced Jackson — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t21_json_2_jackson_advanced
description: "Six step-by-step exercises on protocol design and advanced Jackson: request envelopes, routing, defensive payload reading, Base64, JDBC BLOBs, JSON over a socket, multi-client serving, and round-trip tests."
created: 2026-05-29
last_updated: 2026-11-03
version: 2.1
status: published
authors: ["OOP Teaching Team"]
tags: [java, json, jackson, protocol, base64, blob, jdbc, testing, exercises, year2, comp-c8z03]
---
# JSON in Java II: Protocol Design & Advanced Jackson — Exercises

These exercises build the pieces of a small client-server protocol, in the order the [t21 notes](../t21_json_2_jackson_advanced_notes.md) introduce them. There is no networking here: [t22](../../t22_networking/t22_networking_notes.md) puts these pieces on a socket.

| Exercise | Piece of the protocol | Difficulty |
| :-- | :-- | :-- |
| 01 Request envelope | the shape of every request | Foundation |
| 02 Request dispatcher | sending each request to the right code | Foundation |
| 03 Defensive payload reading | not trusting what the client sent | Intermediate |
| 04 Base64 | carrying binary data inside JSON | Foundation |
| 05 BLOB storage | keeping binary data in MySQL | Intermediate, needs MySQL |
| 06 Round-trip tests | proving it all with JUnit | Extension |

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Some also have an alert box for a common mistake, and a **Think about it** question to discuss.

Each exercise has its own package under `code/src/t21_json_2_jackson_advanced/exercises/`. Give the main class a `run()` method. Jackson is already in `code/pom.xml`.

> **Tip:** create one `ObjectMapper` per class and reuse it. Creating a new one for each call is the most common performance mistake with Jackson.

---

## Exercise 01 — Request envelope

**Goal:** Define the **envelope** every request travels in: a `requestType` that says what the client wants, and a `payload` with the details. The server reads the `requestType` first, and only then knows what to expect in the `payload`.

**You will need:**
[The request envelope](../t21_json_2_jackson_advanced_notes.md#the-request-envelope) and [Reading values from a `JsonNode` payload](../t21_json_2_jackson_advanced_notes.md#reading-values-from-a-jsonnode-payload).

**Steps**

1. Create a class `ClientRequest` with two private fields: `String requestType` and `JsonNode payload`.
2. Give it a no-argument constructor, a two-argument constructor, and getters and setters.
3. In `run()`, deserialise the JSON below into a `ClientRequest`.
4. Print the type, then read the id with `getPayload().get("id").asInt()` and print it.

```json
{"requestType":"GET_PLAYER","payload":{"id":42}}
```

**Example**

```text
Type: GET_PLAYER
Id: 42
```

**Rules**

- `payload` stays a `JsonNode`. Do not replace it with a class such as `PlayerPayload`: you would then need a different envelope for every request type.

<details>
<summary>Hints</summary>

1. A `JsonNode` is a tree of JSON values that Jackson builds for you. `get("id")` returns the node for that field.
2. `MAPPER.readValue(json, ClientRequest.class)` fills both fields, including the tree.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.

**Package:** `t21_json_2_jackson_advanced.exercises.ex01`

**Solution:** [t21 exercise 01 solution code](../../../../code/src/t21_json_2_jackson_advanced/exercises/ex01/).

---

## Exercise 02 — Request dispatcher

**Goal:** Send each request to the right piece of code with a **map of handlers**, not a chain of `if`/`else` statements. Adding a request type then means adding one map entry.

**You will need:** [Routing requests — avoiding an `if-else` chain](../t21_json_2_jackson_advanced_notes.md#routing-requests--avoiding-an-if-else-chain).

**Steps**

1. Create a functional interface `RequestHandler` with one method: `String handle(JsonNode payload) throws Exception`.
2. Create a class `RequestDispatcher` with a field `Map<String, RequestHandler> handlers`.
3. In the constructor, register two handlers:
   - `"PING"` always returns `{"status":"OK"}`;
   - `"ECHO"` returns the payload turned back into a JSON string.
4. Write `dispatch(ClientRequest)`: look up the handler for the request's type and call it.
5. For an unknown type, return `{"status":"ERROR","message":"unknown type"}`.

**Example**

```text
PING  -> {"status":"OK"}
ECHO  -> {"msg":"hello","n":3}         (payload was {"msg":"hello","n":3})
DANCE -> {"status":"ERROR","message":"unknown type"}
```

**Rules**

- `dispatch` contains no `if` on the request type: the map does the choosing.
- An unknown or missing type gives the error reply; it never throws.

<details>
<summary>Hints</summary>

1. Each handler is a lambda: `handlers.put("PING", payload -> "{\"status\":\"OK\"}");`
2. For ECHO, `mapper.writeValueAsString(payload)` turns the tree back into text.

</details>

**Done when**

- [ ] All three requests give the replies shown.

**Think about it:** adding a third request type to the map costs one line and changes no existing code. Adding it to an `if`/`else` chain means editing a method that already works. Which design principle does that difference illustrate?

**Package:** `t21_json_2_jackson_advanced.exercises.ex02`

**Solution:** [t21 exercise 02 solution code](../../../../code/src/t21_json_2_jackson_advanced/exercises/ex02/).
**Tests:**
[t21 exercise 02 tests](../../../../code/test/t21_json_2_jackson_advanced/exercises/ex02/).

---

## Exercise 03 — Defensive payload reading

**Goal:** Read fields from a payload without trusting the client. Sooner or later a client sends a missing field, or a string where you expected a number. Your server must reject that clearly, not quietly store a wrong value.

**You will need:** [Reading values from a `JsonNode` payload](../t21_json_2_jackson_advanced_notes.md#reading-values-from-a-jsonnode-payload).

**Steps**

1. Create a class `Payloads` with the two `static` methods below.
2. `requireInt` returns the field as an `int`. It throws `IllegalArgumentException` if the payload is `null`, the field is missing, or the field is not a JSON whole number.
3. `optionalString` returns the field as text, or `fallback` if the payload is `null`, the field is missing, or the field is not a JSON string.

```java
public static int requireInt(JsonNode payload, String field)
public static String optionalString(JsonNode payload, String field, String fallback)
```

**Example**

```text
{"id":42}     requireInt(..., "id")  gives 42
{"id":"42"}   requireInt(..., "id")  throws: field 'id' must be a whole number
{}            requireInt(..., "id")  throws: missing field 'id'

{"name":"Ana"}  optionalString(..., "name", "(none)")   gives Ana
{"name":"Ana"}  optionalString(..., "title", "(none)")  gives (none)
```

**Rules**

- Test four cases for each method: field present and correct, present but the wrong type, missing, and a `null` payload.
- `{"id":"42"}` is a **string**, so `requireInt` must reject it, not convert it.

> [!WARNING]
> Three similar-looking calls behave differently on a missing field. `payload.get("id")` returns `null`. `payload.get("id").asInt()` throws `NullPointerException`. `payload.path("id").asInt()` silently returns `0`. Picking the wrong one is how a server ends up storing zeros it was never sent.

<details>
<summary>Hints</summary>

1. `node.isInt()` is `true` only for a JSON whole number, and `node.isTextual()` only for a JSON string.
2. Check in this order: payload `null`, then `value == null`, then the type, and only then call `asInt()` or `asText()`.

</details>

**Done when**

- [ ] All five example lines behave as shown.
- [ ] A `null` payload makes `requireInt` throw and `optionalString` return the fallback.

**Package:** `t21_json_2_jackson_advanced.exercises.ex03`

**Solution:** [t21 exercise 03 solution code](../../../../code/src/t21_json_2_jackson_advanced/exercises/ex03/).
**Tests:**
[t21 exercise 03 tests](../../../../code/test/t21_json_2_jackson_advanced/exercises/ex03/).

---

## Exercise 04 — Base64

**Goal:** JSON can only hold text, so binary data such as an image must be turned into text first. Base64 does that. Encode some bytes, put them in a JSON field, take them out again, and check that nothing changed.

**You will need:** [Section 2 — Base64 encoding binary data within JSON](../t21_json_2_jackson_advanced_notes.md#section-2--base64-encoding-binary-data-within-json).

**Steps**

1. Write the two methods below, using `Base64.getEncoder()` and `Base64.getDecoder()`.
2. In `run()`, turn `"Hello, World!"` into bytes with `getBytes(StandardCharsets.UTF_8)`.
3. Build a JSON object with a `fileName` field and a `data` field holding the encoded text.
4. Read the `data` field back out, decode it, and compare it with the original bytes.

```java
public static String encodeToBase64(byte[] data)
public static byte[] decodeFromBase64(String encoded)
```

**Example**

```text
JSON: {"fileName":"hello.txt","data":"SGVsbG8sIFdvcmxkIQ=="}
Bytes match: true
Original 13 bytes, Base64 20 characters
```

**Rules**

- Both methods throw `IllegalArgumentException` for `null`.
- `decodeFromBase64` throws `IllegalArgumentException` for text that is not valid Base64.

> [!WARNING]
> Compare byte arrays with `Arrays.equals(a, b)`. Never use `a.equals(b)` or `a == b`: those compare references, so they report failure even for a perfect copy.

<details>
<summary>Hints</summary>

1. `Base64.getEncoder().encodeToString(data)` and `Base64.getDecoder().decode(encoded)`.
2. `mapper.createObjectNode()` gives you an empty JSON object to `put` fields into.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.

**Think about it:** Base64 makes data about a third bigger (13 bytes became 20 characters). Why is it still the standard way to carry binary data inside JSON?

**Package:** `t21_json_2_jackson_advanced.exercises.ex04`

**Solution:** [t21 exercise 04 solution code](../../../../code/src/t21_json_2_jackson_advanced/exercises/ex04/).

---

## Exercise 05 — BLOB storage (needs MySQL)

**Goal:** Store binary data in a database column, read it back, and list what is stored **without** downloading every file.

**You will need:** [Section 3 — Storing and retrieving binary data with JDBC (BLOB)](../t21_json_2_jackson_advanced_notes.md#section-3--storing-and-retrieving-binary-data-with-jdbc-blob) and the DAO pattern from [t15](../../t15_dao/t15_dao_notes.md).

**Steps**

1. Create the table with [the schema for this exercise](../../../../code/src/t21_json_2_jackson_advanced/exercises/ex05/schema.sql). Its `asset_data` column is a `MEDIUMBLOB`.
2. Create a record `AssetMetadata(int id, String name)`.
3. Create a class `AssetDao` with the three methods below.
4. `insertAsset` stores the bytes with `PreparedStatement.setBytes()`.
5. `getAsset` reads them with `ResultSet.getBytes()`.
6. `getAllMetadata` selects **only** `id` and `name`, never the BLOB column.

```java
public void insertAsset(int id, String name, byte[] data) throws SQLException
public Optional<byte[]> getAsset(int id) throws SQLException
public List<AssetMetadata> getAllMetadata() throws SQLException
```

**Example**

```text
insertAsset(1, "hero.png", sprite)
getAsset(1)      gives the same bytes   (check with Arrays.equals)
getAsset(999)    gives Optional.empty
getAllMetadata() gives [AssetMetadata[id=1, name=hero.png]]
```

**Rules**

- A missing row gives `Optional.empty()`.
- `insertAsset` throws `IllegalArgumentException` for `null` data.

> [!WARNING]
> `getBytes()` returns `null` on a missing row, not an empty array. Returning an `Optional` makes that impossible to forget.

<details>
<summary>Hints</summary>

1. The SQL for `getAsset` is `SELECT asset_data FROM game_assets WHERE id = ?`.
2. Check `rs.next()` first: if it is `false`, there is no such row.

</details>

**Done when**

- [ ] The bytes you read back equal the bytes you stored.
- [ ] A missing id gives `Optional.empty`.
- [ ] `getAllMetadata` never mentions `asset_data` in its SQL.

**Think about it:** listing 200 assets with `SELECT *` pulls every file across the network just to show a list of names. Time both versions if you can. The difference is large.

**Package:** `t21_json_2_jackson_advanced.exercises.ex05`

**Solution:** [t21 exercise 05 solution code](../../../../code/src/t21_json_2_jackson_advanced/exercises/ex05/). It needs a database, so there are no automated tests.

---

## Exercise 06 — Round-trip tests (extension)

**Goal:** Up to now you have checked results by reading printed output. Replace that with JUnit tests that run in a second, every time. A round-trip test is the cheapest way to catch a change that breaks the agreement between client and server.

**You will need:** [Section 4 — Testing JSON round-trips](../t21_json_2_jackson_advanced_notes.md#section-4--testing-json-round-trips).

**Steps**

1. Create a test class `ProtocolRoundTripTest` under `code/test`, in the package below.
2. Write these four tests, using your classes from Exercises 01, 03 and 04, and a `ServerResponse<T>` like the one from t20 (add a copy to your Exercise 01 package):

   ```java
   clientRequest_roundTrip_preservesTypeAndPayload()
   base64_roundTrip_returnsOriginalBytes()
   requireInt_missingField_throwsIllegalArgumentException()
   serverResponse_error_hasStatusErrorAndNullData()
   ```

3. Run them with `mvn test` or the green arrow in IntelliJ.

**Example**

```text
ProtocolRoundTripTest
  passed  clientRequest_roundTrip_preservesTypeAndPayload
  passed  base64_roundTrip_returnsOriginalBytes
  passed  requireInt_missingField_throwsIllegalArgumentException
  passed  serverResponse_error_hasStatusErrorAndNullData
```

**Rules**

- Each test does one thing: arrange the data, act once, then assert.

> [!WARNING]
> `assertEquals(original, restored)` calls `equals`. `ClientRequest` does not override it, so that line compares references and fails on correct code. Either override `equals` and `hashCode`, or compare field by field. Know which you are doing and why.

<details>
<summary>Hints</summary>

1. `JsonNode` *does* override `equals`, so you can compare the two payloads directly.
2. Use `assertArrayEquals` for byte arrays, and `assertThrows` for the missing field.

</details>

**Done when**

- [ ] All four tests pass.

**Package:** `t21_json_2_jackson_advanced.exercises.ex06`

**Solution:** this exercise's solution *is* the test class: [ProtocolRoundTripTest.java](../../../../code/test/t21_json_2_jackson_advanced/exercises/ex06/ProtocolRoundTripTest.java).
