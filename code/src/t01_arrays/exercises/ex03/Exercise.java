package t01_arrays.exercises.ex03;

public class Exercise {

    static int min(int[] xs) {
        if (xs == null || xs.length == 0)
            throw new IllegalArgumentException("null/empty");
        int m = xs[0];
        for (int i = 1; i < xs.length; i++)
            if (xs[i] < m)
                m = xs[i];
        return m;
    }

    static int max(int[] xs) {
        if (xs == null || xs.length == 0)
            throw new IllegalArgumentException("null/empty");
        int m = xs[0];
        for (int i = 1; i < xs.length; i++)
            if (xs[i] > m)
                m = xs[i];
        return m;
    }

    public static void run() {
        System.out.println(min(new int[]{5,2,8,1}) == 1);
        System.out.println(max(new int[]{5,2,8,1}) == 8);
    }
}
