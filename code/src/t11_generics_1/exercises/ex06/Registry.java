package t11_generics_1.exercises.ex06;

import java.util.HashMap;
import java.util.Map;

/**
 * A map that refuses null keys. Values may be null.
 */
public class Registry<K, V> {

    private final Map<K, V> map = new HashMap<>();

    public void put(K key, V value) {
        requireKey(key);
        map.put(key, value);
    }

    public V get(K key) {
        requireKey(key);
        return map.get(key);
    }

    public boolean containsKey(K key) {
        requireKey(key);
        return map.containsKey(key);
    }

    private static void requireKey(Object key) {
        if (key == null) {
            throw new NullPointerException("key must not be null");
        }
    }
}
