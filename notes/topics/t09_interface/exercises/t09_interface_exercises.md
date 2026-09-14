---
title: "Interfaces — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t09_interface
description: "Nine interface exercises: behaviour contracts, multiple interfaces, strategy and comparator factories, command history, and a file-backed filter pipeline."
created: 2026-05-29
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [java, interfaces, polymorphism, exercises, year2, comp-c8z03]
---
# Interfaces — Exercises

> These exercises build directly on the **Interfaces** notes and reuse ideas from earlier
> topics: arrays, collections, LinkedList, Comparator “factories”, and basic IO.

## How to run

Each exercise assumes a package like:

```java
t09_interface.exercises.exNN
```

Create a class `Exercise` in that package with a static entry point:

```java
package t09_interface.exercises.ex01;

public class Exercise {
    public static void run() {
        // your tests here
    }
}
```

Call `Exercise.run()` from your `Main`, as shown in the repo README.

---

## Exercise 01 — A Simple Behaviour Interface

**Objective:** Declare a basic interface and implement it in two classes.
**Description:** You’ll create a very small behaviour (`Greeter`) and two classes that
implement it differently. This warms up your interface syntax.

- Create an interface:

  ```java
  public interface Greeter {
      void greet(String name);
  }
  ```

- Create two implementations, e.g.:

  ```java
  public class CasualGreeter implements Greeter { /* ... */ }
  public class FormalGreeter implements Greeter { /* ... */ }
  ```

  One might print `"Hey, <name>!"`, the other `"Good evening, <name>."`.
- In `Exercise.run()`:
  - Declare a variable of type `Greeter` and assign different implementations to it.
  - Call `greet("Alex")` a few times and observe the polymorphic behaviour.

**Quick check:**

```java
Greeter g = new CasualGreeter();
g.greet("Alex");  // prints casual greeting
g = new FormalGreeter();
g.greet("Alex");  // prints formal greeting
```

---

## Exercise 02 — Interactable Objects in a List

**Objective:** Use interfaces with a `List` to store heterogeneous objects.
**Description:** You’ll model interactable objects in a mini game world and process them via
their shared interface.

- Define:

  ```java
  public interface Interactable {
      void interact();
  }
  ```

- Implement at least three classes, e.g. `Door`, `Chest`, `NPC`, each with its own `interact()`
  message.
- In `Exercise.run()`:
  - Create a `List<Interactable>` (ArrayList or LinkedList).
  - Add one instance of each class.
  - Loop and call `interact()` on each element.

**Extension:**
- Add an `int DEFAULT_INTERACTION_RANGE = 2;` constant to the interface.
- Use it in a dummy `int distanceToPlayer` check before calling `interact()`.

---

## Exercise 03 — Multiple Interfaces + LinkedList

**Objective:** Combine multiple interfaces and a `LinkedList` to organise behaviour.
**Description:** You’ll create two interfaces (`Moveable`, `Damageable`) and a `LinkedList`
that stores objects through one of those interfaces.

- Define:

  ```java
  public interface Moveable {
      void move();
  }

  public interface Damageable {
      void takeDamage(int amount);
  }
  ```

- Implement at least two classes:

  ```java
  public class Player implements Moveable, Damageable { /* ... */ }
  public class Enemy implements Moveable, Damageable { /* ... */ }
  ```

- In `Exercise.run()`:
  - Create a `LinkedList<Moveable>`.
  - Add a `Player` and one or more `Enemy` objects.
  - Iterate from front to back, calling `move()` on each.
- Then cast some of them back to `Damageable` to call `takeDamage(...)`.
  - Be careful: use `instanceof` checks before casting.

**Quick check:**

```java
LinkedList<Moveable> actors = new LinkedList<>();
actors.add(new Player("Hero"));
actors.add(new Enemy("Slime"));
for (Moveable m : actors)
    m.move();
```

---

## Exercise 04 — Strategy Interface for Attacks

**Objective:** Use an interface as a “strategy” that can be swapped at runtime.
**Description:** You’ll design an `AttackStrategy` interface, then give your `Enemy` different
behaviours by plugging in different strategies.

- Define:

  ```java
  public interface AttackStrategy {
      void attack(String targetName);
  }
  ```

- Implement at least two strategies:
  - `MeleeAttack` prints something like `"Swings sword at <targetName>!"`
  - `RangedAttack` prints something like `"Shoots arrow at <targetName>!"`
- Create an `Enemy` class that has a field of type `AttackStrategy` and a method:

  ```java
  public class Enemy {
      private AttackStrategy strategy;

      public Enemy(AttackStrategy strategy) {
          this.strategy = strategy;
      }

      public void setStrategy(AttackStrategy strategy) {
          this.strategy = strategy;
      }

      public void performAttack(String targetName) {
          if (strategy != null)
              strategy.attack(targetName);
      }
  }
  ```

- In `Exercise.run()`:
  - Create an `Enemy` with a `MeleeAttack`.
  - Call `performAttack("Player")`.
  - Change its strategy to `RangedAttack` and call again.

