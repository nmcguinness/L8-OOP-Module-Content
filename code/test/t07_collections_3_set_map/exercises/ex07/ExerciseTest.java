package t07_collections_3_set_map.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t07 ex07 - fix the broken set (equals and hashCode)")
class ExerciseTest {

    @Test
    void equalPositions_haveEqualHashCodes() {
        GridPos a = new GridPos(3, 4);
        GridPos b = new GridPos(3, 4);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashSet_containsLogicallyEqualPosition() {
        Set<GridPos> set = new HashSet<>();
        set.add(new GridPos(1, 1));

        assertTrue(set.contains(new GridPos(1, 1)));
    }

    @Test
    void countDistinct_repeatedCells_countsEachOnce() {
        List<GridPos> path = List.of(new GridPos(0, 0), new GridPos(0, 1), new GridPos(0, 0));

        assertEquals(2, Exercise.countDistinct(path));
    }

    @Test
    void countDistinct_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.countDistinct(null));
    }
}
