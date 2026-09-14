package t08_inheritance.exercises.ex04;

public class Staff extends Weapon {
    public Staff(String id) {
        super(id);
    }

    @Override
    public int damage() {
        return 15;
    }
}
