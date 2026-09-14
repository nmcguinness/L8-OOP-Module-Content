# Cheatsheet - JUnit 5 Assertions

See the [JUnit demo code repository](https://github.com/nmcguinness/L8_OOP_Using_JUnit.git)

## Assertion code snippets

### `assertEquals`

Checks that expected and actual values are equal. Use for exact matches (numbers, strings,
objects with proper `equals`).

```java
assertEquals(80.0, PricingUtils.applyDiscount(100.0, 20.0));
```

### `assertNotEquals`

Verifies the value is **not** equal to a specific unwanted value.

```java
assertNotEquals(50.0, PricingUtils.priceWithVat(100.0, 0.0));
```

### `assertTrue`

Asserts that a boolean condition is true. Prefer domain-specific predicate methods.

```java
assertTrue(DataUtils.isEmail("dev@example.com"));
```

### `assertFalse`

Asserts that a boolean condition is false.

```java
assertFalse(DataUtils.isEmail("not-an-email"));
```

### `assertNull`

Checks that a value is `null` (e.g., lookup misses).

```java
assertNull(DataUtils.findUserEmail(java.util.Map.of("u1","a@b.com"), "missing"));
```

### `assertNotNull`

Checks that a value is not `null` (e.g., non-empty results, constructed objects).

```java
assertNotNull(DataUtils.formatName(" niall ", " mcguinness "));
```

### `assertSame`

Verifies two references point to the **same object** (identity, not equality).

```java
String x = "hi";
assertSame(x, DataUtils.passThrough(x));
```

### `assertNotSame`

Verifies two references are **not** the same object, even if contents match.

```java
assertNotSame(new double[]{}, PricingUtils.pricesWithVat(new double[]{}, 23.0));
```

### `assertArrayEquals`

Compares arrays element-by-element (same length and values).

```java
assertArrayEquals(new double[]{123.0, 61.5},
    PricingUtils.pricesWithVat(new double[]{100.0, 50.0}, 23.0));
```

### `assertIterableEquals`

Compares two `Iterable`s element-by-element in order.

```java
assertIterableEquals(java.util.List.of(0,1,2),
    DataUtils.firstN(java.util.List.of(0,1,2,3), 3));
```

### `assertLinesMatch`

Compares two lists of strings line-by-line; supports simple regex in expected lines.

```java
var expected = java.util.List.of("HELLO", "WORLD");
assertLinesMatch(expected, DataUtils.splitLines("HELLO
WORLD"));
```

### `assertThrows`

Asserts that executing code throws the given exception type (including subclasses). Capture the
exception to inspect message/state.

```java
ArithmeticException ex = assertThrows(
    ArithmeticException.class,
    () -> DataUtils.safeDivide(10, 0)
);
assertTrue(ex.getMessage().contains("divide by zero"));
```

### `assertThrowsExactly`

Like `assertThrows`, but requires the **exact** exception type (no subclasses).

```java
assertThrowsExactly(ArithmeticException.class, () -> DataUtils.safeDivide(10, 0));
```

### `assertDoesNotThrow`

Asserts that executing code completes without throwing. Returns the value of the lambda for
further checks.

```java
String s = assertDoesNotThrow(() -> DataUtils.formatName("zara", "lee"));
assertNotNull(s);
```

### `assertTimeout`

Asserts that code completes **within** a duration (non-preemptive: lets code finish then checks
elapsed).

```java
assertTimeout(java.time.Duration.ofMillis(150),
    () -> DataUtils.delayMillis(50));
```

### `assertTimeoutPreemptively`

Runs code in another thread and interrupts if it exceeds the duration (be careful with
threads/locks).

```java
assertTimeoutPreemptively(java.time.Duration.ofMillis(150),
    () -> DataUtils.delayMillis(50));
```

### `assertAll`

Groups multiple assertions so they all run; reports all failures together.

```java
assertAll("pricing",
    () -> assertEquals(123.0, PricingUtils.priceWithVat(100.0, 23.0)),
    () -> assertEquals(80.0, PricingUtils.applyDiscount(100.0, 20.0))
);
```

### `fail`

Forces a test to fail immediately with a message—use sparingly when a more specific assert
isn’t suitable.

```java
fail("Feature must be enabled for this test");
```

### `assertInstanceOf`

Asserts the object is an instance of a type and returns the casted value for further assertions.

```java
Number n = DataUtils.parseNumber("3.14");
Double d = assertInstanceOf(Double.class, n);
assertTrue(d > 3.0);
```

> **Tip**: For floating-point assertions where small precision errors are expected, use
> overloads with a delta/tolerance or compare `BigDecimal` values.

---

## JUnit Setup Reminders

- **Follow JetBrains’ JUnit tutorial:** [Set up and run JUnit tests in IntelliJ
  IDEA](https://www.jetbrains.com/help/idea/junit.html).
- **Add (latest) JUnit 5 to Maven (`pom.xml`):**

  ```xml
  <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.0</version>
    <scope>test</scope>
  </dependency>
  ```

- **Reload Maven after editing `pom.xml` (before any restart):**
  In IntelliJ, click the Maven tool window’s **Reload All Maven Projects** (🔄) button or
  accept the “Load Maven Changes” prompt. This triggers dependency download and re-indexing.
- **Aside (common fix):** If IntelliJ still shows *“Cannot resolve symbol junit”* after the
  **Maven reload**, **close and reopen IntelliJ** (or **File to Invalidate Caches /
  Restart...**). Restarting only helps **after** Maven has been reloaded.
- Run tests via gutter icons, the **Run** tool window, or `mvn test`.

---

## Appendix A

### Example Test Class Skeleton

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;

public class UtilsTest {

    @Test void pricingBasics() {
        assertEquals(123.0, PricingUtils.priceWithVat(100.0, 23.0));
        assertEquals(80.0, PricingUtils.applyDiscount(100.0, 20.0));
    }

    @Test void emailLookup() {
        assertNull(DataUtils.findUserEmail(Map.of("u1","a@b.com"), "missing"));
        assertTrue(DataUtils.isEmail("dev@example.com"));
    }
}
```

### Classes Under Test

### `PricingUtils.java`

```java
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PricingUtils {

    private PricingUtils() { }

    /** Adds VAT to a net price. Example: 100 @ 23% -> 123.00 */
    public static double priceWithVat(double net, double vatPct) {
        validatePct(vatPct);
        double gross = net * (1.0 + vatPct / 100.0);
        return roundMoney(gross);
    }

    /** Applies a percentage discount to a price. Example: 100 @ 20% -> 80.00 */
    public static double applyDiscount(double price, double discountPct) {
        validatePct(discountPct);
        double out = price * (1.0 - discountPct / 100.0);
        return roundMoney(out);
    }

    /** Bulk helper: returns a new array with VAT applied to each net price. */
    public static double[] pricesWithVat(double[] nets, double vatPct) {
        validatePct(vatPct);
        if (nets == null) return null;
        double[] out = new double[nets.length];
        for (int i = 0; i < nets.length; i++) {
            out[i] = priceWithVat(nets[i], vatPct);
        }
        return out;
    }

    private static void validatePct(double pct) {
        if (pct < 0.0 || pct > 100.0)
            throw new IllegalArgumentException("percentage must be in [0, 100]");
    }

    private static double roundMoney(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
```

### `DataUtils.java`

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class DataUtils {

    private static final Pattern EMAIL_RX =
            Pattern.compile("^[^@\s]+@[^@\s]+\.[^@\s]+$");

    private DataUtils() { }

    // --- Text helpers -------------------------------------------------------

    /** Trim and title-case a first + last name (very naive implementation). */
    public static String formatName(String first, String last) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(last, "last");
        return title(first.trim()) + " " + title(last.trim());
    }

    private static String title(String s) {
        if (s.isEmpty()) return s;
        String lower = s.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    /** Very simple email sanity check (not RFC-complete). */
    public static boolean isEmail(String email) {
        if (email == null) return false;
        return EMAIL_RX.matcher(email).matches();
    }

    /** Split into lines on any line separator; empty/`null` -> empty list. */
    public static List<String> splitLines(String text) {
        if (text == null || text.isEmpty()) return List.of();
        return List.of(text.split("\R"));
    }

    // --- Collections & parsing ---------------------------------------------

    /** Return the first N items of a list (or all items if shorter). */
    public static <T> List<T> firstN(List<T> list, int n) {
        Objects.requireNonNull(list, "list");
        int to = Math.max(0, Math.min(n, list.size()));
        return new ArrayList<>(list.subList(0, to));
    }

    /** Look up a user's email; return null if not found. */
    public static String findUserEmail(Map<String, String> directory, String userId) {
        Objects.requireNonNull(directory, "directory");
        return directory.get(userId);
    }

    /** Parse a number: integer tokens -> Integer; decimals -> Double. */
    public static Number parseNumber(String token) {
        Objects.requireNonNull(token, "token");
        if (token.contains(".")) return Double.valueOf(token);
        return Integer.valueOf(token);
    }

    /** Passthrough reference (useful for assertSame demonstration). */
    public static <T> T passThrough(T value) {
        return value;
    }

    // --- Misc ---------------------------------------------------------------

    /** Divide two ints; throws ArithmeticException on divide by zero. */
    public static int safeDivide(int a, int b) {
        if (b == 0) throw new ArithmeticException("divide by zero");
        return a / b;
    }

    /** Sleep for the requested milliseconds (used in timeout demos). */
    public static void delayMillis(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted", ie);
        }
    }
}
```

### Test Classes

### `PricingUtilsTest.java`

```java
package org.example;// PricingUtilsTest.java
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class PricingUtilsTest {

    @Test
    void priceWithVat_exactEquals() {
        assertEquals(123.0, PricingUtils.priceWithVat(100.0, 23.0));
    }

    @Test
    void applyDiscount_exactEquals() {
        assertEquals(80.0, PricingUtils.applyDiscount(100.0, 20.0));
    }

    @Test
    void priceWithVat_notEquals() {
        // 100 with 0% VAT stays 100.0, so it's definitely not 50.0
        assertNotEquals(50.0, PricingUtils.priceWithVat(100.0, 0.0));
    }

    @Test
    void pricesWithVat_arrayEquals() {
        assertArrayEquals(
                new double[] { 123.0, 61.5 },
                PricingUtils.pricesWithVat(new double[] { 100.0, 50.0 }, 23.0)
        );
    }

    @Test
    void pricesWithVat_notSameReference() {
        // Should return a new array (different reference) even if input is empty
        assertNotSame(new double[] {}, PricingUtils.pricesWithVat(new double[] {}, 23.0));
    }

    @Test
    void assertAll_groupedPricingChecks() {
        assertAll("pricing",
                () -> assertEquals(123.0, PricingUtils.priceWithVat(100.0, 23.0)),
                () -> assertEquals(80.0, PricingUtils.applyDiscount(100.0, 20.0))
        );
    }

    @Test
    void timeout_nonPreemptive() {
        assertTimeout(Duration.ofMillis(150), () -> DataUtils.delayMillis(50));
    }

    @Test
    void timeout_preemptive() {
        assertTimeoutPreemptively(Duration.ofMillis(150), () -> DataUtils.delayMillis(50));
    }

    @Test
    void fail_demo_doesNotTrigger() {
        // Demo of explicit failure without breaking the build
        boolean featureEnabled = true;
        if (!featureEnabled) fail("Feature must be enabled for this test");
        assertTrue(featureEnabled);
    }
}

```

### `DataUtilsTest.java`

```java
package org.example;// DataUtilsTest.java
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DataUtilsTest {

    @Test
    void isEmail_true() {
        assertTrue(DataUtils.isEmail("dev@example.com"));
    }

    @Test
    void isEmail_false() {
        assertFalse(DataUtils.isEmail("not-an-email"));
    }

    @Test
    void findUserEmail_nullWhenMissing() {
        assertNull(DataUtils.findUserEmail(Map.of("u1", "a@b.com"), "missing"));
    }

    @Test
    void formatName_notNullAndWellFormed() {
        assertNotNull(DataUtils.formatName(" niall ", " mcguinness "));
        assertEquals("Niall Mcguinness", DataUtils.formatName(" niall ", " mcguinness "));
    }

    @Test
    void passThrough_sameReference() {
        String x = "hi";
        assertSame(x, DataUtils.passThrough(x));
    }

    @Test
    void firstN_iterableEquals() {
        assertIterableEquals(
                List.of(0, 1, 2),
                DataUtils.firstN(List.of(0, 1, 2, 3), 3)
        );
    }

    @Test
    void splitLines_linesMatch() {
        var expected = List.of("HELLO", "WORLD");
        assertLinesMatch(expected, DataUtils.splitLines("HELLO\nWORLD"));
    }

    @Test
    void safeDivide_throwsArithmetic() {
        ArithmeticException ex = assertThrows(
                ArithmeticException.class,
                () -> DataUtils.safeDivide(10, 0)
        );
        assertTrue(ex.getMessage().contains("divide by zero"));
    }

    @Test
    void safeDivide_throwsExactlyArithmetic() {
        assertThrowsExactly(ArithmeticException.class, () -> DataUtils.safeDivide(10, 0));
    }

    @Test
    void formatName_doesNotThrow_andReturnsValue() {
        String s = assertDoesNotThrow(() -> DataUtils.formatName("zara", "lee"));
        assertNotNull(s);
    }

    @Test
    void parseNumber_instanceOfAndCasted() {
        Number n = DataUtils.parseNumber("3.14");
        Double d = assertInstanceOf(Double.class, n);
        assertTrue(d > 3.0);
    }
}

```
