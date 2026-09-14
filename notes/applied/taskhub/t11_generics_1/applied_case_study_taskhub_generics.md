---
title: "Applied Case Study: TaskHub — Part 1: Generics"
topic: "Generics (Weeks 1–2)"
module: "COMP C8Z03 Object-Oriented Programming"
version: 1.0
---

# Applied Case Study: TaskHub — Part 1: Generics

## Overview

This is **Part 1** of the TaskHub Applied Case Study, corresponding to the **Generics** topic
(Weeks 1–2).

TaskHub is a collaborative task management system that we build incrementally throughout this
semester. Each part of this case study introduces a new design pressure and shows how the
corresponding programming concept addresses it.

In this part, we face the challenge of storing and retrieving different types of entities
(Tasks, Users, Projects) without duplicating code or sacrificing type safety. Generics allow us
to write reusable infrastructure that works with any entity type while letting the compiler
catch type errors before runtime.

We also introduce `Result<T>`, a generic type that represents operations that might fail,
replacing scattered exception handling with explicit, type-safe success-or-failure values.

**Prerequisites:** Before reading this case study, you should have completed the core topic
notes on Generics. This document assumes familiarity with type parameters, bounded types, and
wildcards.

**Runnable Code:** A complete, self-contained Java file demonstrating all concepts from this
part is available
[the Applied Case Study repository](https://github.com/nmcguinness/L8-OOP-Module-Content-Applied-Case-Study.git).

**What this document is:** A guided narrative showing how generics enable reusable, type-safe
infrastructure in a realistic system.

**What this document is not:** A tutorial on generics syntax (see the core topic notes) or a
complete implementation (see Appendix A for runnable code).

---

## Learning Outcomes

After studying this part of the case study, you should be able to:

|  Skill Type | You will be able to... |
|:---------|:-------------|
| Understand | Explain why generic repositories are preferable to type-specific or Object-based alternatives  |
| Apply | Implement a generic interface with multiple type parameters |
| Apply | Use `Function<T, R>` to extract identifiers from arbitrary entity types |
| Apply | Design a `Result<T>` type that represents success or failure explicitly |
| Analyse | Apply bounded type parameters to constrain generic validators |
| Analyse | Justify the use of `Optional<T>` for methods that might not find a result |
| Analyse | Evaluate the trade-offs between exception-based and Result-based error handling |

---

## The Domain

These entities remain stable throughout the TaskHub case study. All code in this document uses them.

```java
/**
 * The lifecycle states a Task can be in.
 *
 * <p>Tasks always start as {@code PENDING} and transition through states
 * based on user actions. The typical flow is:
 * {@code PENDING, IN_PROGRESS, COMPLETED}, though tasks can be
 * {@code CANCELLED} from any non-completed state.</p>
 */
public enum TaskStatus {
    /** Created but not yet started. */
    PENDING,

    /** Assigned and actively being worked on. */
    IN_PROGRESS,

    /** Successfully finished. */
    COMPLETED,

    /** Abandoned or no longer needed. */
    CANCELLED
}
```

```java
/**
 * How urgent or important a task is.
 *
 * <p>Used for sorting task lists and determining processing order.
 * The natural ordering is LOW, MEDIUM, HIGH, CRITICAL</p>
 */
public enum Priority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}
```

```java
/**
 * A unit of work in TaskHub.
 *
 * <p>Tasks are the core entity of the system. Each task has:</p>
 * <ul>
 *   <li>An immutable identifier ({@code id}) used for storage and lookup</li>
 *   <li>An immutable title and description describing the work</li>
 *   <li>Mutable status and priority that change over the task's lifetime</li>
 *   <li>An optional assignee (the user responsible for the task)</li>
 *   <li>Timestamps for auditing (created, due date)</li>
 * </ul>
 *
 * <p>Two tasks are considered equal if they have the same {@code id},
 * regardless of other field values. This supports updating tasks in
 * collections.</p>
 */
public final class Task implements Validatable {

    private final String id;
    private final String title;
    private final String description;
    private TaskStatus status;
    private Priority priority;
    private String assigneeId;      // null means unassigned
    private final long createdAt;
    private Long dueDate;           // null means no deadline

    /**
     * Creates a new task with the given properties.
     *
     * @param id          unique identifier (must not be null or blank)
     * @param title       short description of the work (must not be null or blank)
     * @param description detailed description (may be null)
     * @param priority    importance level (defaults to MEDIUM if null)
     * @throws IllegalArgumentException if id or title is null or blank
     */
    public Task(String id, String title, String description, Priority priority) {
        // Validate required fields
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Task id must not be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank");
        }

        this.id = id.trim();
        this.title = title.trim();
        this.description = (description != null) ? description.trim() : "";
        this.priority = (priority != null) ? priority : Priority.MEDIUM;
        this.status = TaskStatus.PENDING;  // all tasks start as pending
        this.createdAt = System.currentTimeMillis();
    }

    /**
     * Convenience constructor for quick testing.
     * Creates a task with an empty description.
     *
     * @param id       unique identifier
     * @param title    short description
     * @param priority importance level
     */
    public Task(String id, String title, Priority priority) {
        this(id, title, "", priority);
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public String getAssigneeId() { return assigneeId; }
    public long getCreatedAt() { return createdAt; }
    public Long getDueDate() { return dueDate; }

    // Setters for mutable fields
    /**
     * Updates the task's status.
     *
     * @param status the new status (must not be null)
     * @throws IllegalArgumentException if status is null
     */
    public void setStatus(TaskStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        this.status = status;
    }

    /**
     * Updates the task's priority.
     *
     * @param priority the new priority (must not be null)
     * @throws IllegalArgumentException if priority is null
     */
    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("priority must not be null");
        }
        this.priority = priority;
    }

    /**
     * Assigns this task to a user.
     *
     * @param assigneeId the user's ID, or null to unassign
     */
    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    /**
     * Sets the task's deadline.
     *
     * @param dueDate Unix timestamp in milliseconds, or null for no deadline
     */
    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    // Validatable implementation
    /**
     * Returns an identifier for use in validation error messages.
     *
     * @return a string like "Task[T1]"
     */
    @Override
    public String getValidationId() {
        return "Task[" + id + "]";
    }

    // Object overrides
    @Override
    public String toString() {
        String assignee = (assigneeId != null) ? assigneeId : "unassigned";
        return String.format("Task[%s] \"%s\" (%s, %s, %s)",
            id, title, priority, status, assignee);
    }

    /**
     * Two tasks are equal if they have the same ID.
     * This allows updating a task in a collection by saving a modified version.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
```

```java
/**
 * A person who can be assigned tasks in TaskHub.
 *
 * <p>Users are immutable after creation. In a production system, this class
 * would include authentication credentials, roles, and preferences, but we
 * keep it simple for the case study.</p>
 *
 * <p>Two users are equal if they have the same {@code id}.</p>
 */
public final class User implements Validatable {

    private final String id;
    private final String username;
    private final String email;

    /**
     * Creates a new user.
     *
     * @param id       unique identifier (must not be null or blank)
     * @param username display name (must not be null or blank)
     * @param email    contact email (may be null)
     * @throws IllegalArgumentException if id or username is null or blank
     */
    public User(String id, String username, String email) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("User id must not be blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }

        this.id = id.trim();
        this.username = username.trim();
        this.email = (email != null) ? email.trim().toLowerCase() : null;
    }

    // Getters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    // Validatable implementation
    @Override
    public String getValidationId() {
        return "User[" + id + "]";
    }

    // Object overrides
    @Override
    public String toString() {
        return String.format("User[%s] %s <%s>", id, username, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
```

---

## Section 1: The Repository Problem

### The Pressure

TaskHub needs to store and retrieve different kinds of entities. Today we have Tasks and Users;
tomorrow we'll add Projects, Comments, and AuditLogs.

The naive approach is to write a separate storage class for each type:

```java
// Approach A: Separate class for each entity type
class TaskStorage {
    private Map<String, Task> data = new HashMap<>();

    public void save(Task t) {
        data.put(t.getId(), t);
    }

    public Task findById(String id) {
        return data.get(id);
    }

    public Collection<Task> findAll() {
        return data.values();
    }

    // ... 5 more methods ...
}

class UserStorage {
    private Map<String, User> data = new HashMap<>();

    public void save(User u) {
        data.put(u.getId(), u);
    }

    public User findById(String id) {
        return data.get(id);
    }

    public Collection<User> findAll() {
        return data.values();
    }

    // ... same 5 methods, duplicated ...
}
```

This works, but the duplication is concerning:
- Adding a new entity type means copying all the boilerplate
- A bug fix in one storage class must be replicated to all others
- The code is correct today but will rot as the system grows

An alternative is to use `Object`:

```java
// Approach B: Store everything as Object
class GenericStorage {
    private Map<String, Object> data = new HashMap<>();

    public void save(String id, Object obj) {
        data.put(id, obj);
    }

    public Object findById(String id) {
        return data.get(id);
    }
}

// Usage — compiles but is dangerous
GenericStorage storage = new GenericStorage();
storage.save("T1", new Task("T1", "Fix bug", Priority.HIGH));
storage.save("T1", "oops, a String");  // Overwrites the task — no compiler warning!

Task t = (Task) storage.findById("T1");  // ClassCastException at runtime!
```

This eliminates duplication but introduces worse problems:
- No compile-time type checking
- Runtime `ClassCastException` when types don't match
- The code lies — the API says nothing about what types are valid

### The Insight

The storage logic is identical for all entity types — only the *type itself* changes. If we
could parameterise the class by a type, we'd write it once and reuse it safely for any entity.

This is exactly what generics provide.

---

## Section 2: A Generic Repository Interface

### The Design

We define an interface with **type parameters** that act as placeholders:

```java
import java.util.Collection;
import java.util.Optional;

/**
 * A generic repository for storing and retrieving entities.
 *
 * <p>This interface defines the contract for data access. Implementations
 * decide <em>how</em> to store entities (in memory, in a database, in a file).
 * The interface is agnostic to storage mechanism.</p>
 *
 * <p>Type parameters:</p>
 * <ul>
 *   <li>{@code T} — the type of entity (e.g., Task, User)</li>
 *   <li>{@code ID} — the type of the entity's identifier (e.g., String, Long)</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * Repository<Task, String> tasks = new InMemoryRepository<>(Task::getId);
 * tasks.save(new Task("T1", "Fix bug", Priority.HIGH));
 * Optional<Task> found = tasks.findById("T1");
 * }</pre>
 *
 * @param <T>  the entity type
 * @param <ID> the identifier type
 */
public interface Repository<T, ID> {

    /**
     * Stores an entity.
     *
     * <p>If an entity with the same ID already exists, it is replaced.
     * This allows "update" operations by saving a modified entity.</p>
     *
     * @param entity the entity to store (must not be null)
     * @throws IllegalArgumentException if entity is null
     */
    void save(T entity);

    /**
     * Retrieves an entity by its identifier.
     *
     * <p>Returns {@code Optional.empty()} if no entity with the given ID exists.
     * This forces callers to handle the "not found" case explicitly.</p>
     *
     * @param id the identifier to search for
     * @return an Optional containing the entity, or empty if not found
     */
    Optional<T> findById(ID id);

    /**
     * Returns all stored entities.
     *
     * <p>The returned collection should not be modified by the caller.
     * Implementations may return an unmodifiable view.</p>
     *
     * @return all entities (never null, but may be empty)
     */
    Collection<T> findAll();

    /**
     * Removes an entity by its identifier.
     *
     * @param id the identifier of the entity to remove
     * @return true if an entity was removed, false if no entity had that ID
     */
    boolean deleteById(ID id);

    /**
     * Returns the number of stored entities.
     *
     * @return count of entities (zero or more)
     */
    int count();

    /**
     * Checks whether an entity with the given ID exists.
     *
     * @param id the identifier to check
     * @return true if an entity with that ID exists
     */
    boolean existsById(ID id);
}
```

**Design decisions explained:**

| Decision | Rationale |
|:--------|:---------|
| Two type parameters (`T`, `ID`) | Allows different ID types (String, Long, UUID) per entity |
| `Optional<T>` for `findById` | Forces callers to handle "not found" — no null surprises |
| `Collection<T>` for `findAll` | More flexible than `List<T>` — implementations choose the collection type |
| `save` replaces existing | Simplifies "update" — no separate `update` method needed |

---

## Section 3: An In-Memory Implementation

Now we implement the interface. This version stores entities in a `HashMap` — fast and simple,
perfect for development and testing.

```java
import java.util.*;
import java.util.function.Function;

/**
 * An in-memory implementation of {@link Repository}.
 *
 * <p>This implementation:</p>
 * <ul>
 *   <li>Stores entities in a {@code HashMap} for O(1) lookup by ID</li>
 *   <li>Requires a function to extract the ID from an entity</li>
 *   <li>Is <strong>not thread-safe</strong> (see Part 3 for thread-safe versions)</li>
 *   <li>Loses all data when the program exits (see Part 4 for persistence)</li>
 * </ul>
 *
 * <p>The ID extractor is provided at construction time. This allows the
 * repository to work with any entity type without requiring a common
 * interface for ID extraction.</p>
 *
 * <p>Example:</p>
 * <pre>{@code
 * // Task::getId is a method reference that becomes Function<Task, String>
 * Repository<Task, String> tasks = new InMemoryRepository<>(Task::getId);
 * }</pre>
 *
 * @param <T>  the entity type
 * @param <ID> the identifier type
 */
public final class InMemoryRepository<T, ID> implements Repository<T, ID> {

    /** The underlying storage. Key = entity ID, Value = entity. */
    private final Map<ID, T> storage = new HashMap<>();

    /** Function to extract the ID from an entity. */
    private final Function<T, ID> idExtractor;

    /**
     * Creates a new in-memory repository.
     *
     * @param idExtractor a function that extracts the ID from an entity
     *                    (e.g., {@code Task::getId} or {@code User::getId})
     * @throws IllegalArgumentException if idExtractor is null
     */
    public InMemoryRepository(Function<T, ID> idExtractor) {
        if (idExtractor == null) {
            throw new IllegalArgumentException("idExtractor must not be null");
        }
        this.idExtractor = idExtractor;
    }

    /**
     * {@inheritDoc}
     *
     * <p>If an entity with the same ID exists, it is silently replaced.</p>
     */
    @Override
    public void save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }

        // Extract the ID using the provided function
        ID id = idExtractor.apply(entity);
        if (id == null) {
            throw new IllegalArgumentException("entity ID must not be null");
        }

        // Store (or replace) the entity
        storage.put(id, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<T> findById(ID id) {
        // Handle null ID gracefully — return empty rather than throwing
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Returns an unmodifiable view of the stored entities. Attempting to
     * modify the returned collection will throw {@code UnsupportedOperationException}.</p>
     */
    @Override
    public Collection<T> findAll() {
        // Wrap in unmodifiableCollection to prevent external modification
        return Collections.unmodifiableCollection(storage.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(ID id) {
        if (id == null) {
            return false;
        }
        // remove() returns the previous value, or null if not present
        return storage.remove(id) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int count() {
        return storage.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(ID id) {
        if (id == null) {
            return false;
        }
        return storage.containsKey(id);
    }

    /**
     * Returns a string representation for debugging.
     *
     * @return a string like "InMemoryRepository[5 entities]"
     */
    @Override
    public String toString() {
        return "InMemoryRepository[" + count() + " entities]";
    }
}
```

**Key implementation details:**

1. **The `idExtractor` function**: This is how we get the ID from any entity type.
   `Task::getId` becomes a `Function<Task, String>` that the repository calls when saving.

2. **Defensive null checks**: We reject null entities and null IDs, but `findById(null)`
   returns `Optional.empty()` rather than throwing — a reasonable interpretation of "not
   found".

3. **Unmodifiable collection**: `findAll()` returns a wrapped collection that throws if callers
   try to modify it. This protects internal state.

---

## Section 4: Using the Generic Repository

With the interface and implementation in place, creating repositories is trivial:

```java
// Create repositories for different entity types
// The same class works for both — only the type arguments differ
Repository<Task, String> taskRepo = new InMemoryRepository<>(Task::getId);
Repository<User, String> userRepo = new InMemoryRepository<>(User::getId);

// Store some entities
taskRepo.save(new Task("T1", "Set up project repository", Priority.HIGH));
taskRepo.save(new Task("T2", "Write README documentation", Priority.MEDIUM));
taskRepo.save(new Task("T3", "Configure CI/CD pipeline", Priority.HIGH));

userRepo.save(new User("U1", "alice", "alice@taskhub.io"));
userRepo.save(new User("U2", "bob", "bob@taskhub.io"));

// Retrieve by ID — note: no cast needed!
Optional<Task> maybeTask = taskRepo.findById("T1");
maybeTask.ifPresent(task -> {
    System.out.println("Found: " + task.getTitle());
});

// Handle "not found" explicitly
Optional<Task> missing = taskRepo.findById("T999");
if (missing.isEmpty()) {
    System.out.println("Task T999 not found");
}

// Update by saving again (same ID)
taskRepo.findById("T1").ifPresent(task -> {
    task.setStatus(TaskStatus.IN_PROGRESS);
    task.setAssigneeId("U1");
    taskRepo.save(task);  // replaces the existing entry
});

// List all entities
System.out.println("All tasks:");
for (Task t : taskRepo.findAll()) {
    System.out.println("  " + t);
}
```

**Type safety in action:**

```java
// This compiles and works correctly
taskRepo.save(new Task("T4", "Add unit tests", Priority.MEDIUM));

// This does NOT compile — the compiler catches the error
taskRepo.save(new User("U3", "charlie", "charlie@taskhub.io"));
// Error: incompatible types: User cannot be converted to Task
```

The compiler enforces that only `Task` objects go into `taskRepo`. No casts, no runtime surprises.

---

## Section 5: The Validation Problem

### The Pressure

Our repository stores and retrieves entities, but it doesn't validate them. Watch what happens
with invalid data:

```java
// These tasks have problems, but the repository accepts them silently
taskRepo.save(new Task("T5", "AB", Priority.LOW));           // title too short
taskRepo.save(new Task("T6", "", Priority.HIGH));            // title empty (throws in constructor)
taskRepo.save(new Task("T7", "Valid title", null));          // null priority (defaults to MEDIUM)
```

The `Task` constructor does some validation, but not all business rules can live there. What if:
- Title must be at least 3 characters (business rule, not constructor concern)
- A task can't be assigned to a non-existent user
- Duplicate IDs should be rejected with a helpful message

We could add validation to a service layer using exceptions:

```java
public class TaskService {
    public void createTask(Task task) {
        if (task.getTitle().length() < 3) {
            throw new IllegalArgumentException("Title too short");
        }
        if (repository.existsById(task.getId())) {
            throw new IllegalArgumentException("Duplicate ID");
        }
        repository.save(task);
    }
}

// Caller must catch exceptions
try {
    service.createTask(task);
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

This works but has problems:
- Callers might forget the try-catch (the compiler doesn't force it)
- Nothing in the method signature indicates it might fail
- Exception handling is verbose and scattered

### The Insight

An operation that might fail has two possible outcomes: **success** (with a result value) or
**failure** (with an error message). If we represent this explicitly as a return type, callers
must handle both cases — the type system enforces it.

---

## Section 6: `Result<T>` — Explicit Success or Failure

We create a generic type that represents either a successful outcome (containing a value) or a
failed outcome (containing an error message):

```java
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents the outcome of an operation that might fail.
 *
 * <p>A {@code Result} is either:</p>
 * <ul>
 *   <li><strong>Success</strong>: contains a value of type {@code T}</li>
 *   <li><strong>Failure</strong>: contains an error message</li>
 * </ul>
 *
 * <p>This pattern forces callers to handle both cases explicitly, unlike
 * exceptions which can be accidentally ignored. It's commonly used in
 * functional programming and modern APIs.</p>
 *
 * <h3>Example usage:</h3>
 * <pre>{@code
 * Result<Task> result = taskService.create("T1", "Fix bug", Priority.HIGH);
 *
 * if (result.isSuccess()) {
 *     Task task = result.getValue();
 *     System.out.println("Created: " + task);
 * } else {
 *     System.out.println("Error: " + result.getError());
 * }
 * }</pre>
 *
 * <h3>Chaining operations:</h3>
 * <pre>{@code
 * Result<Task> result = service.findById("T1")
 *     .flatMap(task -> service.assign(task, "U1"))
 *     .map(task -> task.getTitle());
 * }</pre>
 *
 * @param <T> the type of the success value
 */
public final class Result<T> {

    /** The success value (null if this is a failure). */
    private final T value;

    /** The error message (null if this is a success). */
    private final String error;

    /**
     * Private constructor — use static factory methods.
     *
     * @param value the success value (may be null for successful void operations)
     * @param error the error message (null for success)
     */
    private Result(T value, String error) {
        this.value = value;
        this.error = error;
    }

    // Factory methods
    /**
     * Creates a successful result containing the given value.
     *
     * @param value the success value (may be null if null is a valid outcome)
     * @param <T>   the value type
     * @return a successful Result containing the value
     */
    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    /**
     * Creates a failed result containing an error message.
     *
     * @param error the error message (must not be null or blank)
     * @param <T>   the would-be value type
     * @return a failed Result containing the error
     * @throws IllegalArgumentException if error is null or blank
     */
    public static <T> Result<T> failure(String error) {
        if (error == null || error.isBlank()) {
            throw new IllegalArgumentException("Error message must not be blank");
        }
        return new Result<>(null, error);
    }

    // Status checks
    /**
     * Returns true if this is a successful result.
     *
     * @return true if success, false if failure
     */
    public boolean isSuccess() {
        return error == null;
    }

    /**
     * Returns true if this is a failed result.
     *
     * @return true if failure, false if success
     */
    public boolean isFailure() {
        return error != null;
    }

    // Value access
    /**
     * Returns the success value.
     *
     * @return the value
     * @throws IllegalStateException if this is a failure
     */
    public T getValue() {
        if (isFailure()) {
            throw new IllegalStateException("Cannot get value from failed Result: " + error);
        }
        return value;
    }

    /**
     * Returns the error message.
     *
     * @return the error message
     * @throws IllegalStateException if this is a success
     */
    public String getError() {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot get error from successful Result");
        }
        return error;
    }

    /**
     * Returns the value if successful, otherwise returns the default.
     *
     * @param defaultValue the value to return if this is a failure
     * @return the success value or the default
     */
    public T getValueOrDefault(T defaultValue) {
        return isSuccess() ? value : defaultValue;
    }

    // Transformation methods
    /**
     * Transforms the success value using the given function.
     *
     * <p>If this is a failure, returns a new failure with the same error.
     * The mapper function is only called for success cases.</p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * Result<Integer> count = Result.success(5);
     * Result<String> label = count.map(n -> "Count: " + n);
     * // label contains "Count: 5"
     * }</pre>
     *
     * @param mapper the transformation function
     * @param <R>    the result type
     * @return a new Result with the transformed value (or same error)
     * @throws NullPointerException if mapper is null
     */
    public <R> Result<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");

        if (isFailure()) {
            // Propagate the failure — note the type changes from T to R
            return Result.failure(error);
        }

        // Apply the transformation
        return Result.success(mapper.apply(value));
    }

    /**
     * Chains operations that themselves return Results.
     *
     * <p>If this is a failure, returns a new failure with the same error.
     * If this is a success, applies the function and returns its Result.</p>
     *
     * <p>This is essential for sequencing dependent operations:</p>
     * <pre>{@code
     * Result<Task> saved = taskService.create(dto)
     *     .flatMap(task -> taskService.validate(task))
     *     .flatMap(task -> taskService.save(task));
     * // If any step fails, the chain short-circuits
     * }</pre>
     *
     * @param mapper the function that returns a Result
     * @param <R>    the result type
     * @return the Result from the mapper, or a failure if this was a failure
     * @throws NullPointerException if mapper is null
     */
    public <R> Result<R> flatMap(Function<T, Result<R>> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");

        if (isFailure()) {
            return Result.failure(error);
        }

        // Apply the function and return its Result directly
        return mapper.apply(value);
    }

    // Object overrides
    @Override
    public String toString() {
        if (isSuccess()) {
            return "Result.success(" + value + ")";
        } else {
            return "Result.failure(\"" + error + "\")";
        }
    }
}
```

**Key design points:**

| Feature | Purpose |
|:-------|:-------|
| Private constructor + static factories | Ensures Results are always valid (either success OR failure, never both) |
| `map` method | Transforms success values without unwrapping |
| `flatMap` method | Chains operations that themselves return Results |
| `getValueOrDefault` | Safe extraction without throwing |

---

## Section 7: Bounded Generics for Validation

We want validators that:
- Work with different entity types (generic)
- Share a common contract (interface)
- Can access entity-specific methods like `getValidationId()` (bounded)

First, we define what "validatable" means:

```java
/**
 * Marker interface for entities that can be validated.
 *
 * <p>Entities implement this interface to signal they support validation
 * and to provide an identifier for use in error messages.</p>
 */
