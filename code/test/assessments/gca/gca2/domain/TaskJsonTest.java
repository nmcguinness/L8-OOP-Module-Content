package assessments.gca.gca2.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Solution to t23 Exercise 05: JSON round trips for Task.
 */
@DisplayName("t23 ex05 - Task JSON round trips")
class TaskJsonTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void task_roundTrip_equalsOriginal() throws Exception {
        Task original = new Task(7, "Fix login", "Users cannot sign in", true);

        String json = mapper.writeValueAsString(original);
        Task restored = mapper.readValue(json, Task.class);

        assertEquals(original, restored);
    }

    @Test
    void taskList_roundTrip_preservesSizeAndFirstElement() throws Exception {
        List<Task> original = List.of(
                new Task(1, "First", "a", false),
                new Task(2, "Second", "b", true));

        String json = mapper.writeValueAsString(original);
        List<Task> restored = mapper.readValue(json, new TypeReference<List<Task>>() { });

        assertEquals(2, restored.size());
        assertEquals(original.get(0), restored.get(0));
    }
}
