---
title: "Equality & Hashing — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t04_equality_hashing
description: "Eight exercises on the equals/hashCode contract, hand-computed hashes, bucket indexing, collision reasoning, and the mutability pitfall."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, equality, hashing, equals, hashcode, exercises, year2, comp-c8z03]
---
# Equality & Hashing — Exercises

> These exercises build directly on the **Equality & Hashing — Foundations** notes. Work from
> top to bottom.
>
> Most tasks include small, checkable outputs. Use the provided method signatures and add tiny
> tests in `main` or `Exercise.run()`.
>
> Hints and snippets you can reuse are in the notes (e.g., 31-multiplier pattern,
> `Objects.hash`, and `Math.floorMod`).

## How to run

Each exercise assumes a package like:

```text
t04_equality_hashing.exercises.exNN
```

Create a class `Exercise` in that package with a static entry point:

```java
package t04_equality_hashing.exercises.ex01;

public final class Exercise {
    public static void run() {
        // call your methods here; print results for quick checks
    }
}
```

Call `Exercise.run()` from your `Main`, as shown in the repo README.

---

## Exercise 01 — Identity vs Equality (warm-up)

**Objective:** Distinguish identity (`==`) from value equality (`equals`).
**Description:** Create **two distinct** `String` objects with the same text and compare using
both `==` and `.equals(...)`. Summarise the difference in your own words.
- In `run()`:

  ```java
  String a = new String("hello");
  String b = new String("hello");
  System.out.println(a == b);       // expect false
  System.out.println(a.equals(b));  // expect true
  // write 1–2 lines explaining identity vs value equality
  ```

---

## Exercise 02 — The equals/hashCode Contract

**Objective:** State and apply the contract correctly.
**Description:** Write a short bullet list of the contract (reflexive, symmetric, transitive,
consistent, non-null). Provide a tiny counterexample that **breaks symmetry** and explain why
it’s dangerous.
- Deliverables:
  - 3–5 bullet points describing the contract.
  - 5–10 lines of Java (or pseudocode) that break symmetry, plus a 1-sentence risk explanation.

---

## Exercise 03 — Hand Hash (string to number, polynomial)

**Objective:** Compute a rolling (polynomial) hash by hand.
**Description:** Using base `b = 31`, compute the hash for `S = "CAB"` step-by-step and report
the final value.
- Work:

  ```text
  h0 = 0
  h1 = 31*h0 + 'C'(67) = ?
  h2 = 31*h1 + 'A'(65) = ?
  h3 = 31*h2 + 'B'(66) = ?  // final
  ```

- Report `h3`.

---

## Exercise 04 — Hand Hash (object fields to number)

**Objective:** Combine field hashes consistently with equality.
**Description:** Given `hash("C1") = 20123` and `hash("a@x.com") = 881234`, compute:

```text
h = 31 * hash("C1") + hash("a@x.com")
```

- Then, in 2–3 sentences, explain why the **same fields** used in `equals` must be used in
  `hashCode` and why immutability helps.

---

## Exercise 05 — From Hash to Bucket Index

**Objective:** Map a hash to a valid array index using modulo and masking.
**Description:** Using your `h3` from Exercise 03, compute indices for table sizes `m = 16` and
`m = 32` using both `h % m` and `h & (m-1)` (assume `h = 0`). If `h` could be negative, show
the expression with `Math.floorMod(h, m)`.
- Quick checks:

  ```java
  int m16 = 16, m32 = 32;
  int idx16_mod = h % m16;
  int idx16_mask = h & (m16 - 1);
  int idx32_mod = h % m32;
  int idx32_mask = h & (m32 - 1);
  int idxSafe = Math.floorMod(h, m16);
  // Print all indices to verify
  ```

---

## Exercise 06 — Implement equals/hashCode (class)

**Objective:** Write consistent overrides based on identifying fields.
**Description:** Implement a `final class Customer` with fields `id` and `email`. Two customers
are equal if their **id** is equal. Implement `hashCode` consistently. Add a tiny `main` (or
`run`) that shows two equal customers produce `equals == true` and equal hash codes.
- Skeleton:

  ```java
  public final class Customer {
      private final String id;
      private final String email;
      // ctor, getters

      @Override public boolean equals(Object o) { /* id-based */ }
      @Override public int hashCode() { /* Objects.hash(id) */ }
  }
  ```

---

## Exercise 07 — Collision Reasoning (toy hash)

**Objective:** Reason about collisions and hash quality.
**Description:** For the **toy** hash `h_sum(S) = sum of ASCII codes`, give **two different**
3-letter strings that produce the **same** result. Show your working. In one sentence, explain
why this toy hash is a poor real-world choice.

---

## Exercise 08 — Mutability Pitfall

**Objective:** Explain why changing the fields that are used to generate a hash is dangerous.
**Description:** Create a class `Tag` with a mutable `name` and write `equals`/`hashCode` based
on `name`. In 1–2 sentences, explain what could go wrong if this object were used in a hashed
lookup and `name` changes after insertion (we’ll implement collections later).
