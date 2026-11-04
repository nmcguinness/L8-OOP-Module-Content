package t11_generics_1.exercises.ex05;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the largest {@code capacity} values seen so far, largest first.
 */
public class TopN<T extends Comparable<T>> {

    private final int capacity;
    private final List<T> values = new ArrayList<>();

    public TopN(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1");
        }
        this.capacity = capacity;
    }

    public void add(T item) {
        if (item == null) {
            throw new NullPointerException("item must not be null");
        }
        // Walk past every value that is bigger than or equal to the new one
        int index = 0;
        while (index < values.size() && item.compareTo(values.get(index)) <= 0) {
            index++;
        }
        values.add(index, item);
        if (values.size() > capacity) {
            values.remove(values.size() - 1);
        }
    }

    // A copy, so callers cannot change the list inside this object
    public ArrayList<T> valuesDescending() {
        return new ArrayList<>(values);
    }
}
