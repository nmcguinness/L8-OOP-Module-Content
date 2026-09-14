package t05_collections_1.exercises.ex02;

import java.util.ArrayList;

public class Exercise {

    public static ArrayList<Integer> toList(int[] xs) {
        ArrayList<Integer> out = new ArrayList<>();
        if (xs == null)
            return out;

        for (int x : xs) {
            out.add(x);
        }
        return out;
    }

    public static int[] toArray(ArrayList<Integer> list) {
        if (list == null)
            return new int[0];

        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static void run() {
        int[] a = { 1, 2, 3 };
        ArrayList<Integer> L = toList(a);
        int[] b = toArray(L);

        System.out.println(b.length == 3 && b[0] == 1 && b[2] == 3);

        System.out.println(toList(null).isEmpty());
        System.out.println(toArray(null).length == 0);
    }

}
