---
title: "Design Patterns II — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t14_design_patterns_2
description: "Six exercises applying Factory, Observer and Adapter across parsers, enemy spawning, event listeners, and legacy system integration."
created: 2026-02-06
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, design-patterns, factory, observer, adapter, coordination, architecture, oop, exercises, year2, comp-c8z03]
---
# Design Patterns II — Exercises

> These exercises build directly on the Design Patterns II — Structure, Coordination &
> Extension notes. This set focuses on Factory, Observer, and Adapter in a small, runnable
> task-processing mini-system.
>
> You will practise creation boundaries (Factory), completion events and plug-in features
> (Observer), and integrating incompatible APIs (Adapter).

## Ground rules

* Keep selection logic out of “core” classes (avoid giant `if`/`switch` in the wrong place).
* Avoid `instanceof` checks to decide behaviour unless explicitly required.
* Prefer simple loops over streams unless asked otherwise.
* Print small outputs in `run()` so behaviour can be verified quickly.
* Keep listener “feature code” inside listeners (not inside the queue).

## How to run

Each exercise includes an `Exercise` class with a static entry point:

```java
package t14_design_patterns_2.exercises.eXX;

public class Exercise {
    public static void run() {
        // call your methods here; print results for quick checks
    }
}
```

## Exercise 01 — ParserFactory by file extension (Factory)

**Objective:** Build a tiny factory that returns the correct `Parser` implementation based on a
file extension (e.g., `"csv"`, `"json"`). Functionally, your solution must support:

* creating the correct parser via `ParserFactory.createFor(extension)`,
* parsing two different inputs via two different parsers,
* demonstrating in `Exercise.run()` that client code **never** calls `new CsvParser()` / `new
  JsonParser()` directly.

### Required API

```java
interface Parser { int parseCount(String input); }

class CsvParser implements Parser { ... }
class JsonParser implements Parser { ... }

class ParserFactory {
    Parser createFor(String extension);
}
```

### Tasks

1. Implement `CsvParser.parseCount()` as: **count non-empty lines** (split by `\n`).
2. Implement `JsonParser.parseCount()` as: **count occurrences of '{'** (treat each `{` as a
   record start).
3. Implement `ParserFactory.createFor(extension)` (case-insensitive):

* `"csv"` to `CsvParser`
* `"json"` to `JsonParser`
  * otherwise throw `IllegalArgumentException("Unsupported extension: " + extension)`
1. In `Exercise.run()`:

   * create a factory,
   * request a csv parser, parse a sample csv string, print the count,
   * request a json parser, parse a sample json string, print the count.

### Sample output

```text
CSV count = 3
JSON count = 2
```

### Constraints

* No direct `new CsvParser()` / `new JsonParser()` in `run()`.

### Done when...

* The correct parser is selected purely by the factory.
* Unsupported extensions throw a clear exception.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t14_design_patterns_2.exercises.ex01;

public class Exercise {
    public static void run() {
        ParserFactory factory = new ParserFactory();

        Parser csv = factory.createFor("csv");
        String csvData = "a,b,c\n1,2,3\n4,5,6\n";
        System.out.println("CSV count = " + csv.parseCount(csvData));

        Parser json = factory.createFor("json");
        String jsonData = "{ \"id\": 1 }\n{ \"id\": 2 }\n";
        System.out.println("JSON count = " + json.parseCount(jsonData));
    }
}

interface Parser {
    int parseCount(String input);
}

class CsvParser implements Parser {
    @Override
    public int parseCount(String input) {
        if (input == null)
            return 0;

        String[] lines = input.split("\n");
        int count = 0;

        for (String line : lines)
            if (!line.trim().isEmpty())
                count++;

        return count;
    }
}

class JsonParser implements Parser {
    @Override
    public int parseCount(String input) {
        if (input == null)
            return 0;

        int count = 0;
        for (int i = 0; i < input.length(); i++)
            if (input.charAt(i) == '{')
                count++;

        return count;
    }
}

