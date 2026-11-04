package t11_generics_1.exercises.ex03;

import java.util.List;

public class Exercise {

    public static void run() {
        List<Integer> lengths = Maps.map(List.of("ant", "zebra"), word -> word.length());
        List<String> labels = Maps.map(List.of(10, 20), hp -> "HP:" + hp);

        System.out.println("Lengths: " + lengths);
        System.out.println("Labels: " + labels);
        System.out.println("From null: " + Maps.map(null, word -> word));
    }
}
