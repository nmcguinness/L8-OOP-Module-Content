package t08_inheritance.exercises.ex09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("t08 ex09 - equals and hashCode across a class hierarchy")
class ItemEqualityTest {

    @Test
    void equals_parentAndChildWithSameId_isFalseBothWays() {
        Item plain = new Item("sword");
        Item enchanted = new EnchantedItem("sword", 3);

        assertFalse(plain.equals(enchanted));
        assertFalse(enchanted.equals(plain));
    }

    @Test
    void equals_sameClassSameFields_isTrueWithEqualHashCodes() {
        Item a = new EnchantedItem("sword", 3);
        Item b = new EnchantedItem("sword", 3);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_differentLevel_isFalse() {
        assertNotEquals(new EnchantedItem("sword", 3), new EnchantedItem("sword", 4));
    }

    @Test
    void hashSet_keepsPlainAndEnchantedSeparate_andDropsTrueDuplicate() {
        Set<Item> bag = new HashSet<>();
        bag.add(new Item("sword"));
        bag.add(new EnchantedItem("sword", 3));
        bag.add(new EnchantedItem("sword", 3));

        assertEquals(2, bag.size());
    }

    @Test
    void toString_usesRuntimeClassName() {
        assertEquals("Item[sword]", new Item("sword").toString());
        assertEquals("EnchantedItem[sword]+3", new EnchantedItem("sword", 3).toString());
    }
}
