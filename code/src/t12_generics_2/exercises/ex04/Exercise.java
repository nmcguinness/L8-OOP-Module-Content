package t12_generics_2.exercises.ex04;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static void run() {
        List<Object> objects = new ArrayList<>();
        Fillers.fill(objects, "hi", 3);
        System.out.println("Objects: " + objects);

        List<Number> numbers = new ArrayList<>();
        Fillers.fill(numbers, 5, 2);
        System.out.println("Numbers: " + numbers);
    }
}
