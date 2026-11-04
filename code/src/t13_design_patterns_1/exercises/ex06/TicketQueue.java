package t13_design_patterns_1.exercises.ex06;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Runs queued commands first in, first out. It never looks inside a command.
 */
public class TicketQueue {

    private final Queue<Command> queue = new ArrayDeque<>();

    public void add(Command command) {
        queue.add(command);
    }

    public int size() {
        return queue.size();
    }

    public void processAll() {
        while (!queue.isEmpty()) {
            queue.poll().execute();
        }
    }
}
