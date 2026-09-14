---
title: "Challenge Exercise: Cargo Manifest"
topic: "Generics"
language: "Java"
estimated_lab_time: "2–3 weeks (approx. 2–3 hours active coding)"
prerequisites:
  - "Classes & objects"
  - "ArrayList/List basics"
  - "Inheritance (abstract classes)"
  - "Methods + overloading"
generated_at: "2026-01-27T09:44:42+00:00"
---

# Challenge Exercise: Cargo Manifest - The Type-Safe Warehouse

## Scenario

A shared warehouse supports two internal teams:

- **Software Development** stores real-world stock items (electronics, furniture, etc.).
- **Games Development** stores digital stock items (collectibles, skins, DLC bundles, etc.).

Last semester, the warehouse system was built in a hurry and uses **raw types** and “anything
goes” lists. It *works*... right up until someone ships the wrong type of item to the wrong
place and the system falls over at runtime.

Your job is to rebuild the core pieces of the warehouse so that:

- you get **compiler errors** instead of runtime surprises, and
- shipping items between lists is done **safely**, using the **PECS** rule (*Producer Extends,
  Consumer Super*).

---

## System overview

This challenge builds a small “type-safe warehouse” where **items** live in **inventories**,
and **utilities** operate on those inventories.

### Core entities

- `Item` (abstract)
  - The common contract for anything the warehouse can store and ship.
  - Provides `getName()`, `getCategory()`, and `getPrice()` for consistent reporting.
- `Electronics`, `Furniture`, `GameCollectible` (concrete `Item` types)
  - Real product categories (SD) and digital item categories (GD).
- `StorageBox<T>`
  - A single-slot container that demonstrates *compile-time* type safety.
  - You can store **exactly one type** per box (e.g., `StorageBox<Integer>` for serial numbers).
- `InventoryUtils`
  - A small utility class that performs generic operations:
    - `calculateTotalValue(List<T extends Item>)` totals prices across a list
    - `shipItems(source, destination)` moves items safely using PECS wildcards
- `WarehouseApp`
  - The demo runner that assembles items, prints manifests, and performs shipments.

### How these pieces interact

- `WarehouseApp` creates `Item` objects (e.g., `Electronics`) and places them into inventories
  (`List<Electronics>`, `List<Item>`, etc.).
- `InventoryUtils.calculateTotalValue(...)` treats an inventory as a **producer** of `Item`
  values and reads prices safely.
- `InventoryUtils.shipItems(...)` treats the source inventory as a **producer** (`? extends
  Item`) and the destination as a **consumer** (`? super Item`) to move items without casts.
- `StorageBox<T>` is used early to show how generics prevent “wrong type, wrong place” errors
  at compile-time.

### Typical use case

A staff member (or automated tool) performs three common actions:

1. **Register items** (create objects and add them to department inventories)
2. **Audit value** (print a manifest and total up the inventory value)
3. **Ship items** (move items from a specialised department list into a central warehouse list)

## Warehouse rules

1. A **storage box** must only ever contain **one specific type** of item.
2. An **inventory list** must only ever contain **one specific family** of items.
3. Shipping must move items **from a producer list** to a **consumer list** without unsafe casting.
4. You must demonstrate your system using *both* contexts:
   - at least one “real product” category (SD focus)
   - at least one “game item” category (GD focus)

---

## Manifest format (for demo output)

When printing an inventory or shipment, use a consistent single-line “manifest” format so your
output is easy to check.

Each line must follow this pattern:

```text
CAT | NAME | PRICE
```

Example:

```text
ELECTRONICS | USB-C Charger | 19.99
FURNITURE | Oak Stool | 79.50
GAME_COLLECTIBLE | Golden Coin Skin | 4.99
```

Notes:
- `CAT` is an uppercase category label you choose (keep it consistent).
- `PRICE` is a `double` formatted to 2 decimal places.

---

## Starter Tasks

### Part 1: `StorageBox<T>` (Week 1)

**Goal:** Create a tiny generic class that proves type safety.

1. **Create the class `StorageBox<T>`**
   - Private field: `item` of type `T`
   - Constructor to set the item
   - Methods:
     - `setItem(T item)`
     - `getItem() : T`

2. **Create `WarehouseApp` with a `main` method**
   - Create:
     - `StorageBox<String>` holding a product name
     - `StorageBox<Integer>` holding a serial number
   - Print both values.

