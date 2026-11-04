package t12_generics_2.exercises.ex11;

import java.util.List;

public final class Merge {

    private Merge() {
    }

    // Two producers, one consumer. A null input list counts as empty.
    public static <T> void merge(List<? extends T> a, List<? extends T> b, List<? super T> out) {
        if (out == null) {
            throw new NullPointerException("out must not be null");
        }
        addAll(a, out);
        addAll(b, out);
    }

    private static <T> void addAll(List<? extends T> src, List<? super T> out) {
        if (src == null) {
            return;
        }
        for (T item : src) {
            out.add(item);
        }
    }
}