class ParserFactory {
    public Parser createFor(String extension) {
        if (extension == null || extension.isBlank())
            throw new IllegalArgumentException("extension is null/blank.");

        String ext = extension.trim().toLowerCase();

        if (ext.equals("csv"))
            return new CsvParser();

        if (ext.equals("json"))
            return new JsonParser();

        throw new IllegalArgumentException("Unsupported extension: " + extension);
    }
}
```

</details>

---

## Exercise 02 — EnemyFactory by difficulty (Factory)

**Objective:** Build an enemy factory that creates a “Slime” enemy differently depending on
difficulty. Functionally, your solution must support:

* selecting enemy configuration in the factory (not the game loop),
* creating `Enemy` instances via `EnemyFactory.createSlime(difficulty)`,
* showing different stats for `EASY` vs `HARD` in `run()`.

### Required API

```java
enum Difficulty { EASY, HARD }

interface Enemy { String name(); int hp(); int damage(); }

class EnemyFactory {
    Enemy createSlime(Difficulty difficulty);
}
```

### Tasks

1. Implement two enemy classes:

   * `Slime` (e.g., hp 20, damage 3)
   * `ArmouredSlime` (e.g., hp 45, damage 6)
2. Implement `EnemyFactory.createSlime(difficulty)`:

* EASY to `Slime`
* HARD to `ArmouredSlime`
1. In `run()` create and print both enemies.

### Sample output

```text
EASY -> Slime (hp=20, dmg=3)
HARD -> Armoured Slime (hp=45, dmg=6)
```

### Constraints

* Difficulty branching must happen in the factory, not in `run()` printing logic.

### Done when...

* Swapping the difficulty changes which class is created.
* The caller treats everything as `Enemy`.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t14_design_patterns_2.exercises.ex02;

public class Exercise {
    public static void run() {
        EnemyFactory factory = new EnemyFactory();

        Enemy easy = factory.createSlime(Difficulty.EASY);
        Enemy hard = factory.createSlime(Difficulty.HARD);

        System.out.println("EASY -> " + format(easy));
        System.out.println("HARD -> " + format(hard));
    }

    private static String format(Enemy e) {
        return e.name() + " (hp=" + e.hp() + ", dmg=" + e.damage() + ")";
    }
}

enum Difficulty {
    EASY,
    HARD
}

interface Enemy {
    String name();
    int hp();
    int damage();
}

class Slime implements Enemy {
    @Override
    public String name() {
        return "Slime";
    }

    @Override
    public int hp() {
        return 20;
    }

    @Override
    public int damage() {
        return 3;
    }
}

class ArmouredSlime implements Enemy {
    @Override
    public String name() {
        return "Armoured Slime";
    }

    @Override
    public int hp() {
        return 45;
    }

    @Override
    public int damage() {
        return 6;
    }
}

class EnemyFactory {
    public Enemy createSlime(Difficulty difficulty) {
        if (difficulty == null)
            throw new IllegalArgumentException("difficulty is null.");

        if (difficulty == Difficulty.EASY)
            return new Slime();

        return new ArmouredSlime();
    }
}
```

</details>

---

## Exercise 03 — Button click listeners (Observer)

**Objective:** Implement a `Button` that notifies multiple listeners when clicked.
Functionally, your solution must support:

* registering listeners (`addListener`),
* removing listeners (`removeListener`),
* notifying all listeners in registration order when `click()` is called,
* demonstrating add/remove behaviour in `run()`.

### Required API

```java
interface ClickListener { void onClick(); }

class Button {
    void addListener(ClickListener l);
    boolean removeListener(ClickListener l);
    void click();
}
```

### Tasks

1. Implement `Button` with an internal list of listeners.
2. Implement two listeners:

   * `SoundListener`: prints `SFX: click`
   * `AnalyticsListener`: prints `ANALYTICS: click`
3. In `run()`:

   * add both listeners, click twice,
   * remove one listener, click once more.

### Sample output

```text
SFX: click
ANALYTICS: click
SFX: click
ANALYTICS: click
ANALYTICS: click
```

### Constraints

* `Button` must not contain special-case code for specific listener types.

### Done when...

* Listeners can be added/removed without changing `Button`.
* Click calls notify all current listeners exactly once per click.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t14_design_patterns_2.exercises.ex03;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    public static void run() {
        Button button = new Button();

        ClickListener sfx = new SoundListener();
        ClickListener analytics = new AnalyticsListener();

        button.addListener(sfx);
        button.addListener(analytics);

        button.click();
        button.click();

        button.removeListener(sfx);

        button.click();
    }
}

interface ClickListener {
    void onClick();
}

class SoundListener implements ClickListener {
    @Override
    public void onClick() {
        System.out.println("SFX: click");
    }
}

class AnalyticsListener implements ClickListener {
    @Override
    public void onClick() {
        System.out.println("ANALYTICS: click");
    }
}