**Extension:**
- Store several `AttackStrategy` objects in a `List<AttackStrategy>` and randomly pick one.

---

## Exercise 05 — Comparator Factory with an Interface

**Objective:** Combine interfaces with `Comparator` and a simple “factory” method.
**Description:** You’ll create an interface to represent something with a name, then build
comparators for sorting a list of those objects in different ways.

- Create:

  ```java
  public interface Named {
      String getName();
  }
  ```

- Implement `GameItem` and `NPC` classes that implement `Named`.
- In a utility class, create “factory” methods that return `Comparator<Named>`:

  ```java
  import java.util.Comparator;

  public final class NamedComparators {
      private NamedComparators() { }

      public static Comparator<Named> byNameAscending() {
          return (a, b) -> a.getName().compareToIgnoreCase(b.getName());
      }

      public static Comparator<Named> byNameLength() {
          return (a, b) -> Integer.compare(a.getName().length(), b.getName().length());
      }
  }
  ```

- In `Exercise.run()`:
  - Create a `List<Named>` containing both `GameItem` and `NPC` objects.
  - Sort the list using `Collections.sort(list, NamedComparators.byNameAscending());`
  - Then sort by `byNameLength()`.

**Quick check:**

```java
Collections.sort(things, NamedComparators.byNameAscending());
// verify first and last element by printing
```

---

## Exercise 06 — Command History with LinkedList & Interface

**Objective:** Use an interface + `LinkedList` to build a simple “command history”.
**Description:** You’ll define a `Command` interface, implement some commands, and then walk
the history to “replay” them.

- Define:

  ```java
  public interface Command {
      void execute();
  }
  ```

- Implement some commands, for example:
  - `PrintCommand` (stores a message and prints it on `execute()`)
  - `AddCommand` (adds two numbers and prints the result)
- In `Exercise.run()`:
  - Create a `LinkedList<Command>` to act as a history.
  - Push a few `Command` objects into the list.
  - Iterate over the list, calling `execute()` on each.

**Extension:**
- Add an `undo()` method to the interface and decide how `undo` should behave for each command
  (e.g. log that it was undone).

---

## Exercise 07 — XmlSerializable Interface (Reading/Writing Simple XML)

**Objective:** Use an interface to standardise XML (or XML-like) saving and loading.
**Description:** You’ll define an `XmlSerializable` interface and use it to save/load simple
objects to/from strings or files.

> You don’t need a full XML parser — a simple tag-based format like `<player name="Alex"
> score="10" />` is enough.

- Define:

  ```java
  public interface XmlSerializable {
      String toXml();
      // Static or separate helper for fromXml(String)
  }
  ```

- Implement a `PlayerProfile` class:

  ```java
  public class PlayerProfile implements XmlSerializable {
      private final String name;
      private final int score;

      public PlayerProfile(String name, int score) {
          this.name = name;
          this.score = score;
      }

      public String getName() { return name; }
      public int getScore() { return score; }

      @Override
      public String toXml() {
          return "<player name="" + name + "" score="" + score + "" />";
      }

      public static PlayerProfile fromXml(String xml) {
          // parse the attributes in a very simple way (string operations)
          // e.g. find name="..." and score="..."
          // (you can keep this deliberately simple for the exercise)
          return null; // TODO: implement
      }
  }
  ```

- In `Exercise.run()`:
  - Create one or two `PlayerProfile` objects.
  - Convert them to XML strings and print them.
  - Convert the strings back to `PlayerProfile` objects and print their fields.

### Aside: Writing XML to a file (optional)

