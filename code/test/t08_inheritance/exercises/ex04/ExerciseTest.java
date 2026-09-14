package t08_inheritance.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t08 ex04 - abstract Weapon and a polymorphic list")
class ExerciseTest {

    @Test
    void damage_differsPerConcreteWeapon() {
        assertEquals(25, new Sword("IronSword").damage());
        assertEquals(15, new Staff("OakStaff").damage());
    }

    @Test
    void describe_isInheritedButCallsTheSubclassDamage() {
        // describe() is defined once on Weapon, yet reports each subclass's damage.
        assertEquals("IronSword (damage=25)", new Sword("IronSword").describe());
        assertEquals("OakStaff (damage=15)", new Staff("OakStaff").describe());
    }

    @Test
    void describe_overAListOfWeapons_dispatchesPerRuntimeType() {
        List<Weapon> weapons = List.of(new Sword("IronSword"), new Staff("OakStaff"));
        assertEquals(
                List.of("IronSword (damage=25)", "OakStaff (damage=15)"),
                weapons.stream().map(Weapon::describe).toList());
    }

    @Test
    void weapon_isAbstractSoItCannotBeInstantiated() {
        assertTrue(Modifier.isAbstract(Weapon.class.getModifiers()),
                "Weapon must stay abstract - there is no such thing as a generic weapon");
    }
}