class Button {
    private List<ClickListener> _listeners = new ArrayList<>();

    public void addListener(ClickListener l) {
        if (l == null)
            throw new IllegalArgumentException("listener is null.");

        _listeners.add(l);
    }

    public boolean removeListener(ClickListener l) {
        if (l == null)
            throw new IllegalArgumentException("listener is null.");

        return _listeners.remove(l);
    }

    public void click() {
        for (ClickListener l : _listeners)
            l.onClick();
    }
}
```

</details>

---

## Exercise 04 — TemperatureSensor events with payload (Observer)

**Objective:** Implement a `TemperatureSensor` that publishes temperature changes to listeners
with a payload. Functionally, your solution must support:

* setting a temperature via `setTemperature(double)`,
* notifying all listeners with the *new* value,
* implementing two independent listeners (display + alarm),
* demonstrating in `run()` that adding listeners doesn’t require editing the sensor.

### Required API

```java
interface TemperatureListener { void onTemperatureChanged(double celsius); }

class TemperatureSensor {
    void addListener(TemperatureListener l);
    boolean removeListener(TemperatureListener l);
    void setTemperature(double celsius);
}
```

### Tasks

1. Create `ConsoleDisplayListener`: prints `TEMP: <value>C`
2. Create `OverheatAlarmListener(threshold)`: prints `ALARM: <value>C` only when value >= threshold
3. In `run()`:

   * add both listeners,
   * call `setTemperature()` with a few values (below and above threshold).

### Sample output

```text
TEMP: 35.0C
TEMP: 60.0C
ALARM: 60.0C
```

### Constraints

* Sensor doesn’t “know” what an alarm is; it only notifies listeners.

### Done when...

* Listeners receive the correct payload values.
* Alarm triggers only when threshold is crossed.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t14_design_patterns_2.exercises.ex04;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    public static void run() {
        TemperatureSensor sensor = new TemperatureSensor();

        sensor.addListener(new ConsoleDisplayListener());
        sensor.addListener(new OverheatAlarmListener(55.0));

        sensor.setTemperature(35.0);
        sensor.setTemperature(60.0);
        sensor.setTemperature(54.9);
        sensor.setTemperature(55.0);
    }
}

interface TemperatureListener {
    void onTemperatureChanged(double celsius);
}

class ConsoleDisplayListener implements TemperatureListener {
    @Override
    public void onTemperatureChanged(double celsius) {
        System.out.println("TEMP: " + celsius + "C");
    }
}

class OverheatAlarmListener implements TemperatureListener {
    private double _threshold;

    public OverheatAlarmListener(double threshold) {
        _threshold = threshold;
    }

    @Override
    public void onTemperatureChanged(double celsius) {
        if (celsius >= _threshold)
            System.out.println("ALARM: " + celsius + "C");
    }
}

class TemperatureSensor {
    private List<TemperatureListener> _listeners = new ArrayList<>();

    public void addListener(TemperatureListener l) {
        if (l == null)
            throw new IllegalArgumentException("listener is null.");

        _listeners.add(l);
    }

    public boolean removeListener(TemperatureListener l) {
        if (l == null)
            throw new IllegalArgumentException("listener is null.");

        return _listeners.remove(l);
    }

    public void setTemperature(double celsius) {
        notifyListeners(celsius);
    }

    private void notifyListeners(double celsius) {
        for (TemperatureListener l : _listeners)
            l.onTemperatureChanged(celsius);
    }
}
```

</details>

---

## Exercise 05 — LegacyLoggerAdapter (Adapter)

**Objective:** Adapt an incompatible legacy logger API to your project’s `Logger` interface.
Functionally, your solution must support:

* using `Logger.log(String)` from app code,
* delegating to `LegacyLogger.logMessage(String)` via an adapter,
* demonstrating in `run()` that app code depends only on `Logger`.

### Required API

```java
interface Logger { void log(String msg); }

class LegacyLogger { void logMessage(String msg); }

class LegacyLoggerAdapter implements Logger {
    LegacyLoggerAdapter(LegacyLogger legacy);
    void log(String msg);
}
```

### Tasks

1. Implement `LegacyLogger` (prints `LEGACY: <msg>`).
2. Implement `LegacyLoggerAdapter` forwarding `log()` to `logMessage()`.
3. In `run()` use `Logger` variable type and log two messages.

### Constraints

