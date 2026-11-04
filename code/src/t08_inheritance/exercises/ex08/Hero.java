package t08_inheritance.exercises.ex08;

import t08_inheritance.exercises.ex04.Weapon;

/**
 * Composition: a Hero has a Weapon and delegates attacks to it.
 * The weapon can be swapped at runtime without a new Hero subclass.
 */
public class Hero {
    private final String name;
    private Weapon weapon;

    public Hero(String name, Weapon startingWeapon) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name is required");
        this.name = name;
        this.weapon = requireWeapon(startingWeapon);
    }

    public void equip(Weapon newWeapon) {
        weapon = requireWeapon(newWeapon);
    }

    public int attack() {
        return weapon.damage();
    }

    public Weapon weapon() {
        return weapon;
    }

    public String describe() {
        return name + " wielding " + weapon.describe();
    }

    private static Weapon requireWeapon(Weapon w) {
        if (w == null)
            throw new IllegalArgumentException("weapon is required");
        return w;
    }
}
