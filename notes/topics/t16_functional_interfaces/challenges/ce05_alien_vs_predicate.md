---
title: "Challenge Exercise: Alien vs Predicate — Shipboard Incident Triage"
subtitle: "Functional interfaces + Comparator factory + one design pattern"
description: "Build a playful incident triage system on a spaceship. Ingest JSON incidents, filter using predicates, prioritise using comparator factory, and execute responses using a design pattern from Design Patterns I/II."
created: 2026-02-11
generated_at: "2026-02-12T14:32:15Z"
version: 1.0
authors: ["OOP Teaching Team"]
tags: ["java", "challenge", "functional-interfaces", "java.util.function", "comparator", "collections", "design-patterns", "year2", "comp-c8z03"]
prerequisites: ["t15_functional_interfaces.md", "t13_design_patterns_2_exercises.md"]
---

# Challenge Exercise: Alien vs Predicate — Shipboard Incident Triage

## Scenario

The commercial towing ship **NOSTROMO-ISH** is returning home when **MOTHER** starts receiving
incident reports from a third-party “sensor aggregation service” (in JSON format).

Some incidents are harmless ship noise. Some are crew error. Some... look *alien*.

Your job is to build a small **incident triage** system that:

1. **loads** incidents from JSON,
2. **filters** them using configurable rules,
3. **prioritises** them using a chosen strategy,
4. **executes** a response plan made of small actions.

Keep it funny. Keep it professional. Treat the data as messy and untrusted.

---

## Starter Tasks

### Model the data (`Incident`)

Create a class to represent an incident report with fields such as:

- `id` (String)
- `timestampUtc` (long)
- `deck` (int)
- `zone` (String)
- `type` (String)
- `threat` (int 0–100)
- `confidence` (int 0–100)
- `notes` (String)

**Add methods** that help your triage logic (examples):
- Validate ranges (`threat`, `confidence`)
- Normalize strings (trim/blank handling)
- A readable `toString()` for log output

---

### Choose a collection you’ve studied and justify it

Pick **one** primary structure to organise your work:

- `ArrayList<Incident>` for the full feed + `Map<String, List<Incident>>` grouped by zone
- `LinkedList<Incident>` as a processing queue (FIFO)
- `Map<String, List<Incident>>` as the primary store (zone to incidents)
- `Set<String>` to deduplicate incident IDs (recommended if your feed can repeat)

Write a 1–2 line comment explaining why your choice fits your design.

---

### Functional interfaces: rule then score then action

You must use:

- `Predicate<Incident>` for “is this relevant / suspicious?”
- `Function<Incident, ?>` for extracting values (key selection, scoring, categorisation)
- `Consumer<Incident>` (or `BiConsumer<K, V>`) for side effects (logging, counters, state updates)
- A **Comparator factory** (see below)

**Important:** Predicates should be *pure* (no mutation). Put side effects in Consumers.

---

### Comparator factory (required)

Create a small factory that returns a `Comparator` based on a mode string (or enum).
It must support at least **3** prioritisation modes, for example:

- `"threat_desc"`
- `"confidence_desc"`
- `"timestamp_desc"`
- `"zone_then_threat"` (bonus)

You must demonstrate changing the mode changes the order of incidents shown in the report.

---

### Design pattern requirement (pick ONE)

Your solution must clearly apply **one** design pattern from Design Patterns I or II notes.
Choose **Strategy** or **Command**, but do **not** use both.

- **If you choose Strategy:** it must switch between **at least two** different triage policies
  (e.g., “Quarantine Mode” vs “Stealth Mode”) without editing the core triage engine.
- **If you choose Command:** it must represent responses as objects that can be **queued** and
  executed without putting special-case logic in your engine.

Write a short comment in code explaining where the pattern lives and why.

---

### Demonstrate your triage in `run()`

In your `Exercise.run()` demonstrate:

1. Load a list of incidents (from the JSON helper or local fallback).
2. Apply your **Predicate** to select which incidents matter.
3. Prioritise them using your **Comparator factory**.
4. Apply **actions** (Consumers or pattern-based actions) to produce an outcome.
5. Print a small triage report.

---

## Required outputs (what your run should show)

Your printed output must include:

- Total incidents loaded
- Total incidents discarded by rules
- Top 5 incidents after sorting (with key details)
- At least one grouped summary (e.g., incidents per zone, or highest-risk zone)
- A clear indication of what “response” happened (without spamming console)

---

## Hints

- Start by hard-coding 6–10 sample incidents **in code**, then swap to JSON once the logic works.
- Treat the JSON feed as untrusted: expect missing fields, blanks, duplicates, and weird ranges.
- Keep your comparator modes small and test them with the same dataset.
- If your system grows, ask: “What should be a pure function?” vs “What should cause side effects?”
- Keep selection logic out of your core engine (avoid one giant `switch` that does everything).

---

## Extensions (optional)

- Add a “manual review” lane: incidents that are not discarded, but not actionable.
- Add a deduplication rule: ignore incidents with repeated IDs.
- Add a “cooldown”: if the same zone triggers 3 incidents in a short window, escalate.
- Add a second data source (another JSON feed) and merge results.
- Add a tiny UI: print a menu allowing the user to choose policy + comparator mode.

---

## Utility: Loading incidents from JSON (starter helper)

You may use a helper class to load incidents from a remote URL that returns JSON.
If you don’t want networking in your submission, provide a fallback that loads from a local
JSON string/file.

**You must not rely on perfect input.** Validate and skip bad incidents.

Suggested helper API:

```java
import java.util.List;

class IncidentFeedClient {
    static List<Incident> fetch(String url);
}
```

Suggested behaviour:
- return an empty list on failure (and print a single warning)
- skip invalid incidents rather than crashing the whole run

---

