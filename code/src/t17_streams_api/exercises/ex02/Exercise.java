package t17_streams_api.exercises.ex02;

import java.util.List;
import java.util.stream.Collectors;

public class Exercise {

    // Each word in capitals, joined with " | "
    public static String upperJoined(List<String> words) {
        if (words == null) {
            return "";
        }
        return words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining(" | "));
    }

    public static void run() {
        System.out.println(upperJoined(List.of("red", "green", "blue")));
    }
}
