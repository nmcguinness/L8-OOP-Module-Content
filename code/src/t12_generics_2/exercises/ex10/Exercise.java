package t12_generics_2.exercises.ex10;

import java.util.ArrayList;
import java.util.List;

/*
 * Wildcard cheat sheet
 *
 * List<?> a
 *   Add:  null only. The element type is unknown, so the compiler rejects everything else.
 *   Read: as Object, the only type every element is certain to be.
 *
 * List<? extends Number> b
 *   Add:  null only. It might be a List<Integer> or a List<Double>, so nothing else is safe.
 *   Read: as Number.
 *
 * List<? super Integer> c
 *   Add:  Integer (and null). Whatever the list really is, it accepts Integers.
 *   Read: as Object only. It might really be a List<Number> or a List<Object>.
 */
public class Exercise {

    public static void run() {
        List<? extends Number> producer = List.of(1, 2.5);
        Number first = producer.get(0);
        System.out.println("Read as Number: " + first);

        List<? super Integer> consumer = new ArrayList<Number>();
        consumer.add(42);
        Object read = consumer.get(0);
        System.out.println("Read as Object: " + read);
    }
}
