package t08_inheritance.exercises.ex05;

import java.util.ArrayList;

public class Exercise {

    public static void run() {
        ArrayList<GameEntity> entities = new ArrayList<>();
        entities.add(new PlayerEntity("Hero"));
        entities.add(new EnemyEntity("Slime"));

        double dt = 0.016; // 16 ms frame

        for (GameEntity ge : entities) {
            ge.update(dt);
            ge.render();
        }

        /*
         * This design makes it easy to add new entity types later (DoorEntity,
         * PickupEntity, etc.) because the main loop only depends on the
         * GameEntity base type. Any subclass that implements update/render
         * can be dropped into the same list with no changes to this loop.
         */
    }
}
