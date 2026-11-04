package t12_generics_2.exercises.ex09;

import java.util.List;

public final class Swaps {

    private Swaps() {
    }

    // Accepts a list of any type
    public static void swapFirstTwo(List<?> items) {
        if (items == null || items.size() < 2) {
            return;
        }
        swapFirstTwoCaptured(items);
    }

    // With List<?>, the compiler cannot prove that get(0) and set(1, ...) use the same type.
    // Calling this method gives the unknown type a name, T, so it can.
    private static <T> void swapFirstTwoCaptured(List<T> items) {
        T first = items.get(0);
        items.set(0, items.get(1));
        items.set(1, first);
    }
}
