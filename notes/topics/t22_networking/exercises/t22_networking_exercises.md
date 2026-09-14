---
title: "Networking I — TCP Sockets and the Client-Server Protocol — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t22_networking
description: "Five progressive exercises building a leaderboard server: echo server, multi-client dispatch, DTOs, a JSON protocol, and error handling."
created: 2026-02-20
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, networking, sockets, tcp, client-server, json, protocol, executorservice, exercises, year2, comp-c8z03]
---
# Networking I — TCP Sockets and the Client-Server Protocol — Exercises

These exercises reinforce **Networking I — TCP Sockets and the Client-Server Protocol** by
building a **Leaderboard Server** that clients connect to over TCP, submitting scores and
requesting rankings.

## Ground rules

- Always use `new PrintWriter(out, true)` (auto-flush) when writing to a socket.
- Always wrap socket streams in try-with-resources or close in `finally`.
- Never propagate exceptions as raw text to the client — always return a structured response.
- One JSON object per line: serialise to a single string before calling `out.println()`.
- Prefer simple loops over streams unless asked otherwise.

## How to run

Each exercise uses:

- **Folder:** `/exercises/topics/t22_networking/exercises/eXX/`
- **Filename:** `Exercise.java`
- **Package:** `t22_networking.exercises.eXX`

```java
package t22_networking.exercises.ex01;

public class Exercise {
    public static void run() throws Exception {
        // start server on a background thread, then run client
    }
}
```

> **Tip:** In exercises that require both a server and a client, start the server on a daemon
> thread first, then run the client on the main thread. This avoids needing two separate
> processes.

```java
Thread serverThread = new Thread(() -> {
    try { new MyServer(9100).start(); }
    catch (Exception e) { e.printStackTrace(); }
});
serverThread.setDaemon(true);   // JVM exits when main thread finishes
serverThread.start();
Thread.sleep(200);              // give the server time to bind
```

## Before you start

### Prerequisites checklist

- [ ] Completed the Networking I notes
- [ ] Completed the Concurrency I exercises (ExecutorService)
- [ ] You can serialise/deserialise objects with Jackson `ObjectMapper`
- [ ] You understand interfaces and generics

---

## Exercise 01 — Echo server and matching client

**Objective:** Build the simplest possible TCP server — one that echoes every line back to the
client — and a matching client. Establish that auto-flush, `readLine`, and try-with-resources
work correctly.

**Context (software + games):**

- **Software dev:** An echo server is the "hello world" of network programming. Every
  production server builds on the same socket primitives.
- **Games dev:** Many multiplayer game servers start with an echo architecture to verify
  connectivity before adding game logic.

### What you are building

- `EchoServer` — binds to a port, accepts one client, echoes each line
- `EchoClient` — connects, sends three messages, prints replies

### Required API

```java
public class EchoServer {
    public EchoServer(int port);
    public void start() throws Exception;  // blocks; handles one client
}

public class EchoClient {
    public EchoClient(String host, int port);
    public void run() throws Exception;
}
```

### Tasks

1. Implement `EchoServer.start()`:
   - Binds a `ServerSocket` to `_port`.
   - Calls `accept()` to wait for one client.
   - Reads lines with `BufferedReader`; echoes each as `"ECHO: " + line`.
   - Stops when `readLine()` returns `null` (client disconnected).
2. Implement `EchoClient.run()`:
   - Connects a `Socket` to `_host`/`_port`.
   - Sends three messages: `"ping"`, `"hello server"`, `"goodbye"`.
   - Reads and prints each reply.
3. In `Exercise.run()`:
   - Start `EchoServer` on a daemon background thread (port 9100).
   - Sleep 200ms.
   - Run `EchoClient`.

### Sample output

```text
ECHO: ping
ECHO: hello server
ECHO: goodbye
```

### Constraints

- Use `new PrintWriter(out, true)` for auto-flush on both sides.
- Use try-with-resources for `ServerSocket`, `Socket`, `BufferedReader`, `PrintWriter`.

### Done when...

- All three replies are printed.
- The server does not hang after the client disconnects.
- No resources are left unclosed.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t22_networking.exercises.ex01;

