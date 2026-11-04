package t07_collections_3_set_map.exercises.ex04;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Exercise {

    // Groups words by lowercased first character; keys are in alphabetical order
    public static Map<Character, List<String>> groupByFirstLetter(List<String> words) {
        Map<Character, List<String>> groups = new TreeMap<>();
        if (words == null)
            return groups;

        for (String word : words) {
            if (word == null || word.isEmpty())
                continue;

            char key = Character.toLowerCase(word.charAt(0));
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
        }
        return groups;
    }

    public static void run() {
        List<String> words = List.of("pear", "apple", "Plum", "banana", "avocado");
        System.out.println(groupByFirstLetter(words));
    }
}
