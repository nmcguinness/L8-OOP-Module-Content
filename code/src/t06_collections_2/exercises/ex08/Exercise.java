package t06_collections_2.exercises.ex08;

import java.util.LinkedList;
import java.util.ListIterator;

public class Exercise {

    public static void mergeIntoA(LinkedList<Integer> a, LinkedList<Integer> b) {
        ListIterator<Integer> ai = a.listIterator();
        ListIterator<Integer> bi = b.listIterator();

        while (bi.hasNext()) {
            int bVal = bi.next();
            boolean inserted = false;

            while (ai.hasNext()) {
                int aVal = ai.next();
                if (aVal >= bVal) {
                    ai.previous();
                    ai.add(bVal);
                    inserted = true;
                    break;
                }
            }

            if (!inserted) {
                ai.add(bVal);
            }
        }
    }

    public static void run() {
        LinkedList<Integer> A = new LinkedList<>();
        A.add(1);
        A.add(4);
        A.add(7);

        LinkedList<Integer> B = new LinkedList<>();
        B.add(2);
        B.add(3);
        B.add(8);

        mergeIntoA(A, B);
        System.out.println(A);
    }
}
