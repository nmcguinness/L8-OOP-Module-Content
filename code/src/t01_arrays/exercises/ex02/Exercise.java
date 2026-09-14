package t01_arrays.exercises.ex02;

public class Exercise {

    static int indexOf(int[] xs, int target) {
        if (xs == null) return -1;
        for (int i = 0; i < xs.length; i++)
            if (xs[i] == target) return i;
        return -1;
    }

    static int count(int[] xs, int target) {
        if (xs == null) return 0;
        int c = 0;
        for (int x : xs) if (x == target) c++;
        return c;
    }

    public static void run() {
        System.out.println(indexOf(new int[]{3,7,7,2}, 7) == 1);
        System.out.println(count(new int[]{3,7,7,2}, 7) == 2);
        System.out.println(indexOf(new int[]{}, 9) == -1);
    }
}
