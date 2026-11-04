package t11_generics_1.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t11 ex01 - Object container versus generic container")
class ExerciseTest {

    @Test
    void legacy_wrongCast_failsOnlyAtRunTime() {
        InventoryLegacy legacy = new InventoryLegacy();
        legacy.add(123);

        assertThrows(ClassCastException.class, () -> {
            String text = (String) legacy.get(0);
        });
    }

    @Test
    void generic_getReturnsTheTypeWithoutACast() {
        Inventory<String> inventory = new Inventory<>();
        inventory.add("shield");

        String item = inventory.get(0);
        assertEquals("SHIELD", item.toUpperCase());
    }
}
