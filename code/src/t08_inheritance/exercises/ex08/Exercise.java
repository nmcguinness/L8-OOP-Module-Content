package t08_inheritance.exercises.ex08;

import t08_inheritance.exercises.ex04.Staff;
import t08_inheritance.exercises.ex04.Sword;

public class Exercise {

    public static void run() {
        Hero hero = new Hero("Aria", new Sword("IronBlade"));
        System.out.println(hero.describe() + " -> attack " + hero.attack());

        hero.equip(new Staff("OakStaff"));
        System.out.println(hero.describe() + " -> attack " + hero.attack());
    }
}
