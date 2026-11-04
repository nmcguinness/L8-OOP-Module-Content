package t11_generics_1.exercises.ex07;

import java.util.ArrayList;
import java.util.List;

/**
 * A first-in, first-out queue of messages of one type. An empty dequeue gives null.
 */
public class MessageBus<T> {

    private final List<T> queue = new ArrayList<>();

    public void enqueue(T message) {
        queue.add(message);
    }

    public T dequeue() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
