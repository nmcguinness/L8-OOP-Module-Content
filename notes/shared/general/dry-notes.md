---
title: DRY Principle in Java OOP
topic_code: shared-general
related_module: COMP C8Z03
intended_audience: Year 2 Software Development Students
pedagogical_focus: Code Quality, Reuse, Refactoring
integration_context: Reinforced in Stage 1 design and Stage 2 refactoring of OOP CA
author: Lecturer in OOP / DkIT
version: 1.0
academic_year: 2025-26
license: CC BY-NC-SA 4.0
file_name: dry-notes.md
---

# The DRY Principle in Java OOP

## Overview

The **DRY principle — Don’t Repeat Yourself** — helps you write Java code that’s clean,
efficient, and easy to maintain.

When code is duplicated, it quickly gets inconsistent: one version changes, the other doesn’t.

Following DRY encourages **clarity**, **reuse**, and **structure**, which are essential in your
**OOP CA** when designing entity classes, validation helpers, and report logic.

## Why DRY Matters

- Reduces duplication and inconsistency.
- Makes maintenance and debugging easier.
- Simplifies testing and validation.
- Encourages modular, reusable design.

## Before and After Example

**Before (duplication):**

```java
if (price < 0 || price > 999) System.out.println("Invalid price");
if (discount < 0 || discount > 50) System.out.println("Invalid discount");
```

**After (DRY refactor):**

```java
private boolean isValidRange(double value, double min, double max) {
    return value >= min && value <= max;
}
```

Now, one helper method handles all range checks.

## Common DRY Techniques

| Technique | Example | Description |
|:--|:--|:--|
| **Helper methods** | `isValidName(name)` | Centralise repeated validation logic. |
| **Constants** | `private static final int MAX_SCORE = 100;` | Avoid hard-coded numbers or strings. |
| **Utility classes** | `DataUtils`, `Validator` | Keep shared logic reusable. |
| **Inheritance/Composition** | `BaseEntity` class | Move shared behaviour to a common class. |

## Try It / Fix It

1. Identify duplication in this snippet:

   ```java
   if (score > 100) System.out.println("Too high");
   if (level > 100) System.out.println("Too high");
   ```

   Refactor it to apply DRY.

2. Review your CA code: where could two similar methods be combined?

## Reflection

How does applying DRY make your project easier to debug and maintain?
Write one example from your current CA where duplication could cause future errors.

## Further reading

- [Geeks for Geeks – DRY Principle in Software
  Development)](https://www.geeksforgeeks.org/software-engineering/dont-repeat-yourselfdry-in-software-development/)
- [Refactoring Guru – Don’t Repeat Yourself](https://refactoring.guru/refactoring)
