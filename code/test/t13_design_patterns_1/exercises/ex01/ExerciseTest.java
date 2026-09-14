package t13_design_patterns_1.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t13 ex01 - Strategy: swappable attack behaviour")
class ExerciseTest {

    @Test
    void meleeAttack_withinTwoMetres_addsFiveToBaseDamage() {
        assertEquals(15, new MeleeAttack().computeDamage(10, 2));
    }

    @Test
    void meleeAttack_beyondTwoMetres_dealsNothing() {
        assertEquals(0, new MeleeAttack().computeDamage(10, 3));
    }

    @Test
    void rangedAttack_withinTenMetres_dealsBaseDamage() {
        assertEquals(10, new RangedAttack().computeDamage(10, 10));
    }

    @Test
    void rangedAttack_beyondTenMetres_dealsNothing() {
        assertEquals(0, new RangedAttack().computeDamage(10, 11));
    }

    @Test
    void enemy_delegatesToWhicheverStrategyItWasGiven() {
        Enemy skeleton = new Enemy("Skeleton", 10, new MeleeAttack());
        Enemy archer = new Enemy("Archer", 10, new RangedAttack());

        // Same distance, same base damage, different result - that is the pattern.
        assertEquals(15, skeleton.attack(1));
        assertEquals(10, archer.attack(1));
    }

    @Test
    void enemy_atRangeWhereEachStrategyDiffers_showsTheSwapMatters() {
        Enemy skeleton = new Enemy("Skeleton", 10, new MeleeAttack());
        Enemy archer = new Enemy("Archer", 10, new RangedAttack());

        assertEquals(0, skeleton.attack(5), "melee cannot reach 5m");
        assertEquals(10, archer.attack(5), "ranged still can");
    }

    @Test
    void enemy_keepsItsName() {
        assertEquals("Skeleton", new Enemy("Skeleton", 10, new MeleeAttack()).getName());
    }
}
