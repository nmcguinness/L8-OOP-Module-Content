package t12_generics_2.exercises.ex08;

class Entity { }

class Enemy extends Entity { }

class Sorters {
    public static <T> void sortWith(java.util.List<T> items, java.util.Comparator<? super T> cmp) {
        if (items == null)
            throw new NullPointerException("items must not be null");

        if (cmp == null)
            throw new NullPointerException("cmp must not be null");

        items.sort(cmp);
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<Enemy> enemies = new java.util.ArrayList<>();
        enemies.add(new Enemy());
        enemies.add(new Enemy());

        java.util.Comparator<Entity> cmp = (a, b) -> 0;
        Sorters.sortWith(enemies, cmp);

        System.out.println(enemies.size()); // 2
    }
}