import java.io.*;
import java.net.*;

public class Exercise {

    public static void run() throws Exception {
        Thread serverThread = new Thread(() -> {
            try {
                new EchoServer(9_100).start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        Thread.sleep(200);
        new EchoClient("localhost", 9_100).run();
    }
}

class EchoServer {

    // === Fields ===
    private int _port;

    // === Constructors ===
    // Creates: an echo server on the given port
    public EchoServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024–65535");
        _port = port;
    }

    // === Public API ===
    // Starts: the server — accepts one client and echoes every line
    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(_port)) {
            Socket client = serverSocket.accept();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    out.println("ECHO: " + line);
                }
            }
        }
    }
}

class EchoClient {

    // === Fields ===
    private String _host;
    private int _port;

    // === Constructors ===
    // Creates: a client targeting the given host and port
    public EchoClient(String host, int port) {
        if (host == null || host.isBlank())
            throw new IllegalArgumentException("host is required");
        _host = host;
        _port = port;
    }

    // === Public API ===
    // Runs: sends three messages and prints replies
    public void run() throws Exception {
        try (Socket socket = new Socket(_host, _port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String[] messages = {"ping", "hello server", "goodbye"};

            for (String msg : messages) {
                out.println(msg);
                System.out.println(in.readLine());
            }
        }
    }
}
```

</details>

---

## Exercise 02 — Multi-client server with `ExecutorService`

**Objective:** Extend the echo server to handle multiple simultaneous clients by submitting
each accepted connection to a thread pool.

**Context (software + games):**

- **Software dev:** Every production server must handle concurrent users. The pool ensures the
  accept loop never blocks waiting for one client to finish.
- **Games dev:** A match lobby server must accept new players while existing ones are already
  connected.

### What you are building

- `MultiClientEchoServer` — accept loop + `ExecutorService` + inner `ClientHandler`
- A demo that connects three clients simultaneously

### Required API

```java
public class MultiClientEchoServer {
    public MultiClientEchoServer(int port);
    public void start() throws Exception;  // accept loop — runs indefinitely
}
```

### Tasks

1. Implement `MultiClientEchoServer`:
   - Creates a `Executors.newCachedThreadPool()`.
   - `start()` loops on `serverSocket.accept()`, submitting each `Socket` to the pool as an
     anonymous `Runnable` (or inner `ClientHandler` class).
   - Each handler echoes lines with the prefix `"[thread-name] ECHO: "`.
   - On client disconnect, prints `"Client disconnected: address"`.
2. In `Exercise.run()`:
   - Start the server on a daemon thread (port 9101).
   - Sleep 200ms.
   - Start three client threads simultaneously, each sending two messages then disconnecting.
   - Sleep 1000ms then print `"All clients done"`.

### Sample output

```text
[pool-1-thread-1] ECHO: client-A msg-1
[pool-1-thread-2] ECHO: client-B msg-1
[pool-1-thread-3] ECHO: client-C msg-1
[pool-1-thread-1] ECHO: client-A msg-2
...
All clients done
```

### Constraints

- The accept loop must not block on any single client.
- Each client gets its own thread from the pool (visible in thread names).

### Done when...

- All three clients receive correct replies.
- Output from the three clients is interleaved (proving concurrent handling).
- The server keeps running after all clients disconnect.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t22_networking.exercises.ex02;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Exercise {

    public static void run() throws Exception {
        Thread serverThread = new Thread(() -> {
            try {
                new MultiClientEchoServer(9_101).start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(200);

        String[] clientNames = {"client-A", "client-B", "client-C"};

        for (String name : clientNames) {
            Thread t = new Thread(() -> {
                try (Socket s = new Socket("localhost", 9_101);
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                     PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

                    out.println(name + " msg-1");
                    System.out.println(in.readLine());
                    out.println(name + " msg-2");
                    System.out.println(in.readLine());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }

        Thread.sleep(1_000);
        System.out.println("All clients done");
    }
}

class MultiClientEchoServer {

    // === Fields ===
    private int _port;
    private ExecutorService _pool;

    // === Constructors ===
    // Creates: a multi-client echo server on the given port
    public MultiClientEchoServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024–65535");
        _port = port;
        _pool = Executors.newCachedThreadPool();
    }

    // === Public API ===
    // Starts: the accept loop — handles each client on a pool thread
    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(_port)) {
            while (true) {
                Socket client = serverSocket.accept();
                _pool.submit(new ClientHandler(client));
            }
        }
    }

    // === Helpers ===
    // Handles: one client connection — echoes lines until disconnect
    private static class ClientHandler implements Runnable {

        private Socket _socket;

        // Creates: a handler for the given socket
        public ClientHandler(Socket socket) {
            _socket = socket;
        }

        // Runs: reads lines and echoes them with the thread name prefix
        @Override
        public void run() {
            String name = Thread.currentThread().getName();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                 PrintWriter out = new PrintWriter(_socket.getOutputStream(), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    out.println("[" + name + "] ECHO: " + line);
                }
            }
            catch (IOException e) {
                System.out.println("Client disconnected: " + _socket.getInetAddress());
            }
            finally {
                try { _socket.close(); } catch (IOException ignored) { }
            }
        }
    }
}
```

</details>

---

## Exercise 03 — `ScoreEntry` DTO and `ServerResponse<T>`

**Objective:** Define the domain model and response wrapper before adding network logic. A
`ScoreEntry` holds one player score; `ServerResponse<T>` standardises all server replies.

**Context (software + games):**

- **Software dev:** Defining the response envelope before writing handlers ensures consistent
  output from day one.
- **Games dev:** Leaderboards, match results, and inventory systems all benefit from a generic
  response wrapper that carries status, message, and typed data.

### What you are building

- `ScoreEntry` domain class (playerId, playerName, score, gameMode)
- `ServerResponse<T>` generic wrapper with static factory methods
- A smoke test that creates both and prints them as JSON

### Required API

```java
public class ScoreEntry {
    public ScoreEntry(int id, String playerId, String playerName, int score, String gameMode);
    // getters; validated constructor; toString()
}

public class ServerResponse<T> {
    public static <T> ServerResponse<T> ok(String message, T data);
    public static <T> ServerResponse<T> error(String message);
    public String getStatus();
    public String getMessage();
    public T getData();
    public boolean isOk();
}
```

### Tasks

1. Implement `ScoreEntry`:
   - Validate: `playerId`, `playerName`, `gameMode` required (non-null, non-blank); `score >=
     0`; `id >= 0`
   - Trim strings, uppercase `gameMode`
   - Implement `toString()` as `"ScoreEntry{id=N, player=name, score=N, mode=X}"`
2. Implement `ServerResponse<T>`:
   - Fields: `_status` (String), `_message` (String), `_data` (T)
- `ok(message, data)` returns `status="OK"`
- `error(message)` returns `status="ERROR"`, `data=null`
  - `isOk()` returns `"OK".equals(_status)`
  - Add Jackson-friendly getters (`getStatus()`, `getMessage()`, `getData()`)
1. In `Exercise.run()`:
   - Create two `ScoreEntry` objects
   - Create `ServerResponse.ok("scores loaded", List.of(entry1, entry2))`
   - Create `ServerResponse.error("player not found")`
   - Use `ObjectMapper` to serialise both responses to JSON strings and print them

### Sample output

```json
{"status":"OK","message":"scores loaded","data":[{"id":1,"playerId":"P001",...},{"id":2,...}]}
{"status":"ERROR","message":"player not found","data":null}
```

### Constraints

- `ServerResponse` must use a type parameter `<T>` — no raw types.
- Both factory methods must be static.

### Done when...

- Both JSON strings print correctly.
- Changing `T` from `List<ScoreEntry>` to `ScoreEntry` compiles without casting.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t22_networking.exercises.ex03;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class Exercise {

    public static void run() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ScoreEntry e1 = new ScoreEntry(1, "P001", "Alice", 4_200, "ranked");
        ScoreEntry e2 = new ScoreEntry(2, "P002", "Bob", 3_875, "ranked");

        ServerResponse<List<ScoreEntry>> ok = ServerResponse.ok("scores loaded", List.of(e1, e2));
        ServerResponse<Void> error = ServerResponse.error("player not found");

        System.out.println(mapper.writeValueAsString(ok));
        System.out.println(mapper.writeValueAsString(error));
    }
}

