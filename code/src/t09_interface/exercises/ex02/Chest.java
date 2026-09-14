package t09_interface.exercises.ex02;

public class Chest implements Interactable {

    private final String description;

    public Chest(String description) {
        this.description = description;
    }

    @Override
    public void interact() {
        System.out.println("You open the " + description + " and find some gold!");
    }

    @Override
    public String getDescription() {
        return description;
    }
}
