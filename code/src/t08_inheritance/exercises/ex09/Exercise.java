package t08_inheritance.exercises.ex09;

import java.util.HashSet;
import java.util.Set;

public class Exercise {

    public static void run() {
        Item sword = new Item("sword");
        Item enchanted = new EnchantedItem("sword", 3);

        System.out.println("sword.equals(enchanted): " + sword.equals(enchanted));
        System.out.println("enchanted.equals(sword): " + enchanted.equals(sword));

        Set<Item> bag = new HashSet<>();
        bag.add(sword);
        bag.add(enchanted);
        bag.add(new EnchantedItem("sword", 3));
        System.out.println("bag size: " + bag.size() + " " + bag);
    }
}
