package t16_functional_interfaces.exercises;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Exercise {

    public static void run() {
        exercise01();
        exercise02();
        exercise03();
        exercise04();
        exercise05();
        exercise06();
        exercise07();
        exercise08();
    }

    // -------------------------------------------------------------------------
    // Exercise 01 - Filter player events using Predicate
    // -------------------------------------------------------------------------

    static class PlayerEvent {
        private String _playerId;
        private String _eventType;
        private int _value;

        public PlayerEvent(String playerId, String eventType, int value) {
            _playerId  = playerId;
            _eventType = eventType;
            _value     = value;
        }

        public String getPlayerId()  { return _playerId; }
        public String getEventType() { return _eventType; }
        public int    getValue()     { return _value; }

        @Override
        public String toString() {
            return _playerId + " | " + _eventType + " | " + _value;
        }
    }

    static <T> List<T> filterItems(List<T> items, Predicate<T> rule) {
        if (items == null || rule == null)
            return new ArrayList<>();

        List<T> result = new ArrayList<>();
        for (T item : items)
            if (rule.test(item))
                result.add(item);
        return result;
    }

    static void exercise01() {
        System.out.println("=== Exercise 01 ===");

        List<PlayerEvent> events = List.of(
            new PlayerEvent("p1", "KILL", 10),
            new PlayerEvent("p2", "LOOT", 5),
            new PlayerEvent("p3", "KILL", 20),
            new PlayerEvent("p1", "MOVE", 1)
        );

        List<PlayerEvent> kills = filterItems(events, e -> e.getEventType().equals("KILL"));
        System.out.println("KILL events:");
        for (PlayerEvent e : kills) System.out.println("  " + e);

        List<PlayerEvent> bigOnes = filterItems(events, e -> e.getValue() >= 10);
        System.out.println("value >= 10:");
        for (PlayerEvent e : bigOnes) System.out.println("  " + e);
    }

    // -------------------------------------------------------------------------
    // Exercise 02 - Combine predicates to detect suspicious activity
    // -------------------------------------------------------------------------

    static void exercise02() {
        System.out.println("=== Exercise 02 ===");

        List<PlayerEvent> events = List.of(
            new PlayerEvent("p1", "KILL", 10),
            new PlayerEvent("p2", "KILL", 20),
            new PlayerEvent("p3", "LOOT", 15),
            new PlayerEvent("p4", "KILL", 5)
        );

        Predicate<PlayerEvent> isKill    = e -> e.getEventType().equals("KILL");
        Predicate<PlayerEvent> isBig     = e -> e.getValue() >= 15;
        Predicate<PlayerEvent> isNotLoot = e -> !e.getEventType().equals("LOOT");

        Predicate<PlayerEvent> suspicious = isKill.and(isBig).and(isNotLoot);

        List<PlayerEvent> flagged = filterItems(events, suspicious);
        System.out.println("Suspicious events:");
        for (PlayerEvent e : flagged) System.out.println("  " + e);
    }

    // -------------------------------------------------------------------------
    // Exercise 03 - Extract fields using Function
    // -------------------------------------------------------------------------

    static <T, R> List<R> mapTo(List<T> items, Function<T, R> selector) {
        if (items == null || selector == null)
            return new ArrayList<>();

        List<R> result = new ArrayList<>();
        for (T item : items)
            result.add(selector.apply(item));
        return result;
    }

    static void exercise03() {
        System.out.println("=== Exercise 03 ===");

        List<PlayerEvent> events = List.of(
            new PlayerEvent("p1", "KILL", 10),
            new PlayerEvent("p2", "LOOT", 5)
        );

        List<String> playerIds  = mapTo(events, PlayerEvent::getPlayerId);
        List<String> eventTypes = mapTo(events, PlayerEvent::getEventType);

        System.out.println("Player IDs:  " + playerIds);
        System.out.println("Event types: " + eventTypes);
    }

    // -------------------------------------------------------------------------
    // Exercise 04 - Perform side effects using Consumer
    // -------------------------------------------------------------------------

    static class Counter {
        private int _count = 0;
        public void increment() { _count++; }
        public int getCount()   { return _count; }
    }

    static <T> void forEach(List<T> items, Consumer<T> action) {
        if (items == null || action == null)
            return;
        for (T item : items)
            action.accept(item);
    }

    static void exercise04() {
        System.out.println("=== Exercise 04 ===");

        List<PlayerEvent> events = List.of(
            new PlayerEvent("p1", "KILL", 10),
            new PlayerEvent("p2", "LOOT", 5),
            new PlayerEvent("p3", "KILL", 20)
        );

        System.out.println("All events:");
        forEach(events, e -> System.out.println("  " + e));

        Counter counter = new Counter();
        forEach(events, e -> { if (e.getEventType().equals("KILL")) counter.increment(); });
        System.out.println("KILL count: " + counter.getCount());
    }

    // -------------------------------------------------------------------------
    // Exercise 05 - Group items using Supplier
    // -------------------------------------------------------------------------

    static class BugTicket {
        private String _id;
        private String _priority;

        public BugTicket(String id, String priority) {
            _id       = id;
            _priority = priority;
        }

        public String getId()       { return _id; }
        public String getPriority() { return _priority; }

        @Override
        public String toString() { return _id + "(" + _priority + ")"; }
    }

    static <T, K> Map<K, List<T>> groupBy(List<T> items,
                                           Function<T, K> keySelector,
                                           Supplier<List<T>> listFactory) {
        if (items == null || keySelector == null || listFactory == null)
            return new HashMap<>();

        Map<K, List<T>> result = new HashMap<>();
        for (T item : items) {
            K key = keySelector.apply(item);
            result.computeIfAbsent(key, k -> listFactory.get()).add(item);
        }
        return result;
    }

    static void exercise05() {
        System.out.println("=== Exercise 05 ===");

        List<BugTicket> tickets = List.of(
            new BugTicket("BUG-01", "HIGH"),
            new BugTicket("BUG-02", "LOW"),
            new BugTicket("BUG-03", "HIGH"),
            new BugTicket("BUG-04", "MEDIUM"),
            new BugTicket("BUG-05", "LOW")
        );

        Map<String, List<BugTicket>> grouped =
            groupBy(tickets, BugTicket::getPriority, ArrayList::new);

        for (Map.Entry<String, List<BugTicket>> entry : grouped.entrySet())
            System.out.println(entry.getKey() + " -> " + entry.getValue());
    }

    // -------------------------------------------------------------------------
    // Exercise 06 - Update a map using BiConsumer
    // -------------------------------------------------------------------------

    static <K, V> void applyToEach(Map<K, V> map, BiConsumer<K, V> action) {
        if (map == null || action == null)
            return;
        for (Map.Entry<K, V> entry : map.entrySet())
            action.accept(entry.getKey(), entry.getValue());
    }

    static void exercise06() {
        System.out.println("=== Exercise 06 ===");

        List<BugTicket> tickets = List.of(
            new BugTicket("BUG-01", "HIGH"),
            new BugTicket("BUG-02", "LOW"),
            new BugTicket("BUG-03", "HIGH"),
            new BugTicket("BUG-04", "MEDIUM")
        );

        Map<String, List<BugTicket>> grouped =
            groupBy(tickets, BugTicket::getPriority, ArrayList::new);

        System.out.println("Counts:");
        applyToEach(grouped, (k, v) -> System.out.println("  " + k + ": " + v.size()));

        Map<String, String> rename = new HashMap<>();
        rename.put("HIGH", "P1");
        rename.put("MEDIUM", "P2");
        rename.put("LOW", "P3");

        Map<String, List<BugTicket>> remapped = new HashMap<>();
        applyToEach(grouped, (k, v) -> remapped.put(rename.getOrDefault(k, k), v));

        System.out.println("Remapped keys: " + remapped.keySet());
    }

    // -------------------------------------------------------------------------
    // Exercise 07 - Comparator factory
    // -------------------------------------------------------------------------

    static class StoreItem {
        private String _name;
        private double _price;
        private int    _stock;

        public StoreItem(String name, double price, int stock) {
            _name  = name;
            _price = price;
            _stock = stock;
        }

        public String getName()  { return _name; }
        public double getPrice() { return _price; }
        public int    getStock() { return _stock; }

        @Override
        public String toString() {
            return _name + "(\u20ac" + _price + ", stock=" + _stock + ")";
        }
    }

    static Comparator<StoreItem> itemComparator(String mode) {
        switch (mode) {
            case "price_asc":
                return Comparator.comparingDouble(StoreItem::getPrice);
            case "price_desc":
                return Comparator.comparingDouble(StoreItem::getPrice).reversed();
            case "stock_desc":
                return Comparator.comparingInt(StoreItem::getStock).reversed();
            case "stock_desc_then_price_asc":
                return Comparator.comparingInt(StoreItem::getStock).reversed()
                                 .thenComparingDouble(StoreItem::getPrice);
            default:
                throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }

    static void exercise07() {
        System.out.println("=== Exercise 07 ===");

        List<StoreItem> items = new ArrayList<>(List.of(
            new StoreItem("Shield",  25.0, 10),
            new StoreItem("Sword",   45.0,  3),
            new StoreItem("Potion",   5.0, 50),
            new StoreItem("Helmet",  30.0, 10)
        ));

        items.sort(itemComparator("price_asc"));
        System.out.println("price_asc:                 " + items);

        items.sort(itemComparator("price_desc"));
        System.out.println("price_desc:                " + items);

        items.sort(itemComparator("stock_desc"));
        System.out.println("stock_desc:                " + items);

        items.sort(itemComparator("stock_desc_then_price_asc"));
        System.out.println("stock_desc_then_price_asc: " + items);
    }

    // -------------------------------------------------------------------------
    // Exercise 08 - Ticket pipeline
    // -------------------------------------------------------------------------

    static class SupportTicket {
        private int    _id;
        private String _category;
        private int    _severity;

        public SupportTicket(int id, String category, int severity) {
            _id       = id;
            _category = category;
            _severity = severity;
        }

        public int    getId()       { return _id; }
        public String getCategory() { return _category; }
        public int    getSeverity() { return _severity; }

        @Override
        public String toString() {
            return "T#" + _id + "[" + _category + ", sev=" + _severity + "]";
        }
    }

    static void exercise08() {
        System.out.println("=== Exercise 08 ===");

        List<SupportTicket> tickets = List.of(
            new SupportTicket(1,  "NETWORK",  9),
            new SupportTicket(2,  "UI",       5),
            new SupportTicket(3,  "DB",       8),
            new SupportTicket(4,  "NETWORK",  7),
            new SupportTicket(5,  "UI",       6),
            new SupportTicket(6,  "DB",       9),
            new SupportTicket(7,  "AUTH",     3),
            new SupportTicket(8,  "AUTH",     8),
            new SupportTicket(9,  "NETWORK",  2),
            new SupportTicket(10, "DB",       7)
        );

        // Filter: severity >= 7
        List<SupportTicket> filtered = filterItems(tickets, t -> t.getSeverity() >= 7);

        // Sort: severity desc, id asc
        filtered.sort(
            Comparator.comparingInt(SupportTicket::getSeverity).reversed()
                      .thenComparingInt(SupportTicket::getId)
        );

        // Group by category
        Map<String, List<SupportTicket>> grouped =
            groupBy(filtered, SupportTicket::getCategory, ArrayList::new);

        // Print top 5
        System.out.println("Top 5 high-severity tickets:");
        int limit = Math.min(5, filtered.size());
        for (int i = 0; i < limit; i++)
            System.out.println("  " + filtered.get(i));

        // Grouped counts
        System.out.println("Grouped counts:");
        applyToEach(grouped, (k, v) -> System.out.println("  " + k + ": " + v.size()));

        // Handle each via Consumer
        Consumer<SupportTicket> handler = t -> System.out.println("  Handled: " + t);
        System.out.println("Handled tickets:");
        forEach(filtered, handler);
    }
}
