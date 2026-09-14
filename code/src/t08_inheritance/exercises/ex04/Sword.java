package t08_inheritance.exercises.ex04;

public class Sword extends Weapon {
    public Sword(String id) {
        super(id);
    }

    @Override
    public int damage() {
        return 25;
    }
}
