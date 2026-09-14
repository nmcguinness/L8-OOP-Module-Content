package t05_collections_1.exercises.ex09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t05 ex09 - group names by first letter")
class ExerciseTest {

    private ArrayList<String> list(String... xs) {
        return new ArrayList<>(List.of(xs));
    }

    @Test
    void groupByFirstLetter_alwaysReturns26Buckets() {
        assertEquals(26, Exercise.groupByFirstLetter(list("alice")).size());
    }

    @Test
    void groupByFirstLetter_placesNamesInTheirLetterBucket() {
        List<ArrayList<String>> g = Exercise.groupByFirstLetter(list("alice", "amy", "bob"));
        assertEquals(List.of("alice", "amy"), g.get(0), "bucket A");
        assertEquals(List.of("bob"), g.get(1), "bucket B");
    }

    @Test
    void groupByFirstLetter_isCaseInsensitiveOnTheFirstLetter() {
        List<ArrayList<String>> g = Exercise.groupByFirstLetter(list("Alice", "amy"));
        assertEquals(2, g.get(0).size(), "upper and lower case A share a bucket");
    }

    @Test
    void groupByFirstLetter_skipsEmptyAndNonLetterNames() {
        List<ArrayList<String>> g = Exercise.groupByFirstLetter(list("", "9lives", "zed"));
        int total = 0;
        for (List<String> bucket : g) total += bucket.size();
        assertEquals(1, total, "only the name starting with a letter is grouped");
        assertEquals(List.of("zed"), g.get(25), "bucket Z");
    }

    @Test
    void groupByFirstLetter_nullInput_returnsEmptyBuckets() {
        List<ArrayList<String>> g = Exercise.groupByFirstLetter(null);
        assertEquals(26, g.size());
        for (List<String> bucket : g) assertTrue(bucket.isEmpty());
    }
}
