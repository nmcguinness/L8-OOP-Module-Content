package t09_interface.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t09 ex03 - two interfaces and a LinkedList")
class ExerciseTest {

    @Test
    void moveAll_movesEachActorByItsOwnSpeed() {
        Player hero = new Player("Hero");
        Enemy slime = new Enemy("Slime");
        Cloud cloud = new Cloud();

        Exercise.moveAll(new LinkedList<>(List.of(hero, slime, cloud)));

        assertEquals(2, hero.getX());
        assertEquals(1, slime.getX());
        assertEquals(3, cloud.getX());
    }

    @Test
    void damageAll_skipsActorsThatAreNotDamageable() {
        Player hero = new Player("Hero");
        Enemy slime = new Enemy("Slime");

        int hit = Exercise.damageAll(new LinkedList<>(List.of(hero, slime, new Cloud())), 10);

        assertEquals(2, hit);
        assertEquals(90, hero.getHealth());
        assertEquals(20, slime.getHealth());
    }

    @Test
    void takeDamage_neverBelowZero() {
        Enemy slime = new Enemy("Slime");

        slime.takeDamage(500);

        assertEquals(0, slime.getHealth());
    }

    @Test
    void takeDamage_negativeAmount_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Player("Hero").takeDamage(-1));
    }
}
