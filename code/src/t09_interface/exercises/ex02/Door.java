package t09_interface.exercises.ex02;

public class Door implements Interactable {

    private final String description;

    public Door(String description) {
        this.description = description;
    }

    @Override
    public void interact() {
        System.out.println("You open the " + description + ".");
    }

    @Override
    public String getDescription() {
        return description;
    }
}
