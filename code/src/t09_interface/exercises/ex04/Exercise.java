package t09_interface.exercises.ex04;

import java.util.List;
import java.util.Random;

public class Exercise {

    // Picks one strategy at random; pass a seeded Random to get repeatable results
    public static AttackStrategy pickRandom(List<AttackStrategy> options, Random random) {
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("options must not be empty");
        }
        return options.get(random.nextInt(options.size()));
    }

    public static void run() {
        Enemy enemy = new Enemy(new MeleeAttack());
        System.out.println(enemy.performAttack("Player"));

        enemy.setStrategy(new RangedAttack());
        System.out.println(enemy.performAttack("Player"));

        // Extension: a strategy chosen at random each turn
        List<AttackStrategy> options = List.of(new MeleeAttack(), new RangedAttack());
        Random random = new Random(42);
        for (int turn = 1; turn <= 3; turn++) {
            enemy.setStrategy(pickRandom(options, random));
            System.out.println("Turn " + turn + ": " + enemy.performAttack("Player"));
        }
    }
}
