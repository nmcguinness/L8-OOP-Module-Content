package t08_inheritance.exercises.ex03;

public class BossEnemy extends Enemy {
    private final int level;

    public BossEnemy(String name, int level) {
        super(name);
        this.level = level;
    }

    @Override
    public String describe() {
        String base = super.describe();
        return base + " [Boss level " + level + "]";
    }
}
