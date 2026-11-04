package t07_collections_3_set_map.exercises.ex06;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A fixed-capacity least-recently-used (LRU) cache from String keys to String values.
 * The first key in the LinkedHashMap is always the least recently used one.
 */
public class SimpleCache {

    private final Map<String, String> entries = new LinkedHashMap<>();
    private final int capacity;

    public SimpleCache(int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException("capacity must be at least 1");
        this.capacity = capacity;
    }

    public void put(String key, String value) {
        if (key == null)
            throw new IllegalArgumentException("key must not be null");

        if (entries.containsKey(key)) {
            // Remove first, so the re-insert moves the key to the most-recent end
            entries.remove(key);
        } else if (entries.size() >= capacity) {
            String leastRecent = entries.keySet().iterator().next();
            entries.remove(leastRecent);
        }
        entries.put(key, value);
    }

    public Optional<String> get(String key) {
        if (!entries.containsKey(key))
            return Optional.empty();

        // A read counts as a use: move the key to the most-recent end
        String value = entries.remove(key);
        entries.put(key, value);
        return Optional.ofNullable(value);
    }

    public int size() {
        return entries.size();
    }

    public boolean containsKey(String key) {
        return entries.containsKey(key);
    }
}
