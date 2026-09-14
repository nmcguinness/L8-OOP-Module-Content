package t03_ordering.exercises.ex01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Exercise {
    public static void run() {
        List<Score> scores = new ArrayList<>();
        scores.add(new Score("Zara", 90));
        scores.add(new Score("Amy", 95));
        scores.add(new Score("Liam", 95)); // tie on value
        scores.add(new Score("Noah", 70));
        scores.add(new Score("Eve", 88));

        // Natural order: value desc, then player name asc
        Collections.sort(scores); // or scores.sort(null);

        System.out.println(scores);

        // Quick checks (should be true):
        System.out.println(scores.get(0).getValue() >= scores.get(1).getValue());
        System.out.println(scores.get(scores.size()-1).getValue() <= scores.get(scores.size()-2).getValue());
    }


}
