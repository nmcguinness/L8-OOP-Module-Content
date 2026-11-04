package t07_collections_3_set_map.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t07 ex04 - group by first letter with a TreeMap")
class ExerciseTest {

    @Test
    void groupByFirstLetter_words_keysAlphabeticalAndInputOrderKept() {
        Map<Character, List<String>> groups =
                Exercise.groupByFirstLetter(List.of("pear", "apple", "Plum", "avocado"));

        assertEquals(List.of('a', 'p'), new ArrayList<>(groups.keySet()));
        assertEquals(List.of("apple", "avocado"), groups.get('a'));
        assertEquals(List.of("pear", "Plum"), groups.get('p'));
    }

    @Test
    void groupByFirstLetter_nullAndEmptyWords_areSkipped() {
        List<String> words = new ArrayList<>(List.of("", "kiwi"));
        words.add(null);

        assertEquals(Map.of('k', List.of("kiwi")), Exercise.groupByFirstLetter(words));
    }

    @Test
    void groupByFirstLetter_nullList_returnsEmptyMap() {
        assertTrue(Exercise.groupByFirstLetter(null).isEmpty());
    }
}
