package t11_generics_1.exercises.ex04;

import java.util.List;

public class Exercise {

    public static void run() {
        List<String> names = List.of("Amy", "Bob", "Ada");
        List<Integer> numbers = List.of(5, 11, 12, 3);

        System.out.println("Starts with A: " + Filters.where(names, name -> name.startsWith("A")));
        System.out.println("Over 10: " + Filters.where(numbers, n -> n > 10));
        System.out.println("Original names: " + names);
    }
}