3. **Type safety check (required)**
   - Try to store a `String` inside the `StorageBox<Integer>`.
   - Confirm you get a **compile-time error**.
   - Leave a short comment explaining what the compiler is protecting you from.

**Deliverable for Week 1**
- `StorageBox.java`
- `WarehouseApp.java` with a simple demo

---

### Part 2: Item hierarchy + bounded generics (Week 2)

**Goal:** Use an inheritance hierarchy and a bounded type parameter.

1. **Create an `Item` hierarchy**
   - Abstract base class `Item`:
     - `public abstract double getPrice();`
     - `public abstract String getName();`
     - `public abstract String getCategory();` (for manifest printing)
   - Create at least **two** concrete subclasses for real products:
     - `Electronics`
     - `Furniture`

2. **Add meaningful state**
   - Each concrete class should have real fields (not hard-coded prices).
   - Example ideas:
     - `Electronics`: `name`, `wattage`, `price`
     - `Furniture`: `name`, `material`, `price`

3. **Write a bounded generic method**
   - In a utility class, implement:

```java
public static <T extends Item> double calculateTotalValue(List<T> items) {
    // sum item.getPrice()
    return 0.0;
}
```

1. **Demonstrate it**
   - Create a `List<Electronics>` with at least **3 items**
   - Print each item as a manifest line
   - Print the total value clearly

**Deliverable for Week 2**
- `Item.java`, `Electronics.java`, `Furniture.java`
- `calculateTotalValue(...)` implemented + demonstrated

---

### Part 3: Shipping with wildcards + PECS (Week 3)

**Goal:** Move items between collections safely using wildcards.

1. **Add at least one Games Development item subtype**
   - Create a class like `GameCollectible` that extends `Item`.
   - It should have enough state to print a meaningful manifest line.

2. **Write the shipping method**
   - Use a signature that applies PECS correctly:

```java
public static void shipItems(List<? extends Item> source, List<? super Item> destination) {
    // TODO
}
```

1. **Implement the move**
   - Move all items from `source` to `destination`.
   - Choose one approach and document it in a short comment:
     - **Option A:** copy items, leave source unchanged
     - **Option B:** remove items from source as you transfer (recommended)

2. **Demonstrate the wildcard behaviour**
   - Create:
     - `List<Electronics> electronicsDepartment`
     - `List<Item> centralWarehouse`
   - Print a “manifest” of both lists **before shipping**
   - Ship items
   - Print a “manifest” of both lists **after shipping**

3. **Reflection comment (required)**
   - Add a code comment answering:
     - Why can’t you safely add a `Furniture` into a `List<? extends Item>`?

**Deliverable for Week 3**
- `GameCollectible.java` (or similar)
- `shipItems(...)` implemented + demonstrated
- Reflection comment included in code

---

## Hints

- **Producer Extends**: `List<? extends Item>` is great for reading (`get`) items safely as `Item`.
- **Consumer Super**: `List<? super Item>` is great for adding (`add`) `Item` values safely.
- If you see yourself writing casts like `(Item) something`, stop and ask: *why am I fighting
  the type system?*

---

## Extensions (optional)

Pick one or two:

1. **Ship between specialised warehouses**
   - Add `List<GameCollectible>` and ship into `List<Item>`.

2. **Add discounts**
   - Add `applyDiscount(List<? extends Item> items, double percent)` that returns the
     discounted total without mutating items.

3. **JUnit tests**
   - Tests for:
     - `calculateTotalValue(...)`
     - `shipItems(...)` moving the correct number of items

---

## Utility

### Suggested project structure

```text
src/
  app/
    WarehouseApp.java
  model/
    Item.java
    Electronics.java
    Furniture.java
    GameCollectible.java
    StorageBox.java
  util/
    InventoryUtils.java
    FileUtils.java
```

### Skeleton: `InventoryUtils`

```java
package util;

import java.util.List;
import model.Item;

public class InventoryUtils {

    public static <T extends Item> double calculateTotalValue(List<T> items) {
        // TODO: sum item.getPrice()
        return 0.0;
    }

    public static void shipItems(List<? extends Item> source, List<? super Item> destination) {
        // TODO: move items from source to destination
    }
}
```

### Optional: simple file loader for manifests

If you want to drive your demo from a text file (one item per line), you can reuse this pattern:

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static ArrayList<String> readAllLines(Path filePath) {
        if (filePath == null)
            return null;

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            return new ArrayList<>(lines);
        }
        catch (IOException e) {
            return null;
        }
    }
}
```
