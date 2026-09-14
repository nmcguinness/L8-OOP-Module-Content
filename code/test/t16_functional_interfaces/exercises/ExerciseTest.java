package t16_functional_interfaces.exercises;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t16 - Predicate, Function, Consumer, Supplier and BiConsumer helpers")
class ExerciseTest {

    private static final double TOLERANCE = 1e-9;

    private static Exercise.PlayerEvent event(String id, String type, int value) {
        return new Exercise.PlayerEvent(id, type, value);
    }

    private static final List<Exercise.PlayerEvent> EVENTS = List.of(
            event("p1", "KILL", 10),
            event("p2", "LOOT", 5),
            event("p3", "KILL", 20),
            event("p1", "MOVE", 1));

    // ---------- Predicate ----------

    @Test
    void filterItems_keepsOnlyMatchingElements() {
        List<Exercise.PlayerEvent> kills =
                Exercise.filterItems(EVENTS, e -> e.getEventType().equals("KILL"));

        assertEquals(List.of("p1", "p3"), Exercise.mapTo(kills, Exercise.PlayerEvent::getPlayerId));
    }

    @Test
    void filterItems_matchNothing_returnsEmptyList() {
        assertTrue(Exercise.filterItems(EVENTS, e -> false).isEmpty());
    }

    @Test
    void filterItems_nullInputs_returnEmptyListRatherThanThrowing() {
        assertTrue(Exercise.filterItems(null, e -> true).isEmpty());
        assertTrue(Exercise.filterItems(EVENTS, null).isEmpty());
    }

    @Test
    void filterItems_returnsAMutableListTheCallerCanSort() {
        List<Exercise.PlayerEvent> out = Exercise.filterItems(EVENTS, e -> true);
        out.sort(Comparator.comparingInt(Exercise.PlayerEvent::getValue));
        assertEquals(1, out.get(0).getValue());
    }

    @Test
    void combinedPredicates_andNarrowsTheSelection() {
        Predicate<Exercise.PlayerEvent> isKill = e -> e.getEventType().equals("KILL");
        Predicate<Exercise.PlayerEvent> isBig = e -> e.getValue() >= 15;

        List<Exercise.PlayerEvent> flagged = Exercise.filterItems(EVENTS, isKill.and(isBig));

        assertEquals(1, flagged.size());
        assertEquals("p3", flagged.get(0).getPlayerId());
    }

    // ---------- Function ----------

    @Test
    void mapTo_extractsTheChosenField() {
        assertEquals(List.of("KILL", "LOOT", "KILL", "MOVE"),
                Exercise.mapTo(EVENTS, Exercise.PlayerEvent::getEventType));
    }

    @Test
    void mapTo_preservesSizeIncludingDuplicates() {
        assertEquals(EVENTS.size(), Exercise.mapTo(EVENTS, Exercise.PlayerEvent::getPlayerId).size());
    }

    @Test
    void mapTo_nullInputs_returnEmptyList() {
        assertTrue(Exercise.mapTo(null, Exercise.PlayerEvent::getPlayerId).isEmpty());
        assertTrue(Exercise.<Exercise.PlayerEvent, String>mapTo(EVENTS, null).isEmpty());
    }

    // ---------- Consumer ----------

    @Test
    void forEach_runsTheActionOncePerElement() {
        Exercise.Counter counter = new Exercise.Counter();
        Exercise.forEach(EVENTS, e -> counter.increment());
        assertEquals(4, counter.getCount());
    }

    @Test
    void forEach_conditionalAction_countsOnlyMatches() {
        Exercise.Counter counter = new Exercise.Counter();
        Exercise.forEach(EVENTS, e -> {
            if (e.getEventType().equals("KILL")) {
                counter.increment();
            }
        });
        assertEquals(2, counter.getCount());
    }

    @Test
    void forEach_nullInputs_doNothing() {
        Exercise.Counter counter = new Exercise.Counter();
        Exercise.forEach(null, e -> counter.increment());
        Exercise.forEach(EVENTS, null);
        assertEquals(0, counter.getCount());
    }

