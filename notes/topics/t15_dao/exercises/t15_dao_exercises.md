---
title: "DB Connectivity I — JDBC + DAO — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t15_dao
description: "Five progressive exercises building a car-rental system: schema, DAO contract, JDBC implementation, service layer, and richer queries."
created: 2026-02-04
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, mysql, phpmyadmin, jdbc, dao, n-tier, exercises, year2, comp-c8z03]
---
# DB Connectivity I — JDBC + DAO — Exercises

These exercises reinforce the lesson **DB Connectivity I — JDBC + DAO (Data Access Object)** by
building a small **Car Rental** backend using a layered structure (**UI then Service then DAO
then DB**).

## Ground rules

* **No SQL in `Main`** (or in UI classes). SQL belongs in DAO only.
* **Service layer contains business rules**, not JDBC.
* Use **PreparedStatement** placeholders (`?`). No string concatenation for SQL.
* Use **try-with-resources** for `Connection`, `PreparedStatement`, `ResultSet`.
* Prefer simple loops over streams unless asked otherwise.
* Print small outputs in `Exercise.run()` so behaviour can be verified quickly.

## How to run

Each exercise uses:

* **Folder:** `code/src/t15_dao/exercises/eXX/`
* **Filename:** `Exercise.java`
* **Package:** `t15_dao.exercises.eXX`

```java
package t15_dao.exercises.ex01;

public class Exercise {
    public static void run() throws Exception {
        // your tests here
    }
}
```

From your `Main.java`, call:

```java
t15_dao.exercises.ex01.Exercise.run();
```

## Before you start

### Prerequisites checklist

* [ ] Completed the lesson notes: JDBC + DAO + layering
* [ ] You can use `ArrayList` and loops
* [ ] You can write defensive checks (null/blank, fail fast)
* [ ] You understand interfaces and polymorphism (program to an interface)

### How to use these exercises

* Work **in order**. Each exercise adds one more layer or capability.
* Try the tasks first, then open the solution.
* Keep your output tiny but consistent (print IDs, counts, and key objects).

---

## Exercise 01 — Create the schema + run a DB smoke test (JDBC)

**Objective:** Create a minimal MySQL schema for a Car Rental system and write a tiny Java
smoke test that proves your JDBC connection works by running a trivial query and printing a
result.

**Context (software + games):**

* **Software:** This is the first step in any CRUD system: confirm your DB is reachable before
  building abstractions.
* **Games:** Similar “smoke tests” exist when validating network connections, analytics
  endpoints, or persistence services before wiring gameplay logic.

### What you are building

* A MySQL database named `car_rental`
* Two tables:

  * `cars` (fleet inventory)
  * `rentals` (rental bookings)
* A Java class `DbSmokeTest` that:

  * Connects using JDBC
  * Runs `SELECT 1`
  * Prints `"DB connection OK..."`

### Required API

None beyond a runnable entry point.

### Tasks

1. In phpMyAdmin, create a database named `car_rental` (use `utf8mb4` collation).
2. Run this SQL:

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
```

1. Insert a couple of cars:

```sql
INSERT INTO cars(reg, make, model, daily_rate, status)
VALUES
('12-LH-1234', 'Toyota', 'Yaris', 45.00, 'AVAILABLE'),
('15-D-7788', 'Ford', 'Focus', 55.00, 'MAINTENANCE');
```

1. Create `DbSmokeTest` in this package: `t15_dao.exercises.ex01`.
2. In `DbSmokeTest.main` (or `Exercise.run()`), run:

   * `SELECT 1`
   * `SELECT COUNT(*) FROM cars`
3. Print both results.

### Sample input/output

Expected output shape (your numbers may differ):

```text
DB connection OK, SELECT 1 -> 1
Cars in fleet -> 2
```

### Constraints

* Use a **PreparedStatement** even for `SELECT 1` (practice the habit).
* Use try-with-resources for everything.

### Done when...

* You can run the program and see `SELECT 1 -> 1`.
* You can query and print a real value from your DB (car count).
* No JDBC resources are left open (everything in try-with-resources).

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
package t15_dao.exercises.ex01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Exercise {

    public static void run() throws Exception {

        String url = "jdbc:mysql://localhost:3306/car_rental?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "car_rental_user";
        String pass = "your_password";

        try (Connection c = DriverManager.getConnection(url, user, pass)) {

            try (PreparedStatement ps = c.prepareStatement("SELECT 1");
                 ResultSet rs = ps.executeQuery()) {

                rs.next();
                int v = rs.getInt(1);
                System.out.println("DB connection OK, SELECT 1 -> " + v);
            }

            try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM cars");
                 ResultSet rs = ps.executeQuery()) {

                rs.next();
                int count = rs.getInt(1);
                System.out.println("Cars in fleet -> " + count);
            }
        }
    }
}
```

