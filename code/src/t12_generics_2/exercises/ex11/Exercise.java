package t12_generics_2.exercises.ex11;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static void run() {
        List<Number> out = new ArrayList<>();
        Merge.merge(List.of(1, 2), List.of(3), out);
        System.out.println("Merged: " + out);

        List<Number> partial = new ArrayList<>();
        Merge.merge(null, List.of(2.5), partial);
        System.out.println("With a null first list: " + partial);
    }
}
