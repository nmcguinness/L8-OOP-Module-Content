---
title: "Challenge Exercise: Frequency Forge"
topic: "Generics"
language: "Java"
estimated_lab_time: "2–3 weeks (approx. 2–3 hours active coding)"
prerequisites:
  - "Classes & objects"
  - "ArrayList/List basics"
  - "HashMap basics"
  - "Methods + overloading"
---

# Challenge Exercise: Frequency Forge

## Scenario

You are working as a junior developer on a mixed **software and games development** team.
Your team has been asked to build a small analysis tool that can **count and report how often
certain items appear in a dataset**.

At first, the data is tiny — just a short list of country codes used for testing a new feature.
But very quickly, the requirements grow. The same logic is needed to process **real datasets**
coming from files, including a **CSV export** and an **XML inventory** from a game factory
system that tracks weapon production.

Your task is not just to make the code *work*, but to **improve it step by step**:
- start with a simple, brute-force solution,
- recognise its limitations,
- refactor to a more efficient approach using collections,
- generalise the logic so it can be reused,
- and finally test it against realistic data while measuring performance.

This mirrors how real systems evolve: quick prototypes first, then cleaner, faster, more
reusable designs as requirements and data scale up.

## Overview

This challenge starts small and grows on purpose.

You will begin with a **tiny list of 12 country codes** and a deliberately **naive** approach.
Then you will progressively improve the solution until you end up with:

- A clean **Map-based** frequency counter (fast + readable)
- A reusable **generic** frequency counter (one algorithm, many data types)
- A realistic test using:
  - a **CSV dataset** of country codes
  - an **XML dataset** of weapons from a game factory inventory
- Basic benchmarking using `System.nanoTime()` (reported in **milliseconds**)

The end result is a mini “pipeline” you can reuse in real software and game projects.

---

## Learning Objectives

By completing this exercise, you should be able to:

- Explain why a naive frequency counter is inefficient
- Use `Map<K, V>` to count frequencies efficiently
- Apply generics to avoid code duplication
- Read structured data from CSV and XML files
- Measure runtime using `System.nanoTime()` and report in milliseconds
- Output a sorted report from a map

---

## Stage 1 – Naive frequency counting (inefficient on purpose)

### What you are building

A frequency counter that works **without Maps**.

Use this hard-coded starter list first:

```java
List<String> codes = List.of(
  "ie","gb","ie","us",
  "ch","ua","ie","cz",
  "gb","ie","ch","ua"
);
```

Write code, in any way you see fit, to count the frequency of occurence of each country code
and output the frequencies in alphabetical order.

Your output should look like:

```text
ch: 2
cz: 1
gb: 2
ie: 4
ua: 2
us: 1
```

This is the “brute force” version:
- For each code, count how many times it appears by scanning the entire list again.

This works fine for 12 values, but becomes slow as the dataset grows.

### Tasks

1. Write a method:

   - `public static void printFrequenciesNaive(List<String> codes)`

2. For each *unique* code:
   - count how many times it appears (nested loops)
   - print one line per code