</details>

---

## Exercise 02 — Model the domain + define your DAO contract (Car)

**Objective:** Build a clean **domain model** (`Car`) and a **DAO interface** (`CarDao`) that
defines the persistence operations your system supports, without committing to JDBC yet.

**Context (software + games):**

* **Software:** DAO interfaces keep database decisions out of business logic and make
  implementations swappable.
* **Games:** The same idea appears when gameplay code targets an interface (e.g., `SaveSystem`)
  while swapping between local save vs cloud save.

### What you are building

* A validated `Car` domain class
* A `CarDao` interface defining CRUD-style operations
* An `InMemoryCarDao` implementation to let you test behaviour before JDBC

### Required API

```java
public class Car {
    public Car(int id, String reg, String make, String model, double dailyRate, String status);
    public int id();
    public String reg();
    public String make();
    public String model();
    public double dailyRate();
    public String status();
}

public interface CarDao {
    int insert(String reg, String make, String model, double dailyRate, String status) throws Exception;
    java.util.Optional<Car> findById(int id) throws Exception;
    java.util.List<Car> findAll() throws Exception;
    boolean updateStatus(int id, String newStatus) throws Exception;
    boolean deleteById(int id) throws Exception;
}
```

### Tasks

1. Create `Car` with these validation rules:

   * `id >= 0`
   * `reg`, `make`, `model`, `status` are required (non-null, non-blank)
   * `dailyRate > 0`
   * Trim strings, uppercase `status`
2. Create the `CarDao` interface with the required methods above.
3. Create `InMemoryCarDao` that implements `CarDao` using:

   * `ArrayList<Car>` storage
   * an integer ID counter that starts at 1
4. In `Exercise.run()`:

   * Insert 2 cars
   * Print all cars
   * Update one to `RENTED`
   * Delete one
   * Print again

### Sample input/output

Expected output shape:

```text
Inserted car id=1
Inserted car id=2
All cars:
 - Car{id=1, reg='12-LH-1234', make='Toyota', model='Yaris', rate=45.0, status=AVAILABLE}
 - Car{id=2, reg='15-D-7788', make='Ford', model='Focus', rate=55.0, status=MAINTENANCE}
Update status id=1 -> true
Delete id=2 -> true
All cars:
 - Car{id=1, ... status=RENTED}
```

### Constraints

* No JDBC in this exercise.
* `updateStatus` returns `false` for `id <= 0`. (Fail fast.)

### Done when...

* The service/user code can talk to `CarDao` without knowing the implementation.
* Your in-memory DAO behaves like a tiny fake database.
* You can demonstrate insert/findAll/update/delete in `run()`.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
package t15_dao.exercises.ex02;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Exercise {

    public static void run() throws Exception {

        CarDao dao = new InMemoryCarDao();

        int a = dao.insert("12-LH-1234", "Toyota", "Yaris", 45.0, "AVAILABLE");
        System.out.println("Inserted car id=" + a);

        int b = dao.insert("15-D-7788", "Ford", "Focus", 55.0, "MAINTENANCE");
        System.out.println("Inserted car id=" + b);

        System.out.println("All cars:");
        for (Car c : dao.findAll())
            System.out.println(" - " + c);

        System.out.println("Update status id=" + a + " -> " + dao.updateStatus(a, "RENTED"));
        System.out.println("Delete id=" + b + " -> " + dao.deleteById(b));

        System.out.println("All cars:");
        for (Car c : dao.findAll())
            System.out.println(" - " + c);
    }
}