class ScoreEntry {

    // === Fields ===
    private int _id;
    private String _playerId;
    private String _playerName;
    private int _score;
    private String _gameMode;

    // === Constructors ===
    // Creates: a validated score entry
    public ScoreEntry(int id, String playerId, String playerName, int score, String gameMode) {
        if (id < 0)
            throw new IllegalArgumentException("id must be >= 0");
        if (playerId == null || playerId.isBlank())
            throw new IllegalArgumentException("playerId is required");
        if (playerName == null || playerName.isBlank())
            throw new IllegalArgumentException("playerName is required");
        if (score < 0)
            throw new IllegalArgumentException("score must be >= 0");
        if (gameMode == null || gameMode.isBlank())
            throw new IllegalArgumentException("gameMode is required");

        _id = id;
        _playerId = playerId.trim();
        _playerName = playerName.trim();
        _score = score;
        _gameMode = gameMode.trim().toUpperCase();
    }

    // === Public API ===
    public int getId() { return _id; }
    public String getPlayerId() { return _playerId; }
    public String getPlayerName() { return _playerName; }
    public int getScore() { return _score; }
    public String getGameMode() { return _gameMode; }

    // === Overrides ===
    @Override
    public String toString() {
        return "ScoreEntry{id=" + _id
            + ", player=" + _playerName
            + ", score=" + _score
            + ", mode=" + _gameMode + "}";
    }
}

