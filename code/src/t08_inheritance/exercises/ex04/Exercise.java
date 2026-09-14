package t08_inheritance.exercises.ex04;

import java.util.ArrayList;

public class Exercise {

    public static void run() {
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(new Sword("IronSword"));
        weapons.add(new Staff("OakStaff"));

        // Polymorphism: each Weapon variable calls the correct overridden
        // damage()/describe() implementation for its actual runtime type.
        for (Weapon w : weapons) {
            System.out.println(w.describe());
        }
    }
}
