package t14_design_patterns_2.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t14 e02 - Factory chosen by difficulty")
class ExerciseTest {

    @Test
    void createSlime_easy_returnsAPlainSlime() {
        Enemy e = new EnemyFactory().createSlime(Difficulty.EASY);
        assertInstanceOf(Slime.class, e);
        assertEquals("Slime", e.name());
        assertEquals(20, e.hp());
        assertEquals(3, e.damage());
    }

    @Test
    void createSlime_hard_returnsAnArmouredSlime() {
        Enemy e = new EnemyFactory().createSlime(Difficulty.HARD);
        assertInstanceOf(ArmouredSlime.class, e);
        assertEquals("Armoured Slime", e.name());
        assertEquals(45, e.hp());
        assertEquals(6, e.damage());
    }

    @Test
    void createSlime_hardEnemy_isStrongerThanEasy() {
        Enemy easy = new EnemyFactory().createSlime(Difficulty.EASY);
        Enemy hard = new EnemyFactory().createSlime(Difficulty.HARD);

        assertTrue(hard.hp() > easy.hp());
        assertTrue(hard.damage() > easy.damage());
    }

    @Test
    void createSlime_null_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new EnemyFactory().createSlime(null));
    }

    @Test
    void createSlime_returnsANewInstanceEachTime() {
        EnemyFactory factory = new EnemyFactory();
        assertTrue(factory.createSlime(Difficulty.EASY) != factory.createSlime(Difficulty.EASY));
    }

    @Test
    void callerSeesOnlyTheEnemyInterface() {
        // Difficulty decides the class; the calling code never names it.
        Enemy e = new EnemyFactory().createSlime(Difficulty.HARD);
        assertTrue(e.hp() > 0);
    }
}
