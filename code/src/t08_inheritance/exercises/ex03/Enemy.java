package t08_inheritance.exercises.ex03;

public class Enemy extends Entity {

    public Enemy(String name) {
        super(name);
    }

    @Override
    public String describe() {
        return "Enemy: " + name();
    }
}
