package t12_generics_2.exercises.ex05;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static void run() {
        List<Integer> source = List.of(1, 2, 3);

        List<Number> numbers = new ArrayList<>(List.of(0.5));
        Copier.copy(source, numbers);
        System.out.println("Numbers: " + numbers);

        List<Object> objects = new ArrayList<>(List.of("start"));
        Copier.copy(source, objects);
        System.out.println("Objects: " + objects);
    }
}
