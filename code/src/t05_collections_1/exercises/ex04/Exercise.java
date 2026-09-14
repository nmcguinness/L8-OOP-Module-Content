package t05_collections_1.exercises.ex04;

import java.util.ArrayList;

public class Exercise {

    public static ArrayList<String> uniqueOrderPreserving(ArrayList<String> input) {
        ArrayList<String> out = new ArrayList<>();
        if (input == null)
            return out;

        for (int i = 0; i < input.size(); i++) {
            String value = input.get(i);
            if (!out.contains(value)) {
                out.add(value);
            }
        }
        return out;
    }

    public static void run() {
        ArrayList<String> in = new ArrayList<>();
        in.add("a");
        in.add("b");
        in.add("a");
        in.add("c");
        in.add("b");

        ArrayList<String> unique = uniqueOrderPreserving(in);
        System.out.println(unique.toString().equals("[a, b, c]"));
    }
}
