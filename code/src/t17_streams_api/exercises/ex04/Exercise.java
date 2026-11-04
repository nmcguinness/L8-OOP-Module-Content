package t17_streams_api.exercises.ex04;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Exercise {

    // Words grouped by length; shortest length first, words in their original order
    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        if (words == null) {
            return Map.of();
        }
        return words.stream()
                .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
    }

    public static void run() {
        System.out.println(groupByLength(List.of("cat", "horse", "dog", "mouse", "ox")));
    }
}
