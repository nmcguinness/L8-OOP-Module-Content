package t12_generics_2.exercises.ex08;

import java.util.Comparator;
import java.util.List;

public final class Sorters {

    private Sorters() {
    }

    // A comparator for T, or for any supertype of T, can put Ts in order
    public static <T> void sortWith(List<T> items, Comparator<? super T> cmp) {
        if (items == null) {
            throw new NullPointerException("items must not be null");
        }
        if (cmp == null) {
            throw new NullPointerException("cmp must not be null");
        }
        items.sort(cmp);
    }
}
