package t06_collections_2.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t06 ex08 - merge B into A in sorted order")
class ExerciseTest {

    private LinkedList<Integer> linked(int... xs) {
        LinkedList<Integer> out = new LinkedList<>();
        for (int x : xs) out.add(x);
        return out;
    }

    @Test
    void mergeIntoA_interleavedValues_leavesASorted() {
        LinkedList<Integer> a = linked(1, 3, 5);
        Exercise.mergeIntoA(a, linked(2, 4, 6));
        assertEquals(List.of(1, 2, 3, 4, 5, 6), a);
    }

    @Test
    void mergeIntoA_modifiesAInPlace_andLeavesBAlone() {
        LinkedList<Integer> a = linked(1, 5);
        LinkedList<Integer> b = linked(3);
        Exercise.mergeIntoA(a, b);
        assertEquals(List.of(1, 3, 5), a);
        assertEquals(List.of(3), b, "B should not be modified");
    }

    @Test
    void mergeIntoA_bValuesAllLarger_appendsThemAtTheEnd() {
        LinkedList<Integer> a = linked(1, 2);
        Exercise.mergeIntoA(a, linked(8, 9));
        assertEquals(List.of(1, 2, 8, 9), a);
    }

    @Test
    void mergeIntoA_emptyB_leavesAUnchanged() {
        LinkedList<Integer> a = linked(1, 2, 3);
        Exercise.mergeIntoA(a, linked());
        assertEquals(List.of(1, 2, 3), a);
    }
}
