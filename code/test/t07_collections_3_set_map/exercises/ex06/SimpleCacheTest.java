package t07_collections_3_set_map.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t07 ex06 - least-recently-used cache")
class SimpleCacheTest {

    @Test
    void put_overCapacity_evictsLeastRecentlyUsed() {
        SimpleCache cache = new SimpleCache(2);
        cache.put("a", "1");
        cache.put("b", "2");

        cache.put("c", "3");

        assertFalse(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
    }

    @Test
    void get_marksEntryAsRecentlyUsed() {
        SimpleCache cache = new SimpleCache(2);
        cache.put("a", "1");
        cache.put("b", "2");

        cache.get("a");
        cache.put("c", "3");

        assertTrue(cache.containsKey("a"));
        assertFalse(cache.containsKey("b"));
    }

    @Test
    void put_existingKeyWhenFull_replacesValueWithoutEvicting() {
        SimpleCache cache = new SimpleCache(2);
        cache.put("a", "1");
        cache.put("b", "2");

        cache.put("a", "updated");

        assertEquals(2, cache.size());
        assertEquals(Optional.of("updated"), cache.get("a"));
        assertTrue(cache.containsKey("b"));
    }

    @Test
    void get_missingKey_returnsEmpty() {
        assertEquals(Optional.empty(), new SimpleCache(1).get("missing"));
    }

    @Test
    void constructor_zeroCapacity_throws() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleCache(0));
    }
}
