package t12_generics_2.exercises.ex04;

class Fillers {
    public static <T> void fill(java.util.List<? super T> out, T value, int count) {
        if (out == null)
            throw new NullPointerException("out must not be null");

        if (count < 0)
            throw new IllegalArgumentException("count must be >= 0");

        for (int i = 0; i < count; i++)
            out.add(value);
    }
}

public class Exercise {
    public static void run() {
        java.util.ArrayList<Object> out = new java.util.ArrayList<>();
        Fillers.fill(out, "hi", 3);
        System.out.println(out); // [hi, hi, hi]

        java.util.ArrayList<Number> out2 = new java.util.ArrayList<>();
        Fillers.fill(out2, 5, 2);
        System.out.println(out2); // [5, 5]
    }
}
