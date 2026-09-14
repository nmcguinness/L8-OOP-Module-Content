package t05_collections_1.exercises.ex01;

import java.util.ArrayList;

public class Exercise {

    public static void run() {
        ArrayList<String> names = new ArrayList<>();

        names.add("Alice");
        names.add("Bob");
        names.add("Cara");

        names.add(0, "Zara");

        names.set(names.size() - 1, "Dee");

        String removedName = "Bob";
        names.remove(removedName);

        for (int i = 0; i < names.size(); i++) {
            System.out.println(i + ": " + names.get(i));
        }

        System.out.println(names.size() == 3);
        System.out.println(!names.contains(removedName));
    }
}
