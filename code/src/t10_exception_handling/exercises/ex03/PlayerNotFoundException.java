package t10_exception_handling.exercises.ex03;

/**
 * Checked: thrown by requireScore, whose callers claim the player must exist.
 */
public class PlayerNotFoundException extends Exception {

    private final String name;

    public PlayerNotFoundException(String name) {
        super("No player named '" + name + "'");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
