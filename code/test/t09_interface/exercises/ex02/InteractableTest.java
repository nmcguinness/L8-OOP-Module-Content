package t09_interface.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 ex02 - default methods on Interactable")
class InteractableTest {

    @Test
    void interactionRange_notOverridden_usesDefaultOfTwo() {
        assertEquals(2, new Door("door").interactionRange());
        assertEquals(2, new Chest("chest").interactionRange());
    }

    @Test
    void interactionRange_overriddenInNpc_isFour() {
        assertEquals(4, new NPC("Shopkeeper").interactionRange());
    }

    @Test
    void canInteract_usesEachImplementersRange() {
        assertFalse(new Door("door").canInteract(3));
        assertTrue(new NPC("Shopkeeper").canInteract(3));
    }

    @Test
    void canInteract_boundaryAndNegative() {
        assertTrue(new Door("door").canInteract(2));
        assertFalse(new Door("door").canInteract(-1));
    }
}