class ServerResponse<T> {

    // === Fields ===
    private String _status;
    private String _message;
    private T _data;

    // === Constructors ===
    // Creates: a response with all fields set
    public ServerResponse(String status, String message, T data) {
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");
        _status = status;
        _message = message;
        _data = data;
    }

    // === Public API ===
    // Creates: a successful response with data
    public static <T> ServerResponse<T> ok(String message, T data) {
        return new ServerResponse<>("OK", message, data);
    }

    // Creates: an error response with no data
    public static <T> ServerResponse<T> error(String message) {
        return new ServerResponse<>("ERROR", message, null);
    }

    // Gets: the status string
    public String getStatus() { return _status; }

    // Gets: the human-readable message
    public String getMessage() { return _message; }

    // Gets: the data payload
    public T getData() { return _data; }

    // Checks: whether the response represents success
    public boolean isOk() { return "OK".equals(_status); }
}
```

</details>

---

## Exercise 04 — JSON protocol: `GET_ALL`, `SUBMIT_SCORE`, `GET_TOP`

**Objective:** Build a full leaderboard server with three request types handled via a
`ClientHandler`, using the `ScoreEntry`, `ServerResponse<T>`, and `ObjectMapper` from Exercise
03.

**Context (software + games):**

- **Software dev:** REST APIs and internal microservices use the same request/dispatch/response
  pattern — just over HTTP rather than raw TCP.
- **Games dev:** Match servers, inventory services, and leaderboard backends all implement a
  protocol exactly like this one.

### What you are building

- `LeaderboardServer` — accept loop backed by `ExecutorService`
- `ClientHandler` — reads one JSON request per line, dispatches to a handler, replies with JSON
- Three request types: `GET_ALL`, `SUBMIT_SCORE`, `GET_TOP_N`
- In-memory score list protected by `synchronized`

### Required request/response shapes

```text
GET_ALL
  request:  { "requestType": "GET_ALL", "payload": {} }
  response: ServerResponse<List<ScoreEntry>> — all entries ordered by score desc

SUBMIT_SCORE
  request:  { "requestType": "SUBMIT_SCORE",
               "payload": { "playerId": "P001", "playerName": "Alice",
                            "score": 4200, "gameMode": "ranked" } }
  response: ServerResponse<ScoreEntry> — the inserted entry with generated ID

