package t12_generics_2.exercises.ex03;

import java.util.List;

public class Exercise {

    public static void run() {
        System.out.println("Integers: " + Numbers.sumNumbers(List.of(1, 2, 3)));
        System.out.println("Doubles: " + Numbers.sumNumbers(List.of(0.5, 1.5, 2.0)));
        System.out.println("Null: " + Numbers.sumNumbers(null));

        // List<? extends Number> numbers = new ArrayList<Integer>();
        // numbers.add(1);  <- does not compile: it might really be a List<Double>
    }
}
