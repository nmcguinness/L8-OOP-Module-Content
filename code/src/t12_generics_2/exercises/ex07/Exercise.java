package t12_generics_2.exercises.ex07;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static void run() {
        List<Enemy> enemies = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        Spawner.addEnemies(enemies);
        Spawner.addEnemies(entities);
        Spawner.addEnemies(objects);

        System.out.println("List<Enemy>: " + enemies);
        System.out.println("List<Entity>: " + entities);
        System.out.println("List<Object>: " + objects);
    }
}
