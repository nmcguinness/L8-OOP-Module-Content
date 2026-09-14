package t08_inheritance.exercises.ex04;

public abstract class Weapon {
    private final String id;

    public Weapon(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public abstract int damage();

    public String describe() {
        return id + " (damage=" + damage() + ")";
    }
}
