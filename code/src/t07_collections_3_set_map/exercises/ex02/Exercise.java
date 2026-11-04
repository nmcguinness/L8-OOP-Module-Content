package t07_collections_3_set_map.exercises.ex02;

import java.util.HashMap;
import java.util.Map;

public class Exercise {

    // Maps each lowercased word to the number of times it appears
    public static Map<String, Integer> wordFrequency(String sentence) {
        Map<String, Integer> freq = new HashMap<>();
        if (sentence == null || sentence.isBlank())
            return freq;

        for (String word : sentence.trim().split("\\s+")) {
            freq.merge(word.toLowerCase(), 1, Integer::sum);
        }
        return freq;
    }

    public static void run() {
        System.out.println(wordFrequency("The cat saw the other cat"));
    }
}
