package t14_design_patterns_2.exercises.ex02;

public class Exercise {
    public static void run() {
        EnemyFactory factory = new EnemyFactory();

        Enemy easy = factory.createSlime(Difficulty.EASY);
        Enemy hard = factory.createSlime(Difficulty.HARD);

        System.out.println("EASY -> " + format(easy));
        System.out.println("HARD -> " + format(hard));
    }

    private static String format(Enemy e) {
        return e.name() + " (hp=" + e.hp() + ", dmg=" + e.damage() + ")";
    }
}

enum Difficulty {
    EASY,
    HARD
}

interface Enemy {
    String name();
    int hp();
    int damage();
}

class Slime implements Enemy {
    @Override public String name()   { return "Slime"; }
    @Override public int hp()        { return 20; }
    @Override public int damage()    { return 3; }
}

class ArmouredSlime implements Enemy {
    @Override public String name()   { return "Armoured Slime"; }
    @Override public int hp()        { return 45; }
    @Override public int damage()    { return 6; }
}

class EnemyFactory {
    public Enemy createSlime(Difficulty difficulty) {
        if (difficulty == null)
            throw new IllegalArgumentException("difficulty is null.");

        if (difficulty == Difficulty.EASY)
            return new Slime();

        return new ArmouredSlime();
    }
}