public interface Validatable {

    /**
     * Returns an identifier for use in validation error messages.
     *
     * <p>This should uniquely identify the entity in a human-readable way,
     * e.g., "Task[T1]" or "User[alice]".</p>
     *
     * @return a validation identifier
     */
    String getValidationId();
}
```

Now we create a validator interface with a **bounded type parameter**:

```java
/**
 * Validates entities of type {@code T}.
 *
 * <p>The bound {@code T extends Validatable} means:</p>
 * <ul>
 *   <li>{@code T} must implement {@code Validatable}</li>
 *   <li>Inside this interface, we can call {@code Validatable} methods on {@code T}</li>
 * </ul>
 *
 * <p>Implementations define the specific validation rules for each entity type.</p>
 *
 * @param <T> the type of entity to validate (must be Validatable)
 */
public interface Validator<T extends Validatable> {

    /**
     * Validates the given entity.
     *
     * @param entity the entity to validate
     * @return {@code Result.success(entity)} if valid,
     *         or {@code Result.failure(errorMessage)} if invalid
     */
    Result<T> validate(T entity);
}
```

And a concrete validator for Tasks:

```java
/**
 * Validates {@link Task} entities.
 *
 * <p>Validation rules:</p>
 * <ul>
 *   <li>Task must not be null</li>
 *   <li>Title must be at least 3 characters</li>
 *   <li>Title must be at most 100 characters</li>
 *   <li>Priority must not be null</li>
 * </ul>
 */
