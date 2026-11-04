package t12_generics_2.exercises.ex08;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Exercise {

    public static void run() {
        List<Enemy> enemies = new ArrayList<>(
                List.of(new Enemy("Zombie"), new Enemy("Orc"), new Enemy("Goblin")));
        Comparator<Entity> byName = Comparator.comparing(Entity::getName);

        System.out.println("Before: " + enemies);
        Sorters.sortWith(enemies, byName);
        System.out.println("After: " + enemies);
    }
}
