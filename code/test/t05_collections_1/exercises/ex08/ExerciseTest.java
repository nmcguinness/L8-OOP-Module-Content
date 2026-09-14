package t05_collections_1.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t05 ex08 - merge two sorted lists")
class ExerciseTest {

    private ArrayList<Integer> list(int... xs) {
        ArrayList<Integer> out = new ArrayList<>();
        for (int x : xs) out.add(x);
        return out;
    }

    @Test
    void mergeSorted_twoSortedLists_returnsOneSortedList() {
        assertEquals(List.of(1, 2, 3, 4, 5, 6),
                     Exercise.mergeSorted(list(1, 3, 5), list(2, 4, 6)));
    }

    @Test
    void mergeSorted_duplicateValues_keepsBothCopies() {
        assertEquals(List.of(1, 1, 2, 2), Exercise.mergeSorted(list(1, 2), list(1, 2)));
    }

    @Test
    void mergeSorted_oneListEmpty_returnsTheOther() {
        assertEquals(List.of(1, 2, 3), Exercise.mergeSorted(list(1, 2, 3), list()));
        assertEquals(List.of(4, 5), Exercise.mergeSorted(list(), list(4, 5)));
    }

    @Test
    void mergeSorted_bothEmpty_returnsEmpty() {
        assertTrue(Exercise.mergeSorted(list(), list()).isEmpty());
    }

    @Test
    void mergeSorted_listsOfDifferentLengths_includesEveryElement() {
        assertEquals(List.of(1, 2, 3, 7, 9), Exercise.mergeSorted(list(1, 9), list(2, 3, 7)));
    }
}