GET_TOP_N
  request:  { "requestType": "GET_TOP_N", "payload": { "n": 3 } }
  response: ServerResponse<List<ScoreEntry>> — top N entries by score desc
```

### Tasks

1. Reuse `ScoreEntry` and `ServerResponse<T>` from Exercise 03.
2. Add `ClientRequest` (as in the notes): fields `requestType` (String) and `payload`
   (`Map<String, Object>`), with `getString(key)` and `getInt(key)` helpers.
3. Implement an in-memory `ScoreRepository`:
   - `ArrayList<ScoreEntry>` storage; auto-incrementing ID counter.
   - `synchronized insert(...)` returns the new `ScoreEntry`.
   - `synchronized getAll()` returns a copy sorted by score descending.
   - `synchronized getTop(int n)` returns the top N from `getAll()`.
4. Implement `ClientHandler.dispatch(ClientRequest req)`:
- `GET_ALL` to `repo.getAll()`
- `SUBMIT_SCORE` to validate payload fields; `repo.insert(...)`
- `GET_TOP_N` to `repo.getTop(n)`
- Unknown type to `ServerResponse.error("unknown request type: " + type)`
1. Implement `LeaderboardServer` with a `newCachedThreadPool()` accept loop.
2. In `Exercise.run()`:
   - Start server (port 9102) on a daemon thread; sleep 300ms.
   - Client 1: submits three scores.
   - Client 2: sends `GET_ALL`.
   - Client 3: sends `GET_TOP_N` with `n=2`.
   - Print all replies.

### Sample output

```text
SUBMIT: {"status":"OK","message":"score recorded","data":{"id":1,"playerId":"P001",...}}
SUBMIT: {"status":"OK","message":"score recorded","data":{"id":2,...}}
SUBMIT: {"status":"OK","message":"score recorded","data":{"id":3,...}}
GET_ALL: {"status":"OK","message":"3 scores","data":[...sorted desc...]}
GET_TOP_N: {"status":"OK","message":"top 2","data":[...top 2...]}
```

### Constraints

- `ScoreRepository` methods that touch the list must be `synchronized`.
- The server must handle all three clients — they may connect simultaneously.
- Invalid `SUBMIT_SCORE` payloads (missing `playerId` etc.) must return a structured error, not
  throw.

### Done when...

- All three request types return correct, well-formed JSON responses.
- Scores in `GET_ALL` and `GET_TOP_N` are sorted by score descending.
- Two simultaneous clients both get correct independent responses.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t22_networking.exercises.ex04;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Exercise {

    public static void run() throws Exception {
        ScoreRepository repo = new ScoreRepository();

        Thread serverThread = new Thread(() -> {
            try {
                new LeaderboardServer(9_102, repo).start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(300);

        ObjectMapper mapper = new ObjectMapper();

        // Client 1: submit three scores
        try (Socket s = new Socket("localhost", 9_102);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            String[] entries = {
                "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P001\",\"playerName\":\"Alice\",\"score\":4200,\"gameMode\":\"ranked\"}}",
                "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P002\",\"playerName\":\"Bob\",\"score\":3875,\"gameMode\":\"ranked\"}}",
                "{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P003\",\"playerName\":\"Carol\",\"score\":5100,\"gameMode\":\"ranked\"}}"
            };

            for (String req : entries) {
                out.println(req);
                System.out.println("SUBMIT: " + in.readLine());
            }
        }

        // Client 2: GET_ALL
        try (Socket s = new Socket("localhost", 9_102);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            out.println("{\"requestType\":\"GET_ALL\",\"payload\":{}}");
            System.out.println("GET_ALL: " + in.readLine());
        }

        // Client 3: GET_TOP_N
        try (Socket s = new Socket("localhost", 9_102);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            out.println("{\"requestType\":\"GET_TOP_N\",\"payload\":{\"n\":2}}");
            System.out.println("GET_TOP_N: " + in.readLine());
        }
    }
}

// ------- Domain -------

class ScoreEntry {
    private int _id; private String _playerId, _playerName, _gameMode; private int _score;
    public ScoreEntry() { _playerId = ""; _playerName = ""; _gameMode = ""; }
    public ScoreEntry(int id, String playerId, String playerName, int score, String gameMode) {
        if (playerId == null || playerId.isBlank()) throw new IllegalArgumentException("playerId required");
        if (playerName == null || playerName.isBlank()) throw new IllegalArgumentException("playerName required");
        if (score < 0) throw new IllegalArgumentException("score must be >= 0");
        if (gameMode == null || gameMode.isBlank()) throw new IllegalArgumentException("gameMode required");
        _id = id; _playerId = playerId.trim(); _playerName = playerName.trim();
        _score = score; _gameMode = gameMode.trim().toUpperCase();
    }
    public int getId() { return _id; }
    public String getPlayerId() { return _playerId; }
    public String getPlayerName() { return _playerName; }
    public int getScore() { return _score; }
    public String getGameMode() { return _gameMode; }
}

class ServerResponse<T> {
    private String _status; private String _message; private T _data;
    public ServerResponse() { _status = ""; _message = ""; }
    public ServerResponse(String status, String message, T data) {
        _status = status; _message = message; _data = data;
    }
    public static <T> ServerResponse<T> ok(String msg, T data) { return new ServerResponse<>("OK", msg, data); }
    public static <T> ServerResponse<T> error(String msg) { return new ServerResponse<>("ERROR", msg, null); }
    public String getStatus() { return _status; }
    public String getMessage() { return _message; }
    public T getData() { return _data; }
    public boolean isOk() { return "OK".equals(_status); }
}

class ClientRequest {
    private String _requestType = "";
    private Map<String, Object> _payload = new HashMap<>();
    public String getRequestType() { return _requestType; }
    public void setRequestType(String t) { _requestType = t; }
    public Map<String, Object> getPayload() { return _payload; }
    public void setPayload(Map<String, Object> p) { _payload = p; }
    public String getString(String key) { Object v = _payload.get(key); return v == null ? null : v.toString(); }
    public int getInt(String key) {
        Object v = _payload.get(key);
        if (v == null) return -1;
        try { return Integer.parseInt(v.toString()); } catch (NumberFormatException e) { return -1; }
    }
}

// ------- Repository -------

class ScoreRepository {
    private final List<ScoreEntry> _entries = new ArrayList<>();
    private int _nextId = 1;

    // Inserts: a new score entry and returns it with the generated ID
    public synchronized ScoreEntry insert(String playerId, String playerName, int score, String gameMode) {
        ScoreEntry entry = new ScoreEntry(_nextId++, playerId, playerName, score, gameMode);
        _entries.add(entry);
        return entry;
    }

    // Gets: all entries sorted by score descending
    public synchronized List<ScoreEntry> getAll() {
        return _entries.stream()
            .sorted(Comparator.comparingInt(ScoreEntry::getScore).reversed())
            .collect(Collectors.toList());
    }

    // Gets: the top N entries by score descending
    public synchronized List<ScoreEntry> getTop(int n) {
        return getAll().stream().limit(Math.max(0, n)).collect(Collectors.toList());
    }
}

// ------- Server -------

class LeaderboardServer {

    // === Fields ===
    private int _port;
    private ScoreRepository _repo;
    private ExecutorService _pool;

    // Creates: a leaderboard server on the given port
    public LeaderboardServer(int port, ScoreRepository repo) {
        _port = port;
        _repo = repo;
        _pool = Executors.newCachedThreadPool();
    }

    // Starts: the accept loop — handles each client on a pool thread
    public void start() throws Exception {
        try (ServerSocket ss = new ServerSocket(_port)) {
            while (true) {
                Socket client = ss.accept();
                _pool.submit(new ClientHandler(client, _repo));
            }
        }
    }

    // Handles: one client connection
    private static class ClientHandler implements Runnable {

        private Socket _socket;
        private ScoreRepository _repo;
        private ObjectMapper _mapper = new ObjectMapper();

        // Creates: a handler for the given socket and repository
        public ClientHandler(Socket socket, ScoreRepository repo) {
            _socket = socket;
            _repo = repo;
        }

        // Runs: reads JSON requests and writes JSON responses
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                 PrintWriter out = new PrintWriter(_socket.getOutputStream(), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    out.println(handle(line));
                }
            }
            catch (IOException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            }
            finally {
                try { _socket.close(); } catch (IOException ignored) { }
            }
        }

        // Handles: one raw JSON request string
        private String handle(String rawJson) {
            try {
                ClientRequest req = _mapper.readValue(rawJson, ClientRequest.class);
                return _mapper.writeValueAsString(dispatch(req));
            }
            catch (Exception e) {
                return "{\"status\":\"ERROR\",\"message\":\"" + e.getMessage() + "\",\"data\":null}";
            }
        }

        // Dispatches: the request to the correct repository method
        private ServerResponse<?> dispatch(ClientRequest req) {
            String type = req.getRequestType();

            if ("GET_ALL".equals(type)) {
                List<ScoreEntry> all = _repo.getAll();
                return ServerResponse.ok(all.size() + " scores", all);
            }

            if ("SUBMIT_SCORE".equals(type)) {
                String playerId = req.getString("playerId");
                String playerName = req.getString("playerName");
                int score = req.getInt("score");
                String gameMode = req.getString("gameMode");

                if (playerId == null || playerId.isBlank())
                    return ServerResponse.error("playerId is required");
                if (playerName == null || playerName.isBlank())
                    return ServerResponse.error("playerName is required");
                if (score < 0)
                    return ServerResponse.error("score must be >= 0");
                if (gameMode == null || gameMode.isBlank())
                    return ServerResponse.error("gameMode is required");

                ScoreEntry inserted = _repo.insert(playerId, playerName, score, gameMode);
                return ServerResponse.ok("score recorded", inserted);
            }

            if ("GET_TOP_N".equals(type)) {
                int n = req.getInt("n");
                if (n <= 0)
                    return ServerResponse.error("n must be >= 1");
                return ServerResponse.ok("top " + n, _repo.getTop(n));
            }

            return ServerResponse.error("unknown request type: " + type);
        }
    }
}
```

