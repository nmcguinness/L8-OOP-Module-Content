---
title: "DB Connectivity I — JDBC + DAO — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t15_dao
description: "Five step-by-step exercises building a car-rental back end: schema and smoke test, a domain class and DAO contract with its own exception type, a JDBC implementation, a service layer, and richer queries."
created: 2026-02-04
last_updated: 2026-11-03
version: 1.2
status: published
authors: ["OOP Teaching Team"]
tags: [java, mysql, phpmyadmin, jdbc, dao, n-tier, exercises, year2, comp-c8z03]
---
# DB Connectivity I — JDBC + DAO — Exercises

These exercises build a small car-rental back end, one layer at a time, following the [t15 notes](../t15_dao_notes.md). The program is split into layers, and each layer talks only to the one below it:

| Layer | Its job | In these exercises |
| :-- | :-- | :-- |
| Presentation | show results, report errors | `Exercise.run()` |
| Service | business rules, such as "only rent an available car" | `CarRentalService` |
| DAO | all the SQL, and nothing else | `CarDao`, `JdbcCarDao` |
| Database | stores the data | MySQL, through phpMyAdmin |

A **DAO** (Data Access Object) is a class whose only job is to move objects in and out of the database. Everything above it calls methods such as `findById(3)` and never sees SQL.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution, and to its tests where there are some. Work through them in order: each one adds one layer or feature to the last.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 01 Schema and smoke test | creating tables, a first JDBC query | Foundation |
| 02 Domain class and DAO contract | a validated class, an interface, a fake DAO | Foundation |
| 03 A JDBC DAO | `PreparedStatement`, mapping rows to objects | Intermediate |
| 04 A service layer | business rules that never touch SQL | Intermediate |
| 05 Richer queries | paging, search, a small report | Intermediate |

## Rules for every exercise

- SQL goes in the DAO only. Never in `run()` or in the service.
- Business rules go in the service. Never JDBC.
- Every value in a query goes through a `?` placeholder. Never join values into SQL text.
- Every `Connection`, `PreparedStatement` and `ResultSet` is opened in try-with-resources.
- DAO methods declare `throws DaoException`, never `throws Exception`. A `SQLException` stays inside the DAO, kept as the cause of a `DaoException`.

## Before you start

- [ ] MySQL is running (for example through XAMPP) and you can open phpMyAdmin.
- [ ] The MySQL driver is in the project's `pom.xml`. See [Step 4](../t15_dao_notes.md#step-4--add-the-mysql-jdbc-driver-to-your-project).
- [ ] You can explain what an interface is and why code should depend on one.

## How to run

Each exercise goes in its own package, `t15_dao.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`.

> **Watch out:** never type a password into a `.java` file. It ends up in Git, in every clone and in your team-mates' history. Every exercise reads its settings from `config/db.properties`, which is listed in `.gitignore`. See [Step 6](../t15_dao_notes.md#step-6--keep-credentials-out-of-your-code).

---

## Exercise 01 — Schema and smoke test

**Goal:** Create the car-rental database, then write the smallest possible Java program that proves it can talk to it. A **smoke test** checks that the basic connection works before you build anything on top.

