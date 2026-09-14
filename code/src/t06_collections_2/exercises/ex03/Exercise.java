package t06_collections_2.exercises.ex03;

import java.util.LinkedList;
import java.util.ListIterator;

public class Exercise {

    public static void run() {
        LinkedList<String> words = new LinkedList<>();
        words.add("Java");
        words.add("is");
        words.add("fun");

        ListIterator<String> it = words.listIterator();

        StringBuilder forward = new StringBuilder();
        while (it.hasNext()) {
            String w = it.next();
            forward.append(w);
            if (it.hasNext()) {
                forward.append(" ");
            }
        }
        System.out.println(forward.toString());

        StringBuilder backward = new StringBuilder();
        while (it.hasPrevious()) {
            String w = it.previous();
            backward.append(w);
            if (it.hasPrevious()) {
                backward.append(" ");
            }
        }
        System.out.println(backward.toString());
    }
}