</details>

---

## Exercise 05 — Error handling, `DISCONNECT`, and protocol documentation

**Objective:** Harden the server with structured error responses for all failure cases, add
clean `DISCONNECT` handling, and write the protocol reference for the README.

**Context (software + games):**

- **Software dev:** Production APIs never expose raw exceptions to callers. Every failure path
  must return a structured, parseable error.
- **Games dev:** Game clients that receive unparseable text will crash or corrupt state.
  Consistent error envelopes are essential.

### What you are building

- `DISCONNECT` request type — server logs disconnection and closes the connection cleanly
- Validation errors returned as structured `ServerResponse.error(...)` not exceptions
- A written protocol reference committed to the README

### Tasks

1. Extend `ClientHandler.dispatch()` from Exercise 04 to handle `DISCONNECT`:
   - Return `ServerResponse.ok("goodbye", null)`.
   - In `ClientHandler.run()`: after sending the `DISCONNECT` response, break out of the read loop.
2. Add at least two additional validation cases to `SUBMIT_SCORE`:
- `score > 999_999` to `"score exceeds maximum"`
- `gameMode` not in `{"RANKED", "CASUAL", "PRACTICE"}` to `"invalid game mode: X"`
1. In `Exercise.run()`:
   - Start the server (port 9103).
   - Test `DISCONNECT`: client sends a score, receives a reply, then sends `DISCONNECT` and
     prints the goodbye response.
   - Test a bad request: send `SUBMIT_SCORE` with a missing `playerId` field; print the error
     response.
   - Test an invalid game mode: send `SUBMIT_SCORE` with `gameMode="TOURNAMENT"`; print the
     error response.
