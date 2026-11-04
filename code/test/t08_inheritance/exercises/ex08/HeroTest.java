package t08_inheritance.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t08_inheritance.exercises.ex04.Staff;
import t08_inheritance.exercises.ex04.Sword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t08 ex08 - composition: a Hero has a Weapon")
class HeroTest {

    @Test
    void attack_delegatesToCurrentWeapon() {
        Hero hero = new Hero("Aria", new Sword("IronBlade"));

        assertEquals(25, hero.attack());
    }

    @Test
    void equip_newWeapon_changesBehaviourAtRuntime() {
        Hero hero = new Hero("Aria", new Sword("IronBlade"));
        Staff staff = new Staff("OakStaff");

        hero.equip(staff);

        assertSame(staff, hero.weapon());
        assertEquals(15, hero.attack());
    }

    @Test
    void describe_includesNameAndWeapon() {
        Hero hero = new Hero("Aria", new Sword("IronBlade"));

        assertEquals("Aria wielding IronBlade (damage=25)", hero.describe());
    }

    @Test
    void nullWeapon_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Hero("Aria", null));
        Hero hero = new Hero("Aria", new Sword("IronBlade"));
        assertThrows(IllegalArgumentException.class, () -> hero.equip(null));
    }
}
