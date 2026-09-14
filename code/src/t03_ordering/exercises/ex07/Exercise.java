package t03_ordering.exercises.ex07;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Exercise {
    public static void run() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Write report", LocalDate.of(2025, 12, 1)));
        tasks.add(new Task("Fix bug", LocalDate.of(2025, 11, 10)));
        tasks.add(new Task("Refactor", LocalDate.of(2025, 11, 20)));

        Collections.sort(tasks); // natural order by due date asc
        System.out.println(tasks);
    }
}
