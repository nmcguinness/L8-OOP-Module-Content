package t12_generics_2.exercises.ex07;

import java.util.List;

public final class Spawner {

    private Spawner() {
    }

    // Was List<Enemy>, which only accepted a List<Enemy>. The method only adds, so ? super Enemy
    public static void addEnemies(List<? super Enemy> out) {
        if (out == null) {
            throw new NullPointerException("out must not be null");
        }
        out.add(new Enemy("Orc"));
        out.add(new Enemy("Goblin"));
    }
}
