package t05_collections_1.exercises.ex07;

import java.util.ArrayList;

public class Exercise {

    private static final class Partition {
        final ArrayList<Integer> evens;
        final ArrayList<Integer> odds;

        Partition(ArrayList<Integer> evens, ArrayList<Integer> odds) {
            this.evens = evens;
            this.odds = odds;
        }
    }

    private static Partition partition(ArrayList<Integer> xs) {
        ArrayList<Integer> evens = new ArrayList<>();
        ArrayList<Integer> odds = new ArrayList<>();

        if (xs != null) {
            for (int i = 0; i < xs.size(); i++) {
                Integer value = xs.get(i);
                if (value == null)
                    continue;

                if (value % 2 == 0) {
                    evens.add(value);
                } else {
                    odds.add(value);
                }
            }
        }
        return new Partition(evens, odds);
    }

    public static ArrayList<Integer> evens(ArrayList<Integer> xs) {
        return partition(xs).evens;
    }

    public static ArrayList<Integer> odds(ArrayList<Integer> xs) {
        return partition(xs).odds;
    }

    private static ArrayList<Integer> toList(int[] xs) {
        ArrayList<Integer> out = new ArrayList<>();
        if (xs != null) {
            for (int i = 0; i < xs.length; i++) {
                out.add(xs[i]);
            }
        }
        return out;
    }

    public static void run() {
        ArrayList<Integer> xs = toList(new int[]{1, 2, 3, 4, 5, 6});
        System.out.println(evens(xs).toString().equals("[2, 4, 6]"));
        System.out.println(odds(xs).toString().equals("[1, 3, 5]"));

        System.out.println(xs.toString().equals("[1, 2, 3, 4, 5, 6]"));
    }
}
