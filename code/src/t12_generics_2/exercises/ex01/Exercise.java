package t12_generics_2.exercises.ex01;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static void run() {
        Entity entity = new Enemy("Orc");
        System.out.println("An Enemy is an Entity: " + entity);

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy("Goblin"));

        // List<Entity> entities = enemies;  <- does not compile
        // If it did, entities.add(new Pickup("Coin")) would put a Pickup into a list of enemies,
        // and enemies.get(1) would then hand back a Pickup where an Enemy was promised.
        System.out.println("Enemies: " + enemies);
    }
}