public final class TaskValidator implements Validator<Task> {

    /** Minimum allowed title length. */
    private static final int MIN_TITLE_LENGTH = 3;

    /** Maximum allowed title length. */
    private static final int MAX_TITLE_LENGTH = 100;

    /**
     * Validates a task against all business rules.
     *
     * @param task the task to validate
     * @return success with the task if valid, failure with error message if not
     */
    @Override
    public Result<Task> validate(Task task) {
        // Null check first
        if (task == null) {
            return Result.failure("Task must not be null");
        }

        // Title length checks
        String title = task.getTitle();
        if (title == null || title.length() < MIN_TITLE_LENGTH) {
            return Result.failure(
                task.getValidationId() + ": title must be at least "
                    + MIN_TITLE_LENGTH + " characters"
            );
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            return Result.failure(
                task.getValidationId() + ": title must be at most "
                    + MAX_TITLE_LENGTH + " characters"
            );
        }

        // Priority check
        if (task.getPriority() == null) {
            return Result.failure(
                task.getValidationId() + ": priority must not be null"
            );
        }

        // All checks passed
        return Result.success(task);
    }
}
```

---

## Section 8: The TaskService Layer

Putting it all together, here's a service that uses `Result<T>` for all operations:

```java
import java.util.Collection;
import java.util.Collections;