class Car {

    private int _id;
    private String _reg;
    private String _make;
    private String _model;
    private double _dailyRate;
    private String _status;

    public Car(int id, String reg, String make, String model, double dailyRate, String status) {

        if (id < 0)
            throw new IllegalArgumentException("id must be >= 0");

        if (reg == null || reg.isBlank())
            throw new IllegalArgumentException("reg is required");

        if (make == null || make.isBlank())
            throw new IllegalArgumentException("make is required");

        if (model == null || model.isBlank())
            throw new IllegalArgumentException("model is required");

        if (dailyRate <= 0.0)
            throw new IllegalArgumentException("dailyRate must be > 0");

        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");

        _id = id;
        _reg = reg.trim();
        _make = make.trim();
        _model = model.trim();
        _dailyRate = dailyRate;
        _status = status.trim().toUpperCase();
    }

    public int id() { return _id; }
    public String reg() { return _reg; }
    public String make() { return _make; }
    public String model() { return _model; }
    public double dailyRate() { return _dailyRate; }
    public String status() { return _status; }

    @Override
    public String toString() {
        return "Car{id=" + _id +
                ", reg='" + _reg + "'" +
                ", make='" + _make + "'" +
                ", model='" + _model + "'" +
                ", rate=" + _dailyRate +
                ", status=" + _status +
                "}";
    }
}

interface CarDao {
    int insert(String reg, String make, String model, double dailyRate, String status) throws Exception;
    Optional<Car> findById(int id) throws Exception;
    List<Car> findAll() throws Exception;
    boolean updateStatus(int id, String newStatus) throws Exception;
    boolean deleteById(int id) throws Exception;
}

class InMemoryCarDao implements CarDao {

    private ArrayList<Car> _cars = new ArrayList<>();
    private int _nextId = 1;

    @Override
    public int insert(String reg, String make, String model, double dailyRate, String status) {

        if (reg == null || reg.isBlank())
            throw new IllegalArgumentException("reg is required");

        Car c = new Car(_nextId, reg, make, model, dailyRate, status);
        _cars.add(c);
        _nextId++;
        return c.id();
    }

    @Override
    public Optional<Car> findById(int id) {
        if (id <= 0)
            return Optional.empty();

        for (Car c : _cars) {
            if (c.id() == id)
                return Optional.of(c);
        }

        return Optional.empty();
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(_cars);
    }

