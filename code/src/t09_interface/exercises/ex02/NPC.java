package t09_interface.exercises.ex02;

public class NPC implements Interactable {

    private final String name;

    public NPC(String name) {
        this.name = name;
    }

    @Override
    public void interact() {
        System.out.println(name + " says: \"Hello, traveller!\"");
    }

    @Override
    public String getDescription() {
        return "NPC " + name;
    }

    // NPCs can be talked to from further away than doors or chests
    @Override
    public int interactionRange() {
        return 4;
    }
}
