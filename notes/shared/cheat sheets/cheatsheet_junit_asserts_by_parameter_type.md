# JUnit Cheatsheet — by Parameter Type

> Goal: help students decide **what tests & asserts** to write for a method’s parameters so
> they hit the usual edge-cases and validation paths.

---

## 1) Primitives — `int`, `long`, `short`, `byte`, `char`

**Typical risks:** off-by-one, boundary checks, negatives vs zero vs positives.

### Checklist

- Minimum allowed; just **below** min (use `assertThrows` if invalid)
- Maximum allowed; just **above** max (use `assertThrows`)
- `0` if allowed; “one less / one more” around thresholds
- Indices/counters: negative must throw?

### Snippets

```java
@Test void int_withinBounds_ok() {
    int r = MyMath.clamp(5, 0, 10);
    assertEquals(5, r);
}
@Test void int_belowMin_throws() {
    assertThrows(IllegalArgumentException.class, () -> MyMath.clamp(-1, 0, 10));
}
@Test void int_aboveMax_throws() {
    assertThrows(IllegalArgumentException.class, () -> MyMath.clamp(11, 0, 10));
}
```

---

## 2) Floating point — `float`, `double`

**Typical risks:** rounding, precision, divide-by-zero.

### Checklist

- Representative values (small/large/negative/zero)
- Use **delta** in `assertEquals(expected, actual, delta)`
- Division by zero: `assertThrows` if enforced

### Snippets

```java
@Test void double_average_delta() {
    double avg = Stats.average(new double[]{1, 2, 3});
    assertEquals(2.0, avg, 1e-9);
}
@Test void double_divideByZero_throws() {
    assertThrows(ArithmeticException.class, () -> MyMath.safeDivide(10.0, 0.0));
}
```

---

## 3) `boolean`

**Typical risks:** inverted logic, missing side-effects.

### Checklist

- True path, false path
- If state toggles, assert **state change**

```java
@Test void bool_featureFlag_truePath() { assertTrue(Features.isEnabled("beta")); }
@Test void bool_featureFlag_falsePath() { assertFalse(Features.isEnabled("unknown")); }
```

---

## 4) `enum`

**Typical risks:** unhandled values, default branches.

### Checklist

- Test **each** enum constant (parameterized tests shine)
- If nulls illegal, `assertThrows`

```java
@ParameterizedTest @EnumSource(Mode.class)
void mode_isHandled(Mode m) { assertNotNull(ModeNamer.label(m)); }

@Test void mode_null_throws() {
    assertThrows(NullPointerException.class, () -> ModeNamer.label(null));
}
```

---

## 5) `String`

**Typical risks:** null, empty, blanks, trimming, case, Unicode, parsing.

### Checklist

- `null` (throw?) — `""` empty — `"   "` blanks
- Normal case; mixed case; **Unicode** (e.g., `"Ångström"`)
- If parsing numbers, invalid formats (`"12x"`) hit error path

```java
@Test void str_normal_ok() { assertEquals("Hello, Niall", Greeter.greet("Niall")); }
@Test void str_empty_throws() { assertThrows(IllegalArgumentException.class, () -> Greeter.greet("")); }
@Test void str_blanks_trimmed() { assertEquals("Hello, Zara", Greeter.greet("  Zara  ")); }
@Test void str_unicode_ok() { assertEquals("Hello, Åsa", Greeter.greet("Åsa")); }
```

---

## 6) Arrays — `int[]`, `double[]`, `String[]`, `T[]`

**Typical risks:** `null`, empty, singleton, duplicates, order, mutation of inputs.

### Checklist

- `null` array (throw or handle), empty, singleton, many, duplicates
- **Order matters?** — `assertArrayEquals`
- **No mutation?** snapshot before; compare after

```java
@Test void arr_null_throws() {
    assertThrows(NullPointerException.class, () -> Arrays2.min(null));
}
@Test void arr_empty_throws() {
    assertThrows(IllegalArgumentException.class, () -> Arrays2.min(new int[]{}));
}
@Test void arr_singleton_ok() {
    assertEquals(42, Arrays2.min(new int[]{42}));
}
@Test void arr_multiple_ok() {
    assertEquals(1, Arrays2.min(new int[]{3,1,7,1}));
}
@Test void arr_noMutation_contract() {
    int[] input = {3,2,1};
    int[] before = input.clone();
    Arrays2.sortedCopy(input);
    assertArrayEquals(before, input); // input unchanged
}
```

