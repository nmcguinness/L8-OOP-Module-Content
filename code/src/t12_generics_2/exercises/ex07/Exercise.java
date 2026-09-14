package t12_generics_2.exercises.ex07;

class Entity { }

class Enemy extends Entity { }

class Spawner {
    public static void addEnemies(java.util.List<? super Enemy> out) {
        if (out == null)
            throw new NullPointerException("out must not be null");

        out.add(new Enemy());
        out.add(new Enemy());
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<Enemy> a = new java.util.ArrayList<>();
        java.util.ArrayList<Entity> b = new java.util.ArrayList<>();
        java.util.ArrayList<Object> c = new java.util.ArrayList<>();

        Spawner.addEnemies(a);
        Spawner.addEnemies(b);
        Spawner.addEnemies(c);

        System.out.println(a.size()); // 2
        System.out.println(b.size()); // 2
        System.out.println(c.size()); // 2
    }
}
