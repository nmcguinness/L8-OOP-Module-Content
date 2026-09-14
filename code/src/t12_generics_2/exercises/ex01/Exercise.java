package t12_generics_2.exercises.ex01;

class Entity { }

class Enemy extends Entity { }

class Pickup extends Entity { }

public class Exercise {
    public static void run() {
        Enemy e = new Enemy();
        Entity x = e; // normal inheritance works

        java.util.List<Enemy> enemies = new java.util.ArrayList<>();

        // java.util.List<Entity> entities = enemies;
        // Not allowed: List<Enemy> is not a List<Entity> (generics are invariant).
        // If allowed, someone could add a Pickup into a List<Enemy>.
        System.out.println(x);
        System.out.println(enemies.size());
    }
}