    @Override
    public boolean updateStatus(int id, String newStatus) {
        if (id <= 0)
            return false;

        if (newStatus == null || newStatus.isBlank())
            throw new IllegalArgumentException("newStatus is required");

        for (int i = 0; i < _cars.size(); i++) {
            Car c = _cars.get(i);
            if (c.id() == id) {
                Car updated = new Car(c.id(), c.reg(), c.make(), c.model(), c.dailyRate(), newStatus);
                _cars.set(i, updated);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteById(int id) {
        if (id <= 0)
            return false;

        for (int i = 0; i < _cars.size(); i++) {
            if (_cars.get(i).id() == id) {
                _cars.remove(i);
                return true;
            }
        }

        return false;
    }
}
```

</details>

---

## Exercise 03 — Implement `JdbcCarDao` (PreparedStatement + mapping)

**Objective:** Replace the in-memory DAO with a real JDBC implementation that runs SQL, maps
rows into `Car` objects, and returns clean Java results (`Optional`, `List`, `boolean`).

**Context (software + games):**

* **Software:** This is the exact “DAO boundary” used in real apps: JDBC code lives in one
  place, everyone else talks to methods.
* **Games:** The same boundary exists for persistence layers (SQLite saves, leaderboard
  backends, telemetry).

### What you are building

* `JdbcCarDao implements CarDao`
* Safe SQL using `PreparedStatement`
* Row to object mapping using a dedicated `mapRow(ResultSet)`

### Required API

Same `Car` and `CarDao` as Exercise 02, plus:

```java
public class JdbcCarDao implements CarDao {
    public JdbcCarDao(String url, String user, String pass);
}
```

### Tasks

1. Copy `Car` and `CarDao` into this exercise package (or import from your shared project code).
2. Implement `JdbcCarDao` with:

   * fields `_url`, `_user`, `_pass`
   * constructor validation (`url` required)
   * `private Connection open()`
3. Implement the DAO methods using SQL against the `cars` table:

   * `insert(...)` returns the generated key
   * `findById(...)` returns `Optional.empty()` if not found or id invalid
   * `findAll()` returns cars ordered by `id`
   * `updateStatus(...)` returns `true` only when exactly one row changed
   * `deleteById(...)` returns `true` only when exactly one row deleted
4. Add `private static Car mapRow(ResultSet rs)` that reads:

   * `id`, `reg`, `make`, `model`, `daily_rate`, `status`
5. In `Exercise.run()`:

   * Create a `JdbcCarDao`
   * Insert one car
   * Read it back by ID
   * Print `findAll()` count

### Sample input/output

```text
Inserted car id=7
Find by id -> Car{id=7, reg='...', ...}
Cars in DB -> 3
```

### Constraints

* Use placeholders (`?`) for all input values.
* Use try-with-resources for every JDBC object.
* Keep SQL inside `JdbcCarDao` only.

### Done when...

* You can swap `CarDao dao = new InMemoryCarDao()` to `new JdbcCarDao(...)` with no other code
  changes.
* CRUD methods work and return expected values.
* Your DAO never leaks `ResultSet` or JDBC types outside.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
package t15_dao.exercises.ex03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Exercise {

    public static void run() throws Exception {

        String url = "jdbc:mysql://localhost:3306/car_rental?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "car_rental_user";
        String pass = "your_password";

        CarDao dao = new JdbcCarDao(url, user, pass);

        int id = dao.insert("24-D-9001", "Honda", "Civic", 62.5, "AVAILABLE");
        System.out.println("Inserted car id=" + id);

        System.out.println("Find by id -> " + dao.findById(id).orElse(null));

        List<Car> all = dao.findAll();
        System.out.println("Cars in DB -> " + all.size());
    }
}

class JdbcCarDao implements CarDao {

    private String _url;
    private String _user;
    private String _pass;

    public JdbcCarDao(String url, String user, String pass) {

        if (url == null || url.isBlank())
            throw new IllegalArgumentException("url is required");

        _url = url.trim();
        _user = user;
        _pass = pass;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }

    @Override
    public int insert(String reg, String make, String model, double dailyRate, String status) throws Exception {

        if (reg == null || reg.isBlank())
            throw new IllegalArgumentException("reg is required");

        if (make == null || make.isBlank())
            throw new IllegalArgumentException("make is required");

        if (model == null || model.isBlank())
            throw new IllegalArgumentException("model is required");

        if (dailyRate <= 0.0)
            throw new IllegalArgumentException("dailyRate must be > 0");

        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");

        String sql = "INSERT INTO cars(reg, make, model, daily_rate, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, reg.trim());
            ps.setString(2, make.trim());
            ps.setString(3, model.trim());
            ps.setDouble(4, dailyRate);
            ps.setString(5, status.trim().toUpperCase());

            int rows = ps.executeUpdate();
            if (rows != 1)
                throw new IllegalStateException("insert failed, rows=" + rows);

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next())
                    throw new IllegalStateException("no generated key returned");
                return keys.getInt(1);
            }
        }
    }

    @Override
    public Optional<Car> findById(int id) throws Exception {

        if (id <= 0)
            return Optional.empty();

        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    @Override
    public List<Car> findAll() throws Exception {

        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ArrayList<Car> out = new ArrayList<>();
            while (rs.next())
                out.add(mapRow(rs));
            return out;
        }
    }

    @Override
    public boolean updateStatus(int id, String newStatus) throws Exception {

        if (id <= 0)
            return false;

        if (newStatus == null || newStatus.isBlank())
            throw new IllegalArgumentException("newStatus is required");

        String sql = "UPDATE cars SET status = ? WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newStatus.trim().toUpperCase());
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(int id) throws Exception {

        if (id <= 0)
            return false;

        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private static Car mapRow(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String reg = rs.getString("reg");
        String make = rs.getString("make");
        String model = rs.getString("model");
        double rate = rs.getDouble("daily_rate");
        String status = rs.getString("status");

        return new Car(id, reg, make, model, rate, status);
    }
}

class Car {

    private int _id;
    private String _reg;
    private String _make;
    private String _model;
    private double _dailyRate;
    private String _status;

    public Car(int id, String reg, String make, String model, double dailyRate, String status) {

        if (id < 0)
            throw new IllegalArgumentException("id must be >= 0");

        if (reg == null || reg.isBlank())
            throw new IllegalArgumentException("reg is required");

        if (make == null || make.isBlank())
            throw new IllegalArgumentException("make is required");

        if (model == null || model.isBlank())
            throw new IllegalArgumentException("model is required");

        if (dailyRate <= 0.0)
            throw new IllegalArgumentException("dailyRate must be > 0");

        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");

        _id = id;
        _reg = reg.trim();
        _make = make.trim();
        _model = model.trim();
        _dailyRate = dailyRate;
        _status = status.trim().toUpperCase();
    }

    public int id() { return _id; }
    public String reg() { return _reg; }
    public String make() { return _make; }
    public String model() { return _model; }
    public double dailyRate() { return _dailyRate; }
    public String status() { return _status; }

    @Override
    public String toString() {
        return "Car{id=" + _id +
                ", reg='" + _reg + "'" +
                ", make='" + _make + "'" +
                ", model='" + _model + "'" +
                ", rate=" + _dailyRate +
                ", status=" + _status +
                "}";
    }
}

interface CarDao {
    int insert(String reg, String make, String model, double dailyRate, String status) throws Exception;
    Optional<Car> findById(int id) throws Exception;
    List<Car> findAll() throws Exception;
    boolean updateStatus(int id, String newStatus) throws Exception;
    boolean deleteById(int id) throws Exception;
}
```

</details>

---

## Exercise 04 — Add a Service layer (business rules, not JDBC)

**Objective:** Add a **CarRentalService** that enforces business rules (valid statuses,
“renting” a car) while depending only on the **DAO interface**, not JDBC.

**Context (software + games):**

* **Software:** Business rules must remain stable even if you swap databases or add caching.
* **Games:** Your “rules system” shouldn’t care whether state is stored locally, remotely, or
  mocked.

### What you are building

* A `CarRentalService` that uses `CarDao`
* A new DAO method for status filtering (`findByStatus`)
* A “rent car” workflow in the service

### Required API

Extend `CarDao` with:

```java
java.util.List<Car> findByStatus(String status) throws Exception;
```

Add:

```java
public class CarRentalService {
    public CarRentalService(CarDao dao);

    public int addCar(String reg, String make, String model, double dailyRate) throws Exception;
    public boolean rentCar(int carId) throws Exception;     // AVAILABLE -> RENTED
    public boolean returnCar(int carId) throws Exception;   // RENTED -> AVAILABLE
    public java.util.List<Car> listAvailable() throws Exception;
}
```

### Tasks

1. Add `findByStatus` to the DAO interface.
2. Implement `findByStatus` in `JdbcCarDao` using:

   * `SELECT ... WHERE status = ? ORDER BY id`
3. Create `CarRentalService` with a private `_dao` field:

   * Validate `_dao` is non-null in constructor.
4. Implement service rules:

   * `addCar` always inserts with status `AVAILABLE`.
   * `rentCar` only succeeds if current status is `AVAILABLE`.
   * `returnCar` only succeeds if current status is `RENTED`.
5. In `Exercise.run()`:

   * Add two cars
   * Rent one
   * Print available cars
   * Return it
   * Print available cars again

### Constraints

* Service must not contain SQL.
* DAO must not contain business rule branching like “only rent if available” (that belongs in
  service).

### Done when...

* Your workflow can be tested without changing DAO code.
* Renting fails safely when the car is not available.
* You can swap DAO implementations (in-memory vs JDBC) and the service still compiles.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
package t15_dao.exercises.ex04;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Exercise {

    public static void run() throws Exception {

        String url = "jdbc:mysql://localhost:3306/car_rental?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "car_rental_user";
        String pass = "your_password";

        CarDao dao = new JdbcCarDao(url, user, pass);
        CarRentalService service = new CarRentalService(dao);

        int a = service.addCar("23-LH-1111", "Toyota", "Corolla", 58.0);
        int b = service.addCar("22-D-2222", "VW", "Golf", 60.0);

        System.out.println("Added cars: " + a + ", " + b);

        System.out.println("Rent car " + a + " -> " + service.rentCar(a));

        System.out.println("Available cars:");
        for (Car c : service.listAvailable())
            System.out.println(" - " + c);

        System.out.println("Return car " + a + " -> " + service.returnCar(a));

        System.out.println("Available cars:");
        for (Car c : service.listAvailable())
            System.out.println(" - " + c);
    }
}

class CarRentalService {

    private static Set<String> ALLOWED = Set.of("AVAILABLE", "RENTED", "MAINTENANCE");

    private CarDao _dao;

    public CarRentalService(CarDao dao) {
        if (dao == null)
            throw new IllegalArgumentException("dao is null");
        _dao = dao;
    }

    public int addCar(String reg, String make, String model, double dailyRate) throws Exception {
        return _dao.insert(reg, make, model, dailyRate, "AVAILABLE");
    }

    public boolean rentCar(int carId) throws Exception {

        Optional<Car> c = _dao.findById(carId);
        if (c.isEmpty())
            return false;

        if (!c.get().status().equals("AVAILABLE"))
            return false;

        return _dao.updateStatus(carId, "RENTED");
    }

    public boolean returnCar(int carId) throws Exception {

        Optional<Car> c = _dao.findById(carId);
        if (c.isEmpty())
            return false;

        if (!c.get().status().equals("RENTED"))
            return false;

        return _dao.updateStatus(carId, "AVAILABLE");
    }

    public List<Car> listAvailable() throws Exception {
        return _dao.findByStatus("AVAILABLE");
    }

    @SuppressWarnings("unused")
    private static boolean isAllowed(String status) {
        if (status == null || status.isBlank())
            return false;
        return ALLOWED.contains(status.trim().toUpperCase());
    }
}

interface CarDao {
    int insert(String reg, String make, String model, double dailyRate, String status) throws Exception;
    Optional<Car> findById(int id) throws Exception;
    List<Car> findAll() throws Exception;
    List<Car> findByStatus(String status) throws Exception;
    boolean updateStatus(int id, String newStatus) throws Exception;
    boolean deleteById(int id) throws Exception;
}

class JdbcCarDao implements CarDao {

    private String _url;
    private String _user;
    private String _pass;

    public JdbcCarDao(String url, String user, String pass) {

        if (url == null || url.isBlank())
            throw new IllegalArgumentException("url is required");

        _url = url.trim();
        _user = user;
        _pass = pass;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }

    @Override
    public int insert(String reg, String make, String model, double dailyRate, String status) throws Exception {

        if (reg == null || reg.isBlank())
            throw new IllegalArgumentException("reg is required");

        if (make == null || make.isBlank())
            throw new IllegalArgumentException("make is required");

        if (model == null || model.isBlank())
            throw new IllegalArgumentException("model is required");

        if (dailyRate <= 0.0)
            throw new IllegalArgumentException("dailyRate must be > 0");

        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");

        String sql = "INSERT INTO cars(reg, make, model, daily_rate, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, reg.trim());
            ps.setString(2, make.trim());
            ps.setString(3, model.trim());
            ps.setDouble(4, dailyRate);
            ps.setString(5, status.trim().toUpperCase());

            int rows = ps.executeUpdate();
            if (rows != 1)
                throw new IllegalStateException("insert failed, rows=" + rows);

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next())
                    throw new IllegalStateException("no generated key returned");
                return keys.getInt(1);
            }
        }
    }

