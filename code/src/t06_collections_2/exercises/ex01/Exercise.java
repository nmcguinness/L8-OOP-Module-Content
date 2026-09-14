package t06_collections_2.exercises.ex01;

import java.util.LinkedList;
import java.util.ListIterator;

public class Exercise {

    public static void run() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(-2);
        list.add(-1);
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) {
            int value = it.next();
            if (value <= 0) {
                it.remove();
            }
        }

        System.out.println(list); // [1, 2, 3, 4]
    }
}
