package t17_streams_api.exercises.ex03;

import java.util.Arrays;
import java.util.List;

public class Exercise {

    // Every tag that appears in any line, once each, in alphabetical order
    public static List<String> allUniqueTags(List<String> tagLines) {
        if (tagLines == null) {
            return List.of();
        }
        return tagLines.stream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .distinct()
                .sorted()
                .toList();
    }

    public static void run() {
        List<String> lines = List.of("java,oop,design", "oop, testing", "design");
        System.out.println(allUniqueTags(lines));
    }
}
