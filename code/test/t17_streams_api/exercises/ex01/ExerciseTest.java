package t17_streams_api.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t17 ex01 - filter and sort with a stream")
class ExerciseTest {

    @Test
    void longNames_example_keepsLongerNamesInOrder() {
        List<String> products = List.of("Lamp", "Bookshelf", "Desk", "Armchair");

        assertEquals(List.of("Armchair", "Bookshelf"), Exercise.longNames(products, 4));
    }

    @Test
    void longNames_exactlyMinLength_isLeftOut() {
        assertTrue(Exercise.longNames(List.of("Desk"), 4).isEmpty());
    }

    @Test
    void longNames_emptyOrNull_returnsEmptyList() {
        assertTrue(Exercise.longNames(List.of(), 3).isEmpty());
        assertTrue(Exercise.longNames(null, 3).isEmpty());
    }

    @Test
    void longNames_doesNotChangeTheInput() {
        List<String> products = new ArrayList<>(List.of("Zebra rug", "Armchair"));

        Exercise.longNames(products, 1);

        assertEquals(List.of("Zebra rug", "Armchair"), products);
    }
}
