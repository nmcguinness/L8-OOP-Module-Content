---
title: Defensive Coding in Java
topic_code: shared-general
related_module: COMP C8Z03
intended_audience: Year 2 Software Development Students
pedagogical_focus: Reliability, Validation, Error Prevention
integration_context: Applied in Stage 2 CSV and Entity Validation of OOP CA
author: Lecturer in OOP / DkIT
version: 1.0
academic_year: 2025-26
license: CC BY-NC-SA 4.0
file_name: defensive-coding-notes.md
---

# Defensive Coding in Java

## Overview

Defensive coding means **writing code that assumes things can go wrong** — invalid data, nulls,
missing files, or bad input.
It’s not about fear; it’s about responsibility.

Professional developers build software that **handles errors gracefully** and **protects data
integrity**.

In your OOP CA, defensive coding ensures your CSV loader and entity setters stay stable even
when the data isn’t perfect.

## Why It Matters

- Prevents crashes and unpredictable behaviour.
- Builds user and team trust in your code.
- Helps you identify and skip invalid data.
- Essential when reading or validating external data (CSV, JSON, user input).

## Common Defensive Patterns

| Pattern | Description | Example |
|:--|:--|:--|
| **Validate early** | Check inputs before use. | `if (name == null \|\| name.isBlank()) return;` |
| **Fail fast** | Exit quickly on invalid input. | `if (price < 0) return;` |
| **Safe defaults** | Substitute safe fallback values. | `discount = Math.max(0, discount);` |
| **Protect state** | Validate setters and keep fields private. | `setEmail(email)` with `contains("@")` check. |
| **Continue safely** | Skip bad data but keep program running. | Skip invalid CSV rows. |

## Example – Defensive CSV Loader

```java
String[] parts = line.split(",");
if (parts.length != EXPECTED_COLUMNS) {
    System.out.println("Invalid row: " + line);
    continue;
}
```

Even when some rows are invalid, the program continues loading the rest safely.

## Try It / Fix It

1. Review your CA’s `CsvLoader` class.
   What happens if a line is empty or has too few columns?
   Add a check to skip and log these lines.

2. Add one new setter to your entity that includes validation (e.g., reject negative IDs).

## Reflection

Why is defensive coding about **thinking ahead**, not just adding `if` statements?
Consider in one sentence how it improves user experience and code reliability.

## Further reading