3. Ensure you do **not** print duplicates (you can use an extra `List<String>` to remember what
   you've already printed).

### Use case

In a small prototype (or a quick “hack day” script), brute force counting might be acceptable.
But once your data grows (log files, analytics, player telemetry), you need better tools.

---

## Stage 2 – Use a Map (efficient frequency counting)

### What you are building

A frequency counter using:

- `Map<String, Integer>`

You will do the counting in a **single pass**:

- If the code has not been seen before, insert with count 1
- Otherwise to increment the count

### Tasks

1. Write a method:

   - `public static Map<String, Integer> countFrequenciesMap(List<String> codes)`

2. Use a `HashMap<String, Integer>` internally.
3. Return the map.

### Use case

This is the standard solution for:
- counting user actions
- counting errors by type
- counting inventory items by ID
- counting achievements earned

---

## Stage 3 – Make it generic (one algorithm, many types)

### What you are building

A **generic** method that can count *anything*.

Instead of hard-coding `String`, you will accept:

- a list of items `T`
- a way to extract a key `K` from each item

This is where generics provide huge value: one method can count country codes, weapon names,
enemy types, achievements, etc.

### Tasks

1. Implement:

   - `public static <T, K> Map<K, Integer> countByKey(Iterable<T> items, Function<T, K>
     keySelector)`

2. Use it for country codes by using `s -> s` as the key selector.

### Use case

In games dev:
- Count enemy spawns by enemy type
- Count loot drops by rarity
- Count player actions by input type

In software dev:
- Count HTTP responses by status code
- Count records by category
- Count events by source

---

## Stage 4 – Trial with real datasets (CSV + XML)

Now we switch from “toy data” to realistic data.

### Dataset A (CSV): Country codes

You are given:

- [country_codes](https://github.com/nmcguinness/L8---OOP---Module-Content/blob/main/code/data/ce03/country_codes.csv)

**Important:** this file contains codes separated by commas (often on one line), so your CSV
reading must collect **every token**, not just the first column.

### Dataset B (XML): Weapon inventory

You are given:

- [weapon_inventory](https://github.com/nmcguinness/L8---OOP---Module-Content/blob/main/code/data/ce03/weapon_inventory.xml)

This represents a factory inventory in a game. Each weapon has:

- `name` (String)
- `strength` (int)

You will load weapons from XML into a `List<Weapon>` then count by weapon name.

---

## Stage 5 – Benchmarking (nanoTime to milliseconds)

For both datasets:

1. Record start time using `System.nanoTime()`
2. Run the frequency counting step
3. Record end time using `System.nanoTime()`
4. Convert to milliseconds:

   - `double ms = (end - start) / 1_000_000.0;`

5. Print the elapsed time in **ms**

### Use case

Benchmarking is how you validate performance claims.
Even simple counters can become bottlenecks when:
- you run them every frame
- you process big datasets
- you perform repeated queries

---

## Provided Classes

You are provided with the following helper and model classes.
You should **not modify** these files.

<details>
<summary><strong>Weapon</strong></summary>

```java
package t11_generics_1.challenges.ce03;

import java.util.Objects;

public class Weapon {
    private String name;
    private int strength;

    public Weapon(String name, int strength) {
        this.name = name;
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "name='" + name + '\'' +
                ", strength=" + strength +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Weapon weapon)) return false;
        return strength == weapon.strength && Objects.equals(name, weapon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, strength);
    }
}

```

</details>

<details>
<summary><strong>FileHelper</strong></summary>

```java
package t11_generics_1.challenges.ce03;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileHelper
{
    /// <summary>
    /// Reads a list of strings from a CSV file (first column only).
    /// </summary>
    public static List<String> readStringsFromCsv(Path csvPath, boolean hasHeader) throws IOException
    {
        if (csvPath == null)
            throw new IllegalArgumentException("csvPath is null.");

        if (!Files.exists(csvPath))
            throw new IOException("CSV file not found: " + csvPath);

        List<String> results = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath))
        {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null)
            {
                if (firstLine && hasHeader)
                {
                    firstLine = false;
                    continue;
                }
                firstLine = false;

                line = line.trim();
                if (line.isEmpty())
                    continue;

                // Your dataset is "token,token,token" (often all on one line),
                // so we must collect *every* token, not just parts[0].
                String[] tokens = line.split(",");

                for (String token : tokens)
                {
                    String value = token.trim();

                    if (!value.isEmpty())
                        results.add(value);
                }
            }
        }

        return results;
    }

    /// <summary>
    /// Reads Weapon objects from an XML file.
    /// Expected structure:
    /// &lt;weapons&gt;
    ///   &lt;weapon&gt;&lt;name&gt;...&lt;/name&gt;&lt;strength&gt;...&lt;/strength&gt;&lt;/weapon&gt;
    /// &lt;/weapons&gt;
    /// </summary>
    public static List<Weapon> readWeaponsFromXml(Path xmlPath) throws Exception
    {
        if (xmlPath == null)
            throw new IllegalArgumentException("xmlPath is null.");

        if (!Files.exists(xmlPath))
            throw new IOException("XML file not found: " + xmlPath);

        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(xmlPath.toFile());

        doc.getDocumentElement().normalize();

        NodeList weaponNodes = doc.getElementsByTagName("weapon");
        List<Weapon> weapons = new ArrayList<>();

        for (int i = 0; i < weaponNodes.getLength(); i++)
        {
            Element weaponEl = (Element) weaponNodes.item(i);

            String name = getChildText(weaponEl, "name").trim();
            String strengthText = getChildText(weaponEl, "strength").trim();

            if (name.isEmpty())
                throw new IllegalStateException("Weapon name is missing/blank at index " + i);

            int strength;
            try
            {
                strength = Integer.parseInt(strengthText);
            }
            catch (NumberFormatException ex)
            {
                throw new IllegalStateException("Weapon strength is not a valid integer at index " + i + ": " + strengthText);
            }

            weapons.add(new Weapon(name, strength));
        }

        return weapons;
    }

    private static String getChildText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0)
            return "";

        return nodes.item(0).getTextContent();
    }
}

```

</details>

<details>
<summary><strong>PrintHelper</strong></summary>

```java
package t11_generics_1.challenges.ce03;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PrintHelper
{
    /// <summary>
    /// Prints map contents sorted by key using the key's toString() ordering.
    /// Keeps things simple for Year 2: no Comparable constraint needed.
    /// </summary>
    public static <K, V> void printSortedByKey(Map<K, V> map)
    {
        if (map == null)
            throw new IllegalArgumentException("map is null.");

        List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Comparator.comparing(e -> e.getKey().toString()));

        for (Map.Entry<K, V> entry : entries)
        {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}


```

</details>

---

## Your Implementation File

Complete the exercise in `Exercise.java` by working through the stages above.

<details>
<summary><strong>Exercise</strong></summary>

```java
package t11_generics_1.challenges.ce03;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exercise {

    public static void run() {
        new Exercise().start();
    }

    public void start() {

        //helper to tell you what directory to put your data file in
        System.out.println("Put your data file in: " + System.getProperty("user.dir"));

        System.out.println("=== Part A: Country code frequency (CSV) ===");
        runStringTest();

        System.out.println("=== Part B: Weapon inventory frequency (XML) ===");
        runXMLTest();
    }

    public void runStringTest()
    {
        String filePath = "code/data/ce03/country_codes.csv";
        Path csvPath = Path.of(filePath);

        List<String> codes;

        try {
            codes = FileHelper.readStringsFromCsv(csvPath, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long start = System.nanoTime();
        Map<String, Integer> counts = countByKey(codes);
        long end = System.nanoTime();



        System.out.println("Rows read: " + codes.size());
        System.out.println("Unique codes: " + counts.size());
        long elapsedNs = end - start;
        double elapsedMs = elapsedNs / 1_000_000.0;
        System.out.println("Elapsed: " + elapsedMs + " ms");
        System.out.println();

        System.out.println("Sorted report:");
        PrintHelper.printSortedByKey(counts);
    }

    public void runXMLTest(){
        String filePath = "code/data/ce03/weapon_inventory.xml";
        Path xmlPath = Path.of(filePath);

        List<Weapon> weapons;
        try {
            weapons = FileHelper.readWeaponsFromXml(xmlPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        long start = System.nanoTime();
        Map<Weapon, Integer> counts = countByKey(weapons);
        long end = System.nanoTime();

        System.out.println("=== Weapon inventory frequency (XML) ===");
        System.out.println("Weapons read: " + weapons.size());
        System.out.println("Unique weapon names: " + counts.size());
        long elapsedNs = end - start;
        double elapsedMs = elapsedNs / 1_000_000.0;
        System.out.println("Elapsed: " + elapsedMs + " ms");
        System.out.println();

        System.out.println("Sorted report:");
        PrintHelper.printSortedByKey(counts);
    }

    public <T> Map<T, Integer> countByKey(List<T> items)
    {
        Map<T, Integer> counts = new HashMap<>();
        for (T item : items)
        {
            Integer current = counts.get(item);
            if (current == null)
                counts.put(item, 1);
            else
                counts.put(item, current + 1);
        }
        return counts;
    }
}

```

</details>

---

## Submission Notes

- Do **not** hard-code dataset contents (beyond the Stage 0 starter list).
- Your final solution should reuse the **generic** counter for both:
  - country codes (`String`)
  - weapons (`Weapon`)
- Keep output sorted using the provided printing helper.

---

## Stretch Ideas (Optional)

- Compare `HashMap` vs `TreeMap` for ordering
- Count weapons by *strength* (you will need to decide how to group strengths)
- Increase dataset size (duplicate the list) and observe benchmark changes