**You will need:** [Step 1](../t15_dao_notes.md#step-1--create-a-database-in-phpmyadmin), [Step 3](../t15_dao_notes.md#step-3--create-the-table-schema) and [Step 7](../t15_dao_notes.md#step-7--connection-smoke-test).

**Steps**

1. In phpMyAdmin, create a database called `car_rental` with the `utf8mb4` collation.
2. In its **SQL** tab, run the script below. It creates a `cars` table and a `rentals` table, and adds two cars.
3. In the `code/config` folder, copy `db.properties.example` to `db.properties` and fill in your settings. With XAMPP's defaults, the user is `root` and the password is empty.
4. Create the record `DbConfig` shown in the notes, in the package `t15_dao.exercises`, so that all the exercises can share it.
5. In `run()`, load the settings. If the file cannot be read, print a helpful message and return.
6. Open a connection with `DriverManager.getConnection`, using the three values from `DbConfig`.
7. Run `SELECT 1` with a `PreparedStatement` and print the result.
8. Run `SELECT COUNT(*) FROM cars` and print the result.
9. Catch `SQLException` in `run()` and print its message with a hint about what to check.

```sql
CREATE TABLE cars (
  id INT NOT NULL AUTO_INCREMENT,
  reg VARCHAR(20) NOT NULL,
  make VARCHAR(40) NOT NULL,
  model VARCHAR(40) NOT NULL,
  daily_rate DECIMAL(8,2) NOT NULL,
  status ENUM('AVAILABLE','RENTED','MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE',
  PRIMARY KEY (id),
  UNIQUE KEY uk_cars_reg (reg)
);

CREATE INDEX idx_cars_status ON cars(status);

CREATE TABLE rentals (
  id INT NOT NULL AUTO_INCREMENT,
  car_id INT NOT NULL,
  customer_name VARCHAR(80) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  status ENUM('OPEN','CLOSED') NOT NULL DEFAULT 'OPEN',
  PRIMARY KEY (id),
  CONSTRAINT fk_rentals_car
    FOREIGN KEY (car_id) REFERENCES cars(id)
      ON DELETE RESTRICT
);

CREATE INDEX idx_rentals_status ON rentals(status);
CREATE INDEX idx_rentals_customer ON rentals(customer_name);

INSERT INTO cars(reg, make, model, daily_rate, status)
VALUES
('12-LH-1234', 'Toyota', 'Yaris', 45.00, 'AVAILABLE'),
('15-D-7788', 'Ford', 'Focus', 55.00, 'MAINTENANCE');
```

**Example**

```text
DB connection OK, SELECT 1 -> 1
Cars in fleet -> 2
```

If MySQL is not running, you see the error instead:

```text
Database error: Communications link failure ...
Check that MySQL is running (XAMPP) and that the car_rental database and cars table exist.
```

**Rules**

- Use a `PreparedStatement` even for `SELECT 1`, to build the habit.
- No database URL, user name or password appears in any `.java` file.

<details>
<summary>Hints</summary>

1. The program runs in the `code` folder, so the path is `Path.of("config", "db.properties")`.
2. A `ResultSet` starts **before** the first row. Call `rs.next()` once, then `rs.getInt(1)`.
3. Several resources can share one `try`: `try (PreparedStatement ps = ...; ResultSet rs = ...)`.

</details>

**Done when**

- [ ] `run()` prints the two lines shown.
- [ ] Every JDBC object is opened in try-with-resources.
- [ ] `git status` does not list `config/db.properties`.

**Package:** `t15_dao.exercises.ex01`

**Solution:** [t15 exercise 01 solution code](../../../../code/src/t15_dao/exercises/ex01/) and [DbConfig](../../../../code/src/t15_dao/exercises/DbConfig.java).
**Tests:** [DbConfig tests](../../../../code/test/t15_dao/exercises/DbConfigTest.java).

---

## Exercise 02 — Domain class and DAO contract

**Goal:** Write the `Car` class and the `CarDao` interface that says what the program can ask the database to do. Then write a fake DAO that keeps cars in a list, so you can test everything without a database.

**You will need:** [Step 8](../t15_dao_notes.md#step-8--domain-class-task) and [Step 9](../t15_dao_notes.md#step-9--dao-exception-and-interface).

**Steps**

1. Create `Car` with six `private final` fields: `id`, `reg`, `make`, `model`, `dailyRate` and `status`, a constructor, and a getter for each, named as in the signatures below.
2. In the constructor, check the values: `id` is 0 or more, the four text fields are not blank, and `dailyRate` is above 0. Trim the text, and make `status` upper case.
3. Add `withId(int)` and `withStatus(String)`. Each returns a **new** `Car` with one value changed.
4. Create a **checked** exception `DaoException` with a constructor that takes a message and a cause.
5. Create the interface `CarDao` below.
6. Create `InMemoryCarDao`, which implements `CarDao` with an `ArrayList<Car>` and an id counter that starts at 1. `insert` stores `car.withId(nextId)`.
7. In `run()`, insert two cars, print them all, mark car 1 as `RENTED`, delete car 2, and print them all again.

```java
public Car(int id, String reg, String make, String model, double dailyRate, String status)

public interface CarDao {
    int insert(Car car) throws DaoException;
    Optional<Car> findById(int id) throws DaoException;
    List<Car> findAll() throws DaoException;
    boolean updateStatus(int id, String newStatus) throws DaoException;
    boolean deleteById(int id) throws DaoException;
}
```

**Example**

```text
Inserted car id=1
Inserted car id=2
All cars:
 - Car{id=1, reg='12-LH-1234', make='Toyota', model='Yaris', rate=45.0, status=AVAILABLE}
 - Car{id=2, reg='15-D-7788', make='Ford', model='Focus', rate=55.0, status=MAINTENANCE}
Update status id=1 -> true
Delete id=2 -> true
All cars:
 - Car{id=1, reg='12-LH-1234', make='Toyota', model='Yaris', rate=45.0, status=RENTED}
```

**Rules**

- `insert` takes a whole `Car`. Pass `0` as the id of a car that is not stored yet; the DAO assigns the real one.
- `updateStatus` and `deleteById` return `false` for an id of 0 or less.
- `InMemoryCarDao` has no `throws` clauses. Nothing in memory can fail, and a class may declare fewer checked exceptions than its interface.

<details>
<summary>Hints</summary>

1. Why not `insert(String reg, String make, String model, double rate, String status)`? Five parameters of similar types are easy to pass in the wrong order, and every new column would change the interface.
2. A `Car` cannot change once made, so `updateStatus` replaces the car in the list: `cars.set(i, cars.get(i).withStatus(newStatus))`.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] `new Car(0, "  ", "Ford", "Focus", 55.0, "AVAILABLE")` throws an `IllegalArgumentException`.

**Package:** `t15_dao.exercises.ex02`

**Solution:** [t15 exercise 02 solution code](../../../../code/src/t15_dao/exercises/ex02/).
**Tests:** [t15 exercise 02 tests](../../../../code/test/t15_dao/exercises/ex02/).

---

## Exercise 03 — A JDBC DAO

**Goal:** Write the real DAO. It runs SQL against the `cars` table, turns each row into a `Car`, and hands back plain Java results: an `Optional`, a `List` or a `boolean`.

**You will need:** [Step 10](../t15_dao_notes.md#step-10--jdbc-implementation-jdbctaskdao).

**Steps**

1. Copy `Car`, `DaoException` and `CarDao` into this package.
2. Create `JdbcCarDao`, which implements `CarDao`. Its constructor takes `url`, `user` and `pass`, stores them in `final` fields, and rejects a blank `url`.
3. Add `private Connection open()`, which calls `DriverManager.getConnection(url, user, pass)`.
4. Add `private static Car mapRow(ResultSet rs)`. It reads the six columns of the current row and returns a `Car`.
5. Write the five DAO methods. Each one catches `SQLException` and throws `new DaoException("what failed", e)`.
6. In `run()`, load a `DbConfig`, create a `JdbcCarDao` from it, insert a car, find it by its new id, and print how many cars `findAll()` returns.

```java
public JdbcCarDao(String url, String user, String pass)
private static Car mapRow(ResultSet rs) throws SQLException
```

**Example** (your ids and counts will differ)

```text
Inserted car id=7
Find by id -> Car{id=7, reg='...', make='...', model='...', rate=..., status=AVAILABLE}
Cars in DB -> 3
```

**Rules**

- `insert` returns the id that MySQL generated.
- `findById` returns `Optional.empty()` for an unknown id, or an id of 0 or less.
- `findAll` returns the cars ordered by `id`.
- `updateStatus` and `deleteById` return `true` only when exactly one row changed.
- No `ResultSet`, `SQLException` or other JDBC type appears in any public method.

<details>
<summary>Hints</summary>

1. To get the new id, pass `Statement.RETURN_GENERATED_KEYS` to `prepareStatement`, then read `ps.getGeneratedKeys()` after `executeUpdate()`.
2. `executeUpdate()` returns the number of rows changed, so `return rows == 1;`.
3. Read a `DECIMAL` column with `rs.getDouble("daily_rate")`.

</details>

**Done when**

- [ ] `run()` prints three lines like those shown.
- [ ] Changing `new InMemoryCarDao()` to `new JdbcCarDao(...)` in a program needs no other change.

**Package:** `t15_dao.exercises.ex03`

**Solution:** [t15 exercise 03 solution code](../../../../code/src/t15_dao/exercises/ex03/).

---

## Exercise 04 — A service layer

**Goal:** Add a `CarRentalService` that holds the business rules, such as "you can only rent an available car". It works with any `CarDao`, so the rules can be tested without a database.

**You will need:** [Step 11](../t15_dao_notes.md#step-11--service-layer-taskservice) and [Where DAO fits in an N-tier structure](../t15_dao_notes.md#where-dao-fits-in-an-n-tier-structure).

**Steps**

1. Add `findByStatus` to `CarDao`, and implement it in `JdbcCarDao` with `SELECT ... WHERE status = ? ORDER BY id`.
2. Create `CarRentalService`. Its constructor takes a `CarDao` and rejects `null`.
3. `addCar` creates `new Car(0, reg, make, model, dailyRate, "AVAILABLE")` and inserts it.
4. `rentCar` looks the car up. It changes the status to `RENTED` only if the car exists and is `AVAILABLE`. Otherwise it returns `false`.
5. `returnCar` does the opposite: `RENTED` back to `AVAILABLE`.
6. `listAvailable` returns the cars whose status is `AVAILABLE`.
7. In `run()`, add two cars, rent one, list the available cars, return it, and list them again.

```java
public CarRentalService(CarDao dao)
public int addCar(String reg, String make, String model, double dailyRate) throws DaoException
public boolean rentCar(int carId) throws DaoException
public boolean returnCar(int carId) throws DaoException
public List<Car> listAvailable() throws DaoException
```

**Example** (your ids will differ)

```text
Added cars: 3, 4
Rent car 3 -> true
Available cars:
 - Car{id=1, reg='12-LH-1234', make='Toyota', model='Yaris', rate=45.0, status=AVAILABLE}
 - Car{id=4, reg='22-D-2222', make='VW', model='Golf', rate=60.0, status=AVAILABLE}
Return car 3 -> true
Available cars:
 - Car{id=1, ...}
 - Car{id=3, ...}
 - Car{id=4, ...}
```

**Rules**

- No SQL in the service.
- The service does not catch `DaoException`. It passes storage failures up to `run()`, which reports them.
- The DAO contains no business rules. "Only rent if available" belongs in the service.

<details>
<summary>Hints</summary>

1. Keep the status names in constants: `private static final String AVAILABLE = "AVAILABLE";`.
2. `Optional<Car> car = dao.findById(carId); if (car.isEmpty()) { return false; }`
3. To test the rules without MySQL, give the service a fake `CarDao`, such as your `InMemoryCarDao` from Exercise 02 with `findByStatus` added. The worked tests do exactly this.

</details>

**Done when**

- [ ] `run()` prints output like that shown.
- [ ] Renting a car that is already rented, or under maintenance, returns `false`.

> **Watch out:** registrations are `UNIQUE` in the table. Run this exercise a second time and the first `addCar` fails, printing `Storage error: Could not insert car 23-LH-1111`. Delete the two cars in phpMyAdmin, or change the registrations, before running it again.

**Package:** `t15_dao.exercises.ex04`

**Solution:** [t15 exercise 04 solution code](../../../../code/src/t15_dao/exercises/ex04/).
**Tests:** [t15 exercise 04 tests](../../../../code/test/t15_dao/exercises/ex04/).

---

## Exercise 05 — Richer queries

**Goal:** Add three queries that real programs need: a page of results, a simple search, and a count of cars in each status. The rest of the program still sees only Java types.

**You will need:** [Step 10](../t15_dao_notes.md#step-10--jdbc-implementation-jdbctaskdao) and [Placeholder](../t15_dao_notes.md#placeholder).

**Steps**

1. Add the three methods below to `CarDao`, and implement them in `JdbcCarDao`.
2. `findPage` uses `ORDER BY id LIMIT ? OFFSET ?`. A `limit` of 0 or less gives an empty list, and a negative `offset` counts as 0.
3. `findByMakeOrModel` uses `WHERE make LIKE ? OR model LIKE ?`. A blank query gives an empty list.
4. `countByStatus` uses `SELECT status, COUNT(*) AS c FROM cars GROUP BY status` and returns a `Map` from status to count.
5. In `run()`, print the first page of three cars, the results of searching for `for`, and the counts.

```java
List<Car> findPage(int offset, int limit) throws DaoException;
List<Car> findByMakeOrModel(String query) throws DaoException;
Map<String, Integer> countByStatus() throws DaoException;
```

**Example** (your cars and counts will differ)

```text
Page (offset=0, limit=3):
 - Car{id=1, reg='12-LH-1234', make='Toyota', model='Yaris', ...}
 - Car{id=2, reg='15-D-7788', make='Ford', model='Focus', ...}
 - Car{id=3, ...}
Search query='for':
 - Car{id=2, reg='15-D-7788', make='Ford', model='Focus', ...}
Count by status:
{AVAILABLE=3, MAINTENANCE=1}
```

**Rules**

- The search text goes through a placeholder like every other value.
- `countByStatus` returns a Java `Map`, not a `ResultSet`.

<details>
<summary>Hints</summary>

1. The `%` wildcards go in the **value**, not the SQL: pass `"%" + query + "%"` to both placeholders.
2. `LIMIT ? OFFSET ?` takes the limit first, so it is placeholder 1, and the offset is placeholder 2.

</details>

**Done when**

- [ ] `run()` prints a page, the search results and the counts.
- [ ] Searching for `' OR '1'='1` finds nothing, rather than every car.

**Think about it:** why is paging done in SQL, rather than by calling `findAll()` and taking the first three cars in Java?

**Package:** `t15_dao.exercises.ex05`

**Solution:** [t15 exercise 05 solution code](../../../../code/src/t15_dao/exercises/ex05/).
