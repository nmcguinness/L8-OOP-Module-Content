---
title: "JSON in Java I: Format & Jackson Basics"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t20_json_1_jackson_basics
description: "Ground-up introduction to JSON: the format itself, string-based serialisation and deserialisation with Jackson, generic wrapper types, and TypeReference for type erasure."
created: 2026-02-26
last_updated: 2026-05-27
version: 2.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, json, jackson, serialisation, objectmapper, generics, typereference, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO4]
previous_topic: t19_concurrency
prerequisites:
  - Generics I & II (bounded types, type erasure)
  - Exception handling (try-catch, checked exceptions)
  - Collections I: ArrayList
---
# JSON in Java I: Format & Jackson Basics

> **Prerequisites:**
> - You can create Java classes with fields, constructors, and methods
> - You understand generics: `Box<T>`, bounded type parameters, `List<T>`
> - You can use `ArrayList` and basic loops
> - You are comfortable with `try-catch` and checked exceptions

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Section 1 — What is JSON?](#section-1--what-is-json)
- [Section 2 — JSON as a file format vs. a wire
  protocol](#section-2--json-as-a-file-format-vs-a-wire-protocol)
- [Section 3 — Setting up Jackson](#section-3--setting-up-jackson)
- [Section 4 — String-based serialisation](#section-4--string-based-serialisation)
- [Section 5 — Generic wrapper types and type
  erasure](#section-5--generic-wrapper-types-and-type-erasure)
- [Reflective questions](#reflective-questions)

---

## What you'll learn

| Skill Type | You will be able to... |
| :- | :- |
| Understand | Describe the JSON format: its data types, structure rules, and how JSON values map to Java types. |
| Understand | Explain the difference between using JSON as a **file format** and using it as a **wire protocol**. |
| Understand | Explain why Java's type erasure means `readValue(json, List<Player>.class)` cannot compile, and how `TypeReference<T>` solves it. |
| Apply | Add Jackson to a Maven project and create a shared `ObjectMapper` instance correctly. |
| Apply | Use `ObjectMapper` to serialise a Java object to a `String` and deserialise a `String` back to an object. |
| Apply | Serialise and deserialise a **generic wrapper type** (`Response<T>`) using `TypeReference`. |
| Apply | Design a simple **JSON request/response protocol** with a `type` field and `payload` envelope. |
| Apply | Base64-encode a `byte[]` for safe inclusion in a JSON string, and decode it back to the original bytes. |
| Apply | Extend a MySQL table with a `MEDIUMBLOB` column and associated metadata columns; update `mysqlSetup.sql` accordingly. |
| Apply | Insert binary data into a `MEDIUMBLOB` column using `PreparedStatement.setBytes()`. |
| Apply | Retrieve binary data from a `MEDIUMBLOB` column using `ResultSet.getBytes()`. |
| Apply | Write a metadata-only SELECT query that deliberately omits the BLOB column. |
| Apply | Send a JSON string over a socket and read it back using `PrintWriter` and `BufferedReader`. |
| Apply | Write JUnit 5 round-trip tests that assert a serialise?deserialise cycle produces an equal object. |
| Debug | Fix common Jackson errors: missing no-arg constructor, field name mismatch, raw type warnings. |
| Debug | Use `@JsonProperty` to correct a JSON key that does not match the getter name convention. |
| Debug | Annotate DTOs with `@JsonIgnoreProperties(ignoreUnknown = true)` to handle evolving protocols defensively. |
| Debug | Safely read fields from a `JsonNode` payload using `payload.has(...)` before calling `.asInt()` / `.asText()`. |

---

## Why this matters

Programs communicate. A method call is communication within a single JVM. A file is
communication across time — you write now, someone reads later. A socket is communication
across space — two programs on different machines exchanging data right now.

Each of these needs a **shared language**. Within a JVM that language is Java. Across a network
it cannot be Java, because the other side may be running a different language, a different JVM
version, or a completely different runtime. The two sides need a format they can both
understand without knowing anything about each other's internal representation.

JSON has become the dominant format for exactly this reason. It is human-readable,
language-neutral, well-specified, and supported by every major programming language. Learning
to read, write, and transmit JSON is a foundational skill for any networked application.

---

## How this builds on previous content

| Earlier topic | Concept carried forward |
| :- | :- |
| [Generics I](../t11_generics_1/t11_generics_1_notes.md) | `Box<T>` to `Response<T>`: wrapping arbitrary payloads in a typed container |
| [Generics II](../t12_generics_2/t12_generics_2_notes.md) | Type erasure to why `TypeReference<List<Player>>` is needed at runtime |
| [Design Patterns I](../t13_design_patterns_1/t13_design_patterns_1_notes.md) | Strategy pattern to request routing: one handler per request type, no `instanceof` chain |
| [DB Connectivity](../t15_dao/t15_dao_notes.md) | DAO returns Java objects to serialise them to JSON before sending across a socket |

---

## Section 1 — What is JSON?

### The format

**JSON (JavaScript Object Notation)** is a text-based data format. It was originally derived
from JavaScript object syntax but has nothing to do with JavaScript as a language — it is
completely language-neutral. A JSON document is just a string of characters that follows a
strict grammar.

JSON has exactly **six value types**:

| JSON type | Example | Java equivalent |
| :- | :- | :- |
| String | `"Alice"` | `String` |
| Number | `42`, `9.2`, `-7` | `int`, `double`, `long` |
| Boolean | `true`, `false` | `boolean` |
| Null | `null` | `null` |
| Array | `[1, 2, 3]` | `List<T>` or array |
| Object | `{"key": "value"}` | class instance or `Map<String, Object>` |

That is the complete list. JSON has no concept of dates, binary data, undefined, functions, or
comments. If you need to represent something that is not in this list (a `LocalDate`, a
`byte[]`), you must encode it as one of the six types — typically a string.

### JSON values in practice

A JSON **string** is surrounded by double quotes. Single quotes are not valid JSON.

```json
"Hello, world"
```

A JSON **number** has no quotes. It can be an integer or have a decimal point. It cannot be
`NaN` or `Infinity`.

```json
42
9.2
-100
```

A JSON **boolean** is lowercase. `True` and `False` are not valid JSON.

```json
true
false
```

A JSON **array** is a comma-separated list of values inside square brackets. The values can be
of different types.

```json
[1, "two", true, null, [3, 4]]
```

A JSON **object** is a comma-separated list of key-value pairs inside curly braces. Keys must
be strings (double-quoted). Values can be any JSON type.

```json
{
  "id": 1,
  "name": "Alice",
  "rating": 9.2,
  "active": true,
  "tags": ["defender", "captain"],
  "address": null
}
```

### A realistic JSON document

Here is what a typical server response payload might look like. Notice that it is just nested
combinations of the six types above.

```json
{
  "status": "SUCCESS",
  "message": "Player retrieved",
  "data": {
    "id": 7,
    "name": "Alice",
    "rating": 9.2,
    "active": true,
    "recentScores": [8, 9, 10, 9, 8]
  }
}
```

### What makes JSON valid

Four rules cover most errors:
1. **Strings must use double quotes** — `"Alice"`, not `'Alice'`
2. **Object keys must be strings** — `{"id": 1}`, not `{id: 1}`
3. **No trailing commas** — `{"a":1, "b":2}` is valid; `{"a":1, "b":2,}` is not
4. **No comments** — JSON has no comment syntax

Violating any of these produces a parse error. When Jackson throws a `JsonParseException`, one
of these four is usually the cause.

### JSON vs. Java: mapping the concepts

| Java concept | JSON representation |
| :- | :- |
| `int`, `long` | Number without decimal: `42` |
| `double`, `float` | Number with decimal: `9.2` |
| `String` | String: `"Alice"` |
| `boolean` | `true` or `false` |
| `null` | `null` |
| Object (class instance) | Object `{}` — each field becomes a key-value pair |
| `List<T>`, array | Array `[]` |
| `Map<String, V>` | Object `{}` — each map entry becomes a key-value pair |
| `LocalDate`, `byte[]`, enum | Must be encoded as a string or number |

---

## Section 2 — JSON as a file format vs. a wire protocol

### JSON as a file format

JSON is often used to store configuration, seed data, or exported records in `.json` files on
disk. In the **ce13** challenge exercise, a `JSONSerialiser<T>` helper reads a JSON file
containing incident reports and converts them into a `List<Incident>` for processing. The
Jackson overloads for this use a `File` or `Path` object:

```java
// File → List<T>
List<T> data = mapper.readValue(path.toFile(), listType);

// List<T> → File
mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), items);
```

This is a useful pattern when JSON is **persistent storage** — it is written once and read
later, possibly by a different program or a different run of the same program.

### JSON as a wire protocol

A networked client-server system has a different need. The client and server exchange messages
in real time over a socket. Neither side wants to write to disk on every request. Instead, they
convert objects to **strings in memory** and send those strings directly over the connection.

The Jackson overloads for this are different:

```java
// Object → String (in memory)
String json = mapper.writeValueAsString(entity);

// String → Object (in memory)
Player p = mapper.readValue(json, Player.class);
```

The distinction matters:

| Aspect | File-based | String-based |
| :- | :- | :- |
| Input / output | `File` or `Path` | `String` |
| Use case | Persistence, config, export | Network communication, inter-process messaging |
| Jackson method | `writeValue(File, ...)` / `readValue(File, ...)` | `writeValueAsString(...)` / `readValue(String, ...)` |

In a client-server application you will almost always use the string-based overloads. The
file-based overloads from ce13 are not used in a networked context.

---

## Section 3 — Setting up Jackson

### Maven dependency

Jackson Databind is the core library. It handles conversion between Java objects and JSON
strings, and it transitively pulls in the two other Jackson modules it depends on (`core` and
`annotations`).

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.2</version>
</dependency>
```

Add this inside the `<dependencies>` block of your `pom.xml`. After saving, reload the Maven
project in IntelliJ (the elephant icon, or right-click then Maven then Reload Project).

### The `ObjectMapper`

`ObjectMapper` is Jackson's central class. It performs all serialisation and deserialisation.
Two important properties:

1. **It is thread-safe** after construction. Multiple threads can call `writeValueAsString` and
   `readValue` on the same instance concurrently without problems.
2. **It is expensive to construct** — it loads configuration, registers modules, and scans
   annotations during construction.

The consequence of both properties is the same: **create one instance and reuse it everywhere**.

```java
import com.fasterxml.jackson.databind.ObjectMapper;

// Creates: a single shared mapper instance — construct once, use everywhere
private static final ObjectMapper MAPPER = new ObjectMapper();
```

Avoid creating a `new ObjectMapper()` inside a loop, inside a method that is called frequently,
or in a per-request handler. A shared `static final` field is the standard pattern.

### What Jackson needs from your class

For Jackson to reconstruct an object from a JSON string (`readValue`), three things must be true:

1. **A public no-argument constructor** — Jackson calls this to create the empty object, then
   populates it.
2. **Public getters** — Jackson uses these to read field values when serialising (object to string).
3. **Public setters** — Jackson uses these to write field values when deserialising (string to
   object).

If the no-arg constructor is missing, Jackson throws `InvalidDefinitionException: No suitable
constructor found` at runtime. If a getter or setter is missing, that field is silently ignored
or left at its default value.

Here is a correctly structured class that Jackson can serialise and deserialise:

```java
public class Player {

    // === Fields ===
    private int fId;
    private String fName;
    private double fRating;

    // === Constructors ===
    // Creates: required no-arg constructor for Jackson deserialisation
    public Player() {
        fId = 0;
        fName = "";
        fRating = 0.0;
    }

    // Creates: convenience constructor for application code
    public Player(int id, String name, double rating) {
        fId = id;
        fName = name;
        fRating = rating;
    }

    // === Public API ===
    // Gets: the player id
    public int getId() { return fId; }

    // Sets: the player id
    public void setId(int id) { fId = id; }

    // Gets: the player name
    public String getName() { return fName; }

    // Sets: the player name
    public void setName(String name) { fName = name; }

    // Gets: the player rating
    public double getRating() { return fRating; }

    // Sets: the player rating
    public void setRating(double rating) { fRating = rating; }

    // === Overrides ===
    @Override
    public int hashCode() {
        return Integer.hashCode(fId);
    }

    @Override
    public String toString() {
        return "Player{id=" + fId + ", name='" + fName + "', rating=" + fRating + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player other = (Player) o;
        return fId == other.fId
            && Double.compare(fRating, other.fRating) == 0
            && java.util.Objects.equals(fName, other.fName);
    }
}
```

**How Jackson maps field names:** Jackson derives the JSON key from the getter name, not the
field name. The rule is: strip `get` from the getter name and lowercase the first letter. So
`getId()` then `"id"`, `getName()` then `"name"`, `getRating()` then `"rating"`. The `f` prefix
on the private field is invisible to Jackson. The resulting JSON is:

```json
{"id":1,"name":"Alice","rating":9.2}
```

**What happens if you name getters poorly:** If you follow the `f` prefix convention but
accidentally name your getter `getFId()` instead of `getId()`, Jackson will derive the key
`"fId"` — and the JSON key in outgoing messages will be `"fId"` rather than `"id"`. When the
string is deserialised, Jackson looks for a setter named `setFId()` and calls it. If only
`setId()` exists, the field is silently left at its default value. No exception is thrown.

The fix in either direction is `@JsonProperty`, which explicitly names the JSON key regardless
of the getter name:

```java
import com.fasterxml.jackson.annotation.JsonProperty;

// Tells Jackson: this getter maps to JSON key "id", not "fId"
@JsonProperty("id")
public int getFId() { return fId; }

// Tells Jackson: this setter maps to JSON key "id"
@JsonProperty("id")
public void setFId(int id) { fId = id; }
```

With `@JsonProperty` in place, the JSON key is always `"id"` regardless of what the getter is
called. The annotation must be placed on both the getter and the corresponding setter, or the
round-trip will be asymmetric.

The cleanest approach, and the one used throughout these notes, is to name getters so the
derived key is correct — `getId()` not `getFId()` — making `@JsonProperty` unnecessary for
straightforward cases.

**Handling unknown fields — `@JsonIgnoreProperties`:** By default, Jackson throws
`UnrecognizedPropertyException` if the JSON being deserialised contains a field that the target
class has no corresponding setter for. This becomes a problem as a protocol evolves: if the
server adds a new field to its response and an older client tries to deserialise it, the client
will crash rather than simply ignoring the new field.

The defensive solution is `@JsonIgnoreProperties(ignoreUnknown = true)` on the class:

```java
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Tells Jackson: silently skip any JSON fields this class does not have a setter for
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
    // ... fields, constructors, getters, setters as before
}
```

This is particularly important for `Response<T>` and `Request`, since both sides of the
protocol may evolve at different times. Annotating them with
`@JsonIgnoreProperties(ignoreUnknown = true)` makes deserialisation tolerant of
forward-compatibility additions.

---

## Section 4 — String-based serialisation

### Serialising a single object

```java
Player p = new Player(1, "Alice", 9.2);

// Converts: Player to a compact JSON string
String json = MAPPER.writeValueAsString(p);

System.out.println(json);
// {"id":1,"name":"Alice","rating":9.2}
```

`writeValueAsString` may throw a checked `JsonProcessingException`. In practice this only
happens if the object contains a type that Jackson cannot handle (a circular reference, for
example). For simple DTO classes it does not throw, but you must either handle or declare it.

### Deserialising a string to a single object

```java
String json = "{\"id\":1,\"name\":\"Alice\",\"rating\":9.2}";

// Converts: JSON string back to a Player instance
Player p = MAPPER.readValue(json, Player.class);

System.out.println(p.getName()); // Alice
```

`readValue` throws `JsonProcessingException` if the string is not valid JSON, or if the JSON
structure does not match the target class (e.g. a string value where a number is expected).

### Serialising a list

```java
List<Player> players = List.of(
    new Player(1, "Alice", 9.2),
    new Player(2, "Bob", 7.5)
);

// Converts: List<Player> to a JSON array string
String json = MAPPER.writeValueAsString(players);

System.out.println(json);
// [{"id":1,"name":"Alice","rating":9.2},{"id":2,"name":"Bob","rating":7.5}]
```

### Deserialising a JSON array — and the type erasure problem

Deserialising a list requires careful handling. You cannot write:

```java
// DOES NOT COMPILE — generic type is erased; List<Player>.class is not valid syntax
List<Player> players = MAPPER.readValue(json, List<Player>.class);
```

The problem is **type erasure**: Java removes generic type parameters at compile time. At
runtime, `List<Player>` and `List<String>` are both just `List`. If you pass `List.class`,
Jackson only knows it should return a `List` — it has no idea what type the elements should be,
so it falls back to `LinkedHashMap` for each element.

The solution is `TypeReference<T>`, which captures the full parameterised type in an anonymous
subclass. Because the type argument is embedded in the subclass's generic supertype signature,
it survives erasure and Jackson can read it back via reflection.

```java
import com.fasterxml.jackson.core.type.TypeReference;

// Converts: JSON array string to List<Player>, preserving the element type at runtime
List<Player> players = MAPPER.readValue(
    json,
    new TypeReference<List<Player>>() {}
);
```

The `{}` creates the anonymous subclass. It looks unusual but is the standard Jackson idiom for
this situation.

### Pretty-printing

The default output of `writeValueAsString` is compact (no whitespace). For logging or
debugging, pretty-print using a configured writer:

```java
// Converts: Player to a human-readable indented JSON string
String pretty = MAPPER.writerWithDefaultPrettyPrinter()
                      .writeValueAsString(player);
```

Pretty-printed output over a socket is wasteful (larger payload, more bandwidth), so use it
only for debugging, not production message sending.

---

## Section 5 — Generic wrapper types and type erasure

### The problem with raw responses

Without a consistent response structure, a client receiving a message over a socket cannot
reliably tell success from failure:

```text
// What does this mean? Is "null" an error? Did the player not exist?
null

// Is this the player JSON, or an error message?
Player not found

// This works but has no standard shape — every handler is different
{"id":7,"name":"Alice","rating":9.2}
```

A **generic response wrapper** solves this by giving every reply the same envelope structure,
regardless of what data it carries.

### Designing `Response<T>`

```json
{
  "status": "SUCCESS",
  "message": "Player retrieved",
  "data": { "id": 7, "name": "Alice", "rating": 9.2 }
}
```

```json
{
  "status": "FAILURE",
  "message": "No player found for id=99",
  "data": null
}
```

The receiver always reads `status` first. If `"SUCCESS"`, it reads `data`. If `"FAILURE"`, it
reads `message` for the error description. The protocol is consistent across all entity types
and all operations.

### Implementing `Response<T>`

```java
/**
 * A generic response wrapper that standardises all server replies.
 * Carries a status string, a human-readable message, and an optional data payload.
 *
 * @param <T> the type of the data payload (may be null on failure)
 * @author OOP Teaching Team
 */
public class Response<T> {

    // === Fields ===
    private String fStatus;
    private String fMessage;
    private T fData;

    // === Constructors ===
    // Creates: empty response — required by Jackson for deserialisation
    public Response() {
        fStatus = "";
        fMessage = "";
        fData = null;
    }

    // Creates: response with all fields set
    public Response(String status, String message, T data) {
        fStatus = status;
        fMessage = message;
        fData = data;
    }

    // === Public API ===
    // Gets: the response status ("SUCCESS" or "FAILURE")
    public String getStatus() { return fStatus; }

    // Sets: the response status
    public void setStatus(String status) { fStatus = status; }

    // Gets: the human-readable result message
    public String getMessage() { return fMessage; }

    // Sets: the result message
    public void setMessage(String message) { fMessage = message; }

    // Gets: the response payload; null on failure responses
    public T getData() { return fData; }

    // Sets: the response payload
    public void setData(T data) { fData = data; }

    // === Helpers ===
    // Creates: a success response carrying the given data payload
    public static <T> Response<T> success(String message, T data) {
        return new Response<>("SUCCESS", message, data);
    }

    // Creates: a failure response with a null data payload
    public static <T> Response<T> failure(String message) {
        return new Response<>("FAILURE", message, null);
    }
}
```

### Serialising `Response<T>` — no special handling needed

Serialisation always works because Jackson inspects the actual runtime type of `fData`:

```java
Player player = new Player(1, "Alice", 9.2);
Response<Player> response = Response.success("Player found", player);

// Converts: Response<Player> to a JSON string — Jackson sees the actual Player at runtime
String json = MAPPER.writeValueAsString(response);
```

Output:

```json
{"status":"SUCCESS","message":"Player found","data":{"id":1,"name":"Alice","rating":9.2}}
```

### Deserialising `Response<T>` — type erasure strikes again

This is where the problem appears. If you write:

```java
// WRONG — Jackson has no idea what T is; 'data' will be deserialised as a LinkedHashMap
Response<Player> r = MAPPER.readValue(json, Response.class);
Player p = (Player) r.getData(); // ClassCastException at runtime!
```

At runtime, `Response.class` carries no information about `T`. Jackson falls back to
deserialising the `data` field as a generic `LinkedHashMap<String, Object>`, which is the
natural mapping for a JSON object when no target type is known. Casting that to `Player` fails
at runtime.

The fix is `TypeReference`, which preserves `T` through to runtime:

```java
// Converts: JSON string to Response<Player>, preserving the Player type parameter
Response<Player> r = MAPPER.readValue(
    json,
    new TypeReference<Response<Player>>() {}
);

Player p = r.getData(); // Works correctly — fData was deserialised as Player
```

### When `data` is a list

```java
// Converts: JSON string to Response<List<Player>>, preserving both generic layers
Response<List<Player>> r = MAPPER.readValue(
    json,
    new TypeReference<Response<List<Player>>>() {}
);

List<Player> players = r.getData();
```

### `TypeReference` decision table

| What you are deserialising | Use |
| :- | :- |
| A single concrete class: `Player` | `readValue(json, Player.class)` |
| A list: `List<Player>` | `new TypeReference<List<Player>>() {}` |
| A response wrapping a single object: `Response<Player>` | `new TypeReference<Response<Player>>() {}` |
| A response wrapping a list: `Response<List<Player>>` | `new TypeReference<Response<List<Player>>>() {}` |

The rule is simple: if the type you are deserialising has any generic parameter, use
`TypeReference`. If it is a plain concrete class with no generics, `ClassName.class` is
sufficient.

---

> **Continues in:** [JSON in Java II — Protocol Design & Advanced
> Jackson](../t21_json_2_jackson_advanced/t21_json_2_jackson_advanced_notes.md)

---

## Reflective questions

1. Jackson derives the JSON key from the getter, not the field. What does a field named
   `_trackId` with a getter `getTrackId()` serialise as, and why does that matter when your
   team renames things?
2. What exactly does Jackson need in order to *deserialise* JSON into your class, and what is
   the symptom when one of those is missing?
3. `mapper.readValue(json, List.class)` compiles, runs, and then throws `ClassCastException`
   the first time you use an element. Explain the chain of events.
4. `Response<T>` serialises with no special handling at all, but deserialising it needs
   `TypeReference`. Why is the problem asymmetric?
5. When is `@JsonIgnore` the right tool, and what would go wrong if you used it on a field the
   client needs?
6. Your server pretty-prints its JSON to make logs readable, and every client immediately
   breaks. What is the connection?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**1. Getter-derived keys.**
It serialises as `trackId`: Jackson strips the `get` prefix and lower-cases the first letter,
following the JavaBean convention. The private field name is invisible to it, which is why a
codebase that prefixes fields with `_` still produces clean JSON.

The consequence for a team is that **renaming a getter is a breaking protocol change**, even
though it looks like a purely internal refactor. `getTrackId()` returns `getId()` silently
changes the wire format and every deployed client stops finding the key. If you need the two to
move independently, pin the key with `@JsonProperty("trackId")` — then the getter can be
renamed freely.

**2. What Jackson needs to deserialise.**
A way to **construct** the object and a way to **populate** it: a no-argument constructor plus
setters, or a constructor annotated so Jackson can bind the parameters
(`@JsonCreator`/`@JsonProperty`, or a `record`, which Jackson handles natively).

The symptoms differ and both are worth recognising. No no-arg constructor gives
`InvalidDefinitionException: cannot construct instance ... no Creators`. Constructor present
but a setter missing gives something nastier — **no exception at all**, just a field left at
`null` or `0`, discovered much later. That silent case is exactly what a round-trip test
catches.

**3. The `List.class` chain.**
Type erasure removes the element type at compile time, so `List.class` tells Jackson only "some
list". With nothing to bind elements to, Jackson falls back to its default representation for a
JSON object: `LinkedHashMap`. The call succeeds and returns a `List` genuinely full of maps.

The compiler then permits `List<Player> players = ...` because the declared type says so, and
the failure is deferred to the first `players.get(0)`, which inserts a hidden cast and throws
`ClassCastException`. Note the crash lands on the *use*, not the *parse* — so the stack trace
points away from the real mistake. `new TypeReference<List<Player>>() {}` carries the full type
through erasure and fixes it at source.

**4. Why the asymmetry.**
Serialising works from an **object that already exists**. Jackson reflects over the live
instance, sees an actual `Track` in the `data` field, and writes it — the declared type
parameter is irrelevant because the runtime object knows what it is.

Deserialising has only **text plus a target type**. Erasure has removed `T` from `Response<T>`,
so Jackson knows to build a `Response` but has nothing telling it what `data` should become —
and defaults to `LinkedHashMap` again. Direction of information flow is the whole explanation:
going out, the object carries its own type; coming in, the type must be supplied.

**5. `@JsonIgnore`.**
Right for fields that should never cross the wire: a password hash, a session token, a database
connection, a cached value that is expensive and derivable. It is a **security and
payload-size** tool.

Used on a field the client needs, the field simply vanishes from the JSON — and on the way back
it deserialises to `null` or `0` with no error anywhere. The client sees a `Track` with a
missing title and nothing reports a problem. Worse, `@JsonIgnore` on a getter suppresses both
directions by default, so the field is neither written nor read. Whenever you add it, ask what
the *receiving* side will do with the gap.

**6. Pretty-printing on the wire.**
The protocol is **line-delimited**: one JSON object per line, and the receiver calls
`readLine()`. Pretty-printing inserts newlines, so a single message arrives as a dozen lines.
The receiver takes the first — `{` — tries to parse it as a whole message, and fails; the
remaining fragments are then read as further "messages", so the stream is permanently out of
step, not merely one bad request.

Pretty-print for logs by all means, but only into the log: keep `writeValueAsString`'s default
single-line output on the socket. This is the framing concern taken further in
[t21](../t21_json_2_jackson_advanced/t21_json_2_jackson_advanced_notes.md) and
[t22](../t22_networking/t22_networking_notes.md).

</details>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t19_concurrency
  domain_emphasis: Balanced

this_lesson:
  topic_code: t20_json_1_jackson_basics
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO4]
```
