package t09_interface.exercises.ex05;

public class NPC implements Named, IGreetPlayer {

    private final String name;

    public NPC(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void greet(String name) {
        System.out.println("Hi " + name + ", I'm " + this.name + "!");
    }
}
