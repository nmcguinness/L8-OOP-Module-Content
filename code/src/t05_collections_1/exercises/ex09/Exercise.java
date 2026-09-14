package t05_collections_1.exercises.ex09;

import java.util.ArrayList;

public class Exercise {

    public static ArrayList<ArrayList<String>> groupByFirstLetter(ArrayList<String> names) {
        ArrayList<ArrayList<String>> buckets = new ArrayList<ArrayList<String>>(26);
        for (int i = 0; i < 26; i++) {
            buckets.add(new ArrayList<String>());
        }

        if (names == null)
            return buckets;

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (name == null || name.isEmpty())
                continue;

            char first = name.charAt(0);
            if (!Character.isLetter(first))
                continue;

            char upper = Character.toUpperCase(first);
            int index = upper - 'A';
            if (index >= 0 && index < 26) {
                buckets.get(index).add(name);
            }
        }

        return buckets;
    }

    public static void run() {
        ArrayList<String> names = new ArrayList<>();
        names.add("alice");
        names.add("bob");
        names.add("amy");

        ArrayList<ArrayList<String>> g = groupByFirstLetter(names);
        System.out.println(g.get(0).toString().equals("[alice, amy]")); // A
        System.out.println(g.get(1).toString().equals("[bob]"));        // B
    }
}