    @Override
    public Optional<Car> findById(int id) throws Exception {

        if (id <= 0)
            return Optional.empty();

        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    @Override
    public List<Car> findAll() throws Exception {

        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ArrayList<Car> out = new ArrayList<>();
            while (rs.next())
                out.add(mapRow(rs));
            return out;
        }
    }

    @Override
    public List<Car> findByStatus(String status) throws Exception {

        if (status == null || status.isBlank())
            return List.of();

        String sql = "SELECT id, reg, make, model, daily_rate, status FROM cars WHERE status = ? ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                ArrayList<Car> out = new ArrayList<>();
                while (rs.next())
                    out.add(mapRow(rs));
                return out;
            }
        }
    }

    @Override
    public boolean updateStatus(int id, String newStatus) throws Exception {

        if (id <= 0)
            return false;

        if (newStatus == null || newStatus.isBlank())
            throw new IllegalArgumentException("newStatus is required");

        String sql = "UPDATE cars SET status = ? WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newStatus.trim().toUpperCase());
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(int id) throws Exception {

        if (id <= 0)
            return false;

        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private static Car mapRow(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String reg = rs.getString("reg");
        String make = rs.getString("make");
        String model = rs.getString("model");
        double rate = rs.getDouble("daily_rate");
        String status = rs.getString("status");

        return new Car(id, reg, make, model, rate, status);
    }
}

class Car {

    private int _id;
    private String _reg;
    private String _make;
    private String _model;
    private double _dailyRate;
    private String _status;

    public Car(int id, String reg, String make, String model, double dailyRate, String status) {

        if (id < 0)
            throw new IllegalArgumentException("id must be >= 0");

        if (reg == null || reg.isBlank())
            throw new IllegalArgumentException("reg is required");

        if (make == null || make.isBlank())
            throw new IllegalArgumentException("make is required");

        if (model == null || model.isBlank())
            throw new IllegalArgumentException("model is required");

        if (dailyRate <= 0.0)
            throw new IllegalArgumentException("dailyRate must be > 0");

        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");

        _id = id;
        _reg = reg.trim();
        _make = make.trim();
        _model = model.trim();
        _dailyRate = dailyRate;
        _status = status.trim().toUpperCase();
    }

    public int id() { return _id; }
    public String reg() { return _reg; }
    public String make() { return _make; }
    public String model() { return _model; }
    public double dailyRate() { return _dailyRate; }
    public String status() { return _status; }

    @Override
    public String toString() {
        return "Car{id=" + _id +
                ", reg='" + _reg + "'" +
                ", make='" + _make + "'" +
                ", model='" + _model + "'" +
                ", rate=" + _dailyRate +
                ", status=" + _status +
                "}";
    }
}
```

</details>

---

## Exercise 05 — Add richer queries (paging + “search”) and reflect on design

**Objective:** Extend your DAO API with common real-world needs: **paging**, **search**, and a
“tiny reporting query”. Then demonstrate that these are still cleanly isolated in the DAO
layer.

### What you are building

* DAO extensions:

  * `findPage(int offset, int limit)`
  * `findByMakeOrModel(String query)` (simple search)
* A small reporting call:

  * `countByStatus()`

### Required API

Add to `CarDao`:

```java
java.util.List<Car> findPage(int offset, int limit) throws Exception;
java.util.List<Car> findByMakeOrModel(String query) throws Exception;
java.util.Map<String, Integer> countByStatus() throws Exception;
```

### Tasks

1. Implement `findPage(offset, limit)` using:

   * `ORDER BY id LIMIT ? OFFSET ?`
2. Implement `findByMakeOrModel(query)` using:

   * `WHERE make LIKE ? OR model LIKE ?`
   * Hint: pass `"%"+q+"%"` to both placeholders
3. Implement `countByStatus()` using:

   * `SELECT status, COUNT(*) AS c FROM cars GROUP BY status`
4. In `Exercise.run()`:

   * Print the first page of 3 cars
   * Print search results for `"for"` (should match Ford, etc.)
   * Print the status counts map

### Constraints

* Clamp or reject invalid paging:

  * If `limit <= 0`, return an empty list.
  * If `offset < 0`, treat it as 0.
* Search query:

  * If query is null/blank, return empty list.

### Done when...

* Paging works without loading the whole table.
* Search works without SQL injection risk (still placeholders).
* Your “report” method returns a clean Java `Map`, not JDBC types.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    ✅ Solution
  </summary>

```java
package t15_dao.exercises.ex05;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exercise {

    public static void run() throws Exception {

        String url = "jdbc:mysql://localhost:3306/car_rental?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "car_rental_user";
        String pass = "your_password";

        CarDao dao = new JdbcCarDao(url, user, pass);

        System.out.println("Page (offset=0, limit=3):");
        for (Car c : dao.findPage(0, 3))
            System.out.println(" - " + c);

        System.out.println("Search query='for':");
        for (Car c : dao.findByMakeOrModel("for"))
            System.out.println(" - " + c);

        System.out.println("Count by status:");
        System.out.println(dao.countByStatus());
    }
}

interface CarDao {
    List<Car> findPage(int offset, int limit) throws Exception;
    List<Car> findByMakeOrModel(String query) throws Exception;
    Map<String, Integer> countByStatus() throws Exception;
}

class JdbcCarDao implements CarDao {

    private String _url;
    private String _user;
    private String _pass;

    public JdbcCarDao(String url, String user, String pass) {
        if (url == null || url.isBlank())
            throw new IllegalArgumentException("url is required");
        _url = url.trim();
        _user = user;
        _pass = pass;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(_url, _user, _pass);
    }

    @Override
    public List<Car> findPage(int offset, int limit) throws Exception {

        if (limit <= 0)
            return List.of();

        int safeOffset = Math.max(0, offset);

        String sql = "SELECT id, reg, make, model, daily_rate, status " +
                     "FROM cars ORDER BY id LIMIT ? OFFSET ?";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, safeOffset);

            try (ResultSet rs = ps.executeQuery()) {
                ArrayList<Car> out = new ArrayList<>();
                while (rs.next())
                    out.add(mapRow(rs));
                return out;
            }
        }
    }

    @Override
    public List<Car> findByMakeOrModel(String query) throws Exception {

        if (query == null || query.isBlank())
            return List.of();

        String q = "%" + query.trim() + "%";

        String sql = "SELECT id, reg, make, model, daily_rate, status " +
                     "FROM cars WHERE make LIKE ? OR model LIKE ? ORDER BY id";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, q);
            ps.setString(2, q);

            try (ResultSet rs = ps.executeQuery()) {
                ArrayList<Car> out = new ArrayList<>();
                while (rs.next())
                    out.add(mapRow(rs));
                return out;
            }
        }
    }

    @Override
    public Map<String, Integer> countByStatus() throws Exception {

        String sql = "SELECT status, COUNT(*) AS c FROM cars GROUP BY status";

        try (Connection c = open();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            HashMap<String, Integer> out = new HashMap<>();
            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("c");
                out.put(status, count);
            }

            return out;
        }
    }

    private static Car mapRow(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String reg = rs.getString("reg");
        String make = rs.getString("make");
        String model = rs.getString("model");
        double rate = rs.getDouble("daily_rate");
        String status = rs.getString("status");

        return new Car(id, reg, make, model, rate, status);
    }
}