---

## 7) Collections — `List<T>` (e.g., `ArrayList<String>`)

**Typical risks:** `null` list, empty, `null` elements, order, duplicates, **mutability**.

### Checklist

- `null` list param (throw or handle)
- Empty / singleton / many + duplicates
- `null` **elements**? either forbid (throw) or define behaviour — test it
- **Order matters?** — `assertIterableEquals`
- **No mutation?** compare before/after or assert new instance

```java
@Test void list_null_throws() {
    assertThrows(NullPointerException.class, () -> Lists.joinWithComma(null));
}
@Test void list_empty_ok() { assertEquals("", Lists.joinWithComma(List.of())); }
@Test void list_singleton_ok() { assertEquals("Amy", Lists.joinWithComma(List.of("Amy"))); }
@Test void list_dupes_ok() { assertEquals("A,A,B", Lists.joinWithComma(List.of("A","A","B"))); }
@Test void list_nullElement_throws() {
    assertThrows(IllegalArgumentException.class, () -> Lists.joinWithComma(List.of("A", null)));
}
@Test void list_noMutation_contract() {
    var data = new java.util.ArrayList<>(java.util.List.of("b","a"));
    var before = java.util.List.copyOf(data);
    var out = Lists.sortedCopy(data);
    assertIterableEquals(before, data);          // input unchanged
    assertIterableEquals(java.util.List.of("a","b"), out);
    assertNotSame(data, out);                    // different instance
}
```

---

## 8) Mixed parameters — e.g., `(List<String> names, int limit, String prefix)`

**Strategy:** drive **every guard/branch** via combinations.

- `names` null/empty/singleton/many + null elements
- `limit` 0/1/max; negative (throws)
- `prefix` null/empty/whitespace/Unicode
- Validate output size/content/order; and whether inputs are mutated.

```java
@Test void filter_limit0_returnsEmpty_noMutation() {
    var input = new java.util.ArrayList<>(java.util.List.of("ann","amy","bob"));
    var before = java.util.List.copyOf(input);
    var out = NameTools.takeWithPrefix(input, 0, "a");
    assertTrue(out.isEmpty());
    assertIterableEquals(before, input);
}
@Test void filter_normal_ok() {
    var out = NameTools.takeWithPrefix(java.util.List.of("ann","amy","bob","áine"), 2, "a");
    assertIterableEquals(java.util.List.of("ann","amy"), out);
}
@Test void filter_prefixUnicode_ok() {
    var out = NameTools.takeWithPrefix(java.util.List.of("ann","áine","bob"), 5, "á");
    assertIterableEquals(java.util.List.of("áine"), out);
}
@Test void filter_negativeLimit_throws() {
    assertThrows(IllegalArgumentException.class, () -> NameTools.takeWithPrefix(java.util.List.of("x"), -1, "x"));
}
@Test void filter_nulls_throws() {
    assertThrows(NullPointerException.class, () -> NameTools.takeWithPrefix(null, 1, "a"));
    assertThrows(NullPointerException.class, () -> NameTools.takeWithPrefix(java.util.List.of("a"), 1, null));
}
```

---

## 9) Mutation vs pure functions (arrays/lists)

- If method **must not modify** inputs, assert unchanged after call.
- If it **should** modify (in-place), assert **same instance** with updated contents.

```java
@Test void inPlaceSort_mutatesSameList() {
    var data = new java.util.ArrayList<>(java.util.List.of(3,1,2));
    var out = Lists.inPlaceSort(data);
    assertSame(data, out);
    assertIterableEquals(java.util.List.of(1,2,3), data);
}
```

---

## 10) Exceptions & messages

Check promised **type** and a meaningful **message** (when specified).

```java
@Test void validateIndex_throwsWithMessage() {
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> Indexer.getAt(List.of(), 0));
    assertTrue(ex.getMessage().toLowerCase().contains("index out of range"));
}
```

---

## 11) Parameterized tests = fewer lines, more coverage