    // ---------- Supplier ----------

    @Test
    void groupBy_bucketsByTheSelectedKey() {
        List<Exercise.BugTicket> tickets = List.of(
                new Exercise.BugTicket("BUG-01", "HIGH"),
                new Exercise.BugTicket("BUG-02", "LOW"),
                new Exercise.BugTicket("BUG-03", "HIGH"));

        Map<String, List<Exercise.BugTicket>> grouped =
                Exercise.groupBy(tickets, Exercise.BugTicket::getPriority, ArrayList::new);

        assertEquals(2, grouped.size());
        assertEquals(2, grouped.get("HIGH").size());
        assertEquals(1, grouped.get("LOW").size());
    }

    @Test
    void groupBy_theSupplierDecidesTheListImplementation() {
        // Swapping ArrayList::new for LinkedList::new changes what each bucket is.
        Map<String, List<Exercise.BugTicket>> grouped = Exercise.groupBy(
                List.of(new Exercise.BugTicket("BUG-01", "HIGH")),
                Exercise.BugTicket::getPriority,
                LinkedList::new);

        assertInstanceOf(LinkedList.class, grouped.get("HIGH"));
    }

    @Test
    void groupBy_nullInputs_returnEmptyMap() {
        assertTrue(Exercise.groupBy(null, Exercise.BugTicket::getPriority, ArrayList::new).isEmpty());
    }

    // ---------- BiConsumer ----------

    @Test
    void applyToEach_visitsEveryKeyAndValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);

        Map<String, Integer> copy = new HashMap<>();
        Exercise.applyToEach(map, copy::put);

        assertEquals(map, copy);
    }

    @Test
    void applyToEach_nullInputs_doNothing() {
        Map<String, Integer> copy = new HashMap<>();
        Exercise.applyToEach(null, copy::put);
        Exercise.applyToEach(new HashMap<String, Integer>(), null);
        assertTrue(copy.isEmpty());
    }

    // ---------- Comparator factory ----------

    private static List<Exercise.StoreItem> items() {
        return new ArrayList<>(List.of(
                new Exercise.StoreItem("Shield", 25.0, 10),
                new Exercise.StoreItem("Sword", 45.0, 3),
                new Exercise.StoreItem("Potion", 5.0, 50),
                new Exercise.StoreItem("Helmet", 30.0, 10)));
    }

    @Test
    void itemComparator_priceAsc_ordersCheapestFirst() {
        List<Exercise.StoreItem> list = items();
        list.sort(Exercise.itemComparator("price_asc"));
        assertEquals(List.of("Potion", "Shield", "Helmet", "Sword"),
                Exercise.mapTo(list, Exercise.StoreItem::getName));
    }

    @Test
    void itemComparator_priceDesc_ordersDearestFirst() {
        List<Exercise.StoreItem> list = items();
        list.sort(Exercise.itemComparator("price_desc"));
        assertEquals(45.0, list.get(0).getPrice(), TOLERANCE);
    }

    @Test
    void itemComparator_stockDesc_ordersByStock() {
        List<Exercise.StoreItem> list = items();
        list.sort(Exercise.itemComparator("stock_desc"));
        assertEquals("Potion", list.get(0).getName());
    }

    @Test
    void itemComparator_tieBreak_usesPriceWhenStockIsEqual() {
        // Shield and Helmet both have stock 10, so price decides between them.
        List<Exercise.StoreItem> list = items();
        list.sort(Exercise.itemComparator("stock_desc_then_price_asc"));
        assertEquals(List.of("Potion", "Shield", "Helmet", "Sword"),
                Exercise.mapTo(list, Exercise.StoreItem::getName));
    }

    @Test
    void itemComparator_unknownMode_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.itemComparator("size_asc"));
    }

    @Test
    void itemComparator_nullMode_throwsNullPointerException() {
        // switch on a null String throws before any case is reached.
        assertThrows(NullPointerException.class, () -> Exercise.itemComparator(null));
    }
}
