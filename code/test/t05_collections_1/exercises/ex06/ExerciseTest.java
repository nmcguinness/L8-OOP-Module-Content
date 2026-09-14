package t05_collections_1.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t05 ex06 - find and update tasks by title")
class ExerciseTest {

    private ArrayList<Exercise.Task> sampleTasks() {
        ArrayList<Exercise.Task> tasks = new ArrayList<>();
        tasks.add(new Exercise.Task("Login", 2));
        tasks.add(new Exercise.Task("Save", 1));
        return tasks;
    }

    @Test
    void indexOfTitle_titlePresent_returnsItsIndex() {
        assertEquals(1, Exercise.indexOfTitle(sampleTasks(), "Save"));
    }

    @Test
    void indexOfTitle_titleAbsent_returnsMinusOne() {
        assertEquals(-1, Exercise.indexOfTitle(sampleTasks(), "Delete"));
    }

    @Test
    void indexOfTitle_nullListOrTitle_returnsMinusOne() {
        assertEquals(-1, Exercise.indexOfTitle(null, "Save"));
        assertEquals(-1, Exercise.indexOfTitle(sampleTasks(), null));
    }

    @Test
    void updatePriority_existingTitle_changesPriorityAndReturnsTrue() {
        ArrayList<Exercise.Task> tasks = sampleTasks();
        assertTrue(Exercise.updatePriority(tasks, "Save", 3));
        assertEquals(3, tasks.get(1).priority);
    }

    @Test
    void updatePriority_unknownTitle_returnsFalseAndChangesNothing() {
        ArrayList<Exercise.Task> tasks = sampleTasks();
        assertFalse(Exercise.updatePriority(tasks, "Nope", 9));
        assertEquals(2, tasks.get(0).priority);
        assertEquals(1, tasks.get(1).priority);
    }
}
