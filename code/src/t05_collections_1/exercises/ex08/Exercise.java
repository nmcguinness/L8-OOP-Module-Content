package t05_collections_1.exercises.ex08;

import java.util.ArrayList;

public class Exercise {

    public static ArrayList<Integer> mergeSorted(ArrayList<Integer> a, ArrayList<Integer> b) {
        ArrayList<Integer> left = (a == null) ? new ArrayList<Integer>() : a;
        ArrayList<Integer> right = (b == null) ? new ArrayList<Integer>() : b;

        ArrayList<Integer> merged = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            int va = left.get(i);
            int vb = right.get(j);
            if (va <= vb) {
                merged.add(va);
                i++;
            } else {
                merged.add(vb);
                j++;
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
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
        ArrayList<Integer> a = toList(new int[]{1, 3, 5});
        ArrayList<Integer> b = toList(new int[]{2, 4, 6});
        System.out.println(mergeSorted(a, b).toString().equals("[1, 2, 3, 4, 5, 6]"));
    }
}
