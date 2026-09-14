package t08_inheritance.exercises.ex03;

public class Entity {
    private final String name;

    public Entity(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public String describe() {
        return "Entity: " + name;
    }
}