/**
 * Service layer for Task operations.
 *
 * <p>This class coordinates validation and persistence. All methods return
 * {@link Result} to indicate success or failure explicitly.</p>
 *
 * <p>The service does not throw exceptions for expected failures (validation
 * errors, not found, duplicates). Only programming errors (null arguments)
 * throw exceptions.</p>
 */
public final class TaskService {

    private final Repository<Task, String> repository;
    private final TaskValidator validator;

    /**
     * Creates a new TaskService.
     *
     * @param repository the repository for storing tasks
     * @throws IllegalArgumentException if repository is null
     */
    public TaskService(Repository<Task, String> repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }
        this.repository = repository;
        this.validator = new TaskValidator();
    }

    /**
     * Creates and stores a new task.
     *
     * <p>The task is validated before saving. If validation fails, the task
     * is not saved and a failure Result is returned.</p>
     *
     * @param id       the task ID
     * @param title    the task title
     * @param priority the task priority
     * @return success with the saved task, or failure with error message
     */
    public Result<Task> create(String id, String title, Priority priority) {
        // Create the task object
        Task task;
        try {
            task = new Task(id, title, priority);
        } catch (IllegalArgumentException e) {
            // Constructor validation failed (null/blank id or title)
            return Result.failure(e.getMessage());
        }

        // Validate business rules
        Result<Task> validation = validator.validate(task);
        if (validation.isFailure()) {
            return validation;
        }

        // Check for duplicates
        if (repository.existsById(id)) {
            return Result.failure("Task with ID '" + id + "' already exists");
        }

        // Save and return
        repository.save(task);
        return Result.success(task);
    }

    /**
     * Finds a task by ID.
     *
     * @param id the task ID to find
     * @return success with the task, or failure if not found
     */
    public Result<Task> findById(String id) {
        return repository.findById(id)
            .map(Result::success)
            .orElse(Result.failure("Task not found: " + id));
    }

    /**
     * Assigns a task to a user.
     *
     * <p>This demonstrates chaining with {@code flatMap}: find the task,
     * then validate the assignment, then update.</p>
     *
     * @param taskId the ID of the task to assign
     * @param userId the ID of the user to assign to
     * @return success with the updated task, or failure with error message
     */
    public Result<Task> assignTo(String taskId, String userId) {
        return findById(taskId)
            .flatMap(task -> {
                // Cannot assign completed or cancelled tasks
                if (task.getStatus() == TaskStatus.COMPLETED) {
                    return Result.failure("Cannot assign a completed task");
                }
                if (task.getStatus() == TaskStatus.CANCELLED) {
                    return Result.failure("Cannot assign a cancelled task");
                }

                // Update the task
                task.setAssigneeId(userId);
                task.setStatus(TaskStatus.IN_PROGRESS);
                repository.save(task);

                return Result.success(task);
            });
    }

    /**
     * Marks a task as completed.
     *
     * @param taskId the ID of the task to complete
     * @return success with the updated task, or failure with error message
     */
    public Result<Task> complete(String taskId) {
        return findById(taskId)
            .flatMap(task -> {
                // Can only complete in-progress tasks
                if (task.getStatus() != TaskStatus.IN_PROGRESS) {
                    return Result.failure(
                        "Can only complete tasks that are in progress (current: "
                            + task.getStatus() + ")"
                    );
                }

                task.setStatus(TaskStatus.COMPLETED);
                repository.save(task);

                return Result.success(task);
            });
    }

    /**
     * Returns all tasks.
     *
     * @return an unmodifiable collection of all tasks
     */
    public Collection<Task> findAll() {
        return repository.findAll();
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return task count
     */
    public int count() {
        return repository.count();
    }
}
```

**Usage example:**

```java
// Set up the service
Repository<Task, String> repo = new InMemoryRepository<>(Task::getId);
TaskService service = new TaskService(repo);

