package t12_generics_2.exercises.ex05;

import java.util.List;

public final class Copier {

    private Copier() {
    }

    // src produces Ts (extends); dst consumes Ts (super). Copies in order, appending.
    public static <T> void copy(List<? extends T> src, List<? super T> dst) {
        if (src == null) {
            throw new NullPointerException("src must not be null");
        }
        if (dst == null) {
            throw new NullPointerException("dst must not be null");
        }
        for (T item : src) {
            dst.add(item);
        }
    }
}
