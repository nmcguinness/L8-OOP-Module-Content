---
title: "Networking I — TCP Sockets and the Client-Server Protocol — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t22_networking
description: "Seven step-by-step exercises building a leaderboard server: an echo server, a multi-client server, a DTO and response wrapper, a JSON protocol with a dispatcher, error handling with DISCONNECT, and two extensions: a testable server and a stoppable one."
created: 2026-02-20
last_updated: 2026-11-03
version: 1.2
status: published
authors: ["OOP Teaching Team"]
tags: [java, networking, sockets, tcp, client-server, json, protocol, executorservice, exercises, year2, comp-c8z03]
---
# Networking I — TCP Sockets and the Client-Server Protocol — Exercises

These exercises build a leaderboard server that clients connect to over TCP, to submit scores and ask for rankings. They follow the [t22 notes](../t22_networking_notes.md) and use the thread pools from t19 and the Jackson code from t20 and t21.

A **protocol** is the set of rules both sides agree on: what a request looks like, and what comes back. In these exercises every message is one line of JSON.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution, and to its tests where there are some. Work through them in order.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 An echo server | `ServerSocket`, `Socket`, reading and writing lines | Foundation |
| 02 Many clients at once | an accept loop and a thread pool | Intermediate |
| 03 A score entry and a response wrapper | a validated DTO, `ServerResponse<T>` | Foundation |
| 04 A JSON protocol | `ClientRequest`, `RequestDispatcher`, `ClientHandler` | Intermediate |
| 05 Errors and disconnecting | structured errors, `DISCONNECT`, writing a protocol | Intermediate |
| 06 A server you can test | port 0, testing over loopback | Extension |
| 07 A server you can stop | a fixed pool, `AutoCloseable` | Extension |

## Rules for every exercise

- Write to a socket through `new PrintWriter(..., true)`. The `true` turns on auto-flush, so each line is sent at once.
- Read and write with `StandardCharsets.UTF_8`, so both ends agree on how text is encoded.
- Open every socket, reader and writer in try-with-resources.
- Never send an exception's text to a client. Always reply with a structured response.

## How to run

Each exercise goes in its own package, `t22_networking.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`.

Each exercise needs a server and a client running at the same time. To keep it to one program, start the server on a **daemon** thread, which does not stop the program from ending, then run the client on the main thread:

```java
Thread serverThread = new Thread(() -> {
    try {
        new EchoServer(9100).start();
    } catch (IOException e) {
        System.out.println("Server stopped: " + e.getMessage());
    }
});
serverThread.setDaemon(true);
serverThread.start();
Thread.sleep(200);
```

The `Thread.sleep(200)` gives the server time to start listening before the client connects.

---

## Exercise 01 — An echo server

**Goal:** Build the simplest possible server, which sends back every line it receives, and a client to talk to it. This proves the basic plumbing works: connecting, sending, reading and closing.