// Create tasks — check results explicitly
Result<Task> result1 = service.create("T1", "Set up repository", Priority.HIGH);
if (result1.isSuccess()) {
    System.out.println("Created: " + result1.getValue());
} else {
    System.out.println("Failed: " + result1.getError());
}

// This will fail validation (title too short)
Result<Task> result2 = service.create("T2", "AB", Priority.LOW);
System.out.println(result2);  // Result.failure("Task[T2]: title must be at least 3 characters")

// Chain operations with flatMap
Result<Task> assigned = service.create("T3", "Write documentation", Priority.MEDIUM)
    .flatMap(task -> service.assignTo(task.getId(), "U1"));

if (assigned.isSuccess()) {
    Task task = assigned.getValue();
    System.out.println("Assigned to: " + task.getAssigneeId());
    System.out.println("Status: " + task.getStatus());  // IN_PROGRESS
}
```

---

## Development Summary

### Done

At the end of Part 1, TaskHub has:

| Component | Description |
|:---------|:-----------|
| `Repository<T, ID>` | Generic interface for data access |
| `InMemoryRepository<T, ID>` | HashMap-based implementation with `Function<T, ID>` for ID extraction |
| `Result<T>` | Type-safe representation of success or failure |
| `Validatable` | Interface for entities that support validation |
| `Validator<T extends Validatable>` | Bounded generic interface for validators |
| `TaskValidator` | Concrete validator with business rules |
| `TaskService` | Service layer coordinating validation and persistence |

**Architecture so far:**

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

**Key achievements:**
- One repository implementation serves all entity types
- Type safety enforced at compile time — no casts, no `ClassCastException`
- Error handling is explicit via `Result<T>` — callers must handle both cases
- Validation is reusable and type-safe via bounded generics

### Next

The infrastructure is solid, but the `TaskService` is starting to accumulate concerns:
- It creates tasks
- It validates tasks
- It assigns tasks
- Soon it will need to order task lists by different criteria
- Soon it will need to notify listeners when tasks change

Each new feature means editing `TaskService`. The class is becoming a "god object" that knows
too much.

In **Part 2 — Design Patterns**, we'll introduce:
- **Strategy pattern** for interchangeable ordering algorithms
- **Command pattern** for encapsulating operations as objects
- **Observer pattern** for decoupled event notification
- **Factory pattern** for creating repositories without hard-coding implementations

These patterns will let TaskHub grow without existing code becoming tangled.

---

## Reflective questions

Use these questions to check your understanding:

1. Why does `Repository<T, ID>` have two type parameters instead of just one?

2. What would happen if `InMemoryRepository` stored entities in a `List` instead of a `Map`?
   How would this affect the `findById` method?

3. Why does `findById` return `Optional<T>` instead of just `T`?

4. In `Result<T>`, why is the constructor private? What would go wrong if it were public?

5. The bound `T extends Validatable` restricts what types can be used with `Validator`. What
   error would you get if you tried to create a `Validator<String>`?

6. `TaskService.assignTo` uses `flatMap` to chain operations. Rewrite it using `if` statements
   and explain which version is clearer.

7. What's the difference between `map` and `flatMap` on `Result<T>`? When would you use each?

## Lesson Context

```yaml
previous_lesson:
  topic_code: none

next_lesson:
  topic_code: applied_case_study_taskhub_patternsgenerics.md
```
