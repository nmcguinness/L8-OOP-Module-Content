package t12_generics_2.exercises.ex06;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static void run() {
        List<Enemy> enemies = List.of(new Enemy("Orc"), new Enemy("Goblin"));
        List<Pickup> pickups = List.of(new Pickup("Coin"), new Pickup("Potion"));
        List<Entity> world = new ArrayList<>();

        Entities.addAllEntities(enemies, world);
        Entities.addAllEntities(pickups, world);

        System.out.println("World size: " + world.size());
        for (Entity entity : world) {
            System.out.println("  " + entity + " (" + entity.getClass().getSimpleName() + ")");
        }
    }
}
