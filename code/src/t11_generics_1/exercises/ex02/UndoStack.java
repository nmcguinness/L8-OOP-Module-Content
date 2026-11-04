package t11_generics_1.exercises.ex02;

import java.util.ArrayList;
import java.util.List;

/**
 * A last-in, first-out stack. The top is the end of the list. Empty pops and peeks give null.
 */
public class UndoStack<T> {

    private final List<T> items = new ArrayList<>();

    public void push(T item) {
        items.add(item);
    }

    public T pop() {
        if (items.isEmpty()) {
            return null;
        }
        return items.remove(items.size() - 1);
    }

    public T peek() {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(items.size() - 1);
    }

    public int size() {
        return items.size();
    }
}