2. Write a `PROTOCOL.md` block (as a comment or separate file) documenting all five request
   types: `GET_ALL`, `SUBMIT_SCORE`, `GET_TOP_N`, `DISCONNECT`, and one error case.

### Sample output

```text
Score submitted: {"status":"OK",...}
Disconnect: {"status":"OK","message":"goodbye","data":null}
Missing playerId: {"status":"ERROR","message":"playerId is required","data":null}
Invalid mode: {"status":"ERROR","message":"invalid game mode: TOURNAMENT","data":null}
```

### Constraints

- After sending the `DISCONNECT` response, the server must close the socket cleanly.
- `DISCONNECT` must be handled — not fall through to the `"unknown request type"` error.
- The protocol document must include: request type, payload fields, and success response shape.

### Done when...

- The `DISCONNECT` flow completes without `null` or exceptions on either side.
- Both error cases return correctly structured `ServerResponse.error(...)` JSON.
- The protocol doc covers all five request types.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution (key changes
  only)</summary>

```java
package t22_networking.exercises.ex05;

// Key additions to ClientHandler.dispatch() from Exercise 04:

// --- DISCONNECT handler ---
// (in dispatch)
if ("DISCONNECT".equals(type)) {
    return ServerResponse.ok("goodbye", null);
}

// --- In run() loop, after out.println(handle(line)): ---
// if (line.contains("\"DISCONNECT\"")) break;

// --- Extended SUBMIT_SCORE validation (inside the SUBMIT_SCORE block) ---
// if (score > 999_999)
//     return ServerResponse.error("score exceeds maximum");
// Set<String> VALID_MODES = Set.of("RANKED", "CASUAL", "PRACTICE");
// if (!VALID_MODES.contains(gameMode.trim().toUpperCase()))
//     return ServerResponse.error("invalid game mode: " + gameMode);

// --- PROTOCOL.md ---
/*
# Leaderboard Server — Protocol Reference

Transport: TCP socket | Format: one JSON object per line (UTF-8) | Port: 9103

## Request envelope
{ "requestType": "<TYPE>", "payload": { ... } }

## Response envelope
{ "status": "OK|ERROR", "message": "<description>", "data": <payload or null> }

## Request types

| requestType | Payload fields | Success data |
|---------------|--------------------------------------------------------|------------------------|
| GET_ALL | (none) | Array of ScoreEntry |
| SUBMIT_SCORE | playerId, playerName, score (int), gameMode (string) | Inserted ScoreEntry |
| GET_TOP_N | n (int) | Array of ScoreEntry |
| DISCONNECT | (none) | null |

## Error responses
Any request that fails returns:
{ "status": "ERROR", "message": "<reason>", "data": null }
Always check status before using data.
*/
```

