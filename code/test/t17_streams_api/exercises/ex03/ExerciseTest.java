package t17_streams_api.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t17 ex03 - flatMap and distinct")
class ExerciseTest {

    @Test
    void allUniqueTags_example() {
        List<String> lines = List.of("java,oop,design", "oop, testing", "design");

        assertEquals(List.of("design", "java", "oop", "testing"), Exercise.allUniqueTags(lines));
    }

    @Test
    void allUniqueTags_spacesAndEmptyTags_areIgnored() {
        assertEquals(List.of("a", "b"), Exercise.allUniqueTags(List.of(" a ,, b ,")));
    }

    @Test
    void allUniqueTags_emptyOrNull_returnsEmptyList() {
        assertTrue(Exercise.allUniqueTags(List.of()).isEmpty());
        assertTrue(Exercise.allUniqueTags(null).isEmpty());
    }
}
