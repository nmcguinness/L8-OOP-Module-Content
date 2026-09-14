package t12_generics_2.exercises.ex11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t12 ex11 - merging two producers into one consumer")
class ExerciseTest {

    @Test
    void merge_twoLists_concatenatesInOrder() {
        ArrayList<Number> out = new ArrayList<>();
        Merge.merge(List.of(1, 2), List.of(3), out);
        assertEquals(List.of(1, 2, 3), out);
    }

    @Test
    void merge_producersOfDifferentSubtypes_shareOneConsumer() {
        ArrayList<Object> out = new ArrayList<>();
        Merge.merge(List.of(1, 2), List.of(3.5), out);
        assertEquals(List.of(1, 2, 3.5), out);
    }

    @Test
    void merge_appendsToAnExistingDestination() {
        ArrayList<Object> out = new ArrayList<>(List.of("keep"));
        Merge.merge(List.of(1), List.of(2), out);
        assertEquals(List.of("keep", 1, 2), out);
    }

    @Test
    void merge_nullProducers_areSkippedRatherThanFatal() {
        ArrayList<Object> out = new ArrayList<>();
        Merge.merge(null, List.of(1), out);
        Merge.merge(List.of(2), null, out);
        Merge.merge(null, null, out);
        assertEquals(List.of(1, 2), out);
    }

    @Test
    void merge_nullConsumer_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> Merge.merge(List.of(1), List.of(2), null));
    }
}