```java
@ParameterizedTest
@ValueSource(ints = { -1, 0, 1, 10 })
void clamp_acceptsOnlyRange(int v) {
    if (v < 0 || v > 5)
        assertThrows(IllegalArgumentException.class, () -> Clamp.toRange(v, 0, 5));
    else
        assertEquals(v, Clamp.toRange(v, 0, 5));
}
```

---

## Setup reminder (IntelliJ + Maven)

- Add JUnit 5 to `pom.xml`, **reload Maven**, then restart IntelliJ only if still unresolved.
- JetBrains guide: <https://www.jetbrains.com/help/idea/junit.html>

---

## Appendix — Sample Test Class

```java
package example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Parameter-Type Coverage Scaffold
 * Replace the TODO methods with your actual code under test.
 */
public class ParameterTypeTests {

    // 1) PRIMITIVES — int/long
    private int clamp(int v, int min, int max) {
        if (min > max) throw new IllegalArgumentException("min>max");
        if (v < min || v > max) throw new IllegalArgumentException("out of range");
        return v;
    }
    @Test void int_withinBounds_ok() { assertEquals(5, clamp(5, 0, 10)); }
    @Test void int_belowMin_throws() { assertThrows(IllegalArgumentException.class, () -> clamp(-1, 0, 10)); }
    @Test void int_aboveMax_throws() { assertThrows(IllegalArgumentException.class, () -> clamp(11, 0, 10)); }

    // 2) FLOATING POINT — double
    private double average(double[] xs) {
        if (xs == null) throw new NullPointerException("xs");
        if (xs.length == 0) throw new IllegalArgumentException("empty");
        double sum = 0; for (double v : xs) sum += v; return sum / xs.length;
    }
    @Test void double_average_delta() { assertEquals(2.0, average(new double[]{1,2,3}), 1e-9); }
    @Test void double_average_null_throws() { assertThrows(NullPointerException.class, () -> average(null)); }
    @Test void double_average_empty_throws() { assertThrows(IllegalArgumentException.class, () -> average(new double[]{})); }

    // 3) BOOLEAN
    private boolean isEven(int n) { return (n & 1) == 0; }
    @Test void bool_truePath() { assertTrue(isEven(10)); }
    @Test void bool_falsePath() { assertFalse(isEven(7)); }

    // 4) ENUM
    enum Mode { BASIC, ADVANCED, PRO }
    private String label(Mode m) {
        if (m == null) throw new NullPointerException("mode");
        return switch (m) { case BASIC -> "Basic"; case ADVANCED -> "Advanced"; case PRO -> "Pro"; };
    }
    @ParameterizedTest @EnumSource(Mode.class)
    void enum_allValuesHandled(Mode m) { assertNotNull(label(m)); }
    @Test void enum_null_throws() { assertThrows(NullPointerException.class, () -> label(null)); }

    // 5) STRING
    private String greet(String name) {
        if (name == null) throw new NullPointerException("name");
        String t = name.trim(); if (t.isEmpty()) throw new IllegalArgumentException("empty");
        return "Hello, " + t;
    }
    @Test void str_normal_ok() { assertEquals("Hello, Niall", greet("Niall")); }
    @Test void str_empty_throws() { assertThrows(IllegalArgumentException.class, () -> greet("")); }
    @Test void str_blanks_trimmed_ok() { assertEquals("Hello, Zara", greet("  Zara  ")); }
    @Test void str_null_throws() { assertThrows(NullPointerException.class, () -> greet(null)); }

    // 6) ARRAYS — int[]
    private int min(int[] xs) {
        if (xs == null) throw new NullPointerException("xs");
        if (xs.length == 0) throw new IllegalArgumentException("empty");
        int m = xs[0]; for (int v : xs) if (v < m) m = v; return m;
    }
    @Test void arr_null_throws() { assertThrows(NullPointerException.class, () -> min(null)); }
    @Test void arr_empty_throws() { assertThrows(IllegalArgumentException.class, () -> min(new int[]{})); }
    @Test void arr_singleton_ok() { assertEquals(42, min(new int[]{42})); }
    @Test void arr_multiple_ok() { assertEquals(1, min(new int[]{3,1,7,1})); }
    private int[] sortedCopy(int[] xs) { int[] c = xs.clone(); java.util.Arrays.sort(c); return c; }
    @Test void arr_noMutation_contract() {
        int[] input = {3,2,1}; int[] before = input.clone(); int[] out = sortedCopy(input);
        assertArrayEquals(before, input); assertArrayEquals(new int[]{1,2,3}, out); assertNotSame(input, out);
    }

    // 7) COLLECTIONS — List<String>
    private String joinWithComma(List<String> names) {
        if (names == null) throw new NullPointerException("names");
        if (names.stream().anyMatch(java.util.Objects::isNull)) throw new IllegalArgumentException("null element");
        return String.join(",", names);
    }
    @Test void list_null_throws() { assertThrows(NullPointerException.class, () -> joinWithComma(null)); }
    @Test void list_empty_ok() { assertEquals("", joinWithComma(List.of())); }
    @Test void list_singleton_ok() { assertEquals("Amy", joinWithComma(List.of("Amy"))); }
    @Test void list_dupes_ok() { assertEquals("A,A,B", joinWithComma(List.of("A","A","B"))); }
    @Test void list_nullElement_throws() { assertThrows(IllegalArgumentException.class, () -> joinWithComma(List.of("A", null))); }
    private List<String> sortedCopyList(List<String> xs) {
        ArrayList<String> copy = new ArrayList<>(xs); copy.sort(String::compareTo); return copy;
    }
    @Test void list_noMutation_contract() {
        ArrayList<String> data = new ArrayList<>(List.of("b","a"));
        List<String> before = List.copyOf(data);
        List<String> out = sortedCopyList(data);
        assertIterableEquals(before, data); assertIterableEquals(List.of("a","b"), out); assertNotSame(data, out);
    }

    // 8) MIXED PARAMS — (List<String> names, int limit, String prefix)
    private List<String> takeWithPrefix(List<String> names, int limit, String prefix) {
        if (names == null) throw new NullPointerException("names");
        if (prefix == null) throw new NullPointerException("prefix");
        if (limit < 0) throw new IllegalArgumentException("limit");
        ArrayList<String> out = new ArrayList<>();
        for (String n : names) {
            if (n != null && n.toLowerCase().startsWith(prefix.toLowerCase())) {
                if (out.size() == limit) break; out.add(n);
            }
        }
        return out;
    }
    @Test void mixed_limit0_returnsEmpty_noMutation() {
        ArrayList<String> input = new ArrayList<>(List.of("ann","amy","bob"));
        List<String> before = List.copyOf(input); List<String> out = takeWithPrefix(input, 0, "a");
        assertTrue(out.isEmpty()); assertIterableEquals(before, input);
    }
    @Test void mixed_normal_ok() {
        List<String> out = takeWithPrefix(List.of("ann","amy","bob","áine"), 2, "a");
        assertIterableEquals(List.of("ann","amy"), out);
    }
    @Test void mixed_prefixUnicode_ok() {
        List<String> out = takeWithPrefix(List.of("ann","áine","bob"), 5, "á");
        assertIterableEquals(List.of("áine"), out);
    }
    @Test void mixed_negativeLimit_throws() {
        assertThrows(IllegalArgumentException.class, () -> takeWithPrefix(List.of("x"), -1, "x"));
    }
    @Test void mixed_nulls_throws() {
        assertThrows(NullPointerException.class, () -> takeWithPrefix(null, 1, "a"));
        assertThrows(NullPointerException.class, () -> takeWithPrefix(List.of("a"), 1, null));
    }

    // 9) EXCEPTIONS & MESSAGES
    private <T> T getAt(List<T> xs, int idx) {
        if (idx < 0 || idx >= xs.size()) throw new IllegalArgumentException("index out of range");
        return xs.get(idx);
    }
    @Test void exception_message_containsHint() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> getAt(List.of(), 0));
        assertTrue(ex.getMessage().toLowerCase().contains("index out of range"));
    }

    // 10) PARAMETERIZED VALUES
    @ParameterizedTest @ValueSource(ints = { -1, 0, 1, 5, 6 })
    void clamp_parametrized(int v) {
        if (v < 0 || v > 5)
            assertThrows(IllegalArgumentException.class, () -> clamp(v, 0, 5));
        else
            assertEquals(v, clamp(v, 0, 5));
    }
}
```
