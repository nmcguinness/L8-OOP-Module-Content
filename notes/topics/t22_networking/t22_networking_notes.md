---
title: "Networking I — TCP Sockets and the Client-Server Protocol"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t22_networking
description: "How to build a multi-client TCP server in Java: ServerSocket, client handlers, a JSON-over-sockets protocol, and a generic ServerResponse<T> wrapper."
created: 2026-02-20
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, networking, sockets, tcp, client-server, json, protocol, executorservice, year2, comp-c8z03]
difficulty_tier: Intermediate
mlos: [MLO4]
previous_topic: t21_json_2_jackson_advanced
prerequisites:
  - Interfaces and generics
  - DB Connectivity / DAO
  - Concurrency I (threads, ExecutorService)
  - JSON serialisation (Jackson ObjectMapper)
---

# Networking I — TCP Sockets and the Client-Server Protocol

> **Prerequisites:**
> - You understand interfaces, generics, and lambda expressions
> - You have a working DAO layer (see [DAO](../t15_dao/t15_dao_notes.md))
> - You understand `ExecutorService` and thread pools (see
>   [Concurrency](../t19_concurrency/t19_concurrency_notes.md))
> - You can serialise and deserialise Java objects to/from JSON

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Key terms](#key-terms)
- [Part 1: A minimal echo server](#part-1-a-minimal-echo-server)
- [Part 2: Multi-client server with
  ExecutorService](#part-2-multi-client-server-with-executorservice)
- [Part 3: Designing a JSON protocol](#part-3-designing-a-json-protocol)
- [Part 4: A full JSON protocol server](#part-4-a-full-json-protocol-server)
- [Part 5: Protocol documentation](#part-5-protocol-documentation)
- [Common mistakes](#common-mistakes)
- [Practice tasks](#practice-tasks)
- [Reflective questions](#reflective-questions)
- [Further reading](#further-reading)

---

## What you'll learn

| Skill Type | You will be able to... |
| :- | :- |
| Understand | Explain what TCP is and why sockets are a reliable transport for a client-server protocol. |
| Understand | Describe the roles of `ServerSocket` and `Socket` in a Java server-client pair. |
| Apply | Write a single-threaded echo server and matching client as a baseline. |
| Apply | Extend to a multi-client server using `ExecutorService` and a `ClientHandler` `Runnable`. |
| Apply | Design and implement a JSON-based request/response protocol. |
| Apply | Implement a generic `ServerResponse<T>` wrapper for all server replies. |
| Apply | Add structured error handling so exceptions never reach the client as raw stack traces. |
| Analyse | Document a protocol: request types, payload fields, and response shape. |

---

## Why this matters

In GCA2 Stage 2, your DAO layer moves to a server. Clients connect over a network, send JSON
requests (insert task, get by ID, filter, etc.), and receive JSON responses. The server handles
multiple clients simultaneously using the `ExecutorService` you learned in
[Concurrency](../t19_concurrency/t19_concurrency_notes.md).

This is a realistic multi-tier architecture: clients know nothing about SQL, and the server
knows nothing about the client's UI. They communicate only via a documented protocol.

---

## How this builds on previous content

| Previous concept | How it reappears here |
| :- | :- |
| Interface + implementation | `ServerSocket` / `Socket` give you streams; you wrap them in familiar `BufferedReader` / `PrintWriter` |
| `ExecutorService` | The server submits each accepted connection as a `Runnable` task to the pool |
| DAO layer | `ClientHandler` calls your DAO — the persistence layer is unchanged |
| Generics (`ServerResponse<T>`) | The generic response wrapper uses the same generic syntax as your DAO return types |
| JSON serialisation | Every request and response travels as a JSON string over the socket |

---

## Key terms

### TCP

**TCP (Transmission Control Protocol)** is a connection-oriented protocol that guarantees
delivery of data in the correct order. Once a connection is established, reading and writing
feel like reading and writing a stream.

### Socket

A **socket** is one endpoint of a two-way connection. A server socket listens for incoming
connections; client sockets make connections. Once connected, both ends have a socket they can
read from and write to.

### `ServerSocket`

Java's `ServerSocket` binds to a port and calls `accept()` to block until a client connects.
`accept()` returns a `Socket` representing that specific client connection.

### `Socket`

Java's `Socket` represents an established connection. You get an `InputStream` (read from the
client) and an `OutputStream` (write to the client) from it.

### Protocol

A **protocol** is an agreed set of rules: what messages look like, what they mean, and what the
expected response is. Your GCA2 protocol uses JSON strings sent as single lines, one message
per `println`.

### `ServerResponse<T>`

A generic wrapper that standardises every response from the server. It carries a status (`"OK"`
or `"ERROR"`), a human-readable message, and an optional data payload of type `T`.

---

## Part 1: A minimal echo server

Before adding your DAO or JSON protocol, get familiar with the socket primitives.

### The server

```java
import java.io.*;
import java.net.*;

public class EchoServer {

    // === Fields ===
    private int _port;

    // === Constructors ===
    // Creates: an echo server that will listen on the given port
    public EchoServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024–65535");
        _port = port;
    }

    // === Public API ===
    // Starts: the server — blocks waiting for one client, echoes each line, then exits
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(_port)) {
            System.out.println("Echo server listening on port " + _port);

            Socket clientSocket = serverSocket.accept();  // blocks until a client connects
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Received: " + line);
                    out.println("ECHO: " + line);  // send reply
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoServer(9_000).start();
    }
}
```

### The client

```java
import java.io.*;
import java.net.*;

public class EchoClient {

    // === Fields ===
    private String _host;
    private int _port;

    // === Constructors ===
    // Creates: a client that will connect to the given host and port
    public EchoClient(String host, int port) {
        if (host == null || host.isBlank())
            throw new IllegalArgumentException("host is required");
        _host = host;
        _port = port;
    }

    // === Public API ===
    // Connects: sends a test message and prints the reply
    public void run() throws IOException {
        try (Socket socket = new Socket(_host, _port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("hello server");      // send
            String reply = in.readLine();     // receive (blocks until a line arrives)
            System.out.println("Reply: " + reply);
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoClient("localhost", 9_000).run();
    }
}
```

Key points:
- `new PrintWriter(..., true)` — the `true` is **auto-flush**: the buffer is flushed after
  every `println`. Without this, data can sit in the buffer and never reach the other end.
- `readLine()` returns `null` when the connection is closed.
- Both sides use `try-with-resources` to close the socket cleanly.

---

## Part 2: Multi-client server with ExecutorService

The echo server above handles one client, then exits. A real server loops on `accept()` and
delegates each connection to a thread pool.

```java
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiClientServer {

    // === Fields ===
    private int _port;
    private ExecutorService _pool;

    // === Constructors ===
    // Creates: a multi-client server using a cached thread pool
    public MultiClientServer(int port) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024–65535");
        _port = port;
        _pool = Executors.newCachedThreadPool();
    }

    // === Public API ===
    // Starts: the server accept loop — runs until the process is stopped
    public void start() throws IOException {
        System.out.println("Server starting on port " + _port);

        try (ServerSocket serverSocket = new ServerSocket(_port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();    // block until a client arrives
                System.out.println("Accepted: " + clientSocket.getInetAddress());
                _pool.submit(new ClientHandler(clientSocket)); // hand off to pool
            }
        }
    }

    // === Helpers ===
    // Handles: one connected client — runs on a pool thread
    private static class ClientHandler implements Runnable {

        private Socket _socket;

        // Creates: a handler for the given socket
        public ClientHandler(Socket socket) {
            _socket = socket;
        }

        // Runs: the client session — reads lines and echoes them
        @Override
        public void run() {
            System.out.println("Handling client on " + Thread.currentThread().getName());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                 PrintWriter out = new PrintWriter(_socket.getOutputStream(), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    out.println("ECHO: " + line);
                }
            }
            catch (IOException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            }
            finally {
                try {
                    _socket.close();
                }
                catch (IOException e) {
                    // nothing useful to do here
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new MultiClientServer(9_000).start();
    }
}
```

Notice:
- The `while (true)` accept loop never blocks waiting for the current client to finish, because
  each client is submitted to the pool.
- The `finally` block closes the socket even if the handler throws an exception.
- Exceptions in the handler are caught and logged — they do not crash the server or the pool thread.

---

## Part 3: Designing a JSON protocol

### Why JSON over raw strings?

Raw strings are fragile: you need to parse tokens manually and the format is hard to extend.
JSON gives you a self-describing structure that maps directly to your Java DTOs.

### Protocol rule: one JSON object per line

Each request and each response is **one JSON string** sent as a single line (terminated by
`\n`). `PrintWriter.println()` adds the newline; `BufferedReader.readLine()` removes it.

Never send multi-line JSON over a socket unless you define explicit length framing — that is
significantly more complex.

---

### Request structure

```json
{
  "requestType": "GET_ALL",
  "payload": {}
}
```

```json
{
  "requestType": "GET_BY_ID",
  "payload": { "id": 42 }
}
```

```json
{
  "requestType": "INSERT",
  "payload": { "title": "Buy milk", "status": "TODO" }
}
```

```json
{
  "requestType": "DISCONNECT",
  "payload": {}
}
```

Use an enum for `requestType` in your Java code to avoid magic strings:

```java
public enum RequestType {
    GET_ALL,
    GET_BY_ID,
    INSERT,
    UPDATE,
    DELETE,
    FILTER,
    DISCONNECT
}
```

---

### `ServerResponse<T>` — the response wrapper

Every server reply uses the same envelope:

```java
import java.util.Optional;

public class ServerResponse<T> {

    // === Fields ===
    private String _status;    // "OK" or "ERROR"
    private String _message;
    private T _data;

    // === Constructors ===
    // Creates: a response with all fields set explicitly
    public ServerResponse(String status, String message, T data) {
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");
        _status = status;
        _message = message;
        _data = data;
    }

    // === Public API ===
    // Gets: the status string — "OK" or "ERROR"
    public String getStatus() {
        return _status;
    }

    // Gets: the human-readable message
    public String getMessage() {
        return _message;
    }

    // Gets: the data payload, which may be null for error responses
    public T getData() {
        return _data;
    }

    // Creates: a successful response carrying data
    public static <T> ServerResponse<T> ok(String message, T data) {
        return new ServerResponse<>("OK", message, data);
    }

    // Creates: an error response with no data
    public static <T> ServerResponse<T> error(String message) {
        return new ServerResponse<>("ERROR", message, null);
    }

    // Checks: whether this response represents a successful operation
    public boolean isOk() {
        return "OK".equals(_status);
    }
}
```

Usage examples:

```java
ServerResponse<List<Task>> ok = ServerResponse.ok("retrieved 3 tasks", tasks);
ServerResponse<Task> ok2 = ServerResponse.ok("task inserted", newTask);
ServerResponse<Void> error = ServerResponse.error("task not found");
```

On the wire as JSON (using Jackson):

```json
{ "status": "OK", "message": "retrieved 3 tasks", "data": [ ... ] }
{ "status": "ERROR", "message": "task not found", "data": null }
```

---

## Part 4: A full JSON protocol server

### Project structure

```text
src/
  server/
    Server.java              → accept loop + pool
    ClientHandler.java       → handles one client session
    RequestDispatcher.java   → routes request type to DAO method
  shared/
    RequestType.java         → enum of request types
    ClientRequest.java       → DTO for incoming requests
    ServerResponse.java      → generic response wrapper
  dao/
    TaskDao.java             → interface
    JdbcTaskDao.java         → JDBC implementation
  domain/
    Task.java                → entity DTO
```

---

### `ClientRequest`

```java
import java.util.Map;

public class ClientRequest {

    // === Fields ===
    private String _requestType;
    private Map<String, Object> _payload;

    // === Constructors ===
    // Creates: an empty ClientRequest — needed by Jackson deserialisation
    public ClientRequest() {
        _requestType = "";
        _payload = Map.of();
    }

    // === Public API ===
    // Gets: the request type string (e.g. "GET_ALL")
    public String getRequestType() {
        return _requestType;
    }

    // Sets: the request type
    public void setRequestType(String requestType) {
        _requestType = requestType;
    }

    // Gets: the payload map
    public Map<String, Object> getPayload() {
        return _payload;
    }

    // Sets: the payload map
    public void setPayload(Map<String, Object> payload) {
        _payload = payload;
    }

    // Gets: a string value from the payload by key, or null if absent
    public String getString(String key) {
        Object v = _payload.get(key);
        return v == null ? null : v.toString();
    }

    // Gets: an integer value from the payload by key, or -1 if absent/unparseable
    public int getInt(String key) {
        Object v = _payload.get(key);
        if (v == null)
            return -1;
        try {
            return Integer.parseInt(v.toString());
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
}
```

---

### `ClientHandler` — full version

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Optional;

public class ClientHandler implements Runnable {

    // === Fields ===
    private Socket _socket;
    private TaskDao _dao;
    private ObjectMapper _mapper;

    // === Constructors ===
    // Creates: a handler for the given socket, using the provided DAO
    public ClientHandler(Socket socket, TaskDao dao) {
        if (socket == null)
            throw new IllegalArgumentException("socket is required");
        if (dao == null)
            throw new IllegalArgumentException("dao is required");

        _socket = socket;
        _dao = dao;
        _mapper = new ObjectMapper();
    }

    // === Public API ===
    // Runs: the client session — reads JSON requests and writes JSON responses
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
             PrintWriter out = new PrintWriter(_socket.getOutputStream(), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                String response = handle(line);
                out.println(response);
            }
        }
        catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        }
        finally {
            try {
                _socket.close();
            }
            catch (IOException ignored) {
            }
        }
    }

    // === Helpers ===
    // Handles: a single raw JSON request line — returns a JSON response string
    private String handle(String rawJson) {
        try {
            ClientRequest req = _mapper.readValue(rawJson, ClientRequest.class);
            ServerResponse<?> response = dispatch(req);
            return _mapper.writeValueAsString(response);
        }
        catch (Exception e) {
            return toErrorJson("malformed request: " + e.getMessage());
        }
    }

    // Dispatches: the request to the correct DAO method based on requestType
    private ServerResponse<?> dispatch(ClientRequest req) throws Exception {
        String type = req.getRequestType();

        if ("GET_ALL".equals(type)) {
            List<Task> tasks = _dao.findAll();
            return ServerResponse.ok("retrieved " + tasks.size() + " tasks", tasks);
        }

        if ("GET_BY_ID".equals(type)) {
            int id = req.getInt("id");
            Optional<Task> task = _dao.findById(id);
            if (task.isEmpty())
                return ServerResponse.error("no task with id=" + id);
            return ServerResponse.ok("task found", task.get());
        }

        if ("INSERT".equals(type)) {
            String title = req.getString("title");
            String status = req.getString("status");
            int newId = _dao.insert(title, status);
            Optional<Task> created = _dao.findById(newId);
            return created.isPresent()
                ? ServerResponse.ok("task created", created.get())
                : ServerResponse.error("insert succeeded but task not found");
        }

        if ("DELETE".equals(type)) {
            int id = req.getInt("id");
            boolean deleted = _dao.deleteById(id);
            if (!deleted)
                return ServerResponse.error("no task with id=" + id);
            return ServerResponse.ok("task deleted", null);
        }

        if ("DISCONNECT".equals(type)) {
            return ServerResponse.ok("goodbye", null);
        }

        return ServerResponse.error("unknown request type: " + type);
    }

    // Converts: an error message to a raw JSON error response string (fallback)
    private String toErrorJson(String message) {
        return "{\"status\":\"ERROR\",\"message\":\"" + message + "\",\"data\":null}";
    }
}
```

---

### `Server` — accept loop

```java
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {

    // === Fields ===
    private int _port;
    private TaskDao _dao;
    private ExecutorService _pool;

    // === Constructors ===
    // Creates: a server on the given port using the provided DAO
    public Server(int port, TaskDao dao) {
        if (port < 1_024 || port > 65_535)
            throw new IllegalArgumentException("port must be 1024–65535");
        if (dao == null)
            throw new IllegalArgumentException("dao is required");

        _port = port;
        _dao = dao;
        _pool = Executors.newCachedThreadPool();
    }

    // === Public API ===
    // Starts: the accept loop — runs indefinitely until interrupted
    public void start() throws IOException {
        System.out.println("Server listening on port " + _port);

        try (ServerSocket serverSocket = new ServerSocket(_port)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client.getInetAddress());
                _pool.submit(new ClientHandler(client, _dao));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/taskhub?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "taskhub_user";
        String pass = "your_password";

        TaskDao dao = new JdbcTaskDao(url, user, pass);
        new Server(9_000, dao).start();
    }
}
```

---

### Client — sending JSON requests

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class TaskClient {

    // === Fields ===
    private String _host;
    private int _port;
    private ObjectMapper _mapper;

    // === Constructors ===
    // Creates: a client that connects to the given host and port
    public TaskClient(String host, int port) {
        if (host == null || host.isBlank())
            throw new IllegalArgumentException("host is required");
        _host = host;
        _port = port;
        _mapper = new ObjectMapper();
    }

    // === Public API ===
    // Runs: a demo session — sends a GET_ALL request and prints the response
    public void run() throws Exception {
        try (Socket socket = new Socket(_host, _port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // --- GET_ALL ---
            Map<String, Object> request = new HashMap<>();
            request.put("requestType", "GET_ALL");
            request.put("payload", Map.of());

            out.println(_mapper.writeValueAsString(request));

            String reply = in.readLine();
            System.out.println("GET_ALL response: " + reply);

            // --- DISCONNECT ---
            request.clear();
            request.put("requestType", "DISCONNECT");
            request.put("payload", Map.of());
            out.println(_mapper.writeValueAsString(request));
        }
    }

    public static void main(String[] args) throws Exception {
        new TaskClient("localhost", 9_000).run();
    }
}
```

---

## Part 5: Protocol documentation

GCA2 requires a protocol document committed to your README. Here is a template:

---

### Protocol Reference

**Transport:** TCP socket
**Format:** One JSON object per line (UTF-8). Each message is terminated by `\n`.
**Port:** 9000 (default)

#### Request envelope

```json
{ "requestType": "<TYPE>", "payload": { ... } }
```

#### Response envelope

```json
{ "status": "OK|ERROR", "message": "<description>", "data": <payload or null> }
```

#### Supported request types

| requestType | Payload fields | Success response data |
| :- | :- | :- |
| `GET_ALL` | _(none)_ | Array of task objects |
| `GET_BY_ID` | `id: int` | Single task object |
| `INSERT` | `title: string`, `status: string` | Inserted task object with generated ID |
| `UPDATE` | `id: int`, `title: string`, `status: string` | Updated task object |
| `DELETE` | `id: int` | `null` |
| `FILTER` | `status: string` | Array of matching task objects |
| `DISCONNECT` | _(none)_ | `null` |

#### Error responses

Any request that fails (unknown type, missing field, DB error, validation failure) returns:

```json
{ "status": "ERROR", "message": "<reason>", "data": null }
```

Clients must always check the `status` field before using `data`.

---

## Common mistakes

| Mistake | What happens | Fix |
| :- | :- | :- |
| `new PrintWriter(out)` without `true` | Data sits in the buffer and is never sent | Always use `new PrintWriter(out, true)` (auto-flush) |
| Sending multi-line JSON | `readLine()` only reads one line — partial JSON is received | Serialise to a single-line string before sending |
| Not closing the socket in `finally` | File descriptor leaks; server eventually runs out of sockets | Always close in `finally` or use try-with-resources |
| Propagating exceptions as stack traces to the client | Client receives unparseable text | Catch all exceptions in the handler; return a `ServerResponse.error(...)` |
| Creating `ObjectMapper` inside `readLine()` loop | New mapper instance on every message — expensive | Create one mapper per handler in the constructor |
| Forgetting `DISCONNECT` handling | Socket stays open after client exits | Always handle `DISCONNECT` and log it cleanly |

---

## Practice tasks

1. Extend the server to handle `UPDATE` and `FILTER` requests.
2. Add input validation in `ClientHandler.dispatch()`: if a required payload field is missing,
   return a structured error instead of throwing.
3. Write a `TaskClient` menu that lets a user choose an operation (GET_ALL, INSERT, DELETE)
   from the console.
4. Test with two client processes open simultaneously. Verify both get correct independent
   responses.
5. Add a request log to the server: print `[THREAD] requestType` for every request received.

---

## Reflective questions

1. Why does `PrintWriter` need `auto-flush = true` when writing to a socket?
2. What is the difference between the server's accept loop and the work done in `ClientHandler`?
3. Why should exceptions in `ClientHandler.run()` never propagate out of the `try/catch`?
4. If you added a second entity (e.g. `User`), how would you extend the protocol without
   breaking existing clients?
5. What is the purpose of `DISCONNECT` when TCP would close the connection anyway when the
   process exits?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**1. Why `PrintWriter` needs auto-flush on a socket.**
`PrintWriter` buffers output for efficiency and only pushes it to the socket when the buffer
fills, is flushed, or is closed. On a request/response protocol that produces **deadlock**: the
server writes its reply into the buffer and blocks on `readLine()` waiting for the next
request, while the client blocks on `readLine()` waiting for a reply that is still sitting in
the server's buffer. Neither side can proceed, and it looks like a hang with no exception.

`new PrintWriter(socket.getOutputStream(), true)` flushes on every `println`, which is exactly
the granularity a line-delimited protocol needs. The equivalent is calling `out.flush()` after
each write — auto-flush just means you cannot forget. Note the `true` only auto-flushes on
`println`/`printf`, not on `print`, so terminate messages with `println`.

**2. Accept loop vs `ClientHandler`.**
Different jobs on different threads. The **accept loop** is the receptionist:
`serverSocket.accept()` blocks until a client connects, returns a `Socket` for that one client,
hands it to the pool, and loops immediately to wait for the next. It does no protocol work at
all.

**`ClientHandler`** is the conversation: it owns one socket for that client's whole session,
reading requests, dispatching to the DAO and writing responses until the client leaves.

The separation is what makes the server multi-client. Do the handling inside the accept loop
and the server serves exactly one client at a time — everyone else waits at `accept()` until
the first disconnects. The loop must stay short and non-blocking so it can get back to
accepting.

**3. Why exceptions must not escape `ClientHandler.run()`.**
Because there is nowhere useful for them to go. `run()` is the top of its thread: an exception
that escapes kills that thread, and with `submit()` it is captured silently in a `Future`
nobody reads, so the client's connection dies with **no error message on either side** — the
client just hangs or sees an abrupt close, and the server log shows nothing.

It also leaks the socket, since cleanup after the throw point never runs. One misbehaving
client (a malformed request, a missing JSON field) should degrade to _one error response_,
never to a dead thread or a destabilised server. Catch inside `run()`, log with the exception
object, attempt to send `ServerResponse.error(...)`, and close the socket in a `finally` or via
try-with-resources.

**4. Adding a `User` entity without breaking existing clients.**
Add, never change. Introduce new request types (`GET_USER_BY_ID`, `INSERT_USER`) and register
new handlers; every existing type keeps its name, payload shape and response shape, so deployed
clients are unaffected and simply never send the new ones. The `DaoRegistry` from the
supplement is what makes this a one-line change rather than a new `ClientHandler` constructor
parameter.

The rules that keep this safe are worth stating: **adding** a field to a response is
backward-compatible (old clients ignore unknown keys, provided Jackson is not configured to
fail on them); **renaming, removing or retyping** a field is not; and an unknown request type
must return a structured error rather than crashing. If a breaking change becomes unavoidable,
version the protocol — a `version` field in the envelope, or `GET_USER_V2` — rather than
mutating an existing type in place.

**5. Purpose of `DISCONNECT` when TCP closes anyway.**
It is the difference between hanging up and the line going dead. `DISCONNECT` is a **graceful,
in-band** goodbye: the server learns the session is over at a known point, can finish any
in-flight work, flush and commit, log a clean end, remove the client from any registry, close
the socket and return its pool thread promptly.

Relying on the process exiting instead means the server discovers the loss only when a read
returns `-1`/`null` or throws — and that can take a long time if the client's machine crashed
or the network dropped, because the OS has no packet telling it so. Until then the handler
thread sits blocked and its resources are pinned; a server that leaks a thread per abandoned
client eventually exhausts its pool. A deliberate `DISCONNECT` also lets you distinguish
"client left normally" from "connection failed", which matters both for logs and for deciding
whether to roll back partial work.

</details>

---

## Further reading

- Oracle Docs — All About Sockets
  <https://docs.oracle.com/javase/tutorial/networking/sockets/>
- Baeldung — Java `ServerSocket`
  <https://www.baeldung.com/a-guide-to-java-sockets>
- Jackson `ObjectMapper` usage
  <https://www.baeldung.com/jackson-object-mapper-tutorial>

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t21_json_2_jackson_advanced
  domain_emphasis: Balanced

this_lesson:
  topic_code: t22_networking
  primary_domain_emphasis: Balanced
  difficulty_tier: Intermediate
mlos: [MLO4]
```
