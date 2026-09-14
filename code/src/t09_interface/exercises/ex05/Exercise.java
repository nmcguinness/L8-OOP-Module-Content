package t09_interface.exercises.ex05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Exercise {

    public static void run() {
        List<Named> things = new ArrayList<>();
        things.add(new GameItem("Health Potion"));
        things.add(new GameItem("Sword"));
        things.add(new NPC("Guard"));
        things.add(new NPC("Merchant"));

        System.out.println("=== Sort by name ascending ===");
        Collections.sort(things, NamedComparators.byNameAscending());
        for (Named n : things) {
            System.out.println(n.getName());
        }

        System.out.println();
        System.out.println("=== Sort by name length ===");
        Collections.sort(things, NamedComparators.byNameLength());
        for (Named n : things) {
            System.out.println(n.getName());
        }
    }
}
