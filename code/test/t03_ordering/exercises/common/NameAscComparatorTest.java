package t03_ordering.exercises.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t03 - NameAscComparator orders products A-Z")
class NameAscComparatorTest {

    private final NameAscComparator comparator = new NameAscComparator();

    @Test
    void compare_earlierName_returnsNegative() {
        assertTrue(comparator.compare(new Product("Apple", 1.0, 4.0),
                                      new Product("Banana", 1.0, 4.0)) < 0);
    }

    @Test
    void compare_identicalNames_returnsZero() {
        assertEquals(0, comparator.compare(new Product("Mouse", 15.0, 4.2),
                                           new Product("Mouse", 99.0, 1.0)),
                     "only the name participates in this ordering");
    }

    @Test
    void sort_listOfProducts_ordersByNameAscending() {
        List<Product> items = new ArrayList<>(List.of(
                new Product("Monitor", 120.0, 4.1),
                new Product("Keyboard", 45.0, 4.5),
                new Product("Mouse", 15.0, 4.2)));

        items.sort(comparator);

        assertEquals(List.of("Keyboard", "Monitor", "Mouse"),
                     items.stream().map(Product::name).toList());
    }

    @Test
    void compare_isCaseSensitive_uppercaseSortsBeforeLowercase() {
        // Documents real behaviour: String.compareTo compares by Unicode value,
        // so "Zebra" comes before "apple". Use CASE_INSENSITIVE_ORDER to change this.
        assertTrue(comparator.compare(new Product("Zebra", 1.0, 1.0),
                                      new Product("apple", 1.0, 1.0)) < 0);
    }
}
