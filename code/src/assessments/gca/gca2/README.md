# GCA2 — N-Tier Reference System

A complete working example of the N-tier architecture required for GCA2.
The domain is a simple **Task** management system with one database table.
Use it as a reference for structuring your own project, understanding how
the layers connect, and writing unit tests for each tier.

---

## Package structure

| Package | Classes | Responsibility |
| :- | :- | :- |
| `domain` | `Task` | Entity / DTO — validated fields, `equals`, `hashCode` |
| `dao` | `GenericDAOInterface`, `TaskDAO` | Database CRUD via JDBC `PreparedStatement` |
| `db` | `DatabaseConnection` | JDBC connection helper — `open()` / `close()` |
| `service` | `ClientDispatcher` | Routes parsed requests to the correct DAO method |
| `server` | `TaskServer`, `ClientHandler`, `ClientRequest`, `ServerResponse<T>` | TCP server, per-client thread, JSON protocol types |
| `client` | `TaskClient` | Demo client — exercises all four operations |
| `sql` | `mysqlSetup.sql` | Recreates schema and seeds five rows |

---

## Setup

1. Run `sql/mysqlSetup.sql` against a local MySQL instance to create
   `gca2_support_db`, the `gca2_user` account, and the `tasks` table.
2. If you change the password, update the `DB_PASS` constant in `TaskServer`.

---

## Running

1. Run `TaskServer.main()` — the server blocks, waiting for connections on port **9 300**.
2. Run `TaskClient.main()` — the client connects, sends four requests, prints responses, disconnects.

---

## Architecture diagram

```mermaid
flowchart TD
    subgraph CLIENT ["client"]
        TC["TaskClient"]
    end

    subgraph SERVER ["server"]
        direction TB
        TS["TaskServer\nServerSocket · ExecutorService"]
        CH["ClientHandler\nImplements Runnable"]
        CR["ClientRequest\nparses requestType + payload"]
        SR["ServerResponse&lt;T&gt;\nok() · error()"]
        TS -->|"submit per connection"| CH
        CH --- CR
        CH --- SR
    end

    subgraph SERVICE ["service"]
        CD["ClientDispatcher\ndispatch(ClientRequest)"]
    end

    subgraph DAO ["dao"]
        GI["GenericDAOInterface&lt;T,K&gt;\n(interface)"]
        TD["TaskDAO\n(implements)"]
        GI -.->|implements| TD
    end

    subgraph DB ["db"]
        DC["DatabaseConnection\nopen() · close(Connection)"]
    end

    subgraph DOMAIN ["domain"]
        T["Task\ntask_id · title · description · completed"]
    end

    subgraph DATABASE ["Database  (MySQL)"]
        TBL[(tasks)]
    end

    TC      <-->|"JSON · TCP port 9300"| TS
    CH      -->|"dispatch(request)"| CD
    CD      -->|"insert / findById\nfindAll / deleteById"| TD
    TD      -->|"open() · close()"| DC
    DC      -->|"JDBC · PreparedStatement"| TBL
    TD      -.->|"maps rows to/from"| T
```

---

## Sequence diagram — INSERT request

```mermaid
sequenceDiagram
    participant TaskClient
    participant TaskServer
    participant ClientHandler
    participant ClientDispatcher
    participant TaskDAO
    participant Database

    TaskClient->>TaskServer: connect TCP port 9300
    TaskServer->>ClientHandler: new ClientHandler(socket) via ExecutorService

    TaskClient->>ClientHandler: JSON {"requestType":"INSERT","payload":{title,description,completed}}
    ClientHandler->>ClientDispatcher: dispatch(clientRequest)
    ClientDispatcher->>TaskDAO: insert(task)
    TaskDAO->>Database: INSERT INTO tasks ... [PreparedStatement]
    Database-->>TaskDAO: generated task_id = 6
    TaskDAO-->>ClientDispatcher: Task with task_id = 6
    ClientDispatcher-->>ClientHandler: ServerResponse.ok("Task inserted with id 6", task)
    ClientHandler-->>TaskClient: JSON {"status":"OK","message":"...","data":{...task...}}

    TaskClient->>ClientHandler: JSON {"requestType":"DISCONNECT","payload":{}}
    Note over ClientHandler: socket closed, thread released
```

---

## ER diagram

```mermaid
erDiagram
    tasks {
        int     task_id     PK
        string  title
        string  description
        boolean completed
    }
```

---

## JSON protocol reference

All requests follow the same envelope:

```json
{ "requestType": "<TYPE>", "payload": { ... } }
```

All responses follow the same envelope:

```json
{ "status": "OK|ERROR", "message": "...", "data": <payload or null> }
```

| Request type | Payload fields | Success `data` |
| :- | :- | :- |
| `INSERT` | `title` (String), `description` (String), `completed` (boolean) | Inserted `Task` with generated `task_id` |
| `FIND_BY_ID` | `taskId` (int) | Matching `Task`, or `ERROR` if not found |
| `LIST` | _(none)_ | `List<Task>` — empty array if table is empty |
| `DELETE_BY_ID` | `taskId` (int) | `true`, or `ERROR` if not found |
| `DISCONNECT` | _(none)_ | _(no response — server closes socket)_ |