## Real-World Parallels: How this maps to incident triage

This challenge is playful, but the architecture mirrors real incident triage systems
(monitoring + alerting + response).

| In-story concept | Real-world analogue | What the concept represents |
| :- | :- | :- |
| Incident report | Event / alert | A unit of incoming telemetry that may need attention |
| Remote JSON feed | External telemetry | Untrusted third-party data source |
| Zone / Deck | Service / subsystem / region | Where the issue is “owned” or located |
| Threat + Confidence | Severity + confidence | Two key signals used to triage |
| Rule tests | Filtering rules | Decide what is relevant/suspicious |
| Risk profiling | Scoring/enrichment | Transform raw data into a decision-friendly form |
| Comparator modes | Priority policies | Different ways to sort urgency |
| Response steps | Runbook/playbook actions | Concrete actions taken after triage |
| Action queue | Work queue | Sequenced execution of response work |

| Pattern in your solution | Real-world analogue | Why teams use it |
| :- | :- | :- |
| Strategy (if chosen) | Policy profile | Swap triage rules/priorities per mode without rewriting the engine |
| Command (if chosen) | Playbook steps | Queue/execute response actions cleanly without hard-coding behaviour |
| Comparator factory | Priority policy selector | Select a sorting policy at runtime by mode |

| Functional interface idea | Real-world analogue | Meaning |
| :- | :- | :- |
| Predicate | Rule | Decide yes/no about an incident |
| Function | Enrichment/extraction | Compute a derived value used by triage |
| Consumer / BiConsumer | Action | Perform an effect (log, count, update state) |
| Supplier | Default provider | Create defaults when data/state is missing |

## Appendix B: Gson helper for reading JSON from a file

This appendix shows one simple way to load a JSON **array of objects** from disk using **Gson**.
The demo uses a `Book` class so you can test the helper without touching your Alien challenge code.

### Setup (Maven / pom.xml)

Add this dependency inside `<dependencies>`:

```xml
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.11.0</version>
</dependency>
```

Reload Maven in IntelliJ:
- Maven tool window to **Reload All Maven Projects**, or
- right-click `pom.xml` then **Maven** then **Reload project**

### Create the demo JSON file

Create this file in your project:

- `data/books_12_sample.json`

Example contents:

```json
[
  { "_title": "Dune", "_author": "Frank Herbert", "_year": 1965 },
  { "_title": "The Left Hand of Darkness", "_author": "Ursula K. Le Guin", "_year": 1969 },
  { "_title": "Neuromancer", "_author": "William Gibson", "_year": 1984 },
  { "_title": "Snow Crash", "_author": "Neal Stephenson", "_year": 1992 },
  { "_title": "Foundation", "_author": "Isaac Asimov", "_year": 1951 },
  { "_title": "I, Robot", "_author": "Isaac Asimov", "_year": 1950 },
  { "_title": "Do Androids Dream of Electric Sheep?", "_author": "Philip K. Dick", "_year": 1968 },
  { "_title": "The Martian", "_author": "Andy Weir", "_year": 2011 },
  { "_title": "Project Hail Mary", "_author": "Andy Weir", "_year": 2021 },
  { "_title": "The Hobbit", "_author": "J. R. R. Tolkien", "_year": 1937 },
  { "_title": "1984", "_author": "George Orwell", "_year": 1949 },
  { "_title": "Brave New World", "_author": "Aldous Huxley", "_year": 1932 }
]
```

### Helper: GsonFileReader

```java
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GsonFileReader<T> {
    private final Gson _gson;
    private final Class<T> _elementType;

    /// <summary>
    /// Creates a JSON file reader that can parse a JSON array into a List of <typeparamref name="T"/>.
    /// </summary>
    /// <param name="elementType">The element class (e.g., Book.class).</param>
    public GsonFileReader(Class<T> elementType) {
        if (elementType == null)
            throw new IllegalArgumentException("elementType is null.");

        _elementType = elementType;
        _gson = new Gson();
    }

    /// <summary>
    /// Reads a JSON file containing a JSON array and returns a list of <typeparamref name="T"/>.
    /// </summary>
    /// <param name="path">Path to the JSON file.</param>
    /// <returns>A new ArrayList containing the loaded items (empty if file is blank).</returns>
    public List<T> readList(Path path) {
        if (path == null)
            throw new IllegalArgumentException("path is null.");

        if (!Files.exists(path))
            throw new IllegalArgumentException("file does not exist: " + path);

        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);

            if (json == null || json.isBlank())
                return new ArrayList<>();

            Type listType = TypeToken.getParameterized(List.class, _elementType).getType();
            List<T> data = _gson.fromJson(json, listType);

            if (data == null)
                return new ArrayList<>();

            return new ArrayList<>(data);
        } catch (IOException ex) {
            throw new IllegalArgumentException("failed to read json: " + path, ex);
        }
    }
}
```

### Demo usage (Book)

```java
import java.nio.file.Path;
import java.util.List;

public class DemoBooksFromFile {
    public static void main(String[] args) {

        //helper to tell you what directory to put your data file in
        System.out.println("Put your data file in: " + System.getProperty("user.dir"));

        GsonFileReader<Book> reader = new GsonFileReader<>(Book.class);
        List<Book> books = reader.readList(Path.of("data/books_12_sample.json"));

        System.out.println("Loaded: " + books.size());
        for (int i = 0; i < books.size() && i < 3; i++)
            System.out.println(books.get(i));
    }
}

class Book {
    private String _title;
    private String _author;
    private int _year;

    public Book() { }

    public String getTitle() { return _title; }
    public String getAuthor() { return _author; }
    public int getYear() { return _year; }

    @Override
    public String toString() {
        return _title + " (" + _year + "), by " + _author;
    }
}
```
