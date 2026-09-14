package t12_generics_2.exercises.ex11;

// Optional challenge: merge two producers into one consumer

class Merge {
    public static <T> void merge(java.util.List<? extends T> a,
                                 java.util.List<? extends T> b,
                                 java.util.List<? super T> out) {

        if (out == null)
            throw new NullPointerException("out must not be null");

        if (a != null) {
            for (T x : a)
                out.add(x);
        }

        if (b != null) {
            for (T x : b)
                out.add(x);
        }
    }
}

public class Exercise {
    public static void run() {
        java.util.List<Integer> a = java.util.List.of(1, 2);
        java.util.List<Integer> b = java.util.List.of(3);

        java.util.ArrayList<Number> out = new java.util.ArrayList<>();
        Merge.merge(a, b, out);

        System.out.println(out); // [1, 2, 3]
    }
}
