package t09_interface.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t09 ex04 - a swappable attack strategy")
class ExerciseTest {

    @Test
    void performAttack_usesCurrentStrategy() {
        Enemy enemy = new Enemy(new MeleeAttack());

        assertEquals("Swings sword at Player!", enemy.performAttack("Player"));
    }

    @Test
    void setStrategy_changesBehaviourAtRuntime() {
        Enemy enemy = new Enemy(new MeleeAttack());

        enemy.setStrategy(new RangedAttack());

        assertEquals("Shoots arrow at Player!", enemy.performAttack("Player"));
    }

    @Test
    void nullStrategy_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Enemy(null));
    }

    @Test
    void lambda_canBeAStrategy() {
        Enemy enemy = new Enemy(target -> "Casts a spell at " + target + "!");

        assertEquals("Casts a spell at Player!", enemy.performAttack("Player"));
    }

    @Test
    void pickRandom_alwaysReturnsOneOfTheOptions() {
        List<AttackStrategy> options = List.of(new MeleeAttack(), new RangedAttack());
        Random random = new Random(1);

        for (int i = 0; i < 20; i++) {
            assertTrue(options.contains(Exercise.pickRandom(options, random)));
        }
    }
}
