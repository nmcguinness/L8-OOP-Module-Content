package t12_generics_2.exercises.ex02;

import java.util.List;

public class Exercise {

    public static void run() {
        System.out.println("Strings:");
        Lists.printAll(List.of("sword", "shield", "bow"));
        System.out.println("Integers:");
        Lists.printAll(List.of(10, 20, 30));
        System.out.println("Entities:");
        List<Entity> entities = List.of(new Enemy("Orc"), new Pickup("Coin"));
        Lists.printAll(entities);

        // List<?> unknown = entities;
        // unknown.add(new Enemy("Troll"));  <- does not compile: the element type is unknown
    }
}