class Car {

    private int _id;
    private String _reg;
    private String _make;
    private String _model;
    private double _dailyRate;
    private String _status;

    public Car(int id, String reg, String make, String model, double dailyRate, String status) {

        if (id < 0)
            throw new IllegalArgumentException("id must be >= 0");

        if (reg == null || reg.isBlank())
            throw new IllegalArgumentException("reg is required");

        if (make == null || make.isBlank())
            throw new IllegalArgumentException("make is required");

        if (model == null || model.isBlank())
            throw new IllegalArgumentException("model is required");

        if (dailyRate <= 0.0)
            throw new IllegalArgumentException("dailyRate must be > 0");

        if (status == null || status.isBlank())
            throw new IllegalArgumentException("status is required");

        _id = id;
        _reg = reg.trim();
        _make = make.trim();
        _model = model.trim();
        _dailyRate = dailyRate;
        _status = status.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return "Car{id=" + _id +
                ", reg='" + _reg + "'" +
                ", make='" + _make + "'" +
                ", model='" + _model + "'" +
                ", rate=" + _dailyRate +
                ", status=" + _status +
                "}";
    }
}
```

</details>

---

## Lesson Context

```yaml
linked_lesson:
  topic_code: "t15_dao"
  lesson_path: "/notes/topics/t15_dao/t15_dao.md"
  primary_domain_emphasis: "Balanced"
  difficulty_tier: "Foundation → Intermediate"
```
