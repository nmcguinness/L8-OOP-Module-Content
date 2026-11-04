package t07_collections_3_set_map.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t07 ex01 - unique visitors with a HashSet")
class ExerciseTest {

    @Test
    void countUniqueVisitors_repeatedIps_countsEachOnce() {
        List<String> log = List.of("1,10.0.0.1", "2,10.0.0.2", "3,10.0.0.1");

        assertEquals(2, Exercise.countUniqueVisitors(log));
    }

    @Test
    void countUniqueVisitors_emptyLog_returnsZero() {
        assertEquals(0, Exercise.countUniqueVisitors(List.of()));
    }

    @Test
    void countUniqueVisitors_malformedAndNullEntries_areSkipped() {
        List<String> log = new ArrayList<>(List.of("no comma here", "1,10.0.0.9", "2,"));
        log.add(null);

        assertEquals(1, Exercise.countUniqueVisitors(log));
    }

    @Test
    void countUniqueVisitors_nullList_throws() {
        assertThrows(IllegalArgumentException.class, () -> Exercise.countUniqueVisitors(null));
    }
}