* Do not change `LegacyLogger` method names.

### Done when...

* App code compiles using only `Logger` and the adapter hides the legacy API.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t14_design_patterns_2.exercises.ex05;

public class Exercise {
    public static void run() {
        LegacyLogger legacy = new LegacyLogger();
        Logger logger = new LegacyLoggerAdapter(legacy);

        logger.log("Server started");
        logger.log("Player connected");
    }
}

interface Logger {
    void log(String msg);
}

class LegacyLogger {
    public void logMessage(String msg) {
        System.out.println("LEGACY: " + msg);
    }
}

class LegacyLoggerAdapter implements Logger {
    private LegacyLogger _legacy;

    public LegacyLoggerAdapter(LegacyLogger legacy) {
        if (legacy == null)
            throw new IllegalArgumentException("legacy is null.");

        _legacy = legacy;
    }

    @Override
    public void log(String msg) {
        _legacy.logMessage(msg);
    }
}
```

</details>

---

## Exercise 06 — GatewayCheckoutAdapter (Adapter)

**Objective:** Adapt a third-party payment gateway API to your `CheckoutService`. Functionally,
your solution must support:

* taking an `int cents` price in `CheckoutService.checkout(int cents)`,
* converting that to a euro string (e.g., `1299` to `"12.99"`),
* calling `ThirdPartyGateway.makePayment(String euroAmount)`,
* demonstrating success/failure cases in `run()`.

### Required API

```java
interface CheckoutService { boolean checkout(int cents); }

class ThirdPartyGateway { boolean makePayment(String euroAmount); }

class GatewayCheckoutAdapter implements CheckoutService {
    GatewayCheckoutAdapter(ThirdPartyGateway gateway);
    boolean checkout(int cents);
}
```

### Tasks

1. Implement `ThirdPartyGateway.makePayment(euroAmount)`:

   * return `false` if the string starts with `"0.00"` or equals `"0.00"`,
   * otherwise print `PAID: <euroAmount>` and return `true`.
2. Implement the adapter converting cents to euro string:

   * `int euros = cents / 100`, `int remainder = cents % 100`
* format remainder with 2 digits (e.g., 5 to `"05"`)
1. In `run()` attempt checkouts for `0`, `199`, `1299`, print the returned boolean each time.

### Sample output

```text
checkout(0) -> false
PAID: 1.99
checkout(199) -> true
PAID: 12.99
checkout(1299) -> true
```

### Constraints

* App code must depend on `CheckoutService`, not the gateway.
* Conversion logic lives in the adapter.

### Done when...

* Values are formatted correctly (`12.05`, not `12.5`).
* Gateway API is usable through the project interface without changing gateway code.

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">? Solution</summary>

```java
package t14_design_patterns_2.exercises.ex06;

public class Exercise {
    public static void run() {
        ThirdPartyGateway gateway = new ThirdPartyGateway();
        CheckoutService checkout = new GatewayCheckoutAdapter(gateway);

        System.out.println("checkout(0) -> " + checkout.checkout(0));
        System.out.println("checkout(199) -> " + checkout.checkout(199));
        System.out.println("checkout(1299) -> " + checkout.checkout(1299));
        System.out.println("checkout(1205) -> " + checkout.checkout(1205));
    }
}

interface CheckoutService {
    boolean checkout(int cents);
}

class ThirdPartyGateway {
    public boolean makePayment(String euroAmount) {
        if (euroAmount == null || euroAmount.equals("0.00"))
            return false;

        if (euroAmount.startsWith("0.00"))
            return false;

        System.out.println("PAID: " + euroAmount);
        return true;
    }
}

class GatewayCheckoutAdapter implements CheckoutService {
    private ThirdPartyGateway _gateway;

    public GatewayCheckoutAdapter(ThirdPartyGateway gateway) {
        if (gateway == null)
            throw new IllegalArgumentException("gateway is null.");

        _gateway = gateway;
    }

    @Override
    public boolean checkout(int cents) {
        if (cents < 0)
            throw new IllegalArgumentException("cents must be >= 0.");

        String euroAmount = formatEuro(cents);
        return _gateway.makePayment(euroAmount);
    }

    private String formatEuro(int cents) {
        int euros = cents / 100;
        int remainder = cents % 100;

        String rem = String.valueOf(remainder);
        if (remainder < 10)
            rem = "0" + rem;

        return euros + "." + rem;
    }
}
```

</details>
