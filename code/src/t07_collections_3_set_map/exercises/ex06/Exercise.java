package t07_collections_3_set_map.exercises.ex06;

public class Exercise {

    public static void run() {
        SimpleCache cache = new SimpleCache(2);
        cache.put("a", "apple");
        cache.put("b", "banana");

        // Reading "a" makes "b" the least recently used entry
        cache.get("a");
        cache.put("c", "cherry");

        System.out.println("contains a? " + cache.containsKey("a"));
        System.out.println("contains b? " + cache.containsKey("b"));
        System.out.println("contains c? " + cache.containsKey("c"));
        System.out.println("get(\"zzz\"): " + cache.get("zzz"));
    }
}
