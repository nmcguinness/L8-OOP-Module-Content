package assessments.gca.gca2.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Solution to t23 Exercise 02: tests for Task.equals and Task.hashCode.
 */
@DisplayName("t23 ex02 - Task equality and hashing")
class TaskEqualityTest {

    @Test
    void equals_identicalFields_isTrue() {
        Task a = new Task(1, "Fix login", "desc", false);
        Task b = new Task(1, "Fix login", "desc", false);

        assertEquals(a, b);
    }

    @Test
    void equals_differentTaskIdOnly_isFalse() {
        Task a = new Task(1, "Fix login", "desc", false);
        Task b = new Task(2, "Fix login", "desc", false);

        assertNotEquals(a, b);
    }

    @Test
    void hashCode_equalTasks_haveEqualHashCodes() {
        Task a = new Task(1, "Fix login", "desc", false);
        Task b = new Task(1, "Fix login", "desc", false);

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_nullOrOtherType_isFalse() {
        Task a = new Task(1, "Fix login", "desc", false);

        assertFalse(a.equals(null));
        assertFalse(a.equals("some string"));
    }

    @Test
    void hashSet_twoEqualTasks_storesOne() {
        Set<Task> set = new HashSet<>();

        set.add(new Task(1, "Fix login", "desc", false));
        set.add(new Task(1, "Fix login", "desc", false));

        assertEquals(1, set.size());
    }
}