- [Medium – Code with Confidence — Part 1](https://refactoring.guru/refactoring)
- [Oracle Java Tutorials – Exceptions and Defensive
  Programming](https://docs.oracle.com/javase/tutorial/essential/exceptions/)

## Appendix A: Defensive Patterns

Each snippet is tiny, explicit, and avoids advanced Java features.

### 1) Guard clauses (fail fast, read easier)

```java
static String normalizeName(String raw) {
    if (raw == null) return "";           // safe default
    String s = raw.trim();
    if (s.isEmpty()) return "";           // early exit
    // happy path
    return s.substring(0, 1).toUpperCase() + s.substring(1);
}
```

### 2) Validate arguments (clear messages)

```java
static void requirePositive(int value, String fieldName) {
    if (value <= 0) throw new IllegalArgumentException(fieldName + " must be > 0 but was " + value);
}
// usage in a setter/ctor:
requirePositive(quantity, "quantity");
```

### 3) Setter with trimming + basic format check

```java
public void setEmail(String email) {
    if (email == null) throw new IllegalArgumentException("email is required");
    String e = email.trim();
    if (e.isEmpty() || !e.contains("@")) throw new IllegalArgumentException("email looks invalid: " + email);
    this.email = e.toLowerCase(); // safe normalization
}
```

### 4) Constructor invariants (reject bad objects early)

```java
public final class Customer {
    private final String id;
    private final String email;
    public Customer(String id, String email){
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("bad email");
        this.id = id.trim();
        this.email = email.trim().toLowerCase();
    }
    // getters...
}
```

### 5) Safe integer parsing with default (no crash on bad CSV)

```java
static int parseIntOr(String s, int fallback){
    if (s == null) return fallback;
    try { return Integer.parseInt(s.trim()); }
    catch (NumberFormatException ex){ return fallback; }
}
```

### 6) CSV line validation (column count + line numbers)

```java
static void loadCsv(java.io.Reader reader, int expectedCols, java.util.List<String[]> out) throws java.io.IOException {
    try (var br = new java.io.BufferedReader(reader)) {
        String line; int lineNo = 0;
        while ((line = br.readLine()) != null) {
            lineNo++;
            if (line.isBlank()) continue;                    // skip empties
            String[] parts = line.split(",", -1);            // keep empties
            if (parts.length != expectedCols) {
                System.out.println("[WARN] line " + lineNo + ": expected " + expectedCols + " cols, got " + parts.length + " → skipped");
                continue;
            }
            out.add(parts);
        }
    }
}
```

### 7) Bounds helpers (1D and 2D)

```java
static boolean inBounds(int[] a, int i){ return a != null && i >= 0 && i < a.length; }
static boolean inBounds2D(int[][] g, int r, int c){
    return g != null && r >= 0 && r < g.length && g[r] != null && c >= 0 && c < g[r].length;
}
```

### 8) Defensive copies (do not leak mutable arrays/lists)

```java
// store a copy
public Order(String[] items) { this.items = items == null ? new String[0] : items.clone(); }
// return a copy
public String[] getItems() { return items.clone(); }
```

### 9) Switch with safe default (unknown category)

```java
static int categoryDiscount(String cat){
    if (cat == null) return 0;
    switch (cat.trim().toLowerCase()) {
        case "student": return 10;
        case "senior": return 15;
        default: return 0;      // unknown → no discount
    }
}
```

### 10) Validate before mutate (setter pattern)

```java
public void setPrice(double price){
    if (Double.isNaN(price) || price < 0.0) throw new IllegalArgumentException("price must be >= 0");
    this.price = price;
}
```

### 11) Tiny assertion helper for manual checks (no JUnit)

```java
static void check(boolean condition, String message){
    if (!condition) throw new IllegalStateException("Check failed: " + message);
}
// usage:
check(total >= 0, "total should not be negative");
```

### 12) Log context on failure (cheap trace)

```java
static void warn(String where, String msg, String data){
    System.out.println("[WARN] " + where + " — " + msg + " | data='" + data + "'");
}
// usage:
warn("CsvLoader#parseRow", "email missing '@'", rawLine);
```

### 13) Null-object style default (avoid null returns)

```java
static String safeString(String s){ return s == null ? "" : s; }
```

### 14) Range clamping (keep values in a safe window)

```java
static int clamp(int value, int min, int max){
    if (min > max) throw new IllegalArgumentException("min > max");
    if (value < min) return min;
    if (value > max) return max;
    return value;
}
```

### 15) Duplicate detection before insert (by key)

```java
static boolean existsById(java.util.ArrayList<Customer> list, String id){
    if (id == null) return false;
    for (Customer c : list) if (c != null && id.equals(c.getId())) return true;
    return false;
}
```

### 16) Try-with-resources for file IO (auto-close even on error)

```java
static java.util.List<String> readAllLines(java.nio.file.Path path) throws java.io.IOException {
    try (var br = java.nio.file.Files.newBufferedReader(path)) {
        var lines = new java.util.ArrayList<String>();
        for (String line; (line = br.readLine()) != null; ) lines.add(line);
        return lines;
    }
}
```

### 17) Enum parsing with fallback (no crash on typos)

```java
enum Status { NEW, ACTIVE, SUSPENDED }
static Status parseStatusOr(String s, Status fallback){
    if (s == null) return fallback;
    try { return Status.valueOf(s.trim().toUpperCase()); }
    catch (IllegalArgumentException ex){ return fallback; }
}
```

### 18) Numeric policy: sentinel vs exception

```java
// Option A: safe default
static double divideOrZero(double a, double b){
    if (b == 0) return 0.0;
    return a / b;
}
// Option B: strict (fail fast)
static double divideStrict(double a, double b){
    if (b == 0) throw new IllegalArgumentException("division by zero");
    return a / b;
}
```

> Pick **one** policy per method and **document it** in Javadoc or comments.

### 19) Reusable CSV cell getters (trim + empty to null)

```java
static String cell(String[] row, int i){
    if (row == null || i < 0 || i >= row.length) return null;
    String s = row[i];
    if (s == null) return null;
    s = s.trim();
    return s.isEmpty() ? null : s;
}
```

### 20) Micro “row to object” mapper with validation

```java
static Customer toCustomer(String[] row){
    String id = cell(row, 0);
    String email = cell(row, 1);
    if (id == null) throw new IllegalArgumentException("id missing");
    if (email == null || !email.contains("@")) throw new IllegalArgumentException("email invalid");
    return new Customer(id, email);
}
```
