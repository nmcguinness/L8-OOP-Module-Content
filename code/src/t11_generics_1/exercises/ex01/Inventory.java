package t11_generics_1.exercises.ex01;

import java.util.ArrayList;
import java.util.List;

/**
 * The generic version: the compiler knows what is inside, so no caller ever casts.
 */
public class Inventory<T> {

    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public T get(int index) {
        return items.get(index);
    }
}