**You will need:**
[Part 1: A minimal echo server](../t22_networking_notes.md#part-1-a-minimal-echo-server).

**Steps**

1. Create `EchoServer`. Its constructor takes a port number.
2. In `start()`, open a `ServerSocket` on the port and call `accept()` to wait for one client.
3. Wrap the socket's input in a `BufferedReader` and its output in a `PrintWriter`.
4. Read lines in a loop. For each one, send back `ECHO: ` followed by the line. Stop when `readLine()` returns `null`, which means the client has disconnected.
5. Create `EchoClient`. Its constructor takes a host and a port.
6. In its `run()`, connect a `Socket`, send `ping`, `hello server` and `goodbye`, and print each reply.
7. In `Exercise.run()`, start the server on port 9100 on a daemon thread, wait 200 milliseconds, then run the client against `localhost`.

```java
public EchoServer(int port)
public void start() throws IOException
public EchoClient(String host, int port)
```

**Example**

```text
ECHO: ping
ECHO: hello server
ECHO: goodbye
```

**Rules**

- Auto-flush on both sides.
- The server handles one client, then finishes.

<details>
<summary>Hints</summary>

1. Several resources can share one `try`: `try (ServerSocket server = new ServerSocket(port); Socket socket = server.accept(); ...)`.
2. Wrap the stream, then the writer: `new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true)`, where `out` is `socket.getOutputStream()`.
3. If the client hangs waiting for a reply, the server's writer is probably not flushing. Check the `true`.

</details>

**Done when**

- [ ] `run()` prints the three lines shown.
- [ ] The server does not hang after the client disconnects.

**Package:** `t22_networking.exercises.ex01`

**Solution:** [t22 exercise 01 solution code](../../../../code/src/t22_networking/exercises/ex01/).

---

## Exercise 02 — Many clients at once

**Goal:** Let the server talk to several clients at the same time. The accept loop hands each new connection to a thread pool and goes straight back to waiting for the next one.

**You will need:** [Part 2: Multi-client server with ExecutorService](../t22_networking_notes.md#part-2-multi-client-server-with-executorservice).

**Steps**

1. Create `MultiClientEchoServer`. Its constructor takes a port and creates a pool with `Executors.newCachedThreadPool()`.
2. In `start()`, loop forever: `accept()` a socket and submit a `ClientHandler` for it to the pool.
3. `ClientHandler` implements `Runnable`. It echoes each line back with the thread's name in front: `[pool-1-thread-2] ECHO: ...`.
4. When `readLine()` returns `null`, the handler prints `Client disconnected:` and the client's address.
5. In `Exercise.run()`, start the server on port 9101 on a daemon thread and wait 200 milliseconds.
6. Start three client threads, `client-A`, `client-B` and `client-C`. Each sends two messages, prints the replies and disconnects.
7. Wait 1 second, then print `All clients done`.

```java
public MultiClientEchoServer(int port)
public void start() throws IOException
```

**Example** (one possible order)

```text
[pool-1-thread-3] ECHO: client-A msg-1
[pool-1-thread-1] ECHO: client-C msg-1
[pool-1-thread-2] ECHO: client-B msg-1
[pool-1-thread-1] ECHO: client-C msg-2
[pool-1-thread-2] ECHO: client-B msg-2
[pool-1-thread-3] ECHO: client-A msg-2
Client disconnected: /127.0.0.1
Client disconnected: /127.0.0.1
Client disconnected: /127.0.0.1
All clients done
```

**Rules**

- The accept loop never reads from a client itself. It only accepts and hands over.

<details>
<summary>Hints</summary>

1. `pool.submit(new ClientHandler(socket));` is the whole body of the loop after `accept()`.
2. Do **not** open the client socket in try-with-resources inside the accept loop. The handler runs later, on another thread, and would find the socket already closed. Let the handler close it.

</details>

**Done when**

- [ ] Every client gets both of its replies.
- [ ] Lines from different clients are mixed together, showing they were handled at the same time.

> [!NOTE]
> The program may take up to a minute to exit after `All clients done`. A cached pool keeps idle threads alive for 60 seconds in case more work arrives. Called from `Main`, this does not matter.

**Package:** `t22_networking.exercises.ex02`

**Solution:** [t22 exercise 02 solution code](../../../../code/src/t22_networking/exercises/ex02/).

---

## Exercise 03 — A score entry and a response wrapper

**Goal:** Before adding any more network code, write the two classes every message will use: a `ScoreEntry` holding one score, and a `ServerResponse<T>` that wraps every reply the server sends.

**You will need:** [Part 3: Designing a JSON protocol](../t22_networking_notes.md#part-3-designing-a-json-protocol) and [ServerResponse&lt;T&gt; — the response wrapper](../t22_networking_notes.md#serverresponset--the-response-wrapper).

**Steps**

1. Create `ScoreEntry` with five fields: `id`, `playerId`, `playerName`, `score` and `gameMode`, a constructor and a getter for each.
2. In the constructor, reject a blank `playerId`, `playerName` or `gameMode`, and a negative `score` or `id`. Trim the text, and make `gameMode` upper case.
3. Create `ServerResponse<T>` with three fields: `status`, `message` and `data`, and a getter for each.
4. Add two static factory methods: `ok(message, data)` sets the status to `OK`, and `error(message)` sets it to `ERROR` with `null` data.
5. Add `isOk()`, which returns `"OK".equals(status)`. Mark it `@JsonIgnore`.
6. In `run()`, make two score entries, wrap them in an `ok` response and make one `error` response. Print each as JSON with an `ObjectMapper`.

```java
public ScoreEntry(int id, String playerId, String playerName, int score, String gameMode)
public static <T> ServerResponse<T> ok(String message, T data)
public static <T> ServerResponse<T> error(String message)
```

**Example**

```text
{"status":"OK","message":"scores loaded","data":[{"id":1,"playerId":"P001","playerName":"Alice","score":4200,"gameMode":"RANKED"},{"id":2,"playerId":"P002","playerName":"Bob","score":3875,"gameMode":"RANKED"}]}
{"status":"ERROR","message":"player not found","data":null}
```

**Rules**

- `ServerResponse` uses a type parameter `<T>`. No raw types.

<details>
<summary>Hints</summary>

1. Without `@JsonIgnore`, Jackson sees `isOk()` as a getter and sends an extra `"ok":true` field.
2. The `<T>` before the return type makes the factory method generic: `public static <T> ServerResponse<T> ok(String message, T data)`.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] `ServerResponse.ok("found", entry)` with a single `ScoreEntry` also compiles, with no cast.

**Package:** `t22_networking.exercises.ex03`

**Solution:** [t22 exercise 03 solution code](../../../../code/src/t22_networking/exercises/ex03/).

---

## Exercise 04 — A JSON protocol

**Goal:** Build the leaderboard server. Each request is one line of JSON naming a request type. A dispatcher sends it to the right handler, and the reply goes back as one line of JSON.

**You will need:** [Part 4: A full JSON protocol server](../t22_networking_notes.md#part-4-a-full-json-protocol-server), especially [RequestHandler and RequestDispatcher](../t22_networking_notes.md#requesthandler-and-requestdispatcher) and [ClientHandler — socket session only](../t22_networking_notes.md#clienthandler--socket-session-only).

The three request types and their replies:

```text
GET_ALL
  request:  {"requestType":"GET_ALL","payload":{}}
  response: ServerResponse<List<ScoreEntry>>, highest score first

SUBMIT_SCORE
  request:  {"requestType":"SUBMIT_SCORE",
             "payload":{"playerId":"P001","playerName":"Alice","score":4200,"gameMode":"ranked"}}
  response: ServerResponse<ScoreEntry>, with its new id

GET_TOP_N
  request:  {"requestType":"GET_TOP_N","payload":{"n":2}}
  response: ServerResponse<List<ScoreEntry>>, the top n by score
```

**Steps**

1. Copy `ScoreEntry` and `ServerResponse<T>` from Exercise 03.
2. Create an enum `RequestType` with `GET_ALL`, `SUBMIT_SCORE` and `GET_TOP_N`.
3. Create `ClientRequest` with a `requestType` and a `payload` (`Map<String, Object>`), as in the notes, with `getString(key)` and `getInt(key)` helpers.
4. Create `ScoreRepository`. It keeps scores in an `ArrayList` with an id counter. `insert`, `getAll` and `getTop` are all `synchronized`. `getAll` returns a sorted copy.
5. Create the interface `RequestHandler` and the class `RequestDispatcher`. The dispatcher keeps one handler per request type in an `EnumMap`, and has one private method per handler.
6. `dispatch` never throws. It catches `IllegalArgumentException` and returns `ServerResponse.error("invalid request: " + message)`.
7. Create `ClientHandler`. It reads each line, turns it into a `ClientRequest` with Jackson, passes it to the dispatcher, and writes the response as JSON. A line that is not valid JSON gets a `malformed request` error.
8. Create `LeaderboardServer`, an accept loop with a cached pool that gives one shared dispatcher to every `ClientHandler`.
9. In `run()`, start the server on port 9102 and wait 300 milliseconds. Client 1 submits three scores, client 2 sends `GET_ALL`, and client 3 sends `GET_TOP_N` with `n` of 2. Print every reply.

```java
public interface RequestHandler {
    ServerResponse<?> handle(ClientRequest request);
}

public ServerResponse<?> dispatch(ClientRequest request)
```

**Example** (shortened)

```text
SUBMIT: {"status":"OK","message":"score recorded","data":{"id":1,"playerId":"P001",...,"score":4200,...}}
SUBMIT: {"status":"OK","message":"score recorded","data":{"id":2,"playerId":"P002",...,"score":3875,...}}
SUBMIT: {"status":"OK","message":"score recorded","data":{"id":3,"playerId":"P003",...,"score":5100,...}}
GET_ALL: {"status":"OK","message":"3 scores","data":[{"id":3,...},{"id":1,...},{"id":2,...}]}
GET_TOP_N: {"status":"OK","message":"top 2","data":[{"id":3,...},{"id":1,...}]}
```

**Rules**

- `ClientHandler` contains no request logic. `RequestDispatcher` contains no socket code.
- A bad `SUBMIT_SCORE`, such as one with no `playerId`, gets an error response. It never crashes the handler.
- Build every response with Jackson. Never join strings to make JSON.

<details>
<summary>Hints</summary>

1. Register handlers with method references: `handlers.put(RequestType.GET_ALL, this::getAll);`.
2. `ScoreEntry`'s constructor already rejects missing fields. Let its `IllegalArgumentException` reach `dispatch`, which turns it into an error response.
3. Because the dispatcher has no socket code, you can test it by calling `dispatch` directly. The worked tests do exactly this.

</details>

**Done when**

- [ ] `run()` prints five replies like those shown.
- [ ] `GET_ALL` and `GET_TOP_N` list the highest score first.
- [ ] Sending `not json` gets a `malformed request` error, and the connection stays open.

**Package:** `t22_networking.exercises.ex04`

**Solution:** [t22 exercise 04 solution code](../../../../code/src/t22_networking/exercises/ex04/).
**Tests:** [t22 exercise 04 tests](../../../../code/test/t22_networking/exercises/ex04/).

---

## Exercise 05 — Errors and disconnecting

**Goal:** Make the server robust. Add a `DISCONNECT` request that ends a session politely, check more of what clients send, and write down the protocol so someone else could write a client.

**You will need:**
[Part 5: Protocol documentation](../t22_networking_notes.md#part-5-protocol-documentation) and [Common mistakes](../t22_networking_notes.md#common-mistakes).

**Steps**

1. Copy your Exercise 04 classes into this package.
2. Add `DISCONNECT` to `RequestType`. Register a handler that returns `ServerResponse.ok("goodbye", null)`.
3. In `ClientHandler`, after sending the reply to a `DISCONNECT`, leave the read loop so the socket closes.
4. Create an enum `GameMode` with `RANKED`, `CASUAL` and `PRACTICE`, and a static method `parse(String text)` that returns an `Optional<GameMode>`, empty for an unknown name.
5. In the dispatcher's `SUBMIT_SCORE` handler, reject a score above 999,999 and a game mode that `parse` does not recognise.
6. In `run()`, start the server on port 9103. Submit a score, then send `DISCONNECT`. On a second connection, submit a score with no `playerId`, then one with the game mode `TOURNAMENT`. Print every reply.
7. Write `PROTOCOL.md`, describing every request type: its payload fields and its success response, plus one example error.

**Example**

```text
Client sent DISCONNECT; closing session
Score submitted: {"status":"OK","message":"score recorded","data":{"id":1,...}}
Disconnect: {"status":"OK","message":"goodbye","data":null}
Missing playerId: {"status":"ERROR","message":"invalid request: playerId is required","data":null}
Invalid mode: {"status":"ERROR","message":"invalid request: invalid game mode: TOURNAMENT","data":null}
```

The first line comes from the server, so it may appear in a different place.

**Rules**

- Decide whether to disconnect from the parsed `ClientRequest`, not by searching the raw line for the word `DISCONNECT`.
- `DISCONNECT` has its own handler. It must not fall through to `unsupported request type`.

<details>
<summary>Hints</summary>

1. `ClientHandler` needs to know the request type **and** send the reply. Parse the line once, keep the `ClientRequest`, and check `request.getRequestType() == RequestType.DISCONNECT` after writing the response.
2. `GameMode.valueOf(text)` throws an `IllegalArgumentException` for an unknown name, so `parse` catches it and returns `Optional.empty()`. The handler then uses `.orElseThrow(() -> new IllegalArgumentException("invalid game mode: " + text))`.

</details>

**Done when**

- [ ] `run()` prints the replies shown.
- [ ] After `DISCONNECT`, neither side throws or prints `null`.
- [ ] `PROTOCOL.md` covers all four request types and one error.

**Package:** `t22_networking.exercises.ex05`

**Solution:** [t22 exercise 05 solution code](../../../../code/src/t22_networking/exercises/ex05/), including [PROTOCOL.md](../../../../code/src/t22_networking/exercises/ex05/PROTOCOL.md).
**Tests:** [t22 exercise 05 tests](../../../../code/test/t22_networking/exercises/ex05/).

---

## Exercise 06 — A server you can test (extension)

**Goal:** Write a server that a JUnit test can start and talk to. Asking for port `0` lets the operating system choose a free port, so a test never fails because the port is already in use.

**You will need:** [Protocol rule: one JSON object per line](../t22_networking_notes.md#protocol-rule-one-json-object-per-line) and your `ClientRequest` and `ServerResponse` from t21.

**Steps**

1. Create `EchoServer`. Its constructor takes a port and opens the `ServerSocket` straight away.
2. Add `port()`, which returns `serverSocket.getLocalPort()`.
3. Add `reply(String line)`, which returns the reply to one line as JSON. It reads a `ClientRequest` and returns `ServerResponse.ok("echo", request.getRequestType())` as text.
4. If the line is not valid JSON, the reply is `ServerResponse.error("not a valid request")`.
5. Add `serveOneClient()`, which accepts **one** client and answers each line it sends.
6. Create `EchoClient` with a static method that sends one line and returns the reply.
7. In `run()`, create the server on port `0`, start it on a thread, send a request, print the reply, and `join()` the thread.
8. Write a test that calls `reply` directly, and one that does the whole round trip.

```java
public EchoServer(int port) throws IOException
public int port()
public void serveOneClient() throws IOException
public static String send(String host, int port, String json) throws IOException
```

**Example** (the client sends `{"requestType":"GET_PLAYER","payload":{"id":42}}`)

```text
Reply: {"status":"OK","message":"echo","data":"GET_PLAYER"}
```

**Rules**

- One JSON message per line, sent with `println`.
- Import `ClientRequest` and `ServerResponse`. Do not copy them.

> [!WARNING]
> `PrintWriter` with auto-flush set to `true` flushes on `println` but **not** on `print`. A message sent with `print` never leaves the buffer, and the other end waits forever.

<details>
<summary>Hints</summary>

1. Open the `ServerSocket` in the constructor, not in `serveOneClient`. The client can then connect as soon as the constructor returns, so you do not need `Thread.sleep`.
2. `reply` needs no socket at all, which makes it the easiest part to test.

</details>

**Done when**

- [ ] `run()` prints the line shown.
- [ ] Sending `not json` gets a reply with `"status":"ERROR"`.
- [ ] A test does the round trip with a `@Timeout`, so a hang fails instead of blocking the build.

**Package:** `t22_networking.exercises.ex06`

**Solution:** [t22 exercise 06 solution code](../../../../code/src/t22_networking/exercises/ex06/).
**Tests:** [t22 exercise 06 tests](../../../../code/test/t22_networking/exercises/ex06/).

---

## Exercise 07 — A server you can stop (extension)

**Goal:** The accept loops so far run forever. Build a server with a fixed pool that a test or a program can shut down cleanly, and show two clients connected **at the same time**.

**You will need:** [Part 2: Multi-client server with ExecutorService](../t22_networking_notes.md#part-2-multi-client-server-with-executorservice).

**Steps**

1. Create `PooledEchoServer` with `Executors.newFixedThreadPool(4)`. It implements `AutoCloseable`.
2. Its `start()` method accepts a client, hands it to the pool with `submit`, and goes straight back to `accept()`.
3. Each pool thread serves one client until that client disconnects. Put the thread's name in the reply message, so you can see which thread answered.
4. `close()` closes the server socket and shuts down the pool.
5. In `run()`, open two client connections, keep both open, and send messages from each in turn.

```java
public PooledEchoServer(int port) throws IOException
public void start()
public void close() throws IOException
```

**Example**

```text
A: {"status":"OK","message":"handled on pool-1-thread-1","data":"FIRST"}
B: {"status":"OK","message":"handled on pool-1-thread-2","data":"SECOND"}
A again: {"status":"OK","message":"handled on pool-1-thread-1","data":"THIRD"}
```

Each client keeps its own thread for its whole connection.

**Rules**

- Client B is answered while client A is still connected.
- Closing the server ends the accept loop without an error message.

<details>
<summary>Hints</summary>

1. `pool.submit(() -> serve(client));` hands one client to the pool.
2. `close()` makes the blocked `accept()` throw `SocketException`. Catch it: that is the normal way for the loop to stop.

</details>

**Done when**

- [ ] Both clients get replies while both are connected.

**Think about it:** your server from Exercise 06 does not refuse a second client; it accepts it late. What does that second client experience while the first is still connected, and why is that worse than being refused?

**Package:** `t22_networking.exercises.ex07`

**Solution:** [t22 exercise 07 solution code](../../../../code/src/t22_networking/exercises/ex07/).
**Tests:** [t22 exercise 07 tests](../../../../code/test/t22_networking/exercises/ex07/).
