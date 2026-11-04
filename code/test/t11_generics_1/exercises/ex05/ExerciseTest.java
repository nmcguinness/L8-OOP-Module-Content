package t11_generics_1.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t11 ex05 - TopN")
class ExerciseTest {

    @Test
    void add_keepsTheLargestInDescendingOrder() {
        TopN<Integer> top = new TopN<>(3);
        for (int n : new int[] {10, 7, 25, 3, 19}) {
            top.add(n);
        }

        assertEquals(List.of(25, 19, 10), top.valuesDescending());
    }

    @Test
    void worksForStrings() {
        TopN<String> top = new TopN<>(2);
        top.add("pear");
        top.add("apple");
        top.add("zucchini");

        assertEquals(List.of("zucchini", "pear"), top.valuesDescending());
    }

    @Test
    void valuesDescending_isACopy() {
        TopN<Integer> top = new TopN<>(2);
        top.add(1);
        top.valuesDescending().clear();

        assertEquals(List.of(1), top.valuesDescending());
    }

    @Test
    void badCapacityOrNullItem_throws() {
        assertThrows(IllegalArgumentException.class, () -> new TopN<Integer>(0));
        assertThrows(NullPointerException.class, () -> new TopN<Integer>(1).add(null));
    }
}
