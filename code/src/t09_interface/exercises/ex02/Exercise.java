package t09_interface.exercises.ex02;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static void run() {
        List<Interactable> objects = new ArrayList<>();
        objects.add(new Door("Wooden door"));
        objects.add(new Chest("Treasure chest"));
        objects.add(new NPC("Shopkeeper"));

        int[] distancesToPlayer = {1, 3, 2};

        for (int i = 0; i < objects.size(); i++) {
            Interactable obj = objects.get(i);
            int distance = distancesToPlayer[i];

            System.out.println("Player is " + distance + " units from " + obj.getDescription());

            if (distance <= Interactable.DEFAULT_INTERACTION_RANGE) {
                obj.interact();
            } else {
                System.out.println("Too far away to interact.");
            }

            System.out.println();
        }
    }
}
