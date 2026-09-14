---
title: "Equality & Hashing in Java"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t04_equality_hashing
description: "A practical introduction to identity vs value equality, the equals/hashCode contract, and how hashing works — without using collection types yet."
created: 2025-10-06
last_updated: 2026-04-14
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, equality, hashing, equals, hashcode, year2, comp-c8z03]
difficulty_tier: Foundation
mlos: [MLO2, MLO3]
previous_topic: t03_ordering
prerequisites:
  - Classes, objects, and Strings
  - Basic method overriding
---

# Equality & Hashing in Java

> **Prerequisites:**
> - Classes/objects; Strings; basic method overriding.

---

## In this topic

- [What you'll learn](#what-youll-learn)
- [Why this matters](#why-this-matters)
- [How this builds on previous content](#how-this-builds-on-previous-content)
- [Identity vs Value Equality](#identity-vs-value-equality)
- [The equals/hashCode contract (do not break this)](#the-equalshashcode-contract-do-not-break-this)
- [How hashing works (collection-independent)](#how-hashing-works-collection-independent)
- [Writing correct equals/hashCode](#writing-correct-equalshashcode)
- [What makes a good hash code?](#what-makes-a-good-hash-code)
- [Pitfalls to avoid](#pitfalls-to-avoid)
- [Worked example: duplicate leak and fix](#worked-example-duplicate-leak-and-fix)
- [Key takeaways](#key-takeaways)
- [Reflective questions](#reflective-questions)
- [Appendix A: Modulo arithmetic (a quick primer)](#appendix-a-modulo-arithmetic-a-quick-primer)

---

## What you'll learn

A quick summary of what **you** should be able to do after this lesson:

| Skill Type                  | You will be able to...                                                                                                                              |
| :-------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------ |
| Understand                    | Distinguish **identity** (`==`) from **value equality** (`equals`) and state the **equals/hashCode contract** clearly.                            |
| Understand                    | Explain, in plain terms, what **hashing** is and why it enables fast lookups.                                                                     |
| Use        | Compute simple **string hashes** (e.g., toy sum and rolling/polynomial) and combine **field hashes** for objects using the 31-multiplier pattern. |
| Use        | Map a hash code to a **bucket index** using `mod` or bit-masking and reason about **collisions** at a high level.                                 |
| Use              | Implement correct `equals` and `hashCode` for a small class, keeping fields consistent across both methods.                                       |
| Debug         | Detect and explain bugs caused by **mutable identifying fields** or inconsistent `equals`/`hashCode`.                                             |

> We will **not** use `HashSet`, `HashMap`, or any collection types here. Those appear in a
> later lesson.

---

## Why this matters

Java gives you two different questions that look like one: *are these the same object?*
and *do these two objects mean the same thing?* Confusing them produces bugs that are hard
to see, because the code reads correctly.

It matters most because the collections in the next topics are built directly on it. A
`HashSet` that will not de-duplicate, or a `HashMap` lookup that fails on an object you can
see in the map, is almost always a broken `equals` or `hashCode` — not a broken collection.

---

## How this builds on previous content

| Earlier topic | Concept carried forward |
| :- | :- |
| [t00 OOP Fundamentals](../t00_oop_fundamentals/t00_oop_fundamentals_notes.md) | Two references can point at one object — that is **identity**. This topic adds a second, weaker notion: equal *values* |
| [t00 Encapsulation](../t00_oop_fundamentals/t00_oop_fundamentals_notes.md) | Which fields are `private` and `final` now decides which fields may safely define identity |
| [t03 Ordering](../t03_ordering/t03_ordering_notes.md) | `compareTo` already compares objects; `equals` answers the narrower question "are these the same thing?" — and the two must agree |

Where this goes next: this topic is a hard prerequisite for [t07 Collections
III](../t07_collections_3_set_map/t07_collections_3_set_map_notes.md). `HashSet` and `HashMap`
are built directly on `equals`/`hashCode`, so a mistake here shows up there as duplicates that
will not de-duplicate and lookups that fail on objects demonstrably in the collection. It
returns again in [t23](../t23_unit_testing/t23_unit_testing_notes.md), where a JSON round-trip
test compares with `equals`.

---

## Identity vs Value Equality

- `==` checks **identity**: “Is this the exact same object in memory?”
- `equals` checks **value equality**: “Do these two objects represent the same value?”

```java
String a = new String("hello");
String b = new String("hello");

System.out.println(a == b);      // false (different objects)
System.out.println(a.equals(b)); // true (same characters)
```

Use `equals` when “sameness” is about data, not the instance.

---

## The equals/hashCode contract (do not break this)

If two objects are **equal** by `equals`, they **must** have the **same hash code**.

Why? Hash-based lookups group values by their hash codes before checking equality. If equal
objects produced different hash codes, lookups could fail.

**Key rules:**
- If `a.equals(b)` is true to `a.hashCode() == b.hashCode()` must be true.
- `equals` should be **reflexive, symmetric, transitive**, and **consistent**.
- `hashCode` should be stable for the object’s lifetime (don’t base it on fields you’ll mutate).

---

## How hashing works (collection-independent)

Hashing turns a value into an integer (**hash code**). A lookup table can use that integer to
quickly narrow down where a value might be stored.

Typical membership check idea:
1) Compute the hash code of the value.
2) Convert that hash to a **bucket index** within a fixed-size table.
3) Compare with `equals` only against the small group in that bucket to confirm.

This is fast because we avoid scanning all items. With a good hash function and a sensible
table size, lookups are close to constant time for practical purposes.

**Terminology (kept simple):**
- **Bucket**: a group where items that share an index are stored.
- **Collision**: different values landing in the same bucket (normal and handled by the table).
- **Resizing**: as items grow, the table may expand to keep buckets from getting crowded.

### Simple, concrete hashing examples (string to number)

These are **illustrations** to build intuition, not Java’s real implementation.

**A) Sum of character codes (toy example)**

```text
Let S = "CAB"
ASCII: 'C'=67, 'A'=65, 'B'=66
h = 67 + 65 + 66 = 198
BucketIndex = h mod TableSize
```

> This is easy to compute but not very robust (many collisions).

**B) Rolling (polynomial) hash (common pattern)**

```text
Given a base b (often 31) and starting h = 0:
for each character c in S:
    h = (b * h + c)

Example: S = "CAB", b = 31
Step 1 (C=67): h = 31*0 + 67 = 67
Step 2 (A=65): h = 31*67 + 65 = 2072
Step 3 (B=66): h = 31*2072 + 66 = 64238
BucketIndex = h mod TableSize
```

> Using a base and multiplication spreads values better than a plain sum.

### Object to number (combining field hashes)

For objects, we combine field hashes in a consistent way. A common pattern is a multiplier like 31:

```text
Let obj have fields: id, email
Let h_id = hash(id), h_email = hash(email)

h = 31 * h_id + h_email
BucketIndex = h mod TableSize
```

If an object has more fields, you keep folding them in with the same pattern:

```text
h = 31*h + hash(fieldN)
```

**Important:**
- Use the **same fields** here as you use in `equals`.
- Prefer fields that don’t change over the object’s lifetime (immutability).
- In real Java code, `Objects.hash(...)` or your IDE can generate a good implementation.

### 3.3 From hash code to bucket index

Tables use the hash code to pick a bucket:

```text
BucketIndex = h mod TableSize
```

We use this formula to map a large integer to a valid index range.

## Writing correct equals/hashCode

### Records (recommended when suitable)

Records provide **value-based equality and hash codes** automatically.

```java
public record Customer(String id, String email) { }

var c1 = new Customer("C1","a@x.com");
var c2 = new Customer("C1","a@x.com");
System.out.println(c1.equals(c2));   // true
System.out.println(c1.hashCode()==c2.hashCode()); // true
```

### Classes (override both consistently)

When using a regular class, override both methods using the **same identifying fields**.

```java
import java.util.Objects;

public final class Customer {
    private final String id;
    private final String email;

    public Customer(String id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

**Guidance**
- Prefer **immutable** identifying fields for equality (e.g., an ID).
- Use the **same fields** in `equals` and `hashCode`.
- Avoid basing equality on fields you plan to mutate.

---

## What makes a good hash code?

- Use `Objects.hash(...)` or your IDE’s generator for correctness and readability.
- Ensure fields used in `equals` are also used in `hashCode`.
- For performance-sensitive scenarios, hand-crafted functions are possible—but prioritize
  **correctness** and **distribution** at this stage.

**Example (hand-crafted)**

```java
@Override
public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (email != null ? email.hashCode() : 0);
    return result;
}
```

---

## Pitfalls to avoid

- **Overriding one without the other**: equal objects must share the same hash code.
- **Mutable keys**: changing fields used for equality after storing an object in a hashed
  structure can break lookups.
- **Inconsistent equality**: breaking symmetry or transitivity leads to hard-to-trace bugs.

---

## Worked example: duplicate leak and fix

Below we demonstrate the *principle* by comparing equality results directly—no collections
involved yet.

```java
class ProductBroken {
    String sku;
    ProductBroken(String sku) { this.sku = sku; }
    // No equals/hashCode → different objects are never "equal" by value
}

final class Product {
    private final String sku;
    Product(String sku) { this.sku = sku; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product other)) return false;
        return java.util.Objects.equals(sku, other.sku);
    }
    @Override public int hashCode() { return java.util.Objects.hash(sku); }
}

public class Demo {
    public static void main(String[] args) {
        var p1 = new ProductBroken("SKU-1");
        var p2 = new ProductBroken("SKU-1");
        System.out.println(p1.equals(p2)); // false (no value equality)

        var q1 = new Product("SKU-1");
        var q2 = new Product("SKU-1");
        System.out.println(q1.equals(q2)); // true (value equality based on sku)
        System.out.println(q1.hashCode()==q2.hashCode()); // true (contract holds)
    }
}
```

---

## Key takeaways

- `==` is identity; `equals` is value equality.
- If `equals` says two objects are equal, `hashCode` **must** be the same.
- Hashing converts values to integers so lookup tables can jump to candidate locations quickly.
- Prefer **records** for value types; for classes, override both methods with the same
  identifying fields.
- Keep identifying fields **immutable**.

---

## Reflective questions

- Which types in your current codebase should behave like **values** (same data to equal)?
- Which fields define identity for those types? Should they be immutable?
- Where might a hashed lookup table (covered next) be useful in your project?

<details style="background:#f5f7ff; border:1px solid rgba(0,0,0,0.15); border-radius:10px;
padding:0.9rem 1rem; margin:1rem 0;">
  <summary style="cursor:pointer; font-weight:800; margin:0;">
    Check your thinking
  </summary>

**Which types should behave like values.**
Ask: *if two instances hold the same data, should the program treat them as the same thing?* If
yes, it is a value type and needs `equals`/`hashCode`. Typical yes: `Money`, `Point`,
`EmailAddress`, `TrackId`, a DTO you deserialise from JSON and compare against an expected
object in a test. Typical no: `Server`, `ClientHandler`, `Scanner`, `Connection` — these have
identity and lifecycle, and two of them are never interchangeable even with identical fields.
The default `Object.equals` (reference identity) is the *correct* behaviour for that second
group; leave it alone.

**Which fields define identity, and should they be immutable.**
Only the fields that make the object *that* object — usually a natural or surrogate key. A
`Player` is identified by its `id`, not by its `score`, which changes constantly. Include the
minimum set that makes two distinct entities distinguishable.

They should be **immutable**, and this matters more than students expect. A hashed collection
files an object into a bucket using `hashCode()` *at the moment you insert it*. Mutate a field
used by `hashCode` afterwards and the object is now sitting in the wrong bucket — `contains(x)`
returns `false` for an object that is demonstrably in the set, and you cannot remove it:

```java
Set<Player> set = new HashSet<>();
Player p = new Player(1, "Ana");
set.add(p);
p.setId(2);                 // mutated a field used by hashCode
System.out.println(set.contains(p));  // false - it is in the set, but in the wrong bucket
```

Make key fields `final` and the problem cannot occur. This is the strongest practical argument
for `record`s, which give you correct `equals`/`hashCode` and immutability for free.

**Where a hashed lookup would help.**
Any place you currently do a linear scan to answer "have I seen this?" or "give me the one with
this key". Concretely: a "seen already" filter over incoming messages (`HashSet`), an in-memory
cache of entities keyed by ID so repeated `findById` calls skip the database (`HashMap<Integer,
Task>`), counting occurrences (`HashMap<String, Integer>`), or routing a request `type` string
to its handler instead of an `if`/`else` chain (`HashMap<String, Handler>` — the pattern used
in [t21](../t21_json_2_jackson_advanced/t21_json_2_jackson_advanced_notes.md)).

The performance argument is the point: scanning a `List` for a match is O(n) per lookup, so a
million lookups over a thousand items is a billion comparisons; a `HashMap` makes each lookup
O(1) on average. But that only holds if `hashCode` distributes well — a `hashCode` that returns
a constant is *legal* and turns every lookup back into a linear scan.

</details>

---

## Appendix A: Modulo arithmetic (a quick primer)

**Idea:** Modulo (“mod”) gives the **remainder** after division.
We write `a mod m` (in Java: `a % m`) and read it as “the remainder when `a` is divided by `m`.”

### Examples

- `13 mod 5 = 3` because `13 = 2×5 + 3`.
- `42 mod 10 = 2` to last digit of 42 is 2.
- `7 mod 7 = 0` to multiples of `m` have remainder 0.

### Why it matters here

- We turn a (possibly huge) hash code `h` into a valid **bucket index** by reducing it to the
  range `0..m-1` with `h mod m`.
- If the table size `m` is a **power of two**, `h mod m` can be computed as a fast **bit
  mask**: `h & (m - 1)`.

### Properties you’ll use

- **Range:** `0 = (a mod m) < m` for positive `a` and `m>0`.
- **Congruence:** If `a mod m == b mod m`, then `a` and `b` fall in the **same bucket** (same
  remainder).
- **Add/multiply:**
  `(a + b) mod m = ((a mod m) + (b mod m)) mod m`
  `(a × b) mod m = ((a mod m) × (b mod m)) mod m`

### Java specifics (sign of result)

- In Java, `a % m` has the **same sign as `a`**.
  E.g., `(-3) % 5 == -3`.
  For bucket indices you usually want non-negative, so use:

  ```java
  int idx = Math.floorMod(h, m);   // always 0..m-1
  ```

  or normalize manually:

  ```java
  int idx = (h % m + m) % m;
  ```

### Quick intuition

- Modulo is **wrap-around** arithmetic: counting on a clock is “mod 12”.
- We use it to wrap any integer hash into a **fixed index range** for array-backed tables.

---

## Lesson Context

```yaml
previous_lesson:
  topic_code: t03_ordering
  domain_emphasis: Balanced

this_lesson:
  topic_code: t04_equality_hashing
  primary_domain_emphasis: Balanced
  difficulty_tier: Foundation
mlos: [MLO2, MLO3]
```
