package t01_arrays.exercises.ex01;

public class Exercise {

    static int[] fillWith(int n, int value) {
        if (n < 0)
            throw new IllegalArgumentException("length < 0");
        int[] out = new int[n];
        for (int i = 0; i < n; i++)
            out[i] = value;
        return out;
    }

    static int sum(int[] xs) {
        if (xs == null)
            return 0;

        if(xs.length == 0)
            return 0;

        int s = 0;
        for (int x : xs) s += x;
        return s;
    }

    static double average(int[] xs) {
        if (xs == null || xs.length == 0)
            return 0.0;

        return (double) sum(xs) / xs.length; // cast before divide to avoid integer division
    }

    public static void run() {
        int[] a = fillWith(4, 5);       // {5,5,5,5}
        System.out.println(sum(a));     // 20
        System.out.println(average(a)); // 5.0
        System.out.println(average(new int[]{})); // 0.0
    }
}
