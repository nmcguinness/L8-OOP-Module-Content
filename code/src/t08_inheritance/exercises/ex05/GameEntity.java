package t08_inheritance.exercises.ex05;

public abstract class GameEntity {
    protected final String name;

    public GameEntity(String name) {
        this.name = name;
    }

    public abstract void update(double dt);

    public abstract void render();
}
