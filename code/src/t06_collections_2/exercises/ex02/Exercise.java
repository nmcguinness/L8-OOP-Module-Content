package t06_collections_2.exercises.ex02;

import java.util.LinkedList;
import java.util.ListIterator;

public class Exercise {

    public static void run() {
        LinkedList<Character> chars = new LinkedList<>();
        chars.add('a');
        chars.add('a');
        chars.add('a');
        chars.add('b');
        chars.add('b');
        chars.add('c');
        chars.add('c');
        chars.add('c');

        ListIterator<Character> it = chars.listIterator();
        if (!chars.isEmpty()) {
            char prev = it.next();

            while (it.hasNext()) {
                char current = it.next();
                if (current != prev) {
                    it.previous();
                    it.add('|');
                    it.next();
                }
                prev = current;
            }
        }

        System.out.println(chars);
    }
}
