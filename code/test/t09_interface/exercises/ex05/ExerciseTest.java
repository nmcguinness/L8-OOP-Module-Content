package t09_interface.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 ex05 - comparator factory over an interface")
class ExerciseTest {

    private static List<String> namesOf(List<Named> items) {
        List<String> out = new ArrayList<>();
        for (Named n : items) {
            out.add(n.getName());
        }
        return out;
    }

    @Test
    void byNameAscending_sortsAlphabetically() {
        List<Named> items = new ArrayList<>(List.of(
                new GameItem("Sword"), new NPC("Anna"), new GameItem("Potion")));

        items.sort(NamedComparators.byNameAscending());

        assertEquals(List.of("Anna", "Potion", "Sword"), namesOf(items));
    }

    @Test
    void byNameAscending_ignoresCase() {
        List<Named> items = new ArrayList<>(List.of(
                new GameItem("banana"), new GameItem("Apple"), new GameItem("cherry")));

        items.sort(NamedComparators.byNameAscending());

        assertEquals(List.of("Apple", "banana", "cherry"), namesOf(items));
    }

    @Test
    void byNameLength_sortsShortestFirst() {
        List<Named> items = new ArrayList<>(List.of(
                new GameItem("Longsword"), new NPC("Al"), new GameItem("Bow")));

        items.sort(NamedComparators.byNameLength());

        assertEquals(List.of("Al", "Bow", "Longsword"), namesOf(items));
    }

    @Test
    void comparators_sortMixedImplementationsOfTheSameInterface() {
        // The comparator is typed on Named, so GameItem and NPC sort together.
        List<Named> items = new ArrayList<>(List.of(new NPC("Zara"), new GameItem("Axe")));

        items.sort(NamedComparators.byNameAscending());

        assertEquals(List.of("Axe", "Zara"), namesOf(items));
    }

    @Test
    void npc_implementsBothInterfaces() {
        NPC npc = new NPC("Guard");
        assertTrue(npc instanceof Named);
        assertTrue(npc instanceof IGreetPlayer);
        assertEquals("Guard", npc.getName());
    }
}
