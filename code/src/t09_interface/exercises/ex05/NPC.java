package t09_interface.exercises.ex05;

public class NPC implements Named {

    private final String name;

    public NPC(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