Here’s a **minimal pattern** to write a single XML string to a file called `profile.xml` using
`Files.writeString` (Java 11+):

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class XmlFileHelper {

    public static void saveProfileToFile(PlayerProfile profile, String fileName) {
        String xml = profile.toXml();
        Path path = Path.of(fileName);

        try {
            Files.writeString(path, xml);
            System.out.println("Saved XML to " + path.toAbsolutePath());
        }
        catch (IOException e) {
            System.out.println("Failed to save XML: " + e.getMessage());
        }
    }
}
```

Usage from `Exercise.run()`:

```java
PlayerProfile profile = new PlayerProfile("Alex", 10);
XmlFileHelper.saveProfileToFile(profile, "profile.xml");
```

To read it back later:

```java
try {
    String xml = Files.readString(Path.of("profile.xml"));
    PlayerProfile loaded = PlayerProfile.fromXml(xml);
    // print loaded.getName() and loaded.getScore()
}
catch (IOException e) {
    System.out.println("Failed to read XML: " + e.getMessage());
}
```

> **Note for students:** this is not “industrial-strength” XML handling, but it’s enough to
> show how an interface (`XmlSerializable`) plus a helper method can give you simple save/load
> features.

---

## Exercise 08 — Text Filter Pipeline using a List of Filters

**Objective:** Build a reusable text-processing pipeline using an interface and a `List`.
**Description:** You’ll create a `TextFilter` interface, several implementations, and a helper
method that runs a string through a sequence of filters.

- Define:

  ```java
  public interface TextFilter {
      String apply(String input);
  }
  ```

- Implement at least three filters:
  - `TrimFilter` — calls `input.trim()`
  - `LowercaseFilter` — calls `input.toLowerCase()`
  - `RegexHightlightFilter` - adds underline HTML tags around phone numbers
- In a utility class, implement:

  ```java
  import java.util.List;

  public final class TextFilters {
      private TextFilters() { }

      public static String applyAll(List<TextFilter> filters, String text) {
          String result = text;
          for (TextFilter f : filters)
              result = f.apply(result);
          return result;
      }
  }
  ```

- In `Exercise.run()`:
  - Build a `List<TextFilter>` (e.g. `new LinkedList<>()`).
- Add filters in a useful order (e.g. trim then lowercase then profanity).
- Call `applyAll(filters, "   This is MY work number 0429470200, call me before 5PM   ")` and
    print the result.

**Extension ideas:**
- Add a `LengthLimitFilter` that truncates the string to 40 characters.
- Create a filter that removes all digits, or all punctuation.
- Let the user build a pipeline interactively via console input (choose which filters to add).

---

## Exercise 09 — File-Backed Profanity Filter using `profane_words.csv`

**Objective:** Extend Exercise 08 by loading the list of bad words from a CSV file and
injecting them into a filter via an `Iterable<String>` field.
**Description:** You’ll create a `ProfanityFilter` that reads its bad words from a file called
`profane_words.csv`, then plug it into the text filter pipeline.

> **Assumption:** `profane_words.csv` is a simple file that contains one bad word per line
> (e.g. `badword1` on line 1, `badword2` on line 2, etc.).

1. Ensure `profane_words.csv` exists in your project (e.g. project root or a `data/` folder):

   ```text
   badword
   verybad
   rude
   ```

2. Create a new filter implementation that takes an `Iterable<String>` of bad words:

   ```java
   import java.util.Locale;

   public class ProfanityFilter implements TextFilter {
       private final Iterable<String> badWords;

       public ProfanityFilter(Iterable<String> badWords) {
           this.badWords = badWords;
       }

       @Override
       public String apply(String input) {
           if (input == null)
               return null;

           String result = input;

           for (String bad : badWords) {
               if (bad == null || bad.isBlank())
                   continue;

               String badLower = bad.toLowerCase(Locale.ROOT).trim();
               if (badLower.isEmpty())
                   continue;

               // Simple replacement (case-insensitive)
               result = result.replaceAll("(?i)" + badLower, "***");
           }

           return result;
       }
   }
   ```

   > **Note:** The field type is `Iterable<String>`, so you can pass any `List<String>`,
   > `LinkedList<String>`, etc.

3. Create a helper method to read the CSV file into a `List<String>`:

   ```java
   import java.io.IOException;
   import java.nio.file.Files;
   import java.nio.file.Path;
   import java.util.ArrayList;
   import java.util.List;

   public final class ProfanityLoader {
       private ProfanityLoader() { }

       public static List<String> loadFromCsv(String fileName) {
           List<String> result = new ArrayList<>();
           Path path = Path.of(fileName);

           try {
               for (String line : Files.readAllLines(path)) {
                   String trimmed = line.trim();
                   if (!trimmed.isEmpty())
                       result.add(trimmed);
               }
           }
           catch (IOException e) {
               System.out.println("Failed to load blacklist: " + e.getMessage());
           }

           return result;
       }
   }
   ```

4. In `Exercise.run()` for `ex09`:
   - Load the bad words using `ProfanityLoader.loadFromCsv("profane_words.csv")`.
   - Create a `ProfanityFilter` with that list.
   - Build your filter pipeline as in Exercise 08, but include the `ProfanityFilter`:

   ```java
   import java.util.LinkedList;
   import java.util.List;

   public class Exercise {
       public static void run() {
           List<String> badWords = ProfanityLoader.loadFromCsv("profane_words.csv");
           TextFilter blacklistFilter = new ProfanityFilter(badWords);

           List<TextFilter> filters = new LinkedList<>();
           filters.add(new TrimFilter());
           filters.add(new LowercaseFilter());
           filters.add(blacklistFilter);

           String input = "   This is SOME veryBad text   ";
           String output = TextFilters.applyAll(filters, input);

           System.out.println("Original: [" + input + "]");
           System.out.println("Filtered: [" + output + "]");
       }
   }
   ```

**Reflection questions for this exercise:**
- How does using `Iterable<String>` for the field make `ProfanityFilter` more flexible than
  hard-coding a `List<String>`?
- How could you modify this design to support reloading the blacklist file at runtime?

---

```yaml
linked_lesson:
  topic_code: "t09_interface"
  lesson_path: "/notes/topics/t09_interface/t09_interface_notes.md"
  primary_domain_emphasis: "Balanced"
  difficulty_tier: "Intermediate"
```
