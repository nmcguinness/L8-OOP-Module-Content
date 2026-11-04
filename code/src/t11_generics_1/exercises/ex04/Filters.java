package t11_generics_1.exercises.ex04;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Filters {

    private Filters() {
    }

    // Returns a new list of the items that pass the test, in their original order
    public static <T> ArrayList<T> where(List<T> items, Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate must not be null");
        }
        ArrayList<T> out = new ArrayList<>();
        if (items == null) {
            return out;
        }
        for (T item : items) {
            if (predicate.test(item)) {
                out.add(item);
            }
        }
        return out;
    }
}