```java
// Exercise.run() tests:
public static void run() throws Exception {
    ScoreRepository repo = new ScoreRepository();
    Thread st = new Thread(() -> {
        try { new LeaderboardServer(9_103, repo).start(); }
        catch (Exception e) { e.printStackTrace(); }
    });
    st.setDaemon(true);
    st.start();
    Thread.sleep(300);

    // Test DISCONNECT
    try (Socket s = new Socket("localhost", 9_103);
         BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
         PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

        out.println("{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P001\",\"playerName\":\"Alice\",\"score\":4200,\"gameMode\":\"ranked\"}}");
        System.out.println("Score submitted: " + in.readLine());

        out.println("{\"requestType\":\"DISCONNECT\",\"payload\":{}}");
        System.out.println("Disconnect: " + in.readLine());
    }

    // Test missing playerId
    try (Socket s = new Socket("localhost", 9_103);
         BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
         PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

        out.println("{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerName\":\"Bob\",\"score\":1000,\"gameMode\":\"ranked\"}}");
        System.out.println("Missing playerId: " + in.readLine());
    }

    // Test invalid game mode
    try (Socket s = new Socket("localhost", 9_103);
         BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
         PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

        out.println("{\"requestType\":\"SUBMIT_SCORE\",\"payload\":{\"playerId\":\"P003\",\"playerName\":\"Carol\",\"score\":2000,\"gameMode\":\"TOURNAMENT\"}}");
        System.out.println("Invalid mode: " + in.readLine());
    }
}
```

</details>

---

## Lesson Context

```yaml
linked_lesson:
  topic_code: "t22_networking"
  lesson_path: "/notes/topics/t22_networking/t22_networking_notes.md"
  primary_domain_emphasis: "Balanced"
  difficulty_tier: "Foundation → Intermediate"
```
