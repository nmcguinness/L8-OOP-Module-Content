package ica2.api;

/**
 * A single, reversible change to the help desk, such as changing a
 * ticket's status or assigning it to an agent.
 *
 * <p>The help desk runs commands through {@link HelpDesk#execute(DeskCommand)},
 * which is what makes {@link HelpDesk#undo()} and {@link HelpDesk#redo()}
 * possible. You may add default, static and private methods to this
 * interface. You must not change or remove the methods declared here.</p>
 */
public interface DeskCommand {

    /**
     * Applies this change.
     *
     * <p>If the change is not valid (for example, an unknown ticket id or a
     * status change that the workflow does not allow), this method changes
     * nothing and returns {@code false}.</p>
     *
     * @param desk the help desk to apply the change to
     * @return {@code true} if the change was applied
     */
    boolean execute(HelpDesk desk);

    /**
     * Reverses this change. It is only called after a successful
     * {@link #execute(HelpDesk)}, and it restores exactly the state that
     * {@code execute} replaced.
     *
     * @param desk the help desk to reverse the change on
     */
    void undo(HelpDesk desk);

    /**
     * Describes this change in one line, for the demo output.
     *
     * @return a short description of this change
     */
    default String describe() {
        return getClass().getSimpleName();
    }
}
