package t12_generics_2.exercises.ex04;

import java.util.List;

public final class Fillers {

    private Fillers() {
    }

    // The list consumes Ts: a List<T>, or a list of any supertype of T, can accept them
    public static <T> void fill(List<? super T> out, T value, int count) {
        if (out == null) {
            throw new NullPointerException("out must not be null");
        }
        if (count < 0) {
            throw new IllegalArgumentException("count must not be negative");
        }
        for (int i = 0; i < count; i++) {
            out.add(value);
        }
    }
}
