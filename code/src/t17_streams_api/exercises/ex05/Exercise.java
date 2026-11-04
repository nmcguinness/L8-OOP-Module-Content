package t17_streams_api.exercises.ex05;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class Exercise {

    public static int sum(List<Integer> scores) {
        if (scores == null) {
            return 0;
        }
        return scores.stream().mapToInt(Integer::intValue).sum();
    }

    // Empty when there are no scores: there is no average of nothing
    public static OptionalDouble average(List<Integer> scores) {
        if (scores == null) {
            return OptionalDouble.empty();
        }
        return scores.stream().mapToInt(Integer::intValue).average();
    }

    public static Optional<Integer> max(List<Integer> scores) {
        if (scores == null) {
            return Optional.empty();
        }
        return scores.stream().max(Integer::compare);
    }

    public static void run() {
        List<Integer> scores = List.of(70, 85, 90, 55);
        System.out.println("sum = " + sum(scores));
        System.out.println("average = " + average(scores));
        System.out.println("max = " + max(scores));
        System.out.println("average of none = " + average(List.of()));
    }
}
