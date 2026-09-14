---
title: "Applied Case Study: TaskHub — Index"
module: "COMP C8Z03 Object-Oriented Programming"
version: 1.0
---

# Applied Case Study: TaskHub

## What Is This?

TaskHub is a **longitudinal applied case study** that runs alongside the core teaching topics
in Year 2 OOP. It demonstrates how individual programming concepts combine into a realistic
multi-tier system.

This document serves as an index to all parts of the case study and provides guidance on how to
use it.

The complete TaskHub implementation (all layers, tests, Docker configuration) is maintained in
a separate repository and is available to students
[the Applied Case Study repository](https://github.com/nmcguinness/L8-OOP-Module-Content-Applied-Case-Study.git).

---

## How to Use the Case Study

1. **Learn the concept first.** The core topic notes (e.g., `t08_generics_1_notes.md`) teach
   the programming concept in isolation with small, focused examples.

2. **Then see it applied.** Each case study part shows how that concept addresses a real design
   pressure in TaskHub.

3. **The case study is optional but recommended.** You can understand the core topics without
   reading the case study, but the case study helps you see *why* the concepts matter.

4. **Don't read ahead.** Each part deliberately avoids concepts from future topics. Reading
   Part 3 before you've studied concurrency will be confusing.

5. **Each part is self-contained.** Parts can reference earlier parts but must not assume
   students have read later parts.

6. **Code is illustrative, not exhaustive.** The complete reference implementation is separate.

---

## The Parts

| Part | Topic | Status |
|:----|:-----|:--------|
| 1 | [Generics](t11_generics_1/applied_case_study_taskhub_generics.md) (t11) | ✅ Complete |
| 2 | Design Patterns (t13/t14) | ⏳ Planned |
| 3 | Concurrency (t19) | ⏳ Planned |
| 4 | Database Connectivity (t15) | ⏳ Planned |
| 5 | Server-Side Java (t22) | ⏳ Planned |
| 6 | Dockerisation | ⏳ Planned |
| 7 | Integration | ⏳ Planned |

> Planned parts are listed without links until the note exists. Each will live in
> a folder named after its topic code, matching `notes/topics/`.

---

## The System at Each Stage

### After Part 1 — Generics

- **TaskHub**
  - domain/
    - Task.java (implements Validatable)
    - User.java (implements Validatable)
    - TaskStatus.java
    - Priority.java
  - core/
    - Result.java
    - Validatable.java
  - repository/
    - Repository.java
    - InMemoryRepository.java
  - validation/
    - Validator.java
    - TaskValidator.java
  - service/
    - TaskService.java

**Capabilities:** Type-safe storage and retrieval. Explicit error handling. Reusable validation.

### After Part 2 — Design Patterns

- **TaskHub (adds)**
  - command/
    - TaskCommand.java
    - CreateTaskCommand.java
    - AssignTaskCommand.java
    - CompleteTaskCommand.java
  - strategy/
    - TaskOrderStrategy.java
    - [implementations]
  - observer/
    - TaskEventListener.java
    - TaskEventPublisher.java
  - factory/
    - RepositoryFactory.java

**Capabilities:** Pluggable ordering. Encapsulated operations. Decoupled notifications.

### After Part 3 — Concurrency

- **TaskHub (adds)**
  - concurrent/
    - TaskQueue.java (thread-safe)
    - TaskSnapshot.java (immutable DTO)
    - TaskWorker.java (Runnable)
    - TaskExecutorService.java (thread pool)

**Capabilities:** Safe concurrent access. Parallel task processing. Immutable data transfer.

### After Part 4 — Database Connectivity

- **TaskHub (adds)**
  - persistence/
    - DatabaseConfig.java
    - JdbcTaskRepository.java
    - JdbcUserRepository.java
    - schema.sql

**Capabilities:** Persistent storage. SQL-free service layer. Swappable repository implementations.

### After Part 5 — Server-Side Java

- **TaskHub (adds)**
  - server/
    - TaskHubServer.java
    - ClientHandler.java (Runnable)
    - TaskHubProtocol.java
    - RequestRouter.java
  - client/
    - TaskHubConsoleClient.java

**Capabilities:** Network access. Multiple simultaneous clients. Protocol-based communication.

### After Part 6 — Dockerisation

- **TaskHub (adds)**
  - docker/
    - Dockerfile
    - docker-compose.yml
    - .env.example
  - config/
    - AppConfig.java

**Capabilities:** Reproducible deployment. Environment-based configuration. One-command startup.

### After Part 7 — Integration

- **TaskHub (complete)**
  - client-fx/
    - TaskHubApp.java
    - TaskListController.java
    - ServerConnection.java
    - TaskHubClient.java
  - [all previous components]

**Capabilities:** Full 3-tier architecture. GUI client. Complete end-to-end workflow.
