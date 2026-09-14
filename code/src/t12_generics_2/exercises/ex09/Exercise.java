package t12_generics_2.exercises.ex09;

class Swaps {
    // Public entry point - accepts any List<?>
    public static void swapFirstTwo(java.util.List<?> items) {
        if (items == null || items.size() < 2)
            return;

        swapFirstTwoCaptured(items);
    }

    // Helper captures the wildcard so we can safely call set()
    // Without this helper, the compiler cannot prove get(0) and set(0,...) use the same type.
    private static <T> void swapFirstTwoCaptured(java.util.List<T> items) {
        T a = items.get(0);
        T b = items.get(1);

        items.set(0, b);
        items.set(1, a);
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<String> a = new java.util.ArrayList<>();
        a.add("A");
        a.add("B");

        Swaps.swapFirstTwo(a);
        System.out.println(a); // [B, A]

        java.util.ArrayList<Integer> b = new java.util.ArrayList<>();
        b.add(10);
        b.add(20);

        Swaps.swapFirstTwo(b);
        System.out.println(b); // [20, 10]
    }
}
