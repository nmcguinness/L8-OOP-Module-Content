package t12_generics_2.exercises.ex09;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static void run() {
        List<String> names = new ArrayList<>(List.of("A", "B", "C"));
        System.out.println("Before: " + names);
        Swaps.swapFirstTwo(names);
        System.out.println("After: " + names);

        List<Integer> numbers = new ArrayList<>(List.of(10, 20));
        System.out.println("Before: " + numbers);
        Swaps.swapFirstTwo(numbers);
        System.out.println("After: " + numbers);
    }
}
