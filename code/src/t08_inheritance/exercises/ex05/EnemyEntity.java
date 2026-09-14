package t08_inheritance.exercises.ex05;

public class EnemyEntity extends GameEntity {

    public EnemyEntity(String name) {
        super(name);
    }

    @Override
    public void update(double dt) {
        System.out.println("Updating enemy " + name + " with dt=" + dt);
    }

    @Override
    public void render() {
        System.out.println("Rendering enemy " + name);
    }
}
