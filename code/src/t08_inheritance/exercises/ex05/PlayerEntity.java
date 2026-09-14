package t08_inheritance.exercises.ex05;

public class PlayerEntity extends GameEntity {

    public PlayerEntity(String name) {
        super(name);
    }

    @Override
    public void update(double dt) {
        System.out.println("Updating player " + name + " with dt=" + dt);
    }

    @Override
    public void render() {
        System.out.println("Rendering player " + name);
    }
}
