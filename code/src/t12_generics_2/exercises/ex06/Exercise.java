package t12_generics_2.exercises.ex06;

class Entity { }

class Enemy extends Entity { }

class Pickup extends Entity { }

class Entities {
    public static void addAllEntities(java.util.List<? extends Entity> src,
                                      java.util.List<? super Entity> dst) {

        if (src == null)
            throw new NullPointerException("src must not be null");

        if (dst == null)
            throw new NullPointerException("dst must not be null");

        for (Entity e : src)
            dst.add(e);
    }
}

public class Exercise {
    public static void run() {
        java.util.List<Enemy> enemies = java.util.List.of(new Enemy(), new Enemy());
        java.util.List<Pickup> pickups = java.util.List.of(new Pickup());

        java.util.ArrayList<Entity> all = new java.util.ArrayList<>();
        Entities.addAllEntities(enemies, all);
        Entities.addAllEntities(pickups, all);

        System.out.println(all.size()); // 3
        System.out.println(all.get(0).getClass().getSimpleName()); // Enemy
    }
}
