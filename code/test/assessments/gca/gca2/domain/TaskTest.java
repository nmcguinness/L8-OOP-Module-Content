package assessments.gca.gca2.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Solution to t23 Exercise 01: tests for the guard clauses on Task.
 */
@DisplayName("t23 ex01 - Task validation")
class TaskTest {

    @Test
    void setTitle_validTitle_storesTrimmedValue() {
        // Arrange
        Task task = new Task();

        // Act
        task.setTitle("  Fix login  ");

        // Assert
        assertEquals("Fix login", task.getTitle());
    }

    @Test
    void setTitle_null_throwsIllegalArgumentException() {
        Task task = new Task();

        assertThrows(IllegalArgumentException.class, () -> task.setTitle(null));
    }

    @Test
    void setTitle_blank_throwsIllegalArgumentException() {
        Task task = new Task();

        assertThrows(IllegalArgumentException.class, () -> task.setTitle("   "));
    }

    @Test
    void setDescription_null_storesEmptyString() {
        Task task = new Task();

        task.setDescription(null);

        assertEquals("", task.getDescription());
    }

    @Test
    void constructor_blankTitle_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Task(1, " ", "d", false));
    }
}
