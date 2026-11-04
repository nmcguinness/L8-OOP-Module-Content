package t09_interface.exercises.ex02;

public interface Interactable {

    void interact();

    String getDescription();

    // Default method: every implementer gets this range unless it overrides it
    default int interactionRange() {
        return 2;
    }

    // Default method built on another interface method; implementers rarely override it
    default boolean canInteract(int distanceToPlayer) {
        return distanceToPlayer >= 0 && distanceToPlayer <= interactionRange();
    }
}
