package t04_equality_hashing.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Customer defines identity by id alone, so two customers with the same id are
 * equal regardless of email. These tests check the equals/hashCode contract.
 */
@DisplayName("t04 ex06 - Customer equality by id")
class ExerciseTest {

    @Test
    void equals_sameId_differentEmail_areEqual() {
        assertTrue(new Exercise.Customer("C1", "a@example.com")
                   .equals(new Exercise.Customer("C1", "different@example.com")));
    }

    @Test
    void equals_differentId_sameEmail_areNotEqual() {
        assertFalse(new Exercise.Customer("C1", "a@example.com")
                    .equals(new Exercise.Customer("C2", "a@example.com")));
    }

    @Test
    void hashCode_equalObjects_produceEqualHashCodes() {
        // The contract that hashed collections depend on
        assertEquals(new Exercise.Customer("C1", "a@example.com").hashCode(),
                     new Exercise.Customer("C1", "other@example.com").hashCode());
    }

    @Test
    void equals_isReflexive() {
        Exercise.Customer a = new Exercise.Customer("C1", "a@example.com");
        assertTrue(a.equals(a));
    }

    @Test
    void equals_isSymmetric() {
        Exercise.Customer a = new Exercise.Customer("C1", "a@example.com");
        Exercise.Customer b = new Exercise.Customer("C1", "b@example.com");
        assertTrue(a.equals(b) && b.equals(a));
    }

    @Test
    void equals_null_returnsFalse() {
        assertFalse(new Exercise.Customer("C1", "a@example.com").equals(null));
    }

    @Test
    void equals_differentType_returnsFalse() {
        assertFalse(new Exercise.Customer("C1", "a@example.com").equals("C1"));
    }

    @Test
    void hashSet_customersWithSameId_storesOnlyOne() {
        Set<Exercise.Customer> set = new HashSet<>();
        set.add(new Exercise.Customer("C1", "a@example.com"));
        set.add(new Exercise.Customer("C1", "different@example.com"));
        assertEquals(1, set.size(), "same id means same customer");
    }
}
